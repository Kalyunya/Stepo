package com.example.fitstepo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.Locale;

public class ExerciseActivity extends AppCompatActivity {

    private TextView timerText;
    private Button pauseButton;
    private LinearLayout dumbbellLayout, treadmillLayout, ropeLayout;

    private String selectedExercise = null;
    private boolean isTimerRunning = false;
    private long elapsedTime = 0;
    private long startTime = 0;

    private Handler handler = new Handler();

    // ← ДОДАНО: властивості класу
    private String loggedUserEmail;
    private String loggedUserFullName;
    private String avatar;

    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            elapsedTime = System.currentTimeMillis() - startTime;
            int seconds = (int) (elapsedTime / 1000);
            timerText.setText(String.format(Locale.getDefault(), "%02d : %02d", seconds / 60, seconds % 60));
            handler.postDelayed(this, 1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timer);

        timerText = findViewById(R.id.timerText);
        pauseButton = findViewById(R.id.pauseButton);
        dumbbellLayout = findViewById(R.id.dumbbell);
        treadmillLayout = findViewById(R.id.treadmill);
        ropeLayout = findViewById(R.id.rope);
        ImageView backButton = findViewById(R.id.backButton);

        // ← ДОДАНО: збереження переданих даних
        loggedUserEmail = getIntent().getStringExtra(Constants.EXTRA_EMAIL);
        loggedUserFullName = getIntent().getStringExtra(Constants.EXTRA_FULL_NAME);
        avatar = getIntent().getStringExtra(Constants.EXTRA_AVATAR);

        dumbbellLayout.setOnClickListener(v -> selectExercise("Dumbbell"));
        treadmillLayout.setOnClickListener(v -> selectExercise("Treadmill"));
        ropeLayout.setOnClickListener(v -> selectExercise("Rope"));

        pauseButton.setOnClickListener(v -> onPauseButtonClick());

        backButton.setOnClickListener(v -> {
            if (isTimerRunning) {
                handler.removeCallbacks(timerRunnable);
                elapsedTime = System.currentTimeMillis() - startTime;
                isTimerRunning = false;
            }

            Intent intent1 = new Intent(ExerciseActivity.this, TrainingStatisticsActivity.class);
            intent1.putExtra("exerciseName", selectedExercise);
            intent1.putExtra("exerciseTime", elapsedTime);

            // ← ВИКОРИСТАННЯ збережених змінних
            intent1.putExtra(Constants.EXTRA_EMAIL, loggedUserEmail);
            intent1.putExtra(Constants.EXTRA_FULL_NAME, loggedUserFullName);
            if (avatar != null) {
                intent1.putExtra(Constants.EXTRA_AVATAR, avatar);
            }

            startActivity(intent1);
            finish();
        });
    }

    private void selectExercise(String exercise) {
        if (selectedExercise != null && elapsedTime > 0) {
            sendResultBack(selectedExercise, elapsedTime);
        }

        selectedExercise = exercise;
        handler.removeCallbacks(timerRunnable);
        isTimerRunning = false;
        elapsedTime = 0;
        timerText.setText("00 : 00");
        pauseButton.setText("Start");

        Toast.makeText(this, exercise + " selected", Toast.LENGTH_SHORT).show();
    }

    private void onPauseButtonClick() {
        if (selectedExercise == null) {
            Toast.makeText(this, "Please select an exercise first!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!isTimerRunning) {
            startTime = System.currentTimeMillis() - elapsedTime;
            handler.postDelayed(timerRunnable, 0);
            pauseButton.setText("Pause");
            isTimerRunning = true;
        } else {
            handler.removeCallbacks(timerRunnable);
            elapsedTime = System.currentTimeMillis() - startTime;
            pauseButton.setText("Start");
            isTimerRunning = false;
        }

        checkAndRequestActivityRecognitionPermission();
    }

    private void sendResultBack(String exerciseName, long timeMillis) {
        Intent intent = new Intent(ExerciseActivity.this, MainActivity.class);
        intent.putExtra("exerciseName", exerciseName);
        intent.putExtra("exerciseTime", timeMillis);
        startActivity(intent);
        finish();
    }

    private void checkAndRequestActivityRecognitionPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACTIVITY_RECOGNITION},
                        1001);
            }
        }
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(timerRunnable);
        super.onDestroy();
    }
}
