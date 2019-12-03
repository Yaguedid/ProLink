package com.example.intest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InboxForEmployers extends AppCompatActivity implements MyRecyclerViewAdapterForMyInbox.ItemClickListener{
    MyRecyclerViewAdapterForMyInbox adapter;
    FirebaseDatabase database;
    DatabaseReference myRef;
    HashMap<String,String> MatchingCandidatesAndAverage=new HashMap<>();
    List<String> MatchingCandidatesListIds;
    List<String> AverageCandidatesList;
    Handler handler = new Handler();
    private String userId;
    private SharedPreferences userinfo;
    List<String> CandidatesFirstNames=new ArrayList<>();
    List<String> CandidatesLastNames=new ArrayList<>();
    List<String> CandidatesFullNames=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.matching_candidates_recycle);
        userinfo=getSharedPreferences("userinfos", MODE_PRIVATE);
        userId=userinfo.getString("id",null);
        database = FirebaseDatabase.getInstance();

       getMatchingCandidatesIds();
    }
    public void setRecycle()
    {
        RecyclerView recyclerView = findViewById(R.id.errorRy);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyRecyclerViewAdapterForMyInbox(this,CandidatesFullNames,AverageCandidatesList);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

    }


    @Override
    public void onItemClick(View view, int position) {
            Toast.makeText(InboxForEmployers.this,MatchingCandidatesListIds.get(position)+"",Toast.LENGTH_LONG).show();
    }


    public  void getMatchingCandidatesIds()
    {
        myRef=database.getReference("EmployersInbox").child(userId);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                   MatchingCandidatesAndAverage.put(child.getKey(),child.getValue().toString());
                }
                MatchingCandidatesListIds= new ArrayList<>(MatchingCandidatesAndAverage.keySet());
                AverageCandidatesList= new ArrayList<>(MatchingCandidatesAndAverage.values());
                threadToForceWaitForFirstName(MatchingCandidatesListIds.size());
            }
            @Override
            public void onCancelled(DatabaseError error) {}
        });
    }
    public void threadToForceWaitForFirstName(final int size)
    {
        getMatchingCandidatesFirstNames();
        Thread thread = new Thread() {
            @Override
            public void run() {

                if(CandidatesFirstNames.size()<size)
                {
                    handler.post(this);

                }else
                {
                    threadToForceWaitForLastName(size);
                }

            }
        };

        thread.start();
    }
    public void threadToForceWaitForLastName(final int size)
    {
        getMatchingCandidatesLastNames();
        Thread thread = new Thread() {
            @Override
            public void run() {

                if(CandidatesLastNames.size()<size)
                {
                    handler.post(this);

                }else
                {
                    showCompleteName();
                }

            }
        };

        thread.start();
    }
    public void getMatchingCandidatesFirstNames()
    {
        for(String id:MatchingCandidatesListIds)
        {
            myRef=database.getReference("Users").child(id).child("FirstName");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String value = dataSnapshot.getValue(String.class);
                    CandidatesFirstNames.add(value);
                }
                @Override
                public void onCancelled(DatabaseError error) {}
            });
        }
    }
    public void getMatchingCandidatesLastNames()
    {
        for(String id:MatchingCandidatesListIds)
        {
            myRef=database.getReference("Users").child(id).child("LastName");
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String value = dataSnapshot.getValue(String.class);
                    CandidatesLastNames.add(value);
                }
                @Override
                public void onCancelled(DatabaseError error) {}
            });
        }
    }
    public void showCompleteName()
    {

        int size=CandidatesLastNames.size();
        for(int i=0;i<size;i++)
        {
            CandidatesFullNames.add(CandidatesLastNames.get(i)+" "+CandidatesFirstNames.get(i));
        }
        setRecycle();
    }

}
