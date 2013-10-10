//Alex Behannon
//10-07-2013
//ADP Week 1

package com.behannon.huntingcompanion;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MapActivity extends FragmentActivity {

	static final LatLng Omaha = new LatLng(41.26, -96.17);
	private GoogleMap map;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

		map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
				.getMap();
		map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
		map.setMyLocationEnabled(true);

		if (map != null) {
			map.addMarker(new MarkerOptions().position(Omaha)
					.title("Omaha"));
			
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(Omaha, 18));
		} else {
			System.out.println("Error with map");
		}

	}

	// Menu List Setup
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		menu.getItem(2).setVisible(false);
		return true;
	}

	// Menu selections setup
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		boolean ret = false;
		if (item.getItemId() == R.id.action_weather) {
			Intent intent = new Intent(MapActivity.this, WeatherActivity.class);
			startActivity(intent);
			ret = true;
		} else if (item.getItemId() == R.id.action_timers) {
			Intent intent = new Intent(MapActivity.this, TimerActivity.class);
			startActivity(intent);
			ret = true;
		} else if (item.getItemId() == R.id.action_calls) {
			Intent intent = new Intent(MapActivity.this, CallsActivity.class);
			startActivity(intent);
			ret = true;
		} else if (item.getItemId() == R.id.action_map) {
			ret = true;
		} else if (item.getItemId() == R.id.action_trophies) {
			Intent intent = new Intent(MapActivity.this, TrophyActivity.class);
			startActivity(intent);
			ret = true;
		} else if (item.getItemId() == R.id.action_settings) {
			Intent intent = new Intent(MapActivity.this, SettingsActivity.class);
			startActivity(intent);
			ret = true;
		} else {
			ret = super.onOptionsItemSelected(item);
		}
		return ret;
	}
}
