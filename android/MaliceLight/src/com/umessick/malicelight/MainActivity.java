package com.umessick.malicelight;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity {
	private SensorManager mSensorManager;
	LocationManager locationmanager;
	
	/* http://developer.android.com/guide/topics/sensors/sensors_overview.html */
	SensorEventListener anySensorListener = new SensorEventListener() {
    	@Override
    	public void onSensorChanged(SensorEvent event) {
    		int type = event.sensor.getType();
    		TextView x;
    		TextView y;
    		TextView z;
    		
    		switch (type) {
    		case Sensor.TYPE_ACCELEROMETER:
    			x = (TextView) findViewById(R.id.txt_acceleromter_x);
    			x.setText(Float.toString(event.values[0]));
            	
            	y = (TextView) findViewById(R.id.txt_acceleromter_y);
            	y.setText(Float.toString(event.values[1]));
            	
            	z = (TextView) findViewById(R.id.txt_acceleromter_z);
            	z.setText(Float.toString(event.values[2]));
    			break;    			
    			
    		case Sensor.TYPE_LIGHT:
    			x = (TextView) findViewById(R.id.txt_light_value);
    	        x.setText(Float.toString(event.values[0]));
    	        break;
       			
    		}
    
    	}

		@Override
		public void onAccuracyChanged(Sensor arg0, int arg1) {
			// TODO Auto-generated method stub
			
		}	
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        /* Create Sensor Manager */
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        
        /* Create Sensors */
        Sensor accel = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER); 
        Sensor light = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT); 
        
        /* Register Sensors */
        mSensorManager.registerListener(anySensorListener, accel, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(anySensorListener, light, SensorManager.SENSOR_DELAY_UI);

        new FetchTask().execute();
    }
        
       /* http://stackoverflow.com/questions/9954477/async-http-post-android */        
        public class FetchTask extends AsyncTask<Void, Void, String> {
            @Override
            protected String doInBackground(Void... params) {
                try {
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost("https://tsar208.grid.csun.edu/malice/public/api/v1/xyzrecords");

                    // Add your data
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                    nameValuePairs.add(new BasicNameValuePair("id", "12345"));
                    nameValuePairs.add(new BasicNameValuePair("stringdata", "AndDev is Cool!"));
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    // Execute HTTP Post Request
                    HttpResponse response = httpclient.execute(httppost);

                    BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "iso-8859-1"), 8);
                    StringBuilder sb = new StringBuilder();
                    sb.append(reader.readLine() + "\n");
                    String line = "0";
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    reader.close();
                    String result11 = sb.toString();
                    
                    Log.d("msg",result11);

                    // parsing data
                    return new String(result11);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String result) {
                if (result != null) {
                	TextView x = (TextView) findViewById(R.id.txt_JSON_value);
        	        x.setText(result);
                } else {
                    // error occured
                }
            }
        }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
