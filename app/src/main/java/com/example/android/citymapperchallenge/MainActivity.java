package com.example.android.citymapperchallenge;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.android.citymapperchallenge.adapters.StationArrivalsAdapter;
import com.example.android.citymapperchallenge.constants.Const;
import com.example.android.citymapperchallenge.model.ArrivalLineTime;
import com.example.android.citymapperchallenge.model.StationArrivals;
import com.example.android.citymapperchallenge.model.NearbyEndPoint.StationsWithinRadius;
import com.example.android.citymapperchallenge.model.NearbyEndPoint.StopPoint;
import com.example.android.citymapperchallenge.model.ArrivalsEndPoint.NextArrivals;
import com.example.android.citymapperchallenge.retrofit.App;
import com.example.android.citymapperchallenge.retrofit.InternetConnectionListener;
import com.example.android.citymapperchallenge.retrofit.TfLUnifyService;

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
        StationArrivalsAdapter.DetailsAdapterListener {

    private final String CLICKED_VIEW = "Clicked View";
    private final String SELECTED_ARRIVAL = "Selected Arrival";
    private final String LOG_TAG = MainActivity.class.getSimpleName();
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

    //TODO: remove logic code off this activity
    //TODO: add in current location
    //TODO: remove commented out code

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.tube_stations_nearby));

        //TODO: make sure this survives configuration changes, and dispose of subscribers if necc

        //set up recyclerView
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new StationArrivalsAdapter(this, mStationArrivalsList, this);
        mRecyclerView.setAdapter(mAdapter);

        apiService = ((App) getApplication()).getService();
        ((App) getApplication()).setInternetConnectionListener(this);

        loadDataRxJava();
    }

    //method to load nearby stations and arrivals from TfL Unify Api
    private void loadDataRxJava() {
        listStops = new ArrayList<>();
        mStationArrivalsList.clear();
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
                                                Log.v(LOG_TAG, "Arrivals observable subscribed"); }
                                            @Override
                                            public void onNext(List<NextArrivals> nextTenTrains) {
                                                //TODO: these should be the next three trains, sometimes they're not in order
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
        Log.v(CLICKED_VIEW, "First view");
        openLineActivity(position, 0);
    }

    @Override
    public void onSecondArrivalClick(View v, int position) {
        Log.v(CLICKED_VIEW, "Second view");
        openLineActivity(position, 1);

    }

    @Override
    public void onThirdArrivalClick(View v, int position) {
        Log.v(CLICKED_VIEW, "Third view");
        openLineActivity(position, 2);

    }

    public void openLineActivity(int position, int index){
        StationArrivals selectedStArr = mStationArrivalsList.get(position);
        double distanceFromStation = selectedStArr.getDistance();
        ArrivalLineTime selectedArrival = selectedStArr.getArrivals().get(index);
        Intent openLineActivity = new Intent(this, LineActivity.class);
        openLineActivity.putExtra(Const.DISTANCE_TO_STATION, distanceFromStation);
        openLineActivity.putExtra(Const.SELECTED_ARRIVAL, selectedArrival);
        startActivity(openLineActivity);
    }
}
