package com.example.login_act;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Sign_up extends AppCompatActivity {

    TextView MarkScreen;
    EditText emailEditText, nameEditText, rollNoEditText;
    Button signUpButton;

    FirebaseDatabase database;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize EditText and Button
        emailEditText = findViewById(R.id.signEmail);
        nameEditText = findViewById(R.id.signName);
        rollNoEditText = findViewById(R.id.signRollNo);
        signUpButton = findViewById(R.id.signUpBtn);
        MarkScreen= findViewById(R.id.toLoginScreen);

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");

        // Set onClickListener for the signUpButton
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the values from EditText fields
                final String email = emailEditText.getText().toString().trim();
                final String name = nameEditText.getText().toString().trim();
                final String rollNo = rollNoEditText.getText().toString().trim();

                // Fetching Android ID and storing it into a constant
                final String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

                // Check if any field is empty
                if (email.isEmpty() || name.isEmpty() || rollNo.isEmpty()) {
                    Toast.makeText(Sign_up.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Query to check if the device ID or email already exists in Firebase
                databaseReference.orderByChild("deviceId").equalTo(deviceId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // Device ID already exists, show toast message
                            Toast.makeText(Sign_up.this, "You are already registered", Toast.LENGTH_SHORT).show();
                        } else {
                            // Check if the email already exists
                            databaseReference.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        // Email already exists, show toast message
                                        Toast.makeText(Sign_up.this, "Email already registered", Toast.LENGTH_SHORT).show();
                                    } else {
                                        // Create a new User object with device ID as password
                                        User user = new User(email, name, rollNo, deviceId);

                                        // Push the user object to Firebase database
                                        databaseReference.push().setValue(user);

                                        // Show success message
                                        Toast.makeText(Sign_up.this, "You have been registered", Toast.LENGTH_SHORT).show();

                                        // Navigate to MainActivity
                                        startActivity(new Intent(Sign_up.this, MainActivity.class));
                                        finish(); // Close the current activity
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    // Handle database error if any
                                    Toast.makeText(Sign_up.this, "Database error", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle database error if any
                        Toast.makeText(Sign_up.this, "Database error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        MarkScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i= new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });
    }
}
