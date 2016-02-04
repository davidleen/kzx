package com.android.yijiang.kzx.adapter;

import java.io.Serializable;
import java.text.DateFormat;

import com.android.yijiang.kzx.R;
import com.android.yijiang.kzx.bean.Contacts;
import com.android.yijiang.kzx.bean.MemberClientBean;
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
 * 客户满意
 * @title com.android.yijiang.kzx.adapter
 * @date 2014年6月12日
 * @author tanke
 */
public class KzxMemberClientAdapter extends BaseAdapter {
	private Context context;
	private List<MemberClientBean> memberClientList=new ArrayList<MemberClientBean>();

	public KzxMemberClientAdapter(Context context) {
		this.context = context;
	}
	
	public void setDataForLoad(List<MemberClientBean> memberClientList,boolean isClear){
		if(isClear){
			this.memberClientList.clear();
		}
		this.memberClientList.addAll(memberClientList);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return memberClientList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if (position >= getCount()) {
			return null;
		}
		return memberClientList.get(position);
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
			convertView = LayoutInflater.from(context).inflate(R.layout.kzx_member_client_lv_item_fragment, parent, false);
			holder = new ViewHolder();
			holder.clientNameTv=(TextView)convertView.findViewById(R.id.clientNameTv);
			holder.clientPhoneTv=(TextView)convertView.findViewById(R.id.clientPhoneTv);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.clientNameTv.setText(memberClientList.get(position).getClientName());
		holder.clientPhoneTv.setText(memberClientList.get(position).getClientPhone());
		return convertView;
	}


	static class ViewHolder {
		public TextView clientNameTv;
		public TextView clientPhoneTv;
	}

}
