package com.android.yijiang.kzx.adapter;

import java.io.Serializable;
import java.text.DateFormat;

import com.android.yijiang.kzx.R;
import com.android.yijiang.kzx.bean.Contacts;
import com.android.yijiang.kzx.bean.MemberMedalBean;
import com.android.yijiang.kzx.fragment.KzxFaceBackDialogFragment;
import com.android.yijiang.kzx.sdk.ACache;
import com.android.yijiang.kzx.sdk.DateUtil;
import com.android.yijiang.kzx.sdk.DateUtils;
import com.android.yijiang.kzx.sdk.StringUtils;
import com.android.yijiang.kzx.ui.Constants;
import com.android.yijiang.kzx.widget.HorizontalListView;
import com.android.yijiang.kzx.widget.SquaredImageView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 勋章详情
 * @title com.android.yijiang.kzx.adapter
 * @date 2014年6月12日
 * @author tanke
 */
public class KzxMemberMedalAdapter extends BaseAdapter {
	private String imageUrl="/assets/medal/";
	private Context context;
	private List<MemberMedalBean> memberMedalList=new ArrayList<MemberMedalBean>();

	public KzxMemberMedalAdapter(Context context) {
		this.context = context;
	}
	
	public void setDataForLoad(List<MemberMedalBean> memberMedalList,boolean isClear){
		if(isClear){
			this.memberMedalList.clear();
		}
		this.memberMedalList.addAll(memberMedalList);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return memberMedalList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if (position >= getCount()) {
			return null;
		}
		return memberMedalList.get(position);
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
			convertView = LayoutInflater.from(context).inflate(R.layout.kzx_member_medal_lv_item_fragment, parent, false);
			holder = new ViewHolder();
			holder.medal_bg=(ImageView)convertView.findViewById(R.id.medal_bg);
			holder.configNameTv=(TextView)convertView.findViewById(R.id.configNameTv);
			holder.reasonTv=(TextView)convertView.findViewById(R.id.reasonTv);
			holder.awarderNameTv=(TextView)convertView.findViewById(R.id.awarderNameTv);
			holder.createTimeTv=(TextView)convertView.findViewById(R.id.createTimeTv);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Picasso.with(context) //
		.load(Constants.locationAPI+imageUrl+memberMedalList.get(position).getIcon()) //
		.placeholder(R.drawable.default_bg) //
		.error(R.drawable.default_bg) //
		.into(holder.medal_bg);
		holder.configNameTv.setText(memberMedalList.get(position).getConfigName());
		holder.reasonTv.setText(memberMedalList.get(position).getReason());
		holder.awarderNameTv.setText(memberMedalList.get(position).getAwarderName()+"颁发");
		holder.createTimeTv.setText(DateUtil.friendly_time(DateUtils.getStrTime(memberMedalList.get(position).getCreateTime(),"yyyy-MM-dd HH:mm:ss")));
		return convertView;
	}


	static class ViewHolder {
		public ImageView medal_bg;
		public TextView configNameTv;
		public TextView reasonTv;
		public TextView awarderNameTv;
		public TextView createTimeTv;
	}

}
