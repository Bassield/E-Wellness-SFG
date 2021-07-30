package com.sfg.EWellnessSFG;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.sfg.EWellnessSFG.Common.Common;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Objects;

import static com.sfg.EWellnessSFG.Common.Common.convertBloodToInt;

import EWellnessSFG.R;

public class PatientInfoActivity extends AppCompatActivity {

    EditText heightBtn;
    EditText weightBtn;
    Spinner bloodTypeSpinner;
    Button updateBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_info);
        updateBtn = findViewById(R.id.updateInfoBtn);
        heightBtn = findViewById(R.id.heightBtn);
        weightBtn = findViewById(R.id.weightBtn);
        final Spinner specialistList = (Spinner) findViewById(R.id.bloodType);
        ArrayAdapter<CharSequence> adapterSpecialistList = ArrayAdapter.createFromResource(this,
                R.array.blood_spinner, android.R.layout.simple_spinner_item);
        adapterSpecialistList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        specialistList.setAdapter(adapterSpecialistList);

        String patient_name = getIntent().getStringExtra("patient_name");
        String patient_email = getIntent().getStringExtra("patient_email");

        assert patient_email != null;
        FirebaseFirestore.getInstance().collection("Patient").document(patient_email).collection("moreInfo")
                .document(patient_email).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                weightBtn.setText(new StringBuilder().append("").append(documentSnapshot.getString("weight")).toString());
                heightBtn.setText(new StringBuilder().append("").append(documentSnapshot.getString("height")).toString());
                if(documentSnapshot.getString("bloodType") != null)
                specialistList.setSelection(convertBloodToInt(Objects.requireNonNull(documentSnapshot.getString("bloodType"))));
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String,Object> map = new HashMap<>();
                map.put("height",""+heightBtn.getText());
                map.put("weight",""+weightBtn.getText());
                map.put("bloodType",""+specialistList.getSelectedItem().toString());
                Log.e("tag", "onClick: "+specialistList.getTag() );
                FirebaseFirestore.getInstance().collection("Patient").document(patient_email).collection("moreInfo")
                        .document(patient_email).set(map);
                Toast.makeText(PatientInfoActivity.this,"Update Success!",Toast.LENGTH_SHORT).show();

            }
        });
       if(Common.CurrentUserType.equals("patient")){
            updateBtn.setVisibility(View.GONE);
            heightBtn.setEnabled(false);
            weightBtn.setEnabled(false);
            specialistList.setEnabled(false);
        }
    }

}
