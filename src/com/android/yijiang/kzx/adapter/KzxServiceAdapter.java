package com.android.yijiang.kzx.adapter;

import java.io.Serializable;
import java.text.DateFormat;

import com.android.yijiang.kzx.R;
import com.android.yijiang.kzx.bean.NoticeBean;
import com.android.yijiang.kzx.bean.ServicerBean;
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
 * 服务商适配器
 * @title com.android.yijiang.kzx.adapter
 * @date 2014年6月12日
 * @author tanke
 */
public class KzxServiceAdapter extends BaseAdapter {

	private Context context;
	private List<ServicerBean> servicerList = new ArrayList<ServicerBean>();
	private DisplayImageOptions options;

	public KzxServiceAdapter(Context context) {
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

	public void setDataForLoader(List<ServicerBean> servicerList,boolean isClear) {
		if(isClear){
			this.servicerList.clear();
		}
		this.servicerList.addAll(servicerList);
		notifyDataSetChanged();
	}

	public void clearDataForLoader(){
		this.servicerList.clear();
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return servicerList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if (position >= getCount()) {
			return null;
		}
		return servicerList.get(position);
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
			convertView = LayoutInflater.from(context).inflate(R.layout.kzx_service_lv_item_fragment, parent, false);
			holder = new ViewHolder();
			holder.company_bg=(CircleImageView)convertView.findViewById(R.id.company_bg);
			holder.stateTv=(ImageView)convertView.findViewById(R.id.stateTv);
			holder.titleTv=(TextView) convertView.findViewById(R.id.titleTv);
			holder.taskTv=(TextView) convertView.findViewById(R.id.taskTv);
			holder.commentTv=(TextView) convertView.findViewById(R.id.commentTv);
			holder.timeTv=(TextView) convertView.findViewById(R.id.timeTv);
			holder.timeTv.setVisibility(View.GONE);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
//		Picasso.with(context) //
//		.load(servicerList.get(position).getCompanyIcon()) //
//		.placeholder(R.drawable.default_bg) //
//		.error(R.drawable.default_bg) //
//		.into(holder.company_bg);
		ImageLoader.getInstance().displayImage(servicerList.get(position).getCompanyIcon(), holder.company_bg, options);
		holder.titleTv.setText(servicerList.get(position).getCompanyName());
		holder.taskTv.setText(context.getString(R.string.renwu_count,servicerList.get(position).getTaskNum()));
		holder.commentTv.setText(context.getString(R.string.daipingjia_count,servicerList.get(position).getNoAppraiseNum()));
		holder.timeTv.setText(DateUtil.friendly_time(DateUtils.getStrTime(servicerList.get(position).getCreateTime(),"yyyy-MM-dd HH:mm:ss")));
		holder.stateTv.setVisibility(servicerList.get(position).getNoReadNum()>0?View.VISIBLE:View.GONE);
		return convertView;
	}
	
	static class ViewHolder {
		public CircleImageView company_bg;
		public TextView titleTv;
		public ImageView stateTv;
		public TextView taskTv;
		public TextView commentTv;
		public TextView timeTv;
	}

}
