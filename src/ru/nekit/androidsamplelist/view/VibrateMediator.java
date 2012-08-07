package ru.nekit.androidsamplelist.view;

import org.puremvc.java.interfaces.INotification;
import org.puremvc.java.patterns.mediator.Mediator;

import ru.nekit.androidsamplelist.R;
import ru.nekit.androidsamplelist.abc.NotificationNames;
import ru.nekit.androidsamplelist.samples.VibrateActivity;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class VibrateMediator extends Mediator implements OnClickListener, OnSeekBarChangeListener {

	public static final String NAME = "vibrateMediator";
	private Activity activity;
	private Button vibrateButton;
	private SeekBar seekBar;
	private TextView valueView;
	private int duration;


	public VibrateMediator(VibrateActivity activity) {
		super(NAME, activity);
		this.activity = activity;
	}

	@Override
	public String[] listNotificationInterests() {

		return new String[]
				{
				NotificationNames.VIBRATE_START,
				NotificationNames.VIBRATE_STOP,
				};

	};

	@Override
	public void handleNotification(INotification note) 
	{
		String name = note.getName();
		if( NotificationNames.VIBRATE_START.equals(name)  )
		{
			vibrateButton.setText(R.string.vibrate_cancel);
		}
		else if( NotificationNames.VIBRATE_STOP.equals(name)  )
		{
			vibrateButton.setText(R.string.vibrate);
		}
	};

	@Override
	public void onRegister() 
	{
		activity.setContentView(R.layout.activity_vibrate);
		vibrateButton = (Button)activity.findViewById(R.id.button_vibrate);
		vibrateButton.setOnClickListener(this);
		valueView = (TextView)activity.findViewById(R.id.valueView);
		seekBar = (SeekBar)activity.findViewById(R.id.seekBar);
		seekBar.setOnSeekBarChangeListener(this);
		seekBar.setMax(5000);
		seekBar.setProgress(2000);
	}

	@Override
	public void onClick(View v) {
		sendNotification(NotificationNames.VIBRATE, duration);
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		valueView.setText(""+progress);
		duration = progress;

	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {}
}