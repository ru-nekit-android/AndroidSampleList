package ru.nekit.androidsamplelist.samples;

import java.io.IOException;
import java.io.InputStream;

import org.taptwo.android.widget.TitleFlowIndicator;
import org.taptwo.android.widget.TitleProvider;
import org.taptwo.android.widget.ViewFlow;


import ru.nekit.androidsamplelist.R;
import ru.nekit.androidsamplelist.activityExtra.GoUpActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PhoneInfoActivity extends GoUpActivity 
{

	private static StringBuffer buffer;
	private TelephonyManager telephonyManager;
	private ViewFlow viewFlow;

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		telephonyManager = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
		setContentView(R.layout.activity_phone_info);
		viewFlow = (ViewFlow) findViewById(R.id.viewFlow);
		PhoneInfoAdapter adapter = new PhoneInfoAdapter();
		viewFlow.setAdapter(adapter, 0);
		TitleFlowIndicator indicator = (TitleFlowIndicator) findViewById(R.id.viewFlowIndicator);
		indicator.setTitleProvider(adapter);
		viewFlow.setFlowIndicator(indicator);
		Intent intent = getIntent();
		if( intent.getCategories() != null && intent.getCategories().contains("ru.nekit.category.ui") )
		{
			getSupportActionBar().setTitle(R.string.title_activity_phone_info_ui);
			getSupportActionBar().setSubtitle(R.string.description_activity_phone_info_ui);
		}
	}

	private String getSysInfo() 
	{
		buffer = new StringBuffer();
		getProperty("os.name", "os.name", buffer);
		getProperty("os.version", "os.version", buffer);
		getProperty("java.vendor.url", "java.vendor.url", buffer);
		getProperty("java.version", "java.version", buffer);
		getProperty("java.class.path", "java.class.path", buffer);
		getProperty("java.class.version", "java.class.version", buffer);
		getProperty("java.vendor", "java.vendor", buffer);
		getProperty("java.home", "java.home", buffer);
		getProperty("user.name", "user.name", buffer);
		getProperty("user.home", "user.home", buffer);
		getProperty("user.dir", "user.dir", buffer);
		return buffer.toString();
	}

	private void getProperty(String desc, String property, StringBuffer tBuffer) 
	{
		tBuffer.append(desc);
		tBuffer.append(" : ");
		tBuffer.append(System.getProperty(property));
		tBuffer.append("\n");
	}

	private String getOperatorName() 
	{
		return telephonyManager.getNetworkOperatorName();
	}

	private String getDeviceID() 
	{
		return telephonyManager.getDeviceId();
	}

	private String getMyPhoneNumber() 
	{
		return telephonyManager.getLine1Number();
	}

	private String getSIMNumber() {
		return telephonyManager.getSimSerialNumber();
	}

	private String getUserID()
	{
		return telephonyManager.getSubscriberId();
	}

	private String getPhoneType() 
	{

		int phoneType = telephonyManager.getPhoneType(); 
		String strPhoneType = "NA";

		switch (phoneType) {
		case TelephonyManager.PHONE_TYPE_GSM:
			strPhoneType = "GSM";
			break;
		case TelephonyManager.PHONE_TYPE_NONE:
			strPhoneType = "NONE";
			break;
		}

		return strPhoneType;
	}

	private String getPhoneInfo() 
	{
		String result = "";
		result += "Operator: " + getOperatorName() + "\n";
		result += "Indification number: " + getDeviceID() + "\n";
		result += "Phone number: " + getMyPhoneNumber() + "\n";
		result += "Sim number: " + getSIMNumber() + "\n";
		result += "Subscriber code: " + getUserID() + "\n";
		result += "Phone type: " + getPhoneType();
		return result;
	}

	private String getScreenInfo()
	{
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);

		String result="";
		result += "Width : " + String.valueOf(dm.widthPixels) + " pixels" + "\n";
		result += "Height : " + String.valueOf(dm.heightPixels) + " pixels" + "\n";
		result += "The Logical Density : " + String.valueOf(dm.density) + "\n";
		result += "X Dimension : " + String.valueOf(dm.xdpi) + " dot/inch" + "\n";
		result += "Y Dimension : " + String.valueOf(dm.ydpi) + " dot/inch" + "\n";
		return result;
	}

	private String getCPUinfo() {
		ProcessBuilder cmd;
		String result = "";
		try {
			String[] args = { "/system/bin/cat", "/proc/cpuinfo" };
			cmd = new ProcessBuilder(args);
			Process process = cmd.start();
			InputStream in = process.getInputStream();
			byte[] re = new byte[1024];
			while (in.read(re) != -1) {
				System.out.println(new String(re));
				result = result + new String(re);
			}
			in.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return result;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) 
	{
		super.onConfigurationChanged(newConfig);
		viewFlow.onConfigurationChanged(newConfig);
	}

	class PhoneInfoAdapter extends BaseAdapter implements TitleProvider
	{

		private LayoutInflater inflater;

		public PhoneInfoAdapter()
		{
			Context contxt = getApplicationContext();
			inflater = (LayoutInflater)contxt.getSystemService(LAYOUT_INFLATER_SERVICE);
		}

		private final String[] names = {"Sys","Phone", "Screen", "CPU"};

		@Override
		public int getCount() 
		{
			return names.length;
		}

		@Override
		public Object getItem(int position) 
		{
			return position;
		}

		@Override
		public long getItemId(int position) 
		{
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView == null) {
				convertView = inflater.inflate(R.layout.phone_info_item, null);
			}
			String text = null;
			switch( position )
			{

			case 0:

				text = getSysInfo();

				break;

			case 1:

				text = getPhoneInfo();

				break;

			case 2:

				text = getScreenInfo();

				break;

			case 3:

				text = getCPUinfo();

				break;

			default:

				break;

			}

			((TextView) convertView.findViewById(R.id.textView)).setText(text);
			return convertView;
		}

		@Override
		public String getTitle(int position)
		{
			return names[position];
		}
	}
}