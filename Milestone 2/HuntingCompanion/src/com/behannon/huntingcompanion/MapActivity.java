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

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MapActivity extends FragmentActivity implements OnMapLongClickListener{

	static final LatLng Omaha = new LatLng(41.26, -96.17);
	private GoogleMap map;
	
	ArrayList<LatLng> listOfPoints = new ArrayList<LatLng>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		
		//Title bar edit
		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM|ActionBar.DISPLAY_SHOW_HOME);

		//Map setup
		map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
				.getMap();
		map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
		map.setMyLocationEnabled(true);
		map.setOnMapLongClickListener(this);
		
		//Checks if map exists or not
		if (map != null) {
			map.addMarker(new MarkerOptions().position(Omaha)
					.title("Omaha"));
			
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(Omaha, 18));
		} else {
			System.out.println("Error with map");
		}
		
		loadPoints();

	}
	
	@Override
	 public void onMapLongClick(LatLng point) {
	  map.addMarker(new MarkerOptions().position(point).title(point.toString()));
	  listOfPoints.add(point);
	  savePoints();
	  
	  Toast.makeText(getApplicationContext(),
				"Marker Added", Toast.LENGTH_SHORT)
				.show();
	  
	 }
	
	public void savePoints(){
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
	
	@Override
	public void onResume(){
		super.onResume();
		
		loadPoints();
	}
	
	public void loadPoints(){
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
		
		if(listOfPoints!=null){
            for(int i=0;i<listOfPoints.size();i++){
                drawMarker(listOfPoints.get(i));
            }
        }
	}
	
	//Put marker on map from list
    private void drawMarker(LatLng point){
    	MarkerOptions markerOptions = new MarkerOptions();
    	markerOptions.position(point);
    	markerOptions.title("Lat:"+point.latitude+","+"Lng:"+point.longitude);
 
        // Adding marker on the Google Map
        map.addMarker(markerOptions);
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
