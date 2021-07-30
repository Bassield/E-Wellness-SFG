package com.sfg.EWellnessSFG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sfg.EWellnessSFG.Common.Common;
import com.sfg.EWellnessSFG.model.Doctor;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import EWellnessSFG.R;

public class DoctorAdapterFiltered extends RecyclerView.Adapter<DoctorAdapterFiltered.DoctorHolder2> implements Filterable {
    public static boolean specialistSearch = false;
    static String doc;
    @SuppressLint("StaticFieldLeak")
    static FirebaseFirestore db = FirebaseFirestore.getInstance();
    static CollectionReference addRequest = db.collection("Request");
    private final List<Doctor> mTubeList;
    private List<Doctor> mTubeListFiltered;
    StorageReference pathReference ;


    public DoctorAdapterFiltered(List<Doctor> tubeList){
        mTubeList = tubeList;
        mTubeListFiltered = tubeList;
    }

    @NonNull
    @Override
    public DoctorHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctor_item,
                parent, false);
        return new DoctorHolder2(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorHolder2 doctorHolder, int i) {
        final Doctor doctor = mTubeListFiltered.get(i);
        final TextView t = doctorHolder.title ;
        doctorHolder.title.setText(doctor.getName());
        ///add image

        String imageId = doctor.getEmail()+".jpg";
        pathReference = FirebaseStorage.getInstance().getReference().child("DoctorProfile/"+ imageId);
        pathReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(doctorHolder.image.getContext())
                        .load(uri)
                        .placeholder(R.mipmap.ic_launcher)
                        .fit()
                        .centerCrop()
                        .into(doctorHolder.image);//Image location

                // profileImage.setImageURI(uri);
            }
        }).addOnFailureListener(exception -> {
            // Handle any errors
        });
        doctorHolder.specialist.setText(new StringBuilder().append("Specialist : ").append(doctor.getSpecialist()).toString());
        final String idPat = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail().toString();
        final String idDoc = doctor.getEmail();
        // doctorHolder.image.setImageURI(Uri.parse("drawable-v24/ic_launcher_foreground.xml"));
        doctorHolder.addDoc.setOnClickListener(v -> {
            Map<String, Object> note = new HashMap<>();
            note.put("id_patient", idPat);
            note.put("id_doctor", idDoc);
            addRequest.document().set(note)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Snackbar.make(t, "Request sent", Snackbar.LENGTH_SHORT).show();
                        }
                    });
            doctorHolder.addDoc.setVisibility(View.INVISIBLE);
        });
        doctorHolder.appointmentBtn.setOnClickListener(v -> {
            doc= doctor.getEmail();
            Common.CurrentDoctor = doctor.getEmail();
            Common.CurrentDoctorName = doctor.getName();
            Common.CurrentPhone = doctor.getTel();
            openPage(v.getContext());

        });

    }

    @Override
    public int getItemCount() {
        return mTubeListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String pattern = constraint.toString().toLowerCase();
                if(pattern.isEmpty()){
                    mTubeListFiltered = mTubeList;
                } else {
                    List<Doctor> filteredList = new ArrayList<>();
                    for(Doctor tube: mTubeList){
                        if(!specialistSearch) {
                            if (tube.getName().toLowerCase().contains(pattern) || tube.getName().toLowerCase().contains(pattern)) {
                                filteredList.add(tube);
                            }
                        }
                        else{
                            if (tube.getSpecialist().toLowerCase().contains(pattern) || tube.getSpecialist().toLowerCase().contains(pattern)) {
                                filteredList.add(tube);
                            }
                        }
                    }
                    mTubeListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mTubeListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mTubeListFiltered = (ArrayList<Doctor>) results.values;
                notifyDataSetChanged();
            }
        };
    }
    static class DoctorHolder2 extends RecyclerView.ViewHolder {

        Button appointmentBtn;
        TextView title;
        TextView specialist;
        ImageView image;
        Button addDoc;
        Button load;
        public DoctorHolder2(@NonNull View itemView) {
            super(itemView);
            addDoc = itemView.findViewById(R.id.addDocBtn);
            title= itemView.findViewById(R.id.doctor_view_title);
            specialist =itemView.findViewById(R.id.text_view_description);
            image=itemView.findViewById(R.id.doctor_item_image);
            appointmentBtn =itemView.findViewById(R.id.appointemenBtn);
        }
    }
    private void openPage(Context wf){
        Intent i = new Intent(wf, TestActivity.class);
        wf.startActivity(i);
    }
}
