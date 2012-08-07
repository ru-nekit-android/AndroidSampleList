package ru.nekit.androidsamplelist.activity;

import ru.nekit.androidsamplelist.activityExtra.SampleCatagoryListActivity;
import ru.nekit.androidsamplelist.facade.SampleListFacade;
import android.os.Bundle;

public class MainActivity extends SampleCatagoryListActivity 
{

	@Override
	protected boolean hasGoUp() 
	{
		return false;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		SampleListFacade.getInstance().startup(this);
	}

	@Override
	protected String getCategory() 
	{
		return "ru.nekit.category.main";
	}

}