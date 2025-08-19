package com.kelompok4.uksapp;

public class Db_Contract {

    // IP untuk server lokal, bisa disesuaikan jika menggunakan server berbeda
    public static String ip = "192.168.0.116";
    private static final String BASE_URL = "http://" + ip + "/my_api_android/";

    // URL untuk operasi dasar
    public static final String urlRegister = BASE_URL + "api-register.php";
    public static final String urlLogin = BASE_URL + "api-login.php";
    public static final String urlProfile = BASE_URL + "profile.php";
    public static final String urlGetedukasi = BASE_URL + "get_edukasi_kesehatan.php";
    public static final String urlEditProfile = BASE_URL + "editProfile.php";
}
