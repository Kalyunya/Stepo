package com.example.fitstepo;

import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.util.Log;

public class StepTracker implements SensorEventListener {

    private static final String PREF_NAME = "StepPrefs";
    private static final String KEY_INITIAL_STEP = "InitialStep";
    private static final String KEY_CURRENT_STEP = "CurrentStep";
    private static final String KEY_POINTS = "CurrentPoints";
    private static final String KEY_SPS = "StepsPerSecond";

    private SensorManager sensorManager;
    private Sensor stepCounterSensor;

    private int initialStepCount = -1;
    private int currentStepCount = 0;
    private float currentPoints = 0f;

    private float heightCm;
    private float weightKg;
    private boolean isMale;

    private long lastStepTimestamp = -1;

    private Context context;
    private Handler decayHandler = new Handler();

    public StepTracker(Context context, float heightCm, float weightKg, boolean isMale) {
        this.context = context.getApplicationContext();
        this.heightCm = heightCm;
        this.weightKg = weightKg;
        this.isMale = isMale;

        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            stepCounterSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        }

        loadSavedData();
    }

    public void startTracking() {
        decayHandler.post(decayRunnable);
        if (stepCounterSensor != null) {
            sensorManager.registerListener(this, stepCounterSensor, SensorManager.SENSOR_DELAY_UI);
            decayHandler.post(decayRunnable);
            Log.d("StepTracker", "✅ Step sensor активирован");
        } else {
            Log.e("StepTracker", "⚠️ Шагомер не доступен на этом устройстве");
        }
    }

    public void stopTracking() {
        decayHandler.removeCallbacks(decayRunnable);
        if (stepCounterSensor != null) {
            sensorManager.unregisterListener(this, stepCounterSensor);
            decayHandler.removeCallbacks(decayRunnable);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            int totalSteps = (int) event.values[0];

            if (initialStepCount == -1) {
                initialStepCount = totalSteps;
                saveInt(KEY_INITIAL_STEP, initialStepCount);
            }

            int newStepCount = totalSteps - initialStepCount;
            long currentTime = System.currentTimeMillis();

            if (lastStepTimestamp != -1 && newStepCount > currentStepCount) {
                long deltaTime = currentTime - lastStepTimestamp;
                if (deltaTime > 0) {
                    float stepsPerSecond = 1000f / deltaTime;
                    saveFloat(KEY_SPS, stepsPerSecond);
                }
            }

            if (newStepCount > currentStepCount) {
                lastStepTimestamp = currentTime;
            }

            currentStepCount = newStepCount;
            currentPoints = calculatePoints();

            saveInt(KEY_CURRENT_STEP, currentStepCount);
            saveFloat(KEY_POINTS, currentPoints);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    public int getCurrentStepCount() {
        return currentStepCount;
    }

    public float getDistanceKm() {
        float stepLengthMeters = 0.415f * heightCm / 100;
        return (currentStepCount * stepLengthMeters) / 1000f;
    }

    public float getPoints() {
        return currentPoints;
    }

    public float getCaloriesBurned() {
        float distanceKm = getDistanceKm();
        float coef = isMale ? 0.9f : 0.8f;
        return weightKg * coef * distanceKm;
    }

    public float getStepsPerSecond() {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getFloat(KEY_SPS, 0f);
    }

    private float calculatePoints() {
        return currentStepCount * weightKg / 100f;
    }

    private void saveInt(String key, int value) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().putInt(key, value).apply();
    }

    private void saveFloat(String key, float value) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().putFloat(key, value).apply();
    }

    private void loadSavedData() {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        initialStepCount = prefs.getInt(KEY_INITIAL_STEP, -1);
        currentStepCount = prefs.getInt(KEY_CURRENT_STEP, 0);
        currentPoints = prefs.getFloat(KEY_POINTS, 0f);
    }

    private final Runnable decayRunnable = new Runnable() {
        @Override
        public void run() {
            long now = System.currentTimeMillis();
            if (lastStepTimestamp != -1 && now - lastStepTimestamp > 5000) {
                saveFloat(KEY_SPS, 0f);
            }
            decayHandler.postDelayed(this, 1000);
        }
    };

}