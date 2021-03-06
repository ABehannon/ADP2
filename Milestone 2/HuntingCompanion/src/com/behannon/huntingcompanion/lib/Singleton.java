//Alex Behannon
//10-07-2013
//ADP Week 1

package com.behannon.huntingcompanion.lib;

import android.content.Intent;

public class Singleton extends Intent{
	
	//Custom values
	static String URL;
	
	//Default singleton setup
	private static Singleton instance = null;
	
	private Singleton(){
		
	}
	
	public static Singleton getInstance(){
		if (instance == null){
			instance = new Singleton();
		}
		return instance;
	}
	
	//CUSTOM METHODS HERE
	
	//URL for API
	public static String APIURL(){
		URL = "http://www.myweather2.com/developer/forecast.ashx?uac=IucBn-/kwC&output=json&query=68118&temp_unit=f&ws_unit=mph";
		return URL;
	}
}