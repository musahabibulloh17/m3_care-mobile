package com.kelompok4.uksapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class HalamanAwal extends AppCompatActivity {

    private ImageView logoImage;
    private TextView titleText, subtitleText;
    private Button btnLogin, btnRegister;

    private static final String SHARED_PREF_NAME = "user_pref";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in"; // Key untuk status login

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_halaman_awal); // Sesuaikan dengan nama layout XML yang digunakan

        // Inisialisasi elemen UI
        logoImage = findViewById(R.id.logoImage);
        titleText = findViewById(R.id.titleText);
        subtitleText = findViewById(R.id.subtitleText);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        // Cek status login, jika sudah login arahkan ke MainActivity
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);

        if (isLoggedIn) {
            // Jika sudah login, langsung buka MainActivity
            Intent intent = new Intent(HalamanAwal.this, MainActivity.class);
            startActivity(intent);
            finish(); // Tutup HalamanAwal agar tidak kembali lagi
        }

        // Set onClickListener untuk tombol login
        btnLogin.setOnClickListener(v -> {
            // Intent untuk pindah ke halaman Login
            Intent intent = new Intent(HalamanAwal.this, Login.class);
            startActivity(intent);
        });

        // Set onClickListener untuk tombol register
        btnRegister.setOnClickListener(v -> {
            // Intent untuk pindah ke halaman Register
            Intent intent = new Intent(HalamanAwal.this, Register.class);
            startActivity(intent);
        });
    }
}
