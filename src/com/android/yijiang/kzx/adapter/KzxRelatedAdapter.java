package com.android.yijiang.kzx.adapter;

import java.io.Serializable;
import java.text.DateFormat;

import com.android.yijiang.kzx.R;
import com.android.yijiang.kzx.bean.AcceptanceBean;
import com.android.yijiang.kzx.bean.TaskDetailBean;
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
 * 相关任务适配器
 * 
 * @title com.android.yijiang.kzx.adapter
 * @date 2014年6月12日
 * @author tanke
 */
public class KzxRelatedAdapter extends BaseAdapter {

	private Context context;
	private List<TaskDetailBean> taskDetailList = new ArrayList<TaskDetailBean>();

	public KzxRelatedAdapter(Context context) {
		this.context = context;
	}

	public void setDataForLoader(List<TaskDetailBean> taskDetailList) {
		this.taskDetailList.addAll(taskDetailList);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return taskDetailList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if (position >= getCount()) {
			return null;
		}
		return taskDetailList.get(position);
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
			convertView = LayoutInflater.from(context).inflate(R.layout.kzx_task_related_lv_item_fragment, parent, false);
			holder = new ViewHolder();
			holder.taskRelatedTitleTv = (TextView) convertView.findViewById(R.id.taskRelatedTitleTv);
			holder.taskRelatedContentTv = (TextView) convertView.findViewById(R.id.taskRelatedContentTv);
			holder.taskRelatedScheduleTv = (TextView) convertView.findViewById(R.id.taskRelatedScheduleTv);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.taskRelatedTitleTv.setText(taskDetailList.get(position).getTitle());
		holder.taskRelatedContentTv.setText(taskDetailList.get(position).getExecutorName());
		holder.taskRelatedScheduleTv.setText(context.getString(R.string.done_count,taskDetailList.get(position).getSchedule()));
		return convertView;
	}

	static class ViewHolder {
		public TextView taskRelatedTitleTv;
		public TextView taskRelatedContentTv;
		public TextView taskRelatedScheduleTv;
	}

}
