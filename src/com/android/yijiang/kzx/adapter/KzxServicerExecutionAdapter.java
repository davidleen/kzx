package com.android.yijiang.kzx.adapter;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.text.DateFormat;

import com.android.yijiang.kzx.R;
import com.android.yijiang.kzx.bean.AttachementBean;
import com.android.yijiang.kzx.bean.TaskBean;
import com.android.yijiang.kzx.bean.ProcessBean;
import com.android.yijiang.kzx.bean.TaskIdsBean;
import com.android.yijiang.kzx.fragment.KzxFaceBackDialogFragment;
import com.android.yijiang.kzx.sdk.ACache;
import com.android.yijiang.kzx.sdk.DateUtil;
import com.android.yijiang.kzx.sdk.StringUtils;
import com.android.yijiang.kzx.ui.ApplicationController;
import com.android.yijiang.kzx.ui.Constants;
import com.android.yijiang.kzx.ui.SingleFragmentActivity;
import com.android.yijiang.kzx.widget.imageview.CircleImageView;
import com.android.yijiang.kzx.widget.HorizontalListView;
import com.android.yijiang.kzx.widget.SquaredImageView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.squareup.picasso.Picasso;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 服务商执行适配器
 * @title com.android.yijiang.kzx.adapter
 * @date 2014年6月12日
 * @author tanke
 */
public class KzxServicerExecutionAdapter extends BaseAdapter implements Filterable{

	private Context context;
	private Gson gson;
	private Type tt;
	private List<TaskBean> taskBeanList = new ArrayList<TaskBean>();
	private DataFilter filter;
	
	private static final String TASK_STATE_0="0";
	private static final String TASK_STATE_1="1";
	private static final String TASK_STATE_2="2";
	private static final String TASK_STATE_3="3";
	private static final String TASK_STATE_4="4";
	private static final String TASK_STATE_5="5";
	private String clientIds;
	private DisplayImageOptions options;
	
