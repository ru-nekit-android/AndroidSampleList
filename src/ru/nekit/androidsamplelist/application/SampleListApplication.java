package ru.nekit.androidsamplelist.application;

import ru.nekit.androidsamplelist.facade.SampleListFacade;
import android.app.Application;

public class SampleListApplication extends Application {

	private SampleListFacade facade;

	public SampleListApplication() 
	{
		facade = SampleListFacade.getInstance();
	}

	public SampleListFacade getFacade()
	{
		return facade;
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

}