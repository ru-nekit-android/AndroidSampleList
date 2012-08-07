package ru.nekit.androidsamplelist.activityExtra;

import ru.nekit.androidsamplelist.R;
import ru.nekit.androidsamplelist.R.id;
import ru.nekit.androidsamplelist.R.layout;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebActivity extends GoUpActivity {

	public static final String URL_DATA = "url";
	private WebView webView;

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web);
		webView = (WebView)findViewById(R.id.webview);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.setWebViewClient(new mWebViewClient());
		Intent intent = getIntent();
		String url = intent.getStringExtra(URL_DATA);
		webView.loadUrl(url); 
		WebSettings ws = webView.getSettings();
		ws.setBuiltInZoomControls(true);
		ws.setDefaultTextEncodingName("utf-8");
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
		if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) 
		{
			webView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	private class mWebViewClient extends WebViewClient
	{
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) 
		{
			view.loadUrl(url);
			return true;
		}
	}
}