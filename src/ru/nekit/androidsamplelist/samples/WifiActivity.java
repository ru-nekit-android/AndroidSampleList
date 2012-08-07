package ru.nekit.androidsamplelist.samples;

import ru.nekit.androidsamplelist.activityExtra.GoUpActivity;
import ru.nekit.androidsamplelist.view.WifiMediator;
import android.os.Bundle;

public class WifiActivity extends GoUpActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		if( !facade.hasMediator(WifiMediator.NAME ))
		{
			facade.registerMediator(new WifiMediator(this));
		}
	}

	@Override
	protected void onDestroy() {
		facade.removeMediator(WifiMediator.NAME);
		super.onDestroy();
	}

}
