package com.android.yijiang.kzx.adapter;

import java.io.Serializable;
import java.text.DateFormat;

import com.android.yijiang.kzx.R;
import com.android.yijiang.kzx.adapter.KzxNewAcceptanceAdapter.OnItemClickListener;
import com.android.yijiang.kzx.adapter.KzxTaskProcessAdapter.ViewHolder;
import com.android.yijiang.kzx.bean.AcceptanceBean;
import com.android.yijiang.kzx.bean.AmountMessageBean;
import com.android.yijiang.kzx.bean.MessageAmountBean;
import com.android.yijiang.kzx.bean.MessageBean;
import com.android.yijiang.kzx.bean.TargetDutyersBean;
import com.android.yijiang.kzx.bean.TaskProcessBean;
import com.android.yijiang.kzx.fragment.KzxFaceBackDialogFragment;
import com.android.yijiang.kzx.sdk.ACache;
import com.android.yijiang.kzx.sdk.DateUtil;
import com.android.yijiang.kzx.sdk.DateUtils;
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
public class KzxMessageAmountAdapter extends BaseAdapter   implements Filterable{

	private Context context;
	private List<MessageAmountBean> messageList=new ArrayList<MessageAmountBean>();
	private AmountMessageBean amountMessageBean;
	private MessageBean messageBean;
	private static final String ACCEPT_INVITE="accept_invite";
	
	public static final int VALUE_LEFT_TEXT = 0;
	public static final int VALUE_RIGHT_TEXT = 1;
	public static final int VALUE_TEXT = 2;
	private DisplayImageOptions options;
	
	private int readNum;
	private DataFilter filter;

	public int getReadNum() {
		return readNum;
	}

	public void setReadNum(int readNum) {
		this.readNum = readNum;
	}

