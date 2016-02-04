package com.android.yijiang.kzx.adapter;

import java.io.Serializable;
import java.text.DateFormat;

import com.android.yijiang.kzx.R;
import com.android.yijiang.kzx.bean.MedalBean;
import com.android.yijiang.kzx.bean.MemberBean;
import com.android.yijiang.kzx.fragment.KzxFaceBackDialogFragment;
import com.android.yijiang.kzx.sdk.ACache;
import com.android.yijiang.kzx.sdk.DateUtil;
import com.android.yijiang.kzx.sdk.StringUtils;
import com.android.yijiang.kzx.ui.ApplicationController;
import com.android.yijiang.kzx.ui.Constants;
import com.android.yijiang.kzx.ui.ContentFragmentActivity;
import com.android.yijiang.kzx.widget.HorizontalListView;
import com.android.yijiang.kzx.widget.SquaredImageView;
import com.android.yijiang.kzx.widget.imageview.CircleImageView;
import com.android.yijiang.kzx.widget.listviewanimations.ArrayAdapter;

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
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 全体成员适配器
 * @title com.android.yijiang.kzx.adapter
 * @date 2014年6月12日
 * @author tanke
 */
public class KzxMemberAdapter extends ArrayAdapter {

	private Context context;
	private List<MemberBean> memberList = new ArrayList<MemberBean>();
	private static final int STATE_1=1;
	private static final int STATE_2=2;
	private static final int STATE_3=3;
	private DisplayImageOptions options;
	
	public KzxMemberAdapter(Context context) {
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
	
	public void setDataForLoader(List<MemberBean> memberList,boolean isClear) {
		if(isClear){
			this.memberList.clear();
		}
		this.memberList.addAll(memberList);
		notifyDataSetChanged();
	}
	
	public List<MemberBean> getMemberList(){
		return memberList;
	}
	
	public void clearDataForLoader(){
		this.memberList.clear();
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return memberList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if (position >= getCount()) {
			return null;
		}
		return memberList.get(position);
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
			convertView = LayoutInflater.from(context).inflate(R.layout.kzx_member_lv_item_fragment, parent, false);
			holder = new ViewHolder();
			holder.actionBtn=(RelativeLayout)convertView.findViewById(R.id.actionBtn);
			holder.user_bg=(SquaredImageView)convertView.findViewById(R.id.user_bg);
			holder.nameTv=(TextView)convertView.findViewById(R.id.nameTv);
			holder.departmentTv=(TextView)convertView.findViewById(R.id.departmentTv);
			holder.leaderNameTv=(TextView)convertView.findViewById(R.id.leaderNameTv);
			holder.infoBtn=(SquaredImageView)convertView.findViewById(R.id.infoBtn);
			holder.callBtn=(ImageButton)convertView.findViewById(R.id.callBtn);
			holder.stateTv=(TextView)convertView.findViewById(R.id.stateTv);
			holder.stateTv.setVisibility(View.GONE);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
//		Picasso.with(context).load(memberList.get(position).getIcon()) //
//		.placeholder(R.drawable.default_bg) //
//		.error(R.drawable.default_bg) //
//		.into(holder.user_bg);
		ImageLoader.getInstance().displayImage(memberList.get(position).getIcon(), holder.user_bg, options);
		holder.nameTv.setText(memberList.get(position).getName());
		if(StringUtils.isEmpty(memberList.get(position).getDepartment())){
			holder.departmentTv.setText("无");
		}else{
			holder.departmentTv.setText(memberList.get(position).getDepartment());
		}
		if(StringUtils.isEmpty(memberList.get(position).getLeaderName())){
			holder.leaderNameTv.setText("无");
		}else{
			holder.leaderNameTv.setText(memberList.get(position).getLeaderName());
		}
//		Drawable typeImage = null;
//		switch (memberList.get(position).getState()) {
//		case STATE_1:
//			typeImage = context.getResources().getDrawable(R.drawable.ppt_checkbox_on);
//			holder.stateTv.setText("邀请中");
//			holder.stateTv.setTextColor(Color.parseColor("#e0793b"));
//			break;
//		case STATE_2:
//			typeImage = context.getResources().getDrawable(R.drawable.ss_checkbox_on);
//			holder.stateTv.setText("已加入");
//			holder.stateTv.setTextColor(Color.parseColor("#1e975d"));
//			break;
//		case STATE_3:
//			typeImage = context.getResources().getDrawable(R.drawable.ss_checkbox_on_di);
//			holder.stateTv.setText("已离职");
//			holder.stateTv.setTextColor(Color.parseColor("#CF1e975d"));
//			break;
//		default:
//			break;
//		}
//		typeImage.setBounds(0, 0, typeImage.getMinimumWidth(), typeImage.getMinimumHeight());
//		holder.stateTv.setCompoundDrawables(typeImage, null, null, null);
		holder.actionBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent mIntent=new Intent(context,ContentFragmentActivity.class);
				mIntent.putExtra("action", "member_info");
				mIntent.putExtra("memberId", memberList.get(position).getId());
				context.startActivity(mIntent);
			}
		});
		holder.infoBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent mIntent=new Intent(context,ContentFragmentActivity.class);
				mIntent.putExtra("action", "member_info");
				mIntent.putExtra("memberId", memberList.get(position).getId());
				context.startActivity(mIntent);
			}
		});
		holder.callBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!StringUtils.isEmpty(memberList.get(position).getPhone())){
					showCallDialog(memberList.get(position).getPhone());
				}
			}
		});
		String memeberId=getContext().getKzxTokenBean().getEncryptMemberId();
		holder.callBtn.setVisibility(memeberId.equals(memberList.get(position).getId())?View.GONE:View.VISIBLE);
		return convertView;
	}

	public ApplicationController getContext() {
		return ((ApplicationController) context.getApplicationContext());
	}

	private void showCallDialog(final String phone){
		new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT).setIcon(null).setMessage(context.getResources().getString(R.string.call_phone_hint)).setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.dismiss();
				Intent intent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+phone));
			    context.startActivity(intent);
			}
		}).create().show();
	}
	
	static class ViewHolder {
		public RelativeLayout actionBtn;
		public SquaredImageView user_bg;
		public SquaredImageView infoBtn;
		public ImageButton callBtn;
		public TextView nameTv;
		public TextView departmentTv;
		public TextView leaderNameTv;
		public TextView stateTv;
	}

}
