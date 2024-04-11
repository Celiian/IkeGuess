package com.example.ikeguess.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.ikeguess.R;
import com.example.ikeguess.customClass.FlashCardMaps;
import com.example.ikeguess.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {


    private GoogleMap mMap;

    private FloatingActionButton rewindFab;

    private FloatingActionButton nextFab;

    private FloatingActionButton infoFab;

    private ArrayList<FlashCardMaps> listQuizz;

    private int currentQuestion;

    private LatLng furnitureLocation;

    private ArrayList<LatLng> historyPoints = new ArrayList<>();

    private int clickLeft = 10;

    private int goodAnswer;

    private int actualPoint = 0;

    int duration;

    private int zoom = 7;

    boolean chrono;
    private SharedPreferences sharedPref;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        com.example.ikeguess.databinding.ActivityMapsBinding binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        listQuizz = getIntent().getParcelableArrayListExtra("listQuizz");
        goodAnswer = getIntent().getIntExtra("goodAnswer", 0);
        currentQuestion = getIntent().getIntExtra("questionIndex", 0);
        chrono = getIntent().getBooleanExtra("chrono", false);
        sharedPref = getSharedPreferences("ikeguess", Context.MODE_PRIVATE);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        TextView questionNumber = findViewById(R.id.questionNumber);
        TextView answerName = findViewById(R.id.answerName);
        TextView question = findViewById(R.id.question);
        ImageView furnitureImg = findViewById(R.id.furnitureImageView);
        rewindFab = findViewById(R.id.rewindFab);
        nextFab = findViewById(R.id.nextFab);
        infoFab = findViewById(R.id.infoButton);

        Picasso.get().load(listQuizz.get(currentQuestion).imageUrl).into(furnitureImg);
        answerName.setText(listQuizz.get(currentQuestion).answerName);
        rewindFab.setVisibility(View.INVISIBLE);
        nextFab.setImageResource(R.drawable.surrender);
        questionNumber.setText("Question : " + (currentQuestion + 1) + " / " + listQuizz.size());

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Règles du mode Hardcore");
        alert.setMessage(
                "Pour chaque meuble, vous avez jusqu'à 10 chances pour trouver l'île ou le lac qui lui as donné son nom. " +
                        "Lorsque vous trouvez la bonne réponse ou que vous épuisez toutes vos chances vous verrez changer d'icone le bouton de plus en bas a droite. " +
                        "Ce bouton vous permettras de \"rewind\" : revoir les points que vous avez posés dans l'ordre."
        );

        alert.setNegativeButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });


        infoFab.setOnClickListener(action -> {
            alert.show();

        });


        rewindFab.setOnClickListener(action -> {
            mMap.clear();
            rewindAction();
            actualPoint = 0;
        });

        nextFab.setOnClickListener(action -> {
            rewindFab.setVisibility(View.VISIBLE);
            nextFab.setImageResource(R.drawable.next);
            drawCircle(furnitureLocation, listQuizz.get(currentQuestion).radius, false, true, Color.RED);
            infoFab.setEnabled(false);
            infoFab.setVisibility(View.INVISIBLE);
            nextFabAction(true);
        });

        if (listQuizz.get(currentQuestion).type == "île"){
            question.setText("A quelle île fait référence ce meuble?");
        }
        else {
            question.setText("A quelle lac fait référence ce meuble?");
        }
        furnitureImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapsActivity.this, furnitureImageActivity.class);

                intent.putExtra("url", listQuizz.get(currentQuestion).imageUrl);
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(MapsActivity.this, (View) furnitureImg, "furniture");
                startActivity(intent, options.toBundle());
            }
        });
        ProgressBar progressBar = findViewById(R.id.progressBar);

        CountDownTimer mCountDownTimer;
        progressBar.setProgress(100);
        mCountDownTimer = new CountDownTimer(20000, 1000) {
            int i = 0;

            @Override
            public void onTick(long millisUntilFinished) {
                if (rewindFab.getVisibility() == View.INVISIBLE) {
                    i++;
                    progressBar.setProgress(100 - (i * 100 / (20000 / 1000)));
                }
            }
            @Override
            public void onFinish() {
                if (rewindFab.getVisibility() == View.INVISIBLE) {
                    rewindFab.setVisibility(View.VISIBLE);
                    infoFab.setEnabled(false);
                    infoFab.setVisibility(View.INVISIBLE);
                    nextFab.setImageResource(R.drawable.next);
                    nextFabAction(true);
                    progressBar.setProgress(0);

                    @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putInt("HardcoreBadAnswer", sharedPref.getInt("HardcoreBadAnswer", 0) + 1);
                }
            }
        };
        if(chrono) {
            progressBar.setVisibility(View.VISIBLE);
            mCountDownTimer.start();
        }
    }


    /**
     * Will take action once the map is ready
     * @param googleMap : GoogleMap object / the main object of gmap to use
     */
    @SuppressLint("SetTextI18n")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        TextView chanceLeft = findViewById(R.id.chanceLeft);
        chanceLeft.setText(clickLeft + " chances restantes");

        furnitureLocation = new LatLng(listQuizz.get(currentQuestion).latAnswer, listQuizz.get(currentQuestion).lgtAnswer);

        double randLat = (Math.random() * 2) - 1;
        double randLng = (Math.random() * 2) - 1;

        LatLng furnitureLocationLarge = new LatLng(furnitureLocation.latitude + randLat, furnitureLocation.longitude + randLng);
        drawCircle(furnitureLocationLarge, 200000, false, true, Color.RED);

        drawCircle(furnitureLocation, listQuizz.get(currentQuestion).radius, true, false, Color.GREEN);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(furnitureLocationLarge, 5));

        mMap.setOnCircleClickListener(circle -> {
            circle.setVisible(true);
            circle.setStrokeColor(Color.GREEN);
            historyPoints.add(circle.getCenter());
            clickLeft--;
            goodAnswer++;
            finishQuestion();
            chanceLeft.setText("Bravo ! Vous avez trouvé en " + (10 - clickLeft) + " essais");
            @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt("HardcoreGoodAnswer", sharedPref.getInt("HardcoreGoodAnswer", 0) + 1);
        });


        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                if (rewindFab.getVisibility() == View.INVISIBLE) {
                    mMap.addMarker(new MarkerOptions().position(point));
                    helper(furnitureLocation, point);
                    historyPoints.add(point);
                    clickLeft--;
                    chanceLeft.setText(clickLeft + " chances left");

                    if (clickLeft == 0) {
                        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putInt("HardcoreBadAnswer", sharedPref.getInt("HardcoreBadAnswer", 0) + 1);
                        drawCircle(furnitureLocation, listQuizz.get(currentQuestion).radius, false, true, Color.RED);
                        finishQuestion();
                    }
                }
            }
        });
    }

    /**
     * Draw a circle on the map
     * @param point : LatLng object / coordinates of the center of the circle
     * @param radius : int type / size of the radius of the circle (in metters)
     * @param clickable : boolean type / indicate if the circle can be clickable
     * @param visible : boolean type / indicate if the circle is visible
     * @param color : int type / use the Color class (Color.GREEN) to choose a color
     */
    private void drawCircle(LatLng point, int radius, boolean clickable, boolean visible, int color) {

        CircleOptions circleOptions = new CircleOptions();

        circleOptions.center(point);

        circleOptions.radius(radius);

        circleOptions.strokeColor(color);

        circleOptions.fillColor(Color.TRANSPARENT);

        circleOptions.strokeWidth(2);

        circleOptions.clickable(clickable);

        circleOptions.visible(visible);

        mMap.addCircle(circleOptions);
    }

    /**
     * Will display some help on top of the screen
     * @param center : LatLng object / the position of the answer
     * @param actualPos : LatLng object / the position of the last try
     */
    @SuppressLint("SetTextI18n")
    private void helper(LatLng center, LatLng actualPos) {

        TextView lat_pos = findViewById(R.id.lat_pos);
        TextView lgt_pos = findViewById(R.id.lgt_pos);

        if (actualPos.latitude > center.latitude) {
            lat_pos.setText("Le meuble est plus au Sud");
        } else if (actualPos.latitude < center.latitude) {
            lat_pos.setText("Le meuble est plus au Nord");
        }

        if (actualPos.longitude > center.longitude) {
            lgt_pos.setText("Le meuble est plus à l'Ouest");
        } else if (actualPos.longitude < center.longitude) {
            lgt_pos.setText("Le meuble est plus à l'Est");
        }
    }

    /**
     * Will change the floatingActionButtons of the bottom of the screen to end this question
     */
    private void finishQuestion() {
        rewindFab.setVisibility(View.VISIBLE);
        infoFab.setEnabled(false);
        infoFab.setVisibility(View.INVISIBLE);
        nextFab.setImageResource(R.drawable.next);

        nextFabAction(true);
    }

    /**
     * will use the historyPoint list to move to all guess try from the user and add a marker on each.
     */
    private void rewindAction() {
        if (actualPoint <= historyPoints.size() - 1) {
            LatLng point = historyPoints.get(actualPoint);

            if (actualPoint < historyPoints.size() - 1) {
                LatLng nextPoint = historyPoints.get(actualPoint + 1);
                double distance = distance(point.latitude, point.longitude, nextPoint.latitude, nextPoint.longitude);
                if (distance < 1) {
                    zoom = 15;
                    duration = 1200;
                } else if (distance < 10) {
                    zoom = 12;
                    duration = 1500;
                } else if (distance < 20) {
                    zoom = 10;
                    duration = 1700;
                } else if (distance < 50) {
                    zoom = 9;
                    duration = 2000;
                } else if (distance > 500) {
                    zoom = 6;
                    duration = 4000;
                } else if (distance > 100) {
                    zoom = 7;
                    duration = 3000;
                } else {
                    zoom = 8;
                    duration = 3000;
                }
            }

            if (actualPoint == historyPoints.size() - 1) {
                zoom = 15;
                duration = 4000;
            }
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point, zoom), duration, new GoogleMap.CancelableCallback() {
                @Override
                public void onCancel() {
                }

                @Override
                public void onFinish() {
                    if (actualPoint <= historyPoints.size() - 1) {
                        // Placing a marker on the position after animated
                        Marker marker = mMap.addMarker(new MarkerOptions().position(point).title("Guess " + actualPoint));
                        if (actualPoint > 0) {
                            drawLine(point, historyPoints.get(actualPoint - 1));
                        }
                        actualPoint++;
                        rewindAction();
                    }
                    if (actualPoint - 1 == historyPoints.size() - 1) {
                        drawCircle(furnitureLocation, listQuizz.get(currentQuestion).radius, false, true, Color.GREEN);
                    }
                }
            });
        }
    }

    /**
     * Change the two floating actions buttons, the bottom right become the "next" button
     * and the bottom left become the "rewind" button
     * @param move : boolean type / indicate if the map need to zoom on the answer point
     */
    private void nextFabAction(boolean move) {
        if(move) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(furnitureLocation, 12), 5000, new GoogleMap.CancelableCallback() {
                @Override
                public void onCancel() {
                }

                @Override
                public void onFinish() {
                }
            });
        }
        nextFab.setOnClickListener(v -> {
            if (currentQuestion == listQuizz.size() - 1) {
                Intent intent = new Intent(MapsActivity.this, GameResumeActivity.class);
                intent.putExtra("goodAnswer", goodAnswer);
                intent.putExtra("numberQuestions", listQuizz.size());
                startActivity(intent);
                finish();
                return;
            } else {
                Intent intent = new Intent(MapsActivity.this, MapsActivity.class);
                intent.putExtra("listQuizz", listQuizz);
                intent.putExtra("chrono", chrono);
                intent.putExtra("goodAnswer", goodAnswer);
                intent.putExtra("questionIndex", currentQuestion + 1);
                startActivity(intent);
                finish();
                return;
            }
        });
    }

    /**
     * Calculate the distance between two points with their lat and long
     * @param lat1 : double latitude of point 1
     * @param lon1 : double longitude of point 1
     * @param lat2 : double latitude of point 2
     * @param lon2 : double longitude of point 2
     * @return
     */
    public static double distance(double lat1, double lon1, double lat2,
                                  double lon2) {

        final int R = 6371; // Radius of the earth

        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c; // convert to meters

        double height = 0;

        distance = Math.pow(distance, 2) + Math.pow(height, 2);

        return Math.sqrt(distance);
    }

    /**
     * Draw a red line between two coordinates (points)
     * @param point1 : LatLng object
     * @param point2 : LatLng object
     */
    public void drawLine(LatLng point1, LatLng point2) {
        mMap.addPolyline((new PolylineOptions()).add(point1, point2).
                // below line is use to specify the width of poly line.
                        width(5)
                // below line is use to add color to our poly line.
                .color(Color.RED)
                // below line is to make our poly line geodesic.
                .geodesic(false));
    }
}