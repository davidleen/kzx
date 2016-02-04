package com.android.yijiang.kzx.adapter;

import java.io.Serializable;
import java.text.DateFormat;

import com.android.yijiang.kzx.R;
import com.android.yijiang.kzx.bean.MedalBean;
import com.android.yijiang.kzx.bean.NoticeBean;
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
 * 激励认可适配器
 * @title com.android.yijiang.kzx.adapter
 * @date 2014年6月12日
 * @author tanke
 */
public class KzxIncentiveAdapter extends BaseAdapter {

	private Context context;
	private List<MedalBean> medalList = new ArrayList<MedalBean>();
	private DisplayImageOptions options;

	public KzxIncentiveAdapter(Context context) {
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

	public void setDataForLoader(List<MedalBean> medalList,boolean isClear) {
		if(isClear){
			this.medalList.clear();
		}
		this.medalList.addAll(medalList);
		notifyDataSetChanged();
	}

	public void updateData(int position,MedalBean medalBean){
		medalList.set(position, medalBean);
		notifyDataSetChanged();
	}
	
	public void clearDataForLoader(){
		this.medalList.clear();
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return medalList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if (position >= getCount()) {
			return null;
		}
		return medalList.get(position);
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
			convertView = LayoutInflater.from(context).inflate(R.layout.kzx_incentive_lv_item_fragment, parent, false);
			holder = new ViewHolder();
			holder.addMedal=(LinearLayout) convertView.findViewById(R.id.addMedal);
			holder.nameTv=(TextView) convertView.findViewById(R.id.nameTv);
			holder.medalTv=(TextView) convertView.findViewById(R.id.medalTv);
			holder.user_bg=(CircleImageView) convertView.findViewById(R.id.user_bg);
			holder.taskLeaderGoodCountTv=(TextView) convertView.findViewById(R.id.taskLeaderGoodCountTv);
			holder.taskClientGoodCountTv=(TextView) convertView.findViewById(R.id.taskClientGoodCountTv);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
//		Picasso.with(context) //
//		.load(medalList.get(position).getIcon()) //
//		.placeholder(R.drawable.ic_avatar_120) //
//		.error(R.drawable.ic_avatar_120) //
//		.into(holder.user_bg);
		ImageLoader.getInstance().displayImage(medalList.get(position).getIcon(), holder.user_bg, options);
		holder.nameTv.setText(medalList.get(position).getName());
		holder.medalTv.setText(medalList.get(position).getMedalCount()+"");
		holder.taskLeaderGoodCountTv.setText(medalList.get(position).getTaskLeaderGoodCount()+"");
		holder.taskClientGoodCountTv.setText(medalList.get(position).getTaskClientGoodCount()+"");
		holder.addMedal.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mOnItemClickListener.onItemClick(position,medalList.get(position));
			}
		});
		return convertView;
	}

	private OnItemClickListener mOnItemClickListener;

	public void setOnItemClickListener(OnItemClickListener l) {
		mOnItemClickListener = l;
	}

	public interface OnItemClickListener {
		public void onItemClick(int position,MedalBean medalBean);
	}
	
	static class ViewHolder {
		public LinearLayout addMedal;
		public TextView nameTv;
		public CircleImageView user_bg;
		public TextView medalTv;
		public TextView taskLeaderGoodCountTv;
		public TextView taskClientGoodCountTv;
	}

}
