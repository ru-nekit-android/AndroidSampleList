package ru.nekit.androidsamplelist.samples;

import static android.app.SearchManager.QUERY;

import java.io.IOException;

import ru.nekit.androidsamplelist.R;
import ru.nekit.androidsamplelist.activityExtra.GoUpActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.github.kevinsawicki.http.HttpRequest;
import com.github.kevinsawicki.http.HttpRequest.HttpRequestException;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

public class ImageIOActivity extends GoUpActivity 
{

	private DisplayImageOptions options;
	private ListView listView;
	private ListAdapter adapter;
	private LoadTask task;
	private ImageLoader imageLoader;

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
		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
		.showStubImage(android.R.drawable.ic_input_add)
		.cacheInMemory()
		.imageScaleType(ImageScaleType.POWER_OF_2)
		.resetViewBeforeLoading()
		.showImageForEmptyUri(android.R.drawable.ic_menu_rotate)
		.cacheOnDisc()
		.build();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		getSupportMenuInflater().inflate(R.menu.search, menu);
		return true;
	}

	@Override
	protected void onStop()
	{
		imageLoader.stop();
		System.gc();
		super.onStop();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.m_search:

			onSearchRequested();

			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	protected void onNewIntent(Intent intent) 
	{
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

		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView imageView;
			if (convertView == null)
			{
				convertView = imageView = new ImageView(ImageIOActivity.this);
				imageView.setScaleType(ScaleType.CENTER_INSIDE);
				imageView.setAdjustViewBounds(true);
			}else
			{
				imageView = (ImageView)convertView;
			}
			imageLoader.displayImage(getItem(position), imageView, options, new SimpleImageLoadingListener() {
				@Override
				public void onLoadingComplete(Bitmap loadedImage) {

				}
				@Override
				public void onLoadingFailed(FailReason failReason) {
					if( failReason == FailReason.OUT_OF_MEMORY )
					{
						System.gc();
					}
				}
			});
			return convertView;
		}
	}

	class LoadTask extends AsyncTask<String, String, Void>
	{

		public static final int RESULT_LENGTH = 8; 

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
				String body = null;
				try{
					body = HttpRequest.get("https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=" + query + "&start="+ (i * RESULT_LENGTH) +"&rsz=" + RESULT_LENGTH).body();
				}catch(HttpRequestException exeption)
				{
					i--;
					continue;
				}
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
			}
			return null;
		}
	}
}