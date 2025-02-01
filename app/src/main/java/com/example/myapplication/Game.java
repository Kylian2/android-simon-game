package com.example.myapplication;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class Game extends AppCompatActivity {

    private ArrayList<Integer> pattern = new ArrayList<>();
    private ArrayList<Integer> currentGuess = new ArrayList<>();
    private int score = 0;
    private boolean isUserTurn = false;

    private Random random = new Random();
    private Handler handler = new Handler();

    private Button green, red, yellow, blue;
    private TextView scoreLabel;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.game);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.game), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button pause = findViewById(R.id.pause);
        Button resume = findViewById(R.id.resume);
        Button finish = findViewById(R.id.finish);

        ConstraintLayout pauseMenu = findViewById(R.id.pauseMenu);

        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseMenu.setVisibility(View.VISIBLE);
            }
        });

        resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseMenu.setVisibility(View.INVISIBLE);
            }
        });

        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        green = findViewById(R.id.green);
        red = findViewById(R.id.red);
        yellow = findViewById(R.id.yellow);
        blue = findViewById(R.id.blue);
        scoreLabel = findViewById(R.id.score);

        setButtonListeners();

        startNewRound();

    }

    private void startNewRound() {
        isUserTurn = false;
        currentGuess.clear();
        scoreLabel.setText("Score : " + score);
        pattern.add(random.nextInt(4) + 1);
        showPattern(0);
    }

    private void showPattern(int index) {
        if (index < pattern.size()) {
            int tile = pattern.get(index);
            Button selectedButton = getButtonFromTile(tile);

            // Afficher la tile
            selectedButton.setAlpha(0.5f);
            handler.postDelayed(() -> {
                selectedButton.setAlpha(1.0f);
                handler.postDelayed(() -> showPattern(index + 1), 500);
            }, 500);

        } else {
            isUserTurn = true;
        }
    }

    private void setButtonListeners() {
        View.OnClickListener buttonClickListener = v -> {
            if (!isUserTurn) return;

            int tile = getTileFromButton((Button) v);
            currentGuess.add(tile);

            if (!checkGuess()) {
                Toast.makeText(this, "Mauvaise réponse ! Score final : " + score, Toast.LENGTH_SHORT).show();
                resetGame();
            } else if (currentGuess.size() == pattern.size()) {
                score++;
                Toast.makeText(this, "Bien joué !", Toast.LENGTH_SHORT).show();
                handler.postDelayed(this::startNewRound, 1000);
            }
        };

        green.setOnClickListener(buttonClickListener);
        red.setOnClickListener(buttonClickListener);
        yellow.setOnClickListener(buttonClickListener);
        blue.setOnClickListener(buttonClickListener);
    }

    private boolean checkGuess() {
        for (int i = 0; i < currentGuess.size(); i++) {
            if (!currentGuess.get(i).equals(pattern.get(i))) {
                return false;
            }
        }
        return true;
    }

    private void resetGame() {
        pattern.clear();
        currentGuess.clear();
        score = 0;
        startNewRound();
    }

    private Button getButtonFromTile(int tile) {
        switch (tile) {
            case 1: return green;
            case 2: return red;
            case 3: return yellow;
            case 4: return blue;
            default: return null;
        }
    }

    private int getTileFromButton(Button button) {
        if (button == green) return 1;
        if (button == red) return 2;
        if (button == yellow) return 3;
        if (button == blue) return 4;
        return -1;
    }
}
