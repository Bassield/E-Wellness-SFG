package com.sfg.EWellnessSFG.adapter;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
//import com.sfg.EWellnessSFG.R;
import com.sfg.EWellnessSFG.model.AppointmentInformation;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import EWellnessSFG.R;

public class ConfirmedAppointmentsAdapter extends FirestoreRecyclerAdapter<AppointmentInformation, ConfirmedAppointmentsAdapter.ConfirmedAppointmentsHolder> {
    StorageReference pathReference ;
    public ConfirmedAppointmentsAdapter(@NonNull FirestoreRecyclerOptions<AppointmentInformation> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ConfirmedAppointmentsHolder confirmedAppointmentsHolder, int position, @NonNull final AppointmentInformation appointmentInformation) {
        confirmedAppointmentsHolder.dateAppointment.setText(appointmentInformation.getTime());
        confirmedAppointmentsHolder.patientName.setText(appointmentInformation.getPatientName());
        confirmedAppointmentsHolder.appointmentType.setText(appointmentInformation.getAppointmentType());

        String imageId = appointmentInformation.getPatientId()+".jpg"; //add a title image
        pathReference = FirebaseStorage.getInstance().getReference().child("DoctorProfile/"+ imageId); //storage the image
        pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(confirmedAppointmentsHolder.patientImage.getContext())
                        .load(uri)
                        .placeholder(R.mipmap.ic_launcher)
                        .fit()
                        .centerCrop()
                        .into(confirmedAppointmentsHolder.patientImage);//Image location

                // profileImage.setImageURI(uri);
            }
        }).addOnFailureListener(exception -> {
            // Handle any errors
        });

    }

    @NonNull
    @Override
    public ConfirmedAppointmentsAdapter.ConfirmedAppointmentsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.confirmed_appointements_item, parent, false);
        return new ConfirmedAppointmentsHolder(v);
    }

    static class ConfirmedAppointmentsHolder extends RecyclerView.ViewHolder{
        TextView dateAppointment;
        TextView patientName;
        TextView appointmentType;
        ImageView patientImage;
        public ConfirmedAppointmentsHolder(@NonNull View itemView) {
            super(itemView);
            dateAppointment = itemView.findViewById(R.id.appointment_date);
            patientName = itemView.findViewById(R.id.patient_name);
            appointmentType = itemView.findViewById(R.id.appointment_type);
            patientImage = itemView.findViewById(R.id.patient_image);
        }
    }
}
