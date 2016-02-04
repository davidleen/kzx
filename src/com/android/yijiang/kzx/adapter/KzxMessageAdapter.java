package com.android.yijiang.kzx.adapter;

import java.io.Serializable;
import java.text.DateFormat;

import com.android.yijiang.kzx.R;
import com.android.yijiang.kzx.bean.AmountMessageBean;
import com.android.yijiang.kzx.bean.MessageBean;
import com.android.yijiang.kzx.bean.TargetDutyersBean;
import com.android.yijiang.kzx.bean.TaskBean;
import com.android.yijiang.kzx.fragment.KzxFaceBackDialogFragment;
import com.android.yijiang.kzx.sdk.ACache;
import com.android.yijiang.kzx.sdk.DateUtil;
import com.android.yijiang.kzx.sdk.StringUtils;
import com.android.yijiang.kzx.ui.Constants;
import com.android.yijiang.kzx.widget.HorizontalListView;
import com.android.yijiang.kzx.widget.SquaredImageView;
import com.android.yijiang.kzx.widget.imageview.CircleImageView;

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
import android.support.v4.app.DialogFragment;
import android.test.MoreAsserts;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
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
 * 我的消息适配器
 * @title com.android.yijiang.kzx.adapter
 * @date 2014年6月12日
 * @author tanke
 */
public class KzxMessageAdapter extends BaseAdapter  implements Filterable{

	private Context context;
	private List<MessageBean> messageList=new ArrayList<MessageBean>();
	private DataFilter filter;
	private DisplayImageOptions options;
	
	public KzxMessageAdapter(Context context) {
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

	public void setDataForLoader(List<MessageBean> messageList) {
		this.messageList.clear();
		this.messageList.addAll(messageList);
		notifyDataSetChanged();
	}
	
	public void clearDataForLoader(){
		this.messageList.clear();
		notifyDataSetChanged();
	}
	
	/**获取未读消息信息*/
	public int getMessageAmount(){
		int amount=0;
		for (MessageBean messageBean : messageList) {
			amount=amount+messageBean.getNoReadNum();
		}
		return amount;
	}
	
	/**清空消息信息*/
	public void reloadDataForAmount(int readNum,int position){
		MessageBean messageBean=getItem(position);
		if(messageBean!=null){
			int newNum=messageBean.getNoReadNum()-readNum;
			messageBean.setNoReadNum(newNum<0?0:newNum);
			this.messageList.set(position, messageBean);
			notifyDataSetChanged();
		}
	}
	
	@Override
	public int getCount() {
		return messageList.size();
	}

	@Override
	public MessageBean getItem(int position) {
		// TODO Auto-generated method stub
		if (position >= getCount()) {
			return null;
		}
		return messageList.get(position);
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
			convertView = LayoutInflater.from(context).inflate(R.layout.kzx_message_lv_item_fragment, parent, false);
			holder = new ViewHolder();
			holder.user_bg = (CircleImageView) convertView.findViewById(R.id.user_bg);
			holder.noReadNumTv=(TextView) convertView.findViewById(R.id.noReadNumTv);
			holder.nameTv=(TextView) convertView.findViewById(R.id.nameTv);
			holder.messageTv=(TextView) convertView.findViewById(R.id.messageTv);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if(!StringUtils.isEmpty(messageList.get(position).getIcon())){
//			Picasso.with(context) //
//			.load(messageList.get(position).getIcon()) //
//			.placeholder(R.drawable.ic_avatar_120) //
//			.error(R.drawable.ic_avatar_120) //
//			.into(holder.user_bg);
			ImageLoader.getInstance().displayImage(messageList.get(position).getIcon(), holder.user_bg, options);
		}
		int noRreadNumber=messageList.get(position).getNoReadNum();
		holder.noReadNumTv.setVisibility(noRreadNumber!=0?View.VISIBLE:View.GONE);
		holder.noReadNumTv.setText(noRreadNumber>99?"99+":noRreadNumber+"");
		holder.nameTv.setText(messageList.get(position).getName());
		holder.messageTv.setText(messageList.get(position).getText());
		return convertView;
	}
	
	static class ViewHolder {
		public CircleImageView user_bg;
		public TextView noReadNumTv;
		public TextView nameTv;
		public TextView messageTv;
	}
	
	@Override
	public Filter getFilter() {
		if (filter == null) {
			filter = new DataFilter(messageList);
		}
		return filter;
	}
	
	private class DataFilter extends Filter {

		private List<MessageBean> original;
		public DataFilter(List<MessageBean> list) {
			this.original = list;
		}
		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			FilterResults results = new FilterResults();
			if (constraint == null || constraint.length() == 0) {
				results.values = original;
				results.count = original.size();
			} else {
				List<MessageBean> mList = new ArrayList<MessageBean>();
				for (MessageBean lb : original) {
					if (lb.getName().indexOf(constraint.toString())!=-1 ) {
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
			messageList = (List<MessageBean>) results.values;
			notifyDataSetChanged();
		}
	}

}
