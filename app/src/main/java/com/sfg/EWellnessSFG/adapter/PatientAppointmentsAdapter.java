package com.sfg.EWellnessSFG.adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

//import com.sfg.EWellnessSFG.R;
import com.sfg.EWellnessSFG.model.AppointmentInformation;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

import EWellnessSFG.R;

public class PatientAppointmentsAdapter extends FirestoreRecyclerAdapter<AppointmentInformation, PatientAppointmentsAdapter.PatientAppointmentsHolder> {
    StorageReference pathReference;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference docRef;
    DocumentSnapshot documentSnapshot;
    final String doctorID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();

    public PatientAppointmentsAdapter(@NonNull FirestoreRecyclerOptions<AppointmentInformation> options) {
        super(options);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    protected void onBindViewHolder(@NonNull PatientAppointmentsHolder patientAppointmentsHolder, int position, @NonNull final AppointmentInformation appointmentInformation) {
        patientAppointmentsHolder.dateAppointment.setText(appointmentInformation.getTime());
        patientAppointmentsHolder.patientName.setText(appointmentInformation.getDoctorName());
        patientAppointmentsHolder.appointmentType.setText(appointmentInformation.getAppointmentType());
        patientAppointmentsHolder.type.setText(appointmentInformation.getType());
        String doctorEmail = appointmentInformation.getDoctorId();
        Log.d("doctor email", doctorEmail);
        docRef = db.collection("Doctor").document("" + doctorEmail + "");
        /* Get the doctor's phone number */
        docRef.get().addOnCompleteListener(task -> {
            DocumentSnapshot document = task.getResult();
            patientAppointmentsHolder.phone.setText(document.getString("tel"));
            Log.d("telephone num", document.getString("tel"));
        });


        //display profile image
        String imageId = appointmentInformation.getDoctorId();
        pathReference = FirebaseStorage.getInstance().getReference().child("DoctorProfile/" + imageId + ".jpg");
        pathReference.getDownloadUrl().addOnSuccessListener(uri -> {
            Picasso.with(patientAppointmentsHolder.image.getContext())
                    .load(uri)
                    .placeholder(R.mipmap.ic_launcher)
                    .fit()
                    .centerCrop()
                    .into(patientAppointmentsHolder.image);
            // profileImage.setImageURI(uri);
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });

        if (appointmentInformation.getAppointmentType().equals("Consultation")) {
            //patientAppointmentsHolder.appointmentType.setBackgroundColor((patientAppointmentsHolder.type.getContext().getResources().getColor(R.color.colorPrimaryDark)));
            patientAppointmentsHolder.appointmentType.setBackground(patientAppointmentsHolder.appointmentType.getContext().getResources().getDrawable(R.drawable.button_radius_primary_color));
        }
        if (appointmentInformation.getType().equals("Accepted")) {
            patientAppointmentsHolder.type.setTextColor(Color.parseColor("#20bf6b"));
        } else if (appointmentInformation.getType().equals("Checked")) {
            patientAppointmentsHolder.type.setTextColor(Color.parseColor("#8854d0"));
        } else {
            patientAppointmentsHolder.type.setTextColor(Color.parseColor("#eb3b5a"));
        }
    }

    @NonNull
    @Override
    public PatientAppointmentsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_appointments_item, parent, false);
        return new PatientAppointmentsHolder(v);
    }


    static class PatientAppointmentsHolder extends RecyclerView.ViewHolder {
        TextView dateAppointment;
        TextView patientName;
        TextView appointmentType;
        TextView type;
        TextView phone;
        ImageView image;

        public PatientAppointmentsHolder(@NonNull View itemView) {
            super(itemView);
            dateAppointment = itemView.findViewById(R.id.appointment_date);
            patientName = itemView.findViewById(R.id.patient_name);
            appointmentType = itemView.findViewById(R.id.appointment_type);
            type = itemView.findViewById(R.id.type);
            phone = itemView.findViewById(R.id.patient_phone);
            image = itemView.findViewById(R.id.patient_image);
        }
    }
}
