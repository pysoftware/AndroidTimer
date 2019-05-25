package com.example.cooltimer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {

    CountDownTimer timer;
    MediaPlayer mediaPlayer;

    final int DEFAULT_PROGRESS = 30;

    SeekBar seekBar;
    TextView textView;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.text);
        button = findViewById(R.id.button);
        seekBar = findViewById(R.id.seekBar);

        timeByDefault();

        seekBar.setOnSeekBarChangeListener(this);
        button.setOnClickListener(this);
    }

    @SuppressLint("SetTextI18n")
    public void timeByDefault() {
        seekBar.setProgress(DEFAULT_PROGRESS);
        int minutes = seekBar.getProgress() / 60;
        int seconds = seekBar.getProgress() % 60;
        seekBar.setProgress(DEFAULT_PROGRESS);
        button.setText("start");
        seekBar.setEnabled(true);
        textView.setText(minutes + ":" + seconds);
    }

    public void startTimer() {
        timer = new CountDownTimer(
                seekBar.getProgress() * 1000, 1000) {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long millisUntilFinished) {
                int minutes = (int) millisUntilFinished / 1000 / 60;
                int seconds = (int) millisUntilFinished / 1000 % 60;
                button.setText("stop");
                seekBar.setEnabled(false);
                seekBar.setProgress(minutes + seconds);
                textView.setText(minutes + ":" + seconds);
            }

            @Override
            public void onFinish() {
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                if (sharedPreferences.getBoolean("enable_sound", true)){
                    String melodyName = sharedPreferences.getString("timer_melody", "first");
                    assert melodyName != null;
                    if (melodyName.equals("first")) {
                        mediaPlayer = MediaPlayer.create(getApplicationContext(),
                                R.raw.crank_1);
                        mediaPlayer.start();
                    }
                    else if (melodyName.equals("second")) {
                        mediaPlayer = MediaPlayer.create(getApplicationContext(),
                                R.raw.crank_2);
                        mediaPlayer.start();
                    }
                }
                timeByDefault();
            }
        }.start();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        textView.setText(progress / 60 + ":" + progress % 60);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button) {
            if (button.getText().equals("start")) {
                startTimer();
            } else if (button.getText().equals("stop")) {
                timer.cancel();
                timeByDefault();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.timer_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent openSettings = new Intent(this, SettingsActivity.class);
                startActivity(openSettings);
                return true;
            case R.id.action_about:
                Intent openAbout = new Intent(this, AboutActivity.class);
                startActivity(openAbout);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
