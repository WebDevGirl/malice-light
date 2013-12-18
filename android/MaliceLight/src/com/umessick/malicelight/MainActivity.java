package com.umessick.malicelight;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.util.Linkify;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	private SensorManager mSensorManager;
	private LocationManager locationmanager;	
	private Intent mServiceIntent;	
	
	int acc_counter = 0;
	int light_counter = 0;
	int rec_counter = 0;
	
	JSONObject record;
	JSONArray records=new JSONArray();

	private String session_id = UUID.randomUUID().toString();
	
	
	long startTime = System.currentTimeMillis(); 
	long elapsedTime; 
	int MAX_REC = 2000;
	int MAX_TIME = 60000; /* 10 Mins */ 

    Sensor accel; 
    Sensor light; 
	
	
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
				
               	try {
		    		/* Create JSON Object */		    	
		    		record=new JSONObject();
		    		record.put("session_id", session_id);
		    		record.put("sequence_number", Integer.toString(acc_counter));
		    		record.put("sensor_id", 1);
		    		record.put("x", Float.toString(event.values[0]));
		    		record.put("y", Float.toString(event.values[1]));
		    		record.put("z", Float.toString(event.values[2]));
		    		record.put("record_date", new Timestamp(System.currentTimeMillis()).toString());
		    		record.put("record_timestamp", Long.toString(System.currentTimeMillis()));
			    	  
			    	/* Add JSONObject to JSONArray */
		    		records.put(record);
			    	   
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    	 		    	
		    	/* Update Counters */
		    	acc_counter++;
		    	rec_counter++;
		    	
    			break;    	
    			


    			
    		case Sensor.TYPE_LIGHT:
    			x = (TextView) findViewById(R.id.txt_light_value);
    	        x.setText(Float.toString(event.values[0]));
    	        
		    	try {
		    		/* Create JSON Object */		    	
		    		record=new JSONObject();
		    		record.put("session_id", session_id);
		    		record.put("sequence_number", Integer.toString(light_counter));
		    		record.put("sensor_id", 2);
		    		record.put("x", Float.toString(event.values[0]));
		    		record.put("y", 0);
		    		record.put("z", 0);
		    		record.put("record_date", new Timestamp(System.currentTimeMillis()).toString());
		    		record.put("record_timestamp", Long.toString(System.currentTimeMillis()));
			    	  
			    	/* Add JSONObject to JSONArray */
		    		records.put(record);
			    	   
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    	
		    	/* Update Counters */
		    	light_counter++;
		        rec_counter++;
        	        
				break;
       			
    		}
    		
    		/* Elapsed time since start of app */
    		elapsedTime = (System.currentTimeMillis() - startTime);
    				
	    	/* Send Data if MAX_REC count reached or MAX_TIME elapsed time reached */
	        if (rec_counter > MAX_REC || (elapsedTime >= MAX_TIME)) { 
	        	
	        	/* Creates a new Intent to send to the download IntentService. */
		        mServiceIntent = new Intent(getApplicationContext(), XYZRecordsCollectionService.class); 
		 			        
		        /* Send Data to Activity */
		    	mServiceIntent.putExtra("records", records.toString());
		    	mServiceIntent.putExtra("startTime", Long.toString(startTime));
		    	mServiceIntent.putExtra("sentTime", Long.toString(System.currentTimeMillis()));
		    	
	        	startService(mServiceIntent);	
	        	
	        	/* Reset Sensor Record Counter + Array */
	        	rec_counter = 0;
	        	records=new JSONArray();
	        }
	        
	        /* Stop Listening to Sensors when MAX_TIME elapsed time has been reached  */	      
	        if (elapsedTime >= MAX_TIME) {
	        	mSensorManager.unregisterListener(anySensorListener, accel);
	        	mSensorManager.unregisterListener(anySensorListener, light);
	        	String listening_time = String.format("%d min, %d sec", 
	        			TimeUnit.MILLISECONDS.toMinutes(elapsedTime),
	        		    TimeUnit.MILLISECONDS.toSeconds(elapsedTime) - 
	        		    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(elapsedTime))
	        	);
	        	
	        	Log.d("elapsedTimeTotal",Long.toString(light_counter + acc_counter));
	        	Log.d("elapsedTimeTotal",listening_time);
	        	
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
        
        /* Display Session ID */
        
        TextView txt_session_value = (TextView) findViewById(R.id.txt_session_value);
        txt_session_value.setText(session_id);
        
        /* Create Sensor Manager */
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        
        /* Create Sensors */
        accel = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER); 
        light = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT); 
        
        /* Register Sensors */
        mSensorManager.registerListener(anySensorListener, accel, SensorManager.SENSOR_DELAY_FASTEST);
        mSensorManager.registerListener(anySensorListener, light, SensorManager.SENSOR_DELAY_FASTEST);
        
        /* Register Button Clicks */
        Button btn = (Button) findViewById(R.id.btn_get_verdict);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            	 /* Fetch Verdict */
            	TextView x = (TextView) findViewById(R.id.txt_verdict_value);
            	x.setText("...updating...");
            	Log.d("verdict", "click");
            	new GetVerdict().execute();
            }
        });
        
        /* Log Session ID */
        Log.i("session",session_id);
        
        /* Add To URL */
        TextView txt_verdict_uri = (TextView) findViewById(R.id.txt_verdict_uri);
        txt_verdict_uri.setText("http://tsar208.grid.csun.edu/malice-light/public/api/v1/verdicts/"+session_id);
        Linkify.addLinks(txt_verdict_uri, Linkify.ALL);
        
       
    }
    
        /* http://stackoverflow.com/questions/9954477/async-http-post-android */ 
       	/* http://stackoverflow.com/a/12069670 */
        public class GetVerdict extends AsyncTask<Void, Void, String> {
            @Override
            protected String doInBackground(Void... params) {
                try {
                	Log.d("verdict", "...starting ...");
                	/* Prep */
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpGet httpget = new HttpGet("http://tsar208.grid.csun.edu/malice-light/public/api/v1/verdicts/"+session_id);
                   
                    /* Execute HTTP Post Request */
                    HttpResponse response = httpclient.execute(httpget);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "iso-8859-1"), 8);
                    StringBuilder sb = new StringBuilder();
                    
                    /* Prep Return Results */
                    sb.append(reader.readLine() + "\n");
                    String line = "0";
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    reader.close();
                    String result11 = sb.toString();
                    
                    Log.d("verdict", "...ending ...");

                    // parsing data
                    return new String(result11);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String result) {
            	String reponse_str;
            	TextView x = (TextView) findViewById(R.id.txt_verdict_value);
            	
            	TextView img_light = (TextView) findViewById(R.id.txt_image_light_uri);
            	TextView img_accel_x = (TextView) findViewById(R.id.txt_image_accel_x_uri);
            	TextView label_light = (TextView) findViewById(R.id.label_image_light_uri);
            	TextView label_accel_x = (TextView) findViewById(R.id.label_image_accel_x_uri);
            	
                if (result != null) {
                	
        	       // x.setText(result);
        	        
        	        /* Parse JSON - http://stackoverflow.com/questions/9605913/how-to-parse-json-in-android */
        	        JSONObject jObject;
					try {
						jObject = new JSONObject(result);
						String img_light_URI = jObject.getString("img_light_URI");
						String img_accel_x_URI = jObject.getString("img_accell_x_URI");
						Integer status_code = jObject.getInt("status");						
						JSONArray data_jarray = jObject.getJSONArray("data");
						
						/* Get Sensor #1*/
						reponse_str = "<br><b>" + data_jarray.getJSONObject(0).getString("sensor_name") + 
								":</b> " +
								data_jarray.getJSONObject(0).getString("record_count") + 
								" records saved";
						
						/* Get Sensor #2 */
						reponse_str += "<br><b>" + data_jarray.getJSONObject(1).getString("sensor_name") + 
								":</b> " +
								data_jarray.getJSONObject(1).getString("record_count") + 
								" records saved";
						
						if (status_code == 200) {
							/* Display Results */
							x.setText(Html.fromHtml(reponse_str));
							
							/* Light Graph */							
							img_light.setText(img_light_URI);
							label_light.setText("Light Sensor Image Analysis");
							
							/* Accelerometer Graph */							
							img_accel_x.setText(img_accel_x_URI);
							label_accel_x.setText("Accelerometer Sensor Image Analysis");
						} else {
							x.setText("Error parsing results");
						}
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Log.e("error","JSONException - something went wrong on: onPostExecute() - GetVerdict");
					}
        	        
        
                } else {
                    // error occurred
                	x.setText("Error parsing results");
                	Log.e("error","something went wrong on: onPostExecute() - GetVerdict");
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
