package com.riverspart;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.riverspart.storage.RSStorable;

public interface RSConfiguration extends RSStorable{
	/**
     * Tag used on log messages.
     */
	public String TAG = "RSConfiguration";
	
	public void loadConfiguration();
	public ViewGroup renderLayout(Activity activity);
	public View renderView(Activity activity);
}
