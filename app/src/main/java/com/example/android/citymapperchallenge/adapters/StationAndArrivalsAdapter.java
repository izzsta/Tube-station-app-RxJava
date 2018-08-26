package com.example.android.citymapperchallenge.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.citymapperchallenge.R;
import com.example.android.citymapperchallenge.nearbyStations.StopPoint;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StationAndArrivalsAdapter extends RecyclerView.Adapter<StationAndArrivalsAdapter.ViewHolder>{

    private ArrayList<StopPoint> mNearbyDetails;
    private Context mContext;
    private DetailsAdapterListener mClickHandler;

    public StationAndArrivalsAdapter(Context c, ArrayList<StopPoint> nearbyDetails,
                                     DetailsAdapterListener clickHandler) {
        mNearbyDetails = nearbyDetails;
        mContext = c;
        mClickHandler = clickHandler;
    }

    @Override
    public StationAndArrivalsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate the item Layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.arrival_list_item, parent,
                false);
        //pass the view to the ViewHolder
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(StationAndArrivalsAdapter.ViewHolder holder, int position) {
        StopPoint stopPoint = mNearbyDetails.get(position);
        String stationName = stopPoint.getCommonName();
        String naptanId = stopPoint.getNaptanId();

        holder.mStationTv.setText(stationName);
        holder.mNaptanId.setText(naptanId);
    }

    @Override
    public int getItemCount() {
        return mNearbyDetails.size();
    }

    public void setStationsToAdapter(ArrayList<StopPoint> nearbyStations){
        mNearbyDetails = nearbyStations;
        notifyDataSetChanged();
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
        @BindView(R.id.naptanIdListTv)
        TextView mNaptanId;

        private ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            mStationTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickHandler.onFirstArrivalClick(v, getAdapterPosition());
                }
            });

            mNaptanId.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    mClickHandler.onSecondArrivalClick(v, getAdapterPosition());
                }
            });

//            mNaptanId.setOnClickListener(new View.OnClickListener(){
//                @Override
//                public void onClick(View v) {
//                    mClickHandler.onThirdArrivalClick(v, getAdapterPosition());
//                }
//            });
        }

    }
}
