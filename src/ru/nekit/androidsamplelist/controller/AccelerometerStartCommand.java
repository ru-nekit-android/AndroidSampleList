package ru.nekit.androidsamplelist.controller;

import org.puremvc.java.interfaces.ICommand;
import org.puremvc.java.interfaces.INotification;
import org.puremvc.java.patterns.command.SimpleCommand;

import ru.nekit.androidsamplelist.abc.NotificationNames;
import ru.nekit.androidsamplelist.model.ForcePoint;
import ru.nekit.androidsamplelist.service.AccelerometerService;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class AccelerometerStartCommand extends SimpleCommand implements ICommand {

	@Override
	public void execute(INotification note) 
	{
		AccelerometerService service = (AccelerometerService)facade.retrieveProxy(AccelerometerService.NAME);
		if( !service.isActive() )
		{
			final ForcePoint point = (ForcePoint)facade.retrieveProxy(ForcePoint.NAME);
			service.start(new SensorEventListener() {

				private int shakeCount = 0;

				@Override
				public void onAccuracyChanged(Sensor sensor, int accuracy) {

				}

				@Override
				public void onSensorChanged(SensorEvent event) 
				{
					if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
						if (isShakeEnough(event.values[SensorManager.DATA_X], event.values[SensorManager.DATA_Y], event.values[SensorManager.DATA_Z]))
							sendNotification(NotificationNames.SHAKE_DETECT);
				}

				private boolean isShakeEnough(float x, float y, float z) {
					double force = 0.0d;
					force += Math.pow((x - point.x) / SensorManager.GRAVITY_EARTH, 2.0);
					force += Math.pow((y - point.y) / SensorManager.GRAVITY_EARTH, 2.0);
					force += Math.pow((z - point.z) / SensorManager.GRAVITY_EARTH, 2.0);
					force = Math.sqrt(force);

					point.force = force;

					point.x = x;
					point.y = y;
					point.z = z;

					if (force >  ForcePoint.threshold_max ) {
						shakeCount++;
						if (shakeCount > 2) {
							shakeCount = 0;
							point.x = 0;
							point.y = 0;
							point.z = 0;
							return true;
						}
					}
					sendNotification(NotificationNames.ACCELEROMETER_STATUS_UPDATE);
					return false;
				}
			});
		}
	}
}