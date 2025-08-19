package com.kelompok4.uksapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditProfile extends AppCompatActivity {

    private static final String SHARED_PREF_NAME = "user_pref";
    private static final String KEY_USER_ID = "user_id";

    private EditText etNamaLengkap, etNIS, etEmail, etTelepon, etKelas, etTanggalLahir;
    private Button btnSave;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);


        etNamaLengkap = findViewById(R.id.etNamaLengkap);
        etNIS = findViewById(R.id.etNIS);
        etEmail = findViewById(R.id.etEmail);
        etTelepon = findViewById(R.id.etTelepon);
        etKelas = findViewById(R.id.etKelas);
        etTanggalLahir = findViewById(R.id.etTanggalLahir);
        btnSave = findViewById(R.id.btnSave);


        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        userId = sharedPreferences.getString(KEY_USER_ID, null);

        if (userId == null) {
            Toast.makeText(this, "User ID tidak ditemukan", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Load existing user profile data
        loadUserProfile();

        // Save profile button click listener
        btnSave.setOnClickListener(v -> saveUserProfile());
    }

    private void loadUserProfile() {
        // URL to fetch user profile data
        String url = Db_Contract.urlProfile + "?user_id=" + userId;

        // Request to fetch user profile data
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle server response
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            if (jsonResponse.getString("status").equals("success")) {
                                JSONObject data = jsonResponse.getJSONObject("data");

                                // Populate EditText fields with the response data
                                etNamaLengkap.setText(data.getString("nama_lengkap"));
                                etNIS.setText(data.getString("nis"));
                                etEmail.setText(data.getString("email"));
                                etTelepon.setText(data.getString("telepon"));
                                etKelas.setText(data.getString("kelas"));
                                etTanggalLahir.setText(data.getString("tanggal_lahir"));
                            } else {
                                Toast.makeText(EditProfile.this, "User not found", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(EditProfile.this, "Failed to load data", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error response
                        Toast.makeText(EditProfile.this, "Failed to connect to server", Toast.LENGTH_SHORT).show();
                    }
                });

        // Add the request to the queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void saveUserProfile() {
        // Retrieve input data from EditText fields
        String namaLengkap = etNamaLengkap.getText().toString().trim();
        String nis = etNIS.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String telepon = etTelepon.getText().toString().trim();
        String kelas = etKelas.getText().toString().trim();
        String tanggalLahir = etTanggalLahir.getText().toString().trim();

        // Validate input data
        if (TextUtils.isEmpty(namaLengkap) || TextUtils.isEmpty(nis) || TextUtils.isEmpty(email) ||
                TextUtils.isEmpty(telepon) || TextUtils.isEmpty(kelas) || TextUtils.isEmpty(tanggalLahir)) {
            Toast.makeText(this, "All fields must be filled", Toast.LENGTH_SHORT).show();
            return;
        }

        // URL to edit user profile
        String url = Db_Contract.urlEditProfile; // URL for profile edit API

        // Send data to the server using Volley (POST request)
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle server response
                        if (response.contains("success")) {
                            Toast.makeText(EditProfile.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                            finish(); // Close the EditProfile Activity after successful update
                        } else {
                            Toast.makeText(EditProfile.this, "Error occurred while updating", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error response
                        Toast.makeText(EditProfile.this, "Failed to connect to server", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                // Send user profile data as POST parameters
                Map<String, String> params = new HashMap<>();
                params.put("user_id", userId);
                params.put("nama_lengkap", namaLengkap);
                params.put("nis", nis);
                params.put("email", email);
                params.put("telepon", telepon);
                params.put("kelas", kelas);
                params.put("tanggal_lahir", tanggalLahir);
                return params;
            }
        };

        // Add the request to the request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
