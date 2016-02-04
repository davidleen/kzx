package com.android.yijiang.kzx.transformer;

import android.support.v4.view.ViewPager.PageTransformer;
import android.view.View;

public class CardTransformer implements PageTransformer {
	 
	private final float scalingStart;

	public CardTransformer(float scalingStart) {
		super();
		this.scalingStart = 1 - scalingStart;
	}

	@Override
	public void transformPage(View page, float position) {

		if (position >= 0) {
			final int w = page.getWidth();
			float scaleFactor = 1 - scalingStart * position;

			page.setAlpha(1 - position);
			page.setScaleX(scaleFactor);
			page.setScaleY(scaleFactor);
			page.setTranslationX(w * (1 - position) - w);
		}
	}
}