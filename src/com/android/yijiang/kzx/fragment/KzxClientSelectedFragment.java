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
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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
import com.android.yijiang.kzx.widget.IndexableListView;
import com.android.yijiang.kzx.widget.MsgTools;
import com.android.yijiang.kzx.widget.PinnedHeaderListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.squareup.picasso.Picasso.LoadedFrom;

/**
 * 选择客户
 * @title com.android.yijiang.kzx.fragment
 * @date 2014年6月11日
 * @author tanke
 */
@SuppressLint("NewApi")
public class KzxClientSelectedFragment extends BaseFragment {

	private String TAG = KzxClientSelectedFragment.class.getName();
	private IndexableListView dateCustomList;
	private ReadContactTask readContactTask;
	private static final String[] PHONES_PROJECTION = new String[] { Phone.DISPLAY_NAME, Phone.NUMBER, Phone.CONTACT_ID };
	
	private HashMap<String, ClientBean> isLeaderHash;
    private static final int PHONES_DISPLAY_NAME_INDEX = 0; //联系人显示名称 
    private static final int PHONES_NUMBER_INDEX = 1; //电话号码 
    private static final int PHONES_CONTACT_ID_INDEX = 2;//联系人的ID  
	
    private Dialog dialog;
	private AsyncHttpClient asyncHttpClient;
	private KzxClientSelectedAdapter kzxClientSelectedAdapter;
	
	private EditText searchEt;

	public static KzxClientSelectedFragment newInstance(HashMap<String, ClientBean> isLeaderHash) {
		KzxClientSelectedFragment fragment = new KzxClientSelectedFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable("isClientHash", isLeaderHash);
		fragment.setArguments(bundle);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		isLeaderHash=(HashMap<String, ClientBean>) getArguments().getSerializable("isClientHash");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View contentView = inflater.inflate(R.layout.kzx_client_selected_fragment, null);
		return contentView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		searchEt=(EditText)view.findViewById(R.id.searchEt);
		kzxClientSelectedAdapter = new KzxClientSelectedAdapter(getActivity());
		dateCustomList = (IndexableListView) view.findViewById(R.id.dateCustomList);
		dateCustomList.setFastScrollEnabled(true);
		dateCustomList.setAdapter(kzxClientSelectedAdapter);
		//搜索框实时搜索
		searchEt.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				kzxClientSelectedAdapter.getFilter().filter(s);
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// 开始检索通讯录
		readContactTask = new ReadContactTask();
		readContactTask.execute();
	}
	
	//选中到添加任务界面
	public void postSelectedClient(){
		HashMap<String, ClientBean> isSelected=kzxClientSelectedAdapter.getIsSelected();
		Intent mIntent=new Intent(getActivity().getPackageName()+".ADD_TASK_RECEIVED_ACTION");
		mIntent.putExtra("data", isSelected);
		mIntent.putExtra("action", "client_selected");
		getActivity().sendBroadcast(mIntent);
		getActivity().finish();
	}

	/**读取联系人的信息*/ 
	public List<ClientBean> readAllContacts() {
		List<ClientBean> contactsList = new ArrayList<ClientBean>();
		Cursor phoneCursor = this.getContext().getContentResolver().query(Phone.CONTENT_URI, PHONES_PROJECTION, null, null, "sort_key_alt asc");
		if (phoneCursor != null) {
			while (phoneCursor.moveToNext()) {
				// 得到手机号码
				String phoneNumber = phoneCursor.getString(PHONES_NUMBER_INDEX);
				// 当手机号码为空的或者为空字段 跳过当前循环
				if (TextUtils.isEmpty(phoneNumber))
					continue;
				// 得到联系人名称
				String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME_INDEX);
				// 得到联系人ID
				Long contactid = phoneCursor.getLong(PHONES_CONTACT_ID_INDEX);
				contactsList.add(new ClientBean(contactid.toString(),phoneNumber, contactName));
			}

			phoneCursor.close();
		}
		return contactsList;
	}

	/**读取通讯录*/
	class ReadContactTask extends AsyncTask<Integer, Integer, List<ClientBean>> {
		@Override
		protected void onPreExecute() {
			dialog = new Dialog(getActivity(), R.style.mystyle);
			dialog.setContentView(R.layout.loading_dialog);
			dialog.setCancelable(false);
			dialog.show();
			super.onPreExecute();
		}

		@Override
		protected List<ClientBean> doInBackground(Integer... params) {
			return readAllContacts();
		}

		@Override
		protected void onPostExecute(List<ClientBean> contactsList) {
			super.onPostExecute(contactsList);
			// 关闭进度弹出框
			dialog.dismiss();
			kzxClientSelectedAdapter.setDataForLoader(contactsList,isLeaderHash);
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
		if (readContactTask != null && readContactTask.getStatus() != AsyncTask.Status.FINISHED)
			readContactTask.cancel(true);
	}

}
