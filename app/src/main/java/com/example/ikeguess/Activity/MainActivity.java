package com.example.ikeguess.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;

import com.example.ikeguess.customClass.QuizzCreator;
import com.example.ikeguess.R;
import com.example.ikeguess.customClass.DataList;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    String quizzLevel;

    private MediaPlayer mediaPlayer;

    boolean chrono = false;

    int timer;

    public static final String URL = "https://students.gryt.tech/api/L2/ikea/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button simpleQuizzButton = findViewById(R.id.simpleQuizzButton);
        Button mediumQuizzButton = findViewById(R.id.mediumQuizzButton);
        Button harcoreQuizzButton = findViewById(R.id.harcoreQuizzButton);
        Button statButton = findViewById(R.id.statButton);
        ImageView aboutIcon = findViewById(R.id.aboutImageView);
        ImageView ikea = findViewById(R.id.imageViewIkea);
        SwitchCompat timeModeSwitch = findViewById(R.id.timeModeSwitch);
        timeModeSwitch.setChecked(chrono);
        timeModeSwitch.setOnClickListener(action -> {
            chrono = !chrono;
        });

        Intent intent = new Intent(this, SimpleQuizzActivity.class);
        Intent intentMaps = new Intent(this, MapsActivity.class);
        Button quizzButton = findViewById(R.id.simpleQuizzButton);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choisissez le nombre de questions");
        String[] options = {"3 questions", "5 questions", "7 questions"};
        QuizzCreator quizzCreator = new QuizzCreator();
        final Resources res = getResources();
        final int sound = res.getIdentifier(String.valueOf(R.raw.ikea), "raw", getPackageName());
        mediaPlayer = MediaPlayer.create(getBaseContext(), sound);

        ikea.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    mediaPlayer = MediaPlayer.create(getBaseContext(), sound);
                } else {
                    mediaPlayer.start();
                }
            }
        });


        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer = MediaPlayer.create(getBaseContext(), sound);
        } else {
            mediaPlayer.start();
        }

        aboutIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });


        DialogInterface.OnClickListener actionListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(MainActivity.this, SimpleQuizzActivity.class);
                DataList dataList;

                try {
                    dataList = getData();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }


                switch (which) {
                    case 0:
                        intent.putExtra("difficulty", 3);
                        quizzCreator.createMultipleSimpleQuizz(dataList, 3, quizzLevel);
                        intent.putExtra("timer", timer);
                        intent.putExtra("listQuizz", quizzCreator.listQuizz);
                        intent.putExtra("chrono", chrono);
                        startActivity(intent);
                        break;
                    case 1:
                        intent.putExtra("difficulty", 5);
                        quizzCreator.createMultipleSimpleQuizz(dataList, 5, quizzLevel);
                        intent.putExtra("timer", timer);
                        intent.putExtra("listQuizz", quizzCreator.listQuizz);
                        intent.putExtra("chrono", chrono);
                        startActivity(intent);
                        break;
                    case 2:
                        intent.putExtra("difficulty", 7);
                        quizzCreator.createMultipleSimpleQuizz(dataList, 7, quizzLevel);
                        intent.putExtra("timer", timer);
                        intent.putExtra("listQuizz", quizzCreator.listQuizz);
                        intent.putExtra("chrono", chrono);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        };
        builder.setItems(options, actionListener);
        builder.setNegativeButton("Revenir au menu", (DialogInterface.OnClickListener) (dialog, which) -> {
            dialog.cancel();
        });
        AlertDialog difficulty = builder.create();
        simpleQuizzButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quizzLevel = "simple";
                timer = 6000;
                difficulty.show();
            }
        });
        mediumQuizzButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quizzLevel = "medium";
                timer = 8000;
                difficulty.show();
            }
        });
        harcoreQuizzButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quizzLevel = "hardcore";
                try {
                    DataList dataList = getData();
                    quizzCreator.createMapsQuizz(dataList, 3);
                    intentMaps.putExtra("listQuizz", quizzCreator.listQuizzMaps);
                    intentMaps.putExtra("chrono", chrono);
                    startActivity(intentMaps);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        statButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentStat = new Intent(MainActivity.this, StatsActivity.class);
                startActivity(intentStat);
            }
        });

        Button listButton = findViewById(R.id.listButton);
        listButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, listQuestionActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Will get all data in a Json from an api
     * @return : DataList object
     * @throws IOException I hope it don't
     */
    private DataList getData() throws IOException {
        OkHttpClient client = new OkHttpClient();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Request request = new Request.Builder()
                .url(URL)
                .build();

        try (Response response = client.newCall(request).execute()) {
            Gson gson = new Gson();

            DataList dataList = gson.fromJson(response.body().string(), DataList.class);
            return dataList;
        }
    }
}