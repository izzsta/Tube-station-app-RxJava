package com.example.android.citymapperchallenge;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.example.android.citymapperchallenge.constants.Const;
import com.example.android.citymapperchallenge.model.ArrivalLineTime;
import com.example.android.citymapperchallenge.model.StationArrivals;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LineActivity extends AppCompatActivity {

    private ArrivalLineTime mSelectedArrival;
    private double mDistance;

    @BindView(R.id.line_id_tv)
    TextView lineIdTv;
    @BindView(R.id.line_name_tv)
    TextView lineNameTv;
    @BindView(R.id.distance_tv)
    TextView distanceTv;
    @BindView(R.id.toolbar_line)
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if(ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
        //TODO: if savedInstanceState = null, as with Baking App?

        Intent receivedIntent = getIntent();
        mSelectedArrival = receivedIntent.getParcelableExtra(Const.SELECTED_ARRIVAL);
        mDistance = receivedIntent.getDoubleExtra(Const.DISTANCE_TO_STATION, 0);

        lineIdTv.setText(mSelectedArrival.getLineId());
        lineNameTv.setText(mSelectedArrival.getLineName());
        distanceTv.setText(String.valueOf(mSelectedArrival.getTime()));

        if(ab!= null) {
            getSupportActionBar().setTitle(mSelectedArrival.getLineName());
        }

        loadStopSequence();
    }

    private void loadStopSequence(){
        
    }
}
