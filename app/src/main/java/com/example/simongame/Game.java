package com.example.simongame;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
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

    private int time = 500;

    private int difficulty;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.game);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.game), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Log.d("DEBUG", MainActivity.DIFFICULTY);
        difficulty = getIntent().getIntExtra(MainActivity.DIFFICULTY, 0);
        if(difficulty == 0){
            finish();
        }
        Log.d("DEBUG", "Début de la partie avec le mode de difficulté " + difficulty);

        setDifficulty(difficulty);

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
        initButton(difficulty);

        startNewRound();

    }

    private void setDifficulty(int difficulty) {
        TextView difficultyLabel = findViewById(R.id.difficultyIndicator);
        String difficultyText;
        switch (difficulty){
            case 1:
                difficultyText = "Facile";
                break;
            case 2:
                difficultyText = "Moyenne";
                break;
            case 3:
                difficultyText = "Difficile";
                break;
            default:
                difficultyText = "Indefinie";
        }
        difficultyLabel.setText(difficultyText);
    }

    private void initButton(int difficulty){
        if (difficulty == 3) {
            green.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#33415C")));
            red.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#7D8597")));
            yellow.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#5C677D")));
            blue.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#979DAC")));
        }
    }
    private void startNewRound() {
        isUserTurn = false;
        currentGuess.clear();
        scoreLabel.setText("Score : " + score);
        pattern.add(random.nextInt(4) + 1);
        if(difficulty != 1 && pattern.size() % 2 == 0 && pattern.size() < 20){
            time -= 75;
        }
        handler.postDelayed(() -> showPattern(0), pattern.size() == 1 ? 1000 : time);
    }

    private void showPattern(int index) {
        if (index < pattern.size()) {
            int tile = pattern.get(index);
            Button selectedButton = getButtonFromTile(tile);

            // Afficher la tile
            selectedButton.setAlpha(0.5f);
            handler.postDelayed(() -> {
                selectedButton.setAlpha(1.0f);
                handler.postDelayed(() -> showPattern(index + 1), time);
            }, time);

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
