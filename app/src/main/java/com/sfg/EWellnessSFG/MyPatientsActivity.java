package com.sfg.EWellnessSFG;

import android.os.Bundle;

import com.sfg.EWellnessSFG.adapter.MyPatientsAdapter;
import com.sfg.EWellnessSFG.model.Patient;
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

public class MyPatientsActivity extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference myPatientsRef = db.collection("Doctor");
    private MyPatientsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_patients);
        setUpRecyclerView();

    }

    public void setUpRecyclerView() {

        final String doctorID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();
        Query query = myPatientsRef.document("" + doctorID + "")
                .collection("MyPatients").orderBy("name", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Patient> options = new FirestoreRecyclerOptions.Builder<Patient>()
                .setQuery(query, Patient.class)
                .build();

        adapter = new MyPatientsAdapter(options);
        //ListMyPatients
        RecyclerView recyclerView = findViewById(R.id.ListMyPatients);
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
