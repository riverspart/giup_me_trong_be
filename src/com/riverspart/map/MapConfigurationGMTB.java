package com.riverspart.map;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.riverspart.nurse.R;

public class MapConfigurationGMTB extends MAPConfigurationImpl{

	@Override
	public ViewGroup renderLayout(Activity activity) {
		ViewGroup v = (ViewGroup) activity.getLayoutInflater().inflate(R.layout.map_configuration, null);
		return v;
	}

	@Override
	public View renderView(Activity activity) {
		ViewGroup layout = renderLayout(activity);
		View v = layout.findViewById(R.id.map_configuration_layout);
		layout.removeView(v);
		return v;
	}

	@Override
	public void loadConfiguration() {
		// TODO Auto-generated method stub
	}

	@Override
	protected void updateLayout() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void saveLayout() {
		// TODO Auto-generated method stub
		
	}
	
}
