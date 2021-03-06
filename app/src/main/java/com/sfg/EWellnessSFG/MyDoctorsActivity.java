package com.sfg.EWellnessSFG;

import android.os.Bundle;

import com.sfg.EWellnessSFG.adapter.MyDoctorsAdapter;
import com.sfg.EWellnessSFG.model.Doctor;
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

public class MyDoctorsActivity extends AppCompatActivity {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference myDoctorsRef = db.collection("Patient");
    private MyDoctorsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_doctors);

        setUpRecyclerView();
    }

    public void setUpRecyclerView() {
        //Get the doctors by patient id
        final String patientID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();
        Query query = myDoctorsRef.document("" + patientID + "")
                .collection("MyDoctors").orderBy("name", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Doctor> options = new FirestoreRecyclerOptions.Builder<Doctor>()
                .setQuery(query, Doctor.class)
                .build();

        adapter = new MyDoctorsAdapter(options);
        //ListMyDoctors
        RecyclerView recyclerView = findViewById(R.id.ListMyDoctors);
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
