package com.example.helloworld;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    private String[] songLyrics = {
            "Куплет 1: Наш перший рядок пісні.",
            "Приспів: Наш приспів, він повторюється.",
            "Куплет 2: Другий куплет, нові слова.",
            "Приспів: Наш приспів, він повторюється.",
            "Куплет 3: Фінальний куплет."
    };

    private int currentLineIndex = 0;
    Button songButton;


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
        songButton = findViewById(R.id.songButton);
        songButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNextLyric();
            }
        });
    }

    private void showNextLyric() {
        if (songLyrics == null || songLyrics.length == 0) {
            return;
        }

        String currentLine = songLyrics[currentLineIndex];
        Toast.makeText(this, currentLine, Toast.LENGTH_LONG).show();

        currentLineIndex++;
        if (currentLineIndex >= songLyrics.length) {
            currentLineIndex = 0;
        }
    }



}