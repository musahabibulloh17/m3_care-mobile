package com.kelompok4.uksapp;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kelompok4.uksapp.HealthEducationItem;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HealthEducationAdapter extends RecyclerView.Adapter<HealthEducationAdapter.ViewHolder> {
    private List<HealthEducationItem> items;
    private OnItemClickListener listener;

    public HealthEducationAdapter(List<HealthEducationItem> items, OnItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trend, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HealthEducationItem item = items.get(position);

        // Set title and description
        holder.titleTextView.setText(item.getJudul());
        holder.descriptionTextView.setText(item.getKonten());

        // Hide both image and video views initially
        holder.imageView.setVisibility(View.GONE);
        holder.webViewVideo.setVisibility(View.GONE);

        // Prioritize video over image
        if (!TextUtils.isEmpty(item.getVideo())) {
            String videoUrl = item.getVideo();
            try {
                String videoId = extractYouTubeVideoId(videoUrl);

                if (!TextUtils.isEmpty(videoId)) {
                    holder.webViewVideo.setVisibility(View.VISIBLE);
                    holder.webViewVideo.getSettings().setJavaScriptEnabled(true);
                    holder.webViewVideo.getSettings().setMediaPlaybackRequiresUserGesture(false);

                    String embedUrl = "https://www.youtube.com/embed/" + videoId;

                    String html = "<html><head>" +
                            "<meta name='viewport' content='width=100%, initial-scale=1.0'>" +
                            "<style>body{margin:0;padding:0;}</style>" +
                            "</head><body>" +
                            "<iframe width='100%' height='100%' src='" + embedUrl +
                            "' frameborder='0' allowfullscreen></iframe></body></html>";

                    holder.webViewVideo.loadData(html, "text/html", "utf-8");
                }
            } catch (Exception e) {
                Log.e("HealthEducationAdapter", "Error processing video URL", e);
            }
        }
        // If no video, try to show image
        else if (!TextUtils.isEmpty(item.getGambar())) {
            String baseUrl = "http://192.168.43.116/login2/";
            String imagePath = item.getGambar();
            String imageUrl = baseUrl + item.getGambar();
            holder.imageView.setVisibility(View.VISIBLE);
            Glide.with(holder.itemView.getContext())
                    .load(imageUrl)
                    .placeholder(R.drawable.x)
                    .error(R.drawable.x)
                    .into(holder.imageView);
        }
        // Item click listener
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(item);
            }
        });
    }

    // Metode extractYouTubeVideoId tetap sama seperti sebelumnya
    private String extractYouTubeVideoId(String videoUrl) {
        if (TextUtils.isEmpty(videoUrl)) {
            return "";
        }

        // Trim and standardize the URL
        videoUrl = videoUrl.trim();

        // List of regex patterns to match different YouTube URL formats
        String[] patterns = {
                "(?:https?://)?(?:www\\.)?(?:youtube\\.com/(?:[^/\\n\\s]+/\\S+/|(?:v|e(?:mbed)?)/|\\S*?[?&]v=)|youtu\\.be/)([a-zA-Z0-9_-]{11})",
                "(?:https?://)?(?:www\\.)?youtube\\.com/embed/([a-zA-Z0-9_-]{11})",
                "(?:https?://)?(?:www\\.)?youtu\\.be/([a-zA-Z0-9_-]{11})"
        };

        for (String patternStr : patterns) {
            Pattern pattern = Pattern.compile(patternStr);
            Matcher matcher = pattern.matcher(videoUrl);

            if (matcher.find()) {
                // Prefer group(1) if it exists, otherwise use group(0)
                try {
                    String videoId = matcher.group(1);
                    if (!TextUtils.isEmpty(videoId)) {
                        return videoId;
                    }
                } catch (IndexOutOfBoundsException e) {
                    // Fallback
                    Log.e("VideoExtractor", "Could not extract video ID: " + videoUrl);
                }
            }
        }

        // If no match is found, return the original URL or an empty string
        return videoUrl.length() == 11 ? videoUrl : "";

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public interface OnItemClickListener {
        void onItemClick(HealthEducationItem item);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView descriptionTextView;
        ImageView imageView;
        WebView webViewVideo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.trendTitle);
            descriptionTextView = itemView.findViewById(R.id.trendDescription);
            imageView = itemView.findViewById(R.id.imageView);
            webViewVideo = itemView.findViewById(R.id.webViewVideo);
        }
    }
}