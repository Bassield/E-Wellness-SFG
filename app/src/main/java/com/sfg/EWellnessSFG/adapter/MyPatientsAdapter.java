package com.sfg.EWellnessSFG.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sfg.EWellnessSFG.ChatActivity;
import com.sfg.EWellnessSFG.DossierMedical;
//import com.sfg.EWellnessSFG.R;
import com.sfg.EWellnessSFG.model.Patient;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

import EWellnessSFG.R;

public class MyPatientsAdapter extends FirestoreRecyclerAdapter<Patient, MyPatientsAdapter.MyPatientsHolder> {
    StorageReference pathReference ;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.
     * @param options
     */
    public MyPatientsAdapter(@NonNull FirestoreRecyclerOptions<Patient> options) {
        super(options);
    }


    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull final MyPatientsHolder myPatientsHolder, int position, @NonNull final Patient patient) {
        myPatientsHolder.textViewTitle.setText(patient.getName());
        myPatientsHolder.textViewTelephone.setText("Tél : "+patient.getTel());
        myPatientsHolder.contactButton.setOnClickListener(v -> openPage(v.getContext(),patient));

        myPatientsHolder.parentLayout.setOnClickListener(v -> openPatientMedicalFolder(v.getContext(),patient));
        myPatientsHolder.callBtn.setOnClickListener(v -> openPage(myPatientsHolder.contactButton.getContext(),patient.getTel()));

        String imageId = patient.getEmail()+".jpg"; //add a title image
        pathReference = FirebaseStorage.getInstance().getReference().child("DoctorProfile/"+ imageId); //storage the image
        pathReference.getDownloadUrl().addOnSuccessListener(uri -> {
            Picasso.with(myPatientsHolder.imageViewPatient.getContext())
                    .load(uri)
                    .placeholder(R.mipmap.ic_launcher)
                    .fit()
                    .centerCrop()
                    .into(myPatientsHolder.imageViewPatient);//Image location

            // profileImage.setImageURI(uri);
        }).addOnFailureListener(exception -> {
            // Handle any errors
        });


    }
    private void openPage(Context wf, String phoneNumber){
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
        wf.startActivity(intent);
    }

    private void openPatientMedicalFolder(Context medicalFolder, Patient patient){
        Intent intent = new Intent(medicalFolder, DossierMedical.class);
        intent.putExtra("patient_name", patient.getName());
        intent.putExtra("patient_email",patient.getEmail());
        intent.putExtra("patient_phone", patient.getTel());
        medicalFolder.startActivity(intent);
    }

    private void openPage(Context wf,Patient p){
        Intent i = new Intent(wf, ChatActivity.class);
        i.putExtra("key1",p.getEmail()+"_"+ Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail());
        i.putExtra("key2", FirebaseAuth.getInstance().getCurrentUser().getEmail() +"_"+p.getEmail());
        wf.startActivity(i);
    }
    @NonNull
    @Override
    public MyPatientsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_patient_item, parent, false);
        return new MyPatientsHolder(v);
    }

    static class MyPatientsHolder extends RecyclerView.ViewHolder{
        //Here we hold the MyDoctorItems
        Button callBtn;
        TextView textViewTitle;
        TextView textViewTelephone;
        ImageView imageViewPatient;
        Button contactButton;
        RelativeLayout parentLayout;
        public MyPatientsHolder(@NonNull View itemView) {
            super(itemView);
            callBtn = itemView.findViewById(R.id.callBtn);
            textViewTitle = itemView.findViewById(R.id.patient_view_title);
            textViewTelephone = itemView.findViewById(R.id.text_view_telephone);
            imageViewPatient = itemView.findViewById(R.id.patient_item_image);
            contactButton = itemView.findViewById(R.id.contact);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }




}
