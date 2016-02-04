package com.android.yijiang.kzx.fragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.SocketTimeoutException;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.client.HttpResponseException;
import org.apache.http.conn.HttpHostConnectException;
import org.json.JSONObject;

import com.android.yijiang.kzx.R;
import com.android.yijiang.kzx.ResourceMap;
import com.android.yijiang.kzx.bean.AttachementBean;
import com.android.yijiang.kzx.bean.TaskBean;
import com.android.yijiang.kzx.bean.TaskProcessBean;
import com.android.yijiang.kzx.http.AsyncHttpClient;
import com.android.yijiang.kzx.http.AsyncHttpResponseHandler;
import com.android.yijiang.kzx.http.RequestParams;
import com.android.yijiang.kzx.sdk.ArithUtils;
import com.android.yijiang.kzx.sdk.FileUtil;
import com.android.yijiang.kzx.sdk.ImageUtils;
import com.android.yijiang.kzx.sdk.StringUtils;
import com.android.yijiang.kzx.ui.ApplicationController;
import com.android.yijiang.kzx.ui.Constants;
import com.android.yijiang.kzx.widget.ClearEditText;
import com.android.yijiang.kzx.widget.MsgTools;
import com.android.yijiang.kzx.widget.ScaleImageView;
import com.android.yijiang.kzx.widget.photoview.PhotoView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Picasso.LoadedFrom;
import com.squareup.picasso.Target;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnDismissListener;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.Html;
import android.text.format.DateUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

/**
 * 快执行附件下载弹出框
 * 
 * @title com.android.yijiang.kzx.fragment
 * @date 2014年6月12日
 * @author tanke
 */
public class KzxAttachementDownloadDialogFragment extends DialogFragment {

	private AsyncHttpClient asyncHttpClient;
	private AttachementBean attachementBean;

	private ImageButton closeBtn;
	private TextView titleTv;
	private ImageView attachementBg;
	private TextView attachementTv;
	private TextView sizeTv;
	private PhotoView attachementCoverBg;
	private ProgressBar default_load;
	private RelativeLayout attachementRelative;
	private Button downloadBtn;

	private String mData;
	private ProgressDialog dialog;
	private DownloadManager mgr = null;