	public KzxMessageAmountAdapter(Context context,MessageBean messageBean) {
		this.context = context;
		this.messageBean=messageBean;
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

	public void setDataForLoader(List<MessageAmountBean> messageList,boolean isFirstLoader,AmountMessageBean amountMessageBean) {
		if(isFirstLoader){
			this.messageList.clear();
			this.amountMessageBean=amountMessageBean;
		}
		for (MessageAmountBean messageAmountBean : messageList) {
			if(!messageAmountBean.isState()){
				readNum++;
			}
		}
		this.messageList.addAll(messageList);
		notifyDataSetChanged();
	}
	
	public void clearDataForLoader(){
		this.messageList.clear();
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return messageList.size();
	}

	@Override
	public Object getItem(int position) {
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
	
	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public int getItemViewType(int position) {
		MessageAmountBean msg = (MessageAmountBean) getItem(position);
		if(msg.getFromAccountId()==amountMessageBean.getAccountId()){
			return VALUE_RIGHT_TEXT;
		}else{
			return VALUE_LEFT_TEXT;
		}
	}
	@SuppressLint("NewApi")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final MessageAmountBean msg = (MessageAmountBean) getItem(position);
		final int type = getItemViewType(position);
		ViewHolder holder = null;

		if (convertView == null) {
			holder = new ViewHolder();
			switch (type) {
			case VALUE_LEFT_TEXT:
				convertView = LayoutInflater.from(context).inflate(R.layout.kzx_message_account_left_lv_item_fragment, parent, false);
				holder.user_bg = (CircleImageView) convertView.findViewById(R.id.user_bg);
				holder.nameTv=(TextView) convertView.findViewById(R.id.nameTv);
				holder.stateTv=(ImageView) convertView.findViewById(R.id.stateTv);
				holder.timeTv=(TextView) convertView.findViewById(R.id.timeTv);
				holder.messageTv=(TextView) convertView.findViewById(R.id.messageTv);
				holder.btnAction=(Button)convertView.findViewById(R.id.btnAction);
				holder.user_content=(RelativeLayout)convertView.findViewById(R.id.user_content);
				break;
			case VALUE_RIGHT_TEXT:
				convertView = LayoutInflater.from(context).inflate(R.layout.kzx_message_account_right_lv_item_fragment, parent, false);
				holder.right_user_bg = (CircleImageView) convertView.findViewById(R.id.right_user_bg);
				holder.right_nameTv=(TextView) convertView.findViewById(R.id.right_nameTv);
				holder.right_stateTv=(TextView) convertView.findViewById(R.id.right_stateTv);
				holder.right_timeTv=(TextView) convertView.findViewById(R.id.right_timeTv);
				holder.user_content=(RelativeLayout)convertView.findViewById(R.id.user_content);
				holder.right_messageTv=(TextView) convertView.findViewById(R.id.right_messageTv);
				holder.user_content=(RelativeLayout)convertView.findViewById(R.id.user_content);
				break;
			}
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		switch (type) {
		case VALUE_LEFT_TEXT:
//			Picasso.with(context).load(messageBean.getIcon()).placeholder(R.drawable.ic_avatar_120).error(R.drawable.ic_avatar_120).into(holder.user_bg);
			ImageLoader.getInstance().displayImage(messageBean.getIcon(), holder.user_bg, options);
			holder.nameTv.setText(messageBean.getName());
			holder.messageTv.setText(msg.getContent().getText());
			holder.timeTv.setText(DateUtil.friendly_time(DateUtils.getStrTime(msg.getCreateTime(),"yyyy-MM-dd HH:mm:ss")));
			holder.stateTv.setVisibility(msg.isState()?View.GONE:View.VISIBLE);
			String actionLabel=msg.getContent().getAction();
			if(!StringUtils.isEmpty(actionLabel)){
				if(ACCEPT_INVITE.equals(actionLabel)){
					holder.btnAction.setVisibility(View.VISIBLE);
					holder.btnAction.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							mOnItemClickListener.onItemClick(position, msg,VALUE_RIGHT_TEXT);
						}
					});
				}else{
					holder.btnAction.setVisibility(View.GONE);
				}
			}else{
				holder.btnAction.setVisibility(View.GONE);
			}
			break;
		case VALUE_RIGHT_TEXT:
//			Picasso.with(context).load(amountMessageBean.getAccountIcon()).placeholder(R.drawable.default_bg).error(R.drawable.default_bg).into(holder.right_user_bg);
			ImageLoader.getInstance().displayImage(amountMessageBean.getAccountIcon(), holder.right_user_bg, options);
			holder.right_nameTv.setText(amountMessageBean.getAccountName());
			holder.right_messageTv.setText(msg.getContent().getText());
			String stateStr=msg.isState()?context.getString(R.string.read_hint):context.getString(R.string.noread_hint);
			holder.right_timeTv.setText(DateUtil.friendly_time(DateUtils.getStrTime(msg.getCreateTime(),"yyyy-MM-dd HH:mm:ss"))+"  "+stateStr);
			break;
		}
		holder.user_content.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mOnItemClickListener.onItemClick(position, msg,VALUE_TEXT);
			}
		});
		return convertView;
	}


	private OnItemClickListener mOnItemClickListener;

	public void setOnItemClickListener(OnItemClickListener l) {
		mOnItemClickListener = l;
	}

	public interface OnItemClickListener {
		public void onItemClick(int position,MessageAmountBean messageAmountBean,int typeCurrent);
	}
	

	@Override
	public Filter getFilter() {
		if (filter == null) {
			filter = new DataFilter(messageList);
		}
		return filter;
	}
	
	private class DataFilter extends Filter {

		private List<MessageAmountBean> original;
		public DataFilter(List<MessageAmountBean> list) {
			this.original = list;
		}
		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			FilterResults results = new FilterResults();
			if (constraint == null || constraint.length() == 0) {
				results.values = original;
				results.count = original.size();
			} else {
				List<MessageAmountBean> mList = new ArrayList<MessageAmountBean>();
				for (MessageAmountBean lb : original) {
					if (lb.getContent().getText().indexOf(constraint.toString())!=-1 ) {
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
			messageList = (List<MessageAmountBean>) results.values;
			notifyDataSetChanged();
		}
	}
	
	static class ViewHolder {
		public CircleImageView user_bg;
		public TextView nameTv;
		public ImageView stateTv;
		public TextView messageTv;
		public TextView timeTv;
		public Button btnAction;
		public CircleImageView right_user_bg;
		public TextView right_nameTv;
		public TextView right_stateTv;
		public TextView right_messageTv;
		public TextView right_timeTv;
		public RelativeLayout user_content;
	}

}
