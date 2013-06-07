package com.riverspart.nurse;

import android.app.TabActivity;
import android.content.SharedPreferences;
import android.os.Bundle;

@SuppressWarnings("deprecation")
public abstract class NurseConfigurationImpl extends TabActivity implements NurseConfiguration {

	@Override
	public abstract void loadConfiguration();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(renderLayout(this));
	}
	
	@Override
	public void store() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void retrieve() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unstore() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveStorage(SharedPreferences prefs) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loadStorage(SharedPreferences prefs) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clearStorage(SharedPreferences prefs) {
		// TODO Auto-generated method stub
		
	}
}
