package com.example.week4accelerometer;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private boolean color = false;
    private View view;
    private long lastUpdateTime;
    private static float SHAKE_THRESHOLD_GRAVITY = 2;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        view = findViewById(R.id.txt_color);
        view.setBackgroundColor(Color.GREEN);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        lastUpdateTime = System.currentTimeMillis();
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            getAccelerometer(sensorEvent);
        }

    }

    private void getAccelerometer(SensorEvent event) {
        float[] values = event.values;
        //Movement
        float x = values[0];
        float y = values[1];
        float z = values[2];

        float gX = x / SensorManager.GRAVITY_EARTH;
        float gY = x / SensorManager.GRAVITY_EARTH;
        float gZ = x / SensorManager.GRAVITY_EARTH;

        //gForce will be close to 1 when there is no movement
        float gForce = (float)Math.sqrt(gX * gX + gY * gY + gZ * gZ);

        long currentTime = System.currentTimeMillis();
        if (gForce >= SHAKE_THRESHOLD_GRAVITY) //
        {
            if (currentTime - lastUpdateTime < 200) {
                return;
            }
            lastUpdateTime = currentTime;
            Toast.makeText(this, "Device was shaken", Toast.LENGTH_SHORT).show();
            if (color == true) {
                view.setBackgroundColor(Color.GREEN);
            } else {
                view.setBackgroundColor(Color.RED);
            }
            color = !color;
        }
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                sensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
}