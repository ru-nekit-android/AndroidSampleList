package ru.nekit.androidsamplelist.facade;

import org.puremvc.java.patterns.facade.Facade;

import ru.nekit.androidsamplelist.abc.NotificationNames;
import ru.nekit.androidsamplelist.controller.AccelerometerStartCommand;
import ru.nekit.androidsamplelist.controller.AccelerometerStopCommand;
import ru.nekit.androidsamplelist.controller.StartupCommand;
import ru.nekit.androidsamplelist.controller.VibrateCommand;
import android.app.Activity;
import android.content.Context;

public class SampleListFacade extends Facade 
{

	private static SampleListFacade instance = null;
	private static Context context;

	private SampleListFacade()
	{
	}

	public static Context getContext()
	{
		return context;
	}

	public static SampleListFacade getInstance()
	{
		if( instance == null )
		{
			instance = new SampleListFacade();
		}
		return instance;
	}

	@Override
	protected void initializeController()
	{
		super.initializeController();

		registerCommand( NotificationNames.STARTUP, new StartupCommand() );
		registerCommand( NotificationNames.VIBRATE, new VibrateCommand() );
		registerCommand(NotificationNames.ACCELEROMETER_START, new AccelerometerStartCommand());
		registerCommand(NotificationNames.ACCELEROMETER_STOP, new AccelerometerStopCommand());
	}

	public void startup( Activity activity )
	{
		context = activity;
		sendNotification( NotificationNames.STARTUP, activity );
	}
}