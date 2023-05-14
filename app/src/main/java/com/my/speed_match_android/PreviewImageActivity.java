package com.my.speed_match_android;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class PreviewImageActivity extends AppCompatActivity {

    private static final int CHOOSE_ACTIVITY_REQUEST_CODE = 1;
    int scoreResult = 0;
    int maxScore = 5;
    TextView scoreText;
    ImageView previewImage;
    int selectedImageId = -1;
    ArrayList<Integer> images;
    Intent chooseActivity;
    int counterMatches = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent cameIntent = getIntent();
        if (cameIntent != null){
            maxScore = cameIntent.getIntExtra("maxScore", 10);
        }
        images = new ArrayList<>(Arrays.asList(R.drawable.baseline_4g_mobiledata_24,
                R.drawable.baseline_4g_mobiledata_24_red,
                R.drawable.baseline_4g_plus_mobiledata_24,
                R.drawable.baseline_4g_plus_mobiledata_24_pink,
                R.drawable.baseline_accessible_24,
                R.drawable.baseline_accessible_24_orange,
                R.drawable.baseline_accessible_forward_24,
                R.drawable.baseline_accessible_forward_24_brown));
        chooseActivity = new Intent(this, ChooseActivity.class);
        chooseActivity.putIntegerArrayListExtra("images", images);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_image);

        scoreText = findViewById(R.id.text_score);
        scoreText.setText(scoreResult + " / " + maxScore);
        previewImage = findViewById(R.id.image_one);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onPostResume() {
        if (selectedImageId == -1){
            Random random = new Random();
            int imageIndex = random.nextInt(images.size());
            selectedImageId = images.get(imageIndex);
            previewImage.setImageResource(selectedImageId);
            chooseActivity.putExtra("selectedImageId", selectedImageId);
            Log.i("game", "Post -> " + selectedImageId);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivityForResult(chooseActivity, CHOOSE_ACTIVITY_REQUEST_CODE);
                }
            }, 1000);
        }
        super.onPostResume();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            counterMatches++;
            if (data == null){
                setResult(RESULT_CANCELED);
                finish();
                return;
            }
            boolean result = data.getBooleanExtra("right", false);
            if (result){
                scoreResult++;
                scoreText.setTextColor(getResources().getColor(R.color.right));
            }
            else{
                scoreText.setTextColor(getResources().getColor(R.color.mistake));
            }
            selectedImageId = -1;
            scoreText.setText(scoreResult + " / " + maxScore);
            if (counterMatches == maxScore){
                Intent intent = new Intent();
                intent.putExtra("scores", scoreResult);
                setResult(RESULT_OK, intent);
                finish();
            }
        }
        if (requestCode == CHOOSE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_CANCELED){
            setResult(RESULT_CANCELED);
            finish();
        }
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
        Log.i("game", "Restore " + selectedImageId);
    }
}