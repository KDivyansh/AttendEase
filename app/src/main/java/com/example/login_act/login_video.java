package com.example.login_act;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.widget.MediaController;
import android.widget.VideoView;
import android.view.View;

public class login_video extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_video);


        VideoView videoView = (VideoView) findViewById(R.id.introVideo);
        videoView.setVideoPath("android.resource://" + getPackageName() + "/" + R.raw.minorpro);

        MediaController mediaController = new MediaController(this);
        //link mediaController to videoView
        mediaController.setAnchorView(videoView);
        //allow mediaController to control our videoView

        videoView.start();
      final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                videoView.setVisibility(View.INVISIBLE);
            }
        }, 7000);

    }
}