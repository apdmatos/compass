package pt.smart.thought.activities;

import pt.smart.thought.views.Rose;
import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class Compass extends Activity {
	
	private SensorManager sensorManager;
	private Sensor sensor;
	private Rose rose;
	
	private SensorEventListener sensorListener = new SensorEventListener() {
		
		@Override
		public void onSensorChanged(SensorEvent event) {
			int orientation = (int) event.values[0];
			Log.d("Compass", "Got sensor event: " + event.values[0]);
			rose.setDirection(orientation);
		}
		
		@Override
		public void onAccuracyChanged(Sensor arg0, int arg1) { }
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Set full screen view
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
				WindowManager.LayoutParams.FLAG_FULLSCREEN); 
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		// Create new instance of custom Rose and set it on the screen
		rose = new Rose(this);
		setContentView(rose);
		
		// Get sensor and sensor manager
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE); 
		sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
		
		Log.d("Compass", "onCreated");
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		sensorManager.registerListener(sensorListener , sensor, SensorManager.SENSOR_DELAY_NORMAL);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		sensorManager.unregisterListener(sensorListener);
	}
	
}
