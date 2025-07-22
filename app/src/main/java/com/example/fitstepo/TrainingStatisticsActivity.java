package com.example.fitstepo;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class TrainingStatisticsActivity extends AppCompatActivity {

    private ImageView calendarButton;
    private BottomNavigationView bottomNavigation;

    private TextView stepsText;
    private TextView distanceText;
    private TextView caloriesText;
    private TextView rope;
    private TextView treadmill;
    private TextView barbell;
    private TextView pointsText;

    private StepTracker stepTracker;
    private Handler handler = new Handler();
    private DashboardFragment currentDashboardFragment = null;
    private MainProgressBarsManager progressManager;

    private ImageView profileImage;
    private TextView helloText;
    private String loggedUserEmail;
    private String loggedUserFullName;
    private String avatar;

    private static final String PREFS_NAME = "ExercisePrefs"; // ДОДАНО

    private final Runnable updateRunnable = new Runnable() {
        @Override
        public void run() {
            int steps = stepTracker.getCurrentStepCount();
            float distance = stepTracker.getDistanceKm();
            float baseCalories = stepTracker.getCaloriesBurned();
            float points = stepTracker.getPoints();

            // ДОДАНО: зчитування збережених вправ
            SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
            float ropeVal = prefs.getFloat("Rope", 0f);
            float treadmillVal = prefs.getFloat("Treadmill", 0f);
            float dumbbellVal = prefs.getFloat("Dumbbell", 0f);

            float totalCalories = baseCalories
                    + (ropeVal * 0.17f)
                    + (treadmillVal * 0.035f)
                    + (dumbbellVal * 0.4f);

            // Вивід
            stepsText.setText(" " + steps);
            distanceText.setText(String.format("%.2f km", distance));
            caloriesText.setText(String.format("%.0f kcal", totalCalories));
            pointsText.setText(String.format("%.1f", points));

            double ropeValue = distance / 0.00135;
            double treadmillValue = distance * 4000;
            double barbellValue = distance / 0.003;

            rope.setText(String.format("%.0f", ropeVal));
            treadmill.setText(String.format("%.0f", treadmillVal));
            barbell.setText(String.format("%.0f", dumbbellVal));

            if (currentDashboardFragment != null) {
                currentDashboardFragment.updateProgressBar(25);
                currentDashboardFragment.updatePtsText((int) points);
            }

            handler.postDelayed(this, 1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_statistics);

        Intent intent = getIntent();
        loggedUserEmail = intent.getStringExtra(Constants.EXTRA_EMAIL);
        loggedUserFullName = intent.getStringExtra(Constants.EXTRA_FULL_NAME);
        avatar = intent.getStringExtra(Constants.EXTRA_AVATAR);

        profileImage = findViewById(R.id.profileImage);
        helloText = findViewById(R.id.helloText);

        if (loggedUserEmail != null) {
            AvatarUtils.loadUserAvatar(this, loggedUserEmail, profileImage);
        }

        if (helloText != null && loggedUserFullName != null) {
            helloText.setText("Hello " + loggedUserFullName + "!");
        }

        profileImage.setOnClickListener(v -> {
            Intent profileIntent = new Intent(TrainingStatisticsActivity.this, ProfileActivity.class);
            profileIntent.putExtra(Constants.EXTRA_EMAIL, loggedUserEmail);
            profileIntent.putExtra(Constants.EXTRA_FULL_NAME, loggedUserFullName);
            if (avatar != null) {
                profileIntent.putExtra(Constants.EXTRA_AVATAR, avatar);
            }
            startActivity(profileIntent);
        });

        ImageView fill0 = findViewById(R.id.progress_fill1);
        ImageView fill1 = findViewById(R.id.progress_fill3);
        ImageView fill2 = findViewById(R.id.progress_fill5);

        int maxHeightDp = 146;
        float scale = getResources().getDisplayMetrics().density;
        int maxHeightPx = (int) (maxHeightDp * scale + 0.5f);

        progressManager = new MainProgressBarsManager(fill0, fill1, fill2, maxHeightPx);
        progressManager.setProgress(0, 70, 100);
        progressManager.setProgress(1, 40, 100);
        progressManager.setProgress(2, 75, 100);

        calendarButton = findViewById(R.id.calendarButton);
        bottomNavigation = findViewById(R.id.bottomNavigation);
        stepsText = findViewById(R.id.stepsText);
        distanceText = findViewById(R.id.distanceText);
        caloriesText = findViewById(R.id.caloriesText);
        pointsText = findViewById(R.id.pointsText);

        rope = findViewById(R.id.rope);
        treadmill = findViewById(R.id.treadmill);
        barbell = findViewById(R.id.barbell);

        TextView dateText = findViewById(R.id.dateText);
        dateText.setText(DateHelper.getFormattedDate());

        // ДОДАНО: обробка intent після виконання вправи
        if (intent != null && intent.hasExtra("exerciseName") && intent.hasExtra("exerciseTime")) {
            String exerciseName = intent.getStringExtra("exerciseName");
            long exerciseTimeMs = intent.getLongExtra("exerciseTime", 0);
            double exerciseTimeSeconds = exerciseTimeMs / 1000.0;

            double value = 0;

            switch (exerciseName) {
                case "Rope":
                    value = exerciseTimeSeconds * (88.0 / 50.0);
                    rope.setText(String.format("%.0f", value));
                    saveExerciseValue("Rope", (float) value);
                    break;
                case "Treadmill":
                    value = (exerciseTimeSeconds / 60.0) * 100;
                    treadmill.setText(String.format("%.0f", value));
                    saveExerciseValue("Treadmill", (float) value);
                    break;
                case "Dumbbell":
                    value = exerciseTimeSeconds / 2.5;
                    barbell.setText(String.format("%.0f", value));
                    saveExerciseValue("Dumbbell", (float) value);
                    break;
            }

            getIntent().removeExtra("exerciseName");
            getIntent().removeExtra("exerciseTime");
        }

        calendarButton.setOnClickListener(v -> {
            Intent intent1 = new Intent(TrainingStatisticsActivity.this, WorkoutScheduleActivity.class);
            intent1.putExtra(Constants.EXTRA_EMAIL, loggedUserEmail);
            intent1.putExtra(Constants.EXTRA_FULL_NAME, loggedUserFullName);
            if (avatar != null) {
                intent1.putExtra(Constants.EXTRA_AVATAR, avatar);
            }
            startActivity(intent1);
        });

        barbell.setOnClickListener(v -> {
            Intent intent1 = new Intent(TrainingStatisticsActivity.this, WorkoutScheduleActivity.class);
            startActivity(intent1);
        });

        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_medal) {
                currentDashboardFragment = new DashboardFragment();
                Bundle bundle = new Bundle();
                bundle.putString(Constants.EXTRA_EMAIL, loggedUserEmail);
                bundle.putString(Constants.EXTRA_FULL_NAME, loggedUserFullName);
                if (avatar != null) {
                    bundle.putString(Constants.EXTRA_AVATAR, avatar);
                }
                currentDashboardFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, currentDashboardFragment)
                        .commit();
                return true;
            } else if (itemId == R.id.nav_home) {
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                if (fragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .remove(fragment)
                            .commit();
                }
                currentDashboardFragment = null;
                return true;
            } else if (itemId == R.id.nav_heart) {
                DailyOverviewFragment overviewFragment = new DailyOverviewFragment();
                Bundle bundle = new Bundle();
                bundle.putString(Constants.EXTRA_EMAIL, loggedUserEmail);
                bundle.putString(Constants.EXTRA_FULL_NAME, loggedUserFullName);
                if (avatar != null) {
                    bundle.putString(Constants.EXTRA_AVATAR, avatar);
                }
                overviewFragment.setArguments(bundle);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, overviewFragment)
                        .commit();
                currentDashboardFragment = null;
                return true;
            } else if (itemId == R.id.nav_stats) {
                Intent statsIntent = new Intent(TrainingStatisticsActivity.this, Frame18Activity.class);
                statsIntent.putExtra(Constants.EXTRA_EMAIL, loggedUserEmail);
                statsIntent.putExtra(Constants.EXTRA_FULL_NAME, loggedUserFullName);
                if (avatar != null) {
                    statsIntent.putExtra(Constants.EXTRA_AVATAR, avatar);
                }
                startActivity(statsIntent);
                return true;
            } else if (itemId == R.id.nav_play) {
                Intent playIntent = new Intent(TrainingStatisticsActivity.this, ExerciseActivity.class);
                playIntent.putExtra(Constants.EXTRA_EMAIL, loggedUserEmail);
                playIntent.putExtra(Constants.EXTRA_FULL_NAME, loggedUserFullName);
                if (avatar != null) {
                    playIntent.putExtra(Constants.EXTRA_AVATAR, avatar);
                }
                startActivity(playIntent);
                return true;
            }

            return false;
        });

        float heightCm = 175f;
        float weightKg = 70f;
        boolean isMale = true;

        stepTracker = new StepTracker(this, heightCm, weightKg, isMale);
        stepTracker.startTracking();
        handler.post(updateRunnable);

        checkAndRequestActivityRecognitionPermission();

        startService(new Intent(this, PulseService.class));
    }

    private void saveExerciseValue(String name, float value) {
        getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                .edit()
                .putFloat(name, value)
                .apply();
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public StepTracker getStepTracker() {
        return stepTracker;
    }
}
