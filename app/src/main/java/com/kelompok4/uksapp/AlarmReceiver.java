package com.kelompok4.uksapp;

import static android.content.Context.ALARM_SERVICE;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "UKS_NOTIFICATION_CHANNEL";
    private static final int NOTIFICATION_ID = 1;

    @Override
    public void onReceive(Context context, Intent intent) {
        // Dapatkan waktu dan deskripsi notifikasi dari intent
        String description = intent.getStringExtra("description");

        // Menampilkan Toast sebagai pemberitahuan sementara
        Toast.makeText(context, "Reminder: " + description, Toast.LENGTH_SHORT).show();

        // Buat notifikasi dan tampilkan
        sendNotification(context, description);
    }

    // Mengirim notifikasi
    private void sendNotification(Context context, String description) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Cek jika versi Android >= Oreo untuk membuat channel notifikasi
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "UKS Notification Channel";
            String descriptionChannel = "Channel for UKS app notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(descriptionChannel);
            notificationManager.createNotificationChannel(channel);
        }

        // Membuat notifikasi
        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle("Reminder")
                .setContentText(description)
                .setSmallIcon(R.drawable.ic_notification)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build();

        // Menampilkan notifikasi
        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    // Helper untuk mengatur alarm
    public static void setAlarm(Context context, long triggerTime, String description) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("description", description);  // Menyertakan deskripsi dalam Intent

        // Membuat PendingIntent untuk AlarmReceiver
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Mengatur AlarmManager
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
        }
    }
}
