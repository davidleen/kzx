package com.android.yijiang.kzx.adapter;

import java.io.Serializable;
import java.text.DateFormat;

import com.android.yijiang.kzx.R;
import com.android.yijiang.kzx.bean.MedalBean;
import com.android.yijiang.kzx.bean.TargetDutyersBean;
import com.android.yijiang.kzx.fragment.KzxFaceBackDialogFragment;
import com.android.yijiang.kzx.sdk.ACache;
import com.android.yijiang.kzx.sdk.DateUtil;
import com.android.yijiang.kzx.sdk.StringUtils;
import com.android.yijiang.kzx.ui.Constants;
import com.android.yijiang.kzx.widget.HorizontalListView;
import com.android.yijiang.kzx.widget.SquaredImageView;
import com.android.yijiang.kzx.widget.imageview.CircleImageView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
 * 战略目标详情适配器
 * 
 * @title com.android.yijiang.kzx.adapter
 * @date 2014年6月12日
 * @author tanke
 */
public class KzxStrategicInfoAdapter extends BaseAdapter {

	private Context context;
	private List<TargetDutyersBean> targetDutyersList = new ArrayList<TargetDutyersBean>();
	private DisplayImageOptions options;

	public KzxStrategicInfoAdapter(Context context) {
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

	public void setDataForLoader(List<TargetDutyersBean> targetDutyersList) {
		this.targetDutyersList.clear();
		this.targetDutyersList.addAll(targetDutyersList);
		notifyDataSetChanged();
	}
	
	public void clearDataForLoader(){
		this.targetDutyersList.clear();
		notifyDataSetChanged();
	}
	
	
	@Override
	public int getCount() {
		return targetDutyersList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if (position >= getCount()) {
			return null;
		}
		return targetDutyersList.get(position);
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
			convertView = LayoutInflater.from(context).inflate(R.layout.kzx_strategic_info_lv_item_fragment, parent, false);
			holder = new ViewHolder();
			holder.user_bg = (CircleImageView) convertView.findViewById(R.id.user_bg);
			holder.nameTv = (TextView) convertView.findViewById(R.id.nameTv);
			holder.executeCountTv = (TextView) convertView.findViewById(R.id.executeCountTv);
			holder.sponsorCountTv = (TextView) convertView.findViewById(R.id.sponsorCountTv);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
//		Picasso.with(context) //
//				.load(targetDutyersList.get(position).getIcon()) //
//				.placeholder(R.drawable.ic_avatar_120) //
//				.error(R.drawable.ic_avatar_120) //
//				.into(holder.user_bg);
		ImageLoader.getInstance().displayImage(targetDutyersList.get(position).getIcon(), holder.user_bg, options);
		holder.nameTv.setText(targetDutyersList.get(position).getName());
		holder.executeCountTv.setText(context.getString(R.string.zhixingrenwu_count,targetDutyersList.get(position).getExecuteCount()));
		holder.sponsorCountTv.setText(context.getString(R.string.faqirenwu_count,targetDutyersList.get(position).getSponsorCount()));
		return convertView;
	}

	static class ViewHolder {
		public TextView nameTv;
		public TextView executeCountTv;
		public TextView sponsorCountTv;
		public CircleImageView user_bg;
	}

}
