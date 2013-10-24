//Alex Behannon
//10-07-2013
//ADP Week 1

package com.behannon.huntingcompanion;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class MapActivity extends FragmentActivity implements
		OnMapLongClickListener {

	// static final LatLng Omaha = new LatLng(41.26, -96.17);
	private GoogleMap map;

	ArrayList<LatLng> listOfPoints = new ArrayList<LatLng>();
	ArrayList<String> pointNames = new ArrayList<String>();
	LatLng selectedCoords;
	String selectedName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);

		// Title bar edit
		getActionBar().setDisplayOptions(
				ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME);

		// Map setup
		map = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map)).getMap();
		map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
		map.setMyLocationEnabled(true);
		map.setOnMapLongClickListener(this);

		// Checks if map exists or not - if map exists, attempts to move to GPS location
		if (map != null) {
			try {
				//Set up location manager
				LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
				
				//Set up criteria
				Criteria criteria = new Criteria();
				
				//Set up provider
				String provider = locationManager.getBestProvider(criteria, true);
				
				//Get current location
				Location myLocation = locationManager.getLastKnownLocation(provider);
				
				//get lat and long
				double latitude = myLocation.getLatitude();
				double longitude = myLocation.getLongitude();
				LatLng currentLoc = new LatLng(latitude, longitude);
				
				//Move camera to lat and long
				map.moveCamera(CameraUpdateFactory.newLatLng(currentLoc));
				map.animateCamera(CameraUpdateFactory.zoomTo(14));
			} catch (Exception e) {
				System.out.println("Error with GPS location");
			}
		} else {
			System.out.println("Error with map");
		}

		loadPoints();
		
		//Testing infowindow setup
		map.setOnInfoWindowClickListener(new OnInfoWindowClickListener(){

			@Override
			public void onInfoWindowClick(Marker marker) {
				
				selectedCoords = marker.getPosition();
				selectedName = marker.getTitle();
				System.out.println("TEST OUTPUT:" + selectedCoords);
				
				marker.remove();
				
				delPoint();

			}
			
		});

	}

	@Override
	public void onMapLongClick(final LatLng point) {
		
		//Alert setup
		AlertDialog.Builder alert = new AlertDialog.Builder(this);
		
		//Set alert info
		alert.setTitle("Add Marker");
		alert.setMessage("Enter the title below");
		
		//Edit text for alert
		final EditText input = new EditText(this);
		alert.setView(input);
		
		//alert accept
		alert.setPositiveButton("Add Marker", new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int whichButton){
				String value = input.getText().toString();
				
				addMarker(value);
				
			}

			private void addMarker(String value) {
				map.addMarker(new MarkerOptions().position(point).title(value).snippet("Tap here to delete marker"));
				listOfPoints.add(point);
				pointNames.add(value);
				System.out.println("TEST:" + value + ": " + point);
				savePoints();

				Toast.makeText(getApplicationContext(), "Marker Added",
						Toast.LENGTH_SHORT).show();
			}
		});
		
		alert.show();

	}
	

	public void savePoints() {
		try {
			// Modes: MODE_PRIVATE, MODE_WORLD_READABLE, MODE_WORLD_WRITABLE
			FileOutputStream output = openFileOutput("latlngpointsnames.txt",
					Context.MODE_PRIVATE);
			DataOutputStream dout = new DataOutputStream(output);
			dout.writeInt(pointNames.size()); // Save line count
			for (String string : pointNames) {
				dout.writeUTF(string);
				Log.v("write", string);
			}
			dout.flush(); // Flush stream ...
			dout.close(); // ... and close.
		} catch (IOException exc) {
			exc.printStackTrace();
		}
		try {
			// Modes: MODE_PRIVATE, MODE_WORLD_READABLE, MODE_WORLD_WRITABLE
			FileOutputStream output = openFileOutput("latlngpoints.txt",
					Context.MODE_PRIVATE);
			DataOutputStream dout = new DataOutputStream(output);
			dout.writeInt(listOfPoints.size()); // Save line count
			for (LatLng point : listOfPoints) {
				dout.writeUTF(point.latitude + "," + point.longitude);
				Log.v("write", point.latitude + "," + point.longitude);
			}
			dout.flush(); // Flush stream ...
			dout.close(); // ... and close.
		} catch (IOException exc) {
			exc.printStackTrace();
		}
	}
	
	public void delPoint(){
		try{
			System.out.println("TESTING DEL");
			Iterator<LatLng> it = listOfPoints.iterator();
			while (it.hasNext()){
				LatLng cpoint = it.next();
				if(cpoint.equals(selectedCoords)){
					it.remove();
					System.out.println("POINT REMOVED");
				}
			}
			Iterator<String> it2 = pointNames.iterator();
			while (it2.hasNext()){
				String cname = it2.next();
				if(cname.equals(selectedName)){
					it2.remove();
					System.out.println("NAME REMOVED");
				}
			}
			savePoints();
			
		} catch(Exception e){
			e.printStackTrace();
		}
		map.clear();
		loadPoints();
	}

	@Override
	public void onResume() {
		super.onResume();

		loadPoints();
	}

	public void loadPoints() {
		try {
			FileInputStream input = openFileInput("latlngpoints.txt");
			DataInputStream din = new DataInputStream(input);
			int sz = din.readInt(); // Read line count
			for (int i = 0; i < sz; i++) {
				String str = din.readUTF();
				Log.v("read", str);
				String[] stringArray = str.split(",");
				double latitude = Double.parseDouble(stringArray[0]);
				double longitude = Double.parseDouble(stringArray[1]);
				listOfPoints.add(new LatLng(latitude, longitude));
			}
			din.close();
		} catch (IOException exc) {
			exc.printStackTrace();
		}
		
		try {
			FileInputStream input = openFileInput("latlngpointsnames.txt");
			DataInputStream din = new DataInputStream(input);
			int sz = din.readInt(); // Read line count
			for (int i = 0; i < sz; i++) {
				String str = din.readUTF();
				Log.v("read", str);
				pointNames.add(new String(str));
			}
			din.close();
		} catch (IOException exc) {
			exc.printStackTrace();
		}

		if (listOfPoints != null) {
			if(pointNames != null){
				for (int i = 0; i < pointNames.size(); i++) {
					String name = pointNames.get(i);
					for (int x = 0; x < listOfPoints.size(); x++) {
						drawMarker(listOfPoints.get(x), name);
					}
				}
			} else {
				System.out.println("ERROR LOADING");
			}
		}
	}

	// Put marker on map from list
	private void drawMarker(LatLng point, String name) {
		map.addMarker(new MarkerOptions().position(point).title(name).snippet("Tap here to delete marker"));
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
