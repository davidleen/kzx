package com.android.yijiang.kzx.adapter;

import java.io.Serializable;
import java.text.DateFormat;

import com.android.yijiang.kzx.R;
import com.android.yijiang.kzx.adapter.KzxCopySelectedAdapter.OnItemClickListener;
import com.android.yijiang.kzx.bean.LeaderBean;
import com.android.yijiang.kzx.bean.TaskCanRelateBean;
import com.android.yijiang.kzx.fragment.KzxFaceBackDialogFragment;
import com.android.yijiang.kzx.sdk.ACache;
import com.android.yijiang.kzx.sdk.DateUtil;
import com.android.yijiang.kzx.sdk.DateUtils;
import com.android.yijiang.kzx.sdk.StringUtils;
import com.android.yijiang.kzx.ui.Constants;
import com.android.yijiang.kzx.ui.ContentFragmentActivity;
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
import android.support.v4.app.DialogFragment;
import android.test.MoreAsserts;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

/**
 * 关联任务适配器
 * @title com.android.yijiang.kzx.adapter
 * @date 2014年6月12日
 * @author tanke
 */
public class KzxTaskSelectedAdapter extends BaseAdapter implements Filterable{

	private Context context;
	private DataFilter filter;
	private static HashMap<String, TaskCanRelateBean> isSelected;// 记录选中数据
	private List<TaskCanRelateBean> leaderList = new ArrayList<TaskCanRelateBean>();

	public KzxTaskSelectedAdapter(Context context) {
		this.context = context;
		isSelected = new HashMap<String, TaskCanRelateBean>();
	}
	
	public void setDataForLoader(List<TaskCanRelateBean> leaderList,HashMap<String, TaskCanRelateBean> isSelected) {
		this.leaderList.addAll(leaderList);
		if(isSelected!=null){
			//设置选中值
			this.isSelected=isSelected;
		}
		notifyDataSetChanged();
	}
	
	public void removeDataForOne(String position,TaskCanRelateBean taskCanRelateBean){
		isSelected.remove(position);
		notifyDataSetChanged();
	}
	
	
	public void clearDataForLoader(){
		this.leaderList.clear();
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return leaderList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if (position >= getCount()) {
			return null;
		}
		return leaderList.get(position);
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
			convertView = LayoutInflater.from(context).inflate(R.layout.kzx_task_selected_lv_item_fragment, parent, false);
			holder = new ViewHolder();
			holder.titleTv = (TextView) convertView.findViewById(R.id.titleTv);
			holder.dateTv = (TextView) convertView.findViewById(R.id.dateTv);
			holder.switchBox = (CheckBox) convertView.findViewById(R.id.switchBox);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.titleTv.setText(leaderList.get(position).getTitle());
		holder.dateTv.setText(DateUtils.getStrTime(leaderList.get(position).getCreatetime(),"yyyy-MM-dd"));
		holder.switchBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					isSelected.put(leaderList.get(position).getId(), leaderList.get(position));
				} else {
					isSelected.remove(leaderList.get(position).getId());
				}
				mOnItemClickListener.onItemClick(leaderList.get(position).getId(), leaderList.get(position), isChecked);
			}
		});
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mOnItemClickListener.onItemClick(leaderList.get(position).getId(), leaderList.get(position), (isSelected.get(position) == null ? true : false));
				holder.switchBox.setChecked((isSelected.get(leaderList.get(position).getId())==null ? true : false));
			}
		});
		holder.switchBox.setChecked((isSelected.get(leaderList.get(position).getId())==null ? false : true));
		return convertView;
	}
	
	private OnItemClickListener mOnItemClickListener;

	public void setOnItemClickListener(OnItemClickListener l) {
		mOnItemClickListener = l;
	}

	public interface OnItemClickListener {
		public void onItemClick(String position, TaskCanRelateBean taskCanRelateBean,boolean isChecked);
	}

	
	public void clearSelectedPosition(){
		isSelected.clear();
		notifyDataSetChanged();
	}
	
	public static HashMap<String, TaskCanRelateBean> getIsSelected() {
		return isSelected;
	}
	
	static class ViewHolder {
		public TextView titleTv;
		public TextView dateTv;
		public CheckBox switchBox;
	}

	@Override
	public Filter getFilter() {
		if (filter == null) {
			filter = new DataFilter(leaderList);
		}
		return filter;
	}

	private class DataFilter extends Filter {

		private List<TaskCanRelateBean> original;

		public DataFilter(List<TaskCanRelateBean> list) {
			this.original = list;
		}

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			FilterResults results = new FilterResults();
			if (constraint == null || constraint.length() == 0) {
				results.values = original;
				results.count = original.size();
			} else {
				List<TaskCanRelateBean> mList = new ArrayList<TaskCanRelateBean>();
				for (TaskCanRelateBean lb : original) {
					if (lb.getTitle().toUpperCase().startsWith(constraint.toString().toUpperCase())) {
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
			leaderList = (List<TaskCanRelateBean>) results.values;
			notifyDataSetChanged();
		}

	}
	
}
