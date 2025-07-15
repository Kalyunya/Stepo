package com.example.fitstepo;

import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class TrainingStatisticsActivity extends AppCompatActivity {

    private ImageView progressFill;
    private ImageView calendarButton;
    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Підключення до оновленого XML
        setContentView(R.layout.activity_training_statistics);

        // Ініціалізація елементів інтерфейсу
        progressFill = findViewById(R.id.progress_fill);
        calendarButton = findViewById(R.id.calendarButton);
        bottomNavigation = findViewById(R.id.bottomNavigation);

        // Налаштування висоти прогресу
        setProgressHeight(100);

        // Обробка кліку по кнопці календаря
        calendarButton.setOnClickListener(v -> {
            Intent intent = new Intent(TrainingStatisticsActivity.this, WorkoutScheduleActivity.class);
            startActivity(intent);
        });

        // Обробка кліків нижньої навігації
        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_medal) {
                Fragment dashboardFragment = new DashboardFragment();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, dashboardFragment)
                        .commit();
                return true;

            } else if (itemId == R.id.nav_home) {
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                if (fragment != null) {
                    getSupportFragmentManager().beginTransaction()
                            .remove(fragment)
                            .commit();
                }
                return true;

            } else if (itemId == R.id.nav_heart) {
                Intent intent = new Intent(TrainingStatisticsActivity.this, DailyOverviewActivity.class);
                startActivity(intent);
                return true;
            }

            return false;
        });
    }

    // Метод для встановлення висоти прогрес-бару
    private void setProgressHeight(int heightPx) {
        ViewGroup.LayoutParams params = progressFill.getLayoutParams();
        params.height = heightPx;
        progressFill.setLayoutParams(params);
    }
}
