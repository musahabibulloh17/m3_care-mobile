package com.kelompok4.uksapp;

import static android.content.Context.ALARM_SERVICE;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;

public class ReminderFragment extends Fragment {

    private static final String API_URL = "http://192.168.0.116/my_api_android/get_notification.php";
    private RecyclerView recyclerView;
    private NotificationAdapter notificationAdapter;
    private ArrayList<Notification> notificationList = new ArrayList<>();
    private ArrayList<Notification> originalNotificationList = new ArrayList<>();
    private TextView btnMonday, btnTuesday, btnWednesday, btnThursday, btnFriday, btnSaturday, btnSunday;
    private String selectedDate;
    private TextView textViewDate;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_reminder, container, false);

        textViewDate = rootView.findViewById(R.id.textViewDate);
        recyclerView = rootView.findViewById(R.id.recyclerView);

        Calendar calendar = Calendar.getInstance();
        String currentDate = String.format("%1$te %1$tB", calendar);
        textViewDate.setText(currentDate);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        notificationAdapter = new NotificationAdapter(notificationList);
        recyclerView.setAdapter(notificationAdapter);

        btnMonday = rootView.findViewById(R.id.textMonday);
        btnTuesday = rootView.findViewById(R.id.textTuesday);
        btnWednesday = rootView.findViewById(R.id.textWednesday);
        btnThursday = rootView.findViewById(R.id.textThursday);
        btnFriday = rootView.findViewById(R.id.textFriday);
        btnSaturday = rootView.findViewById(R.id.textSaturday);
        btnSunday = rootView.findViewById(R.id.textSunday);

        btnMonday.setOnClickListener(v -> selectDay(Calendar.MONDAY));
        btnTuesday.setOnClickListener(v -> selectDay(Calendar.TUESDAY));
        btnWednesday.setOnClickListener(v -> selectDay(Calendar.WEDNESDAY));
        btnThursday.setOnClickListener(v -> selectDay(Calendar.THURSDAY));
        btnFriday.setOnClickListener(v -> selectDay(Calendar.FRIDAY));
        btnSaturday.setOnClickListener(v -> selectDay(Calendar.SATURDAY));
        btnSunday.setOnClickListener(v -> selectDay(Calendar.SUNDAY));

        highlightCurrentDay(calendar.get(Calendar.DAY_OF_WEEK));

        new GetNotificationTask().execute(API_URL);

        return rootView;
    }

    private void selectDay(int dayOfWeek) {
        Calendar calendar = Calendar.getInstance();

        int currentDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        currentDayOfWeek = (currentDayOfWeek == Calendar.SUNDAY) ? 7 : currentDayOfWeek - 1;
        dayOfWeek = (dayOfWeek == Calendar.SUNDAY) ? 7 : dayOfWeek - 1;

        int difference = dayOfWeek - currentDayOfWeek;

        calendar.add(Calendar.DAY_OF_MONTH, difference);

        selectedDate = String.format("%1$tY-%1$tm-%1$td", calendar);

        String selectedDateDisplay = String.format("%1$te %1$tB", calendar);
        textViewDate.setText(selectedDateDisplay);

        highlightCurrentDay(dayOfWeek == 7 ? Calendar.SUNDAY : dayOfWeek + 1);

        resetToOriginalList();

        filterNotificationsByDate(selectedDate);
    }

    private void highlightCurrentDay(int dayOfWeek) {
        resetButtonColors();

        switch (dayOfWeek) {
            case Calendar.MONDAY:
                btnMonday.setBackgroundResource(R.drawable.bg_circle_selected);
                btnMonday.setTextColor(getResources().getColor(android.R.color.white));
                break;
            case Calendar.TUESDAY:
                btnTuesday.setBackgroundResource(R.drawable.bg_circle_selected);
                btnTuesday.setTextColor(getResources().getColor(android.R.color.white));
                break;
            case Calendar.WEDNESDAY:
                btnWednesday.setBackgroundResource(R.drawable.bg_circle_selected);
                btnWednesday.setTextColor(getResources().getColor(android.R.color.white));
                break;
            case Calendar.THURSDAY:
                btnThursday.setBackgroundResource(R.drawable.bg_circle_selected);
                btnThursday.setTextColor(getResources().getColor(android.R.color.white));
                break;
            case Calendar.FRIDAY:
                btnFriday.setBackgroundResource(R.drawable.bg_circle_selected);
                btnFriday.setTextColor(getResources().getColor(android.R.color.white));
                break;
            case Calendar.SATURDAY:
                btnSaturday.setBackgroundResource(R.drawable.bg_circle_selected);
                btnSaturday.setTextColor(getResources().getColor(android.R.color.white));
                break;
            case Calendar.SUNDAY:
                btnSunday.setBackgroundResource(R.drawable.bg_circle_selected);
                btnSunday.setTextColor(getResources().getColor(android.R.color.white));
                break;
        }
    }

    private void resetButtonColors() {
        btnMonday.setBackgroundResource(R.drawable.bg_circle_unselected);
        btnMonday.setTextColor(getResources().getColor(android.R.color.darker_gray));
        btnTuesday.setBackgroundResource(R.drawable.bg_circle_unselected);
        btnTuesday.setTextColor(getResources().getColor(android.R.color.darker_gray));
        btnWednesday.setBackgroundResource(R.drawable.bg_circle_unselected);
        btnWednesday.setTextColor(getResources().getColor(android.R.color.darker_gray));
        btnThursday.setBackgroundResource(R.drawable.bg_circle_unselected);
        btnThursday.setTextColor(getResources().getColor(android.R.color.darker_gray));
        btnFriday.setBackgroundResource(R.drawable.bg_circle_unselected);
        btnFriday.setTextColor(getResources().getColor(android.R.color.darker_gray));
        btnSaturday.setBackgroundResource(R.drawable.bg_circle_unselected);
        btnSaturday.setTextColor(getResources().getColor(android.R.color.darker_gray));
        btnSunday.setBackgroundResource(R.drawable.bg_circle_unselected);
        btnSunday.setTextColor(getResources().getColor(android.R.color.darker_gray));
    }

    private void resetToOriginalList() {
        notificationList.clear();
        notificationList.addAll(originalNotificationList);
        notificationAdapter.notifyDataSetChanged();
    }

    private void filterNotificationsByDate(String date) {
        ArrayList<Notification> filteredNotifications = new ArrayList<>();
        for (Notification notif : originalNotificationList) {
            String[] dates = notif.getNotificationDate().split(",");
            for (String availableDate : dates) {
                if (availableDate.trim().equals(date)) {
                    filteredNotifications.add(notif);
                    break;
                }
            }
        }
        notificationList.clear();
        notificationList.addAll(filteredNotifications);
        notificationAdapter.notifyDataSetChanged();

        if (filteredNotifications.isEmpty()) {
            Toast.makeText(getActivity(), "Tidak ada notifikasi untuk tanggal ini", Toast.LENGTH_SHORT).show();
        }
    }

    private class GetNotificationTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String response = null;
            try {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("user_pref", Context.MODE_PRIVATE);
                String userId = sharedPreferences.getString("user_id", "unknown");

                if ("unknown".equals(userId)) {
                    return null;
                }

                String urlStr = params[0] + "?user_id=" + userId;
                URL url = new URL(urlStr);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setConnectTimeout(15000);
                urlConnection.setReadTimeout(15000);

                InputStreamReader reader = new InputStreamReader(urlConnection.getInputStream());
                StringBuilder responseBuilder = new StringBuilder();
                int input;
                while ((input = reader.read()) != -1) {
                    responseBuilder.append((char) input);
                }
                response = responseBuilder.toString();
            } catch (Exception e) {
                Log.e("GetNotificationTask", "Error in doInBackground", e);
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                if (result == null || result.isEmpty()) {
                    Log.e("GetNotificationTask", "Received empty or null response");
                    return;
                }

                JSONObject jsonObject = new JSONObject(result);
                if (jsonObject.has("notifications")) {
                    JSONArray notifications = jsonObject.getJSONArray("notifications");

                    originalNotificationList.clear(); // Bersihkan daftar asli
                    ArrayList<Notification> todayNotifications = new ArrayList<>(); // Untuk menyimpan notifikasi hari ini

                    Calendar todayCalendar = Calendar.getInstance();
                    String todayDate = String.format("%1$tY-%1$tm-%1$td", todayCalendar); // Format: "YYYY-MM-DD"

                    AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);

                    for (int i = 0; i < notifications.length(); i++) {
                        JSONObject notification = notifications.getJSONObject(i);
                        String timeIntervals = notification.getString("time_intervals"); // Waktu notifikasi
                        String descriptions = notification.getString("description"); // Deskripsi notifikasi
                        String notificationDates = notification.getString("notification_date"); // Banyak tanggal

                        String[] dateArray = notificationDates.split(","); // Format: "YYYY-MM-DD,YYYY-MM-DD"
                        String[] times = timeIntervals.split(","); // Format: "HH:mm:ss,HH:mm:ss"
                        String[] descArray = descriptions.split(","); // Format: "Deskripsi pagi,siang,malam"

                        for (String date : dateArray) {
                            date = date.trim();

                            for (int j = 0; j < times.length; j++) {
                                String notificationTime = times[j].trim();

                                // Remove the seconds part, if present, so it's in "HH:mm" format
                                if (notificationTime.length() == 8) {
                                    notificationTime = notificationTime.substring(0, 5); // "HH:mm" format
                                }

                                String description = (j < descArray.length) ? descArray[j].trim() : "Deskripsi tidak tersedia";

                                if (notificationTime.matches("\\d{2}:\\d{2}")) {
                                    Notification notif = new Notification(notificationTime, description, date);
                                    originalNotificationList.add(notif);

                                    // Jika tanggal cocok dengan hari ini, tambahkan ke daftar notifikasi hari ini
                                    if (date.equals(todayDate)) {
                                        todayNotifications.add(notif);
                                    }

                                    // Atur AlarmManager untuk notifikasi
                                    String[] timeParts = notificationTime.split(":");
                                    int hour = Integer.parseInt(timeParts[0]);
                                    int minute = Integer.parseInt(timeParts[1]);

                                    Calendar alarmCalendar = Calendar.getInstance();
                                    alarmCalendar.set(Calendar.YEAR, Integer.parseInt(date.split("-")[0]));
                                    alarmCalendar.set(Calendar.MONTH, Integer.parseInt(date.split("-")[1]) - 1);
                                    alarmCalendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(date.split("-")[2]));
                                    alarmCalendar.set(Calendar.HOUR_OF_DAY, hour);
                                    alarmCalendar.set(Calendar.MINUTE, minute);
                                    alarmCalendar.set(Calendar.SECOND, 0);

                                    // Jika waktu alarm sudah lewat, abaikan untuk hari ini
                                    if (alarmCalendar.before(Calendar.getInstance())) {
                                        Log.d("AlarmManager", "Skipped past notification: " + alarmCalendar.getTime());
                                        continue;
                                    }

                                    Intent intent = new Intent(getActivity(), NotificationReceiver.class);
                                    intent.putExtra("description", description); // Kirimkan deskripsi sebagai data notifikasi
                                    PendingIntent pendingIntent = PendingIntent.getBroadcast(
                                            getActivity(),
                                            (int) alarmCalendar.getTimeInMillis(), // ID unik untuk tiap notifikasi
                                            intent,
                                            PendingIntent.FLAG_UPDATE_CURRENT
                                    );

                                    // Set alarm untuk waktu yang ditentukan
                                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmCalendar.getTimeInMillis(), pendingIntent);
                                    Log.d("AlarmManager", "Set alarm for: " + alarmCalendar.getTime());
                                }
                            }
                        }
                    }

                    // Perbarui RecyclerView untuk menampilkan notifikasi hari ini
                    notificationList.clear();
                    notificationList.addAll(todayNotifications);
                    notificationAdapter.notifyDataSetChanged();

                    if (todayNotifications.isEmpty()) {
                        Toast.makeText(getActivity(), "Tidak ada notifikasi aktif untuk hari ini", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Tidak ada notifikasi aktif", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Log.e("GetNotificationTask", "Error in onPostExecute", e);
            }
        }
    }
}