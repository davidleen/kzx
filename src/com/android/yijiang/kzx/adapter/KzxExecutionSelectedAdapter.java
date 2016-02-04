package com.android.yijiang.kzx.adapter;

import java.io.Serializable;
import java.text.DateFormat;

import com.android.yijiang.kzx.R;
import com.android.yijiang.kzx.adapter.KzxCopySelectedAdapter.ViewHolder;
import com.android.yijiang.kzx.bean.LeaderBean;
import com.android.yijiang.kzx.fragment.KzxFaceBackDialogFragment;
import com.android.yijiang.kzx.sdk.ACache;
import com.android.yijiang.kzx.sdk.DateUtil;
import com.android.yijiang.kzx.sdk.StringUtils;
import com.android.yijiang.kzx.ui.Constants;
import com.android.yijiang.kzx.ui.ContentFragmentActivity;
import com.android.yijiang.kzx.widget.imageview.CircleImageView;
import com.android.yijiang.kzx.widget.HorizontalListView;
import com.android.yijiang.kzx.widget.SquaredImageView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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
 * 执行人适配器
 * @title com.android.yijiang.kzx.adapter
 * @date 2014年6月12日
 * @author tanke
 */
public class KzxExecutionSelectedAdapter extends BaseAdapter implements Filterable{

	private Context context;
	private DataFilter filter;
	private String typeStr;
	private static HashMap<String, LeaderBean> isSelected;// 记录选中数据
	private List<LeaderBean> leaderList = new ArrayList<LeaderBean>();
	private DisplayImageOptions options;

	public KzxExecutionSelectedAdapter(Context context,String typeStr) {
		this.context = context;
		this.typeStr=typeStr;
		isSelected = new HashMap<String, LeaderBean>();
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

	public void setDataForLoader(List<LeaderBean> leaderList,HashMap<String, LeaderBean> isSelected) {
		this.leaderList.addAll(leaderList);
		if(isSelected!=null){
			//设置选中值
			this.isSelected=isSelected;
		}
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
			convertView = LayoutInflater.from(context).inflate(R.layout.kzx_execution_selected_lv_item_fragment, parent, false);
			holder = new ViewHolder();
			holder.user_bg = (CircleImageView) convertView.findViewById(R.id.user_bg);
			holder.titleTv = (TextView) convertView.findViewById(R.id.titleTv);
			holder.amountTv = (TextView) convertView.findViewById(R.id.amountTv);
			holder.departmentTv = (TextView) convertView.findViewById(R.id.departmentTv);
			holder.switchBox = (CheckBox) convertView.findViewById(R.id.switchBox);
			if("single-select".equals(typeStr)){
				holder.switchBox.setBackgroundResource(R.drawable.checkbox_task_single_style);
			}else{
				holder.switchBox.setBackgroundResource(R.drawable.checkbox_copy_style);
			}
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
//		Picasso.with(context) //
//		.load(leaderList.get(position).getIcon()) //
//		.placeholder(R.drawable.ic_avatar_120) //
//		.error(R.drawable.ic_avatar_120) 
//		.into(holder.user_bg);
		ImageLoader.getInstance().displayImage(leaderList.get(position).getIcon(), holder.user_bg, options);
		holder.titleTv.setText(leaderList.get(position).getName());
		holder.departmentTv.setText(leaderList.get(position).getDepartment());
		holder.amountTv.setText(context.getResources().getString(R.string.zaiban_count,leaderList.get(position).getExecCount()));
		holder.switchBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					if("single-select".equals(typeStr)){
						clearSelectedPosition();
					}
					isSelected.put(leaderList.get(position).getId(), leaderList.get(position));
				} else {
					isSelected.remove(leaderList.get(position).getId());
				}
			}
		});
		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				holder.switchBox.setChecked((isSelected.get(leaderList.get(position).getId()) == null ? true : false));
			}
		});
		holder.switchBox.setChecked((isSelected.get(leaderList.get(position).getId()) == null ? false : true));
		return convertView;
	}
	
	public void clearSelectedPosition(){
		isSelected.clear();
		notifyDataSetChanged();
	}
	
	public static HashMap<String, LeaderBean> getIsSelected() {
		return isSelected;
	}

	static class ViewHolder {
		public CircleImageView user_bg;
		public TextView titleTv;
		public TextView departmentTv;
		public TextView amountTv;
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

		private List<LeaderBean> original;

		public DataFilter(List<LeaderBean> list) {
			this.original = list;
		}

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			FilterResults results = new FilterResults();
			if (constraint == null || constraint.length() == 0) {
				results.values = original;
				results.count = original.size();
			} else {
				List<LeaderBean> mList = new ArrayList<LeaderBean>();
				for (LeaderBean lb : original) {
					if (lb.getName().toUpperCase().startsWith(constraint.toString().toUpperCase()) || lb.getDepartment().toUpperCase().startsWith(constraint.toString().toUpperCase())) {
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
			leaderList = (List<LeaderBean>) results.values;
			notifyDataSetChanged();
		}

	}

}
