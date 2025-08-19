package com.kelompok4.uksapp;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HealthEducationFragment extends Fragment {
    private RecyclerView recyclerViewEducation;
    private HealthEducationAdapter healthEducationAdapter;
    private RequestQueue requestQueue;

    private static List<HealthEducationItem> healthEducationItems = new ArrayList<>();
    public static List<HealthEducationItem> getHealthEducationItems() {
        return healthEducationItems;
    }

    // Metode untuk memperbarui items dari luar (opsional)
    public static void setHealthEducationItems(List<HealthEducationItem> items) {
        healthEducationItems = items;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_education, container, false);

        // Inisialisasi RequestQueue
        requestQueue = Volley.newRequestQueue(requireContext());

        // Initialize RecyclerView
        recyclerViewEducation = view.findViewById(R.id.recyclerViewHealthEducation);

        // Set Layout Manager untuk vertical scroll
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                getContext(),
                LinearLayoutManager.VERTICAL,
                false
        );
        recyclerViewEducation.setLayoutManager(layoutManager);
        // Ambil data dari API
        fetchHealthEducationItems();

        return view;
    }
    private void fetchHealthEducationItems() {
        String url = Db_Contract.urlGetedukasi;
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        healthEducationItems.clear();
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject jsonObject = response.getJSONObject(i);

                            HealthEducationItem item = new HealthEducationItem(
                                    jsonObject.getInt("id"),
                                    jsonObject.getString("judul"),
                                    jsonObject.getString("konten"),
                                    jsonObject.getString("gambar"),
                                    jsonObject.getString("video"),
                                    jsonObject.getString("tanggal_dibuat"),
                                    jsonObject.getInt("user_id")
                            );

                            healthEducationItems.add(item);

                            // Log untuk debugging
                            Log.d("HealthEducationFragment", "Item added: " + item.getJudul());
                        }

                        // Cetak jumlah item untuk memastikan
                        Log.d("HealthEducationFragment", "Total items: " + healthEducationItems.size());

                        if (getActivity() != null) {
                            getActivity().runOnUiThread(() -> {
                                if (healthEducationItems.isEmpty()) {
                                    Toast.makeText(getContext(), "Tidak ada data edukasi", Toast.LENGTH_SHORT).show();
                                }

                                healthEducationAdapter = new HealthEducationAdapter(
                                        healthEducationItems,
                                        item -> {
                                            // Your existing click listener
                                        }
                                );
                                recyclerViewEducation.setAdapter(healthEducationAdapter);
                            });
                        }

                    } catch (JSONException e) {
                        Log.e("HealthEducationFragment", "JSON Parsing Error", e);
                    }
                },
                error -> {
                    // Error handling
                    Log.e("HealthEducationFragment", "Network Error", error);
                    // More detailed error logging
                    if (error.networkResponse != null) {
                        String errorMessage = new String(error.networkResponse.data);
                        Log.e("HealthEducationFragment", "Error Response: " + errorMessage);
                        Toast.makeText(getContext(), "Gagal memuat data: " + errorMessage, Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getContext(), "Gagal memuat data edukasi", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        requestQueue.add(jsonArrayRequest);
    }

    public interface OnItemSelectedListener {
    }
}