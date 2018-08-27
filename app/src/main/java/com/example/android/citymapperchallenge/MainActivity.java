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
import com.example.android.citymapperchallenge.model.StationArrivals;
import com.example.android.citymapperchallenge.nearbyStations.StationsWithinRadius;
import com.example.android.citymapperchallenge.nearbyStations.StopPoint;
import com.example.android.citymapperchallenge.nextArrivals.NextArrivals;
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
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<StopPoint> listStops;
    private ArrayList<StationArrivals> mStationArrivalsList = new ArrayList<>();
    private StationAndArrivalsAdapter mAdapter;
    private TfLUnifyService apiService;


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
        mAdapter = new StationAndArrivalsAdapter(this, mStationArrivalsList, this);
        mRecyclerView.setAdapter(mAdapter);

        apiService = ((App) getApplication()).getService();
        ((App) getApplication()).setInternetConnectionListener(this);

        loadDataRxJava();
    }


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
