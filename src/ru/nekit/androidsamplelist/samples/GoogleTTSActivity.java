package ru.nekit.androidsamplelist.samples;

import ru.nekit.androidsamplelist.activityExtra.GoUpActivity;
import android.content.Intent;
import android.os.Bundle;

public class GoogleTTSActivity extends GoUpActivity{

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		finish();
		startActivity(new Intent(this, JsonTwitterActivity.class));
	}
}