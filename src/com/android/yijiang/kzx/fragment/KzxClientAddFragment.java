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
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts.Photo;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.android.yijiang.kzx.adapter.KzxClientSelectedAdapter;
import com.android.yijiang.kzx.adapter.KzxExecutionAdapter;
import com.android.yijiang.kzx.adapter.KzxInvitePartnerAdapter;
import com.android.yijiang.kzx.adapter.KzxMessageAdapter;
import com.android.yijiang.kzx.adapter.KzxPartnerAdapter;
import com.android.yijiang.kzx.adapter.KzxStrategicAdapter;
import com.android.yijiang.kzx.adapter.KzxStrategicInfoAdapter;
import com.android.yijiang.kzx.bean.ClientBean;
import com.android.yijiang.kzx.bean.Contacts;
import com.android.yijiang.kzx.bean.LeaderBean;
import com.android.yijiang.kzx.bean.QueryClientBean;
import com.android.yijiang.kzx.http.AsyncHttpClient;
import com.android.yijiang.kzx.http.AsyncHttpResponseHandler;
import com.android.yijiang.kzx.http.RequestParams;
import com.android.yijiang.kzx.sdk.BitmapUtil;
import com.android.yijiang.kzx.sdk.Blur;
import com.android.yijiang.kzx.sdk.FileUtil;
import com.android.yijiang.kzx.sdk.ImageCompressUtil;
import com.android.yijiang.kzx.sdk.RegexUtils;
import com.android.yijiang.kzx.sdk.StringUtils;
import com.android.yijiang.kzx.ui.ApplicationController;
import com.android.yijiang.kzx.ui.Constants;
import com.android.yijiang.kzx.ui.ContentFragmentActivity;
import com.android.yijiang.kzx.ui.LoginActivity;
import com.android.yijiang.kzx.ui.MainActivity;
import com.android.yijiang.kzx.ui.SingleFragmentActivity;
import com.android.yijiang.kzx.ui.MainActivity.MessageReceiver;
import com.android.yijiang.kzx.widget.CircleImageView;
import com.android.yijiang.kzx.widget.ClearEditText;
import com.android.yijiang.kzx.widget.MsgTools;
import com.android.yijiang.kzx.widget.PinnedHeaderListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.squareup.picasso.Picasso.LoadedFrom;

/**
 * 新增客户
 * 
 * @title com.android.yijiang.kzx.fragment
 * @date 2014年6月11日
 * @author tanke
 */
@SuppressLint("NewApi")
public class KzxClientAddFragment extends BaseFragment {

	private String TAG = KzxClientAddFragment.class.getName();

	private AutoCompleteTextView phoneTv;// 电话
	private AutoCompleteTextView nameTv;// 姓名

	private Dialog dialog;
	private ClientBean clientBean;
	private AsyncHttpClient asyncHttpClient;

