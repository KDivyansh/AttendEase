package com.example.login_act;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class ViewAttendance extends AppCompatActivity {

    Button BackButton;

    TextView Sname;
    TextView EnId;
    TextView sub1;
    TextView sub2;
    TextView sub3;
    TextView sub4;
    TextView sub5;
    TextView sub6;

    FirebaseDatabase database;
    DatabaseReference usersReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_attendance);

        Sname = findViewById(R.id.Sname);
        EnId = findViewById(R.id.EnId);
        sub1 = findViewById(R.id.sub1);
        sub2 = findViewById(R.id.sub2);
        sub3 = findViewById(R.id.sub3);
        sub4 = findViewById(R.id.sub4);
        sub5 = findViewById(R.id.sub5);
        sub6 = findViewById(R.id.sub6);

        database = FirebaseDatabase.getInstance();
        usersReference = database.getReference().child("Users");
        final String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);

        // Query to check if the device ID exists in Firebase
        usersReference.orderByChild("deviceId").equalTo(deviceId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        // Get the user corresponding to the device ID
                        User user = snapshot.getValue(User.class);
                        if (user != null) {

                            // Update the attendance count for the subject code
                            int cmp608 = user.getCMP608();
                            int cmp618 = user.getCMP618();
                            int cs321 = user.getCS321();
                            int cs324 = user.getCS324();
                            int cs402 = user.getCS402();
                            int ps315 = user.getPS315();
                            String sname = user.getName();
                            String enid = user.getRollNo();


                            sub1.setText(String.valueOf(cmp608) + "/45");
                            sub2.setText(String.valueOf(cmp618) + "/45");
                            sub3.setText(String.valueOf(cs321) + "/30");
                            sub4.setText(String.valueOf(cs324) + "/45");
                            sub5.setText(String.valueOf(cs402) + "/45");
                            sub6.setText(String.valueOf(ps315) + "/30");
                            Sname.setText(sname);
                            EnId.setText(enid);


                            return;
                        }
                    }
                }
                // Device ID not found in database or user not found
                Toast.makeText(ViewAttendance.this, "Device not registered or user not found", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error if any
                Toast.makeText(ViewAttendance.this, "Database error", Toast.LENGTH_SHORT).show();
            }
        });




        //buttonclick to go back
        BackButton = findViewById(R.id.backButton);
        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ViewAttendance.this, menu_activity.class);
                startActivity(intent);
            }
        });
    }
}