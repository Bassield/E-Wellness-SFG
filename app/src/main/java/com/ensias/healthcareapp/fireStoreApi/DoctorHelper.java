package com.ensias.healthcareapp.fireStoreApi;

import android.annotation.SuppressLint;

import com.ensias.healthcareapp.model.Doctor;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class DoctorHelper {
    @SuppressLint("StaticFieldLeak")
    static FirebaseFirestore db = FirebaseFirestore.getInstance();
    static CollectionReference DoctorRef = db.collection("Doctor");

    public static void addDoctor(String name, String address, String tel,String speciality){
        Doctor doctor = new Doctor(name,address,tel, Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail(),speciality);

        DoctorRef.document(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser().getEmail())).set(doctor);

    }
}
