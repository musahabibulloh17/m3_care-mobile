package com.kelompok4.uksapp;

public class EducationItem {
    private String judul;
    private String gambar;  // Atribut untuk URL gambar
    private String video;

    // Konstruktor
    public EducationItem(String judul, String gambar, String video) {
        this.judul = judul;
        this.gambar = gambar;  // Menyimpan URL gambar
        this.video = video;
    }

    // Getter untuk judul
    public String getJudul() {
        return judul;
    }

    // Getter untuk gambar (URL gambar)
    public String getGambar() {
        return gambar;
    }

    // Getter untuk video
    public String getVideo() {
        return video;
    }

    // Getter untuk image URL (ganti imageUrl dengan gambar)
    public String getImageUrl() {
        return gambar; // Mengembalikan URL gambar yang benar
    }
}
