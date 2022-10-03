package com.example.imagepro.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.imagepro.R;

import org.opencv.android.OpenCVLoader;

public class MainActivity extends AppCompatActivity {
    static {
        if(OpenCVLoader.initDebug()){
            Log.d("MainActivity: ","Opencv is loaded");
        }
        else {
            Log.d("MainActivity: ","Opencv failed to load");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button region_button = findViewById(R.id.region_button);
        region_button.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, RegionActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP)));

    }
}