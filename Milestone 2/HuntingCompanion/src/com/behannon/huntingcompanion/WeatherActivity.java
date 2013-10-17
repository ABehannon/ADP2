//Alex Behannon
//10-07-2013
//ADP Week 1

package com.behannon.huntingcompanion;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.behannon.huntingcompanion.lib.FileSaving;
import com.behannon.huntingcompanion.lib.Web;
import com.handlerexploit.prime.widgets.RemoteImageView;

public class WeatherActivity extends Activity {

	// initial setup for variables and such
	Context _context;
	Boolean _connected;
	Boolean internetConnection = true;
	String zipcode;
	String weatherCode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weather);
		
		getActionBar().setDisplayOptions(
				ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);

		weatherSetup();
	}
	
	@Override
	public void onResume(){
		super.onResume();
		weatherSetup();
	}

	// Menu List Setup
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		menu.getItem(0).setVisible(false);
		return true;
	}
	
	// Menu selections setup
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		boolean ret = false;
		if (item.getItemId() == R.id.action_weather) {
			ret = true;
		} else if (item.getItemId() == R.id.action_timers) {
			Intent intent = new Intent(WeatherActivity.this,
					TimerActivity.class);
			startActivity(intent);
			ret = true;
		} else if (item.getItemId() == R.id.action_calls) {
			Intent intent = new Intent(WeatherActivity.this,
					CallsActivity.class);
			startActivity(intent);
			ret = true;
		} else if (item.getItemId() == R.id.action_map) {
			Intent intent = new Intent(WeatherActivity.this, MapActivity.class);
			startActivity(intent);
			ret = true;
		} else if (item.getItemId() == R.id.action_trophies) {
			Intent intent = new Intent(WeatherActivity.this,
					TrophyActivity.class);
			startActivity(intent);
			ret = true;
		} else if (item.getItemId() == R.id.action_settings) {
			Intent intent = new Intent(WeatherActivity.this,
					SettingsActivity.class);
			startActivity(intent);
			ret = true;
		} else {
			ret = super.onOptionsItemSelected(item);
		}
		return ret;
	}

	// Layout test
	private void weatherSetup() {

		// Setting up textViews for updating
		TextView cityText = (TextView) findViewById(R.id.cityText);
		TextView tempText = (TextView) findViewById(R.id.tempText);
		TextView windText = (TextView) findViewById(R.id.windText);

		// Set context
		_context = this;

		// Test internet connection
		testConnection();

		// Update weather
		try {
			if (internetConnection = true) {
				cityText.setText("City\nUpdating...");
				tempText.setText("Temperature\nUpdating...");
				windText.setText("Wind Direction\nUpdating...");

				displayZipData();
			} else {
				Toast.makeText(getApplicationContext(),
						"You are not currently connected to the internet.",
						Toast.LENGTH_LONG).show();

				cityText.setText("City\nUnavailable");
				tempText.setText("Temperature\nUnavailable");
				windText.setText("Wind Direction\nUnavailable");
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
			
			getWeather();

		} catch (JSONException e) {

			zipcode = "68118";
			e.printStackTrace();
			
			getWeather();

		}
	}

	// Get the weather of the requested location
	@SuppressWarnings("unused")
	private void getWeather() {

		// Create init variables and fix URL info for weather
		String URLp1 = "http://www.myweather2.com/developer/forecast.ashx?uac=IucBn-/kwC&output=json&query=";
		String URLp2 = "&temp_unit=f&ws_unit=mph";
		String moddedURL = URLp1 + zipcode + URLp2;
		String encodeURL;

		// Create init variables and fix URL info zip
		String zipURLp1 = "http://zipcodedistanceapi.redline13.com/rest/oxT5EaVv5gSTGzpKOpJcopnrF3FWv8gUF9ZkjVQpcIiThID67niwMGYsJDpfMF9s/info.json/";
		String zipURLp2 = "/degrees";
		String moddedURL2 = zipURLp1 + zipcode + zipURLp2;
		String zipencodeURL;

		try {
			zipencodeURL = URLEncoder.encode(moddedURL2, "UTF-8");
		} catch (Exception e) {
			Log.e("Encoding Failure", "Bad URL");
			zipencodeURL = "";
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
		
		// weather info
		URL finalURL;
		try {
			finalURL = new URL(moddedURL);
			weatherRequest newRequest = new weatherRequest();
			newRequest.execute(finalURL);
			System.out.println("Modded URL: " + moddedURL);
		} catch (MalformedURLException e) {
			Log.e("Bad URL", "Malformed URL");
			finalURL = null;
		}

	}

	// Background tasks going on when using tool
	private class weatherRequest extends AsyncTask<URL, Void, String> {

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
			TextView tempText = (TextView) findViewById(R.id.tempText);
			TextView windText = (TextView) findViewById(R.id.windText);

			// Get JSON info for weather
			try {
				// JSON Object grab
				JSONObject json = new JSONObject(result);
				JSONObject weatherInfo = json.getJSONObject("weather")
						.getJSONArray("curren_weather").getJSONObject(0);
				JSONObject windInfo = weatherInfo.getJSONArray("wind")
						.getJSONObject(0);

				// String set from json
				String getTemp = weatherInfo.getString("temp");
				String getWeatherType = weatherInfo.getString("weather_text");
				String getWindDir = windInfo.getString("dir").replace("Not Available", "");;
				String getWindAmount = windInfo.getString("speed");
				int getWeatherCode = Integer.valueOf(weatherInfo.getString("weather_code"));

				// Print out to screen
				windText.setText("Wind Direction\n" + getWindAmount + "MPH " + getWindDir);
				tempText.setText("Temperature\n" + getTemp + "¡F\n" + getWeatherType);
				
				//Setup for image of weather type
				if(getWeatherCode < 10){
					weatherCode = "cond00" + getWeatherCode;
				}else if (getWeatherCode >= 10 && getWeatherCode < 100){
					weatherCode = "cond0" + getWeatherCode;
				}else{
					weatherCode = "cond" + getWeatherCode;
				}
				String imageURL = "http://img.weather.weatherbug.com/forecast/icons/localized/90x76/en/trans/" + weatherCode + ".png";
				RemoteImageView remoteImageView = (RemoteImageView)findViewById(R.id.weatherType);
				remoteImageView.setImageURL(imageURL);

				System.out.println("JSON SUCCESSFUL");
			} catch (JSONException e) {
				Log.e("JSON", "JSON OBJECT EXCEPTION");
				windText.setText("Wind Direction\nUnavailable");
				tempText.setText("Temperature\nUnavailable");
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
