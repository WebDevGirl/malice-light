package com.umessick.malicelight;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class XYZRecordsCollectionService extends IntentService {
	// Used to write to the system log from this class.
    public static final String LOG_TAG = "RSSPullService";

    /**
     * An IntentService must always have a constructor that calls the super constructor. The
     * string supplied to the super constructor is used to give a name to the IntentService's
     * background thread.
     */
    public XYZRecordsCollectionService() {
        super("XYZRecordsCollectionService");
    }
    
    @Override
    public void onCreate(){
    	super.onCreate();
    }

    /**
     * In an IntentService, onHandleIntent is run on a background thread.  As it
     * runs, it broadcasts its current status using the LocalBroadcastManager.
     * @param workIntent The Intent that starts the IntentService. This Intent contains the
     * URL of the web site from which the RSS parser gets data.
     */
    @Override
    protected void onHandleIntent(Intent workIntent) {
    	
    	Log.d("servicenotes","...starting record collection.....");
            
    	String records = workIntent.getStringExtra("records");
       Long startTime = Long.decode(workIntent.getStringExtra("startTime"));
       String sentTime = workIntent.getStringExtra("sentTime");
       
    	 
       try {
    	   HttpClient httpclient = new DefaultHttpClient();
    	   HttpPost httppost = new HttpPost("http://tsar208.grid.csun.edu/malice-light/public/api/v1/xyzrecords/bulk");
    	    	     
    	   List<NameValuePair> parameters= new ArrayList<NameValuePair>();
    	   parameters.add(new BasicNameValuePair("json", records.toString()));
    	   
    	   httppost.setEntity(new UrlEncodedFormEntity(parameters));
    	   HttpResponse response = httpclient.execute(httppost);
    	     
           BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "iso-8859-1"), 8);
           StringBuilder sb = new StringBuilder();
           sb.append(reader.readLine() + "\n");
           String line = "0";
           
           while ((line = reader.readLine()) != null) {
        	   sb.append(line + "\n");
           }
           reader.close();
           String results = sb.toString();
             
           if (results != null && results != "") {
        	  // Log.d("serviceRESULTS",results);
        	   Long elapsedTime = (System.currentTimeMillis() - startTime);
        	      String listening_time = String.format("%d min, %d sec", 
        	   			TimeUnit.MILLISECONDS.toMinutes(elapsedTime),
        	   		    TimeUnit.MILLISECONDS.toSeconds(elapsedTime) - 
        	   		    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(elapsedTime))
        	       );
        	   Log.d("elapsedTime",listening_time);
           } else {
        	   Log.d("serviceRESULTS","none");
           }
    	   
//    	   HttpClient httpclient = new DefaultHttpClient();
//           HttpPost httppost = new HttpPost("http://tsar208.grid.csun.edu/malice-light/public/api/v1/xyzrecords");
//           httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//
//           // Execute HTTP Post Request
//           HttpResponse response = httpclient.execute(httppost);
//
//           BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "iso-8859-1"), 8);
//           StringBuilder sb = new StringBuilder();
//           sb.append(reader.readLine() + "\n");
//           String line = "0";
//           
//           	while ((line = reader.readLine()) != null) {
//                 sb.append(line + "\n");
//             }
//             reader.close();
//             String results = sb.toString();
//             
//             if (results != null && results != "") {
//           	  Log.d("service",results);
//             } else {
//           	  Log.d("service","none");
//             }
//             
         } catch (Exception e) {
             e.printStackTrace();
  
         }
    }
}

