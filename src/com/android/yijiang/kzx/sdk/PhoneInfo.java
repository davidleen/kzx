package com.android.yijiang.kzx.sdk;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * 读取手机设备信息测试代码
 * 
 * @title com.talented.utils
 * @date 2013-8-16
 * @author tanke
 */
public class PhoneInfo {

	private TelephonyManager telephonyManager;
	/**
	 * 国际移动用户识别码
	 */
	private String IMSI;
	private Context cxt;

	public PhoneInfo(Context context) {
		cxt = context;
		telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
	}

	/**
	 * 获取电话号码
	 */
	public String getNativePhoneNumber() {
		String NativePhoneNumber = null;
		NativePhoneNumber = telephonyManager.getLine1Number();
		return NativePhoneNumber;
	}
	
	/**
	 * 获取手机型号
	 */
	public String getPhoneModel(){
		return android.os.Build.MODEL;
	}
	
	/**
	 * 获取手机IMEI
	 */
	public String getPhoneDeviceId(){
		return telephonyManager.getDeviceId();
	}
	
	/**
	 * 获取手机厂商
	 */
	public String getPhoneActurer(){
		return android.os.Build.MANUFACTURER;
	}
	
	/**
	 * 获取手机系统SDK版本号
	 */
	public String getPhoneSDK(){
		return android.os.Build.VERSION.SDK;
	}
	
	/**
	 * 获取手机系统Firmware/OS 版本号
	 */
	public String getPhoneRelease(){
		return android.os.Build.VERSION.RELEASE;
	}

	/**
	 * 获取手机服务商信息
	 */
	public String getProvidersName() {
		String ProvidersName = "N/A";
		try {
			IMSI = telephonyManager.getSubscriberId();
			// IMSI号前面3位460是国家，紧接着后面2位00 02是中国移动，01是中国联通，03是中国电信。
			System.out.println(IMSI);
			if (IMSI.startsWith("46000") || IMSI.startsWith("46002")) {
				ProvidersName = "中国移动";
			} else if (IMSI.startsWith("46001")) {
				ProvidersName = "中国联通";
			} else if (IMSI.startsWith("46003")) {
				ProvidersName = "中国电信";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ProvidersName;
	}
	
	/**
	 * 组装需要传递的客户端信息
	 * @param 手机型号
	 * @param 手机厂商
	 * @param 本机号码
	 * @param 版本号
	 * @param 设备ID
	 */
	public String getClientInfo(){
		StringBuilder sb = new StringBuilder();
		sb.append(getPhoneModel());
		sb.append("||");
		sb.append(getPhoneActurer());
		sb.append("||");
		sb.append(getNativePhoneNumber());
		sb.append("||");
		sb.append(getPhoneRelease());
		sb.append("||");
		sb.append(getPhoneDeviceId());
		return sb.toString();
	}

	public String getPhoneInfo() {
		TelephonyManager tm = (TelephonyManager) cxt.getSystemService(Context.TELEPHONY_SERVICE);
		StringBuilder sb = new StringBuilder();

		sb.append("\nDeviceId(IMEI) = " + tm.getDeviceId());
		sb.append("\nDeviceSoftwareVersion = " + tm.getDeviceSoftwareVersion());
		sb.append("\nLine1Number = " + tm.getLine1Number());
		sb.append("\nNetworkCountryIso = " + tm.getNetworkCountryIso());
		sb.append("\nNetworkOperator = " + tm.getNetworkOperator());
		sb.append("\nNetworkOperatorName = " + tm.getNetworkOperatorName());
		sb.append("\nNetworkType = " + tm.getNetworkType());
		sb.append("\nPhoneType = " + tm.getPhoneType());
		sb.append("\nSimCountryIso = " + tm.getSimCountryIso());
		sb.append("\nSimOperator = " + tm.getSimOperator());
		sb.append("\nSimOperatorName = " + tm.getSimOperatorName());
		sb.append("\nSimSerialNumber = " + tm.getSimSerialNumber());
		sb.append("\nSimState = " + tm.getSimState());
		sb.append("\nSubscriberId(IMSI) = " + tm.getSubscriberId());
		sb.append("\nVoiceMailNumber = " + tm.getVoiceMailNumber());
		return sb.toString();
	}
}