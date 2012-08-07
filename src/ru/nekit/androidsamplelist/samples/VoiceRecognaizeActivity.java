package ru.nekit.androidsamplelist.samples;

import java.util.ArrayList;
import java.util.List;

import ru.nekit.androidsamplelist.R;
import ru.nekit.androidsamplelist.activityExtra.GoUpActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

public class VoiceRecognaizeActivity extends GoUpActivity{


	static final int VOICE_RECOGNAIZE = 1;
	private ListView list;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_voice_recognaize);
		list = (ListView)findViewById(android.R.id.list);
		checkOnVoiceRecognaizeSupports();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		menu.add(0, VOICE_RECOGNAIZE, 0, "Voice recognaize")
		.setIcon( checkOnVoiceRecognaizeSupports() ? R.drawable.ic_mic : R.drawable.ic_mic_muted )
		.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		if( item.getItemId() == VOICE_RECOGNAIZE )
		{
			Intent listenIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
			listenIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getClass().getPackage().getName());
			listenIntent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Please talk!");
			listenIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
			listenIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 10);
			startActivityForResult(listenIntent, VOICE_RECOGNAIZE);
		}
		return super.onMenuItemSelected(featureId, item);
	}

	private boolean checkOnVoiceRecognaizeSupports()
	{
		List<ResolveInfo> activitiesList =  getPackageManager().queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH), 0);
		boolean hasVoiceRecognaize = activitiesList.size() != 0;
		if( !hasVoiceRecognaize )
		{
			new AlertDialog.Builder(this).setTitle("Attention").
			setMessage("Voice recognaize not supports").setPositiveButton("OK",  new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					dialog.dismiss();
				}
			}).
			setCancelable(true).create().show();
		}
		return hasVoiceRecognaize;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if( requestCode == VOICE_RECOGNAIZE )
		{
			if( resultCode == RESULT_OK ) 
			{
				ArrayList<String> suggestedWords = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

				if( suggestedWords.size() == 0 )
				{
					new AlertDialog.Builder(this).setTitle("Attention").
					setMessage("No result").setPositiveButton("OK",  new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							dialog.dismiss();
						}
					}).
					setCancelable(true).create().show();
				}else
				{
					list.setAdapter(new ArrayAdapter<String> (this, android.R.layout.simple_list_item_1, suggestedWords));
				}
			}
			else if( resultCode == RESULT_CANCELED ) 
			{
				new AlertDialog.Builder(this).setTitle("Attention").
				setMessage("Voice recognaize canceled").setPositiveButton("OK",  new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						dialog.dismiss();
					}
				}).
				setCancelable(true).create().show();
			}

		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}