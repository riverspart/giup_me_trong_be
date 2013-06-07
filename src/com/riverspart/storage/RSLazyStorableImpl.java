package com.riverspart.storage;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.riverspart.RSConfigurationImpl;

public abstract class RSLazyStorableImpl extends RSConfigurationImpl implements
		RSLazyStorable {
	
	private static final String PREFERENCE_KEY_STRING = "com.riverspart";
	private SharedPreferences prefs;
	

	public RSLazyStorableImpl() {
		super();
		if(getCurrentActivity()!= null) {
			prefs = getCurrentActivity().getSharedPreferences(RSLazyStorableImpl.PREFERENCE_KEY_STRING, Context.MODE_WORLD_WRITEABLE);
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		//prefs = getCurrentActivity().getSharedPreferences(RSLazyStorableImpl.PREFERENCE_KEY_STRING, Context.MODE_WORLD_READABLE);
		prefs = this.getSharedPreferences(RSLazyStorableImpl.PREFERENCE_KEY_STRING, Context.MODE_WORLD_READABLE);
	}
	
	//		Start process
	/*-----------------------------------------------------------*/
	//		Stop process
	
	/***********************************************************/
	
	/**
	 * Override this when invoke methods
	 * while not running this activity
	 * means: non-activity runtime
	 */
	public Activity getCurrentActivity() {
		return null;
	}

	@Override
	public abstract void saveStorage(SharedPreferences prefs);
	@Override
	public void store() {
		saveStorage(prefs);
		//super.store(); //Must call store() to super if not abstract overriding in super
	}

	@Override
	public abstract void loadStorage(SharedPreferences prefs);
	@Override
	public void retrieve() {
		super.retrieve();
		loadStorage(prefs);
	}

	@Override
	public abstract void clearStorage(SharedPreferences prefs);
	@Override
	public void unstore() {
		super.unstore();
		clearStorage(prefs);
	}
}
