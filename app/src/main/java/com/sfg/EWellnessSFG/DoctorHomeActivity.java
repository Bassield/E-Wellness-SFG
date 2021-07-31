package com.sfg.EWellnessSFG;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.sfg.EWellnessSFG.Common.Common;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;
import java.util.Objects;

import EWellnessSFG.R;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class DoctorHomeActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    static String doc;
    Button SignOutBtn2;
    Button BtnRequest;
    Button listPatients;
    Button appointmentBtn;

    @OnClick(R.id.profile)
    void profileBtnClick() {
        Intent k = new Intent(DoctorHomeActivity.this, ProfileDoctorActivity.class);
        startActivity(k);
    }

    Unbinder unbinder;

    @OnClick(R.id.myCalendarBtn)
    void myCalendarOnclick() {
        Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();
        Intent k = new Intent(DoctorHomeActivity.this, MyCalendarDoctorActivity.class);
        startActivity(k);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_home); //layout of PHYSICIAN home page
        unbinder = ButterKnife.bind(this, this);
        Common.CurrentDoctor = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail();
        Common.CurrentUserType = "doctor";
        listPatients = findViewById(R.id.listPatients);
        BtnRequest = findViewById(R.id.btnRequest);
        SignOutBtn2 = findViewById(R.id.signOutBtn);
        appointmentBtn = findViewById(R.id.appointment);
        SignOutBtn2.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
        BtnRequest.setOnClickListener(v -> {
            Intent k = new Intent(DoctorHomeActivity.this, ConfirmedAppointmentActivity.class);
            startActivity(k);
        });
        listPatients.setOnClickListener(v -> {
            Intent k = new Intent(DoctorHomeActivity.this, MyPatientsActivity.class);
            startActivity(k);
        });
        appointmentBtn.setOnClickListener(v -> {
            // doc = FirebaseAuth.getInstance().getCurrentUser().getEmail().toString();
            //showDatePickerDialog(v.getContext());
            Intent k = new Intent(DoctorHomeActivity.this, DoctorAppointmentActivity.class);
            startActivity(k);
        });

    }

    public void showDatePickerDialog(Context wf) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                wf,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = "month_day_year: " + month + "_" + dayOfMonth + "_" + year;
        openPage(view.getContext(), doc, date);
    }

    private void openPage(Context wf, String d, String day) {
        Intent i = new Intent(wf, AppointmentActivity.class);
        i.putExtra("key1", d + "");
        i.putExtra("key2", day);
        i.putExtra("key3", "doctor");
        wf.startActivity(i);
    }
}
