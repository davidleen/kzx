package com.android.yijiang.kzx.fragment;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.apache.http.Header;
import org.apache.http.client.HttpResponseException;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnDismissListener;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.Html;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView.OnEditorActionListener;
import android.widget.AbsListView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.yijiang.kzx.R;
import com.android.yijiang.kzx.ResourceMap;
import com.android.yijiang.kzx.adapter.KzxExecutionAdapter;
import com.android.yijiang.kzx.adapter.KzxMessageAdapter;
import com.android.yijiang.kzx.adapter.KzxNoticeAdapter;
import com.android.yijiang.kzx.adapter.KzxPartnerAdapter;
import com.android.yijiang.kzx.adapter.KzxStrategicAdapter;
import com.android.yijiang.kzx.adapter.KzxStrategicInfoAdapter;
import com.android.yijiang.kzx.bean.AttachementBean;
import com.android.yijiang.kzx.bean.NoticeBean;
import com.android.yijiang.kzx.bean.TargetDutyersBean;
import com.android.yijiang.kzx.bean.TargetMemberBean;
import com.android.yijiang.kzx.bean.TaskCanRelateBean;
import com.android.yijiang.kzx.http.AsyncHttpClient;
import com.android.yijiang.kzx.http.AsyncHttpResponseHandler;
import com.android.yijiang.kzx.http.RequestParams;
import com.android.yijiang.kzx.sdk.ArithUtils;
import com.android.yijiang.kzx.sdk.BitmapUtil;
import com.android.yijiang.kzx.sdk.Blur;
import com.android.yijiang.kzx.sdk.DateUtil;
import com.android.yijiang.kzx.sdk.DateUtils;
import com.android.yijiang.kzx.sdk.FileUtil;
import com.android.yijiang.kzx.sdk.ImageCompressUtil;
import com.android.yijiang.kzx.sdk.RegexUtils;
import com.android.yijiang.kzx.sdk.StringUtils;
import com.android.yijiang.kzx.sdk.UIHelper;
import com.android.yijiang.kzx.ui.ApplicationController;
import com.android.yijiang.kzx.ui.Constants;
import com.android.yijiang.kzx.ui.ContentFragmentActivity;
import com.android.yijiang.kzx.ui.MainActivity;
import com.android.yijiang.kzx.ui.SingleFragmentActivity;
import com.android.yijiang.kzx.ui.MainActivity.MessageReceiver;
import com.android.yijiang.kzx.widget.CircleImageView;
import com.android.yijiang.kzx.widget.MsgTools;
import com.android.yijiang.kzx.widget.PinnedHeaderListView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.squareup.picasso.Picasso.LoadedFrom;

/**
 * 我的消息
 * 
 * @title com.android.yijiang.kzx.fragment
 * @date 2014年6月11日
 * @author tanke
 */
@SuppressLint("NewApi")
public class KzxNoticeInfoFragment extends BaseFragment {

	private String TAG = KzxBenchResultFragment.class.getName();
	private SwipeRefreshLayout swipeLayout;

	private TextView titleTv;
	private TextView timeTv;
	private TextView userTv;

	private boolean notDoDecrypt;
	private NoticeBean noticeBean;
	private String noticeId;
	private AsyncHttpClient asyncHttpClient;

	private LinearLayout attachementLayout;
	private TextView contentTv;

	// 头部View
	private ImageButton backBtn;

