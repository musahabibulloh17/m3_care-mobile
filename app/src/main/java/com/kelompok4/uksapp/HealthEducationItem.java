package com.kelompok4.uksapp;

import android.os.Parcel;
import android.os.Parcelable;


public class HealthEducationItem implements Parcelable {
    private int id;
    private String judul;
    private String konten;
    private String gambar;
    private String video;
    private String tanggalDibuat;
    private int userId;

    public HealthEducationItem(int id, String judul, String konten, String gambar, String video, String tanggalDibuat, int userId) {
        this.id = id;
        this.judul = judul;        this.konten = konten;
        this.gambar = gambar;
        this.video = video;
        this.tanggalDibuat = tanggalDibuat;
        this.userId = userId;
    }
    protected HealthEducationItem(Parcel in) {
        id = in.readInt();
        judul = in.readString();
        konten = in.readString();
        gambar = in.readString();
        video = in.readString();
        tanggalDibuat = in.readString();
        userId = in.readInt();
    }
    public static final Creator<HealthEducationItem> CREATOR = new Creator<HealthEducationItem>() {
        @Override
        public HealthEducationItem createFromParcel(Parcel in) {
            return new HealthEducationItem(in);
        }

        @Override
        public HealthEducationItem[] newArray(int size) {
            return new HealthEducationItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(judul);
        dest.writeString(konten);
        dest.writeString(gambar);
        dest.writeString(video);
        dest.writeString(tanggalDibuat);
        dest.writeInt(userId);
    }

    // Getter methods
    public int getId() { return id; }
    public String getJudul() { return judul; }
    public String getKonten() { return konten; }
    public String getGambar() { return gambar; }
    public String getVideo() { return video; }
    public String getTanggalDibuat() { return tanggalDibuat; }
    public int getUserId() { return userId; }
}