package com.kelompok4.uksapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.RetryPolicy;
import com.android.volley.DefaultRetryPolicy;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DashboardFragment extends Fragment implements HealthEducationFragment.OnItemSelectedListener{


    private static final String SHARED_PREF_NAME = "user_pref";
    private static final String KEY_USER_ID = "user_id";
    private static final long AUTO_SCROLL_DELAY = 5000; // 5 seconds
    private SharedPreferences sharedPreferences;
    private TextView userGreetingTextView;
    private RequestQueue requestQueue;
    private RecyclerView recyclerViewTrends;
    private HealthEducationAdapter healthEducationAdapter;
    private CardView stokObatCard, reminderCard, cetakSuratCard;

    private Handler autoScrollHandler;
    private List<HealthEducationItem> healthEducationItems = new ArrayList<>();
    private View viewAllTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        sharedPreferences = requireActivity().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        requestQueue = Volley.newRequestQueue(requireContext());

        userGreetingTextView = view.findViewById(R.id.userGreetingTextView);


        initializeViews(view);
        setClickListeners();

        recyclerViewTrends = view.findViewById(R.id.recyclerViewTrends);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewTrends.setLayoutManager(layoutManager);

        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerViewTrends);

        // Ambil data edukasi kesehatan dari API
        fetchHealthEducationItems();

        return view;
    }

    private void fetchHealthEducationItems() {
        String url = Db_Contract.urlGetedukasi; // Pastikan URL ini benar
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    try {
                        healthEducationItems.clear();

                        for (int i = 0; i < response.length(); i++) {
                            JSONObject itemJson = response.getJSONObject(i);
                            HealthEducationItem item = new HealthEducationItem(
                                    itemJson.getInt("id"),
                                    itemJson.getString("judul"),
                                    itemJson.getString("konten"),
                                    itemJson.getString("gambar"),
                                    itemJson.getString("video"), // Sesuaikan dengan struktur API
                                    itemJson.getString("tanggal_dibuat"), // Sesuaikan dengan struktur API
                                    itemJson.getInt("user_id")
                            );
                            healthEducationItems.add(item);
                        }

                        // Setup adapter setelah data diterima
                        if (!healthEducationItems.isEmpty()) {
                            healthEducationAdapter = new HealthEducationAdapter(
                                    healthEducationItems,
                                    item -> {
                                        HealthEducationDetailFragment detailFragment =
                                                HealthEducationDetailFragment.newInstance(item);
                                        requireActivity().getSupportFragmentManager()
                                                .beginTransaction()
                                                .replace(R.id.fragment_container, detailFragment)
                                                .addToBackStack(null)
                                                .commit();
                                    }
                            );
                            recyclerViewTrends.setAdapter(healthEducationAdapter);
                            setupAutoScroll(
                                    (LinearLayoutManager)recyclerViewTrends.getLayoutManager(),
                                    healthEducationItems.size()
                            );
                        } else {
                            Toast.makeText(getContext(), "Tidak ada data edukasi", Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        Log.e("DashboardFragment", "Error parsing health education data", e);
                        Toast.makeText(getContext(), "Gagal memuat data", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Log.e("DashboardFragment", "Error fetching health education", error);
                    Toast.makeText(getContext(), "Gagal memuat data", Toast.LENGTH_SHORT).show();
                }
        );

        // Set retry policy
        int socketTimeout = 10000;
        RetryPolicy policy = new DefaultRetryPolicy(
                socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        );
        jsonArrayRequest.setRetryPolicy(policy);

        requestQueue.add(jsonArrayRequest);
    }

    // Method lainnya tetap sama seperti sebelumnya...





    private String getTimeBasedGreeting() {
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        if (hour >= 5 && hour < 12) {
            return "Selamat Pagi";
        } else if (hour >= 12 && hour < 15) {
            return "Selamat Siang";
        } else if (hour >= 15 && hour < 18) {
            return "Selamat Sore";
        } else {
            return "Selamat Malam";
        }
    }

    private void initializeViews(View view) {
        stokObatCard = view.findViewById(R.id.Profil);
        reminderCard = view.findViewById(R.id.edukasi);
        cetakSuratCard = view.findViewById(R.id.Rekamkesehatan);
        viewAllTextView = view.findViewById(R.id.textView13);
    } private void setClickListeners() {
        stokObatCard.setOnClickListener(v -> navigateToFragment(new ProfileFragment()));
        reminderCard.setOnClickListener(v -> navigateToFragment(new ReminderFragment()));
        cetakSuratCard.setOnClickListener(v -> navigateToFragment(new ProfileFragment()));

        // Listener untuk viewAllTextView
        viewAllTextView.setOnClickListener(v -> {
            HealthEducationFragment trendsFragment = new HealthEducationFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("healthEducationItems", new ArrayList<>(healthEducationItems));
            trendsFragment.setArguments(bundle);

            navigateToFragment(trendsFragment);
        }); // Jangan lupa tanda titik koma di sini
    }



    private void navigateToFragment(Fragment fragment) {
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }
    private void setupAutoScroll(LinearLayoutManager layoutManager, int itemCount) {
        if (itemCount == 0) {
            return; // Jangan lakukan auto-scroll jika tidak ada item
        }
        autoScrollHandler = new Handler(Looper.getMainLooper());
        Runnable autoScrollRunnable = new Runnable() {
            int currentPosition = 0;

            @Override
            public void run() {
                currentPosition = (currentPosition + 1) % itemCount;
                recyclerViewTrends.smoothScrollToPosition(currentPosition);
                autoScrollHandler.postDelayed(this, AUTO_SCROLL_DELAY);
            }
        };
        autoScrollHandler.postDelayed(autoScrollRunnable, AUTO_SCROLL_DELAY);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (autoScrollHandler != null) {
            autoScrollHandler.removeCallbacksAndMessages(null);
        }
    }
}
