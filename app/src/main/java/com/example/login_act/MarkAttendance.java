package com.example.login_act;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.util.Log;
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

import java.io.UnsupportedEncodingException;
import java.util.Calendar;

public class MarkAttendance extends AppCompatActivity {

    TextView subjectCodeEditText;
    Button markButton;
    FirebaseDatabase database;
    private NfcAdapter nfcAdapter;
    private Context context;
    private PendingIntent pendingIntent;
    private IntentFilter writingTagFilters[];
    DatabaseReference usersReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_attendance);

        // Initialize EditText and Button
        subjectCodeEditText = (TextView)findViewById(R.id.markSubjectCode);
        markButton = findViewById(R.id.markbtn);
        context = this;

        SharedPreferences sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String stringValue = sharedPref.getString("STRING_KEY", "error");

        subjectCodeEditText.setText(stringValue);
        // Initialize Firebase Database reference
        database = FirebaseDatabase.getInstance();
        usersReference = database.getReference().child("Users");


        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (nfcAdapter == null || !nfcAdapter.isEnabled()) {
            Toast.makeText(context, "NFC is not available or not enabled.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Set onClickListener for the markButton
        markButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Fetching Android ID and storing it into a constant
                final String deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);





                // Get the subject code entered by the user
//                final String subjectCode = subjectCodeEditText.getText().toString().trim();

                final String subjectCode = subjectCodeEditText.getText().toString().trim();


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
                                    updateUserAttendance(snapshot.getRef(), user, subjectCode);
                                    return;
                                }
                            }
                        }
                        // Device ID not found in database or user not found
                        Toast.makeText(MarkAttendance.this, "Device not registered or user not found", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle database error if any
                        Toast.makeText(MarkAttendance.this, "Database error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        nfcAdapter = NfcAdapter.getDefaultAdapter(context);
        if (nfcAdapter == null) {
            // Toast.makeText(context, "Your device does not support NFC", Toast.LENGTH_SHORT).show();
            //finish();
            subjectCodeEditText.setText("Your device does not support NFC");
        }
        readFromIntent(getIntent());
        pendingIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
                PendingIntent.FLAG_IMMUTABLE);
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        tagDetected.addCategory(Intent.CATEGORY_DEFAULT);
        writingTagFilters = new IntentFilter[]{tagDetected};
    }

    private void updateUserAttendance(DatabaseReference userRef, User user, String subjectCode) {
        // Update attendance count for the subject code
        Calendar calendar = Calendar.getInstance();
        long CurTime = calendar.getTimeInMillis();
        long latAccess = user.getLatestAccess();
        int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY); // 24-hour format
        switch (subjectCode) {
            case "CS321":
                // Check timestamp range here
                if ((hourOfDay == 9) ) {
                    if(((CurTime - latAccess)>600000)){
                        user.setCS321(user.getCS321() + 1);
                        user.setlatestAccess(CurTime);
                        // Update the user's data in the database
                        userRef.setValue(user);
                        Toast.makeText(MarkAttendance.this, "Attendance marked successfully", Toast.LENGTH_SHORT).show();}
                    else{
                        Toast.makeText(MarkAttendance.this, "Attendance marked Already for this session", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MarkAttendance.this, "The Class isn't Scheduled at the moment", Toast.LENGTH_SHORT).show();
                }
                break;
            case "CS402":
                // Check timestamp range here
                if ((hourOfDay == 10)) {
                    if(((CurTime - latAccess)>600000)){
                        user.setCS402(user.getCS402() + 1);
                        user.setlatestAccess(CurTime);
                        // Update the user's data in the database
                        userRef.setValue(user);
                        Toast.makeText(MarkAttendance.this, "Attendance marked successfully", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(MarkAttendance.this, "Attendance marked Already for this session", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MarkAttendance.this, "The Class isn't Scheduled at the moment", Toast.LENGTH_SHORT).show();
                }
                break;
            case "CS324":
                if ((hourOfDay == 11)) {
                    if(((CurTime - latAccess)>600000)){
                        user.setCS324(user.getCS324() + 1);
                        user.setlatestAccess(CurTime);
                        // Update the user's data in the database
                        userRef.setValue(user);
                        Toast.makeText(MarkAttendance.this, "Attendance marked successfully", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(MarkAttendance.this, "Attendance marked Already for this session", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MarkAttendance.this, "The Class isn't Scheduled at the moment", Toast.LENGTH_SHORT).show();
                }
                break;
            case "CMP608":
                if ((hourOfDay == 12) ) {
                    if(((CurTime - latAccess)>600000)){
                        user.setCMP608(user.getCMP608() + 1);
                        user.setlatestAccess(CurTime);
                        // Update the user's data in the database
                        userRef.setValue(user);
                        Toast.makeText(MarkAttendance.this, "Attendance marked successfully", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(MarkAttendance.this, "Attendance marked Already for this session", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MarkAttendance.this, "The Class isn't Scheduled at the moment", Toast.LENGTH_SHORT).show();
                }
                break;
            case "CMP618":
                if ((hourOfDay == 11) ) {
                    if(((CurTime - latAccess)>600000)){
                        user.setCMP618(user.getCMP618() + 1);
                        user.setlatestAccess(CurTime);
                        // Update the user's data in the database
                        userRef.setValue(user);
                        Toast.makeText(MarkAttendance.this, "Attendance marked successfully", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(MarkAttendance.this, "Attendance marked Already for this session", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MarkAttendance.this, "The Class isn't Scheduled at the moment", Toast.LENGTH_SHORT).show();
                }
                break;
            case "PS315":
                if ((hourOfDay == 13)  ) {
                    if(((CurTime - latAccess)>600000)){
                        user.setPS315(user.getPS315() + 1);
                        user.setlatestAccess(CurTime);
                        // Update the user's data in the database
                        userRef.setValue(user);
                        Toast.makeText(MarkAttendance.this, "Attendance marked successfully", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(MarkAttendance.this, "Attendance marked Already for this session", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MarkAttendance.this, "The Class isn't Scheduled at the moment", Toast.LENGTH_SHORT).show();
                }
                break;
            // Add more cases for additional subject codes if needed
            default:
                // Subject code not recognized
                Toast.makeText(MarkAttendance.this, "Invalid subject code", Toast.LENGTH_SHORT).show();
                return;
        }



        startActivity(new Intent(MarkAttendance.this, menu_activity.class));
        finish();


    }


    private void readFromIntent(Intent intent) {

        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {

            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] msgs = null;
            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
            }
            buildTagViews(msgs);
        }
    }

    private void buildTagViews(NdefMessage[] msgs) {

        if (msgs == null || msgs.length == 0) return;
        String text = "";
        byte[] payload = msgs[0].getRecords()[0].getPayload();
        String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";
        int languageCodeLength = payload[0] & 0063;
        try {
            text = new String(payload,
                    languageCodeLength + 1,
                    payload.length - languageCodeLength - 1,
                    textEncoding);
        } catch (UnsupportedEncodingException e) {
            Log.e("UnsupportedEncoding", e.getMessage());
        }
        subjectCodeEditText.setText("NFC Content : " + text);
    }




}
