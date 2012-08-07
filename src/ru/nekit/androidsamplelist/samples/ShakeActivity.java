package ru.nekit.androidsamplelist.samples;

import ru.nekit.androidsamplelist.activityExtra.GoUpActivity;
import ru.nekit.androidsamplelist.view.ShakeMediator;
import android.os.Bundle;

public class ShakeActivity extends GoUpActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onResume() {
		if( !facade.hasMediator(ShakeMediator.NAME ))
		{
			facade.registerMediator(new ShakeMediator(this));
		}
		onSearchRequested();
		super.onResume();
	}

	@Override
	protected void onPause() {
		facade.removeMediator(ShakeMediator.NAME);
		super.onPause();
	}
}