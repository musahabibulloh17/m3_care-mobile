package com.kelompok4.uksapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import android.util.Log;  // Import Log

public class Login extends AppCompatActivity {

    private static final String SHARED_PREF_NAME = "user_pref"; // Nama SharedPreferences
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_SESSION_ID = "session_id";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";
    private static final String TAG = "LoginActivity";  // Menambahkan TAG untuk log

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView btnRegister, forgetPasswordText;
    private ImageView showHidePassword; // ImageView untuk show/hide password
    private boolean isPasswordVisible = false; // Untuk melacak status password

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Inisialisasi UI
        etEmail = findViewById(R.id.emailInput);
        etPassword = findViewById(R.id.passwordInput);
        btnLogin = findViewById(R.id.loginButton);
        btnRegister = findViewById(R.id.registerText);
        forgetPasswordText = findViewById(R.id.forgetPasswordText);
        showHidePassword = findViewById(R.id.passwordIcon); // Menambahkan ImageView

        // Menambahkan fungsi backButton
        ImageButton backButton = findViewById(R.id.backButton); // Menghubungkan ImageButton
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Intent untuk kembali ke HalamanAwal
                Intent intent = new Intent(Login.this, HalamanAwal.class);
                startActivity(intent);
                finish(); // Menutup activity Login setelah berpindah ke HalamanAwal
            }
        });

        // Navigasi ke halaman Register
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Register.class));
            }
        });

        // Proses Login
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Email dan Password tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                    return;
                }

                // URL API Login
                String url = Db_Contract.urlLogin + "?email=" + email + "&password=" + password;

                // Kirim permintaan login
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    String status = response.getString("status");
                                    if (status.equals("success")) {
                                        Toast.makeText(getApplicationContext(), response.getString("message"), Toast.LENGTH_SHORT).show();

                                        // Dapatkan data dari respons
                                        String userId = response.getString("user_id");
                                        String sessionId = response.getString("session_id");

                                        // Simpan data ke SharedPreferences
                                        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("email", email); // Menyimpan email ke SharedPreferences
                                        editor.putString(KEY_USER_ID, userId);
                                        editor.putString(KEY_SESSION_ID, sessionId);
                                        editor.putBoolean(KEY_IS_LOGGED_IN, true);
                                        editor.apply();

                                        // Pindah ke halaman MainActivity setelah login sukses
                                        Intent intent = new Intent(Login.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Login Gagal: " + response.getString("message"), Toast.LENGTH_SHORT).show();
                                        Log.e(TAG, "Login Failed: " + response.getString("message"));  // Log error login gagal
                                    }
                                } catch (JSONException e) {
                                    Log.e(TAG, "Error parsing response: " + e.getMessage());  // Log error saat parsing JSON
                                    e.printStackTrace();
                                    Toast.makeText(getApplicationContext(), "Error parsing response: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Volley error: " + error.getMessage());  // Log error Volley
                        Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                // Tambahkan request ke queue
                requestQueue.add(jsonObjectRequest);
            }
        });

        // Navigasi ke halaman ResetPassword saat teks "Forget password?" diklik
        forgetPasswordText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, ResetPassword.class);
                startActivity(intent);
            }
        });

        // Show/Hide password
        showHidePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPasswordVisible) {
                    etPassword.setTransformationMethod(android.text.method.PasswordTransformationMethod.getInstance());
                    showHidePassword.setImageResource(R.drawable.ic_visibility_off); // Ganti icon
                } else {
                    etPassword.setTransformationMethod(null); // Show password
                    showHidePassword.setImageResource(R.drawable.ic_visibility); // Ganti icon
                }
                isPasswordVisible = !isPasswordVisible; // Toggle status
            }
        });
    }

    // Fungsi untuk hashing password menggunakan SHA-256
    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "Hashing error: " + e.getMessage());  // Log error saat hashing
            e.printStackTrace();
            return password; // Return password asli jika terjadi error
        }
    }

    // Getter untuk SharedPreferences Name
    public static String getSharedPrefName() {
        return SHARED_PREF_NAME;
    }
}

