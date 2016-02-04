package com.android.yijiang.kzx.adapter;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.yijiang.kzx.R;
import com.android.yijiang.kzx.bean.AttachementBean;
import com.android.yijiang.kzx.bean.TaskBean;
import com.android.yijiang.kzx.bean.TaskProcessBean;
import com.android.yijiang.kzx.fragment.KzxFaceBackDialogFragment;
import com.android.yijiang.kzx.sdk.ArithUtils;
import com.android.yijiang.kzx.sdk.DateUtil;
import com.android.yijiang.kzx.sdk.DateUtils;
import com.android.yijiang.kzx.sdk.StringUtils;
import com.android.yijiang.kzx.widget.imageview.CircleImageView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.squareup.picasso.Picasso;

/**
 * 反馈适配器
 * 
 * @title com.android.yijiang.kzx.adapter
 * @date 2014年6月12日
 * @author tanke
 */
public class KzxTaskProcessAdapter extends BaseAdapter {

	private Context context;
	private Gson gson;
	private Type tt;
	private List<TaskProcessBean> taskProcessList = new ArrayList<TaskProcessBean>();
	public static final int VALUE_TIME_TIP = 0;// 5种不同的布局
	public static final int VALUE_LEFT_TEXT = 1;
	public static final int VALUE_LEFT_IMAGE = 2;
	public static final int VALUE_RIGHT_TEXT = 4;
	public static final int VALUE_RIGHT_IMAGE = 5;
	
	private Drawable zuaiDrawable;
	private Drawable yanqiDrawable;
	private Drawable shunxuDrawable;
	private Drawable zhuajinDrawable;
	private Drawable jinduDrawable;
	private Drawable yanqiDrawableWhite;
	private Drawable shunxuDrawableWhite;
	private Drawable zhuajinDrawableWhite;
	private Drawable jinduDrawableWhite;
	private Drawable yanshouDrawable;
	private Drawable fangongDrawable;
	private Drawable haopingDrawable;
	private Drawable chapingDrawable;
	private DisplayImageOptions options;
	
