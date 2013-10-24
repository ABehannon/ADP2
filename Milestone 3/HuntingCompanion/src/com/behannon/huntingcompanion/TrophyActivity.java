//Alex Behannon
//10-07-2013
//ADP Week 1

package com.behannon.huntingcompanion;

import java.util.HashMap;
import java.util.Map;

import com.behannon.huntingcompanion.lib.SeparatedListAdapter;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class TrophyActivity extends Activity {
	
	public final static String ITEM_TITLE = "title";
	public final static String ITEM_CAPTION = "caption";

	// SectionHeaders
	private final static String[] days = new String[]{"Trophy 1", "Trophy 2", "Trophy 3"};

	// Section Contents
	private final static String[] notes = new String[]{"List Item 1", "List Item 2", "List Item 3"};

	// MENU - ListView
	private ListView addJournalEntryItem;

	// Adapter for ListView Contents
	private SeparatedListAdapter adapter;

	// ListView Contents
	private ListView journalListView;

	public Map<String, ?> createItem(String title, String caption)
		{
			Map<String, String> item = new HashMap<String, String>();
			item.put(ITEM_TITLE, title);
			item.put(ITEM_CAPTION, caption);
			return item;
		}


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
		
		//Action bar fix
		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM|ActionBar.DISPLAY_SHOW_HOME);
		

		// Create the ListView Adapter
		adapter = new SeparatedListAdapter(this);
		ArrayAdapter<String> listadapter = new ArrayAdapter<String>(this, R.layout.list_item, notes);

		// Add Sections
		for (int i = 0; i < days.length; i++)
			{
				adapter.addSection(days[i], listadapter);
			}
		
		// Get a reference to the ListView holder
		journalListView = (ListView) this.findViewById(R.id.list_journal);

		// Set the adapter on the ListView holder
		journalListView.setAdapter(adapter);

		// Listen for Click events
		journalListView.setOnItemClickListener(new OnItemClickListener()
			{
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long duration)
					{
						String item = (String) adapter.getItem(position);
						Toast.makeText(getApplicationContext(), item, Toast.LENGTH_SHORT).show();
					}
			});
		
	}

	//Menu List Setup
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		menu.getItem(4).setVisible(false);
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
