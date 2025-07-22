package com.example.fitstepo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class WorkoutScheduleActivity extends AppCompatActivity {

    private String email;
    private String fullName;
    private String avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_schedule);

        // 📨 Отримання даних з Intent
        Intent intent = getIntent();
        email = intent.getStringExtra(Constants.EXTRA_EMAIL);
        fullName = intent.getStringExtra(Constants.EXTRA_FULL_NAME);
        avatar = intent.getStringExtra(Constants.EXTRA_AVATAR);

        // 🗓️ Встановлення дати
        TextView dayOfWeekText = findViewById(R.id.day_of_week);
        TextView dateText = findViewById(R.id.date);

        Date currentDate = new Date();
        String dayOfWeek = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(currentDate); // Thursday
        String date = new SimpleDateFormat("d MMMM", Locale.ENGLISH).format(currentDate);     // 4 July

        dayOfWeekText.setText(dayOfWeek);
        dateText.setText(date);

//        // 🖼️ Якщо є аватар — можеш вставити ImageView
//        ImageView avatarImage = findViewById(R.id.avatarImage); // ← створити в XML при потребі
//        if (avatarImage != null && avatar != null && avatar.startsWith("content://")) {
//            avatarImage.setImageURI(Uri.parse(avatar));
//        }

        // 🔙 Назад з передачею даних
        ImageView backButton = findViewById(R.id.back);
        backButton.setOnClickListener(v -> {
            Intent backIntent = new Intent(WorkoutScheduleActivity.this, TrainingStatisticsActivity.class);
            backIntent.putExtra(Constants.EXTRA_EMAIL, email);
            backIntent.putExtra(Constants.EXTRA_FULL_NAME, fullName);
            if (avatar != null) {
                backIntent.putExtra(Constants.EXTRA_AVATAR, avatar);
            }
            startActivity(backIntent);
            finish();
        });
    }
}
