package com.example.cooltimer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements
        SeekBar.OnSeekBarChangeListener, View.OnClickListener,
        SharedPreferences.OnSharedPreferenceChangeListener {

    CountDownTimer timer;
    MediaPlayer mediaPlayer;

    SharedPreferences sharedPreferences;

    SeekBar seekBar;
    TextView textView;
    Button button;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        textView = findViewById(R.id.text);
        button = findViewById(R.id.button);
        seekBar = findViewById(R.id.seekBar);

        timeByDefault();

        seekBar.setOnSeekBarChangeListener(this);
        button.setOnClickListener(this);

        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("SetTextI18n")
    public void timeByDefault() {
        setIntervalFromSharedPreference(sharedPreferences);
        int minutes = seekBar.getProgress() / 60;
        int seconds = seekBar.getProgress() % 60;
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

            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("SetTextI18n")
    private void setIntervalFromSharedPreference(SharedPreferences sharedPreference) {
        int defaultInterval = Integer.valueOf(
                Objects.requireNonNull(
                        sharedPreference.getString("timer default interval", "30")));
        textView.setText("0:" + defaultInterval);
        seekBar.setProgress(defaultInterval);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("timer default interval")){
            setIntervalFromSharedPreference(sharedPreferences);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }
}
