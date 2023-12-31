package com.example.runningapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    //VARIABLES - BUTTONS etc.
    public Button startButton;
    public Button stopButton;
    public Button resetButton;
    public Button showResultButton;
    public TextView timeCountTextView;
    public TextView stepCountTextView;

    //BOOLEANS, TIME AND HANDLER/RUNNABLE
    public boolean isTimerRunning = false;
    public long startTime = 0;
    public Handler timerHandler = new Handler();
    public Runnable timerRunnable;

    public int stepCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //FINDING THE BUTTONS etc.
        startButton = findViewById(R.id.buttonStart);
        stopButton = findViewById(R.id.buttonStop);
        resetButton = findViewById(R.id.buttonReset);

        timeCountTextView = findViewById(R.id.textViewTime);
        stepCountTextView = findViewById(R.id.textViewSteps);

        //VARIABLES FOR 2ND PAGE
        showResultButton = findViewById(R.id.buttonShowResult);


        //START BUTTON
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTimer();
                startStepCounter();
            }
        });

        //STOP BUTTON
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopTimer();
                stopStepCounter();
            }
        });

        //RESET BUTTON
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearData();
            }
        });

        //SHOW RESULT ON SECOND PAGE
        showResultButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //CALCULATIONS
                //DATE
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String dateOfRun = dateFormat.format(new Date());

                //STEPS - METERS * 0.8
                String stepCount = stepCountTextView.getText().toString();
                int stepIndex = stepCount.indexOf(":");
                double totalMeters = 0;
                if (stepIndex >= 0)
                {
                    String calculateSteps = stepCount.substring(stepIndex + 1).trim();
                    totalMeters = Double.parseDouble(calculateSteps) * 0.8;
                }

                //CALORIES * 0.04
                int calorIndex = stepCount.indexOf(":");
                double totalCalories = 0;
                if (calorIndex >= 0)
                {
                    String calculateCalories = stepCount.substring(calorIndex + 1).trim();
                    totalCalories = Double.parseDouble(calculateCalories) * 0.04;
                }

                //TIME TAKEN IN SECONDS
                String timeInSeconds = timeCountTextView.getText().toString();

                //START THE INTENT ON 2ND PAGE and PASS THE DATA
                Intent SecondActivity = new Intent(getApplicationContext(), SecondActivity.class);
                SecondActivity.putExtra("date", dateOfRun);
                SecondActivity.putExtra("totalMeters", totalMeters);
                SecondActivity.putExtra("totalCalories", totalCalories);
                SecondActivity.putExtra("timeInSeconds", timeInSeconds);
                startActivity(SecondActivity);
            }

        });
    }



    //START TIMER METHOD
    public void startTimer() {
        if (!isTimerRunning) {
            startTime = System.currentTimeMillis() - (timeCountTextView.getTag() != null ? (long) timeCountTextView.getTag() : 0);
            timerRunnable = new Runnable() {
                @Override
                public void run() {
                    long elapsedTime = System.currentTimeMillis() - startTime;
                    int seconds = (int) (elapsedTime / 1000);
                    int minutes = seconds / 60;
                    seconds %= 60;
                    timeCountTextView.setText(String.format("Time: " + "%02d:%02d", minutes, seconds));
                    if (isTimerRunning) {
                        timerHandler.postDelayed(this, 1000);
                    }
                }
            };
            timerHandler.post(timerRunnable);
            isTimerRunning = true;
        }
    }

    //STOP TIMER METHOD
    public void stopTimer() {
        if (isTimerRunning) {
            timerHandler.removeCallbacks(timerRunnable);
            isTimerRunning = false;
        }
    }

    //START STEPS METHOD
    public void startStepCounter() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isTimerRunning) {
                    stepCount++;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            stepCountTextView.setText("Steps: " + stepCount);
                        }
                    });
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    //DO NOTHING ON STEPS
    public void stopStepCounter() {

    }

    //CLEAR DATA METHODS
    public void clearTimer() {
        timeCountTextView.setText("Time: " + "00:00");
        isTimerRunning = false;
        startTime = 0; // Reset the timer to zero
        timerHandler.removeCallbacks(timerRunnable);
    }

    public void clearStepCount() {
        stepCountTextView.setText("Steps: 0");
        stepCount = 0;
    }

    public void clearData() {
        clearTimer();
        clearStepCount();
    }
}
