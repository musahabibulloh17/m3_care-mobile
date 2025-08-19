package com.kelompok4.uksapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "NotificationChannel";
    private static final int NOTIFICATION_ID = 1; // ID unik untuk setiap notifikasi

    @Override
    public void onReceive(Context context, Intent intent) {
        String description = intent.getStringExtra("description");

        Uri soundUri = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.ininotifnya);
        Log.d("NotificationReceiver", "Sound URI: " + soundUri);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            NotificationChannel notificationChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Notifikasi Pengingat",
                    NotificationManager.IMPORTANCE_HIGH
            );
            notificationChannel.setDescription("Channel untuk notifikasi pengingat");
            notificationChannel.setSound(soundUri, audioAttributes);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        Notification notification = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle("Pengingat Minum Obat")
                .setContentText(description != null ? description : "Tidak ada deskripsi")
                .setSmallIcon(R.drawable.logoijo2)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .build();

        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            Log.e("NotificationReceiver", "POST_NOTIFICATIONS permission not granted");
            return;
        }

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(context);
        notificationManagerCompat.notify((int) System.currentTimeMillis(), notification);
    }
}
