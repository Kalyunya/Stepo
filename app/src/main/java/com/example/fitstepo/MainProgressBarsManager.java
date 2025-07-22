package com.example.fitstepo;

import android.view.ViewGroup;
import android.widget.ImageView;

public class MainProgressBarsManager {

    private ImageView progressFill1;
    private ImageView progressFill3;
    private ImageView progressFill5;

    private int maxHeightPx;

    public MainProgressBarsManager(ImageView fill1, ImageView fill3, ImageView fill5, int maxHeightPx) {
        this.progressFill1 = fill1;
        this.progressFill3 = fill3;
        this.progressFill5 = fill5;
        this.maxHeightPx = maxHeightPx;
    }

    public void setProgress(int barIndex, int points, int maxPoints) {
        if (points < 0) points = 0;
        if (points > maxPoints) points = maxPoints;

        int newHeight = (int) ((points / (float) maxPoints) * maxHeightPx);

        ImageView bar = null;
        switch (barIndex) {
            case 0: bar = progressFill1; break;
            case 1: bar = progressFill3; break;
            case 2: bar = progressFill5; break;
            default: return;
        }

        if (bar != null) {
            ViewGroup.LayoutParams params = bar.getLayoutParams();
            params.height = newHeight;
            bar.setLayoutParams(params);
        }
    }
}