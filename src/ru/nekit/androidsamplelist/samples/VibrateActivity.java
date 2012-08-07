package ru.nekit.androidsamplelist.samples;

import ru.nekit.androidsamplelist.activityExtra.GoUpActivity;
import ru.nekit.androidsamplelist.view.VibrateMediator;
import android.os.Bundle;

public class VibrateActivity extends GoUpActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		if( !facade.hasMediator(VibrateMediator.NAME ))
		{
			facade.registerMediator(new VibrateMediator(this));
		}
	}

	@Override
	protected void onDestroy() {
		facade.removeMediator(VibrateMediator.NAME);
		super.onDestroy();
	}

}
