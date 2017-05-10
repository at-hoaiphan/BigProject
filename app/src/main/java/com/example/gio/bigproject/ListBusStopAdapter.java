package com.example.gio.bigproject;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gio.bigproject.model.bus_stop.PlaceStop;

import java.util.List;

/**
 * Copyright by Gio.
 * Created on 4/5/2017.
 */

public class ListBusStopAdapter extends RecyclerView.Adapter<ListBusStopAdapter.ViewHolder> {

//    private List<Result> mItems;
    private List<PlaceStop> mItems;
    private PlaceListener mPlaceListener;

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvLatLongStation;
        TextView tvNameStation;
        ImageView imgStation;

        ViewHolder(View itemView) {
            super(itemView);
            tvLatLongStation = (TextView) itemView.findViewById(R.id.tvLatLong);
            tvNameStation = (TextView) itemView.findViewById(R.id.tvNameStation);
            imgStation = (ImageView) itemView.findViewById(R.id.imgStation);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mPlaceListener.onPlaceClick(getAdapterPosition());
                }
            });
        }
    }

    public ListBusStopAdapter(List<PlaceStop> posts) {
        mItems = posts;
    }

    @Override
    public ListBusStopAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View postView = inflater.inflate(R.layout.item_place, parent, false);

        return new ViewHolder(postView);
    }

//    @Override
//    public void onBindViewHolder(ListBusStopAdapter.ViewHolder holder, int position) {
//
//        Result item = mItems.get(position);
//        TextView tvIdStation = holder.tvLatLongStation;
//        TextView tvNameStation = holder.tvNameStation;
//        tvIdStation.setText(String.valueOf(item.getGeometry().getLocation().getLat()) + ";"
//                + String.valueOf(item.getGeometry().getLocation().getLng()));
//        tvNameStation.setText(item.getName());
//
//        holder.imgStation.setImageResource(R.drawable.ic_bus_stop);
//    }
@Override
public void onBindViewHolder(ListBusStopAdapter.ViewHolder holder, int position) {

    PlaceStop item = mItems.get(position);
    TextView tvLatLongStation = holder.tvLatLongStation;
    TextView tvNameStation = holder.tvNameStation;
    tvLatLongStation.setText(String.valueOf(item.getLatitude()) + ";"
            + String.valueOf(item.getLongitude()));
    tvNameStation.setText(item.getName());

    holder.imgStation.setImageResource(R.drawable.ic_bus_stop);
}

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void updateAnswers(List<PlaceStop> items) {
        mItems = items;
        notifyDataSetChanged();
    }

    private PlaceStop getItem(int adapterPosition) {
        return mItems.get(adapterPosition);
    }

    public interface PlaceListener {
        void onPlaceClick(int id);
    }

    public void setPlaceOnClickListener(PlaceListener mPlaceListener) {
        this.mPlaceListener = mPlaceListener;
    }
}
