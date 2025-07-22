package com.example.fitstepo;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.Random;

public class PulseService extends Service {

    public static float simulatedHeartRate = 65f;

    private static final String PREF_NAME = "StepPrefs";
    private static final String KEY_SPS = "StepsPerSecond";

    private static final float MAX_PULSE = 190f;
    private static final float REST_PULSE_MIN = 60f;
    private static final float REST_PULSE_MAX = 70f;
    private static final float RECOVERY_TARGET = 65f;

    private final Handler handler = new Handler();
    private final Random random = new Random();

    private final Runnable pulseRunnable = new Runnable() {
        @Override
        public void run() {
            updatePulse();
            handler.postDelayed(this, 1000);
        }
    };

    private void updatePulse() {
        SharedPreferences prefs = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        float stepsPerSecond = prefs.getFloat(KEY_SPS, 0f);

        Log.d("PulseDebug", "SPS=" + Math.round(stepsPerSecond) + " | HR=" + Math.round(simulatedHeartRate));

        if (stepsPerSecond < 0.1f) {
            int chance = random.nextInt(100);
            int change = (chance < 15) ? -1 : (chance < 30 ? 1 : 0);
            float newPulse = simulatedHeartRate + change;

            if (newPulse >= REST_PULSE_MIN && newPulse <= REST_PULSE_MAX) {
                simulatedHeartRate = newPulse;
            }
        } else {
            float targetPulse = 90f + (stepsPerSecond * 750f);
            if (targetPulse > MAX_PULSE) targetPulse = MAX_PULSE;

            float growthFactor = 1f - (float) Math.pow((simulatedHeartRate / MAX_PULSE), 2);
            if (growthFactor < 0.1f) growthFactor = 0.1f;

            float intensity = Math.min(stepsPerSecond / 1.0f, 1.0f);

            if (targetPulse > simulatedHeartRate) {
                float increment = (targetPulse - simulatedHeartRate) * 0.25f * intensity * growthFactor;
                simulatedHeartRate += increment;
            } else {
                float balance = (targetPulse - simulatedHeartRate) * 0.1f * intensity;
                simulatedHeartRate += balance;
            }
        }

        if (stepsPerSecond == 0f) {
            if (simulatedHeartRate > RECOVERY_TARGET) {
                simulatedHeartRate -= (simulatedHeartRate - RECOVERY_TARGET) * 0.03f;
            } else if (simulatedHeartRate < RECOVERY_TARGET) {
                simulatedHeartRate += (RECOVERY_TARGET - simulatedHeartRate) * 0.02f;
            }

            if (Math.abs(simulatedHeartRate - RECOVERY_TARGET) < 0.5f) {
                simulatedHeartRate = RECOVERY_TARGET;
            }
        }

        Log.d("PulseDebug", String.format("SPS=%.1f | HR=%d", stepsPerSecond, Math.round(simulatedHeartRate)));
    }

    @Override
    public void onCreate() {
        super.onCreate();
        handler.post(pulseRunnable);
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacks(pulseRunnable);
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
