package ru.nekit.androidsamplelist.samples;

import static android.app.SearchManager.QUERY;

import java.io.IOException;

import ru.nekit.androidsamplelist.R;
import ru.nekit.androidsamplelist.activityExtra.GoUpActivity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.github.kevinsawicki.http.HttpRequest;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

public class ImageIOActivity extends GoUpActivity 
{

	private ListView listView;
	private ListAdapter adapter;
	private LoadTask task;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		getSupportMenuInflater().inflate(R.menu.search, menu);
		//		SubMenu subMenu = menu.addSubMenu(0, -1, 0, null);
		//		subMenu.setIcon(android.R.drawable.ic_menu_revert);
		//		MenuItem head = subMenu.add(0, 0, 0, "");
		//	
		//		head.setActionView(R.layout.menu_item);
		//		MenuItem subMenuItem = subMenu.getItem();
		//		subMenuItem.setTitle("Count");
		//		subMenuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		//	MenuItem menuItemCreateCart  = menu.add(0, 1, 0, "!!!");
		//    
		//
		//    TextView tv = new TextView(this);
		//    tv.setText("!!!");
		//    tv.setTextColor(0xff8800);
		//    tv.setBackgroundColor(0xff0000);
		//    menuItemCreateCart.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		//    menuItemCreateCart.setActionView(tv);
		return true;
	}


	@Override
	protected void onStop() {
		System.gc();
		
		super.onStop();
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.m_search:

			onSearchRequested();

			return true;
		case R.id.m_clear:

			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		requestWindowFeature(Window.FEATURE_PROGRESS);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState);
		setSupportProgressBarIndeterminateVisibility(false);
		setContentView(R.layout.activity_image_io);
		adapter = new ListAdapter(this);
		listView = (ListView)findViewById(android.R.id.list);
		listView.setAdapter(adapter);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		setIntent(intent);
		handleIntent(intent);
	}

	private void handleIntent(Intent intent) {
		if (Intent.ACTION_SEARCH.equals(intent.getAction()))
		{
			search(intent.getStringExtra(QUERY));
		}
	}

	private void search(final String query) {
		getSupportActionBar().setSubtitle("Search for: \"" + query + "\"");
		if (task != null ) {
			task.cancel(true);
			task = null;
			System.gc();
		}
		task = new LoadTask();
		task.execute(query);
	}

	class ListAdapter extends ArrayAdapter<String> {

		public ListAdapter(Context context) {
			super(context, 0);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView iv;
			if (convertView == null)
				convertView = iv = new ImageView(ImageIOActivity.this);
			else
				iv = (ImageView)convertView;
			UrlImageViewHelper.setUrlDrawable(iv, getItem(position), android.R.drawable.ic_menu_gallery, null);
			return iv;
		}
	}

	class LoadTask extends AsyncTask<String, String, Void>
	{

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			setSupportProgressBarIndeterminateVisibility(true);
			adapter.clear();
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			setSupportProgressBarIndeterminateVisibility(false);
		}

		@Override
		protected void onProgressUpdate(String... values) {
			adapter.add(values[0]);
			super.onProgressUpdate(values);
		}

		@Override
		protected Void doInBackground(String... params) 
		{
			final String query = params[0];
			for( int i = 0; i < 2; i++ )
			{
				if( isCancelled() )
				{
					return null;
				}
				String body = HttpRequest.get("https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=" + query + "&start="+ (i * 8) +"&rsz=" + 8).body();
				//JSONObject json = null;
				//JSONArray results = null;
				//final ArrayList<String> urls = new ArrayList<String>();
				JsonFactory factory = new JsonFactory();
				JsonParser jsonParser = null;
				try {
					jsonParser = factory.createJsonParser(body);
					jsonParser.nextToken();
					while ( jsonParser.nextToken() != JsonToken.END_OBJECT) {
						jsonParser.nextToken();
						String fieldname = jsonParser.getCurrentName();
						if ("results".equals(fieldname)) { 
							while ( jsonParser.nextToken() != JsonToken.END_ARRAY ) {
								fieldname = jsonParser.getCurrentName();
								while ( jsonParser.nextToken() != JsonToken.END_OBJECT ) {
									jsonParser.nextToken();
									fieldname = jsonParser.getCurrentName();
									if( "url".equals(fieldname) )
									{
										publishProgress(jsonParser.getText());	
										while ( jsonParser.nextToken()!= JsonToken.END_OBJECT);
										break;
									}
								}

							}
							break;
						}
					}   

				} catch (JsonParseException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

				//				try {
				//					json = new JSONObject(body);
				//					results = json.getJSONObject("responseData").getJSONArray("results");
				//					for (int j = 0; j < results.length(); j++) {	
				//						JSONObject result = null;
				//						try {
				//							result = results.getJSONObject(j);
				//							String url = result.getString("url");
				//							urls.add(url);
				//							publishProgress(url);	
				//							if( isCancelled() )
				//							{
				//								return null;
				//							}
				//						} catch (JSONException e) {
				//							e.printStackTrace();
				//						}
				//					}
				//				} catch (JSONException e) {
				//					e.printStackTrace();
				//				}
			}
			return null;
		}
	}
}