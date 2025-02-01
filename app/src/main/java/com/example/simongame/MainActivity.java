package com.example.simongame;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private static int EASY = 1;
    private static int MEDIUM = 2;
    private static int HARD = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        final int[] difficulty = {0};

        Button start = findViewById(R.id.start);
        start.setEnabled(false);
        Button easy = findViewById(R.id.easyButton);
        Button medium = findViewById(R.id.mediumButton);
        Button hard = findViewById(R.id.hardButton);

        TextView difficultyLabel = findViewById(R.id.difficultyLabel);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(difficulty[0] != 0){
                    Log.d("DEBUG", "Début de la partie avec le mode de difficulté " + difficulty[0]);
                    Intent intent = new Intent(MainActivity.this, Game.class);
                    startActivity(intent);
                }
            }
        });

        easy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                difficulty[0] = EASY;
                start.setEnabled(true);
                difficultyLabel.setText("La difficulté est définie sur FACILE");
            }
        });

        medium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                difficulty[0] = MEDIUM;
                start.setEnabled(true);
                difficultyLabel.setText("La difficulté est définie sur MOYENNE");
            }
        });

        hard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                difficulty[0] = HARD;
                start.setEnabled(true);
                difficultyLabel.setText("La difficulté est définie sur DIFFICILE");
            }
        });
    }
}