package com.example.android.citymapperchallenge;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.citymapperchallenge.adapters.SequenceAdapter;
import com.example.android.citymapperchallenge.constants.Const;
import com.example.android.citymapperchallenge.model.ArrivalLineTime;
import com.example.android.citymapperchallenge.model.SequenceEndPoint.LineSequence;
import com.example.android.citymapperchallenge.model.SequenceEndPoint.StopPoint;
import com.example.android.citymapperchallenge.retrofit.RetrofitHelper;
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

public class LineActivity extends AppCompatActivity {

    private final String LOG_TAG = LineActivity.class.getSimpleName();
    @BindView(R.id.toolbar_line)
    Toolbar toolbar;
    @BindView(R.id.stops_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.no_internet_line_tv)
    TextView mNoInternetTv;
    @BindView(R.id.progress_bar_line)
    ProgressBar progressBarLine;
    private ArrivalLineTime mSelectedArrival;
    private double mDistance;
    private String mLineId;
    private String mLineName;
    private RetrofitHelper mRetrofitHelper;
    private TfLUnifyService apiService;
    private LinearLayoutManager mLayoutManager;
    private SequenceAdapter mAdapter;
    private ArrayList<StopPoint> mStopPointList = new ArrayList<>();
    private int currentStationPosition;
    private String mNaptanId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line);
        ButterKnife.bind(this);
        //set up action bar
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        mRetrofitHelper = new RetrofitHelper();

        //set up recycler view
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new SequenceAdapter(this, mStopPointList, currentStationPosition);
        mRecyclerView.setAdapter(mAdapter);

        //TODO: if savedInstanceState = null, as with Baking App?
        if (savedInstanceState == null) {
            Intent receivedIntent = getIntent();
            mSelectedArrival = receivedIntent.getParcelableExtra(Const.SELECTED_ARRIVAL);
            mDistance = receivedIntent.getDoubleExtra(Const.DISTANCE_TO_STATION, 0); //unfinished
            mLineId = mSelectedArrival.getLineId();
            mLineName = mSelectedArrival.getLineName();
            mNaptanId = receivedIntent.getStringExtra(Const.NAPTAN_ID);
        } else {
            mLineId = savedInstanceState.getString(Const.SAVED_LINE_ID);
            mNaptanId = savedInstanceState.getString(Const.SAVED_NAPTAN_ID);
            mLineName = savedInstanceState.getString(Const.SAVED_LINE_NAME);
        }

        if (ab != null) {
            getSupportActionBar().setTitle(mLineName);
        }

        if (isInternetAvailable()) {
            mRecyclerView.setVisibility(View.VISIBLE);
            mNoInternetTv.setVisibility(View.GONE);
            apiService = mRetrofitHelper.getService();
            loadStopSequence();
        } else {
            mRecyclerView.setVisibility(View.GONE);
            mNoInternetTv.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(Const.SAVED_LINE_ID, mLineId);
        outState.putString(Const.SAVED_NAPTAN_ID, mNaptanId);
        outState.putString(Const.SAVED_LINE_NAME, mLineName);
        super.onSaveInstanceState(outState);
    }

    private void loadStopSequence() {
        Observable<LineSequence> sequenceObservable = apiService.getLineSequence(mLineId);
        sequenceObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LineSequence>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        progressBarLine.setVisibility(View.VISIBLE);
                        Log.v(LOG_TAG, "Subscribed to line sequence observable");
                    }

                    @Override
                    public void onNext(LineSequence lineSequence) {
                        //get first stop point sequence from API
                        List<StopPoint> foundStops;
                        foundStops = lineSequence.
                                getStopPointSequences().get(0).getStopPoint();
                        //iterate through stops, check if stop was the selected stop and add to adapter
                        for (int i = 0; i < foundStops.size(); i++) {
                            StopPoint stopPoint = foundStops.get(i);
                            String stationId = stopPoint.getStationId();
                            if (stationId.equals(mNaptanId)) {
                                currentStationPosition = i;
                            }
                            mStopPointList.add(stopPoint);
                        }
                        mAdapter.updateSequenceAdapter(currentStationPosition);
                        mRecyclerView.getLayoutManager().scrollToPosition(currentStationPosition);
                        Log.v(LOG_TAG, "Line sequence observable onNext reached");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(LOG_TAG, "Error with line sequence observable");
                    }

                    @Override
                    public void onComplete() {
                        progressBarLine.setVisibility(View.GONE);
                        Log.v(LOG_TAG, "Line sequence observable completed");
                    }
                });
    }

    //check internet connectivity
    private boolean isInternetAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
