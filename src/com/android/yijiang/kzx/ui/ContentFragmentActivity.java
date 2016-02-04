package com.android.yijiang.kzx.ui;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.http.client.HttpResponseException;
import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

import com.android.yijiang.kzx.R;
import com.android.yijiang.kzx.R.anim;
import com.android.yijiang.kzx.R.color;
import com.android.yijiang.kzx.R.drawable;
import com.android.yijiang.kzx.R.id;
import com.android.yijiang.kzx.R.layout;
import com.android.yijiang.kzx.R.string;
import com.android.yijiang.kzx.bean.AmountMemberBean;
import com.android.yijiang.kzx.bean.ClientBean;
import com.android.yijiang.kzx.bean.KzxTokenBean;
import com.android.yijiang.kzx.bean.LeaderBean;
import com.android.yijiang.kzx.bean.MedalBean;
import com.android.yijiang.kzx.bean.MemberBean;
import com.android.yijiang.kzx.bean.MessageBean;
import com.android.yijiang.kzx.bean.NoticeBean;
import com.android.yijiang.kzx.bean.PartnerBean;
import com.android.yijiang.kzx.bean.ServicerBean;
import com.android.yijiang.kzx.bean.TargetCanRelateBean;
import com.android.yijiang.kzx.bean.TargetMemberBean;
import com.android.yijiang.kzx.bean.TaskBean;
import com.android.yijiang.kzx.bean.TaskDetailBean;
import com.android.yijiang.kzx.bean.VersionsUpdate;
import com.android.yijiang.kzx.fragment.KzxAboutFragment;
import com.android.yijiang.kzx.fragment.KzxAccountInfoFragment;
import com.android.yijiang.kzx.fragment.KzxAddIncentiveFragment;
import com.android.yijiang.kzx.fragment.KzxAddTaskFragment;
import com.android.yijiang.kzx.fragment.KzxClientResultBatchFragment;
import com.android.yijiang.kzx.fragment.KzxCopySelectedFragment;
import com.android.yijiang.kzx.fragment.KzxCreateTeamFragment;
import com.android.yijiang.kzx.fragment.KzxCycleDoneFragment;
import com.android.yijiang.kzx.fragment.KzxExecutionSelectedFragment;
import com.android.yijiang.kzx.fragment.KzxExecutorSelectedFragment;
import com.android.yijiang.kzx.fragment.KzxGetPwdFragment;
import com.android.yijiang.kzx.fragment.KzxInviteFragment;
import com.android.yijiang.kzx.fragment.KzxMedalPersonSelectedFragment;
import com.android.yijiang.kzx.fragment.KzxMemberClientFragment;
import com.android.yijiang.kzx.fragment.KzxMemberInfoFragment;
import com.android.yijiang.kzx.fragment.KzxMemberLeaderFragment;
import com.android.yijiang.kzx.fragment.KzxMemberMedalFragment;
import com.android.yijiang.kzx.fragment.KzxMessageAmountFragment;
import com.android.yijiang.kzx.fragment.KzxMessageFragment;
import com.android.yijiang.kzx.fragment.KzxNoticeInfoFragment;
import com.android.yijiang.kzx.fragment.KzxPartnerBatchFragment;
import com.android.yijiang.kzx.fragment.KzxRegisterFragment;
import com.android.yijiang.kzx.fragment.KzxServiceResultFragment;
import com.android.yijiang.kzx.fragment.KzxServicerBenchResultFragment;
import com.android.yijiang.kzx.fragment.KzxSettingsFragment;
import com.android.yijiang.kzx.fragment.KzxStrategicInfoFragment;
import com.android.yijiang.kzx.fragment.KzxTargetSelectedFragment;
import com.android.yijiang.kzx.fragment.KzxTaskDetailInfoFragment;
import com.android.yijiang.kzx.fragment.KzxTaskModifyFragment;
import com.android.yijiang.kzx.fragment.KzxTaskProcessFragment;
import com.android.yijiang.kzx.fragment.KzxTaskSelectedFragment;
import com.android.yijiang.kzx.fragment.KzxTermSettingsFragment;
import com.android.yijiang.kzx.fragment.TestFragment;
import com.android.yijiang.kzx.http.AsyncHttpClient;
import com.android.yijiang.kzx.http.AsyncHttpResponseHandler;
import com.android.yijiang.kzx.http.RequestParams;
import com.android.yijiang.kzx.sdk.ShareToSNS;
import com.android.yijiang.kzx.sdk.StringUtils;
import com.android.yijiang.kzx.sdk.SystemBarTintManager;
import com.android.yijiang.kzx.transformer.CardTransformer;
import com.android.yijiang.kzx.transformer.DepthPageTransformer;
import com.android.yijiang.kzx.transformer.ZoomOutPageTransformer;
import com.android.yijiang.kzx.widget.MsgTools;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import android.annotation.SuppressLint;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewStub;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;

