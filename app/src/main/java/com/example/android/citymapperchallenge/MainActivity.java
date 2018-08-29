package com.example.android.citymapperchallenge;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.citymapperchallenge.adapters.StationArrivalsAdapter;
import com.example.android.citymapperchallenge.constants.Const;
import com.example.android.citymapperchallenge.model.ArrivalLineTime;
import com.example.android.citymapperchallenge.model.ArrivalsEndPoint.NextArrivals;
import com.example.android.citymapperchallenge.model.NearbyEndPoint.StationsWithinRadius;
import com.example.android.citymapperchallenge.model.NearbyEndPoint.StopPoint;
import com.example.android.citymapperchallenge.model.StationArrivals;
import com.example.android.citymapperchallenge.retrofit.App;
import com.example.android.citymapperchallenge.retrofit.InternetConnectionListener;
import com.example.android.citymapperchallenge.retrofit.TfLUnifyService;
import com.example.android.citymapperchallenge.utils.CustomComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements InternetConnectionListener,
        StationArrivalsAdapter.DetailsAdapterListener, LocationListener {

    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private final int PERMISSION_REQUEST_FINE_COARSE_LOCATION = 5;
    @BindView(R.id.no_internet_tv)
    TextView mNoInternetTv;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.toolbar_main)
    Toolbar toolbar;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<StopPoint> listStops;
    private ArrayList<StationArrivals> mStationArrivalsList = new ArrayList<>();
    private StationArrivalsAdapter mAdapter;
    private TfLUnifyService apiService;
    private LocationManager locationManager;
    private String provider;

    //TODO: remove logic code off this activity
    //TODO: remove commented out code

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.tube_stations_nearby));

        //set up recyclerView
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new StationArrivalsAdapter(this, mStationArrivalsList, this);
        mRecyclerView.setAdapter(mAdapter);

        apiService = ((App) getApplication()).getService();
        ((App) getApplication()).setInternetConnectionListener(this);

        //get current location
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            //permissions not granted, so request permissions
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_FINE_COARSE_LOCATION);
        } else {
            //permissions granted, get location
            getLocation(locationManager, provider);
        }

        loadDataRxJava();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getLocation(locationManager, provider);
        loadDataRxJava();
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    //method to load nearby stations and arrivals from TfL Unify Api
    private void loadDataRxJava() {
        listStops = new ArrayList<>();
        mStationArrivalsList.clear();
        //TODO: change to Disposable Observable?
        //create first observable to return list of nearby stopPoints
        Observable<StationsWithinRadius> observable = apiService.getNearbyStations();
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<StationsWithinRadius>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.v(LOG_TAG, "Subscribed to nearby stations observable");
                    }

                    @Override
                    public void onNext(StationsWithinRadius stationsWithinRadius) {
                        //TODO: is this the right place to put this?
                        mRecyclerView.setVisibility(View.VISIBLE);
                        mNoInternetTv.setVisibility(View.GONE);
                        listStops = (ArrayList<StopPoint>)
                                stationsWithinRadius.getStopPoints();
                        Log.v(LOG_TAG, "list of stops found: " + listStops);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.e(LOG_TAG, "Nearby stations observer error");
                    }

                    @Override
                    public void onComplete() {
                        //iterate through the stop points
                        for (int i = 0; i < listStops.size(); i++) {
                            StopPoint stopPoint = listStops.get(i);
                            //create objects from information available from stop points
                            final StationArrivals foundDetails = new StationArrivals(stopPoint.getCommonName(),
                                    stopPoint.getNaptanId(), stopPoint.getDistance(), null);
                            mStationArrivalsList.add(foundDetails);

                            //query the arrivals endpoint, for each naptanId, to get arrivals
                            Observable<List<NextArrivals>> arrivalsObservable =
                                    apiService.getNextArrivals(stopPoint.getNaptanId());
                            arrivalsObservable
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(new Observer<List<NextArrivals>>() {
                                        @Override
                                        public void onSubscribe(Disposable d) {
                                            Log.v(LOG_TAG, "Arrivals observable subscribed");
                                        }

                                        @Override
                                        public void onNext(List<NextArrivals> nextTenTrains) {
                                            Collections.sort(nextTenTrains, new CustomComparator());
                                            ArrayList<ArrivalLineTime> nextThreeArrivals = new ArrayList<>();
                                            //find first three arrivals
                                            for (int j = 0; j < 3; j++) {
                                                NextArrivals foundTrain = nextTenTrains.get(j);
                                                ArrivalLineTime arrival = new ArrivalLineTime
                                                        (foundTrain.getLineId(), foundTrain.getLineName(),
                                                                foundTrain.getTimeToStation());
                                                nextThreeArrivals.add(arrival);
                                                //complete the StationArrivals objects
                                                foundDetails.setArrivals(nextThreeArrivals);
                                                Log.d(LOG_TAG, "arrival added" + arrival);
                                            }
                                            //notify adapter of changes to data
                                            mAdapter.notifyDataSetChanged();
                                        }

                                        @Override
                                        public void onError(Throwable e) {
                                            e.printStackTrace();
                                            Log.e(LOG_TAG, "Arrivals observable error");

                                        }

                                        @Override
                                        public void onComplete() {
                                            Log.v(LOG_TAG, "Arrivals observable completed");

                                        }
                                    });
                        }
                        Log.v(LOG_TAG, "Nearby stations observer complete");
                    }
                });
    }

    @Override
    public void onInternetUnavailable() {
        mRecyclerView.setVisibility(View.GONE);
        mNoInternetTv.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onFirstArrivalClick(View v, int position) {
        openLineActivity(position, 0);
    }

    @Override
    public void onSecondArrivalClick(View v, int position) {
        openLineActivity(position, 1);

    }

    @Override
    public void onThirdArrivalClick(View v, int position) {
        openLineActivity(position, 2);

    }

    public void openLineActivity(int position, int index) {
        StationArrivals selectedStArr = mStationArrivalsList.get(position);
        double distanceFromStation = selectedStArr.getDistance();
        String naptanId = selectedStArr.getNaptanId();
        ArrivalLineTime selectedArrival = selectedStArr.getArrivals().get(index);
        Intent openLineActivity = new Intent(this, LineActivity.class);
        openLineActivity.putExtra(Const.DISTANCE_TO_STATION, distanceFromStation);
        openLineActivity.putExtra(Const.SELECTED_ARRIVAL, selectedArrival);
        openLineActivity.putExtra(Const.NAPTAN_ID, naptanId);
        startActivity(openLineActivity);
    }

    // get current location methods
    @Override
    public void onLocationChanged(Location location) {
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        Log.v(LOG_TAG, "Latitude found:" + String.valueOf(lat));
        Log.v(LOG_TAG, "Longitude found:" + String.valueOf(lng));

        //TODO: check location is within London
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {
        Toast.makeText(this, "Enabled new provider " + provider,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String s) {
        Toast.makeText(this, "Disabled provider " + provider,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_FINE_COARSE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                   getLocation(locationManager, provider);
                } else {
                    // permission denied
                    Toast.makeText(this, "Default location used",
                            Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    public void getLocation(LocationManager lm, String provider){
        try {
            Location location = lm.getLastKnownLocation(provider);
            if (location != null) {
                System.out.println("Provider " + provider + " has been selected.");
                onLocationChanged(location);
            } else {
                Log.v(LOG_TAG, "Latitude Location not available");
                Log.v(LOG_TAG, "Longitude Location not available");
            }
        } catch (SecurityException e){
            Log.e(LOG_TAG, "Location permissions error: " + e);
        }
    }
}
