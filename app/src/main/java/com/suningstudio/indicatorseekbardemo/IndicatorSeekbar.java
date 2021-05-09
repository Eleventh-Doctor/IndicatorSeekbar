package com.suningstudio.indicatorseekbardemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class IndicatorSeekbar extends LinearLayout {

    private static final String TAG = IndicatorSeekbar.class.getSimpleName();
    DisplayMetrics dm;
    ArrayList<Point> points = new ArrayList<>();

    public IndicatorSeekbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initLayout(attrs);
    }

    private void initLayout(AttributeSet attrs) {
        dm = getContext().getResources().getDisplayMetrics();
        TypedArray ta = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.IndicatorSeekbar, 0, 0);
        View view = inflate(getContext(), R.layout.seekbar, this);
        TextView tvGuest = view.findViewById(R.id.tvGuest);
        SeekBar sbGuest = view.findViewById(R.id.sbGuest);
        sbGuest.measure(View.MeasureSpec.makeMeasureSpec(dm.widthPixels, View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        try {
            CharSequence[] entries = ta.getTextArray(R.styleable.IndicatorSeekbar_android_entries);
            int duration = ta.getInteger(R.styleable.IndicatorSeekbar_animation_duration, 200);
            sbGuest.setMax(entries.length - 1);
            tvGuest.setText(entries[0].toString());
            // width actual of seekbar minus padding left and right
            int widthOfSeekbar = sbGuest.getMeasuredWidth() - sbGuest.getPaddingLeft() - sbGuest.getPaddingRight();
            // 5 điểm 4 đoạn
            int widthOfSegment = widthOfSeekbar / (entries.length - 1);
            for (int i = 0; i < entries.length; i++) {
                if (i == 0) {
                    points.add(new Point(sbGuest.getPaddingLeft(), 0));
                } else {
                    points.add(new Point(points.get(i - 1).x + widthOfSegment, 0));
                }
            }
            sbGuest.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    tvGuest.setText(entries[progress].toString());
                    if (progress == 0) {
                        tvGuest.animate().translationX(0).setDuration(duration);
                    } else if (progress == entries.length - 1) {
                        tvGuest.animate().translationX(dm.widthPixels - tvGuest.getWidth()).setDuration(duration);
                    } else {
                        tvGuest.animate().translationX(points.get(progress).x - (tvGuest.getWidth() / 2)).setDuration(duration);
                    }
                }
                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
