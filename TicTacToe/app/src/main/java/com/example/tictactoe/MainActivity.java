package com.example.tictactoe;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private String currentPlayer = "X";
    private String[][] gameState = new String[3][3];
    private boolean gameActive = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                gameState[i][j] = "";
            }
        }

        findViewById(R.id.button_reset).setVisibility(View.GONE);
    }

    public void onCellClick(View view) {
        Button buttonClicked = (Button) view;
        String tag = (String) buttonClicked.getTag();

        int row = Integer.parseInt(tag.split(",")[0]);
        int col = Integer.parseInt(tag.split(",")[1]);

        if (gameState[row][col].equals("") && gameActive) {
            gameState[row][col] = currentPlayer;
            buttonClicked.setText(currentPlayer);

            if (checkWinner()) {
                gameActive = false;
                Toast.makeText(this, currentPlayer + " caștiga!", Toast.LENGTH_SHORT).show();
                findViewById(R.id.button_reset).setVisibility(View.VISIBLE);
            } else if (checkDraw()) {
                gameActive = false;
                Toast.makeText(this, "Remiza!", Toast.LENGTH_SHORT).show();
                findViewById(R.id.button_reset).setVisibility(View.VISIBLE);
            } else {
                currentPlayer = (currentPlayer.equals("X")) ? "O" : "X";
            }
        }
    }

    private boolean checkWinner() {
        for (int i = 0; i < 3; i++) {
            if (gameState[i][0].equals(gameState[i][1]) && gameState[i][0].equals(gameState[i][2]) && !gameState[i][0].equals("")) {
                return true;
            }
            if (gameState[0][i].equals(gameState[1][i]) && gameState[0][i].equals(gameState[2][i]) && !gameState[0][i].equals("")) {
                return true;
            }
        }
        if (gameState[0][0].equals(gameState[1][1]) && gameState[0][0].equals(gameState[2][2]) && !gameState[0][0].equals("")) {
            return true;
        }
        if (gameState[0][2].equals(gameState[1][1]) && gameState[0][2].equals(gameState[2][0]) && !gameState[0][2].equals("")) {
            return true;
        }
        return false;
    }

    private boolean checkDraw() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (gameState[i][j].equals("")) {
                    return false;
                }
            }
        }
        return true;
    }

    public void resetGame(View view) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                gameState[i][j] = "";
                Button button = findViewById(getResources().getIdentifier("button" + i + j, "id", getPackageName()));
                button.setText("");
            }
        }
        currentPlayer = "X";
        gameActive = true;
        findViewById(R.id.button_reset).setVisibility(View.GONE);
    }
}
