//Alex Behannon
//10-07-2013
//ADP Week 1

package com.behannon.huntingcompanion;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class TrophyActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trophy);
		
		Button addButton = (Button)findViewById(R.id.addButton);
		addButton.setOnClickListener(new View.OnClickListener() {
		    public void onClick(View v) {
		        nextActivity();
		    }
		});
		
	}

	//Menu List Setup
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		menu.getItem(1).setVisible(false);
		return true;
	}
	
	//Menu selections setup
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		boolean ret = false;
		if (item.getItemId() == R.id.action_weather) {
			Intent intent = new Intent(TrophyActivity.this, WeatherActivity.class);
			startActivity(intent);
			ret = true;
		} else if (item.getItemId() == R.id.action_timers) {
			Intent intent = new Intent(TrophyActivity.this, TimerActivity.class);
			startActivity(intent);
			ret = true;
		} else if (item.getItemId() == R.id.action_calls) {
			Intent intent = new Intent(TrophyActivity.this, CallsActivity.class);
			startActivity(intent);
			ret = true;
		} else if (item.getItemId() == R.id.action_map) {
			Intent intent = new Intent(TrophyActivity.this, MapActivity.class);
			startActivity(intent);
			ret = true;
		} else if (item.getItemId() == R.id.action_trophies) {
			ret = true;
		} else if (item.getItemId() == R.id.action_settings) {
			Intent intent = new Intent(TrophyActivity.this, SettingsActivity.class);
			startActivity(intent);
			ret = true;
		} else {
			ret = super.onOptionsItemSelected(item);
		}
		return ret;
	}
	
	// Called when attempting to open the second activity
	public void nextActivity() {
		Intent myIntent = new Intent(TrophyActivity.this, AddTrophyActivity.class);
		TrophyActivity.this.startActivity(myIntent);
	}
}
