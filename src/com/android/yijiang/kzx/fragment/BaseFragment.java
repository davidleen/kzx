package com.android.yijiang.kzx.fragment;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import org.apache.http.client.HttpResponseException;

import com.android.yijiang.kzx.R;
import com.android.yijiang.kzx.ResourceMap;
import com.android.yijiang.kzx.widget.MsgTools;

import android.support.v4.app.Fragment;

/**
 * 存放一些公用的方法 
 * @author lintanke
 */
public class BaseFragment extends Fragment{

	/**数据请求失败回调*/
	void onFailureToast(Throwable error){
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
}
