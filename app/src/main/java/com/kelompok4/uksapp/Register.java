package com.kelompok4.uksapp;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.mindrot.jbcrypt.BCrypt;
import java.util.Calendar;
import java.util.HashMap;

public class Register extends AppCompatActivity {

    private EditText etFullName, etUsername, etPassword, etEmail, etNIS, etPhone, etBirthDate, etClass;
    private Button btnRegister;
    private TextView tvLogin;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Inisialisasi elemen-elemen input
        etFullName = findViewById(R.id.et_full_name);
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        etEmail = findViewById(R.id.et_email);
        etNIS = findViewById(R.id.et_nis);
        etPhone = findViewById(R.id.et_phone);
        etBirthDate = findViewById(R.id.et_birth_date);
        etClass = findViewById(R.id.et_class);
        btnRegister = findViewById(R.id.btn_register);
        tvLogin = findViewById(R.id.tv_login);

        // Setup DatePicker untuk tanggal lahir
        etBirthDate.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    Register.this,
                    (view, yearSelected, monthOfYear, dayOfMonth) -> {
                        String date = yearSelected + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                        etBirthDate.setText(date);
                    },
                    year,
                    month,
                    day
            );
            datePickerDialog.show();
        });

        // Tombol kembali (gunakan ImageView)
        ImageView backButton = findViewById(R.id.ic_arrow_back);
        backButton.setOnClickListener(view -> {
            Intent intent = new Intent(Register.this, Login.class);
            startActivity(intent);
            finish(); // Menutup activity Register agar tidak kembali ke Register
        });

        btnRegister.setOnClickListener(view -> {

            // Ambil data dari input
            String fullName = etFullName.getText().toString().trim();
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String nis = etNIS.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String birthDate = etBirthDate.getText().toString().trim();
            String userClass = etClass.getText().toString().trim();

            // Validasi input
            if (fullName.isEmpty() || username.isEmpty() || password.isEmpty() || email.isEmpty() || nis.isEmpty() || phone.isEmpty() || birthDate.isEmpty() || userClass.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Ada Data Yang Masih Kosong", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isValidEmail(email)) {
                Toast.makeText(getApplicationContext(), "Email tidak valid", Toast.LENGTH_SHORT).show();
                return;
            }

            // Hash password menggunakan bcrypt
            String hashedPassword = hashPassword(password);

            // Kirim data ke server menggunakan Volley
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Db_Contract.urlRegister,
                    response -> {
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), Login.class));
                        finish();
                    },
                    error -> Toast.makeText(getApplicationContext(), "Error: " + error.toString(), Toast.LENGTH_SHORT).show()
            ) {
                @Override
                protected HashMap<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("nama_lengkap", fullName);
                    params.put("username", username);
                    params.put("password", hashedPassword); // Gunakan password yang di-hash
                    params.put("email", email);
                    params.put("nis", nis);
                    params.put("telepon", phone);
                    params.put("tanggal_lahir", birthDate);
                    params.put("kelas", userClass);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        });

        // Tindakan untuk login
        tvLogin.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), Login.class));
            finish();
        });

        // Inisialisasi ImageView untuk Show/Hide Password
        ImageView ivShowPassword = findViewById(R.id.iv_show_password);

        // Tangani klik pada ikon mata untuk Show/Hide password
        ivShowPassword.setOnClickListener(v -> {
            if (etPassword.getTransformationMethod() instanceof PasswordTransformationMethod) {
                // Jika password sedang tersembunyi, ubah menjadi terlihat
                etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                ivShowPassword.setImageResource(R.drawable.ic_visibility); // Ganti dengan ikon hide
            } else {
                // Jika password sedang terlihat, ubah menjadi tersembunyi
                etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                ivShowPassword.setImageResource(R.drawable.ic_visibility_off); // Ganti dengan ikon show
            }
        });
    }

    // Method untuk hashing password menggunakan bcrypt
    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    // Method untuk validasi format email
    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
