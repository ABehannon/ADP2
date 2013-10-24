//Alex Behannon
//10-07-2013
//ADP Week 1

package com.behannon.huntingcompanion;

import java.net.MalformedURLException;

import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.json.JSONException;
import org.json.JSONObject;

import com.behannon.huntingcompanion.lib.FileSaving;
import com.behannon.huntingcompanion.lib.Web;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class TimerActivity extends Activity {

	// initial setup for variables and such
	Context _context;
	Boolean _connected;
	Boolean internetConnection = true;
	String zipcode;
	String sunrise;
	String sunset;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_timer);

		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM|ActionBar.DISPLAY_SHOW_HOME);
		
		timerSetup();
		
		Button sunriseButton = (Button)findViewById(R.id.sunriseButton);
		sunriseButton.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
		    	Toast.makeText(getApplicationContext(),
						"Sunrise Timer Clicked",
						Toast.LENGTH_SHORT).show();
		    }
		});
		
		Button sunsetButton = (Button)findViewById(R.id.sunsetButton);
		sunsetButton.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
		    	Toast.makeText(getApplicationContext(),
						"Sunset Timer Clicked",
						Toast.LENGTH_SHORT).show();
		    }
		});

	}
	
	public void displayZipData() {

		String read = FileSaving.readStringFile(this, "zipData", false);

		// Init variables
		JSONObject json;
		
		try {

			// create json from the file loaded
			json = new JSONObject(read);
			
			// Set the zipcode for data retreival
			zipcode = json.get("zip").toString();
			
			System.out.println("Saved JSON Data: " + json);
			
			getTimes();

		} catch (JSONException e) {

			zipcode = "68118";
			e.printStackTrace();
			
			getTimes();
			

		}
	}

	// Menu List Setup
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		menu.getItem(1).setVisible(false);
		return true;
	}

	// Menu selections setup
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		boolean ret = false;
		if (item.getItemId() == R.id.action_weather) {
			Intent intent = new Intent(TimerActivity.this,
					WeatherActivity.class);
			startActivity(intent);
			ret = true;
		} else if (item.getItemId() == R.id.action_timers) {
			ret = true;
		} else if (item.getItemId() == R.id.action_calls) {
			Intent intent = new Intent(TimerActivity.this, CallsActivity.class);
			startActivity(intent);
			ret = true;
		} else if (item.getItemId() == R.id.action_map) {
			Intent intent = new Intent(TimerActivity.this, MapActivity.class);
			startActivity(intent);
			ret = true;
		} else if (item.getItemId() == R.id.action_trophies) {
			Intent intent = new Intent(TimerActivity.this, TrophyActivity.class);
			startActivity(intent);
			ret = true;
		} else if (item.getItemId() == R.id.action_settings) {
			Intent intent = new Intent(TimerActivity.this,
					SettingsActivity.class);
			startActivity(intent);
			ret = true;
		} else {
			ret = super.onOptionsItemSelected(item);
		}
		return ret;
	}

	// Layout test
	private void timerSetup() {

		// Setting up textViews for updating
		TextView sunriseText = (TextView) findViewById(R.id.sunriseTimeText);
		TextView sunsetText = (TextView) findViewById(R.id.sunsetTimeText);

		// Set context
		_context = this;

		// Test internet connection
		testConnection();

		// Update weather
		try {
			if (internetConnection = true) {
				sunriseText.setText("Updating...");
				sunsetText.setText("Updating...");

				displayZipData();
			} else {
				Toast.makeText(getApplicationContext(),
						"You are not currently connected to the internet.",
						Toast.LENGTH_LONG).show();

				sunriseText.setText("Unavailable");
				sunsetText.setText("Unavailable");

			}
		} finally {

		}

	}

	// Called to test internet when button pressed or app started
	private void testConnection() {

		_connected = Web.getConnectionStatus(_context);
		if (_connected) {
			internetConnection = true;
			System.out.println("ONLINE");

		} else {
			Toast.makeText(getApplicationContext(),
					"You are not currently connected to the internet.",
					Toast.LENGTH_LONG).show();
			internetConnection = false;
			System.out.println("OFFLINE");
		}
	}

	// Get the times of the requested zipcode
	@SuppressWarnings("unused")
	private void getTimes() {

		// Create init variables and fix URL info for timers
		String URLp1 = "http://api.wunderground.com/api/7de62da5b7cef6d0/astronomy/q/";
		String URLp2 = ".json";
		String moddedURL = URLp1 + zipcode + URLp2;
		String encodeURL;
		
		// Create init variables and fix URL info zip
		String zipURLp1 = "http://zipcodedistanceapi.redline13.com/rest/oxT5EaVv5gSTGzpKOpJcopnrF3FWv8gUF9ZkjVQpcIiThID67niwMGYsJDpfMF9s/info.json/";
		String zipURLp2 = "/degrees";
		String moddedURL2 = zipURLp1 + zipcode + zipURLp2;
		String zipencodeURL;	

		try {
			encodeURL = URLEncoder.encode(moddedURL, "UTF-8");
		} catch (Exception e) {
			Log.e("Encoding Failure", "Bad URL");
			encodeURL = "";
		}
		
		// zipcode info
		URL finalURL2;
		try {
			finalURL2 = new URL(moddedURL2);
			zipcodeRequest newRequest = new zipcodeRequest();
			newRequest.execute(finalURL2);
			System.out.println("Modded URL: " + moddedURL2);
		} catch (MalformedURLException e) {
			Log.e("Bad URL", "Malformed URL");
			finalURL2 = null;
		}

		// time info
		URL finalURL;
		try {
			finalURL = new URL(moddedURL);
			timesRequest newRequest = new timesRequest();
			newRequest.execute(finalURL);
			System.out.println("Modded URL: " + moddedURL);
		} catch (MalformedURLException e) {
			Log.e("Bad URL", "Malformed URL");
			finalURL = null;
		}

	}

	// Background tasks going on when using tool
	private class timesRequest extends AsyncTask<URL, Void, String> {

		@Override
		protected String doInBackground(URL... urls) {
			String response = "";
			for (URL url : urls) {
				response = Web.getURLStringResponse(url);

			}
			return response;
		}

		@SuppressWarnings("deprecation")
		@Override
		protected void onPostExecute(String result) {

			// Setting up textViews for updating
			TextView sunriseText = (TextView) findViewById(R.id.sunriseTimeText);
			TextView sunsetText = (TextView) findViewById(R.id.sunsetTimeText);
		
			// Get JSON info for zip	
			try {
				// JSON Object grab
				JSONObject json = new JSONObject(result);
				
				
				int sunriseHour = Integer.valueOf(json.getJSONObject("moon_phase").getJSONObject("sunrise").getString("hour"));
				int sunriseMinute = Integer.valueOf(json.getJSONObject("moon_phase").getJSONObject("sunrise").getString("minute"));
				int sunsetHour = Integer.valueOf(json.getJSONObject("moon_phase").getJSONObject("sunset").getString("hour"));
				int sunsetMinute = Integer.valueOf(json.getJSONObject("moon_phase").getJSONObject("sunset").getString("minute"));
				
				//Keeps sunrise in 12 hour format
				if(sunriseHour > 12){
					sunriseHour = sunriseHour - 12;
					if(sunriseMinute < 10){
						sunrise = sunriseHour + ":0" + sunriseMinute;
					} else{
						sunrise = sunriseHour + ":" + sunriseMinute;
					}
					sunrise = sunriseHour + ":" + sunriseMinute;
				} else{
					if(sunriseMinute < 10){
						sunrise = sunriseHour + ":0" + sunriseMinute;
					} else{
					sunrise = sunriseHour + ":" + sunriseMinute;
					}
				}

				//Keeps sunset in 12 hour format
				if(sunsetHour > 12){
					sunsetHour = sunsetHour - 12;
					if(sunsetMinute < 10){
						sunset = sunsetHour + ":0" + sunsetMinute;
					} else{
						sunset = sunsetHour + ":" + sunsetMinute;
					}
					sunset = sunsetHour + ":" + sunsetMinute;
				} else{
					if(sunsetMinute < 10){
						sunset = sunsetHour + ":0" + sunsetMinute;
					} else{
					sunset = sunsetHour + ":" + sunsetMinute;
					}
				}
				
				// Print out to screen
				sunriseText.setText(sunrise + " AM");
				sunsetText.setText(sunset + " PM");

				System.out.println("JSON SUCCESSFUL");
			} catch (JSONException e) {
				Log.e("JSON", "JSON OBJECT EXCEPTION");
				sunriseText.setText("Unavailable");
				sunsetText.setText("Unavailable");
			}

		}
	}
	
	// Background tasks going on when using tool
	private class zipcodeRequest extends AsyncTask<URL, Void, String> {

		@Override
		protected String doInBackground(URL... urls) {
			String response = "";
			for (URL url : urls) {
				response = Web.getURLStringResponse(url);

			}
			return response;
		}

		@Override
		protected void onPostExecute(String result) {

			// Setting up textViews for updating
			TextView cityText = (TextView) findViewById(R.id.cityText);

			// Get JSON info for zip
			try {
				// JSON Object grab
				JSONObject json2 = new JSONObject(result);

				// String set from json
				String zipInfo = json2.getString("city");

				// Print out to screen
				cityText.setText(zipInfo);

				System.out.println("JSON SUCCESSFUL");
			} catch (JSONException e) {
				Log.e("JSON", "JSON OBJECT EXCEPTION");
				cityText.setText("City\nUnavailable");
			}

		}
	}
}
