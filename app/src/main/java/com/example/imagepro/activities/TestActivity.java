package com.example.imagepro.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.imagepro.R;

import org.opencv.android.OpenCVLoader;

public class TestActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_test);

        Button region_button = findViewById(R.id.region_button);
        region_button.setOnClickListener(v -> startActivity(new Intent(TestActivity.this, RegionActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP)));

        Button class_button = findViewById(R.id.label_button);
        class_button.setOnClickListener(v -> startActivity(new Intent(TestActivity.this, LabelActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP)));

        Button tile_button = findViewById(R.id.tile_button);
        tile_button.setOnClickListener(v -> startActivity(new Intent(TestActivity.this, TileActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP)));

        Button rotation_button = findViewById(R.id.rotation_button);
        rotation_button.setOnClickListener(v -> startActivity(new Intent(TestActivity.this, RotationActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP)));

        Button graph_button = findViewById(R.id.graph_button);
        graph_button.setOnClickListener(v -> startActivity(new Intent(TestActivity.this, GraphActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP)));

        Button car_button = findViewById(R.id.car_button);
        car_button.setOnClickListener(v -> startActivity(new Intent(TestActivity.this, CarActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP)));
    }
}