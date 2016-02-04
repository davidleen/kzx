package com.android.yijiang.kzx.adapter;

import java.io.Serializable;
import java.text.DateFormat;

import com.android.yijiang.kzx.R;
import com.android.yijiang.kzx.bean.NoticeBean;
import com.android.yijiang.kzx.bean.TaskCanRelateBean;
import com.android.yijiang.kzx.fragment.KzxFaceBackDialogFragment;
import com.android.yijiang.kzx.sdk.ACache;
import com.android.yijiang.kzx.sdk.DateUtil;
import com.android.yijiang.kzx.sdk.DateUtils;
import com.android.yijiang.kzx.sdk.StringUtils;
import com.android.yijiang.kzx.ui.Constants;
import com.android.yijiang.kzx.widget.HorizontalListView;
import com.android.yijiang.kzx.widget.SquaredImageView;
import com.android.yijiang.kzx.widget.imageview.CircleImageView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.squareup.picasso.Picasso;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.app.DialogFragment;
import android.test.MoreAsserts;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 公告适配器
 * 
 * @title com.android.yijiang.kzx.adapter
 * @date 2014年6月12日
 * @author tanke
 */
public class KzxNoticeAdapter extends BaseAdapter {

	private Context context;
	private List<NoticeBean> noticeList = new ArrayList<NoticeBean>();
	private DisplayImageOptions options;
	
	public KzxNoticeAdapter(Context context) {
		this.context = context;
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.ic_avatar_120)
		.showImageForEmptyUri(R.drawable.ic_avatar_120)
		.showImageOnFail(R.drawable.ic_avatar_120)
		.cacheInMemory(true)
		.cacheOnDisk(true)
		.considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.displayer(new FadeInBitmapDisplayer(300))
		.build();
	}

	public void setDataForLoader(List<NoticeBean> noticeList, boolean isClear) {
		if (isClear) {
			this.noticeList.clear();
		}
		this.noticeList.addAll(noticeList);
		notifyDataSetChanged();
	}

	public void clearDataForLoader() {
		this.noticeList.clear();
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return noticeList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if (position >= getCount()) {
			return null;
		}
		return noticeList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@SuppressLint("NewApi")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.kzx_notice_lv_item_fragment, parent, false);
			holder = new ViewHolder();
			holder.titleTv = (TextView) convertView.findViewById(R.id.titleTv);
			holder.timeTv = (TextView) convertView.findViewById(R.id.timeTv);
			holder.user_bg = (CircleImageView) convertView.findViewById(R.id.user_bg);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
//		Picasso.with(context) //
//				.load(noticeList.get(position).getCreaterIcon()) //
//				.placeholder(R.drawable.ic_avatar_120) //
//				.error(R.drawable.ic_avatar_120) //
//				.into(holder.user_bg);
		ImageLoader.getInstance().displayImage(noticeList.get(position).getCreaterIcon(), holder.user_bg, options);
		holder.titleTv.setText(noticeList.get(position).getTitle());
		holder.timeTv.setText(DateUtil.friendly_time(DateUtils.getStrTime(noticeList.get(position).getCreateTime(), "yyyy-MM-dd HH:mm:ss")));
		return convertView;
	}

	static class ViewHolder {
		public TextView titleTv;
		public CircleImageView user_bg;
		public TextView timeTv;
	}

}
