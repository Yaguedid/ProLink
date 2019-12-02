package com.example.intest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListOfMatchingOffers extends AppCompatActivity implements MyRecyclerViewAdapterForListMatchingOffers.ItemClickListener{
    MyRecyclerViewAdapterForListMatchingOffers adapter;
    HashMap<String,String> MatchingJobsAndAverage=new HashMap<>();
    HashMap<String,String> MatchingJobsIdsAndTitles=new HashMap<>();
    List<String> OfferTitlesList ;
    List<String> OfferAverageList ;
    List<String> OfferAverageListCopy ;
    List<String> OfferIdsList;
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
            OfferIdsList=new ArrayList<>(MatchingJobsIdsAndTitles.keySet());
        }

      //  sort();
        setRecycle();
    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(ListOfMatchingOffers.this,"Id "+OfferIdsList.get(position),Toast.LENGTH_LONG).show();
        Intent intent =new Intent(ListOfMatchingOffers.this,ApplayAnOffre.class);
        intent.putExtra("offerId",OfferIdsList.get(position));
        startActivity(intent);
    }
    public void setRecycle()
    {
        RecyclerView recyclerView = findViewById(R.id.errorRy);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyRecyclerViewAdapterForListMatchingOffers(this,OfferTitlesList,OfferAverageList);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

    }
    public void sort()
    {OfferAverageListCopy=new ArrayList<>(MatchingJobsAndAverage.values());
        double lastAv=0;
        int index=0;
        for (String average:OfferAverageListCopy)
        {
            double avInDouble=Double.valueOf(average.trim());
            if(avInDouble>=lastAv)
            {
                lastAv=avInDouble;
                index=OfferAverageList.indexOf(average);


                OfferAverageList.remove(index);
                OfferAverageList.add(0,average);

                OfferTitlesList.remove(index);
                OfferTitlesList.add(0,average);

                OfferIdsList.remove(index);
                OfferIdsList.add(0,average);
            }

        }
    }
}
