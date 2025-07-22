package com.example.fitstepo;

import android.Manifest;
import android.content.Intent;
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

    // 👇 ДОДАНО: змінні для профілю
    private ImageView profileImage;
    private TextView helloText;
    private String loggedUserEmail;
    private String loggedUserFullName;
    private String avatar;

    private final Runnable updateRunnable = new Runnable() {
        @Override
        public void run() {
            int steps = stepTracker.getCurrentStepCount();
            float distance = stepTracker.getDistanceKm();
            float calories = stepTracker.getCaloriesBurned();
            float points = stepTracker.getPoints();

            stepsText.setText(" " + steps);
            pointsText.setText(String.format("%.1f", points));
            distanceText.setText(String.format("%.2f km", distance));
            caloriesText.setText(String.format("%.0f kcal", calories));

            double ropeValue = distance / 0.00135;
            double treadmillValue = distance * 4000;
            double barbellValue = distance / 0.003;

            rope.setText(String.format("%.0f", ropeValue));
            treadmill.setText(String.format("%.0f", treadmillValue));
            barbell.setText(String.format("%.0f", barbellValue));

            if (currentDashboardFragment != null) {
                currentDashboardFragment.updateProgressBar(25);
                currentDashboardFragment.updatePtsText((int) points);
            }
            handler.postDelayed(this, 1000);
        }
    };
//        public void run() {
//            int steps = stepTracker.getCurrentStepCount();
//            float distance = stepTracker.getDistanceKm();
//            float calories = stepTracker.getCaloriesBurned();
//            float points = stepTracker.getPoints();
//
//            stepsText.setText(" " + steps);
//            pointsText.setText(String.format("%.1f", points));
//            distanceText.setText(String.format("%.2f km", distance));
//            caloriesText.setText(String.format("%.0f kcal", calories));
//
//            double ropeValue = distance / 0.00135;
//            double treadmillValue = distance * 4000;
//            double barbellValue = distance / 0.003;
//
//            rope.setText(String.format("%.0f", ropeValue));
//            treadmill.setText(String.format("%.0f", treadmillValue));
//            barbell.setText(String.format("%.0f", barbellValue));
//
//
//
//            int distanceProgress = (int) ((distance / 5f) * 100);
//            int stepsProgress = (int) ((steps / 10000f) * 100);
//            int pointsProgress = (int) ((points / 10000f) * 100);
//
//
//            distanceProgress = Math.min(distanceProgress, 100);
//            stepsProgress = Math.min(stepsProgress, 100);
//            pointsProgress = Math.min(pointsProgress, 100);
//
//            progressManager.setProgress(0, distanceProgress, 100);
//            progressManager.setProgress(1, stepsProgress, 100);
//            progressManager.setProgress(2, pointsProgress, 100);
//
//            if (currentDashboardFragment != null) {
//                currentDashboardFragment.updateProgressBar(25);
//                currentDashboardFragment.updatePtsText((int) points);
//            }
//
//            handler.postDelayed(this, 1000);
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_statistics);

        // 👇 ДОДАНО: зчитування з Intent
        Intent intent = getIntent();
        loggedUserEmail = intent.getStringExtra(Constants.EXTRA_EMAIL);
        loggedUserFullName = intent.getStringExtra(Constants.EXTRA_FULL_NAME);
        avatar = intent.getStringExtra(Constants.EXTRA_AVATAR);

        // 👇 ДОДАНО: знаходимо в макеті
        profileImage = findViewById(R.id.profileImage);
        helloText = findViewById(R.id.helloText);

//        // 👇 ДОДАНО: аватар
//        if (avatar != null && avatar.startsWith("content://")) {
//            profileImage.setImageURI(Uri.parse(avatar));
//        } else {
//            profileImage.setImageResource(R.drawable.default_avatar);
//        }

        // ✅ ВСТАНОВЛЕННЯ АВАТАРА ЧЕРЕЗ AvatarUtils
        if (loggedUserEmail != null) {
            AvatarUtils.loadUserAvatar(this, loggedUserEmail, profileImage);
        }


        // 👇 ДОДАНО: ім’я
        if (helloText != null && loggedUserFullName != null) {
            helloText.setText("Hello " + loggedUserFullName + "!");
        }

        // 👇 ДОДАНО: перехід на профіль
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

//        progressManager = new MainProgressBarsManager(fill0, fill1, fill2, maxHeightPx);
        MainProgressBarsManager progressManager = new MainProgressBarsManager(fill0, fill1, fill2, maxHeightPx);
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

    public StepTracker getStepTracker() {
        return stepTracker;
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
}
