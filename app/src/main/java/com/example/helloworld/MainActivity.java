package com.example.helloworld;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // !!! ИЗМЕНЕНИЕ 1: Я изменил имя файла настроек на "TestPrefs",
    // чтобы гарантированно стереть старые данные о 100 запусках.
    private static final String PREFS_NAME = "TestPrefs";

    private static final String PREF_OPEN_COUNT = "OpenCount";
    private static final String PREF_IS_RATED = "IsRated";

    // !!! ИЗМЕНЕНИЕ 2: Поставил 1 вместо 10, чтобы вы увидели диалог СРАЗУ при запуске.
    // Когда все проверите, поменяйте обратно на 10.
    private static final int TARGET_OPEN_COUNT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkAppStartCount();
    }

    private void checkAppStartCount() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Считываем данные
        boolean isRated = prefs.getBoolean(PREF_IS_RATED, false);
        int openCount = prefs.getInt(PREF_OPEN_COUNT, 0) + 1;

        // Сохраняем новый счетчик
        prefs.edit().putInt(PREF_OPEN_COUNT, openCount).apply();

        // !!! ИЗМЕНЕНИЕ 3: Выводим сообщение на экран, чтобы вы видели, что код работает
        Toast.makeText(this, "Запуск номер: " + openCount, Toast.LENGTH_SHORT).show();

        if (isRated) {
            Toast.makeText(this, "Приложение уже оценено ранее", Toast.LENGTH_SHORT).show();
            return;
        }

        // Проверка: если запусков больше или равно цели
        if (openCount >= TARGET_OPEN_COUNT) {
            showRatingDialog();
        }
    }

    private void showRatingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Оцініть наш додаток");
        builder.setMessage("Нам важлива ваша думка! Будь ласка, поставте оцінку.");

        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setPadding(50, 50, 50, 50);

        final RatingBar ratingBar = new RatingBar(this);
        ratingBar.setNumStars(5);
        ratingBar.setStepSize(1.0f);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        ratingBar.setLayoutParams(params);

        linearLayout.addView(ratingBar);
        builder.setView(linearLayout);

        builder.setPositiveButton("Готово", (dialog, which) -> {
            float rating = ratingBar.getRating();

            if (rating == 0) {
                Toast.makeText(MainActivity.this, "Ви не обрали оцінку", Toast.LENGTH_SHORT).show();
                return;
            }

            markAsRated(); // Блокируем показы в будущем

            if (rating >= 4) {
                showPlayMarketDialog();
            } else {
                showFeedbackDialog();
            }
        });

        builder.setNegativeButton("Пізніше", (dialog, which) -> {
            // Если нажали "Позже", сбрасываем счетчик на 0, чтобы диалог появился снова
            resetOpenCount();
            dialog.dismiss();
        });

        builder.setCancelable(false);
        builder.show();
    }

    private void showFeedbackDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Що пішло не так?");
        builder.setMessage("Розкажіть, як ми можемо покращити додаток.");

        final EditText input = new EditText(this);
        input.setHint("Введіть ваш відгук тут...");

        LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.VERTICAL);
        container.setPadding(50, 20, 50, 20);
        container.addView(input);

        builder.setView(container);

        builder.setPositiveButton("Надіслати", (dialog, which) -> {
            Toast.makeText(MainActivity.this, "Дякуємо за відгук!", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Скасувати", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void showPlayMarketDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Дякуємо за високу оцінку!")
                .setMessage("Чи не могли б ви залишити відгук у Google Play?")
                .setPositiveButton("Звісно!", (dialog, which) -> openPlayStore())
                .setNegativeButton("Ні, дякую", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private void openPlayStore() {
        final String appPackageName = getPackageName();
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }

    private void markAsRated() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        prefs.edit().putBoolean(PREF_IS_RATED, true).apply();
    }

    private void resetOpenCount() {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        prefs.edit().putInt(PREF_OPEN_COUNT, 0).apply();
    }
}