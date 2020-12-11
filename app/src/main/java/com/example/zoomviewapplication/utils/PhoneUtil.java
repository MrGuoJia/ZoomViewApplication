package com.example.zoomviewapplication.utils;

import android.content.Context;

public class PhoneUtil {
    /**
     * dp 转 px
     */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
