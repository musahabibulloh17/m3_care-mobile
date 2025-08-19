package com.kelompok4.uksapp;


import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HealthEducationDetailFragment extends Fragment {
    private static final String ARG_HEALTH_EDUCATION_ITEM = "health_education_item";

    private HealthEducationItem healthEducationItem;

    private TextView titleTextView;
    private TextView dateTextView;
    private TextView contentTextView;
    private ImageView imageView;
    private WebView videoWebView;

    public static HealthEducationDetailFragment newInstance(HealthEducationItem item) {
        HealthEducationDetailFragment fragment = new HealthEducationDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_HEALTH_EDUCATION_ITEM, item);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            healthEducationItem = getArguments().getParcelable(ARG_HEALTH_EDUCATION_ITEM);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        // Initialize views
        titleTextView = view.findViewById(R.id.titleTextView);
        imageView = view.findViewById(R.id.imageView);
        videoWebView = view.findViewById(R.id.videoWebView);

        // Populate views with data



        // Handle image
        if (!TextUtils.isEmpty(healthEducationItem.getGambar())) {
            String baseUrl = "http://10.0.2.2/my_api_android/";
            String imageUrl = baseUrl + healthEducationItem.getGambar();

            imageView.setVisibility(View.VISIBLE);
            Glide.with(requireContext())
                    .load(imageUrl)
                    .placeholder(R.drawable.x)
                    .error(R.drawable.x)
                    .into(imageView);
        }

        // Handle video
        else if (!TextUtils.isEmpty(healthEducationItem.getVideo())) {
            videoWebView.setVisibility(View.VISIBLE);
            videoWebView.getSettings().setJavaScriptEnabled(true);
            videoWebView.getSettings().setMediaPlaybackRequiresUserGesture(false);

            String videoId = extractYouTubeVideoId(healthEducationItem.getVideo());
            if (!TextUtils.isEmpty(videoId)) {
                String embedUrl = "https://www.youtube.com/embed/" + videoId;

                String html = "<html><head>" +
                        "<meta name='viewport' content='width=100%, initial-scale=1.0'>" +
                        "<style>body{margin:0;padding:0;}</style>" +
                        "</head><body>" +
                        "<iframe width='100%' height='100%' src='" + embedUrl +
                        "' frameborder='0' allowfullscreen></iframe></body></html>";

                videoWebView.loadData(html, "text/html", "utf-8");
            } else {
                videoWebView.setVisibility(View.GONE);
            }
        } else {
            videoWebView.setVisibility(View.GONE);
        }
        view.findViewById(R.id.backButton).setOnClickListener(v -> requireActivity().onBackPressed());

        return view;
    }


    // Metode untuk mengekstrak ID video YouTube (sama seperti di HealthEducationAdapter)
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
                try {
                    String videoId = matcher.group(1);
                    if (!TextUtils.isEmpty(videoId)) {
                        return videoId;
                    }
                } catch (IndexOutOfBoundsException e) {
                    Log.e("VideoExtractor", "Could not extract video ID: " + videoUrl);
                }
            }
        }

        return videoUrl.length() == 11 ? videoUrl : "";
    }
}
