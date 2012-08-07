package ru.nekit.androidsamplelist.service;

import org.puremvc.java.interfaces.IProxy;
import org.puremvc.java.patterns.proxy.Proxy;

import ru.nekit.androidsamplelist.facade.SampleListFacade;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class AccelerometerService extends Proxy implements IProxy {

	public static String NAME = "accelerometerService";
	private SensorEventListener listener;
	private SensorManager sensorManager ;

	public AccelerometerService() {
		super(NAME);
	}

	public void start(SensorEventListener listener)
	{
		sensorManager = (SensorManager)SampleListFacade.getContext().getSystemService(Context.SENSOR_SERVICE);
		Sensor sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		sensorManager.registerListener(listener, sensor, SensorManager.SENSOR_DELAY_UI);
		this.listener = listener;
	}

	public Boolean isActive()
	{
		return sensorManager != null;
	}

	public void stop()
	{
		sensorManager.unregisterListener(listener);
		sensorManager = null;
	}
}