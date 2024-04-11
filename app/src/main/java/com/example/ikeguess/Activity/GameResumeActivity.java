package com.example.ikeguess.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ikeguess.R;

public class GameResumeActivity extends AppCompatActivity {

    private int goodAnswer;

    private int numberQuestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_resume);


        TextView topTextView = findViewById(R.id.topTextView);
        TextView percentTextView = findViewById(R.id.percentTextView);
        TextView resumeTextView = findViewById(R.id.resumeTextView);
        Button menuButton = findViewById(R.id.menuButton);

        goodAnswer = getIntent().getIntExtra("goodAnswer", 0);
        numberQuestions = getIntent().getIntExtra("numberQuestions", 0);

        int percent = (int) ((((float) goodAnswer / numberQuestions)) * 100);
        if (percent > 60){
            topTextView.setText("Bravo vous avez passé le niveau !");
        }
        else {
            topTextView.setText("Malheuresement vous avez raté ce niveau ");
        }

        percentTextView.setText(String.valueOf(percent) + "%");
        resumeTextView.setText(goodAnswer + " bonnes réponses sur " + numberQuestions);


        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}