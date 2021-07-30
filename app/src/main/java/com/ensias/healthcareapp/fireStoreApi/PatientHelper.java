package com.ensias.healthcareapp.fireStoreApi;

import android.annotation.SuppressLint;

import com.ensias.healthcareapp.model.Patient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class PatientHelper {
    @SuppressLint("StaticFieldLeak")
    static FirebaseFirestore db = FirebaseFirestore.getInstance();
    static CollectionReference PatientRef = db.collection("Patient");

    public static void addPatient(String name, String address, String tel){
        Patient patient = new Patient(name,address,tel, Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail(),"aaa", "aaa");
        System.out.println("Create object patient");
        PatientRef.document(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser().getEmail())).set(patient);
    }
}
