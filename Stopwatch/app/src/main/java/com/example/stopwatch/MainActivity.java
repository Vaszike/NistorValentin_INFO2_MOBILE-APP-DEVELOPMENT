package com.example.stopwatch;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import java.util.Locale;


import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView textViewTimer;
    private Button buttonStartStop, buttonLap, buttonReset;
    private ListView listViewTimes;

    private long startTime = 0;
    private boolean running = false;
    private Handler handler = new Handler();
    private long timeInMillis = 0;
    private ArrayList<String> lapTimes = new ArrayList<>();
    private ArrayAdapter<String> lapTimesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewTimer = findViewById(R.id.textViewTimer);
        buttonStartStop = findViewById(R.id.buttonStartStop);
        buttonLap = findViewById(R.id.buttonLap);
        buttonReset = findViewById(R.id.buttonReset);
        listViewTimes = findViewById(R.id.listViewTimes);

        lapTimesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, lapTimes);
        listViewTimes.setAdapter(lapTimesAdapter);

    }

    private Runnable updateTimer = new Runnable() {
        @Override
        public void run() {
            if (running) {
                timeInMillis = System.currentTimeMillis() - startTime;
                updateTimerText();
                handler.postDelayed(this, 1000);
            }
        }
    };

    private void updateTimerText() {
        long hours = (timeInMillis / (1000 * 60 * 60)) % 24;
        long minutes = (timeInMillis / (1000 * 60)) % 60;
        long seconds = (timeInMillis / 1000) % 60;

        String time = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, seconds);
        textViewTimer.setText(time);
    }

    public void onStartStopClick(View view) {
        if (running) {
            running = false;
            buttonStartStop.setText("Start");
            buttonLap.setEnabled(false);
            buttonReset.setVisibility(View.VISIBLE);
        } else {
            startTime = System.currentTimeMillis() - timeInMillis;
            handler.post(updateTimer);
            running = true;
            buttonStartStop.setText("Stop");
            buttonLap.setEnabled(true);
            buttonReset.setVisibility(View.GONE);
        }
    }

    public void onLapClick(View view) {
        if (running) {
            String currentLapTime = textViewTimer.getText().toString();
            lapTimes.add(currentLapTime);
            lapTimesAdapter.notifyDataSetChanged();
        }
    }

    public void onResetClick(View view) {
        timeInMillis = 0;
        updateTimerText();
        lapTimes.clear();
        lapTimesAdapter.notifyDataSetChanged();
        buttonStartStop.setText("Start");
        buttonLap.setEnabled(false);
        buttonReset.setVisibility(View.GONE);
    }
}
