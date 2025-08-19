package com.kelompok4.uksapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class EdukasiKesehatanFragment extends Fragment {

    private RecyclerView posterRecyclerView;
    private PosterAdapter posterAdapter;
    private List<PosterItem> posterItems;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edukasi_kesehatan, container, false);

        posterRecyclerView = view.findViewById(R.id.posterRecyclerView);

        // Mengatur layout manager horizontal untuk geser kiri-kanan
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        posterRecyclerView.setLayoutManager(layoutManager);

        // Inisialisasi data poster
        initializePosterData();

        // Setup adapter
        posterAdapter = new PosterAdapter(posterItems);
        posterRecyclerView.setAdapter(posterAdapter);

        return view;
    }

    private void initializePosterData() {
        posterItems = new ArrayList<>();
        posterItems.add(new PosterItem("Cuci Tangan", "Cara mencegah penyebaran kuman", R.drawable.imageduapuluj));
        posterItems.add(new PosterItem("Gizi Seimbang", "Tips makanan sehat untuk tubuh", R.drawable.imageduapuluj));
        posterItems.add(new PosterItem("Olahraga Teratur", "Manfaat aktivitas fisik", R.drawable.imageduapuluj));
        posterItems.add(new PosterItem("Tidur Cukup", "Pentingnya istirahat yang berkualitas", R.drawable.imageduapuluj));
    }
}