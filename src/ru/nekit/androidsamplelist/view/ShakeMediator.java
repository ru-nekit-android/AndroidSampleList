package ru.nekit.androidsamplelist.view;

import org.puremvc.java.interfaces.INotification;
import org.puremvc.java.patterns.mediator.Mediator;

import ru.nekit.androidsamplelist.R;
import ru.nekit.androidsamplelist.abc.NotificationNames;
import ru.nekit.androidsamplelist.model.ForcePoint;
import ru.nekit.androidsamplelist.samples.ShakeActivity;
import android.app.Activity;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class ShakeMediator extends Mediator implements OnSeekBarChangeListener{

	public static final String NAME = "shakeMediator";
	private Activity activity;
	private TextView statusView;
	private TextView valueView;
	private SeekBar seekBar;
	private ForcePoint point;

	public ShakeMediator(ShakeActivity activity) {
		super(NAME, activity);
		this.activity = activity;
	}

	@Override
	public String[] listNotificationInterests() {

		return new String[]
				{
				NotificationNames.SHAKE_DETECT,
				NotificationNames.ACCELEROMETER_STATUS_UPDATE,
				};

	};

	@Override
	public void handleNotification(INotification note) 
	{
		String name = note.getName();
		if( NotificationNames.SHAKE_DETECT.equals(name) )
		{
			Toast.makeText(activity, "Shake detect", Toast.LENGTH_LONG).show();
			sendNotification(NotificationNames.VIBRATE, 100);
		}else
			if( NotificationNames.ACCELEROMETER_STATUS_UPDATE.equals(name) )
			{
				point = (ForcePoint)facade.retrieveProxy(ForcePoint.NAME);
				point.round(3);
				statusView.setText("[ x: " + point.x + " | y: " + point.y + " | z: " + point.z + " ]\n" + "Force: " + point.force);
			}
	};

	@Override
	public void onRegister() 
	{
		activity.setContentView(R.layout.activity_shake);
		statusView = (TextView)activity.findViewById(R.id.statusView);
		seekBar = (SeekBar)activity.findViewById(R.id.seekBar);
		seekBar.setOnSeekBarChangeListener(this);
		valueView = (TextView)activity.findViewById(R.id.valueView);
		valueView.setText("3 g");
		seekBar.setMax(500);
		seekBar.setProgress(300);
		sendNotification(NotificationNames.ACCELEROMETER_START);
	}

	@Override
	public void onRemove() 
	{
		sendNotification(NotificationNames.ACCELEROMETER_STOP);
		super.onRemove();
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		ForcePoint.threshold_max = (float)progress/100;
		valueView.setText("" + ForcePoint.threshold_max + " g");
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
	}

}