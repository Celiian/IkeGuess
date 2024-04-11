package com.example.ikeguess.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.ikeguess.BuildConfig;
import com.example.ikeguess.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        TextView versionTextView = findViewById(R.id.versionTextView);
        String version = "V."+BuildConfig.VERSION_NAME;
        versionTextView.setText(version);
    }
}