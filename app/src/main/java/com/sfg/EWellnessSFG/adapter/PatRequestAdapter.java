package com.sfg.EWellnessSFG.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

//import com.sfg.EWellnessSFG.R;
import com.sfg.EWellnessSFG.model.Doctor;
import com.sfg.EWellnessSFG.model.Patient;
import com.sfg.EWellnessSFG.model.Request;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import static androidx.core.content.ContextCompat.startActivities;

import java.util.Objects;

import EWellnessSFG.R;

public class PatRequestAdapter extends FirestoreRecyclerAdapter<Request, PatRequestAdapter.PatRequestHolder> {
    @SuppressLint("StaticFieldLeak")
    static FirebaseFirestore db = FirebaseFirestore.getInstance();
    static CollectionReference addRequest = db.collection("Request");

    public PatRequestAdapter(@NonNull FirestoreRecyclerOptions<Request> options) {
        super(options);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull final PatRequestHolder RequestHolder, final int i, @NonNull final Request request) {
        final TextView t = RequestHolder.title ;
        final String idPat = request.getId_patient();
        final String idDoc = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();
        final String HourPath = request.getHour_path();

        assert idDoc != null;
        db.collection("Doctor").document(idDoc).get().addOnSuccessListener(documentSnapshot -> {
            final Doctor onligneDoc = documentSnapshot.toObject(Doctor.class);
            db.collection("Patient").document(idPat).get().addOnSuccessListener(documentSnapshot12 -> {
                final Patient pat= documentSnapshot12.toObject(Patient.class);
                assert pat != null;
                RequestHolder.title.setText(pat.getName());
                RequestHolder.specialise.setText("Want to be your patient");
                RequestHolder.addDoc.setOnClickListener(v -> {
                    assert onligneDoc != null;
                    db.collection("Patient").document(idPat).collection("MyDoctors").document(idDoc).set(onligneDoc);
                    db.collection("Doctor").document(idDoc+"").collection("MyPatients").document(idPat).set(pat);
                    addRequest.whereEqualTo("id_doctor",idDoc+"").whereEqualTo("id_patient",idPat+"").get().addOnSuccessListener(queryDocumentSnapshots -> {
                        for (QueryDocumentSnapshot documentSnapshot1 : queryDocumentSnapshots){
                            addRequest.document(documentSnapshot1.getId()).delete();

                        }
                    });
                    db.document(HourPath).update("chosen","true");
                    Snackbar.make(t, "Patient added", Snackbar.LENGTH_SHORT).show();
                    RequestHolder.addDoc.setVisibility(View.INVISIBLE);

                });
            });
        });
    }

    public void deleteItem(int position) {
        String hour =getSnapshots().getSnapshot(position).getString("hour_path");
        assert hour != null;
        db.document(hour).delete();
        getSnapshots().getSnapshot(position).getReference().delete();
    }

    @NonNull
    @Override
    public PatRequestHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pat_request_item,
                parent, false);
        return new PatRequestHolder(v);
    }

    static class PatRequestHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView specialise;
        ImageView image;
        Button addDoc;
        public PatRequestHolder(@NonNull View itemView) {
            super(itemView);
            addDoc = itemView.findViewById(R.id.pat_request_accept_btn);
            title= itemView.findViewById(R.id.pat_request_title);
            specialise =itemView.findViewById(R.id.pat_request_description);
            image=itemView.findViewById(R.id.pat_request_item_image);

        }
    }
}
