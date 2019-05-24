package com.example.cooltimer;

import android.annotation.SuppressLint;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener, View.OnClickListener {

    CountDownTimer timer;

    final int DEFAULT_PROGRESS = 30;
    private int minutes;
    private int seconds;

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

    public void timeByDefault() {
        seekBar.setProgress(DEFAULT_PROGRESS);
        minutes = seekBar.getProgress() / 60;
        seconds = seekBar.getProgress() % 60;
        seekBar.setProgress(DEFAULT_PROGRESS);
        button.setText("start");
        seekBar.setEnabled(true);
        textView.setText(minutes + ":" + seconds);
    }

    public void startTimer(){
            timer = new CountDownTimer(
                seekBar.getProgress() * 1000, 1000) {
            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long millisUntilFinished) {
                button.setText("stop");
                seekBar.setEnabled(false);
                textView.setText((int)millisUntilFinished / 1000 / 60 + ":" + (int)millisUntilFinished / 1000 % 60);
            }

            @Override
            public void onFinish() {
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
        switch (v.getId()){
            case R.id.button:
                if (button.getText().equals("start")) {
                    startTimer();
                }
                else if (button.getText().equals("stop")){
                    timer.cancel();
                    timeByDefault();
                }
                break;
        }
    }
}
