package com.example.android.meme;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.VideoView;

public class SplashActivity extends Activity {

    private static int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try{
                    VideoView videoView = new VideoView(getApplicationContext());
                    setContentView(videoView);
                    Uri path = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.meme_intro);
                    videoView.setVideoURI(path);
                    videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mediaPlayer) {
                            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                            finish();
                        }
                    });
                    videoView.start();
                }
                catch (Exception e)
                {
                    startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                    finish();
                }
            }
        }, SPLASH_TIME_OUT);
    }
}
