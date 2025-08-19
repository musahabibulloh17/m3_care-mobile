package com.kelompok4.uksapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class VerifyOTP extends AppCompatActivity {

    private String email;  // Menyimpan email untuk digunakan nanti

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);

        // Mengambil email yang diteruskan dari activity sebelumnya
        email = getIntent().getStringExtra("email");

        // Menyesuaikan ID sesuai dengan yang baru di XML
        EditText otp1 = findViewById(R.id.otp1);
        EditText otp2 = findViewById(R.id.otp2);
        EditText otp3 = findViewById(R.id.otp3);
        EditText otp4 = findViewById(R.id.otp4);
        EditText otp5 = findViewById(R.id.otp5);
        EditText otp6 = findViewById(R.id.otp6);
        Button button = findViewById(R.id.btnSubmit); // Sesuaikan dengan ID yang tepat, jika ada perubahan
        ProgressBar progressBar = findViewById(R.id.progress); // Pastikan progress bar ada jika diperlukan
        ImageView ivBack = findViewById(R.id.ivBack); // Menginisialisasi ImageView untuk tombol kembali

        // Menambahkan TextWatcher ke setiap EditText untuk OTP
        otp1.addTextChangedListener(createOtpTextWatcher(otp2));
        otp2.addTextChangedListener(createOtpTextWatcher(otp3));
        otp3.addTextChangedListener(createOtpTextWatcher(otp4));
        otp4.addTextChangedListener(createOtpTextWatcher(otp5));
        otp5.addTextChangedListener(createOtpTextWatcher(otp6));

        // Listener untuk tombol kembali (ivBack)
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Kembali ke activity ResetPassword
                Intent intent = new Intent(getApplicationContext(), ResetPassword.class);
                startActivity(intent);
                finish(); // Menutup activity saat ini (VerifyOTP)
            }
        });

        // Submit button click listener
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Mengambil nilai OTP dari setiap EditText
                String otp = otp1.getText().toString().trim() + otp2.getText().toString().trim() + otp3.getText().toString().trim()
                        + otp4.getText().toString().trim() + otp5.getText().toString().trim() + otp6.getText().toString().trim();

                if (otp.isEmpty() || otp.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Kode OTP tidak boleh kosong dan harus 6 digit!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                // Kirim OTP untuk verifikasi ke server
                String url = "http://192.168.0.116/my_api_android/verify-otp.php";

                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());

                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                progressBar.setVisibility(View.GONE);

                                if (response.equals("OTP verified")) {
                                    // Jika OTP valid, lanjut ke halaman reset password
                                    Intent intent = new Intent(getApplicationContext(), NewPassword.class);
                                    intent.putExtra("email", email); // Pass email ke NewPassword activity
                                    intent.putExtra("otp", otp);     // Pass OTP ke NewPassword activity
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
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
                        params.put("email", email);
                        params.put("otp", otp);  // Mengirimkan OTP lengkap (gabungan dari semua EditText)
                        return params;
                    }
                };

                queue.add(stringRequest);
            }
        });
    }

    // Fungsi untuk membuat TextWatcher yang akan otomatis memindahkan fokus ke EditText berikutnya
    private TextWatcher createOtpTextWatcher(final EditText nextEditText) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() == 1) {
                    nextEditText.requestFocus(); // Pindah ke EditText berikutnya
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        };
    }
}
