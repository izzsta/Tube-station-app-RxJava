package com.example.android.citymapperchallenge;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.android.citymapperchallenge.adapters.StationAndArrivalsAdapter;
import com.example.android.citymapperchallenge.model.ArrivalLineTime;
import com.example.android.citymapperchallenge.model.NearbyStationDetails;
import com.example.android.citymapperchallenge.nearbyStations.StationsWithinRadius;
import com.example.android.citymapperchallenge.nearbyStations.StopPoint;
import com.example.android.citymapperchallenge.nextArrivals.NextTenTrains;
import com.example.android.citymapperchallenge.retrofit.App;
import com.example.android.citymapperchallenge.retrofit.InternetConnectionListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements InternetConnectionListener,
        StationAndArrivalsAdapter.DetailsAdapterListener {

    private final String CLICKED_VIEW = "Clicked View";
    private final String LOG_TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.nearest_stops)
    TextView mNearestStopsTv;
    @BindView(R.id.next_arrivals)
    TextView mNextArrivalsTv;
    @BindView(R.id.no_internet_tv)
    TextView mNoInternetTv;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    //ArrayList<ArrivalLineTime> nextThreeArrivals;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<StopPoint> listStops;
    private String nextArrivalsJson;
    private ArrayList<NearbyStationDetails> mNearbyDetails;
    private StationAndArrivalsAdapter mAdapter;
    private boolean arrivalsLoopFinished = false;


    //TODO: remove logic code off this activity
    //TODO: set arrivalsLoopfinished to false in onResume
    //TODO: add in current location
    //TODO: remove commented out code

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //TODO: make sure this survives configuration changes, and dispose of subscribers if necc

        //set up recyclerView
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        //set to data adapter
        mAdapter = new StationAndArrivalsAdapter(this, new ArrayList<NearbyStationDetails>(0),
                this);
        mRecyclerView.setAdapter(mAdapter);

        //load data for adapter
        ((App) getApplication()).setInternetConnectionListener(this);
        //loadNearbyStationsAndArrivals();

        loadDataRxJava();
    }


    private void loadDataRxJava() {
        mNearbyDetails = new ArrayList<>();
        listStops = new ArrayList<>();
        //create first observable to return list of nearby stopPoints
        Observable<StationsWithinRadius> observable = ((App) getApplication()).getService().getNearbyStations();
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<StationsWithinRadius>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Log.e(LOG_TAG, "Subscribed to nearby stations observable");
                    }

                    @Override
                    public void onNext(StationsWithinRadius stationsWithinRadius) {
                        listStops = (ArrayList<StopPoint>)
                                stationsWithinRadius.getStopPoints();
                        Log.e(LOG_TAG, "list of stops found: " + listStops);

                        //mNearestStopsTv.setText(listStops.toString());
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.e(LOG_TAG, "Nearby stations observer error");

                    }

                    @Override
                    public void onComplete() {
                        int numberOfNearbyStations = listStops.size();

                        for (int i = 0; i < numberOfNearbyStations; i++) {

                            Log.e(LOG_TAG, "Starting arrivals process with" + i);
                            if (arrivalsLoopFinished = true) {
                                StopPoint stopPoint = listStops.get(i);
                                arrivalsLoopFinished = false;
                                //for each stop point, create an object with station details and next three arrivals
                                final NearbyStationDetails foundDetails = new NearbyStationDetails();
                                foundDetails.setStation(stopPoint.getCommonName());
                                String naptanId = stopPoint.getNaptanId();
                                foundDetails.setNaptanId(naptanId);
                                foundDetails.setDistance(stopPoint.getDistance());

                                //create new observable
                                Observable<List<NextTenTrains>> arrivalsObservable = ((App) getApplication())
                                        .getService().getNextArrivals(naptanId);
                                arrivalsObservable
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Observer<List<NextTenTrains>>() {
                                            @Override
                                            public void onSubscribe(Disposable d) {
                                                Log.e(LOG_TAG, "Arrivals observable subscribed");
                                            }

                                            @Override
                                            public void onNext(List<NextTenTrains> nextTenTrains) {
                                                //for the first three arrivals, create ArrivalLineTime objects
                                                ArrayList<ArrivalLineTime> nextThreeArrivals = new ArrayList<>();

                                                for (int j = 0; j < 3; j++) {

                                                    Log.e(LOG_TAG, "Starting process with j:" + j);
                                                    NextTenTrains foundTrain = nextTenTrains.get(j);
                                                    String lineId = foundTrain.getLineId();
                                                    String lineName = foundTrain.getLineName();
                                                    long time = foundTrain.getTimeToStation();
                                                    ArrivalLineTime arrival = new ArrivalLineTime(lineId, lineName, time);
                                                    nextThreeArrivals.add(arrival);
                                                    Log.d(LOG_TAG, "arrival added" + lineId + lineName + time);
                                                }
                                                foundDetails.setArrivals(nextThreeArrivals);
                                            }

                                            @Override
                                            public void onError(Throwable e) {
                                                e.printStackTrace();
                                                Log.e(LOG_TAG, "Arrivals observable error");

                                            }

                                            @Override
                                            public void onComplete() {
                                                //TODO: move to next i
                                                arrivalsLoopFinished = true;
                                                Log.e(LOG_TAG, "Arrivals observable completed");

                                            }
                                        });
                                //add station details and arrival details to a NearbyStationDetails object
                                mNearbyDetails.add(foundDetails);
                            }
                        }
                        Log.e(LOG_TAG, "Nearby stations observer complete");
                        mAdapter.setStationsToAdapter(mNearbyDetails);

                    }
                });
    }

