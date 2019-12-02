package com.example.intest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApplayAnOffre extends AppCompatActivity {



    /**** Employer Detalils ****/

    ImageView EmployerImage;
    TextView EmployerFirstName;
    TextView EmployerLastName;
    TextView EmployerEmaile;
    TextView OffreTitle;
    TextView OffreDescription;


    /* offer id */
    String OffreId;

    /*current use id */
    String userId;


    Uri uri;

    /* Databes Firebas */
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference offerRef;
    DatabaseReference EmployerRef;




    /* data base storage */
    private StorageReference mStorageRef;
    private Button uploadImag;
    private Button applayButton;

    private int PICK_IMAGE_INTENT=2;
    private ProgressDialog progressDialog;



    public Map<String, String> map= new HashMap<>();
    public Map<String, String> EmpMap= new HashMap<>();




    private SharedPreferences userinfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applay_an_offre);



         userinfo=getSharedPreferences("userinfos", MODE_PRIVATE);
         userId=userinfo.getString("id",null);
         Intent intent=getIntent();
         if(intent!=null)
         {
             OffreId=intent.getStringExtra("offerId");
         }
         instantiateviews();


        progressDialog =new ProgressDialog(this);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        getOffre(OffreId);


        uploadImag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent =new Intent(Intent.ACTION_PICK);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("application/pdf");
               // intent.setType("image/*");
                startActivityForResult(intent,PICK_IMAGE_INTENT);
            }
        });

        applayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadCv();
            }
        });


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

            if (requestCode==PICK_IMAGE_INTENT && resultCode == RESULT_OK){
               uri=data.getData(); }



    }
    void uploadCv(){

        String reposetry = "offer_"+OffreId;
        if(!(uri==null)) {

            if(accept()){
                    progressDialog.setMessage("uploading...");
                    progressDialog.show();
                    StorageReference filePath = mStorageRef.child(reposetry).child(userId);
                    filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();

                            /************ Start Dialoge ********** */


                            new AlertDialog.Builder(ApplayAnOffre.this)
                                    .setTitle("Post ")
                                    .setMessage("Your applay finished successfully !")
                                    .setCancelable(false)
                                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            Intent intent =new Intent( ApplayAnOffre.this, DashbordUser.class);
                                            startActivity(intent);
                                        }
                                    })

                                    // A null listener allows the button to dismiss the dialog and take no further action.
                                    //.setNegativeButton(android.R.string.no, null)
                                    .setIcon(R.drawable.chekicon)
                                    .show();
                        }
                    });
            }else {
                Toast.makeText(ApplayAnOffre.this, "Only PDF format is accepted!",Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(ApplayAnOffre.this, "Pleas select a pdf !",Toast.LENGTH_LONG).show();
        }
    }
    public void getOffre(String offerId ) {
        offerRef = database.getReference().child("Offers").child(offerId) ;
        offerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    map.put(child.getKey(),child.getValue().toString());}

                String detail= map.get("Details");
                String id= map.get("Poster Id");
                String title= map.get("Title");
                OffreTitle.setText(title);
                OffreDescription.setText(detail);
                getEmplyer(id);
               // Log.d("mydata","details" + detail + "id " + id + " title" + title );

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void getEmplyer(String employerId ) {

        EmployerRef = database.getReference().child("Users").child(employerId) ;

        EmployerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    EmpMap.put(child.getKey(),child.getValue().toString());}

                String firsName= EmpMap.get("FirstName");
                String lastName= EmpMap.get("LastName");
                String mail= EmpMap.get("Email");
                String urlPic= EmpMap.get("Picture");

                new ApplayAnOffre.DownloadImageTask((ImageView)EmployerImage)
                        .execute(urlPic);




                EmployerFirstName.setText(firsName);
                EmployerLastName.setText(lastName);
                EmployerEmaile.setText(mail);

               //Log.w("emp" , firsName + "  " + lastName+ "   " +mail+ "   "+ urlPic);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
    public boolean accept() {
        String var=uri.toString();
            if (var.contains(".pdf")) {
                return true;
            }else {
                return false;}
    }

    public void instantiateviews()
    {
        EmployerFirstName =(TextView) findViewById(R.id.Employer_FirstNameId);
        EmployerLastName =(TextView) findViewById(R.id.Employer_LastNameId);
        EmployerEmaile =(TextView) findViewById(R.id.Employer_EmailId);
        EmployerImage =(ImageView) findViewById(R.id.Employer_ImageId) ;


        /* offre details */

        OffreTitle =(TextView) findViewById(R.id.Emploer_OffreTittleId);
        OffreDescription =(TextView) findViewById(R.id.Employer_OfferBodyId);


        uploadImag =(Button) findViewById(R.id.UplodCv_BtnId);
        applayButton =(Button) findViewById(R.id.applay_Btn);

    }
}
