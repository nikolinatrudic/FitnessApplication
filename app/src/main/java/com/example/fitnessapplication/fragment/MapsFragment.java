package com.example.fitnessapplication.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

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

import com.example.fitnessapplication.R;
import com.example.fitnessapplication.WorkoutFactory;
import com.example.fitnessapplication.WorkoutInterface;
import com.example.fitnessapplication.database.FitnessDatabase;
import com.example.fitnessapplication.database.dao.SportDao;
import com.example.fitnessapplication.database.entities.Sport;
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
import com.google.maps.android.SphericalUtil;

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

    private WorkoutFactory workoutFactory;

    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 99;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        mMapView = view.findViewById(R.id.mapView);

        initGoogleMap(savedInstanceState);

        startImage = (ImageView) view.findViewById(R.id.buttonStart);

        stopImage = (ImageView) view.findViewById(R.id.buttonStop);

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

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
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            MY_PERMISSIONS_REQUEST_FINE_LOCATION);
                }

                /*fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                // Got last known location. In some rare situations this can be null.
                                if (location != null) {
                                    startLoc = new LatLng(location.getLatitude(), location.getLongitude());
                                    map1.addMarker(new MarkerOptions()
                                            .position(startLoc)
                                            .title("Start location"));
                                    map1.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 16.0f));
                                } else {
                                    Toast.makeText(getContext(), "Location not found", Toast.LENGTH_SHORT);
                                }
                            }
                        });*/

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
                if(points.size() >= 2) {
                    locationManager.removeUpdates(locationListener);
                    //locationStop();
                    int listSize = points.size();
                    //float[] resultArray = new float[5];
                    Log.e("msg", "trenutno: lat:" + currentLoc.latitude + " log: " + currentLoc.longitude);
                    Log.e("msg", "size: " + points.size());
                    //Location.distanceBetween(startLoc.latitude, startLoc.longitude, stopLoc.latitude, stopLoc.longitude, resultArray);
                    double promenljiva = SphericalUtil.computeArea(points);

                    String sportName = getArguments().getString("sportName");
                    WorkoutInterface workout = workoutFactory.getWorkout(sportName, (float) promenljiva);
                    Bundle bundle = new Bundle();
                    bundle.putString("km", promenljiva + "");
                    bundle.putString("calories", workout.countCalories() + "");
                    bundle.putString("averageSpeed", workout.calculateSpeed() + "");

                    SportPage sportPage = new SportPage();
                    SportDao sportDao = FitnessDatabase.getInstance(getContext()).sportDao();
                    Sport sport = sportDao.findSport(sportName);
                    Log.d("Sport name", sport.getName());
                    sportPage.setSport(sport);

                    sportPage.setArguments(bundle);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, sportPage);
                    fragmentTransaction.commit();
                } else{
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

    private void locationStop(){
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
}