//        //for each stop point, find the next three arrivals and
//        // create objects to later pass to the adapter
//        int numberOfNearbyStations = listStops.size();
//
//        for (int i = 0; i < numberOfNearbyStations; i++) {
//
//            nextThreeArrivals.clear();
//            StopPoint stopPoint = listStops.get(i);
//
//            //for each stop point, create an object with station details and next three arrivals
//            NearbyStationDetails foundDetails = new NearbyStationDetails();
//            foundDetails.setStation(stopPoint.getCommonName());
//            String naptanId = stopPoint.getNaptanId();
//            foundDetails.setNaptanId(naptanId);
//            foundDetails.setDistance(stopPoint.getDistance());
//
//            //create new observable
//            Observable<List<NextTenTrains>> arrivalsObservable = ((App) getApplication())
//                    .getService().getNextArrivals(naptanId);
//            arrivalsObservable
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Observer<List<NextTenTrains>>() {
//                        @Override
//                        public void onSubscribe(Disposable d) {
//                            Log.e(LOG_TAG, "Arrivals observable subscribed");
//                        }
//
//                        @Override
//                        public void onNext(List<NextTenTrains> nextTenTrains) {
//                            nextThreeArrivals = new ArrayList<>();
//                            //for the first three arrivals, create ArrivalLineTime objects
//                            for (int j = 0; j < 3; j++) {
//                                NextTenTrains foundTrain = nextTenTrains.get(j);
//                                String lineId = foundTrain.getLineId();
//                                String lineName = foundTrain.getLineName();
//                                long time = foundTrain.getTimeToStation();
//                                ArrivalLineTime arrival = new ArrivalLineTime(lineId, lineName, time);
//                                nextThreeArrivals.add(arrival);
//                                Log.d(LOG_TAG, "arrival added" + lineId + lineName + time);
//                            }
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//                            e.printStackTrace();
//                            Log.e(LOG_TAG, "Arrivals observable error");
//
//                        }
//
//                        @Override
//                        public void onComplete() {
//                            Log.e(LOG_TAG, "Arrivals observable completed");
//
//                        }
//                    });
//            //add station details and arrival details to a NearbyStationDetails object
//            foundDetails.setArrivals(nextThreeArrivals);
//            mNearbyDetails.add(foundDetails);
//        }
//        mAdapter.setStationsToAdapter(mNearbyDetails);
//    }

//    //load stations and arrivals using Retrofit on background thread
//    private void loadNearbyStationsAndArrivals(){
//        ((App) getApplication()).getService().getNearbyStations()
//                .enqueue(getStationsWithinRadius());
//    }
//
//    private Callback<StationsWithinRadius> getStationsWithinRadius() {
//        return new Callback<StationsWithinRadius>(){
//
//            @Override
//            public void onResponse(Call<StationsWithinRadius> call, Response<StationsWithinRadius> response) {
//                if (response.isSuccessful()) {
//                    mNearestStopsTv.setVisibility(View.VISIBLE);
//                    mNoInternetTv.setVisibility(View.GONE);
//
//                    StationsWithinRadius stationsFound = response.body();
//                    listStopPoints = (ArrayList<StopPoint>) stationsFound.getStopPoints();
//
//                    //end target is an array of NearbyStationDetail objects
//                    mNearbyDetails = new ArrayList<>();
//
//                    //for each stop point, get station name, distance and naptanID
//                    int numberOfNearbyStations = listStopPoints.size();
//
//                    for (int i=0; i<numberOfNearbyStations; i++) {
//
//                        nextThreeArrivals.clear();
//                        NearbyStationDetails foundDetails = new NearbyStationDetails();
//                        StopPoint stopPoint = listStopPoints.get(i);
//                        foundDetails.setStation(stopPoint.getCommonName());
//                        String naptanId = stopPoint.getNaptanId();
//                        foundDetails.setNaptanId(naptanId);
//                        foundDetails.setDistance(stopPoint.getDistance());
//
//                        //use naptanID to query the arrivals end point
//                        ((App) getApplication()).getService().getNextArrivals(naptanId)
//                                .enqueue(getNextTenTrains());
//
//                        foundDetails.setArrivals(nextThreeArrivals);
//                        mNearbyDetails.add(foundDetails);
//                    }
//
//                    //mAdapter.setRecipesToAdapter(mNearbyDetails);
//
//                    Log.d(LOG_TAG, "nearest stops loaded from API");
//                } else {
//                    int statusCode = response.code();
//                    Log.d(LOG_TAG, "failed with status: " + statusCode);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<StationsWithinRadius> call, Throwable t) {
//                Log.d(LOG_TAG, "error loading from API " + t.getMessage());
//
//            }
//        };
//    }
//
//
//
//    private Callback<List<NextTenTrains>> getNextTenTrains(){
//        return new Callback<List<NextTenTrains>>() {
//            @Override
//            public void onResponse(Call<List<NextTenTrains>> call, Response<List<NextTenTrains>> responseTwo) {
//                if (responseTwo.isSuccessful()) {
//                    mNextArrivalsTv.setVisibility(View.VISIBLE);
//                    mNoInternetTv.setVisibility(View.GONE);
//
//                    List<NextTenTrains> nextTenTrainsList = responseTwo.body();
//
////                    nextArrivalsJson = responseTwo.body().toString();
////                    mNextArrivalsTv.setText(nextArrivalsJson);
//
////                    mNearbyDetails = new ArrayList<>();
////                    int numberOfNearbyStations = listStopPoints.size();
////
////                    for (int i=0; i<numberOfNearbyStations; i++){
////
////                        //NearbyStationDetails foundDetails = new NearbyStationDetails();
////                        StopPoint stopPoint = listStopPoints.get(i);
////                        foundDetails.setStation(stopPoint.getCommonName());
////                        foundDetails.setNaptanId(stopPoint.getNaptanId());
////                        foundDetails.setDistance(stopPoint.getDistance());
//
//                        //create method to feed in naptan ID and get list of arrivals for each station
////                        nextThreeArrivals = new ArrayList<>();
//                        for (int j=0; j<3; j++){
//                            NextTenTrains foundTrain = nextTenTrainsList.get(j);
//                            String lineId = foundTrain.getLineId();
//                            String lineName = foundTrain.getLineName();
//                            long time = foundTrain.getTimeToStation();
//                            ArrivalLineTime arrival = new ArrivalLineTime(lineId, lineName, time);
//                            nextThreeArrivals.add(arrival);
//                            Log.d(LOG_TAG, "arrival added" + lineId + lineName + time);
//                        }
//                    //need to create an array of objects containing station name,
//                    //naptanID, distance, next three arrivals and their tube lines
//
//
//                    Log.d(LOG_TAG, "next arrivals loaded from API");
//                } else {
//                    int statusCode = responseTwo.code();
//                    Log.d(LOG_TAG, "failed with status: " + statusCode);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<NextTenTrains>> call, Throwable t) {
//                Log.d(LOG_TAG, "error loading from API " + t.getMessage());
//
//            }
//        };
//    }


    @Override
    public void onInternetUnavailable() {
        mNearestStopsTv.setVisibility(View.GONE);
        mNoInternetTv.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onFirstArrivalClick(View v, int position) {
        Log.e(CLICKED_VIEW, "First view");

    }

    @Override
    public void onSecondArrivalClick(View v, int position) {
        Log.e(CLICKED_VIEW, "Second view");

    }

    @Override
    public void onThirdArrivalClick(View v, int position) {
        Log.e(CLICKED_VIEW, "Third view");
    }

}
