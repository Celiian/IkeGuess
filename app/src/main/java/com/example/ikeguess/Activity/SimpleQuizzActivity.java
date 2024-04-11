package com.example.ikeguess.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import com.example.ikeguess.R;
import com.example.ikeguess.customClass.FlashCard;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SimpleQuizzActivity extends AppCompatActivity {

    private int currentQuestion;
    private int goodAnswer;
    private MediaPlayer mediaPlayer;
    private boolean chrono;
    private int timer;
    private SharedPreferences sharedPref;


    /**
     * Will display the image or show a button to play the sound, depending on the type of the media in the flashCard
     * @param flashcard : FlashCard object / the object that contain all the data of the current question
     */
    private void mediaDisplay(FlashCard flashcard) {
        if (flashcard.mediaType.equals("image")) {
            ImageView IkeaImageView = findViewById(R.id.IkeaImageView);
            Button buttonSound = findViewById(R.id.buttonSound);

            buttonSound.setVisibility(View.INVISIBLE);

            Picasso.get().load((String) flashcard.mediaUrl).into(IkeaImageView);

            IkeaImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SimpleQuizzActivity.this, furnitureImageActivity.class);
                    intent.putExtra("url", flashcard.mediaUrl);
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(SimpleQuizzActivity.this, (View) IkeaImageView, "furniture");
                    startActivity(intent, options.toBundle());
                }
            });


        } else {
            Button buttonSound = findViewById(R.id.buttonSound);

            buttonSound.setOnClickListener(new View.OnClickListener() {
                final Resources res = getResources();

                final int sound = res.getIdentifier(flashcard.mediaUrl, "raw", getPackageName());

                @Override
                public void onClick(View v) {
                    mediaPlayer = MediaPlayer.create(getBaseContext(), sound);

                    if (mediaPlayer.isPlaying()) {
                        mediaPlayer.stop();
                        mediaPlayer = MediaPlayer.create(getBaseContext(), sound);
                    } else {
                        mediaPlayer.start();
                    }
                }
            });
        }
    }


    /**
     * This function will add the radioButton of the possibles answers to the radioGroup, and set the number of the question
     * @param radioGroup : RadioGroup object / the radioGroup in which you want to add the radioButtons
     * @param flashcard : FlashCard object / the object that contain all the data of the current question
     */
    private void displayQuizz(RadioGroup radioGroup, FlashCard flashcard) {
        TextView idQuizzTextView = findViewById(R.id.idQuizzTextView);

        idQuizzTextView.setText(String.valueOf(currentQuestion + 1 + " / " + flashcard.numberOfQuestions));

        mediaDisplay(flashcard);

        flashcard.answers.forEach(name -> {
            RadioButton rb = new RadioButton(this);
            rb.setText(name);
            rb.setId(View.generateViewId());
            radioGroup.addView(rb);
        });

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_quizz);
        sharedPref = getSharedPreferences("ikeguess", Context.MODE_PRIVATE);

        ArrayList<FlashCard> listQuizz = getIntent().getParcelableArrayListExtra("listQuizz");
        goodAnswer = getIntent().getIntExtra("goodAnswer", 0);

        ProgressBar progressBar = findViewById(R.id.progressBar);
        RadioGroup radioGroup = findViewById(R.id.radioGroup);
        Button validateButton = findViewById(R.id.validateButton);

        //TextView validateTextView = findViewById(R.id.validateTextView);
        currentQuestion = getIntent().getIntExtra("questionIndex", 0);


        CountDownTimer mCountDownTimer;
        progressBar.setProgress(100);
        timer = getIntent().getIntExtra("timer", 0);
        mCountDownTimer = new CountDownTimer(timer, 1000) {
            int i = 0;

            @Override
            public void onTick(long millisUntilFinished) {
                if (validateButton.getText() != "Suivant") {
                    i++;
                    progressBar.setProgress(100 - (i * 100 / (timer / 1000)));
                }
            }

            @Override
            public void onFinish() {
                if (validateButton.getText() != "Suivant") {
                    progressBar.setProgress(0);
                    Intent intent = new Intent(SimpleQuizzActivity.this, SimpleQuizzActivity.class);
                    intent.putExtra("questionIndex", currentQuestion + 1);
                    intent.putExtra("chrono", chrono);
                    intent.putExtra("timer", timer);
                    intent.putExtra("goodAnswer", goodAnswer);
                    intent.putExtra("listQuizz", listQuizz);
                    startActivity(intent);
                    finish();
                    return;
                }
            }
        };

        if (currentQuestion == listQuizz.size()) {
            Intent intent = new Intent(SimpleQuizzActivity.this, GameResumeActivity.class);
            intent.putExtra("goodAnswer", goodAnswer);
            intent.putExtra("numberQuestions", listQuizz.size());
            startActivity(intent);
            finish();
            return;
        }

            TextView validateTextView = findViewById(R.id.validateTextView);

        FlashCard actualFlashCard = listQuizz.get(currentQuestion);
        displayQuizz(radioGroup, actualFlashCard);


        if("son".equals(actualFlashCard.mediaType)) {
            TextView questionFr = findViewById(R.id.questionFr);
            TextView questionSud = findViewById(R.id.questionSud);

            questionFr.setText("Quel est la traduction du nom de ce meuble ?");
            questionSud.setText("Vad är översättningen av namnet på denna möbel?"
            );
        }
        validateButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                int id = radioGroup.getCheckedRadioButtonId();

                if (validateButton.getText() == "Suivant") {
                    Intent intent = new Intent(SimpleQuizzActivity.this, SimpleQuizzActivity.class);
                    intent.putExtra("questionIndex", currentQuestion + 1);
                    intent.putExtra("goodAnswer", goodAnswer);
                    intent.putExtra("listQuizz", listQuizz);
                    intent.putExtra("timer", timer);
                    intent.putExtra("chrono", chrono);
                    radioGroup.removeAllViews();
                    startActivity(intent);
                    finish();
                    return;
                }

                if (id == -1) {
                    Toast.makeText(SimpleQuizzActivity.this, "Veuillez sélectionner une réponse", Toast.LENGTH_SHORT).show();
                    return;
                }
                SharedPreferences.Editor editor = sharedPref.edit();
                RadioButton button = radioGroup.findViewById(id);
                if (button.getText().equals(actualFlashCard.goodAnswer)) {
                    validateTextView.setText("Super ! Tu as eu bon.");
                    goodAnswer++;
                    if("son".equals(actualFlashCard.mediaType)){
                        editor.putInt("MediumGoodAnswer", sharedPref.getInt("SimpleGoodAnswer", 0) + 1);
                    }
                    else {
                        editor.putInt("SimpleGoodAnswer", sharedPref.getInt("SimpleGoodAnswer", 0) + 1);
                    }
                } else {
                    validateTextView.setText("Dommage ! La bonne réponse était : " + actualFlashCard.goodAnswer);
                    if ("son".equals(actualFlashCard.mediaType)) {
                        editor.putInt("SimpleBadAnswer", sharedPref.getInt("SimpleGoodAnswer", 0) + 1);
                    } else {
                        editor.putInt("MediumBadAnswer", sharedPref.getInt("SimpleGoodAnswer", 0) + 1);
                    }
                }

                editor.apply();


                radioGroup.removeAllViews();
                validateButton.setText("Suivant");
            }
        });

        chrono = getIntent().getBooleanExtra("chrono", false);

        if (chrono) {
            progressBar.setVisibility(View.VISIBLE);
            mCountDownTimer.start();
        }
    }

}