	public KzxServicerExecutionAdapter(Context context) {
		this.context = context;
		gson = new Gson();
		tt = new TypeToken<List<AttachementBean>>() { 	}.getType();
		clientIds=getContext().getKzxTokenBean().getClientIds();
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

	public ApplicationController getContext() {
		return ((ApplicationController) context.getApplicationContext());
	}
	
	public void setDataForLoader(List<TaskBean> taskBeanList, boolean isClear) {
		if (isClear) {
			this.taskBeanList.clear();
		}
		this.taskBeanList.addAll(taskBeanList);
		notifyDataSetChanged();
	}

	public void refreshSchedule(TaskIdsBean taskIdsBean){
		for (TaskBean taskBean : taskBeanList) {
			if(taskBean.getId().equals(taskIdsBean.getTaskId())){
				taskBean.setSchedule(taskIdsBean.getSchedule());
				if(!taskBean.getState().equals(taskIdsBean.getState())){
					taskBeanList.remove(taskBean);
				}
				notifyDataSetChanged();
				break;
			}
		}
	}
	
	public void clearDataForGuo(TaskBean taskBean){
		this.taskBeanList.remove(taskBean);
		notifyDataSetChanged();
	}
	
	public void clearDataForLoader() {
		this.taskBeanList.clear();
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return taskBeanList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if (position >= getCount()) {
			return null;
		}
		return taskBeanList.get(position);
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
			convertView = LayoutInflater.from(context).inflate(R.layout.kzx_servicer_execution_lv_item_fragment, parent, false);
			holder = new ViewHolder();
			holder.user_bg = (CircleImageView) convertView.findViewById(R.id.user_bg);
			holder.urgencyTv=(TextView)convertView.findViewById(R.id.urgencyTv);
			holder.userTv = (TextView) convertView.findViewById(R.id.userTv);
			holder.titleTv = (TextView) convertView.findViewById(R.id.titleTv);
			holder.kzx_ic_task_alert = (ImageView) convertView.findViewById(R.id.kzx_ic_task_alert);
			holder.kzx_ic_task_attachment = (ImageView) convertView.findViewById(R.id.kzx_ic_task_attachment);
			holder.kzx_ic_task_time = (ImageView) convertView.findViewById(R.id.kzx_ic_task_time);
			holder.kzx_ic_task_message = (ImageView) convertView.findViewById(R.id.kzx_ic_task_message);
			holder.kzx_ic_task_message_tv = (TextView) convertView.findViewById(R.id.kzx_ic_task_message_tv);
			holder.kzx_ic_task_time_tv = (TextView) convertView.findViewById(R.id.kzx_ic_task_time_tv);
			holder.kzx_ic_task_divider = (ImageView) convertView.findViewById(R.id.kzx_ic_task_divider);
			holder.middleDivider=(ImageView)convertView.findViewById(R.id.middleDivider);
			holder.facebackBtn = (TextView) convertView.findViewById(R.id.facebackBtn);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		//是否紧急
		holder.urgencyTv.setVisibility("false".equals(taskBeanList.get(position).getIsUrgency())?View.GONE:View.VISIBLE);
		// 标题
		holder.titleTv.setText(taskBeanList.get(position).getTitle());
		// 危险任务
		if ("1".equals(taskBeanList.get(position).getIsDanger())) {
			holder.kzx_ic_task_alert.setVisibility(View.VISIBLE);
		} else {
			holder.kzx_ic_task_alert.setVisibility(View.GONE);
		}
		// 附件
		if (!StringUtils.isNullOrEmpty(taskBeanList.get(position).getAttachement())) {
			List<AttachementBean> attachementList = gson.fromJson(taskBeanList.get(position).getAttachement(), tt);
			holder.kzx_ic_task_attachment.setVisibility(!StringUtils.isNullOrEmpty(attachementList) ? View.VISIBLE : View.GONE);
		} else {
			holder.kzx_ic_task_attachment.setVisibility(View.GONE);
		}
		// 超期天数
		Date endDate=new Date(taskBeanList.get(position).getEndTime());
		int hour=DateUtil.getExpiredHour(DateUtil.dataToString(endDate));
		long diftime=0;
		if(taskBeanList.get(position).getCompleteTime()==0){
			diftime=System.currentTimeMillis() / 1000;
		}else{
			diftime=taskBeanList.get(position).getCompleteTime()/1000;
		}
		long dif = (diftime - taskBeanList.get(position).getEndTime() / 1000) / (24 * 60 * 60);
		if (dif > 0) {
			holder.kzx_ic_task_time.setColorFilter(Color.parseColor("#fd2e1f"));
			holder.kzx_ic_task_time_tv.setTextColor(Color.parseColor("#fd2e1f"));
			holder.kzx_ic_task_time_tv.setText(context.getResources().getString(R.string.day,dif));
		} else {
//			holder.kzx_ic_task_time_tv.setText(context.getResources().getString(R.string.day,Math.abs(dif)));
			if(hour>0&hour<=12){
				holder.kzx_ic_task_time_tv.setText(context.getResources().getString(R.string.day_bantian));
			}else if(hour>12&hour<=24){
				holder.kzx_ic_task_time_tv.setText(context.getResources().getString(R.string.day,1));
			}else{
				holder.kzx_ic_task_time_tv.setText(context.getResources().getString(R.string.day,Math.abs(dif)));
			}
			holder.kzx_ic_task_time_tv.setTextColor(Color.parseColor("#1ea839"));
			holder.kzx_ic_task_time.setColorFilter(Color.parseColor("#1ea839"));
		}
		// 最新反馈
		if (!StringUtils.isEmpty(taskBeanList.get(position).getLastProcess())) {
			ProcessBean lastProcess = gson.fromJson(taskBeanList.get(position).getLastProcess(), ProcessBean.class);
			holder.kzx_ic_task_message.setEnabled(true);
			holder.kzx_ic_task_divider.setVisibility(View.VISIBLE);
			String name = taskBeanList.get(position).getLastProcessCreaterName();
			String content = lastProcess.getContent();
			holder.kzx_ic_task_message_tv.setText(name + ":" + content);
		} else {
			holder.kzx_ic_task_message.setEnabled(false);
			holder.kzx_ic_task_divider.setVisibility(View.GONE);
			holder.kzx_ic_task_message_tv.setText(context.getResources().getString(R.string.taskprocess_empty));
		}
		// 头像
		if (!StringUtils.isEmpty(taskBeanList.get(position).getSponsorIcon())) {
			holder.user_bg.setVisibility(View.VISIBLE);
			holder.userTv.setVisibility(View.GONE);
//			Picasso.with(context) //
//					.load(taskBeanList.get(position).getSponsorIcon()) //
//					.placeholder(R.drawable.ic_avatar_120) //
//					.error(R.drawable.ic_avatar_120) //
//					.into(holder.user_bg);
			ImageLoader.getInstance().displayImage(taskBeanList.get(position).getSponsorIcon(), holder.user_bg, options);
		} else {
			holder.user_bg.setVisibility(View.GONE);
			holder.userTv.setVisibility(View.VISIBLE);
			String sponsorName = taskBeanList.get(position).getSponsorName();
			holder.userTv.setText(sponsorName.substring(sponsorName.length() - 1, sponsorName.length()));
		}
		if((TASK_STATE_3.equals(taskBeanList.get(position).getState())||TASK_STATE_4.equals(taskBeanList.get(position).getState()))&&!StringUtils.isEmpty(taskBeanList.get(position).getRelateClient())&&clientIds.indexOf(taskBeanList.get(position).getRelateClient())!=-1&&taskBeanList.get(position).getClientEndIsGood()==0){
			holder.facebackBtn.setText(context.getString(R.string.taskprocess_comment_title_hint));
		} else{
			holder.facebackBtn.setText(context.getString(R.string.taskprocess_comment_hint));
		}
		// 反馈
		holder.facebackBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mOnItemClickListener.onItemClick(position, taskBeanList.get(position));
			}
		});
		return convertView;
	}

	private OnItemClickListener mOnItemClickListener;

	public void setOnItemClickListener(OnItemClickListener l) {
		mOnItemClickListener = l;
	}

	public interface OnItemClickListener {
		public void onItemClick(int position, TaskBean taskBean);
	}

	static class ViewHolder {
		public CircleImageView user_bg;
		public TextView userTv;
		public TextView urgencyTv;
		public TextView titleTv;
		public ImageView kzx_ic_task_alert;
		public ImageView kzx_ic_task_attachment;
		public ImageView kzx_ic_task_time;
		public ImageView kzx_ic_task_message;
		public ImageView kzx_ic_task_divider;
		public TextView kzx_ic_task_time_tv;
		public TextView kzx_ic_task_message_tv;
		public ImageView middleDivider;
		public TextView facebackBtn;
	}


	@Override
	public Filter getFilter() {
		if (filter == null) {
			filter = new DataFilter(taskBeanList);
		}
		return filter;
	}
	
	private class DataFilter extends Filter {

		private List<TaskBean> original;
		public DataFilter(List<TaskBean> list) {
			this.original = list;
		}
		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			FilterResults results = new FilterResults();
			if (constraint == null || constraint.length() == 0) {
				results.values = original;
				results.count = original.size();
			} else {
				List<TaskBean> mList = new ArrayList<TaskBean>();
				for (TaskBean lb : original) {
					if (lb.getTitle().indexOf(constraint.toString())!=-1 || lb.getContent().indexOf(constraint.toString())!=-1) {
						mList.add(lb);
					}
				}
				results.values = mList;
				results.count = mList.size();
			}
			return results;
		}
		@Override
		protected void publishResults(CharSequence constraint, FilterResults results) {
			taskBeanList = (List<TaskBean>) results.values;
			notifyDataSetChanged();
		}
	}
	
}
