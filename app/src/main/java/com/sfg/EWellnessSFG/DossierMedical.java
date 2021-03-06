package com.sfg.EWellnessSFG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sfg.EWellnessSFG.Common.Common;
import com.sfg.EWellnessSFG.adapter.ConsultationFragmentAdapter;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import EWellnessSFG.R;

public class DossierMedical extends AppCompatActivity {
    private final static String TAG = "DossierMedical";
    final String patientID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference patRef = db.collection("Patient").document("" + patientID + "");
    StorageReference pathReference;
    private String patient_email;
    private String patient_name;
    private String patient_phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dossier_medical);
        ImageView image = findViewById(R.id.imageView2);
        patient_email = getIntent().getStringExtra("patient_email");
        this.configureViewPager();

        Log.d(TAG, "onCreate dossier medical activity: started");
        getIncomingIntent();

        FloatingActionButton createNewFicheButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        createNewFicheButton.setOnClickListener(view -> openPatientRecord());
        if (Common.CurrentUserType.equals("patient")) {
            createNewFicheButton.setVisibility(View.GONE);
        }
        Button infoBtn = findViewById(R.id.infoBtn);
        infoBtn.setOnClickListener(v -> openPatientInfo());

        String imageId = patient_email + ".jpg"; //add a title image
        pathReference = FirebaseStorage.getInstance().getReference().child("DoctorProfile/" + imageId); //storage the image
        pathReference.getDownloadUrl().addOnSuccessListener(uri -> {
            Picasso.with(image.getContext())
                    .load(uri)
                    .placeholder(R.mipmap.ic_launcher)
                    .fit()
                    .centerCrop()
                    .into(image);//Image location

            // profileImage.setImageURI(uri);
        }).addOnFailureListener(exception -> {
            // Handle any errors
        });


    }

    //Receive patient information from the previous activity
    private void getIncomingIntent() {
        Log.d(TAG, "getIncomingIntent: checking for incoming intents");
        //Check if the incoming intents exist
        if (getIntent().hasExtra("patient_name") && getIntent().hasExtra("patient_email")) {
            Log.d(TAG, "getIncomingIntent: found intent extras.");
            patient_name = getIntent().getStringExtra("patient_name");
            patient_email = getIntent().getStringExtra("patient_email");
            patient_phone = getIntent().getStringExtra("patient_phone");

            //set patient name, email, phone number
            setPatientInfo(patient_name, patient_email, patient_phone);
        } else {
            Log.d(TAG, "No intent");
            patRef.addSnapshotListener(this, (documentSnapshot, e) -> {
                assert documentSnapshot != null;
                patient_name = documentSnapshot.getString("name");
                patient_phone = documentSnapshot.getString("tel");
                patient_email = documentSnapshot.getString("email");

                //set patient name, email, phone number
                setPatientInfo(patient_name, patient_email, patient_phone);
            });
        }


    }

    //Add patient name, email, phone number to the medical folder
    private void setPatientInfo(String patient_name, String patient_email, String patient_phone) {
        Log.d(TAG, "setPatientInfo: put patient info");

        TextView name = findViewById(R.id.patient_name);
        name.setText(patient_name);

        TextView email = findViewById(R.id.patient_address);
        email.setText(patient_email);

        TextView number = findViewById(R.id.phone_number);
        number.setText(patient_phone);


    }

    private void configureViewPager() {
        // 1 - Get ViewPager from layout
        ViewPager pager = (ViewPager) findViewById(R.id.ViewPagerDossier);
        // 2 - Set Adapter PageAdapter and glue it together
        pager.setAdapter(new ConsultationFragmentAdapter(getSupportFragmentManager()));
        // 1 - Get TabLayout from layout
        TabLayout tabs = (TabLayout) findViewById(R.id.activity_main_tabs);
        // 2 - Glue TabLayout and ViewPager together
        tabs.setupWithViewPager(pager);
        // 3 - Design purpose. Tabs have the same width
        tabs.setTabMode(TabLayout.MODE_FIXED);
        //Set Adapter PageAdapter and glue it
        TextView text = new TextView(this);
        //((ViewGroup) tabs.getChildAt(0)).getChildAt(0).setBackgroundColor(0xFF00FF00);
        //((ViewGroup) tabs.getChildAt(0)).getChildAt(1).setBackgroundColor(0xFF00FF00);

    }

    private void openPatientRecord() {
        Intent intent = new Intent(this, RecordActivity.class);
        String patient_name = getIntent().getStringExtra("patient_name");
        String patient_email = getIntent().getStringExtra("patient_email");
        intent.putExtra("patient_email", patient_email);
        intent.putExtra("patient_name", patient_name);
        startActivity(intent);
    }

    private void openPatientInfo() {
        Intent intent = new Intent(this, PatientInfoActivity.class);
        String patient_name = getIntent().getStringExtra("patient_name");
        String patient_email = getIntent().getStringExtra("patient_email");
        intent.putExtra("patient_email", patient_email);
        intent.putExtra("patient_name", patient_name);
        startActivity(intent);
    }

}