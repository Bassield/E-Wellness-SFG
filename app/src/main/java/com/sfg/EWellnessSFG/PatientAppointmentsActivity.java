package com.sfg.EWellnessSFG;

import android.os.Bundle;

import com.sfg.EWellnessSFG.adapter.PatientAppointmentsAdapter;
import com.sfg.EWellnessSFG.model.AppointmentInformation;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Objects;

import EWellnessSFG.R;

public class PatientAppointmentsActivity extends AppCompatActivity {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference myDoctorsRef = db.collection("Patient");
    private PatientAppointmentsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_appointments);

        setUpRecyclerView();
    }

    public void setUpRecyclerView() {
        //Get the doctors by patient id
        final String doctorID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();
        Query query = myDoctorsRef.document("" + doctorID + "")
                .collection("calendar").orderBy("time", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<AppointmentInformation> options = new FirestoreRecyclerOptions.Builder<AppointmentInformation>()
                .setQuery(query, AppointmentInformation.class)
                .build();

        adapter = new PatientAppointmentsAdapter(options);
        //List current appointments
        RecyclerView recyclerView = findViewById(R.id.patient_appointments);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
