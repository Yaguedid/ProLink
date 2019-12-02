package com.example.intest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListOfMatchingOffers extends AppCompatActivity implements MyRecyclerViewAdapterForListMatchingOffers.ItemClickListener{
    MyRecyclerViewAdapterForListMatchingOffers adapter;
    HashMap<String,String> MatchingJobsAndAverage=new HashMap<>();
    HashMap<String,String> MatchingJobsIdsAndTitles=new HashMap<>();
    List<String> OfferTitlesList ;
    List<String> OfferAverageList ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.matching_offers_recycle);
        Intent intent=getIntent();
        if(intent!=null)
        {
            MatchingJobsAndAverage=(HashMap<String, String>)intent.getSerializableExtra("MatchingJobsAndAverage");
            MatchingJobsIdsAndTitles=(HashMap<String, String>)intent.getSerializableExtra("MatchingJobsIdsAndTitles");
            OfferAverageList=new ArrayList<>(MatchingJobsAndAverage.values());
            OfferTitlesList=new ArrayList<>(MatchingJobsIdsAndTitles.values());
        }
        Log.d("zbikhl",OfferTitlesList.get(0)+" and size ="+OfferTitlesList.size());
        setRecycle();
    }

    @Override
    public void onItemClick(View view, int position) {

    }
    public void setRecycle()
    {
        RecyclerView recyclerView = findViewById(R.id.errorRy);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyRecyclerViewAdapterForListMatchingOffers(this,OfferTitlesList,OfferAverageList);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

    }
}
