package com.kelompok4.uksapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private List<Notification> notificationList; // Daftar notifikasi

    // Constructor
    public NotificationAdapter(List<Notification> notificationList) {
        this.notificationList = (notificationList != null) ? notificationList : new ArrayList<>();
    }

    // Inflate layout untuk setiap item di RecyclerView
    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    // Binding data ke ViewHolder
    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Notification notification = notificationList.get(position);

        if (notification != null) {
            // Set data ke TextView waktu dan deskripsi
            holder.textViewTime.setText(notification.getTime());
            holder.textViewDescription.setText(notification.getDescription());
        }
    }

    // Mendapatkan jumlah item dalam daftar
    @Override
    public int getItemCount() {
        return (notificationList != null) ? notificationList.size() : 0;
    }

    // Method untuk memperbarui data dalam adapter
    public void updateData(List<Notification> newNotificationList) {
        this.notificationList = (newNotificationList != null) ? newNotificationList : new ArrayList<>();
        notifyDataSetChanged(); // Perbarui tampilan RecyclerView
    }

    // ViewHolder untuk mengelola tampilan item
    public static class NotificationViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTime;       // Menampilkan waktu notifikasi
        TextView textViewDescription; // Menampilkan deskripsi notifikasi

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);

            // Inisialisasi elemen UI dari layout item_notification.xml
            textViewTime = itemView.findViewById(R.id.textViewTime);
            textViewDescription = itemView.findViewById(R.id.textViewDescription);
        }
    }
}
