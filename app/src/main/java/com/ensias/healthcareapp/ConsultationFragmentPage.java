package com.ensias.healthcareapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ensias.healthcareapp.adapter.ConsultationAdapter;
import com.ensias.healthcareapp.model.Record;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ConsultationFragmentPage extends Fragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ConsultationAdapter adapter;
    View result;

    public ConsultationFragmentPage() {
        // Required empty public constructor
    }

    public static ConsultationFragmentPage newInstance() {
        return new ConsultationFragmentPage();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        result = inflater.inflate(R.layout.fragment_consultation_page, container, false);
        setUpRecyclerView();
        return result;
    }

    private void setUpRecyclerView() {

        String email_id = getActivity().getIntent().getExtras().getString("patient_email");
        CollectionReference recordRef = db.collection("Patient").document(email_id).collection("MyMedicalFolder");
        Query query = recordRef.whereEqualTo("type", "Consultation");

        FirestoreRecyclerOptions<Record> options = new FirestoreRecyclerOptions.Builder<Record>()
                .setQuery(query, Record.class)
                .build();

        adapter = new ConsultationAdapter(options);

        RecyclerView recyclerView = result.findViewById(R.id.consultationRecycleView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}