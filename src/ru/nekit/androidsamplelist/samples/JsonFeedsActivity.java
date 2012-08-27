package ru.nekit.androidsamplelist.samples;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import ru.nekit.androidsamplelist.R;
import ru.nekit.androidsamplelist.activityExtra.GoUpActivity;
import ru.nekit.androidsamplelist.activityExtra.WebActivity;
import ru.nekit.androidsamplelist.listAdapter.JsonFeedsListAdapter;
import ru.nekit.androidsamplelist.model.vo.ActionItemVO;
import ru.nekit.androidsamplelist.model.vo.FeedSimpleItemVO;
import ru.nekit.androidsamplelist.utils.Translit;
import ru.nekit.androidsamplelist.utils.WidgetUtils;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.view.Window;

public class JsonFeedsActivity extends GoUpActivity implements OnItemClickListener, OnInitListener {

	private static final int SPEECH = 1;
	private LoadJson task;
	private TextToSpeech tts;
	private List<FeedSimpleItemVO> dataSource;
	private ListView list;
	private JsonFeedsListAdapter adapter;
	private FeedSimpleItemVO currentItem;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_PROGRESS);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_json_feeds);
		list = (ListView)findViewById(android.R.id.list);
		dataSource = new ArrayList<FeedSimpleItemVO>();
		adapter = new JsonFeedsListAdapter(this, dataSource);
		list.setAdapter(adapter);
		task = (LoadJson) getLastNonConfigurationInstance();
		if( task == null )
		{
			task = new LoadJson();
			task.execute();
		}
		list.setOnItemClickListener(this);
	}

	public Object onRetainNonConfigurationInstance() 
	{
		return task;
	}

	private class TaskCancelException extends Exception
	{

		private static final long serialVersionUID = 1L;

	}

	private class LoadJson extends AsyncTask<Void, FeedSimpleItemVO, Integer>
	{

		static final String FEED = "http://www.imaladec.net/phpExamples/news.php";

		@Override
		protected void onPreExecute() 
		{
			super.onPreExecute();
			setSupportProgressBarIndeterminateVisibility(true);
			adapter.clear();
		}

		private void watchCancel() throws TaskCancelException
		{
			if( isCancelled() )
			{
				throw new TaskCancelException();
			}
		}

		@Override
		protected Integer doInBackground(Void... params) 
		{
			StringBuilder builder = new StringBuilder();
			HttpClient client = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(FEED);
			HttpResponse response;
			try{
				try {
					response = client.execute(httpGet);
					StatusLine statusLine = response.getStatusLine();
					int statusCode = statusLine.getStatusCode();
					if (statusCode == HttpURLConnection.HTTP_OK ) 
					{
						HttpEntity entity = response.getEntity();
						InputStream content = entity.getContent();
						BufferedReader reader = new BufferedReader(new InputStreamReader(content));
						String line;
						while ((line = reader.readLine()) != null) {
							builder.append(line);
							watchCancel();
						}
					}
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				String jsonString = builder.toString();
				JSONArray jsonArray = null;
				int length = 0;
				try {
					jsonArray = new JSONArray(jsonString);
					length = jsonArray.length();
					SimpleDateFormat  format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					for (int i = 0 ;i< length; i++ ) 
					{
						watchCancel();
						JSONObject jsonObject = null;
						try {
							jsonObject = jsonArray.getJSONObject(i);
							FeedSimpleItemVO feedItem = new FeedSimpleItemVO(jsonObject.getString("name"), 
									format.parse(jsonObject.getString("storydate")), 
									new URL(jsonObject.getString("image")), 
									new URL(jsonObject.getString("url")));
							publishProgress(feedItem);	
						} catch (MalformedURLException e) {
							e.printStackTrace();
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				return length;
			}catch( TaskCancelException exception )
			{
				Toast.makeText( JsonFeedsActivity.this, "Canceled", Toast.LENGTH_SHORT).show();
				return 0;
			}
		}

		@Override
		protected void onProgressUpdate(FeedSimpleItemVO... values) 
		{
			FeedSimpleItemVO feedItem = values[0];
			dataSource.add(feedItem);
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(Integer result) 
		{
			setSupportProgressBarIndeterminateVisibility(false);
			adapter.notifyDataSetChanged();
			Toast.makeText( JsonFeedsActivity.this, "Count: "+ result, Toast.LENGTH_SHORT).show();
			super.onPostExecute(result);
		}
	}

	private static final ActionItemVO[] actionListData = new ActionItemVO[]{ 
		new ActionItemVO("Open in browser", R.drawable.ic_web), 
		new ActionItemVO("Speech", R.drawable.ic_volume)
	};

	@Override
	public void onItemClick(AdapterView<?> adapter, View view, final int index, long i)
	{
		currentItem = this.adapter.getItem(index);
		WidgetUtils.showSelectAccountTypeDialog(this, "Choose an action", actionListData, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if( which == 0 )
				{
					dialog.dismiss();
					Intent intent = new Intent(JsonFeedsActivity.this, WebActivity.class);
					intent.putExtra(WebActivity.URL_DATA, currentItem.url.toString());
					startActivity(intent);
				}else if( which == 1 )
				{
					if( tts == null )
					{
						Intent checkTTSIntent = new Intent(); 
						checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA); 
						startActivityForResult(checkTTSIntent, SPEECH); 
					}else{
						speech();
					}
				}
			}
		} );
	}

	private void speech()
	{
		String text = Translit.translit(currentItem.name);
		tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
	}

	@Override
	public void onStop()
	{
		if (tts != null)
		{
			tts.stop();
			tts.shutdown();
		}
		if( task != null )
		{
			task.cancel(true);
		}
		super.onStop();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(  requestCode == SPEECH )
		{
			if( resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS )
			{
				tts = new TextToSpeech(this, this); 
			}else
			{ 
				Intent installTTSIntent = new Intent(); 
				installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA); 
				startActivity(installTTSIntent); 
			} 
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override

	public void onInit(int initStatus) {
		if (initStatus == TextToSpeech.SUCCESS)  
		{
			tts.setLanguage(Locale.US);
			speech();
		}else if (initStatus == TextToSpeech.LANG_MISSING_DATA || initStatus == TextToSpeech.LANG_NOT_SUPPORTED) {
			Log.e("TTS", "This Language is not supported");
		} 
	}
}