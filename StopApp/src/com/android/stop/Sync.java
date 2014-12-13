package com.android.stop;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

public class Sync extends Activity{
	Context _context;
	
	//Key Nodes
	public static final String KEY_LOCATION_NAME = "location_name";
	public static final String KEY_ACCIDENT_CATEGORY = "accident_category";
	public static final String KEY_INJURED_NUMBER = "number_injured";
	public static final String KEY_DEAD_NUMBER = "number_dead";
	public static final String KEY_IMAGE = "image";
	public static final String KEY_ACCIDENT_DATE = "date_posted";
	
	public Sync(Context context){
		_context = context;
	}
	
	@Override 
	protected void onCreate(Bundle savedInstance){
		super.onCreate(savedInstance);
	}
	
	//Sync Updwards 
	public void syncUp(){
		
	}
}
