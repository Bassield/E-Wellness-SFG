package com.sfg.EWellnessSFG.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sfg.EWellnessSFG.ChatActivity;
//import com.sfg.EWellnessSFG.R;
import com.sfg.EWellnessSFG.model.Doctor;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

import EWellnessSFG.R;

public class MyDoctorsAdapter extends FirestoreRecyclerAdapter<Doctor, MyDoctorsAdapter.MyDoctorAppointmentHolder> {
    StorageReference pathReference ;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.
     * @param options
     */
    public MyDoctorsAdapter(@NonNull FirestoreRecyclerOptions<Doctor> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyDoctorAppointmentHolder myDoctorsHolder, int position, @NonNull final Doctor doctor) {
        myDoctorsHolder.textViewTitle.setText(doctor.getName());
        myDoctorsHolder.textViewDescription.setText(new StringBuilder().append("Specialist : ").append(doctor.getSpecialist()).toString());
        myDoctorsHolder.sendMessageButton.setOnClickListener(v -> openPage(v.getContext(),doctor));
        myDoctorsHolder.callBtn.setOnClickListener(v -> openPage(myDoctorsHolder.sendMessageButton.getContext(),doctor.getTel()));
//
        String imageId = doctor.getEmail()+".jpg"; //add a title image
        pathReference = FirebaseStorage.getInstance().getReference().child("DoctorProfile/"+ imageId); //storage the image
        pathReference.getDownloadUrl().addOnSuccessListener(uri -> {
            Picasso.with(myDoctorsHolder.imageViewDoctor.getContext())
                    .load(uri)
                    .placeholder(R.mipmap.ic_launcher)
                    .fit()
                    .centerCrop()
                    .into(myDoctorsHolder.imageViewDoctor);//Image location

            // profileImage.setImageURI(uri);
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }

    private void openPage(Context wf, String phoneNumber){
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
        wf.startActivity(intent);
    }

    private void openPage(Context wf, Doctor d){
        Intent i = new Intent(wf, ChatActivity.class);
        i.putExtra("key1",d.getEmail()+"_"+ Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail());
        i.putExtra("key2", FirebaseAuth.getInstance().getCurrentUser().getEmail() +"_"+d.getEmail());
        wf.startActivity(i);
    }

    @NonNull
    @Override
    public MyDoctorAppointmentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_doctor_item, parent, false);
        return new MyDoctorAppointmentHolder(v);
    }

    static class MyDoctorAppointmentHolder extends RecyclerView.ViewHolder{
        //Here we hold the MyDoctorItems
        TextView textViewTitle;
        TextView textViewDescription;
        TextView textViewStatus;
        ImageView imageViewDoctor;
        Button sendMessageButton;
        Button callBtn;
        Button contactButton;
        public MyDoctorAppointmentHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.doctor_view_title);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
            textViewStatus = itemView.findViewById(R.id.onlineStatut);
            imageViewDoctor = itemView.findViewById(R.id.doctor_item_image);
            sendMessageButton = itemView.findViewById(R.id.voir_fiche_btn);
            callBtn = itemView.findViewById(R.id.callBtn);
            contactButton = itemView.findViewById(R.id.contact);
        }
    }
}
