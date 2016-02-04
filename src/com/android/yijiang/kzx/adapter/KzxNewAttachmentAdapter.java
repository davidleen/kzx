package com.android.yijiang.kzx.adapter;

import java.io.File;
import java.io.Serializable;
import java.text.DateFormat;

import com.android.yijiang.kzx.R;
import com.android.yijiang.kzx.bean.AcceptanceBean;
import com.android.yijiang.kzx.bean.LeaderBean;
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
import java.util.HashMap;
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
 * 添加新附件适配器
 * @title com.android.yijiang.kzx.adapter
 * @date 2014年6月12日
 * @author tanke
 */
public class KzxNewAttachmentAdapter extends BaseAdapter {

	private Context context;
	private List<File> attachmentList=new ArrayList<File>();

	public KzxNewAttachmentAdapter(Context context) {
		this.context = context;
	}
	
	public void setDataForLoader(List<File> attachmentList) {
		this.attachmentList.addAll(attachmentList);
		notifyDataSetChanged();
	}
	
	public void clearDataForLoader() {
		this.attachmentList.clear();
	}
	
	public void setDataForLoaderOne(File attachment) {
		clearDataForLoader();
		this.attachmentList.add(attachment);
		notifyDataSetChanged();
	}
	
	public void clearDataForLoaderOne(File attachment) {
		this.attachmentList.remove(attachment);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return attachmentList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if (position >= getCount()) {
			return null;
		}
		return attachmentList.get(position);
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
			convertView = LayoutInflater.from(context).inflate(R.layout.kzx_new_acceptance_lv_item_fragment, parent, false);
			holder = new ViewHolder();
			holder.acceptanceTv=(TextView)convertView.findViewById(R.id.acceptanceTv);
			holder.clearBtn=(ImageButton)convertView.findViewById(R.id.clearBtn);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.acceptanceTv.setText(attachmentList.get(position).getName());
		holder.clearBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mOnItemClickListener.onItemClick(position,attachmentList.get(position));
			}
		});
		return convertView;
	}


	private OnItemClickListener mOnItemClickListener;

	public void setOnItemClickListener(OnItemClickListener l) {
		mOnItemClickListener = l;
	}

	public interface OnItemClickListener {
		public void onItemClick(int position,File attachment);
	}
	
	static class ViewHolder {
		public TextView acceptanceTv;
		public ImageButton clearBtn;
	}

	public List<File> getAttachmentList() {
		return attachmentList;
	}

	public void setAttachmentList(List<File> attachmentList) {
		this.attachmentList = attachmentList;
	}

	
}
