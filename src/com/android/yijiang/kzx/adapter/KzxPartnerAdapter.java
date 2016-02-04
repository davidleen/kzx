package com.android.yijiang.kzx.adapter;

import java.io.Serializable;
import java.text.DateFormat;

import com.android.yijiang.kzx.R;
import com.android.yijiang.kzx.bean.MessageBean;
import com.android.yijiang.kzx.bean.PartnerBean;
import com.android.yijiang.kzx.fragment.KzxFaceBackDialogFragment;
import com.android.yijiang.kzx.sdk.ACache;
import com.android.yijiang.kzx.sdk.DateUtil;
import com.android.yijiang.kzx.sdk.StringUtils;
import com.android.yijiang.kzx.ui.ApplicationController;
import com.android.yijiang.kzx.ui.Constants;
import com.android.yijiang.kzx.widget.HorizontalListView;
import com.android.yijiang.kzx.widget.SquaredImageView;
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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 我的伙伴适配器
 * @title com.android.yijiang.kzx.adapter
 * @date 2014年6月12日
 * @author tanke
 */
public class KzxPartnerAdapter extends ArrayAdapter {

	private Context context;
	private List<PartnerBean> partnerList = new ArrayList<PartnerBean>();
	private DisplayImageOptions options;

	public KzxPartnerAdapter(Context context) {
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
	
	public void setDataForLoader(List<PartnerBean> partnerList) {
		this.partnerList.clear();
		this.partnerList.addAll(partnerList);
		notifyDataSetChanged();
	}
	
	public void clearDataForLoader(){
		this.partnerList.clear();
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return partnerList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if (position >= getCount()) {
			return null;
		}
		return partnerList.get(position);
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
			convertView = LayoutInflater.from(context).inflate(R.layout.kzx_partner_lv_item_fragment, parent, false);
			holder = new ViewHolder();
			holder.actionBtn=(RelativeLayout) convertView.findViewById(R.id.actionBtn);
			holder.merchant_bg=(SquaredImageView)convertView.findViewById(R.id.merchant_bg);
			holder.nameTv=(TextView)convertView.findViewById(R.id.nameTv);
			holder.executionTv=(TextView)convertView.findViewById(R.id.executionTv);
			holder.checkTv=(TextView)convertView.findViewById(R.id.checkTv);
			holder.commentTv=(TextView)convertView.findViewById(R.id.commentTv);
			holder.infoBtn=(SquaredImageView)convertView.findViewById(R.id.infoBtn);
			holder.callBtn=(ImageButton)convertView.findViewById(R.id.callBtn);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
//		Picasso.with(context) //
//		.load(partnerList.get(position).getIcon()) //
//		.placeholder(R.drawable.default_bg) //
//		.error(R.drawable.default_bg) //
//		.into(holder.merchant_bg);
		ImageLoader.getInstance().displayImage(partnerList.get(position).getIcon(), holder.merchant_bg, options);
		holder.nameTv.setText(partnerList.get(position).getPartnerName());
		holder.executionTv.setText(partnerList.get(position).getState1Count()+"");
		holder.checkTv.setText(partnerList.get(position).getState2Count()+"");
		holder.commentTv.setText(partnerList.get(position).getState3Count()+"");
		holder.actionBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mOnItemClickListener.onItemClick(position,partnerList.get(position));
			}
		});
		holder.infoBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mOnItemClickListener.onItemClick(position,partnerList.get(position));
			}
		});
		holder.callBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(!StringUtils.isEmpty(partnerList.get(position).getPhone())){
					showCallDialog(partnerList.get(position).getPhone());
				}
			}
		});
		String memeberId=getContext().getKzxTokenBean().getEncryptMemberId();
		holder.callBtn.setVisibility(memeberId.equals(partnerList.get(position).getPartnerId())?View.GONE:View.VISIBLE);
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

	private OnItemClickListener mOnItemClickListener;

	public void setOnItemClickListener(OnItemClickListener l) {
		mOnItemClickListener = l;
	}

	public interface OnItemClickListener {
		public void onItemClick(int position,PartnerBean partnerBean);
	}
	
	static class ViewHolder {
		public SquaredImageView merchant_bg;
		public SquaredImageView infoBtn;
		public ImageButton callBtn;
		public RelativeLayout actionBtn;
		public TextView nameTv;
		public TextView executionTv;
		public TextView checkTv;
		public TextView commentTv;
	}

}
