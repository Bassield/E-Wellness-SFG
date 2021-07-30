package com.ensias.healthcareapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import com.ensias.healthcareapp.model.Record;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class RecordActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private EditText disease;
    private EditText description;
    private EditText treatment;
    private Spinner recordType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        disease = findViewById(R.id.record_disease);
        description = findViewById(R.id.record_description);
        treatment = findViewById(R.id.record_treatment);
        recordType = findViewById(R.id.record_type_spinner);

        //Spinner to choose fiche type
        @SuppressLint("CutPasteId") Spinner spinner = findViewById(R.id.record_type_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.record_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        //Add fiche
        Button addRecordButton = findViewById(R.id.button_add_record);
        addRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addRecord();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String SelectedRecordType = adapterView.getItemAtPosition(i).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void addRecord() {
        String diseaseRecord = disease.getText().toString();
        String descriptionRecord = description.getText().toString();
        String treatmentRecord = treatment.getText().toString();
        String typeRecord = recordType.getSelectedItem().toString();

        String patient_name = getIntent().getStringExtra("patient_name");
        String patient_email = getIntent().getStringExtra("patient_email");


        CollectionReference ficheRef = FirebaseFirestore.getInstance().collection("Patient").document("" + patient_email + "")
                .collection("MyMedicalFolder");
        ficheRef.document().set(new Record(diseaseRecord, descriptionRecord, treatmentRecord, typeRecord, Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail()));
        //ficheRef.add(new Fiche(diseaseRecord, descriptionRecord, treatmentRecord, typeRecord, FirebaseAuth.getInstance().getCurrentUser().getEmail().toString()));
        Toast.makeText(this, "Record added." + patient_name, Toast.LENGTH_LONG).show();
        finish();
    }

}
