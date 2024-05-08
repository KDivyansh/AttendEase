package com.example.login_act;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.UnsupportedEncodingException;


public class start_page extends AppCompatActivity {

    ImageView MarkScreen;
    TextView courseCode;

    public static final String ERROR_DETECTED = "No NFC Tag Detected";
    public static final String WRITE_SUCCESS = "Text Written Successfully";
    public static final String WRITE_ERROR = "Error During Writing, Please Try Again";
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    private IntentFilter writingTagFilters[];
    private boolean writeMode;
    private Context context;

    public String CourseId;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);

        courseCode = findViewById(R.id.CourseCode);

        context = this;

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (nfcAdapter == null || !nfcAdapter.isEnabled()) {
            Toast.makeText(context, "NFC is not available or not enabled.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        VideoView videoView = (VideoView) findViewById(R.id.introVideo);
        videoView.setVideoPath("android.resource://" + getPackageName() + "/" + R.raw.minorpro1);

        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(videoView);

        videoView.start();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                videoView.setVisibility(View.INVISIBLE);
            }
        }, 5500);


        nfcAdapter = NfcAdapter.getDefaultAdapter(context);
        if (nfcAdapter == null){
            // Toast.makeText(context, "Your device does not support NFC", Toast.LENGTH_SHORT).show();
            //finish();
            courseCode.setText("Your device does not support NFC");
        }
        readFromIntent(getIntent());
        pendingIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
                PendingIntent.FLAG_IMMUTABLE);
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        tagDetected.addCategory(Intent.CATEGORY_DEFAULT);
        writingTagFilters = new IntentFilter[]{tagDetected};

        SharedPreferences sharedPref = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("STRING_KEY", CourseId);
        editor.apply();


        MarkScreen = findViewById(R.id.toLoginScreen);

        MarkScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i= new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        });
    }
    private void readFromIntent(Intent intent) {

        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {

            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] msgs = null;
            if (rawMsgs != null){
                msgs = new NdefMessage[rawMsgs.length];
                for (int i=0; i<rawMsgs.length; i++){
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
        } catch (UnsupportedEncodingException e){
            Log.e("UnsupportedEncoding", e.getMessage());
        }
        courseCode.setText("NFC Content : " + text);
        CourseId = text;
    }
    @Override
    protected void onPause() {
        super.onPause();
        nfcAdapter.disableForegroundDispatch(this);
    }



    @Override
    protected void onResume() {
        super.onResume();
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, writingTagFilters, null);


    }
}