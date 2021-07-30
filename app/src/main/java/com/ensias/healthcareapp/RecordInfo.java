package com.ensias.healthcareapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class RecordInfo extends AppCompatActivity {
    TextView t1, t2, t3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_info);
        t1 = findViewById(R.id.textView2);
        t2 = findViewById(R.id.textView4);
        t3 = findViewById(R.id.textView5);

        t1.setText(String.format("Date of creation%s", getIntent().getStringExtra("dateCreated")));
        t2.setText(getIntent().getStringExtra("doctor"));
        t3.setText(getIntent().getStringExtra("description"));
    }
}
