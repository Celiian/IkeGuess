package com.example.ikeguess.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ikeguess.R;
import com.squareup.picasso.Picasso;

public class furnitureImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_furniture_image);
        ImageView IkeaImageView = findViewById(R.id.IkeaImageView);
        String url = getIntent().getStringExtra("url");
        Picasso.get().load(url).into(IkeaImageView);

        View clickView = findViewById(R.id.clickView);
        clickView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                supportFinishAfterTransition();
            }
        });


        }
}