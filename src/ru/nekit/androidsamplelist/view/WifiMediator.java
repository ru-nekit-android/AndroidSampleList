package ru.nekit.androidsamplelist.view;

import org.puremvc.java.patterns.mediator.Mediator;

import ru.nekit.androidsamplelist.R;
import ru.nekit.androidsamplelist.samples.WifiActivity;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class WifiMediator extends Mediator implements OnClickListener {

	public static final String NAME = "wifiMediator";

	private static WifiManager wifiManager;

	private Activity activity;
	private Button wifiButton;
	private Button wifiSettingsButton;
	private TextView wifiStatusView;
	boolean enabled;

	boolean connected;

	BroadcastReceiver wifiStateChangedReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) 
		{
			final String action = intent.getAction();
			//answer http://stackoverflow.com/questions/5888502/android-wifi-how-to-detect-when-wifi-connection-has-been-established
			if( action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION) )
			{
				NetworkInfo info = (NetworkInfo)intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
				if (info.getState().equals(NetworkInfo.State.CONNECTED))
				{
					connected = true;
					updateStatus(null);
				}
			}else if( action.equals(ConnectivityManager.CONNECTIVITY_ACTION) ){
				final ConnectivityManager connMgr = (ConnectivityManager) 
						activity.getSystemService(Context.CONNECTIVITY_SERVICE);

				final android.net.NetworkInfo wifi = 
						connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

				final android.net.NetworkInfo mobile = 
						connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);


				if( wifi.isAvailable() ){
					enabled = true;
				}
				if( mobile.isAvailable() ){
					//Do something else.
				}

			}else if (action.equals(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION)) {
				if (intent.getBooleanExtra(WifiManager.EXTRA_SUPPLICANT_CONNECTED, false)) {
					//connected = true;
				} else {
					connected = false;
				}
				updateStatus(null);
			}else{
				int extraWifiState = intent.getIntExtra(
						WifiManager.EXTRA_WIFI_STATE,
						WifiManager.WIFI_STATE_UNKNOWN);

				switch (extraWifiState) {

				case WifiManager.WIFI_STATE_DISABLED:
					updateStatus("Wifi disabled");
					break;

				case WifiManager.WIFI_STATE_DISABLING:
					enabled = false;
					updateStatus("Wifi disabling...");
					break;

				case WifiManager.WIFI_STATE_ENABLED:
					enabled = true;
					updateStatus("Wifi enabled");
					break;

				case WifiManager.WIFI_STATE_ENABLING:
					updateStatus("Wifi enabling...");
					break;

				case WifiManager.WIFI_STATE_UNKNOWN:
					updateStatus("Wifi unknown state");
					break;
				}
			}
		}
	};

	private void updateStatus(String status) {
		int ip = wifiManager.getConnectionInfo().getIpAddress();
		String ssid = wifiManager.getConnectionInfo().getSSID();
		String ipString = String.format("%d.%d.%d.%d", (ip & 0xff), (ip >> 8 & 0xff), (ip >> 16 & 0xff), (ip >> 24 & 0xff));
		wifiStatusView.setText( "Status: " + (status == null ? (enabled  ? "Wifi enabled\n" : "Wifi disabled\n") : status ) 
				+ ( connected ? "SSID: " + ssid + "\nIP: " + ipString : enabled ? "Getting ssid" : "\nNot connected to wifi"));
		wifiButton.setText( enabled ? "Off wifi" : "On wifi" );
	}

	public WifiMediator(WifiActivity activity) {
		super(NAME, activity);
		this.activity = activity;
		wifiManager = (WifiManager)activity.getSystemService(Context.WIFI_SERVICE);
	}

	@Override
	public void onRegister() 
	{
		activity.setContentView(R.layout.activity_wifi);
		wifiButton = (Button)activity.findViewById(R.id.wifi);
		wifiButton.setOnClickListener(this);
		wifiSettingsButton = (Button)activity.findViewById(R.id.button_wifi_settings);
		wifiSettingsButton.setOnClickListener(this);
		wifiStatusView = (TextView)activity.findViewById(R.id.statusView);
		enabled = wifiManager.isWifiEnabled();
		updateStatus(null);
		activity.registerReceiver(wifiStateChangedReceiver, new IntentFilter(
				WifiManager.WIFI_STATE_CHANGED_ACTION));
		activity.registerReceiver(wifiStateChangedReceiver, new IntentFilter(
				WifiManager.NETWORK_STATE_CHANGED_ACTION));
		activity.registerReceiver(wifiStateChangedReceiver, new IntentFilter(
				WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION));
		activity.registerReceiver(wifiStateChangedReceiver, new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION));
	}

	@Override
	public void onRemove() {
		activity.unregisterReceiver(wifiStateChangedReceiver);
		super.onRemove();
	}

	@Override
	public void onClick(View v)
	{
		if( v.getId() == R.id.wifi )
		{
			wifiManager.setWifiEnabled(!enabled);
		}else if( v.getId() == R.id.button_wifi_settings )
		{
			//Intent intent = new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK);//Settings.ACTION_WIFI_SETTINGS);
			//activity.startActivity(intent);
			Intent intent = new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK);       
			intent.putExtra("only_access_points", true);
			intent.putExtra("extra_prefs_show_button_bar", true);
			intent.putExtra("wifi_enable_next_on_connect", true);
			activity.startActivityForResult(intent, 1);
		}
	}
}