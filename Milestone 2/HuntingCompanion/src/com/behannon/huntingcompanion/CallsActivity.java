//Alex Behannon
//10-07-2013
//ADP Week 1

package com.behannon.huntingcompanion;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class CallsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calls);
		
		//Action bar fix
		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM|ActionBar.DISPLAY_SHOW_HOME);
		
		//Handle media for calls
        final MediaPlayer duckCall = MediaPlayer.create(this, R.raw.duckcall);
        final MediaPlayer coyoteCall = MediaPlayer.create(this, R.raw.coyotecall);
        final MediaPlayer elkCall = MediaPlayer.create(this, R.raw.elkcall);
        final MediaPlayer turkeyCall = MediaPlayer.create(this, R.raw.turkeycall);
        
		//Set up listview for calls
        final ListView callsList = (ListView) findViewById(R.id.callsList);
        
        //Calls handling from listview
        callsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

        	@Override
        	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
        		System.out.println("POS: " + position);

                switch(position){
                	//Call 1 Duck
                	case 0:
                		duckCall.start();
                		break;
                      
                	//Call 2 Coyote
                	case 1:
                		coyoteCall.start();
                		break;
                		
                    //Call 3 Turkey
                    case 2:
                		turkeyCall.start();
                    	break;
                          
                    //Call 4 Elk
                    case 3:
                		elkCall.start();
                    	break;

                }
                
        	}
                
        });
        
	}

	//Menu List Setup
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		menu.getItem(3).setVisible(false);
		return true;
	}
	
	//Menu selections setup
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		boolean ret = false;
		if (item.getItemId() == R.id.action_weather) {
			Intent intent = new Intent(CallsActivity.this, WeatherActivity.class);
			startActivity(intent);
			ret = true;
		} else if (item.getItemId() == R.id.action_timers) {
			Intent intent = new Intent(CallsActivity.this, TimerActivity.class);
			startActivity(intent);
			ret = true;
		} else if (item.getItemId() == R.id.action_calls) {
			ret = true;
		} else if (item.getItemId() == R.id.action_map) {
			Intent intent = new Intent(CallsActivity.this, MapActivity.class);
			startActivity(intent);
			ret = true;
		} else if (item.getItemId() == R.id.action_trophies) {
			Intent intent = new Intent(CallsActivity.this, TrophyActivity.class);
			startActivity(intent);
			ret = true;
		} else if (item.getItemId() == R.id.action_settings) {
			Intent intent = new Intent(CallsActivity.this, SettingsActivity.class);
			startActivity(intent);
			ret = true;
		} else {
			ret = super.onOptionsItemSelected(item);
		}
		
		return ret;
	}
}