	public static KzxClientAddFragment newInstance(ClientBean clientBean) {
		KzxClientAddFragment fragment = new KzxClientAddFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable("clientBean", clientBean);
		fragment.setArguments(bundle);
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		clientBean=(ClientBean) getArguments().getSerializable("clientBean");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.kzx_client_add_fragment, null);
		return contentView;
	}
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		postLoadAllClient();
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		phoneTv = (AutoCompleteTextView) view.findViewById(R.id.phoneTv);
		nameTv = (AutoCompleteTextView) view.findViewById(R.id.nameTv);
		if(clientBean!=null){
			phoneTv.setText(clientBean.getPhone());
			nameTv.setText(clientBean.getName());
		}
	}

	/**获取客户列表**/
	public void postLoadAllClient() {
		asyncHttpClient = new AsyncHttpClient();
		asyncHttpClient.addHeader("Cookie", getContext().getLoginUserCookie());
		asyncHttpClient.setUserAgent(Constants.USER_AGENT);
		asyncHttpClient.post(getActivity(), Constants.queryAllClientAPI, null, clientResponseHandler);
	}
	
	/**获取客户列表回调*/
	AsyncHttpResponseHandler clientResponseHandler=new AsyncHttpResponseHandler() {
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
					Gson gson=new Gson();
					List<QueryClientBean> clientList=gson.fromJson(data, new TypeToken<List<QueryClientBean>>() {}.getType());
					if(!StringUtils.isNullOrEmpty(clientList)){
						//客户电话提示
						List<String> clientPhoneList=new ArrayList<String>();
						for (QueryClientBean queryClientBean : clientList) {
							clientPhoneList.add(queryClientBean.getPhone());
						}
						ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),  
				                android.R.layout.simple_dropdown_item_1line,clientPhoneList);  
						phoneTv.setAdapter(adapter);  
						phoneTv.setThreshold(1);  
						//客户姓名提示
						List<String> clientNameList=new ArrayList<String>();
						for (QueryClientBean queryClientBean : clientList) {
							clientNameList.add(queryClientBean.getName());
						}
						ArrayAdapter<String> adapterName = new ArrayAdapter<String>(getActivity(),  
				                android.R.layout.simple_dropdown_item_1line,clientNameList);  
						nameTv.setAdapter(adapterName);  
						nameTv.setThreshold(1);  
						
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				MsgTools.toast(getActivity(), getString(R.string.request_error),  ResourceMap.LENGTH_SHORT);
			}
		}
		@Override
		public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
			onFailureToast(error);
		}
	};
	
	/**登录*/ 
	public void postSelectedClient() {
		String name=nameTv.getText().toString();
		String phone=phoneTv.getText().toString();
		if(StringUtils.isEmpty(phone)){
			MsgTools.toast(getActivity(), getString(R.string.input_phone_hint), ResourceMap.LENGTH_SHORT);
			return;
		} 
		if(!RegexUtils.checkMobile(phone)){
			MsgTools.toast(getActivity(), getString(R.string.input_true_phone_hint), ResourceMap.LENGTH_SHORT);
			return;
		} 
		if(StringUtils.isEmpty(name)){
			MsgTools.toast(getActivity(), getString(R.string.input_name_hint), ResourceMap.LENGTH_SHORT);
			return;
		} 
		asyncHttpClient = new AsyncHttpClient();
		RequestParams rp = new RequestParams();
		rp.put("name", name);
		rp.put("phone", phone);
		asyncHttpClient.addHeader("Cookie", getContext().getLoginUserCookie());
		asyncHttpClient.setUserAgent(Constants.USER_AGENT);
		asyncHttpClient.post(getActivity(), Constants.relateClientAPI, rp, responseHandler);
	}
	
	/**登录回调*/
	AsyncHttpResponseHandler responseHandler=new AsyncHttpResponseHandler() {
		@Override
		public void onStart() {
			super.onStart();
			showProgressDialog();
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
					Gson gson=new Gson();
					final ClientBean clientBean=gson.fromJson(data, ClientBean.class);
					//自定义Dialog用于显示客户信息
					final Dialog dialog = new Dialog(getActivity(), R.style.mystyle);
					dialog.setContentView(R.layout.client_dialog);
					dialog.setCancelable(true);
					dialog.show();
					ImageButton btn_close=(ImageButton) dialog.findViewById(R.id.btn_close);
					TextView phoneTv=(TextView) dialog.findViewById(R.id.phoneTv);
					TextView nameTv=(TextView) dialog.findViewById(R.id.nameTv);
					phoneTv.setText(clientBean.getPhone());
					nameTv.setText(clientBean.getName());
					Button btnSure=(Button)dialog.findViewById(R.id.btnSure);
					btnSure.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							dialog.dismiss();
							Intent mIntent=new Intent(getActivity().getPackageName()+".ADD_TASK_RECEIVED_ACTION");
							mIntent.putExtra("data", clientBean);
							mIntent.putExtra("action", "client_add");
							getActivity().sendBroadcast(mIntent);
							getActivity().finish();
						}
					});
					btn_close.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							dialog.dismiss();
						}
					});
				}
			} catch (Exception e) {
				MsgTools.toast(getActivity(), getString(R.string.request_error),  ResourceMap.LENGTH_SHORT);
			}
		}
		@Override
		public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
			onFailureToast(error);
		}

		@Override
		public void onFinish() {
			super.onFinish();
			dialog.dismiss();
		}
	};
	
	/**进度弹出框*/
	private void showProgressDialog(){
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
