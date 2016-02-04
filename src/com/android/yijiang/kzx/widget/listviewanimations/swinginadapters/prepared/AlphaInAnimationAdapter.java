package com.android.yijiang.kzx.widget.listviewanimations.swinginadapters.prepared;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.android.yijiang.kzx.widget.listviewanimations.swinginadapters.AnimationAdapter;
import com.android.yijiang.kzx.widget.nineoldandroids.animation.Animator;
import com.android.yijiang.kzx.widget.nineoldandroids.animation.Animator;
import com.android.yijiang.kzx.widget.nineoldandroids.animation.AnimatorListenerAdapter;
import com.android.yijiang.kzx.widget.nineoldandroids.animation.AnimatorSet;
import com.android.yijiang.kzx.widget.nineoldandroids.animation.ObjectAnimator;
import com.android.yijiang.kzx.widget.nineoldandroids.animation.ValueAnimator;
import com.android.yijiang.kzx.widget.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;
import com.android.yijiang.kzx.widget.nineoldandroids.view.ViewHelper;

import static com.android.yijiang.kzx.widget.nineoldandroids.view.ViewHelper.setAlpha;
import static com.android.yijiang.kzx.widget.nineoldandroids.view.ViewHelper.setTranslationX;
import static com.android.yijiang.kzx.widget.nineoldandroids.view.ViewPropertyAnimator.animate;

public class AlphaInAnimationAdapter extends AnimationAdapter {

    public AlphaInAnimationAdapter(final BaseAdapter baseAdapter) {
        super(baseAdapter);
    }

    @Override
    protected long getAnimationDelayMillis() {
        return DEFAULTANIMATIONDELAYMILLIS;
    }

    @Override
    protected long getAnimationDurationMillis() {
        return DEFAULTANIMATIONDURATIONMILLIS;
    }

    @Override
    public Animator[] getAnimators(final ViewGroup parent, final View view) {
        return new Animator[0];
    }
}
