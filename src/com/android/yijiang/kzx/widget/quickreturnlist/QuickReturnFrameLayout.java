/*
 * Copyright 2013 Dave Morgan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.yijiang.kzx.widget.quickreturnlist;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.AbsListView.OnScrollListener;

/**
 * A FrameLayout that resizes itself in response to an AbsListview.
 * This mimics the "Quick Return" UI pattern as demonstrated by Roman Nurik here:
 * https://code.google.com/p/romannurik-code/source/browse/misc/scrolltricks
 * @author Dave Morgan
 */
public class QuickReturnFrameLayout extends FrameLayout implements OnScrollListener {

	/**
	 * A handy class for storing information about out list child views
	 */
	private static class ChildDescriptor {
		public int index;
		public int total;
		public int drawable;
		
		public ChildDescriptor(int index){
			this.index = index;
		}
		
		/**
		 * Calculates the approximate scroll position.
		 * This is the total size of the "imaginary views" that have scrolled off
		 * the top of the list. This will not work well if you don't have child
		 * views with fixed equal heights. Note: If you have large separators you
		 * should probably adjust this code or the view will resize noticeable 
		 * more slowly than your list is scrolling.
		 * @return the approximate scroll position in pixels
		 */
		public int getApproximateScrollPosition(){
			return index*total+(total-drawable);
		}
	}
	
	public static int MAX_HEIGHT_DP = 50;
	public static int MIN_HEIGHT_DP = 0;
	
	public static final int SCROLL_DIRECTION_INVALID = 0;
	public static final int SCROLL_DIRECTION_UP = 1;
	public static final int SCROLL_DIRECTION_DOWN = 2;
	
	/** So we can keep track of which direction we're going **/
	private int direction = SCROLL_DIRECTION_INVALID;
	/** So we can compare to the last updated position **/
	private ChildDescriptor lastchild;
	/** So we can let something else also listen for the scroll **/
	private OnScrollListener onscrolllistener;
	/** This holds our maximum view height in pixels. **/
	private int maxheight;
	/** This holds our minimum view height in pixels. **/
	private int minheight;
	
	public QuickReturnFrameLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		calculateMinMax();
	}

	public QuickReturnFrameLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		calculateMinMax();
	}

	public QuickReturnFrameLayout(Context context) {
		super(context);
		calculateMinMax();
	}
	
	/**
	 * Converts our static DP min/max heights for this view into pixels so they will be more useful.
	 */
	private void calculateMinMax(){
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		maxheight = (int) (metrics.density * (float)MAX_HEIGHT_DP);
		minheight = (int) (metrics.density * (float)MIN_HEIGHT_DP);
	}
	
	
	/**
	 * Adjusts the height of this view within a specified range.
	 * @param howmuch the number of pixels by which to change the height
	 * @param max the maximum height in pixels
	 * @param min the minimum height in pixels
	 */
	private void adjustHeight(int howmuch, int max, int min){
		
		// These conditionals will keep the view from flickering
		// as the first list child view bounces in and out of visibility.
		if((howmuch < 0)&&(direction != SCROLL_DIRECTION_UP)){
			direction = SCROLL_DIRECTION_UP;
			return;
		} else if((howmuch > 0)&&(direction != SCROLL_DIRECTION_DOWN)){
			direction = SCROLL_DIRECTION_DOWN;
			return;
		}
		
		// Now we restrict the height adjustment.
		int current = getHeight();
		current += howmuch;
		if(current < min){
			current = min;
		} else if(current > max){
			current = max;
		}
		// Make the adjustment.
		ViewGroup.LayoutParams f = getLayoutParams();
		if(f.height!=current){
			f.height=current;
			setLayoutParams(f);
		}
	}
	
	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		
		ChildDescriptor currentchild = getFirstChildItemDescriptor(view, firstVisibleItem);
		try{
			// attempt to adjust the height based on the difference of two probes
			adjustHeight(lastchild.getApproximateScrollPosition()-currentchild.getApproximateScrollPosition(), maxheight, minheight);
			lastchild = currentchild;
		} catch (NullPointerException e){
			// we'll always get an exception if the list isn't ready
			lastchild = currentchild;
		} catch (Exception e){ }
		
		// passing off to the extra listener, if there is one
		if(onscrolllistener != null){
			onscrolllistener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
		}
	}
	
	/**
	 * Stores some information on the first visible child view in a handy class. 
	 * @param view the target AbsListView
	 * @param index the list position of the first visible item
	 * @return a ChildDescriptor with useful information on our child view.
	 */
	private static ChildDescriptor getFirstChildItemDescriptor(AbsListView view, int index){
		ChildDescriptor h = new ChildDescriptor(index);
		try{
			Rect r = new Rect();
			View child = view.getChildAt(0);
			child.getDrawingRect(r);
			h.total = r.height();
			view.getChildVisibleRect(child, r, null);
			h.drawable = r.height();
			return h;
		} catch (Exception e){}
		return null;
	}


	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// passing off to the extra listener, if there is one
		if(onscrolllistener != null){
			onscrolllistener.onScrollStateChanged(view, scrollState);
		}
	}
	
	/**
	 * Sets this view as a listener on an AbsListView so it can adjust itself accordingly.
	 * @param view the target AbsListview
	 */
	public void attach(AbsListView view){
		view.setOnScrollListener(this);
	}
	
	/**
	 * Just in case we want something else to be able to listen as well.
	 * @param l another OnScrollListener
	 */
	public void setOnScrollListener(OnScrollListener l){
		onscrolllistener = l;
	}


}
