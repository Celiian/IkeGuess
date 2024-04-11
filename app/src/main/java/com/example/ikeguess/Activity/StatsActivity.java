package com.example.ikeguess.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ikeguess.R;

public class StatsActivity extends AppCompatActivity {

    SharedPreferences sharedPref;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        sharedPref = getSharedPreferences("ikeguess", Context.MODE_PRIVATE);

        TextView SimpleGoodAnswer = findViewById(R.id.SimpleGoodAnswer);
        TextView SimpleBadAnswer = findViewById(R.id.SimpleBadAnswer);

        TextView MediumGoodAnswer = findViewById(R.id.MediumGoodAnswer);
        TextView MediumBadAnswer = findViewById(R.id.MediumBadAnswer);

        TextView HardcoreGoodAnswer = findViewById(R.id.HardcoreGoodAnswer);
        TextView HardcoreBadAnswer = findViewById(R.id.HardcoreBadAnswer);
        System.out.println(sharedPref.getInt("SimpleGoodAnswer", 0));
        SimpleGoodAnswer.setText("Nombre de bonnes réponses : " +  sharedPref.getInt("SimpleGoodAnswer", 0));
        SimpleBadAnswer.setText("Nombre de mauvaises réponses : " +  sharedPref.getInt("SimpleBadAnswer", 0));

        MediumGoodAnswer.setText("Nombre de bonnes réponses : " +  sharedPref.getInt("MediumGoodAnswer", 0));
        MediumBadAnswer.setText("Nombre de mauvaises réponses : " +  sharedPref.getInt("MediumBadAnswer", 0));

        HardcoreGoodAnswer.setText("Nombre de bonnes réponses : " +  sharedPref.getInt("HardcoreGoodAnswer", 0));
        HardcoreBadAnswer.setText("Nombre de mauvaises réponses : " +  sharedPref.getInt("HardcoreBadAnswer", 0));

        Button menuButton = findViewById(R.id.returnButton);

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}