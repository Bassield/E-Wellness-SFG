package com.sfg.EWellnessSFG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.sfg.EWellnessSFG.fireStoreApi.DoctorHelper;
import com.sfg.EWellnessSFG.fireStoreApi.PatientHelper;
import com.sfg.EWellnessSFG.fireStoreApi.UserHelper;

import static android.widget.AdapterView.*;

import EWellnessSFG.R;

public class FirstSigningActivity extends AppCompatActivity {
    private static final String TAG = "FirstSigningActivity";
    private EditText fullName;
    private EditText birthday;
    private EditText tel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_signin);
        Button btn = (Button) findViewById(R.id.confirmeBtn);
        fullName = (EditText) findViewById(R.id.firstSignFullName);
        birthday = (EditText) findViewById(R.id.firstSignBirthDay);
        tel = (EditText) findViewById(R.id.firstSignTel);

        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.planets_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        final Spinner specialistList = (Spinner) findViewById(R.id.specialite_spinner);
        ArrayAdapter<CharSequence> adapterSpecialistList = ArrayAdapter.createFromResource(this,
                R.array.specialist_spinner, android.R.layout.simple_spinner_item);
        adapterSpecialistList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        specialistList.setAdapter(adapterSpecialistList);
        String newAccountType = spinner.getSelectedItem().toString();

        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = spinner.getSelectedItem().toString();
                Log.e(TAG, "onItemSelected:" + selected);
                if (selected.equals("Doctor")) {
                    specialistList.setVisibility(View.VISIBLE);
                } else {
                    specialistList.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                specialistList.setVisibility(View.GONE);
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fullName, birtDay, tel, type, specialist;
                fullName = FirstSigningActivity.this.fullName.getText().toString();
                birtDay = birthday.getText().toString();
                tel = FirstSigningActivity.this.tel.getText().toString();
                type = spinner.getSelectedItem().toString();
                specialist = specialistList.getSelectedItem().toString();
                UserHelper.addUser(fullName, birtDay, tel, type);
                if (type.equals("Patient")) {
                    PatientHelper.addPatient(fullName, "address", tel, birtDay);
                    System.out.println("Add patient " + fullName + " to patient collection");

                } else {
                    DoctorHelper.addDoctor(fullName, "address", tel, specialist);
                }
                Intent k = new Intent(FirstSigningActivity.this, MainActivity.class);
                startActivity(k);
            }

        });
    }

}
