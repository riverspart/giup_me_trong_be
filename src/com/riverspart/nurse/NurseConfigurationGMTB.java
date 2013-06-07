package com.riverspart.nurse;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

import com.riverspart.data.RSPortalControlPackage;
import com.riverspart.gcm.GCMConfigurationGMTB;
import com.riverspart.gps.GPSConfigurationGMTB;
import com.riverspart.map.MapConfigurationGMTB;



public class NurseConfigurationGMTB extends NurseConfigurationImpl {

	
	 @Override
	 public void onCreate(Bundle savedInstanceState) {
		  super.onCreate(savedInstanceState);
		 
		  Resources res = getResources(); // Resource object to get Drawables
		  TabHost tabHost = getTabHost(); // The activity TabHost
		  TabHost.TabSpec spec; // Reusable TabSpec for each tab
		  Intent intent; // Reusable Intent for each tab
		
		  // Create an Intent to launch an Activity for the tab (to be reused)
		  intent = new Intent().setClass(this, GCMConfigurationGMTB.class);
		  spec = tabHost.newTabSpec("server")
				  .setIndicator(getString(R.string.nurse_configuration_tabtitle_gcm), res.getDrawable(R.drawable.ic_tab_server))
				  .setContent(intent);
		  tabHost.addTab(spec);
		
		  // Do the same for the other tabs
		
		  intent = new Intent().setClass(this, MapConfigurationGMTB.class);
		  spec = tabHost.newTabSpec("map")
				  .setIndicator(getString(R.string.nurse_configuration_tabtitle_map), res.getDrawable(R.drawable.ic_tab_map))
				  .setContent(intent);
		  tabHost.addTab(spec);
		  
		  
		  intent = new Intent().setClass(this, GPSConfigurationGMTB.class);
		  spec = tabHost.newTabSpec("gps")
				  .setIndicator(getString(R.string.nurse_configuration_tabtitle_gps), res.getDrawable(R.drawable.ic_tab_gps))
				  .setContent(intent);
		  tabHost.addTab(spec);
		  
		  //set tab which one you want open first time 0 or 1 or 2
		  tabHost.setCurrentTab(0);
	  
	}
	
	@Override
	public void finish() {
		//pass result to NursePortalGMTB
		setResult(RSPortalControlPackage.STATE_READY);
		super.finish();
	}
	 
	@Override
	public void loadConfiguration() {
		// TODO Auto-generated method stub
	}

	@Override
	public ViewGroup renderLayout(Activity activity) {
		
		ViewGroup nurseLayout = (ViewGroup) activity.getLayoutInflater().inflate(R.layout.nurse_configuration, null);
		return nurseLayout;
	}

	@Override
	public View renderView(Activity activity) {
		
		ViewGroup layout = renderLayout(activity);
		View v = layout.findViewById(android.R.id.tabhost);
		layout.removeView(v);
		return v;
	}

	
}
