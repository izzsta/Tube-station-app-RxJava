package com.example.android.citymapperchallenge;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.citymapperchallenge.adapters.StationArrivalsAdapter;
import com.example.android.citymapperchallenge.constants.Const;
import com.example.android.citymapperchallenge.model.ArrivalLineTime;
import com.example.android.citymapperchallenge.model.ArrivalsEndPoint.NextArrivals;
import com.example.android.citymapperchallenge.model.NearbyEndPoint.StationsWithinRadius;
import com.example.android.citymapperchallenge.model.NearbyEndPoint.StopPoint;
import com.example.android.citymapperchallenge.model.StationArrivals;
import com.example.android.citymapperchallenge.retrofit.RetrofitHelper;
import com.example.android.citymapperchallenge.retrofit.TfLUnifyService;
import com.example.android.citymapperchallenge.utils.CustomComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements
        StationArrivalsAdapter.DetailsAdapterListener, LocationListener {

    private final String LOG_TAG = MainActivity.class.getSimpleName();
    private final int PERMISSION_REQUEST_FINE_COARSE_LOCATION = 5;
    @BindView(R.id.no_internet_tv)
    TextView mNoInternetTv;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.toolbar_main)
    Toolbar toolbar;
    @BindView(R.id.progress_bar_main)
    ProgressBar progressBar;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<StopPoint> listStops = new ArrayList<>();
    private ArrayList<StationArrivals> mStationArrivalsList = new ArrayList<>();
    private StationArrivalsAdapter mAdapter;
    private TfLUnifyService apiService;
    private LocationManager locationManager;
    private String provider;
    private Double mLat = 51.5025;
    private Double mLon = -0.1348;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if(ab!=null) {
            ab.setTitle(getResources().getString(R.string.tube_stations_nearby));
        }

        //set up recyclerView
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new StationArrivalsAdapter(this, mStationArrivalsList, this);
        mRecyclerView.setAdapter(mAdapter);

        //get current location
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            //permissions not granted, so request permissions
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_REQUEST_FINE_COARSE_LOCATION);
        } else {
            //permissions granted
            provider = locationManager.getBestProvider(new Criteria(), false);
            getLocation(locationManager, provider);
            //if connected, proceed with API queries
            if (isInternetAvailable()) {
                mRecyclerView.setVisibility(View.VISIBLE);
                mNoInternetTv.setVisibility(View.GONE);
                apiService = new RetrofitHelper().getService();
                loadDataRxJava(createQueryMap(mLat, mLon));
            } else {
                mRecyclerView.setVisibility(View.GONE);
                mNoInternetTv.setVisibility(View.VISIBLE);
            }
        }
    }

    //method to load nearby stations and arrivals from TfL Unify Api
    private void loadDataRxJava(Map<String, String> queryMap) {
        listStops.clear();
        mStationArrivalsList.clear();
        //create first observable to return list of nearby stopPoints
        Observable<StationsWithinRadius> observable = apiService.getNearbyStations(queryMap);
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<StationsWithinRadius>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        progressBar.setVisibility(View.VISIBLE);
                        Log.v(LOG_TAG, "Subscribed to nearby stations observable");
                    }

                    @Override
                    public void onNext(StationsWithinRadius stationsWithinRadius) {
                        //get list of nearby stop points from endpoint
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

                            //create objects from information already available
                            final StationArrivals foundDetails = new StationArrivals(stopPoint.getCommonName(),
                                    stopPoint.getNaptanId(), stopPoint.getDistance(), null);
                            mStationArrivalsList.add(foundDetails);

                            //for each naptanID, query the arrivals endpoint to get list of arrivals
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
                                            //if at least one arrival is found, proceed

                                            Collections.sort(nextTenTrains, new CustomComparator());
                                            ArrayList<ArrivalLineTime> nextThreeArrivals = new ArrayList<>();
                                            int arrivalsNumber = Math.min(nextTenTrains.size(), 3);

                                            //find first three arrivals
                                            for (int j = 0; j < arrivalsNumber; j++) {
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
                                            progressBar.setVisibility(View.GONE);
                                            Log.v(LOG_TAG, "Arrivals observable completed");
                                        }
                                    });
                        }
                        Log.v(LOG_TAG, "Nearby stations observer complete");
                    }
                });
    }

    //onClick methods for adapter
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

    //pass selected arrival info to and open LineActivity
    public void openLineActivity(int position, int index) {
        StationArrivals selectedStArr = mStationArrivalsList.get(position);
        Intent openLineActivity = new Intent(this, LineActivity.class);
        openLineActivity.putExtra(Const.DISTANCE_TO_STATION, selectedStArr.getDistance());
        openLineActivity.putExtra(Const.SELECTED_ARRIVAL, selectedStArr.getArrivals().get(index));
        openLineActivity.putExtra(Const.NAPTAN_ID, selectedStArr.getNaptanId());
        startActivity(openLineActivity);
    }

    //get current location methods
    private void getLocation(LocationManager lm, String provider) {
        try {
            Location location = lm.getLastKnownLocation(provider);
            if (location != null) {
                onLocationChanged(location);
            } else {
                Log.v(LOG_TAG, "Latitude Location not available");
                Log.v(LOG_TAG, "Longitude Location not available");
            }
        } catch (SecurityException e) {
            Log.e(LOG_TAG, "Location permissions error: " + e);
        }
    }

    //method to get current location, or return to default
    @Override
    public void onLocationChanged(Location location) {
        Double foundLat = location.getLatitude();
        Double foundLon = location.getLongitude();
        Log.v(LOG_TAG, "Latitude found:" + String.valueOf(foundLat));
        Log.v(LOG_TAG, "Longitude found:" + String.valueOf(foundLon));
        //if current location is within London, set mLat and mLon
        if (foundLat < Const.NORTHERNMOST_LAT && foundLat > Const.SOUTHERNMOST_LAT) {
            mLat = foundLat;
        }
        if (foundLon > Const.WESTERNMOST_LAT && foundLon < Const.EASTERNMOST_LAT) {
            mLon = foundLon;
        }
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

    //request location permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_FINE_COARSE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    provider = locationManager.getBestProvider(new Criteria(), false);
                    getLocation(locationManager, provider);
                    if (isInternetAvailable()) {
                        mRecyclerView.setVisibility(View.VISIBLE);
                        mNoInternetTv.setVisibility(View.GONE);
                        apiService = new RetrofitHelper().getService();
                        loadDataRxJava(createQueryMap(mLat, mLon));
                    } else {
                        mRecyclerView.setVisibility(View.GONE);
                        mNoInternetTv.setVisibility(View.VISIBLE);
                    }
                } else {
                    // permission denied
                    Toast.makeText(this, "Default location used",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private Map<String, String> createQueryMap(Double lat, Double lon) {
        Map<String, String> data = new HashMap<>();
        data.put("lat", lat.toString());
        data.put("lon", lon.toString());
        data.put("stopTypes", getString(R.string.metro_station));
        data.put("radius", getString(R.string.radius));
        data.put("modes", getString(R.string.tube_mode));
        return data;
    }

    //check internet connectivity
    private boolean isInternetAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
