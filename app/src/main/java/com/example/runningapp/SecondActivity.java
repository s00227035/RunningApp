package com.example.runningapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Date;


public class SecondActivity extends AppCompatActivity {

    //VARIABLES
    public TextView showResultDate;
    public TextView showResultMetersRun;
    public TextView showResultCaloriesBurned;
    public TextView showResultTotalTime;
    public Button btnGoBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        //GET THE DATA
        showResultDate = findViewById(R.id.textResultDate);
        showResultMetersRun = findViewById(R.id.textResultMetersRun);
        showResultCaloriesBurned = findViewById(R.id.textResultCalories);
        showResultTotalTime = findViewById(R.id.textResultTime);
        Intent intent = getIntent();

        //DATE
        String date = intent.getStringExtra("date");
        showResultDate.setText("Date of the Run: " + date);

        //TOTAL METERS RUN
        double totalMeters = intent.getDoubleExtra("totalMeters",0);
        showResultMetersRun.setText("Meters Run: " + Double.toString(totalMeters));

        //TOTAL CALORIES BURNED PER RUN
        double totalCalories = intent.getDoubleExtra("totalCalories", 0);
        showResultCaloriesBurned.setText("Calories Burned: " + Double.toString(totalCalories));

        //TOTAL TIME TAKEN FOR THE RUN
        String timeInSeconds = intent.getStringExtra("timeInSeconds");
        showResultTotalTime.setText("Total time taken: " + (timeInSeconds));

        //CLOSE THE SECOND ACTIVITY AND RETURN BACK
        btnGoBack = findViewById(R.id.buttonGoBack);
        btnGoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
