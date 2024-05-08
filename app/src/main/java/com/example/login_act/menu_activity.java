package com.example.login_act;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class menu_activity extends AppCompatActivity {

    Button toMarkAttendance, toViewAttendance, toSharedDocs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Initialize buttons
        toMarkAttendance = findViewById(R.id.toMarkAttendance);
        toViewAttendance = findViewById(R.id.toviewAttendance);
        toSharedDocs = findViewById(R.id.toSharedDocs);

        // Click listeners
        toMarkAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(menu_activity.this, MarkAttendance.class);
                startActivity(intent);
            }
        });

        toViewAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(menu_activity.this, ViewAttendance.class);
                startActivity(intent);
            }
        });

        toSharedDocs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(menu_activity.this, SharedDocs.class);
                startActivity(intent);
            }
        });
    }
}
