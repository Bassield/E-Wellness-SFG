package com.sfg.EWellnessSFG;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sfg.EWellnessSFG.model.Hour;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

import EWellnessSFG.R;

public class AppointmentActivity extends AppCompatActivity {

    //final List<String> fruits_list = new ArrayList<String>(Arrays.asList(fruits));
    static FirebaseFirestore db = FirebaseFirestore.getInstance();

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointement);
        LinearLayout lis = findViewById(R.id.listDate);
        String patient_email = getIntent().getStringExtra("key1");
        String day = getIntent().getStringExtra("key2");
        assert patient_email != null;
        assert day != null;
        final CollectionReference addRequest = db.collection("Doctor").document(patient_email).collection("calendar").document(day).collection("hour");


        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(140, 398);
        layoutParams.setMargins(200, 0, 300, 0);

        for (int i = 8; i<19;i++){
            final int j = i;
            TextView text = new TextView(this);
            text.setText(i + ":00");
            LinearLayout l = new LinearLayout(this);
            l.setMinimumHeight(250);
            l.addView(text, layoutParams);
            final Button btn = new Button(this);
            addRequest.document(i+"").get().addOnSuccessListener(documentSnapshot -> {
                Hour note = documentSnapshot.toObject(Hour.class);
                if(note != null){
                    btn.setText("already chosen");
                }
                else{
                    btn.setText("confirmed this hour");
                    btn.setOnClickListener(v -> {
                        Hour h =new Hour(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail());
                        addRequest.document(j+"").set(h);
                    });
                }

            });

            l.addView(btn);
            lis.addView(l);
        }

    }


}