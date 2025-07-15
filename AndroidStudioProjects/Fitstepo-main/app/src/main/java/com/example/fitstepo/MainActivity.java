package com.example.fitstepo;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Ініціалізація DBHelper і створення бази, якщо її ще немає
        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Перевірка, чи є записи в таблиці Users (спочатку може бути пусто)
        try {
            Cursor cursor = db.rawQuery("SELECT * FROM Users", null);
            if (cursor.getCount() == 0) {
                Log.d("DB", "Таблиця Users порожня. Додаємо тестовий запис.");

                // Вставляємо тестовий запис, щоб перевірити, що все працює
                db.execSQL(
                        "INSERT INTO Users (fullName, Password, Email, Gender, Age, Height, Weight, Goals, Time) VALUES " +
                                "('Тестовий Користувач', '12345', 'test@example.com', 'Male', 30, 175, 70, 'Lose Weight', '08:00')"
                );
            } else {
                Log.d("DB", "Таблиця Users вже має записи.");
            }
            cursor.close();

            // Перевіряємо, чи запис додався
            Cursor cursorCheck = db.rawQuery("SELECT Email, fullName FROM Users", null);
            while (cursorCheck.moveToNext()) {
                String email = cursorCheck.getString(cursorCheck.getColumnIndexOrThrow("Email"));
                String fullName = cursorCheck.getString(cursorCheck.getColumnIndexOrThrow("fullName"));
                Log.d("DB", "Користувач: " + fullName + ", Email: " + email);
            }
            cursorCheck.close();

        } catch (Exception e) {
            Log.e("DB", "Помилка при роботі з базою: " + e.getMessage());
        } finally {
            db.close();
        }

        // Переходимо на WelcomeActivity
        Intent intent = new Intent(MainActivity.this,
                ProfileActivity.class);
        startActivity(intent);
        finish();
    }
}
