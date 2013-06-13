package com.riverspart.map;

import com.riverspart.storage.RSLazyNFileStorableImpl;

public abstract class MAPConfigurationImpl extends RSLazyNFileStorableImpl implements MAPConfiguration {

	protected int defaultMapType;
	protected boolean displayMySharing;
	protected boolean alertOnNotifier;
	protected int markerAppearanceType;

	@Override
	public int getDefaultMapType(){
		return defaultMapType;
	}
	
	@Override
	public boolean getDisplayMySharing(){
		return displayMySharing;
	}
	
	@Override
	public boolean getAlertOnNotifier(){
		return alertOnNotifier;
	}
	
	@Override
	public int getMarkerAppearanceType(){
		return markerAppearanceType;
	}
}
