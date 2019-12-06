package function;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class getOffersIds {
    public List<String> domainList;
    public List<String> typeList;
    public List<String> reqList;
    public List<String> skillsList;

    List<String> ListIdsDomaine = new ArrayList<>();
    List<String> ListIdsType = new ArrayList<>();
    List<String> ListIdsRequirements = new ArrayList<>();
    List<String> ListIdsSkills = new ArrayList<>();

    FirebaseDatabase database;
    DatabaseReference offerSettingsRef,offerIdRef;
   public void getOffersIds(){

   }
    public void getOffersIds(List<String> domainList,List<String> skillsList,List<String> reqList,List<String> typeList){
        domainList=new ArrayList<>(domainList);
        reqList=new ArrayList<>(reqList);
        skillsList=new ArrayList<>(skillsList);
        typeList=new ArrayList<>(typeList);
        database = FirebaseDatabase.getInstance();

    }

    private void getOfferIds() {


        for(String domain:domainList)
        {
            offerSettingsRef=database.getReference("Offer Domains").child(domain);
            offerSettingsRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot child: dataSnapshot.getChildren()) {
                        ListIdsDomaine.add(child.getValue().toString());


                    }

                }
                @Override
                public void onCancelled(DatabaseError error) {}
            });


        }

        /**************/
        /***Skills ***/
        for(String skill:skillsList)
        {
            offerSettingsRef=database.getReference("Offer Skills").child(skill);
            offerSettingsRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot child: dataSnapshot.getChildren()) {
                        ListIdsSkills.add(child.getValue().toString());

                    }
                }
                @Override
                public void onCancelled(DatabaseError error) {}
            });

        }

        /**************/
        /***Requirements ***/

        for(String req:reqList)
        {
            offerSettingsRef=database.getReference("Offer Requirements").child(req);
            offerSettingsRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot child: dataSnapshot.getChildren()) {
                        ListIdsRequirements.add(child.getValue().toString());

                    }
                }
                @Override
                public void onCancelled(DatabaseError error) {}
            });

        }
        /**************/
        /***offer Type ***/
        for(String type:typeList)
        {
            offerSettingsRef=database.getReference("Offer Type").child(type);
            offerSettingsRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot child: dataSnapshot.getChildren()) {
                        ListIdsType.add(child.getValue().toString());

                    }

                }
                @Override
                public void onCancelled(DatabaseError error) {}
            });

        }

    }
}
