//Alex Behannon
//10-07-2013
//ADP Week 1

package com.behannon.huntingcompanion;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

public class WeatherActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weather);
	}

	//Menu List Setup
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		menu.getItem(5).setVisible(false);
		return true;
	}
	
	//Menu selections setup
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		boolean ret = false;
		if (item.getItemId() == R.id.action_weather) {
			ret = true;
		} else if (item.getItemId() == R.id.action_timers) {
			Intent intent = new Intent(WeatherActivity.this, TimerActivity.class);
			startActivity(intent);
			ret = true;
		} else if (item.getItemId() == R.id.action_calls) {
			Intent intent = new Intent(WeatherActivity.this, CallsActivity.class);
			startActivity(intent);
			ret = true;
		} else if (item.getItemId() == R.id.action_map) {
			Intent intent = new Intent(WeatherActivity.this, MapActivity.class);
			startActivity(intent);
			ret = true;
		} else if (item.getItemId() == R.id.action_trophies) {
			Intent intent = new Intent(WeatherActivity.this, TrophyActivity.class);
			startActivity(intent);
			ret = true;
		} else if (item.getItemId() == R.id.action_settings) {
			Intent intent = new Intent(WeatherActivity.this, SettingsActivity.class);
			startActivity(intent);
			ret = true;
		} else {
			ret = super.onOptionsItemSelected(item);
		}
		return ret;
	}
	
}
