package com.example.gio.bigproject.data;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gio.bigproject.R;
import com.example.gio.bigproject.data.model.Result;

import java.util.List;

/**
 * Copyright by Gio.
 * Created on 4/5/2017.
 */

public class AnswersAdapter extends RecyclerView.Adapter<AnswersAdapter.ViewHolder> {

    private List<Result> mItems;
    private Context mContext;

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvIdStation;
        TextView tvNameStation;
        ImageView imgStation;
        PostItemListener mItemListener;

        ViewHolder(View itemView) {
            super(itemView);
            tvIdStation = (TextView) itemView.findViewById(R.id.tvIdStation);
            tvNameStation = (TextView) itemView.findViewById(R.id.tvNameStation);
            imgStation = (ImageView) itemView.findViewById(R.id.imgStation);
        }
    }

    public AnswersAdapter(Context context, List<Result> posts) {
        mItems = posts;
        mContext = context;
    }

    @Override
    public AnswersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View postView = inflater.inflate(R.layout.item_owner, parent, false);

        return new ViewHolder(postView);
    }

    @Override
    public void onBindViewHolder(AnswersAdapter.ViewHolder holder, int position) {

        Result item = mItems.get(position);
        TextView tvIdStation = holder.tvIdStation;
        TextView tvNameStation = holder.tvNameStation;
        tvIdStation.setText(String.valueOf(item.getGeometry().getLocation().getLat()) + ";"
                + String.valueOf(item.getGeometry().getLocation().getLng()));
        tvNameStation.setText(item.getName());
        Log.d("", "onBindViewHolder: " + item.getGeometry().getLocation().getLat());

        holder.imgStation.setImageResource(R.drawable.ic_bus_stop);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void updateAnswers(List<Result> items) {
        mItems = items;
        notifyDataSetChanged();
    }

    private Result getItem(int adapterPosition) {
        return mItems.get(adapterPosition);
    }

    public interface PostItemListener {
        void onPostClick(long id);
    }
}
