package com.kelompok4.uksapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class NewPassword extends AppCompatActivity {

    private static final String TAG = "NewPassword";
    private String email;  // Menyimpan email
    private String otp;    // Menyimpan OTP yang diterima

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password);

        // Mengambil email dan OTP yang diteruskan dari VerifyOTP activity
        email = getIntent().getStringExtra("email");
        otp = getIntent().getStringExtra("otp");

        // Menyambungkan elemen UI
        EditText editTextNewPassword = findViewById(R.id.new_password);
        EditText editTextConfirmPassword = findViewById(R.id.confirm_password);
        Button button = findViewById(R.id.btnSubmit);
        ProgressBar progressBar = findViewById(R.id.progress);

        // Inisialisasi ImageView untuk Show/Hide Password
        ImageView ivShowPasswordNew = findViewById(R.id.iv_show_password_new);
        ImageView ivShowPasswordConfirm = findViewById(R.id.iv_show_password_confirm);

        // Tangani klik pada ikon mata untuk Show/Hide password (New Password)
        ivShowPasswordNew.setOnClickListener(v -> {
            if (editTextNewPassword.getTransformationMethod() instanceof android.text.method.PasswordTransformationMethod) {
                // Jika password sedang tersembunyi, ubah menjadi terlihat
                editTextNewPassword.setTransformationMethod(android.text.method.HideReturnsTransformationMethod.getInstance());
                ivShowPasswordNew.setImageResource(R.drawable.ic_visibility); // Ganti dengan ikon hide
            } else {
                // Jika password sedang terlihat, ubah menjadi tersembunyi
                editTextNewPassword.setTransformationMethod(android.text.method.PasswordTransformationMethod.getInstance());
                ivShowPasswordNew.setImageResource(R.drawable.ic_visibility_off); // Ganti dengan ikon show
            }
        });

        // Tangani klik pada ikon mata untuk Show/Hide password (Confirm Password)
        ivShowPasswordConfirm.setOnClickListener(v -> {
            if (editTextConfirmPassword.getTransformationMethod() instanceof android.text.method.PasswordTransformationMethod) {
                // Jika password sedang tersembunyi, ubah menjadi terlihat
                editTextConfirmPassword.setTransformationMethod(android.text.method.HideReturnsTransformationMethod.getInstance());
                ivShowPasswordConfirm.setImageResource(R.drawable.ic_visibility); // Ganti dengan ikon hide
            } else {
                // Jika password sedang terlihat, ubah menjadi tersembunyi
                editTextConfirmPassword.setTransformationMethod(android.text.method.PasswordTransformationMethod.getInstance());
                ivShowPasswordConfirm.setImageResource(R.drawable.ic_visibility_off); // Ganti dengan ikon show
            }
        });

        // Menambahkan listener untuk backButton
        ImageButton backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> {
            // Kembali ke activity VerifyOTP
            Intent intent = new Intent(NewPassword.this, VerifyOTP.class);
            startActivity(intent);
            finish(); // Menutup activity NewPassword
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newPassword = editTextNewPassword.getText().toString().trim();
                String confirmPassword = editTextConfirmPassword.getText().toString().trim();

                // Validasi password
                if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Password baru dan konfirmasi password tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!newPassword.equals(confirmPassword)) {
                    Toast.makeText(getApplicationContext(), "Password dan konfirmasi password tidak cocok!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                // Kirim password baru ke server
                String url = "http://192.168.0.116/my_api_android/new-password.php";  // Pastikan URL API sudah benar

                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progressBar.setVisibility(View.GONE);

                                // Log response untuk debugging
                                Log.d(TAG, "Response: " + response);

                                // Jika berhasil mengubah password
                                if (response.equals("Password berhasil diubah")) {
                                    Toast.makeText(getApplicationContext(), "Password berhasil diubah", Toast.LENGTH_SHORT).show();

                                    // Intent untuk kembali ke halaman login
                                    Intent intent = new Intent(NewPassword.this, Login.class);
                                    startActivity(intent);
                                    finishAffinity();  // Menutup seluruh stack activity

                                } else {
                                    // Menampilkan error jika respons tidak sesuai
                                    Toast.makeText(getApplicationContext(), "Gagal mengubah password: " + response, Toast.LENGTH_SHORT).show();
                                }

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), "Terjadi kesalahan! Coba lagi nanti.", Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("email", email);  // Kirim email
                        params.put("otp", otp);      // Kirim OTP
                        params.put("new-password", newPassword);  // Kirim password baru
                        return params;
                    }
                };

                // Menambahkan request ke queue
                queue.add(stringRequest);
            }
        });
    }
}