import com.android.yijiang.kzx.bean.TaskCanRelateBean;

/**
 * 单例
 * 
 * @title com.android.yijiang.kzx.ui
 * @date 2014年6月12日
 * @author tanke
 */
public class ContentFragmentActivity extends FragmentActivity {

	private FragmentManager mFragmentManager = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.content_single_frame);

		AppManager.getAppManager().addActivity(this);

		if ("partner_info".equals(getIntent().getStringExtra("action"))) {
			PartnerBean partnerBean = (PartnerBean) getIntent().getSerializableExtra("partnerBean");
			addFragmentContent(KzxPartnerBatchFragment.newInstance(partnerBean));
		} else if ("get_pwd".equals(getIntent().getStringExtra("action"))) {
			addFragmentContent(KzxGetPwdFragment.newInstance());
		} else if ("register".equals(getIntent().getStringExtra("action"))) {
			String phone=getIntent().getStringExtra("phone");
			addFragmentContent(KzxRegisterFragment.newInstance(phone));
		} else if ("create_team".equals(getIntent().getStringExtra("action"))) {
			addFragmentContent(KzxCreateTeamFragment.newInstance());
		} else if ("strategic_info".equals(getIntent().getStringExtra("action"))) {
			boolean notDoDecrypt = getIntent().getBooleanExtra("notDoDecrypt", false);
			TargetMemberBean targetMemberBean = (TargetMemberBean) getIntent().getSerializableExtra("targetMemberBean");
			String targetId = getIntent().getStringExtra("targetId");
			addFragmentContent(KzxStrategicInfoFragment.newInstance(targetMemberBean, targetId, notDoDecrypt));
		} else if ("cycle_done".equals(getIntent().getStringExtra("action"))) {
			addFragmentContent(KzxCycleDoneFragment.newInstance());
		} else if ("message".equals(getIntent().getStringExtra("action"))) {
			boolean isnew = getIntent().getBooleanExtra("isnew", false);
			addFragmentContent(KzxMessageFragment.newInstance(isnew));
		} else if ("message_info".equals(getIntent().getStringExtra("action"))) {
			MessageBean messageBean = (MessageBean) getIntent().getSerializableExtra("messageBean");
			int position = getIntent().getIntExtra("position", 0);
			addFragmentContent(KzxMessageAmountFragment.newInstance(messageBean, position));
		} else if ("member_info".equals(getIntent().getStringExtra("action"))) {
			boolean notDoDecrypt = getIntent().getBooleanExtra("notDoDecrypt", false);
			String memberId = getIntent().getStringExtra("memberId");
			addFragmentContent(KzxMemberInfoFragment.newInstance(memberId, notDoDecrypt));
		} else if ("account_info".equals(getIntent().getStringExtra("action"))) {
			addFragmentContent(KzxAccountInfoFragment.newInstance());
		} else if ("add_medal".equals(getIntent().getStringExtra("action"))) {
			MedalBean medalBean = (MedalBean) getIntent().getSerializableExtra("medalBean");
			int position = getIntent().getIntExtra("position", 0);
			addFragmentContent(KzxAddIncentiveFragment.newInstance(medalBean, position));
		} else if ("copy_selected".equals(getIntent().getStringExtra("action"))) {
			// 抄送人
			HashMap<String, LeaderBean> isLeaderHash = (HashMap<String, LeaderBean>) getIntent().getSerializableExtra("data");// 选中抄送人
			addFragmentContent(KzxCopySelectedFragment.newInstance(isLeaderHash));
		} else if ("report_selected".equals(getIntent().getStringExtra("action"))) {
			// 汇报人
			HashMap<String, LeaderBean> isLeaderHash = (HashMap<String, LeaderBean>) getIntent().getSerializableExtra("data");// 选中抄送人
			addFragmentContent(KzxExecutorSelectedFragment.newInstance(isLeaderHash));
		} else if ("execution_selected".equals(getIntent().getStringExtra("action"))) {
			// 执行人
			HashMap<String, LeaderBean> isLeaderHash = (HashMap<String, LeaderBean>) getIntent().getSerializableExtra("data");// 选中执行人
			String typeStr = getIntent().getStringExtra("typeStr");
			addFragmentContent(KzxExecutionSelectedFragment.newInstance(isLeaderHash, typeStr));
		} else if ("medal_person_selected".equals(getIntent().getStringExtra("action"))) {
			// 执行人
			HashMap<String, LeaderBean> isLeaderHash = (HashMap<String, LeaderBean>) getIntent().getSerializableExtra("data");// 选中执行人
			String typeStr = getIntent().getStringExtra("typeStr");
			addFragmentContent(KzxMedalPersonSelectedFragment.newInstance(isLeaderHash, typeStr));
		} else if ("target_selected".equals(getIntent().getStringExtra("action"))) {
			// 相关目标
			HashMap<String, TargetCanRelateBean> isTargetHash = (HashMap<String, TargetCanRelateBean>) getIntent().getSerializableExtra("data");// 相关目标
			addFragmentContent(KzxTargetSelectedFragment.newInstance(isTargetHash));
		} else if ("task_selected".equals(getIntent().getStringExtra("action"))) {
			// 相关任务
			HashMap<String, TaskCanRelateBean> isTaskHash = (HashMap<String, TaskCanRelateBean>) getIntent().getSerializableExtra("data");// 选中抄送人
			addFragmentContent(KzxTaskSelectedFragment.newInstance(isTaskHash));
		} else if ("client_selected".equals(getIntent().getStringExtra("action"))) {
			// 相关客户
			HashMap<String, ClientBean> isTaskHash = (HashMap<String, ClientBean>) getIntent().getSerializableExtra("data");// 选中抄送人
			addFragmentContent(KzxClientResultBatchFragment.newInstance(isTaskHash, null));
		} else if ("task_detail".equals(getIntent().getStringExtra("action"))) {
			boolean notDoDecrypt = getIntent().getBooleanExtra("notDoDecrypt", false);
			String taskId = getIntent().getStringExtra("taskId");
			String type = getIntent().getStringExtra("type");
			addFragmentContent(KzxTaskDetailInfoFragment.newInstance(taskId, notDoDecrypt, type));
		} else if ("invite".equals(getIntent().getStringExtra("action"))) {
			addFragmentContent(KzxInviteFragment.newInstance());
		} else if ("task_process".equals(getIntent().getStringExtra("action"))) {
			TaskDetailBean taskBean = (TaskDetailBean) getIntent().getSerializableExtra("taskBean");
			addFragmentContent(KzxTaskProcessFragment.newInstance(taskBean));
		} else if ("add_task".equals(getIntent().getStringExtra("action"))) {
			TargetMemberBean targetMemberBean = (TargetMemberBean) getIntent().getSerializableExtra("targetMemberBean");
			LeaderBean leaderBean = (LeaderBean) getIntent().getSerializableExtra("leaderBean");
			addFragmentContent(KzxAddTaskFragment.newInstance(targetMemberBean, leaderBean));
		} else if ("modify_task".equals(getIntent().getStringExtra("action"))) {
			String detailStr = getIntent().getStringExtra("detailStr");
			String actionStr = getIntent().getStringExtra("actionStr");
			TaskDetailBean taskBean = (TaskDetailBean) getIntent().getSerializableExtra("taskBean");
			addFragmentContent(KzxTaskModifyFragment.newInstance(taskBean, detailStr, actionStr));
		} else if ("notice_info".equals(getIntent().getStringExtra("action"))) {
			NoticeBean noticeBean = (NoticeBean) getIntent().getSerializableExtra("noticeBean");
			String noticeId = getIntent().getStringExtra("noticeId");
			boolean notDoDecrypt = getIntent().getBooleanExtra("notDoDecrypt", false);
			addFragmentContent(KzxNoticeInfoFragment.newInstance(noticeBean, noticeId, notDoDecrypt));
		} else if ("member_medal".equals(getIntent().getStringExtra("action"))) {
			AmountMemberBean amountMemberBean = (AmountMemberBean) getIntent().getSerializableExtra("amountMemberBean");
			addFragmentContent(KzxMemberMedalFragment.newInstance(amountMemberBean));
		} else if ("servicer_info".equals(getIntent().getStringExtra("action"))) {
			ServicerBean servicerBean = (ServicerBean) getIntent().getSerializableExtra("servicerBean");
			addFragmentContent(KzxServicerBenchResultFragment.newInstance(servicerBean));
		} else if ("member_leader".equals(getIntent().getStringExtra("action"))) {
			AmountMemberBean amountMemberBean = (AmountMemberBean) getIntent().getSerializableExtra("amountMemberBean");
			addFragmentContent(KzxMemberLeaderFragment.newInstance(amountMemberBean));
		} else if ("member_client".equals(getIntent().getStringExtra("action"))) {
			AmountMemberBean amountMemberBean = (AmountMemberBean) getIntent().getSerializableExtra("amountMemberBean");
			addFragmentContent(KzxMemberClientFragment.newInstance(amountMemberBean));
		}
	}

	// 添加新的视图
	private void addFragmentContent(Fragment fragment) {
		if (null == mFragmentManager) {
			mFragmentManager = getSupportFragmentManager();
		}
		FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.container, fragment);
		fragmentTransaction.addToBackStack(null);
		fragmentTransaction.commit();
	}

	// 移除视图
	private void removeFragment() {
		if (null == mFragmentManager) {
			mFragmentManager = getSupportFragmentManager();
		}
		mFragmentManager.popBackStack();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Fragment fragment = (Fragment) mFragmentManager.findFragmentById(R.id.container);
		if (fragment != null) {
			fragment.onActivityResult(requestCode, resultCode, data);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		JPushInterface.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		JPushInterface.onPause(this);
	}

	@Override
	public void onBackPressed() {
		if (mFragmentManager != null && mFragmentManager.getBackStackEntryCount() > 1) {
			removeFragment();
			return;
		}
		Fragment mFragment = mFragmentManager.findFragmentById(R.id.container);
		if (mFragment instanceof KzxMessageAmountFragment) {
			AppManager.getAppManager().finishActivity(this);
		} else if (mFragment instanceof KzxCopySelectedFragment) {
			AppManager.getAppManager().finishActivity(this);
		} else if (mFragment instanceof KzxExecutorSelectedFragment) {
			AppManager.getAppManager().finishActivity(this);
		} else if (mFragment instanceof KzxExecutionSelectedFragment) {
			AppManager.getAppManager().finishActivity(this);
		} else if (mFragment instanceof KzxTargetSelectedFragment) {
			AppManager.getAppManager().finishActivity(this);
		} else if (mFragment instanceof KzxTaskSelectedFragment) {
			AppManager.getAppManager().finishActivity(this);
		} else if (mFragment instanceof KzxClientResultBatchFragment) {
			AppManager.getAppManager().finishActivity(this);
		} else if (mFragment instanceof KzxInviteFragment) {
			AppManager.getAppManager().finishActivity(this);
		} else if (mFragment instanceof KzxTaskProcessFragment) {
			AppManager.getAppManager().finishActivity(this);
		} else if (mFragment instanceof KzxTaskModifyFragment) {
			AppManager.getAppManager().finishActivity(this);
		} else if (mFragment instanceof KzxMemberMedalFragment) {
			AppManager.getAppManager().finishActivity(this);
		} else if (mFragment instanceof KzxMemberClientFragment) {
			AppManager.getAppManager().finishActivity(this);
		} else if (mFragment instanceof KzxMemberLeaderFragment) {
			AppManager.getAppManager().finishActivity(this);
		} else if (mFragment instanceof KzxCycleDoneFragment) {
			AppManager.getAppManager().finishActivity(this);
		} else if (mFragment instanceof KzxServicerBenchResultFragment) {
			AppManager.getAppManager().finishActivity(this);
		} else if (mFragment instanceof KzxRegisterFragment) {
			AppManager.getAppManager().finishActivity(this);
		} else if (mFragment instanceof KzxGetPwdFragment) {
			AppManager.getAppManager().finishActivity(this);
		} else if (mFragment instanceof KzxNoticeInfoFragment) {
			AppManager.getAppManager().finishActivity(this);
		} else if(!StringUtils.isEmpty(getIntent().getStringExtra("type"))){
			AppManager.getAppManager().finishActivity(this);
		}else {
			AppManager.getAppManager().finishActivity(this);
			Intent i = new Intent(this, MainActivity.class);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(i);
		}
	}
}
