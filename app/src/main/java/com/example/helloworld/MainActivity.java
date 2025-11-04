package com.example.helloworld;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // 0 = empty, 1 = X, 2 = O
    private int[] board = new int[9];
    private boolean xTurn = true; // хід X спочатку
    private ImageButton[] cells = new ImageButton[9];
    private Button resetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Ініціалізація кнопок
        cells[0] = findViewById(R.id.btn0);
        cells[1] = findViewById(R.id.btn1);
        cells[2] = findViewById(R.id.btn2);
        cells[3] = findViewById(R.id.btn3);
        cells[4] = findViewById(R.id.btn4);
        cells[5] = findViewById(R.id.btn5);
        cells[6] = findViewById(R.id.btn6);
        cells[7] = findViewById(R.id.btn7);
        cells[8] = findViewById(R.id.btn8);

        for (int i = 0; i < 9; i++) {
            cells[i].setOnClickListener(this);
            // Початково чисті
            board[i] = 0;
            cells[i].setImageDrawable(null);
            cells[i].setEnabled(true);
        }

        resetButton = findViewById(R.id.resetButton);
        resetButton.setOnClickListener(v -> resetGame());
    }

    @Override
    public void onClick(View v) {
        int index = -1;
        for (int i = 0; i < 9; i++) {
            if (v.getId() == cells[i].getId()) {
                index = i;
                break;
            }
        }
        if (index == -1) return;

        if (board[index] != 0) {
            Toast.makeText(this, "Ця клітина вже зайнята", Toast.LENGTH_SHORT).show();
            return;
        }

        if (xTurn) {
            // X
            cells[index].setImageResource(R.drawable.ic_cross);
            board[index] = 1;
        } else {
            // O
            cells[index].setImageResource(R.drawable.ic_circle);
            board[index] = 2;
        }

        cells[index].setEnabled(false);

        int winner = checkWinner();
        if (winner != 0) {
            String who = (winner == 1) ? "Хрестики" : "Нулики";
            Toast.makeText(this, who + " перемогли!", Toast.LENGTH_LONG).show();
            disableAll();
            return;
        } else if (isBoardFull()) {
            Toast.makeText(this, "Нічия!", Toast.LENGTH_LONG).show();
            return;
        }

        xTurn = !xTurn;
    }

    private void resetGame() {
        for (int i = 0; i < 9; i++) {
            board[i] = 0;
            cells[i].setImageDrawable(null);
            cells[i].setEnabled(true);
        }
        xTurn = true;
        Toast.makeText(this, "Нова гра", Toast.LENGTH_SHORT).show();
    }

    private void disableAll() {
        for (ImageButton b : cells) {
            b.setEnabled(false);
        }
    }

    private boolean isBoardFull() {
        for (int i = 0; i < 9; i++) {
            if (board[i] == 0) return false;
        }
        return true;
    }

    private int checkWinner() {
        int[][] lines = {
                {0,1,2}, {3,4,5}, {6,7,8},
                {0,3,6}, {1,4,7}, {2,5,8},
                {0,4,8}, {2,4,6}
        };
        for (int[] l : lines) {
            if (board[l[0]] != 0 &&
                    board[l[0]] == board[l[1]] &&
                    board[l[1]] == board[l[2]]) {
                return board[l[0]];
            }
        }
        return 0; // ніхто
    }

}
