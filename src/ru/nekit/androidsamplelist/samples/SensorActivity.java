package ru.nekit.androidsamplelist.samples;

import java.util.ArrayList;
import java.util.List;

import ru.nekit.androidsamplelist.R;
import ru.nekit.androidsamplelist.activityExtra.GoUpActivity;
import ru.nekit.androidsamplelist.listAdapter.MenuListAdapter;
import ru.nekit.androidsamplelist.model.vo.MenuItemVO;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class SensorActivity extends GoUpActivity implements OnItemClickListener  {

	private static final String SENSOR_TYPE = "sensor_type";

	private ListView menuView;
	private MenuListAdapter adapter;
	private ArrayList<MenuItemVO> dataList;
	private List<Integer> sensorList;
	private Sensor sensor;
	private SensorManager sensorManager;
	private TextView textView;

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);

		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		Intent intent = getIntent();
		if( intent.hasExtra(SENSOR_TYPE)  )
		{
			setContentView(R.layout.activity_sensor);
			int type = intent.getIntExtra(SENSOR_TYPE, -1);
			sensor = sensorManager.getDefaultSensor(type);
			textView = (TextView)findViewById(R.id.textView);
			setTitle(sensor.getName());
			getSupportActionBar().setSubtitle(sensor.getVendor());
			textView.setText("Value: "+ String.valueOf(sensor.getMaximumRange()));
			sensorManager.registerListener(sensorEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL);
		}
		else
		{
			setContentView(R.layout.activity_sample_list_category);
			menuView = (ListView)findViewById(R.id.menuView);
			dataList = new ArrayList<MenuItemVO>();
			sensorList = new ArrayList<Integer>();
			getData();
			adapter = new MenuListAdapter(this, dataList);
			menuView.setDivider(null);
			menuView.setDividerHeight(0);
			menuView.setOnItemClickListener(this);
			menuView.setAdapter(adapter);
		}
	}

	SensorEventListener sensorEventListener = new SensorEventListener() {

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) 
		{}

		@Override
		public void onSensorChanged(SensorEvent event) 
		{
			if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) 
			{
				textView.setText("Value: " + String.valueOf(event.values[0]) + " cm");
			}else if (event.sensor.getType() == Sensor.TYPE_LIGHT) 
			{
				textView.setText("Value: " + String.valueOf(event.values[0] + " lux"));
			}else if (event.sensor.getType() == Sensor.TYPE_ORIENTATION ){

				textView.setText("[ x: " + String.valueOf(event.values[0]) + " | y: " + String.valueOf(event.values[1]) + " | z: " + String.valueOf(event.values[2]) + " ]");
			}
			else if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER ){

				textView.setText("m/s^2: [ x: " + String.valueOf(event.values[0]) + " | y: " + String.valueOf(event.values[1]) + " | z: " + String.valueOf(event.values[2]) + " ]");
			}
			else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD ){

				textView.setText("uT: [ x: " + String.valueOf(event.values[0]) + " | y: " + String.valueOf(event.values[1]) + " | z: " + String.valueOf(event.values[2]) + " ]");
			}
		}
	};

	private void getData()
	{
		List<Sensor> listSensor = sensorManager.getSensorList(Sensor.TYPE_ALL);
		for (int i = 0; i < listSensor.size(); i++) 
		{
			dataList.add(
					new MenuItemVO(listSensor.get(i).getName(), 
							"Version: " + listSensor.get(i).getVersion())
					);
			sensorList.add(listSensor.get(i).getType());
		}
	}

	@Override
	protected void onDestroy() 
	{
		if( sensor != null )
		{
			sensorManager.unregisterListener(sensorEventListener);
		}
		super.onDestroy();
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View view, int position, long i)
	{
		Intent go = new Intent(this, SensorActivity.class);
		go.putExtra(SENSOR_TYPE, sensorList.get(position));
		startActivity(go);
	}

}