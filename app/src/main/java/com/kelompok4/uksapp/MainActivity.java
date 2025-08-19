package com.kelompok4.uksapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private static final String SHARED_PREF_NAME = "user_pref";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in"; // Key untuk status login

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Cek status login
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);

        if (!isLoggedIn) {
            // Jika belum login, arahkan pengguna kembali ke halaman login
            startActivity(new Intent(getApplicationContext(), Login.class));
            finish(); // Tutup MainActivity
            return;
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Fragment selectedFragment;

            if (item.getItemId() == R.id.fr_healthrecord) {
                selectedFragment = new HealthEducationFragment();
            } else if (item.getItemId() == R.id.fr_reminder) {
                selectedFragment = new ReminderFragment();
            } else if (item.getItemId() == R.id.fr_profil) {
                selectedFragment = new ProfileFragment();
            } else {
                selectedFragment = new DashboardFragment();
            }

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            return true;
        });

        // Muat fragment default (Dashboard) hanya saat pertama kali
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DashboardFragment()).commit();
        }
    }
}
