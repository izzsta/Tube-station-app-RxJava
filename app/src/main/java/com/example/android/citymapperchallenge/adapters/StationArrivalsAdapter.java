package com.example.android.citymapperchallenge.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.citymapperchallenge.R;
import com.example.android.citymapperchallenge.model.ArrivalLineTime;
import com.example.android.citymapperchallenge.model.StationArrivals;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StationArrivalsAdapter extends RecyclerView.Adapter<StationArrivalsAdapter.ViewHolder> {

    private ArrayList<StationArrivals> mNearbyDetails;
    private Context mContext;
    private DetailsAdapterListener mClickHandler;

    public StationArrivalsAdapter(Context c, ArrayList<StationArrivals> nearbyDetails,
                                  DetailsAdapterListener clickHandler) {
        mNearbyDetails = nearbyDetails;
        mContext = c;
        mClickHandler = clickHandler;
    }

    @Override
    public StationArrivalsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate the item Layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.arrival_list_item, parent,
                false);
        //pass the view to the ViewHolder
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(StationArrivalsAdapter.ViewHolder holder, int position) {
        StationArrivals nearbyStationDetails = mNearbyDetails.get(position);
        String stationName = nearbyStationDetails.getStation();
        ArrayList<ArrivalLineTime> threeArrivals = nearbyStationDetails.getArrivals();
        String firstArrival = "";
        String secondArrival = "";
        String thirdArrival = "";

        if (threeArrivals != null) {
            if (threeArrivals.size() >= 1) {
                firstArrival = arrivalToString(threeArrivals.get(0));
            }
            if (threeArrivals.size() >= 2) {
                secondArrival = arrivalToString(threeArrivals.get(1));
            }
            if (threeArrivals.size() >= 3) {
                thirdArrival = arrivalToString(threeArrivals.get(2));
            }
        }
        holder.mStationTv.setText(stationName);
        holder.mFirstArrivalTv.setText(firstArrival);
        holder.mSecondArrivalTv.setText(secondArrival);
        holder.mThirdArrivalTv.setText(thirdArrival);

    }

    @Override
    public int getItemCount() {
        return mNearbyDetails.size();
    }

    //create onClickListener interface
    public interface DetailsAdapterListener {
        void onFirstArrivalClick(View v, int position);

        void onSecondArrivalClick(View v, int position);

        void onThirdArrivalClick(View v, int position);
    }

    //create viewholder class
    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.stationListTv)
        TextView mStationTv;
        @BindView(R.id.firstArrivalTv)
        TextView mFirstArrivalTv;
        @BindView(R.id.secondArrivalTv)
        TextView mSecondArrivalTv;
        @BindView(R.id.thirdArrivalTv)
        TextView mThirdArrivalTv;

        private ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            mFirstArrivalTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickHandler.onFirstArrivalClick(v, getAdapterPosition());
                }
            });

            mSecondArrivalTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickHandler.onSecondArrivalClick(v, getAdapterPosition());
                }
            });

            mThirdArrivalTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickHandler.onThirdArrivalClick(v, getAdapterPosition());
                }
            });
        }
    }

    private String arrivalToString(ArrivalLineTime arr) {
        String lineName = arr.getLineName();
        long arrivalTime = arr.getTime();
        long arrInMins = TimeUnit.SECONDS.toMinutes(arrivalTime);
        return lineName + " line: " + arrInMins + " mins";
    }
}
