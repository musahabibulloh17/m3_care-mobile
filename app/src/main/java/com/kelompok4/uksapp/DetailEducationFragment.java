        package com.kelompok4.uksapp;

        import android.os.Bundle;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageView;
        import android.widget.TextView;

        import androidx.annotation.NonNull;
        import androidx.annotation.Nullable;
        import androidx.fragment.app.Fragment;

        public class DetailEducationFragment extends Fragment {
            @Nullable
            @Override
            public View onCreateView(@NonNull LayoutInflater inflater,
                                     @Nullable ViewGroup container,
                                     @Nullable Bundle savedInstanceState) {
                View view = inflater.inflate(R.layout.fragment_detail_education, container, false);

                // Ambil data dari arguments
                Bundle args = getArguments();
                if (args != null) {
                    int itemId = args.getInt("ITEM_ID");
                    String title = args.getString("ITEM_TITLE");
                    String description = args.getString("ITEM_DESCRIPTION");
                    int imageResourceId = args.getInt("ITEM_IMAGE");

                    // Set data ke views
                    TextView titleTextView = view.findViewById(R.id.detail_education_title);
                    TextView descriptionTextView = view.findViewById(R.id.detail_education_description);
                    ImageView imageView = view.findViewById(R.id.detail_education_image);

                    titleTextView.setText(title);
                    descriptionTextView.setText(description);
                    imageView.setImageResource(imageResourceId);
                }


                return view;
            }
        }
