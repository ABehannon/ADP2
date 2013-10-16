//Alex Behannon
//10-07-2013
//ADP Week 1

package com.behannon.huntingcompanion;

import org.json.JSONException;
import org.json.JSONObject;

import com.behannon.huntingcompanion.lib.FileSaving;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SettingsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		getActionBar().setDisplayOptions(
				ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);
		
		displayZipData();

		// Button for saving zipcode
		Button saveButton = (Button) findViewById(R.id.saveButton);
		saveButton.setOnClickListener(new View.OnClickListener() {
		
			public void onClick(View v) {
				System.out.println("CLICKED!");
				EditText zipBox = (EditText) findViewById(R.id.zipText);

				String zipcode = zipBox.getText().toString();

				if (zipcode.length() == 5) {
					 
					String zipTest = "{\"zip\":" + zipcode +  "}";
					System.out.println("Save Data: " + zipcode);
					FileSaving.storeStringFile(getApplicationContext(),
							"zipData", zipTest, false);

					Toast.makeText(getApplicationContext(),
							"The settings have been saved.", Toast.LENGTH_LONG)
							.show();
					
				} else{
					Toast.makeText(getApplicationContext(),
							"The zipcode length is incorrect.", Toast.LENGTH_LONG)
							.show();
				}
				
			}
		});

	}
	
	//Get current zipcode info
	public void displayZipData() {

		String read = FileSaving.readStringFile(this, "zipData", false);

		// Init variables
		JSONObject json;
		String zipcode;
		
		try {

			// create json from the file loaded
			json = new JSONObject(read);
			
			// Set the zipcode for data retreival
			zipcode = json.get("zip").toString();
			
			System.out.println("Saved JSON Data: " + json);

		} catch (JSONException e) {

			zipcode = "68118";
			e.printStackTrace();

		}
		
		EditText zipBox = (EditText) findViewById(R.id.zipText);
		zipBox.setText(zipcode);
	}

	// Menu List Setup
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		menu.getItem(5).setVisible(false);
		return true;
	}

	// Menu selections setup
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		boolean ret = false;
		if (item.getItemId() == R.id.action_weather) {
			Intent intent = new Intent(SettingsActivity.this,
					WeatherActivity.class);
			startActivity(intent);
			ret = true;
		} else if (item.getItemId() == R.id.action_timers) {
			Intent intent = new Intent(SettingsActivity.this,
					TimerActivity.class);
			startActivity(intent);
			ret = true;
		} else if (item.getItemId() == R.id.action_calls) {
			Intent intent = new Intent(SettingsActivity.this,
					CallsActivity.class);
			startActivity(intent);
			ret = true;
		} else if (item.getItemId() == R.id.action_map) {
			Intent intent = new Intent(SettingsActivity.this, MapActivity.class);
			startActivity(intent);
			ret = true;
		} else if (item.getItemId() == R.id.action_trophies) {
			Intent intent = new Intent(SettingsActivity.this,
					TrophyActivity.class);
			startActivity(intent);
			ret = true;
		} else if (item.getItemId() == R.id.action_settings) {
			ret = true;
		} else {
			ret = super.onOptionsItemSelected(item);
		}
		return ret;
	}
}
