package com.example.android.citymapperchallenge.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.citymapperchallenge.R;
import com.example.android.citymapperchallenge.model.SequenceEndPoint.StopPoint;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SequenceAdapter extends RecyclerView.Adapter<SequenceAdapter.ViewHolder> {

    private ArrayList<StopPoint> mStopPoints;
    private Context mContext;
    private int mCurrentStationPosition;

    public SequenceAdapter(Context context, ArrayList<StopPoint> stopPoints, int stationPosition) {
        mStopPoints = stopPoints;
        mContext = context;
        mCurrentStationPosition = stationPosition;
    }

    @NonNull
    @Override
    public SequenceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.stop_sequence_item, parent,
                false);
        //pass the view to the ViewHolder
        SequenceAdapter.ViewHolder viewHolder = new SequenceAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SequenceAdapter.ViewHolder holder, int position) {
        StopPoint stopPoint = mStopPoints.get(position);
        String stopPointName = stopPoint.getName();
        if(position == mCurrentStationPosition){
            holder.mStopTv.setText(stopPointName);
            holder.mStopTv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            holder.mStopTv.setTextColor(mContext.getResources().getColor(R.color.primary_text));
        } else {
            holder.mStopTv.setText(stopPointName);
        }
    }

    @Override
    public int getItemCount() {
        return mStopPoints.size();
    }

    public void updateSequenceAdapter(int stationPosition){
        mCurrentStationPosition = stationPosition;
        notifyDataSetChanged();
    }

    //create viewholder class
    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.stopTv)
        TextView mStopTv;

        private ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
