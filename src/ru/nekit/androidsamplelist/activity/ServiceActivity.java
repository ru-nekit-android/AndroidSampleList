package ru.nekit.androidsamplelist.activity;

import ru.nekit.androidsamplelist.activityExtra.SampleCatagoryListActivity;
import android.os.Bundle;

public class ServiceActivity extends SampleCatagoryListActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
	}

	@Override
	protected String getCategory() 
	{
		return "ru.nekit.category.service";
	}
}