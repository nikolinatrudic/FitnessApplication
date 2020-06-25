package com.example.fitnessapplication.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.fitnessapplication.Accelerometer;
import com.example.fitnessapplication.AccelerometerSingleton;
import com.example.fitnessapplication.R;
import com.example.fitnessapplication.WorkoutFactory;
import com.example.fitnessapplication.WorkoutInterface;
import com.example.fitnessapplication.database.FitnessDatabase;
import com.example.fitnessapplication.database.LoggedInUser;
import com.example.fitnessapplication.database.dao.SportDao;
import com.example.fitnessapplication.database.entities.Sport;
import com.example.fitnessapplication.database.entities.Workout;
import com.example.fitnessapplication.fragment.SportPage;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MapsFragment extends Fragment implements OnMapReadyCallback {
    public static final String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    private MapView mMapView;
    private GoogleMap map1;
    private LatLng currentLoc;
    private LatLng startLoc;
    private LatLng stopLoc;
    private PolylineOptions poptions;
    private Polyline polyline1;
    private List<LatLng> points;
    private boolean stop = false;

    private ImageView startImage;
    private ImageView stopImage;

    private FusedLocationProviderClient fusedLocationClient;
    private LocationListener locationListener;
    private LocationManager locationManager;

    private SensorManager sensorManager;
    private Sensor sensor;
    private SensorEventListener stepDetector;

    private Accelerometer accelerometer;
    private int currentStepsNumber;
    private int totalStepsNumber;

    private WorkoutFactory workoutFactory;

    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 99;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        mMapView = view.findViewById(R.id.mapView);

        workoutFactory = new WorkoutFactory();

        initGoogleMap(savedInstanceState);

        startImage = (ImageView) view.findViewById(R.id.buttonStart);

        stopImage = (ImageView) view.findViewById(R.id.buttonStop);

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        accelerometer = AccelerometerSingleton.getInstance().getAccelerometer();
        Log.e("msg", getArguments().getString("sportName"));

        return view;
    }


    private void initGoogleMap(Bundle savedInstanceState) {
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        mMapView.onCreate(mapViewBundle);

        mMapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        map1 = map;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_FINE_LOCATION);
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            currentLoc = new LatLng(location.getLatitude(), location.getLongitude());
                            map1.addMarker(new MarkerOptions()
                                    .position(currentLoc)
                                    .title("My location"));
                            map1.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 16.0f));
                        } else {
                            Toast.makeText(getContext(), "Location not found", Toast.LENGTH_SHORT);
                        }
                    }
                });

        poptions = new PolylineOptions();

        polyline1 = map1.addPolyline(poptions);
        points = new ArrayList<>();

        startImage.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                currentStepsNumber = accelerometer.getStepsNumber();
                stepDetector = new SensorEventListener() {
                    @Override
                    public void onSensorChanged(SensorEvent sensorEvent) {
                        if (sensorEvent != null) {
                            float x_acceleration = sensorEvent.values[0];
                            float y_acceleration = sensorEvent.values[1];
                            float z_acceleration = sensorEvent.values[2];

                            accelerometer.calculateSteps(x_acceleration, y_acceleration, z_acceleration);
                        }
                    }

                    @Override
                    public void onAccuracyChanged(Sensor sensor, int accuracy) {

                    }

                };

                sensorManager.registerListener(stepDetector, sensor, SensorManager.SENSOR_DELAY_NORMAL, 10000);

                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            MY_PERMISSIONS_REQUEST_FINE_LOCATION);
                }

                int minTime = 10000;
                float minDistance = (float) 3;
                Criteria criteria = new Criteria();
                criteria.setPowerRequirement(Criteria.POWER_LOW);
                criteria.setAccuracy(Criteria.ACCURACY_FINE);
                String bestProvider = locationManager.getBestProvider(criteria, false);

                locationListener = new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        points.add(new LatLng(location.getLatitude(), location.getLongitude()));
                        polyline1.setPoints(points);
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {

                    }
                };
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                locationManager.requestLocationUpdates(bestProvider, minTime, minDistance, locationListener);
            }
        });


        stopImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                totalStepsNumber = accelerometer.getStepsNumber();
                if (totalStepsNumber - currentStepsNumber >= 20) {
                    locationManager.removeUpdates(locationListener);
                    sensorManager.unregisterListener(stepDetector);

                    String sportName = getArguments().getString("sportName");
                    SportDao sportDao = FitnessDatabase.getInstance(getContext()).sportDao();
                    Sport sport = sportDao.findSport(sportName);
                    Log.e("msg", sportName);
                    WorkoutInterface workout = workoutFactory.getWorkout(sportName, totalStepsNumber - currentStepsNumber, sport.getCaloriesPerKm());
                    float km = workout.calculateKm();
                    float calories = workout.countCalories();

                    Bundle bundle = new Bundle();
                    bundle.putString("km", km + "");
                    bundle.putString("calories", calories + "");
                    bundle.putString("averageSpeed", workout.calculateSpeed() + "");

                    SportPage sportPage = new SportPage();
                    Log.d("Sport name", sport.getName());
                    sportPage.setSport(sport);
                    sportPage.setArguments(bundle);

                    addWorkoutInDatabase(km, calories, LoggedInUser.getInstance().getUser().getId(), sport.getSportId());

                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, sportPage);
                    fragmentTransaction.commit();
                } else {
                    FragmentManager manager = getActivity().getSupportFragmentManager();
                    FragmentTransaction trans = manager.beginTransaction();
                    trans.remove(MapsFragment.this);
                    trans.commit();
                    manager.popBackStack();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        mMapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    private void locationStop() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_FINE_LOCATION);
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            stopLoc = new LatLng(location.getLatitude(), location.getLongitude());
                            map1.addMarker(new MarkerOptions()
                                    .position(stopLoc)
                                    .title("Stop location"));
                            map1.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 16.0f));
                        } else {
                            Toast.makeText(getContext(), "Location not found", Toast.LENGTH_SHORT);
                        }
                    }
                });
    }

    private void addWorkoutInDatabase(float km, float calories, int userId, int sportId) {
        Workout workout = new Workout();
        workout.setKm(km);
        workout.setUserId(userId);
        workout.setSportId(sportId);
        workout.setCalories(calories);

        FitnessDatabase.getInstance(getContext()).workoutDao().insertWorkout(workout);
        Log.e("msg","uspesno upisao");
    }

}