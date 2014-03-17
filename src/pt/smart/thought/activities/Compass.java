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
	private Sensor accelerometer;
	private Sensor magnetometer;
	private Rose rose;
	
	private SensorEventListener sensorListener = new SensorEventListener() {
		
		float[] mGravity;
		float[] mGeomagnetic;
		
		@Override
		public void onSensorChanged(SensorEvent event) {
			if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
			    mGravity = event.values;
			if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
			    mGeomagnetic = event.values;
			if (mGravity != null && mGeomagnetic != null) {
			    float R[] = new float[9];
			    float I[] = new float[9];
			    boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
			    if (success) {
			        float orientation[] = new float[3];
			        SensorManager.getOrientation(R, orientation);
			        float azimut = orientation[0];
			        rose.setDirection(azimut);
			    }
			}
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
		accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	    magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
				
		Log.d("Compass", "onCreated");
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		sensorManager.registerListener(sensorListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
	    sensorManager.registerListener(sensorListener, magnetometer, SensorManager.SENSOR_DELAY_NORMAL);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		sensorManager.unregisterListener(sensorListener);
	}
	
}