	public static KzxNoticeInfoFragment newInstance(NoticeBean noticeBean, String noticeId, boolean notDoDecrypt) {
		KzxNoticeInfoFragment fragment = new KzxNoticeInfoFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable("noticeBean", noticeBean);
		bundle.putString("noticeId", noticeId);
		bundle.putBoolean("notDoDecrypt", notDoDecrypt);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		noticeBean = (NoticeBean) getArguments().getSerializable("noticeBean");
		noticeId = getArguments().getString("noticeId", "");
		notDoDecrypt = getArguments().getBoolean("notDoDecrypt", false);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.kzx_notice_info_fragment, null);
		return contentView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		titleTv = (TextView) view.findViewById(R.id.titleTv);
		timeTv = (TextView) view.findViewById(R.id.timeTv);
		userTv = (TextView) view.findViewById(R.id.userTv);
		attachementLayout = (LinearLayout) view.findViewById(R.id.attachementLayout);
		contentTv = (TextView) view.findViewById(R.id.contentTv);
		backBtn = (ImageButton) view.findViewById(R.id.backBtn);
		backBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getActivity().finish();
				if(notDoDecrypt){
					Intent i = new Intent(getActivity(), MainActivity.class);
					i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
					getActivity().startActivity(i);
				}
			}
		});
		swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
		swipeLayout.setColorScheme(android.R.color.holo_blue_dark, android.R.color.holo_red_dark, android.R.color.holo_blue_dark, android.R.color.holo_red_dark);
		swipeLayout.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				postLoad();
			}
		});
	}
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		postLoad();
	}

	/** 加载headerView数据(notDoDecrypt:是否加密标致) */
	private void postLoad() {
		if (noticeBean == null && !StringUtils.isEmpty(noticeId)) {
			asyncHttpClient = new AsyncHttpClient();
			asyncHttpClient.addHeader("Cookie", getContext().getLoginUserCookie());
			asyncHttpClient.setUserAgent(Constants.USER_AGENT);
			RequestParams rp = new RequestParams();
			rp.put("id", noticeId);

			if (notDoDecrypt) {
				rp.put("notDoDecrypt", notDoDecrypt);
			}

			asyncHttpClient.post(getActivity(), Constants.noticeInfoAPI, rp, responseHandler);
		} else {
			swipeLayout.setEnabled(false);
			initDataForUI(noticeBean);
		}
	}

	/** 公告详情加载数据回调 */
	AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
		@Override
		public void onStart() {
			super.onStart();
			swipeLayout.setRefreshing(true);
		}

		@Override
		public void onFinish() {
			super.onFinish();
			swipeLayout.setRefreshing(false);
		}

		@Override
		public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
			String content = new String(responseBody);
			try {
				boolean success = new JSONObject(content).optBoolean("success", false);
				String message = new JSONObject(content).optString("message");
				String data = new JSONObject(content).optString("data");
				if (!success) {
					MsgTools.toast(getActivity(), message, ResourceMap.LENGTH_SHORT);
				} else {
					NoticeBean noticeBean = new Gson().fromJson(data, NoticeBean.class);
					initDataForUI(noticeBean);
				}
			} catch (Exception e) {
				e.printStackTrace();
				MsgTools.toast(getActivity(), getString(R.string.request_error), ResourceMap.LENGTH_SHORT);
			}
		}

		@Override
		public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
			onFailureToast(error);
		}
	};

	/** 初始化公告信息 */
	private void initDataForUI(NoticeBean noticeBean) {
		if (noticeBean == null) {
			return;
		}
		userTv.setText(getString(R.string.notice_user,noticeBean.getCreaterName()));
		titleTv.setText(noticeBean.getTitle());
		timeTv.setText(DateUtil.friendly_time(DateUtils.getStrTime(noticeBean.getCreateTime(), "yyyy-MM-dd HH:mm:ss")));
		// contentTv.getSettings().setSupportZoom(false);
		// contentTv.getSettings().setBuiltInZoomControls(false);
		// contentTv.getSettings().setJavaScriptEnabled(true);
		// contentTv.getSettings().setDefaultFontSize(contentTv.getSettings().getDefaultFontSize());
		// UIHelper.addWebImageShow(getActivity(), contentTv);
		// String body = UIHelper.WEB_STYLE + noticeBean.getContent();
		// // 过滤掉 img标签的width,height属性
		// body = body.replaceAll("(<img[^>]*?)\\s+width\\s*=\\s*\\S+", "$1");
		// body = body.replaceAll("(<img[^>]*?)\\s+height\\s*=\\s*\\S+", "$1");
		// // 添加点击图片放大支持
		// body = body.replaceAll("(<img[^>]+src=\")(\\S+)\"",
		// "$1$2\" onClick=\"javascript:mWebViewImageListener.onImageClick('$2')\"");
		// contentTv.loadDataWithBaseURL(null, body, "text/html", "utf-8",
		// null);
		// contentTv.setWebViewClient(UIHelper.getWebViewClient());
		// contentTv.setVisibility(View.VISIBLE);
		Spanned htmlStr = Html.fromHtml(noticeBean.getContent());
		contentTv.setText(htmlStr);
		// 附件
		if (!StringUtils.isEmpty(noticeBean.getAttachement())) {
			// 附件
			List<AttachementBean> attachementList = new Gson().fromJson(noticeBean.getAttachement(), new TypeToken<List<AttachementBean>>() {}.getType());
			if (!StringUtils.isNullOrEmpty(attachementList)) {
				// 循环添加附件视图
				attachementLayout.removeAllViews();
				for (final AttachementBean attachementBean : attachementList) {
					View attachementView = LayoutInflater.from(getActivity()).inflate(R.layout.kzx_task_attachment_lv_item_fragment, null);
					ImageView attachementBg = (ImageView) attachementView.findViewById(R.id.attachementBg);
					TextView attachmentTv = (TextView) attachementView.findViewById(R.id.attachmentTv);
					TextView attachmentSizeTv = (TextView) attachementView.findViewById(R.id.attachmentSizeTv);
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
					attachmentTv.setText(attachementBean.getName());
					attachmentSizeTv.setText(ArithUtils.getKb(attachementBean.getSize()));
					// 设置Tag管理
					attachementView.setTag(attachementBean.getName());
					// 添加到视图中
					attachementLayout.addView(attachementView);
					attachementView.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							DialogFragment myFragment = KzxAttachementDownloadDialogFragment.newInstance(attachementBean);
							myFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.mystyle3);
							myFragment.show(getFragmentManager(), TAG);
						}
					});
				}
			}
		}
	}

	public ApplicationController getContext() {
		return ((ApplicationController) getActivity().getApplicationContext());
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (asyncHttpClient != null)
			asyncHttpClient.cancelRequests(getActivity(), true);
	}

}