	public KzxTaskProcessAdapter(Context context) {
		this.context = context;
		gson = new Gson();
		tt = new TypeToken<List<AttachementBean>>() { }.getType();
		
		zuaiDrawable = context.getResources().getDrawable(R.drawable.kzx_ic_task_process_zuai);
		yanqiDrawable = context.getResources().getDrawable(R.drawable.kzx_ic_task_process_more);
		shunxuDrawable = context.getResources().getDrawable(R.drawable.kzx_ic_task_process_ok);
		zhuajinDrawable= context.getResources().getDrawable(R.drawable.kzx_ic_task_process_fire);
		jinduDrawable = context.getResources().getDrawable(R.drawable.kzx_ic_task_process_more);
		yanqiDrawableWhite = context.getResources().getDrawable(R.drawable.kzx_ic_task_process_more_white);
		shunxuDrawableWhite = context.getResources().getDrawable(R.drawable.kzx_ic_task_process_ok_white);
		zhuajinDrawableWhite= context.getResources().getDrawable(R.drawable.kzx_ic_task_process_fire_white);
		jinduDrawableWhite = context.getResources().getDrawable(R.drawable.kzx_ic_task_process_more_white);
		yanshouDrawable = context.getResources().getDrawable(R.drawable.kzx_ic_taskprocess_arrow_action);
		fangongDrawable = context.getResources().getDrawable(R.drawable.kzx_ic_taskprocess_arrow_back_action);
		haopingDrawable = context.getResources().getDrawable(R.drawable.kzx_ic_taskprocess_good_comment_action);
		chapingDrawable = context.getResources().getDrawable(R.drawable.kzx_ic_taskprocess_bad_comment_action);
		
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

	public void setDataForLoader(List<TaskProcessBean> taskProcessList, boolean isClear) {
		if (isClear) {
			this.taskProcessList.clear();
		}
		this.taskProcessList.addAll(taskProcessList);
		Collections.sort(this.taskProcessList, new ComparatorTaskProcessBean());
		notifyDataSetChanged();
	}

	class ComparatorTaskProcessBean implements Comparator {
		public int compare(Object arg0, Object arg1) {
			TaskProcessBean user0 = (TaskProcessBean) arg0;
			TaskProcessBean user1 = (TaskProcessBean) arg1;
			int flag = String.valueOf(user0.getCreateTime()).compareTo(String.valueOf(user1.getCreateTime()));
			return flag;
		}
	}

	public void setDataForLoaderOne(TaskProcessBean taskProcessBean) {
		this.taskProcessList.add(taskProcessBean);
		notifyDataSetChanged();
	}

	public void clearDataForLoader() {
		this.taskProcessList.clear();
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return taskProcessList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if (position >= getCount()) {
			return null;
		}
		return taskProcessList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getViewTypeCount() {
		return 5;
	}

	@Override
	public int getItemViewType(int position) {
		TaskProcessBean msg = taskProcessList.get(position);
		int type = msg.getViewType();
		return type;
	}

	@SuppressLint("NewApi")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		TaskProcessBean msg = taskProcessList.get(position);
		int type = getItemViewType(position);
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			switch (type) {
			case VALUE_TIME_TIP:
				convertView = LayoutInflater.from(context).inflate(R.layout.faceback_list_item_time_tip, null);
				holder.tvTimeTip = (TextView) convertView.findViewById(R.id.tv_time_tip);
				holder.tvTimeTip.setText(DateUtils.getStrTime(msg.getCreateTime(), "MM/dd HH:mm"));
				break;
			// 左边
			case VALUE_LEFT_TEXT:
				convertView = LayoutInflater.from(context).inflate(R.layout.faceback_list_item_left_text, null);
				holder.ivLeftIcon = (CircleImageView) convertView.findViewById(R.id.iv_icon);
				holder.btnLeftText = (TextView) convertView.findViewById(R.id.btn_left_text);
				holder.btnLeftText.setMaxWidth(context.getResources().getDisplayMetrics().widthPixels - context.getResources().getDisplayMetrics().widthPixels / 4);
				holder.timeLeftTv = (TextView) convertView.findViewById(R.id.timeLeftTv);
				holder.type_left_text = (TextView) convertView.findViewById(R.id.type_left_text);
				holder.left_attachement = (LinearLayout) convertView.findViewById(R.id.left_attachement);
				break;
			case VALUE_LEFT_IMAGE:
//				convertView = LayoutInflater.from(context).inflate(R.layout.faceback_list_item_left_image, null);
//				holder.ivLeftIcon = (CircleImageView) convertView.findViewById(R.id.iv_icon);
//				holder.ivLeftImage = (ImageView) convertView.findViewById(R.id.iv_left_image);
				break;
			// 右边
			case VALUE_RIGHT_TEXT:
				convertView = LayoutInflater.from(context).inflate(R.layout.faceback_list_item_right_text, null);
				holder.ivRightIcon = (CircleImageView) convertView.findViewById(R.id.iv_icon);
				holder.btnRightText = (TextView) convertView.findViewById(R.id.btn_right_text);
				holder.type_right_text = (TextView) convertView.findViewById(R.id.type_right_text);
				holder.btnRightText.setMaxWidth(context.getResources().getDisplayMetrics().widthPixels - context.getResources().getDisplayMetrics().widthPixels / 4);
				holder.timeRightTv = (TextView) convertView.findViewById(R.id.timeRightTv);
				holder.right_attachement = (LinearLayout) convertView.findViewById(R.id.right_attachement);
				break;
			case VALUE_RIGHT_IMAGE:
//				convertView = LayoutInflater.from(context).inflate(R.layout.faceback_list_item_right_image, null);
//				holder.ivRightIcon = (CircleImageView) convertView.findViewById(R.id.iv_icon);
//				holder.ivRightImage = (ImageView) convertView.findViewById(R.id.iv_right_image);
				break;
			default:
				break;
			}
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		switch (type) {
		// 左边
		case VALUE_LEFT_TEXT:
			holder.btnLeftText.setVisibility(!StringUtils.isEmpty(msg.getContent()) ? View.VISIBLE : View.GONE);
			holder.btnLeftText.setText(Html.fromHtml(msg.getContent()));
//			holder.timeLeftTv.setText(DateUtil.convertDateToShowStr(new Date(msg.getCreateTime())));
			holder.timeLeftTv.setText(DateUtil.friendly_time(DateUtils.getStrTime(msg.getCreateTime(),"yyyy-MM-dd HH:mm:ss")));
			// 头像
			if (!StringUtils.isEmpty(msg.getCreaterIcon())) {
//				Picasso.with(context) //
//						.load(msg.getCreaterIcon()) //
//						.placeholder(R.drawable.ic_avatar_120) //
//						.error(R.drawable.ic_avatar_120) //
//						.into(holder.ivLeftIcon);
				ImageLoader.getInstance().displayImage(msg.getCreaterIcon(), holder.ivLeftIcon, options);
			}
			// 附件
			if (!StringUtils.isNullOrEmpty(msg.getAttachement())) {
				List<AttachementBean> attachementList = gson.fromJson(msg.getAttachement(), tt);
				holder.left_attachement.setVisibility(!StringUtils.isNullOrEmpty(attachementList) ? View.VISIBLE : View.GONE);
				Drawable fileImage = context.getResources().getDrawable(R.drawable.kzx_ic_task_attachment);
				fileImage.setBounds(0, 0, fileImage.getMinimumWidth(), fileImage.getMinimumHeight());
				holder.left_attachement.removeAllViews();
				for (final AttachementBean attachementBean : attachementList) {
					View attachementView = LayoutInflater.from(context).inflate(R.layout.faceback_attachement_lv_item, null);
					ImageView attachementBg = (ImageView) attachementView.findViewById(R.id.attachementBg);
					TextView attachementTv = (TextView) attachementView.findViewById(R.id.attachementTv);
					TextView sizeTv = (TextView) attachementView.findViewById(R.id.sizeTv);
					if (!StringUtils.isEmpty(attachementBean.getType())) {
						if (attachementBean.getType().indexOf("ppt") != -1) {
							attachementBg.setBackgroundResource(R.drawable.kzx_ic_task_process_ppt);
						} else if (attachementBean.getType().indexOf("doc") != -1) {
							attachementBg.setBackgroundResource(R.drawable.kzx_ic_task_process_word);
						} else if (attachementBean.getType().indexOf("xls") != -1) {
							attachementBg.setBackgroundResource(R.drawable.kzx_ic_task_process_excel);
						} else if (attachementBean.getType().indexOf("rar") != -1) {
							attachementBg.setBackgroundResource(R.drawable.kzx_ic_task_process_rar);
						} else if (attachementBean.getType().indexOf("pdf") != -1) {
							attachementBg.setBackgroundResource(R.drawable.kzx_ic_task_process_pdf);
						} else if (StringUtils.isPicture(attachementBean.getType())) {
							attachementBg.setBackgroundResource(R.drawable.kzx_ic_task_process_image);
						} else {
							attachementBg.setBackgroundResource(R.drawable.kzx_ic_task_process_other);
						}
					} else {
						attachementBg.setBackgroundResource(R.drawable.kzx_ic_task_process_other);
					}
					attachementTv.setText(attachementBean.getName());
					sizeTv.setText(ArithUtils.getKb(attachementBean.getSize()));
					attachementView.findViewById(R.id.attachementBtn).setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							mOnItemClickListener.onItemClick(attachementBean);
						}
					});
					holder.left_attachement.addView(attachementView);
				}
			} else {
				holder.left_attachement.setVisibility(View.GONE);
			}
			// 反馈类型
			holder.type_left_text.setVisibility(View.VISIBLE);
			holder.type_left_text.setTextColor(Color.parseColor("#777777"));
			validateTaskProcessType(holder.type_left_text,msg.getType(),0);
			// 进度条
//			if (!StringUtils.isEmpty(msg.getSchedule())) {
//				holder.schedule_progress_bar.setVisibility(View.VISIBLE);
//				holder.schedule_left_text.setVisibility(View.VISIBLE);
//				holder.schedule_progress_bar.setProgress(Integer.valueOf(msg.getSchedule()));
//				holder.schedule_left_text.setText(msg.getSchedule() + "%");
//			} else {
//				holder.schedule_progress_bar.setVisibility(View.GONE);
//				holder.schedule_left_text.setVisibility(View.GONE);
//			}
			break;
		// 右边
		case VALUE_RIGHT_TEXT:
			holder.btnRightText.setVisibility(!StringUtils.isEmpty(msg.getContent()) ? View.VISIBLE : View.GONE);
			holder.btnRightText.setText(Html.fromHtml(msg.getContent()));
//			holder.timeRightTv.setText(DateUtil.convertDateToShowStr(new Date(msg.getCreateTime())));
			holder.timeRightTv.setText(DateUtil.friendly_time(DateUtils.getStrTime(msg.getCreateTime(),"yyyy-MM-dd HH:mm:ss")));
			if (!StringUtils.isEmpty(msg.getCreaterIcon())) {
//				Picasso.with(context) //
//						.load(msg.getCreaterIcon()) //
//						.placeholder(R.drawable.ic_avatar_120) //
//						.error(R.drawable.ic_avatar_120) //
//						.into(holder.ivRightIcon);
				ImageLoader.getInstance().displayImage(msg.getCreaterIcon(), holder.ivRightIcon, options);
			}
			// 反馈类型
			holder.type_right_text.setVisibility(View.VISIBLE);
			validateTaskProcessType(holder.type_right_text,msg.getType(),1);
			// 附件
			if (!StringUtils.isNullOrEmpty(msg.getAttachement())) {
				List<AttachementBean> attachementList = gson.fromJson(msg.getAttachement(), tt);
				holder.right_attachement.setVisibility(!StringUtils.isNullOrEmpty(attachementList) ? View.VISIBLE : View.GONE);
				Drawable fileImage = context.getResources().getDrawable(R.drawable.kzx_ic_task_attachment);
				fileImage.setBounds(0, 0, fileImage.getMinimumWidth(), fileImage.getMinimumHeight());
				holder.right_attachement.removeAllViews();
				for (final AttachementBean attachementBean : attachementList) {
					View attachementView = LayoutInflater.from(context).inflate(R.layout.faceback_attachement_lv_item, null);
					ImageView attachementBg = (ImageView) attachementView.findViewById(R.id.attachementBg);
					TextView attachementTv = (TextView) attachementView.findViewById(R.id.attachementTv);
					TextView sizeTv = (TextView) attachementView.findViewById(R.id.sizeTv);
					if (!StringUtils.isEmpty(attachementBean.getType())) {
						if (attachementBean.getType().indexOf("ppt") != -1) {
							attachementBg.setBackgroundResource(R.drawable.kzx_ic_task_process_ppt);
						} else if (attachementBean.getType().indexOf("doc") != -1) {
							attachementBg.setBackgroundResource(R.drawable.kzx_ic_task_process_word);
						} else if (attachementBean.getType().indexOf("xls") != -1) {
							attachementBg.setBackgroundResource(R.drawable.kzx_ic_task_process_excel);
						} else if (attachementBean.getType().indexOf("rar") != -1) {
							attachementBg.setBackgroundResource(R.drawable.kzx_ic_task_process_rar);
						} else if (attachementBean.getType().indexOf("pdf") != -1) {
							attachementBg.setBackgroundResource(R.drawable.kzx_ic_task_process_pdf);
						} else if (StringUtils.isPicture(attachementBean.getType())) {
							attachementBg.setBackgroundResource(R.drawable.kzx_ic_task_process_image);
						} else {
							attachementBg.setBackgroundResource(R.drawable.kzx_ic_task_process_other);
						}
					} else {
						attachementBg.setBackgroundResource(R.drawable.kzx_ic_task_process_other);
					}
					attachementTv.setText(attachementBean.getName());
					try {
						sizeTv.setText(ArithUtils.getKb(attachementBean.getSize()));
					} catch (Exception e) {
						e.printStackTrace();
						sizeTv.setText("0KB");
					}
					attachementView.findViewById(R.id.attachementBtn).setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							mOnItemClickListener.onItemClick(attachementBean);
						}
					});
					holder.right_attachement.addView(attachementView);
				}
			} else {
				holder.right_attachement.setVisibility(View.GONE);
			}
			break;
		default:
			break;
		}
		return convertView;
	}
	
	private void validateTaskProcessType(TextView type_left_text,String type,int leftOrRight){
		if ("99".equals(type) || StringUtils.isEmpty(type) || "0".equals(type)) {
			type_left_text.setVisibility(View.GONE);
		} else if ("1".equals(type)) {
			zuaiDrawable.setBounds(0, 0, zuaiDrawable.getMinimumWidth(), zuaiDrawable.getMinimumHeight());
			type_left_text.setCompoundDrawables(zuaiDrawable, null, null, null);
			type_left_text.setText(context.getString(R.string.taskprocess_yudaowenti));
		} else if ("2".equals(type)) {
			yanqiDrawable.setBounds(0, 0, yanqiDrawable.getMinimumWidth(), yanqiDrawable.getMinimumHeight());
			yanqiDrawableWhite.setBounds(0, 0, yanqiDrawableWhite.getMinimumWidth(), yanqiDrawableWhite.getMinimumHeight());
			type_left_text.setCompoundDrawables(leftOrRight==0?yanqiDrawable:yanqiDrawableWhite, null, null, null);
			type_left_text.setText(context.getString(R.string.taskprocess_yanqi));
		} else if ("3".equals(type)) {
			shunxuDrawable.setBounds(0, 0, shunxuDrawable.getMinimumWidth(), shunxuDrawable.getMinimumHeight());
			shunxuDrawableWhite.setBounds(0, 0, shunxuDrawableWhite.getMinimumWidth(), shunxuDrawableWhite.getMinimumHeight());
			type_left_text.setCompoundDrawables(leftOrRight==0?shunxuDrawable:shunxuDrawableWhite, null, null, null);
			type_left_text.setText(context.getString(R.string.taskprocess_shunli));
		} else if ("4".equals(type)) {
			zhuajinDrawable.setBounds(0, 0, zhuajinDrawable.getMinimumWidth(), zhuajinDrawable.getMinimumHeight());
			zhuajinDrawableWhite.setBounds(0, 0, zhuajinDrawableWhite.getMinimumWidth(), zhuajinDrawableWhite.getMinimumHeight());
			type_left_text.setCompoundDrawables(leftOrRight==0?zhuajinDrawable:zhuajinDrawableWhite, null, null, null);
			type_left_text.setText(context.getString(R.string.taskprocess_zhuajin));
		} else if ("5".equals(type)) {
			jinduDrawable.setBounds(0, 0, jinduDrawable.getMinimumWidth(), jinduDrawable.getMinimumHeight());
			jinduDrawableWhite.setBounds(0, 0, jinduDrawableWhite.getMinimumWidth(), jinduDrawableWhite.getMinimumHeight());
			type_left_text.setCompoundDrawables(leftOrRight==0?jinduDrawable:jinduDrawableWhite, null, null, null);
			type_left_text.setText(context.getString(R.string.taskprocess_jindu));
		}
	}

	private OnItemClickListener mOnItemClickListener;

	public void setOnItemClickListener(OnItemClickListener l) {
		mOnItemClickListener = l;
	}

	public interface OnItemClickListener {
		public void onItemClick(AttachementBean attachementBean);
	}

	static class ViewHolder {
		private TextView tvTimeTip;// 时间

		private CircleImageView ivLeftIcon;// 左边的头像
		private TextView btnLeftText;// 左边的文本
		private ImageView ivLeftImage;// 左边的图像
		private LinearLayout topLeftContent;
		private TextView timeLeftTv;
		private TextView type_left_text;// 类型
		private LinearLayout left_attachement;// 左边附件布局

		private CircleImageView ivRightIcon;// 右边的头像
		private TextView btnRightText;// 右边的文本
		private TextView type_right_text;
		private ImageView ivRightImage;// 右边的图像
		private TextView timeRightTv;
		private LinearLayout right_attachement;
	}

}
