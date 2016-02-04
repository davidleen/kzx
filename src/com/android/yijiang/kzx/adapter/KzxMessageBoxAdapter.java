package com.android.yijiang.kzx.adapter;

import java.io.Serializable;
import java.text.DateFormat;

import com.android.yijiang.kzx.R;
import com.android.yijiang.kzx.adapter.KzxPartnerAdapter.OnItemClickListener;
import com.android.yijiang.kzx.bean.AcceptanceBean;
import com.android.yijiang.kzx.bean.AttachementBean;
import com.android.yijiang.kzx.bean.MessageBoxBean;
import com.android.yijiang.kzx.bean.PartnerBean;
import com.android.yijiang.kzx.fragment.KzxFaceBackDialogFragment;
import com.android.yijiang.kzx.sdk.ACache;
import com.android.yijiang.kzx.sdk.DateUtil;
import com.android.yijiang.kzx.sdk.StringUtils;
import com.android.yijiang.kzx.ui.Constants;
import com.android.yijiang.kzx.ui.SingleFragmentActivity;
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
import android.graphics.drawable.Drawable;
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
 * 消息适配器
 * @title com.android.yijiang.kzx.adapter
 * @date 2014年6月12日
 * @author tanke
 */
public class KzxMessageBoxAdapter extends BaseAdapter {

	private Context context;
	private Typeface tf;
	private List<MessageBoxBean> messageBoxList=new ArrayList<MessageBoxBean>();

	public KzxMessageBoxAdapter(Context context) {
		this.context = context;
		tf=Typeface.createFromAsset(context.getAssets(), "fonts/circular_book.otf");
	}

	public void setDataForLoader(List<MessageBoxBean> messageBoxList){
		this.messageBoxList.addAll(messageBoxList);
		notifyDataSetChanged();
	}
	
	public void clearDataForLoader(MessageBoxBean messageBoxBean){
		this.messageBoxList.remove(messageBoxBean);
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return messageBoxList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if (position >= getCount()) {
			return null;
		}
		return messageBoxList.get(position);
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
			convertView = LayoutInflater.from(context).inflate(R.layout.kzx_messagebox_lv_item_fragment, parent, false);
			holder = new ViewHolder();
			holder.messageTv=(TextView)convertView.findViewById(R.id.messageTv);
			holder.refuseInvite=(TextView)convertView.findViewById(R.id.refuseInvite);
			holder.acceptInvite=(TextView)convertView.findViewById(R.id.acceptInvite);
			holder.positionTv=(TextView)convertView.findViewById(R.id.positionTv);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.positionTv.setText((position+1)+"");
		holder.positionTv.setTypeface(tf);
		holder.messageTv.setText(messageBoxList.get(position).getText());
		holder.refuseInvite.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mOnItemClickListener.onItemClick(position, messageBoxList.get(position),"refuse");
			}
		});
		holder.acceptInvite.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mOnItemClickListener.onItemClick(position, messageBoxList.get(position),"accept");
			}
		});
		return convertView;
	}
	
	private OnItemClickListener mOnItemClickListener;

	public void setOnItemClickListener(OnItemClickListener l) {
		mOnItemClickListener = l;
	}

	public interface OnItemClickListener {
		public void onItemClick(int position,MessageBoxBean messageBoxBean,String typeStr);
	}

	static class ViewHolder {
		public TextView positionTv;
		public TextView messageTv;
		public TextView refuseInvite;
		public TextView acceptInvite;
	}

}
