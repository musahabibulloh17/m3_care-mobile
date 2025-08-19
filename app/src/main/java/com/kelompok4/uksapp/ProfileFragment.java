package com.kelompok4.uksapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class ProfileFragment extends Fragment {

    private static final String SHARED_PREF_NAME = "user_pref";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";

    private TextView tvNamaLengkap, tvNIS, tvEmail, tvTelepon, tvKelas, tvTanggalLahir;
    private Button btnLogout;
    private ImageView ivEdit; // Kembali tambahkan ivEdit
    private String userId;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Inisialisasi komponen UI
        tvNamaLengkap = view.findViewById(R.id.tvNamaLengkap);
        tvNIS = view.findViewById(R.id.tvNIS);
        tvEmail = view.findViewById(R.id.tvEmail);
        tvTelepon = view.findViewById(R.id.tvTelepon);
        tvKelas = view.findViewById(R.id.tvKelas);
        tvTanggalLahir = view.findViewById(R.id.tvTanggalLahir);
        btnLogout = view.findViewById(R.id.btn_Logout);
        ivEdit = view.findViewById(R.id.ivEdit); // Tambahkan kembali inisialisasi untuk ivEdit

        // Ambil user_id dari SharedPreferences
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        userId = sharedPreferences.getString(KEY_USER_ID, null);

        if (userId != null) {
            // Load data profil pengguna
            loadUserProfile();
        } else {
            Toast.makeText(getContext(), "User ID tidak ditemukan", Toast.LENGTH_SHORT).show();
        }

        // Fungsi tombol logout
        btnLogout.setOnClickListener(v -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.putBoolean(KEY_IS_LOGGED_IN, false);
            editor.apply();

            Intent intent = new Intent(getContext(), Login.class);
            startActivity(intent);
            getActivity().finish();
        });

        // Fungsi tombol edit profile
        ivEdit.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), EditProfile.class);
            startActivity(intent);
        });

        return view;
    }

    private void loadUserProfile() {
        String url = Db_Contract.urlProfile + "?user_id=" + userId;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("status").equals("success")) {
                                JSONObject data = response.getJSONObject("data");

                                // Ambil data dari respons dan tampilkan
                                tvNamaLengkap.setText(data.getString("nama_lengkap"));
                                tvNIS.setText(data.getString("nis"));
                                tvEmail.setText(data.getString("email"));
                                tvTelepon.setText(data.getString("telepon"));
                                tvKelas.setText(data.getString("kelas"));
                                tvTanggalLahir.setText(data.getString("tanggal_lahir"));

                            } else {
                                Toast.makeText(getContext(), "Pengguna tidak ditemukan", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), "Kesalahan parsing data", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Gagal memuat profil: " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonObjectRequest);
    }
}
