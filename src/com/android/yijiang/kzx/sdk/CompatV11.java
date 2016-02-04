package com.android.yijiang.kzx.sdk;
import android.animation.ValueAnimator;
import android.view.View;

class CompatV11 {

    static void setAlpha(View view, float alpha) {
        view.setAlpha(alpha);
    }

    static void postOnAnimation(View view, Runnable runnable) {
        view.postDelayed(runnable, ValueAnimator.getFrameDelay());
    }

}