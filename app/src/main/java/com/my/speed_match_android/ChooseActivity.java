package com.my.speed_match_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Random;

public class ChooseActivity extends AppCompatActivity {

    ImageView previewImage;
    int selectedImageId = -1;
    int previewImageId;
    Button noButton;
    Button yesButton;
    boolean match;
    ArrayList<Integer> images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);
        previewImage = findViewById(R.id.image_one);
        Intent cameIntent = getIntent();
        if (cameIntent == null){
            setResult(RESULT_CANCELED);
            finish();
            return;
        }
        images = cameIntent.getIntegerArrayListExtra("images");
        previewImageId = cameIntent.getIntExtra("selectedImageId", -1);
        if (previewImageId == -1){
            Log.i("game", "Image id not putted or ImageId = 1???");
            setResult(RESULT_CANCELED);
            finish();
            return;
        }
        noButton = findViewById(R.id.no_button);
        yesButton = findViewById(R.id.yes_button);

        noButton.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.putExtra("right", previewImageId != selectedImageId);
            setResult(RESULT_OK, intent);
            finish();
        });

        yesButton.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.putExtra("right", previewImageId == selectedImageId);
            setResult(RESULT_OK, intent);
            finish();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (selectedImageId == -1) {
            Random random = new Random();
            if (random.nextInt(10) <= 2) {
                previewImage.setImageResource(previewImageId);
                selectedImageId = previewImageId;
            } else {
                selectedImageId = images.get(random.nextInt(images.size()));
                previewImage.setImageResource(selectedImageId);
            }
        }
        Log.i("game", "Choose: Resume -> " + selectedImageId + " = " + previewImageId);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("selectedImageId", selectedImageId);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        selectedImageId = savedInstanceState.getInt("selectedImageId", -1);
        previewImage.setImageResource(selectedImageId);
    }
}