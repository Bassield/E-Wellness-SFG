package com.sfg.EWellnessSFG.fireStoreApi;

import android.annotation.SuppressLint;

import com.sfg.EWellnessSFG.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class UserHelper {
    @SuppressLint("StaticFieldLeak")
    static FirebaseFirestore db = FirebaseFirestore.getInstance();
    static CollectionReference UsersRef = db.collection("User");

    public static void addUser(String name, String address, String tel,String type){
        User user = new User(name,address,tel, Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail(),type);
        UsersRef.document(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser().getEmail())).set(user);

    }
}
