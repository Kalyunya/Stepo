package com.example.fitstepo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class DashboardFragment extends Fragment {

    private ProgressBar progressBar;
    private TextView ptsText;

    private int maxPoints = 50;

    // 👇 ДОДАНО: дані користувача
    private String email;
    private String fullName;
    private String avatar;

    public DashboardFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);

        progressBar = root.findViewById(R.id.progressBar);
        ptsText = root.findViewById(R.id.pts);

        progressBar.setMax(maxPoints);

        // 👇 Зчитування з Bundle
        Bundle args = getArguments();
        if (args != null) {
            email = args.getString(Constants.EXTRA_EMAIL);
            fullName = args.getString(Constants.EXTRA_FULL_NAME);
            avatar = args.getString(Constants.EXTRA_AVATAR);
        }

        return root;
    }

    public void updateProgressBar(int points) {
        if (progressBar != null) {
            progressBar.setProgress(points);
        }
    }

    public void updatePtsText(int points) {
        if (ptsText != null) {
            ptsText.setText(points + " pts.");
        }
    }
}
