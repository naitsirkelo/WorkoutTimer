package com.example.timer;

import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    CountDownTimer timer = null;
    ImageView backgroundImg;
    TextView showTimer, showRoundStation;
    Spinner workSpinner, pauseSpinner, roundsSpinner, stationsSpinner;
    int workTime = 1, pauseTime = 1, roundsVal = 1, roundsCount = 0, stationsVal = 1 , stationsCount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showTimer = findViewById(R.id.timer_text);
        showRoundStation = findViewById(R.id.r_s_text);
        backgroundImg = findViewById(R.id.bg);
        sendViewToBack(backgroundImg);
        backgroundImg.setBackgroundColor(Color.parseColor("#88EA6C"));

        if (timer == null) {
            showTimer.setText("0.0");
            showRoundStation.setText("R:0/0 S:0/0");
        }

        Button start = findViewById(R.id.start_btn);
        Button reset = findViewById(R.id.reset_btn);

        Integer[] seconds = new Integer[]{1, 2, 3, 4, 5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55, 60};
        Integer[] rounds = new Integer[]{2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20};
        Integer[] stations = new Integer[]{2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};


        workSpinner = findViewById(R.id.work_spinner);
        ArrayAdapter<Integer> work_adapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, seconds);
        workSpinner.setAdapter(work_adapter);

        pauseSpinner = findViewById(R.id.pause_spinner);
        ArrayAdapter<Integer> pause_adapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, seconds);
        pauseSpinner.setAdapter(pause_adapter);

        roundsSpinner = findViewById(R.id.rounds_spinner);
        ArrayAdapter<Integer> rounds_adapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, rounds);
        roundsSpinner.setAdapter(rounds_adapter);

        stationsSpinner = findViewById(R.id.stations_spinner);
        ArrayAdapter<Integer> stations_adapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, stations);
        stationsSpinner.setAdapter(stations_adapter);


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    workTime = 1000 * Integer.parseInt(workSpinner.getSelectedItem().toString());
                } catch (NumberFormatException nfe) {
                    System.out.println("Could not parse " + nfe + "!");
                }

                try {
                    pauseTime = 1000 * Integer.parseInt(pauseSpinner.getSelectedItem().toString());
                } catch (NumberFormatException nfe) {
                    System.out.println("Could not parse " + nfe + "!");
                }

                try {
                    roundsVal = Integer.parseInt(roundsSpinner.getSelectedItem().toString());
                } catch (NumberFormatException nfe) {
                    System.out.println("Could not parse " + nfe + "!");
                }

                try {
                    stationsVal = Integer.parseInt(stationsSpinner.getSelectedItem().toString());
                } catch (NumberFormatException nfe) {
                    System.out.println("Could not parse " + nfe + "!");
                }

                final int w = workTime, p = pauseTime, r = roundsVal, s = stationsVal;

                for (int i = 0; i < s; i++) {

                    final int ii = i + 1;

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {

                        @Override
                        public void run() {

                            for (int j = 0; j < r; j++) {

                                final int jj = j;

                                final Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {

                                    @Override
                                    public void run() {

                                        int jjj = jj + 1;
                                        String t = "R:" + jjj + "/" + r + " S:" + ii + "/" + s;
                                        showRoundStation.setText(t);

                                        backgroundImg.setBackgroundColor(Color.parseColor("#FF270F"));
                                        startTimer(w);

                                        final Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {

                                            @Override
                                            public void run() {

                                                stationsCount = ii;
                                                roundsCount = jj + 1;
                                                backgroundImg.setBackgroundColor(Color.parseColor("#88EA6C"));
                                                startTimer(p);
                                            }
                                        }, w);
                                    }
                                }, j * (workTime + pauseTime));
                            }
                        }
                    }, roundsVal * i * (workTime + pauseTime));
                }
            }
        });


        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = getIntent();
                overridePendingTransition( 0, 0);
                finish();
                startActivity(intent);
                overridePendingTransition( 0, 0);
            }
        });
    }


    void startTimer(int t) {

        timer = new CountDownTimer(t, 1) {

            public void onTick(long millisUntilFinished) {
                showTimer.setText(millisUntilFinished / 1000 + "." + ((millisUntilFinished / 100) % 10));
            }

            public void onFinish() {
                if (roundsCount == roundsVal && stationsCount == stationsVal) {
                    showTimer.setText("Finished!");
                }
            }
        };
        timer.start();
    }


    public static void sendViewToBack(final View child) {
        final ViewGroup parent = (ViewGroup) child.getParent();
        if (null != parent) {
            parent.removeView(child);
            parent.addView(child, 0);
        }
    }
}
