package com.android.yijiang.kzx.adapter;

import java.io.Serializable;
import java.text.DateFormat;

import com.android.yijiang.kzx.R;
import com.android.yijiang.kzx.bean.NoticeBean;
import com.android.yijiang.kzx.bean.TargetMemberBean;
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
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 战略目标适配器
 * @title com.android.yijiang.kzx.adapter
 * @date 2014年6月12日
 * @author tanke
 */
public class KzxStrategicAdapter extends BaseAdapter {

	private Context context;
	private List<TargetMemberBean> targetMemberList = new ArrayList<TargetMemberBean>();

	public KzxStrategicAdapter(Context context) {
		this.context = context;
	}

	public void setDataForLoader(List<TargetMemberBean> targetMemberList,boolean isClear) {
		if(isClear){
			this.targetMemberList.clear();
		}
		this.targetMemberList.addAll(targetMemberList);
		notifyDataSetChanged();
	}

	public void clearDataForLoader(){
		this.targetMemberList.clear();
		notifyDataSetChanged();
	}
	
	
	@Override
	public int getCount() {
		return targetMemberList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if (position >= getCount()) {
			return null;
		}
		return targetMemberList.get(position);
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
			convertView = LayoutInflater.from(context).inflate(R.layout.kzx_strategic_lv_item_fragment, parent, false);
			holder = new ViewHolder();
			holder.DoneTargetSwitch=(TextView) convertView.findViewById(R.id.DoneTargetSwitch);
			holder.DoneOkSwitch=(TextView) convertView.findViewById(R.id.DoneOkSwitch);
			holder.DoneCancelSwitch=(TextView)convertView.findViewById(R.id.DoneCancelSwitch);
			holder.actionBtn=(LinearLayout)convertView.findViewById(R.id.actionBtn);
			holder.endTimeTv=(TextView)convertView.findViewById(R.id.endTimeTv);
			holder.titleTv=(TextView)convertView.findViewById(R.id.titleTv);
			holder.taskTv=(TextView)convertView.findViewById(R.id.taskTv);
			holder.dangerTv=(TextView)convertView.findViewById(R.id.dangerTv);
			holder.doneTargetBtn=(FrameLayout)convertView.findViewById(R.id.doneTargetBtn);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.endTimeTv.setText(DateUtils.getStrTime(targetMemberList.get(position).getEndTime(),"MM月dd日"));
		holder.titleTv.setText(targetMemberList.get(position).getTitle());
		holder.dangerTv.setVisibility(targetMemberList.get(position).getDangerCount()>0?View.VISIBLE:View.GONE);
		holder.taskTv.setText("总任务:"+targetMemberList.get(position).getTaskCount()+" "+"执行中:"+targetMemberList.get(position).getExecCount());
		holder.actionBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mOnItemClickListener.onItemClick(position,targetMemberList.get(position),0);
			}
		});
		holder.doneTargetBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(targetMemberList.get(position).getTaskCount()==0&&targetMemberList.get(position).getState()==1){
					mOnItemClickListener.onItemClick(position,targetMemberList.get(position),1);
				}
			}
		});
		//targetMemberList.get(position).getTaskCount()==0&&
		if(targetMemberList.get(position).getState()==1){
			holder.DoneTargetSwitch.setVisibility(View.VISIBLE);
			holder.DoneOkSwitch.setVisibility(View.GONE);
			holder.DoneCancelSwitch.setVisibility(View.GONE);
		}else if(targetMemberList.get(position).getState()==2){
			holder.DoneTargetSwitch.setVisibility(View.GONE);
			holder.DoneOkSwitch.setVisibility(View.GONE);
			holder.DoneCancelSwitch.setVisibility(View.VISIBLE);
		}else if(targetMemberList.get(position).getState()==3){
			holder.DoneTargetSwitch.setVisibility(View.GONE);
			holder.DoneOkSwitch.setVisibility(View.VISIBLE);
			holder.DoneCancelSwitch.setVisibility(View.GONE);
		}
		
		return convertView;
	}

	private OnItemClickListener mOnItemClickListener;

	public void setOnItemClickListener(OnItemClickListener l) {
		mOnItemClickListener = l;
	}

	public interface OnItemClickListener {
		public void onItemClick(int position,TargetMemberBean targetMemberBean,int type);
	}
	
	static class ViewHolder {
		public LinearLayout actionBtn;
		public TextView DoneTargetSwitch;
		public TextView DoneOkSwitch;
		public TextView DoneCancelSwitch;
		public TextView endTimeTv;
		public TextView titleTv;
		public TextView taskTv;
		public FrameLayout doneTargetBtn;
		public TextView dangerTv;
	}

}