	public static KzxAttachementDownloadDialogFragment newInstance(AttachementBean attachementBean) {
		KzxAttachementDownloadDialogFragment fragment = new KzxAttachementDownloadDialogFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable("attachementBean", attachementBean);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		mgr = (DownloadManager) getActivity().getSystemService(getActivity().DOWNLOAD_SERVICE);
		closeBtn = (ImageButton) view.findViewById(R.id.closeBtn);
		titleTv = (TextView) view.findViewById(R.id.titleTv);
		attachementBg = (ImageView) view.findViewById(R.id.attachementBg);
		attachementTv = (TextView) view.findViewById(R.id.attachementTv);
		sizeTv = (TextView) view.findViewById(R.id.sizeTv);
		attachementRelative=(RelativeLayout)view.findViewById(R.id.attachementRelative);
		downloadBtn = (Button) view.findViewById(R.id.downloadBtn);
		default_load=(ProgressBar)view.findViewById(R.id.default_load);
		attachementCoverBg=(PhotoView)view.findViewById(R.id.attachementCoverBg);
		downloadBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(attachementCoverBg.getVisibility()==View.GONE){
					postDownload();
				}else{
					if(!StringUtils.isEmpty(mData)){
						Picasso.with(getActivity())
						.load(mData)
						.placeholder(R.drawable.default_bg)
						.error(R.drawable.default_bg)
						.into(downloadTarget);
					}
				}
			}
		});
		closeBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		if (!StringUtils.isEmpty(attachementBean.getType())) {
			if (attachementBean.getType().indexOf("ppt") != -1) {
				attachementBg.setBackgroundResource(R.drawable.kzx_ic_task_process_ppt_128);
			} else if (attachementBean.getType().indexOf("doc") != -1) {
				attachementBg.setBackgroundResource(R.drawable.kzx_ic_task_process_word_128);
			} else if (attachementBean.getType().indexOf("xls") != -1) {
				attachementBg.setBackgroundResource(R.drawable.kzx_ic_task_process_excel_128);
			} else if (attachementBean.getType().indexOf("rar") != -1) {
				attachementBg.setBackgroundResource(R.drawable.kzx_ic_task_process_rar_128);
			} else if (attachementBean.getType().indexOf("pdf") != -1) {
				attachementBg.setBackgroundResource(R.drawable.kzx_ic_task_process_pdf_128);
			} else if (StringUtils.isPicture(attachementBean.getType())) {
				//图片直接显示
				attachementBg.setBackgroundResource(R.drawable.kzx_ic_task_process_image_128);
				attachementRelative.setVisibility(View.GONE);
				postDownload();
			} else {
				attachementBg.setBackgroundResource(R.drawable.kzx_ic_task_process_other_128);
			}
		} else {
			attachementBg.setBackgroundResource(R.drawable.kzx_ic_task_process_other_128);
		}
		attachementTv.setText(attachementBean.getName());
		sizeTv.setText(getResources().getString(ResourceMap.getString_file_size())+ArithUtils.getKb(attachementBean.getSize()));
	}

	public void startDownload(String downloadUrl) {
		Uri uri = Uri.parse(downloadUrl);
		Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).mkdirs();
		Request dwreq = new DownloadManager.Request(uri);
		dwreq.setTitle(attachementBean.getName());
		dwreq.setDescription(attachementBean.getName());
		dwreq.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, attachementBean.getName());
		dwreq.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
		dwreq.setVisibleInDownloadsUi(true);  
		long downloadId = mgr.enqueue(dwreq);
	}

	// 获取下载地址
	private void postDownload() {
		asyncHttpClient = new AsyncHttpClient();
		asyncHttpClient.addHeader("Cookie", getContext().getLoginUserCookie());
		asyncHttpClient.setUserAgent(Constants.USER_AGENT);
		RequestParams rp = new RequestParams();
		rp.put("id", attachementBean.getId());
		asyncHttpClient.post(getActivity(), Constants.downloadAPI, rp, responseHandler);
	}

	private AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
		public void onStart() {
			if (StringUtils.isPicture(attachementBean.getType())){
				default_load.setVisibility(View.VISIBLE);
			}else{
				dialog = new ProgressDialog(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
				dialog.setMessage(getResources().getString(ResourceMap.getString_file_url()));
				dialog.setIndeterminate(true);
				dialog.setCancelable(true);
				dialog.setOnDismissListener(new OnDismissListener() {
					@Override
					public void onDismiss(DialogInterface dialog) {
						asyncHttpClient.cancelRequests(getActivity(), true);
					}
				});
				dialog.show();
			}
		};

		@Override
		public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
			String content = new String(responseBody);
			try {
				boolean success=new JSONObject(content).optBoolean("success",false);
				String message=new JSONObject(content).optString("message");
				String data=new JSONObject(content).optString("data");
				if(success){
					mData=data;
					if (StringUtils.isPicture(attachementBean.getType())){
						attachementCoverBg.setVisibility(View.VISIBLE);
						Picasso.with(getActivity())
						.load(data)
						.placeholder(R.drawable.default_bg)
						.error(R.drawable.default_bg)
						.into(uiTarget);
					}else{
						startDownload(data);
						dismiss();
					}
				}else{
					MsgTools.toast(getActivity(), message, ResourceMap.LENGTH_SHORT);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public void onFinish() {
			if (dialog != null && dialog.isShowing()) {
				dialog.dismiss();
			}
			if (!StringUtils.isPicture(attachementBean.getType())){
				default_load.setVisibility(View.GONE);
			}
		};

		@Override
		public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
			if (error instanceof UnknownHostException) {
				MsgTools.toast(getActivity(), getString(R.string.request_network_error),  ResourceMap.LENGTH_SHORT);
			} else if (error instanceof HttpResponseException) {
				MsgTools.toast(getActivity(), getString(R.string.request_error),  ResourceMap.LENGTH_SHORT);
			} else if (error instanceof SocketTimeoutException) {
				MsgTools.toast(getActivity(), getString(R.string.request_timeout),  ResourceMap.LENGTH_SHORT);
			} else {
				MsgTools.toast(getActivity(), getString(R.string.request_error),  ResourceMap.LENGTH_SHORT);
			}
		}
	};
	
	Target uiTarget=new Target(){
		@Override
		public void onBitmapFailed(Drawable arg0) {
			// TODO Auto-generated method stub
		}
		@Override
		public void onBitmapLoaded(Bitmap mBitmap, LoadedFrom arg1) {
			default_load.setVisibility(View.GONE);
			BitmapDrawable bd = new BitmapDrawable(mBitmap);
			attachementCoverBg.setImageDrawable(bd);
		}
		@Override
		public void onPrepareLoad(Drawable arg0) {
		}
		
	};
	
	Target downloadTarget=new Target(){
		@Override
		public void onBitmapFailed(Drawable arg0) {
			// TODO Auto-generated method stub
		}
		@Override
		public void onBitmapLoaded(Bitmap mBitmap, LoadedFrom arg1) {
			File mFile=FileUtil.getFileFromBytes(mBitmap);
			if(mFile.exists()){
				MsgTools.toast(getActivity(), mFile.getAbsolutePath(),  ResourceMap.LENGTH_SHORT);
			}
		}
		@Override
		public void onPrepareLoad(Drawable arg0) {
			
		}
		
	};


	@Override
	public void onCreate(Bundle savedInstanceState) {
		attachementBean = (AttachementBean) getArguments().getSerializable("attachementBean");
		super.onCreate(savedInstanceState);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		getDialog().getWindow().setLayout(android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.FILL_PARENT);
		getDialog().setCancelable(true);
		getDialog().setCanceledOnTouchOutside(true);
		View view = inflater.inflate(R.layout.kzx_attachement_download_dialog_fragment, container, false);
		return view;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		return super.onCreateDialog(savedInstanceState);
	}

	public ApplicationController getContext() {
		return ((ApplicationController) getActivity().getApplicationContext());
	}
}
