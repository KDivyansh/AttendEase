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

public class MainActivity extends AppCompatActivity {

    TextView Signup;
    EditText emailEditText;
    Button loginButton;

    FirebaseDatabase database;
    DatabaseReference usersReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Signup = findViewById(R.id.toSignUpScreen);

        // Initialize EditText and Button
        emailEditText = findViewById(R.id.loginEmail);
        loginButton = findViewById(R.id.loginbtn);

        // Initialize Firebase Database reference
        database = FirebaseDatabase.getInstance();
        usersReference = database.getReference().child("Users");

        // Set onClickListener for the loginButton
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Fetching Android ID and storing it into a constant
                final String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

                // Get the email entered by the user
                final String email = emailEditText.getText().toString().trim();

                // Check if email field is empty
                if (email.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Query to check if the device ID exists in Firebase
                usersReference.orderByChild("deviceId").equalTo(deviceId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // Device ID exists, now check if the email matches
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                User user = snapshot.getValue(User.class);
                                if (user != null && user.getEmail().equals(email)) {
                                    // Email matches, redirect to MarkAttendance activity
                                    startActivity(new Intent(MainActivity.this, menu_activity.class));
                                    finish(); // Close the current activity
                                    return;
                                }
                            }
                            // Email does not match
                            Toast.makeText(MainActivity.this, "Incorrect email", Toast.LENGTH_SHORT).show();
                        } else {
                            // Device ID not found in database
                            Toast.makeText(MainActivity.this, "Device not registered", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle database error if any
                        Toast.makeText(MainActivity.this, "Database error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

       Signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i= new Intent(getApplicationContext(), Sign_up.class);
                startActivity(i);
            }
        });


    }
}