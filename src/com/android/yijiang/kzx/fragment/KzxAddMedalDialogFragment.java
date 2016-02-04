package com.android.yijiang.kzx.fragment;

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
import com.android.yijiang.kzx.bean.MedalBean;
import com.android.yijiang.kzx.bean.MedalConfigBean;
import com.android.yijiang.kzx.http.AsyncHttpClient;
import com.android.yijiang.kzx.http.AsyncHttpResponseHandler;
import com.android.yijiang.kzx.http.RequestParams;
import com.android.yijiang.kzx.sdk.StringUtils;
import com.android.yijiang.kzx.ui.ApplicationController;
import com.android.yijiang.kzx.ui.Constants;
import com.android.yijiang.kzx.widget.MsgTools;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Picasso.LoadedFrom;
import com.squareup.picasso.Target;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
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
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

/**
 * 颁发勋章弹出框
 * 
 * @title com.yijiang.jiangdou.fragment
 * @date 2014年5月7日
 * @author tanke
 */
public class KzxAddMedalDialogFragment extends DialogFragment {

	private InputMethodManager mInputMethodManager;
	
	private MedalConfigBean medalConfigBean;
	private AsyncHttpClient asyncHttpClient;
	private MedalBean medalBean;
	private List<String> copytoList=new ArrayList<String>();
	private String imageUrl="/kzx/assets/medal/";
	private int position=0;
	
	private Dialog dialog;
	private ImageView medalIv;
	private TextView medalTv;
	private EditText contentTv;
	private Button btnSave;

	public static KzxAddMedalDialogFragment newInstance(MedalConfigBean medalConfigBean,MedalBean medalBean,int position) {
		KzxAddMedalDialogFragment fragment = new KzxAddMedalDialogFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable("medalBean", medalBean);
		bundle.putSerializable("medalConfigBean", medalConfigBean);
		bundle.putInt("position", 0);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		// 获取键盘
		mInputMethodManager = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
		medalIv = (ImageView) view.findViewById(R.id.medalIv);
		medalTv = (TextView) view.findViewById(R.id.medalTv);
		contentTv = (EditText) view.findViewById(R.id.contentTv);
		btnSave = (Button) view.findViewById(R.id.btnSave);
		Picasso.with(getActivity()) //
				.load(Constants.locationAPI+imageUrl+medalConfigBean.getIcon().replace("128", "256")) //
				.placeholder(R.drawable.default_bg) //
				.error(R.drawable.default_bg) //
				.into(medalIv);
		medalTv.setText(medalConfigBean.getName());
		btnSave.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String reason=contentTv.getText().toString();
				if(!StringUtils.isEmpty(reason)){
					postAddMedal(reason);
				}
			}
		});
		contentTv.requestFocus(); // EditText获得焦点
		getDialog().getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_VISIBLE); // 显示软键盘
	}

	/**添加勋章请求*/ 
	private void postAddMedal(String Reason){
		asyncHttpClient = new AsyncHttpClient();
		asyncHttpClient.addHeader("Cookie", getContext().getLoginUserCookie());
		asyncHttpClient.setUserAgent(Constants.USER_AGENT);
		RequestParams rp = new RequestParams();
		rp.put("copyto", new Gson().toJson(copytoList));
		rp.put("medalName", medalConfigBean.getName());
		rp.put("Reason", Reason);
		rp.put("ConfigId", medalConfigBean.getId());
		asyncHttpClient.post(getActivity(), Constants.addMedalAPI, rp, responseHandler);
	}
	
	/**添加勋章请求回调*/ 
	private AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
		@Override
		public void onStart() {
			super.onStart();
			dialog = new Dialog(getActivity(), R.style.mystyle);
			dialog.setContentView(R.layout.loading_dialog);
			dialog.setCancelable(true);
			dialog.setOnDismissListener(new OnDismissListener() {
				@Override
				public void onDismiss(DialogInterface dialog) {
					asyncHttpClient.cancelRequests(getActivity(), true);
				}
			});
			dialog.show();
		}
		@Override
		public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
			String content = new String(responseBody);
			try {
				boolean success = new JSONObject(content).optBoolean("success", false);
				String message = new JSONObject(content).optString("message");
				String data = new JSONObject(content).optString("data");
				if (success) {
					mInputMethodManager.hideSoftInputFromWindow(contentTv.getWindowToken(), InputMethodManager.RESULT_HIDDEN);
					dismiss();
					// 关闭当前页
					getActivity().finish();
					// 刷新发起信息页面
					medalBean.setMedalCount(medalBean.getMedalCount()+1);
					Intent mIntent=new Intent(getActivity().getPackageName()+".INCENTIVE_RECEIVED_ACTION");
					mIntent.putExtra("action", "reload");
					mIntent.putExtra("position", position);
					mIntent.putExtra("medalBean", medalBean);
					getActivity().sendBroadcast(mIntent);
				} 
				MsgTools.toast(getActivity(), message, 3000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		public void onFinish() {
			if(dialog!=null&&dialog.isShowing()){
				dialog.dismiss();
			}
		};
		@Override
		public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
			onFailureToast(error);
		}
	};
	
	/**数据请求失败回调*/
	private void onFailureToast(Throwable error){
		if (error instanceof UnknownHostException) {
			MsgTools.toast(getActivity(), getString(R.string.request_network_error), ResourceMap.LENGTH_SHORT);
		} else if (error instanceof HttpResponseException) {
			MsgTools.toast(getActivity(), getString(R.string.request_error), ResourceMap.LENGTH_SHORT);
		} else if (error instanceof SocketTimeoutException) {
			MsgTools.toast(getActivity(), getString(R.string.request_timeout), ResourceMap.LENGTH_SHORT);
		} else {
			MsgTools.toast(getActivity(), getString(R.string.request_error), ResourceMap.LENGTH_SHORT);
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		medalConfigBean = (MedalConfigBean) getArguments().getSerializable("medalConfigBean");
		medalBean=(MedalBean) getArguments().getSerializable("medalBean");
		position=getArguments().getInt("position", 0);
		copytoList.add(medalBean.getId());
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//		getDialog().getWindow().setLayout(android.view.ViewGroup.LayoutParams.FILL_PARENT, android.view.ViewGroup.LayoutParams.FILL_PARENT);
		getDialog().setCancelable(true);
		getDialog().setCanceledOnTouchOutside(true);
		View view = inflater.inflate(R.layout.kzx_add_medal_dialog_fragment, container, false);
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
