package com.example.intest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyRecyclerViewAdapterForListMatchingOffers extends RecyclerView.Adapter<MyRecyclerViewAdapterForListMatchingOffers.ViewHolder> {

    private List<String> OfferTitle;
    private List<String> MatchingAvList;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    MyRecyclerViewAdapterForListMatchingOffers(Context context, List<String> data, List<String> matchingAv) {
        this.mInflater = LayoutInflater.from(context);
        this.OfferTitle = data;
        this.MatchingAvList=matchingAv;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.row_layout, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String offerTitle = OfferTitle.get(position);
        String average=MatchingAvList.get(position);
        holder.offerIdTitleView.setText(offerTitle);
        holder.matchingOfferAv.setText(average);
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return OfferTitle.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView offerIdTitleView;
        TextView matchingOfferAv;


        ViewHolder(View itemView) {
            super(itemView);
            offerIdTitleView = itemView.findViewById(R.id.OfferTitleId);
            matchingOfferAv=itemView.findViewById(R.id.MatchinAvId);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return OfferTitle.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}

