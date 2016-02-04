package com.android.yijiang.kzx.adapter;

import java.io.Serializable;
import java.text.DateFormat;

import com.android.yijiang.kzx.R;
import com.android.yijiang.kzx.adapter.KzxIncentiveAdapter.OnItemClickListener;
import com.android.yijiang.kzx.bean.AcceptanceBean;
import com.android.yijiang.kzx.bean.MedalBean;
import com.android.yijiang.kzx.bean.MedalConfigBean;
import com.android.yijiang.kzx.bean.TaskBean;
import com.android.yijiang.kzx.fragment.KzxFaceBackDialogFragment;
import com.android.yijiang.kzx.sdk.ACache;
import com.android.yijiang.kzx.sdk.DateUtil;
import com.android.yijiang.kzx.sdk.KeyBoardUtils;
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

import com.nostra13.universalimageloader.core.DisplayImageOptions;
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
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.DialogFragment;
import android.test.MoreAsserts;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView.OnEditorActionListener;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 勋章信息适配器
 * @title com.android.yijiang.kzx.adapter
 * @date 2014年6月12日
 * @author tanke
 */
public class KzxMedalConfigAdapter extends BaseAdapter {

	private Context context;
	public static String imageUrl="/assets/medal/";
	private List<MedalConfigBean> medalConfigList=new ArrayList<MedalConfigBean>();
//	private PopupWindow popupWindow;
	private View rightMenuView ;
	private DisplayImageOptions options;
	public KzxMedalConfigAdapter(Context context) {
		this.context = context;
//		rightMenuView = LayoutInflater.from(context).inflate(R.layout.kzx_incentive_medal_config_write_item, null);
//		popupWindow = new PopupWindow(rightMenuView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//		popupWindow.setFocusable(true);// 取得焦点
//		popupWindow.setBackgroundDrawable(new BitmapDrawable());
//		popupWindow.update();  
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
	
	public void setDataForLoader(List<MedalConfigBean> medalConfigList){
		this.medalConfigList.clear();
		this.medalConfigList.addAll(medalConfigList);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return medalConfigList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if (position >= getCount()) {
			return null;
		}
		return medalConfigList.get(position);
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
			int widthLayout=context.getResources().getDisplayMetrics().widthPixels/3;
			int widthIcon=context.getResources().getDisplayMetrics().widthPixels/4;
			convertView = LayoutInflater.from(context).inflate(R.layout.kzx_incentive_medal_config_lv_item, parent, false);
			holder = new ViewHolder();
			holder.medalIcon=(ImageView)convertView.findViewById(R.id.medalIcon);
			holder.medalTv=(TextView)convertView.findViewById(R.id.medalTv);
			holder.addMedalBtn=(LinearLayout)convertView.findViewById(R.id.addMedalBtn);
			holder.medalPoint=(ImageView)convertView.findViewById(R.id.medalPoint);
			holder.addMedalBtn.getLayoutParams().height=widthLayout;
			holder.medalIcon.setMaxHeight(widthIcon);
			holder.medalIcon.setMaxWidth(widthIcon);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		int width=context.getResources().getDisplayMetrics().widthPixels/5;
		int height=width;
		Picasso.with(context) //
		.load(Constants.locationAPI+imageUrl+medalConfigList.get(position).getIcon().replace("128", "256")) //
		.placeholder(R.drawable.default_bg) //
		.resize(width, height, true)
		.error(R.drawable.default_bg) //
		.into(holder.medalIcon);
		holder.medalTv.setText(medalConfigList.get(position).getName());
		holder.addMedalBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
//				final EditText title=(EditText) rightMenuView.findViewById(R.id.titleEt);
//				final EditText contentEt=(EditText) rightMenuView.findViewById(R.id.contentEt);
//				title.setText(medalConfigList.get(position).getName());
//				contentEt.setText("");
//				holder.addMedalBtn.setEnabled(false);
//				holder.medalPoint.setVisibility(View.VISIBLE);
//				popupWindow.showAsDropDown(v);
//				popupWindow.setOnDismissListener(new OnDismissListener() {
//					@Override
//					public void onDismiss() {
//						KeyBoardUtils.closeKeybord(contentEt, context);
//						holder.medalPoint.setVisibility(View.GONE);
//						holder.addMedalBtn.setEnabled(true);
//					}
//				});
//				rightMenuView.findViewById(R.id.writeBtn).setOnClickListener(new OnClickListener() {
//					@Override
//					public void onClick(View v) {
//						String titleStr=title.getText().toString();
//						String contentStr=contentEt.getText().toString();
//						if(StringUtils.isEmpty(titleStr)){
//							return;
//						}
//						KeyBoardUtils.closeKeybord(contentEt, context);
//						mOnItemClickListener.onItemClick(medalConfigList.get(position),titleStr,contentStr);
//					}
//				});
//				contentEt.setOnEditorActionListener(new OnEditorActionListener() {
//					@Override
//					public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//						 if (actionId == KeyEvent.ACTION_DOWN || actionId == EditorInfo.IME_ACTION_SEND) {
//							String titleStr=title.getText().toString();
//							String contentStr=contentEt.getText().toString();
//							if(StringUtils.isEmpty(titleStr)){
//								return false;
//							}
//							KeyBoardUtils.closeKeybord(contentEt, context);
//							mOnItemClickListener.onItemClick(medalConfigList.get(position),titleStr,contentStr);
//		                 }
//		                 return true;
//					}
//				});
//				title.postDelayed(new Runnable() {
//					@Override
//					public void run() {
//						KeyBoardUtils.openKeybord(title, context);
//					}
//				}, 300);
				mOnItemClickListener.onItemClick(medalConfigList.get(position));
			}
		});
		return convertView;
	}
	
	private OnItemClickListener mOnItemClickListener;

	public void setOnItemClickListener(OnItemClickListener l) {
		mOnItemClickListener = l;
	}

	public interface OnItemClickListener {
		public void onItemClick(MedalConfigBean medalConfigBean);
	}
	
	static class ViewHolder {
		public LinearLayout addMedalBtn;
		public ImageView medalPoint;
		public ImageView medalIcon;
		public TextView medalTv;
	}

}
