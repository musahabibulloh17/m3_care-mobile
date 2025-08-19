package com.kelompok4.uksapp;

public class Notification {
    private String time; // Waktu notifikasi (contoh: 08:00:00)
    private String description; // Deskripsi notifikasi (contoh: Minum obat pagi)
    private String notificationDate; // Tanggal notifikasi (contoh: 2024-12-10)

    // Constructor
    public Notification(String time, String description, String notificationDate) {
        this.time = time;
        this.description = description;
        this.notificationDate = notificationDate;
    }

    // Getter untuk waktu
    public String getTime() {
        return time;
    }

    // Setter untuk waktu
    public void setTime(String time) {
        this.time = time;
    }

    // Getter untuk deskripsi
    public String getDescription() {
        return description;
    }

    // Setter untuk deskripsi
    public void setDescription(String description) {
        this.description = description;
    }

    // Getter untuk tanggal notifikasi
    public String getNotificationDate() {
        return notificationDate;
    }

    // Setter untuk tanggal notifikasi
    public void setNotificationDate(String notificationDate) {
        this.notificationDate = notificationDate;
    }

    // Method tambahan untuk mendapatkan waktu notifikasi
    public String getNotificationTime() {
        return time; // Mengambil waktu dari field time
    }

    // Method tambahan untuk mendapatkan tanggal notifikasi
    public String getDate() {
        return notificationDate; // Mengambil tanggal dari field notificationDate
    }

    @Override
    public String toString() {
        return "Notification{" +
                "time='" + time + '\'' +
                ", description='" + description + '\'' +
                ", notificationDate='" + notificationDate + '\'' +
                '}';
    }
}
