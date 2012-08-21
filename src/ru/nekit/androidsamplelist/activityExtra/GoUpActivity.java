package ru.nekit.androidsamplelist.activityExtra;

import ru.nekit.androidsamplelist.R;
import android.os.Bundle;
import android.view.MenuItem;

public class GoUpActivity extends FacadeActivity {

	protected boolean hasGoUp()
	{
		return true;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		if( hasGoUp() )
		{
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		if( item.getItemId() == android.R.id.home )
		{
			finish();
			overridePendingTransition(R.anim.slide_right_in, R.anim.slide_right_out);
		}
		return super.onMenuItemSelected(featureId, item);
	}

}
