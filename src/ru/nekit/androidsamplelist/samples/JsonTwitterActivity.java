package ru.nekit.androidsamplelist.samples;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

import ru.nekit.androidsamplelist.R;
import ru.nekit.androidsamplelist.activityExtra.GoUpActivity;
import ru.nekit.androidsamplelist.listAdapter.JsonTwitterListAdapter;
import ru.nekit.androidsamplelist.model.vo.ActionItemVO;
import ru.nekit.androidsamplelist.model.vo.TwitterItemVO;
import ru.nekit.androidsamplelist.utils.WidgetUtils;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech.EngineInfo;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.searchview.OnQueryTextListener;
import com.actionbarsherlock.widget.searchview.SearchView;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.tts.TextToSpeechBeta;
import com.google.tts.TextToSpeechBeta.OnInitListener;

@SuppressLint("NewApi")
public class JsonTwitterActivity extends GoUpActivity implements OnQueryTextListener, OnInitListener, OnItemClickListener
{

	private SearchView searchView;
	private JsonTask task;
	private ArrayList<TwitterItemVO> dataSource;
	private JsonTwitterListAdapter adapter;
	private ListView list;
	private TwitterItemVO currentItem;
	private TextToSpeechBeta tts;

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_json_twitter);
		dataSource = new ArrayList<TwitterItemVO>();
		adapter = new JsonTwitterListAdapter(this, dataSource);
		list = (ListView)findViewById(android.R.id.list);
		list.setAdapter(adapter);
		list.setOnItemClickListener(this);
	}

	@Override    
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = new MenuInflater(this);
		inflater.inflate(R.menu.search_view, menu);
		MenuItem item = menu.findItem(R.id.menu_search);
		searchView = (SearchView)item.getActionView();
		searchView.setQuery("android", true);
		searchView.setQueryHint("Search Hint"); 
		searchView.setOnQueryTextListener(this);       
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	public boolean onQueryTextChange(String newText) {
		return false;
	}

	@Override
	public boolean onQueryTextSubmit(String query) 
	{
		task = new JsonTask();
		task.execute(query);
		return false;
	}

	private class JsonTask extends AsyncTask<String, TwitterItemVO, Integer>
	{

		private ProgressDialog progressDialog;

		private String getSearchUrl(String searchTerm)
		{
			int page = 1;
			int limit = 100;
			return "http://search.twitter.com/search.json?q=@" + searchTerm + "&rpp=" + limit + "&page=" + page;
		}

		@Override
		protected void onPreExecute()
		{
			adapter.clear();
			progressDialog = ProgressDialog.show(JsonTwitterActivity.this, "", "Loading. Please wait...", true);  
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(Integer result) 
		{
			super.onPostExecute(result);
			if( result == 0 )
			{
				Toast toast = Toast.makeText(JsonTwitterActivity.this, "No result", Toast.LENGTH_SHORT);
				toast.setGravity( Gravity.CENTER, 0, 0);
				toast.show();
			}
			searchView.clearFocus();
			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
			progressDialog.dismiss();
			adapter.notifyDataSetChanged();
		}

		@Override
		protected Integer doInBackground(String... params) 
		{
			String query = params[0];
			HttpClient client = new DefaultHttpClient();
			HttpGet get = new HttpGet(getSearchUrl(query));
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			String responseBody = null;
			try {
				responseBody = client.execute(get, responseHandler);
			} catch(Exception ex) {
				ex.printStackTrace();
			}
			JsonObject jsonObject = null;
			JsonParser parser = new JsonParser();
			try {
				jsonObject = (JsonObject)parser.parse(responseBody);
			}catch(Exception ex){
				ex.printStackTrace();
			}
			JsonArray array = null;
			try {
				array = (JsonArray)jsonObject.get("results");
			} catch(Exception ex){
				ex.printStackTrace();
			}
			int result = array.size();
			for(JsonElement element : array) {
				JsonObject object = (JsonObject)element;
				TwitterItemVO item = new TwitterItemVO(
						object.get("from_user").getAsString(),
						object.get("text").getAsString(),
						object.get("profile_image_url").getAsString()
						);
				publishProgress(item);
			}
			return result;
		}

		@Override
		protected void onProgressUpdate(TwitterItemVO... values) 
		{
			TwitterItemVO item = values[0];
			dataSource.add(item);
			super.onProgressUpdate(values);
		}
	}

	private static final ActionItemVO[] actionListData = new ActionItemVO[]{ 
		new ActionItemVO("Speech", R.drawable.ic_volume)
	};

	private String ttsEngine;

	@Override
	public void onItemClick(AdapterView<?> adapter, View view, final int index, long i)
	{
		currentItem = this.adapter.getItem(index);
		WidgetUtils.showSelectAccountTypeDialog(this, "Choose an action", actionListData, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) 
			{
				if( which == 0 )
				{
					tts = new TextToSpeechBeta(JsonTwitterActivity.this, JsonTwitterActivity.this);

					if( ttsEngine == null )
					{
						ttsEngine = tts.getDefaultEngineExtended();
						List<EngineInfo> list = tts.getEngines();
						if( list.size() != 0 )
						{
							for( EngineInfo info : list )
							{
								if( "com.svox.classic".equals(info.name) )
								{
									ttsEngine = info.name;
									break;
								}
								if( "com.svox.pico".equals(info.name) )
								{
									ttsEngine = info.name;
								}
								if( "com.googlecode.eyesfree.espeak".equals(info.name) )
								{
									ttsEngine = info.name;
								}
							}
						}
					}
					tts.setEngineByPackageNameExtended(ttsEngine);
					tts.setLanguage( Locale.US );
				}
			}
		} );
	}

	@Override
	protected void onDestroy() {
		if( tts != null )
		{
			tts.stop();
			tts.shutdown();
		}
		super.onDestroy();
	}

	private void speech()
	{
		tts.speak(currentItem.message, TextToSpeechBeta.QUEUE_FLUSH, null);
	}

	@Override
	public void onInit(int result, int version) 
	{
		if( result == TextToSpeechBeta.SUCCESS )
		{
			speech();
		}else{
			tts.shutdown();
		}
	}
}