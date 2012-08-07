package ru.nekit.androidsamplelist.controller;

import org.puremvc.java.interfaces.ICommand;
import org.puremvc.java.interfaces.INotification;
import org.puremvc.java.patterns.command.SimpleCommand;

import ru.nekit.androidsamplelist.abc.NotificationNames;
import ru.nekit.androidsamplelist.facade.SampleListFacade;

import android.content.Context;
import android.os.Handler;
import android.os.Vibrator;

public class VibrateCommand extends SimpleCommand implements ICommand {

	private static Handler handler;
	private static Runnable runnable;

	@Override
	public void execute(INotification note) 
	{

		String name = note.getName();
		if( NotificationNames.VIBRATE.equals(name) )
		{
			Vibrator vibrate = (Vibrator)SampleListFacade.getContext().getSystemService(Context.VIBRATOR_SERVICE);
			if( handler == null )
			{
				Integer duration = (Integer)note.getBody();
				handler = new Handler();			
				sendNotification(NotificationNames.VIBRATE_START);
				runnable = new Runnable() {
					@Override
					public void run() {
						sendNotification(NotificationNames.VIBRATE_STOP);
						handler = null;
					}
				};
				vibrate.vibrate(duration);
				handler.postDelayed(runnable, duration);
			}else
			{
				handler.removeCallbacks(runnable);
				handler = null;
				vibrate.cancel();
				sendNotification(NotificationNames.VIBRATE_STOP);
			}
		}
	}
}