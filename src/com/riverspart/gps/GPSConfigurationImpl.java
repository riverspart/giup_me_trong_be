package com.riverspart.gps;

import com.riverspart.storage.RSLazyNFileStorableImpl;


public abstract class GPSConfigurationImpl extends RSLazyNFileStorableImpl implements GPSConfiguration {
	
	protected String deviceAlias;
	protected float distanceUpdate;
	protected float timeUpdate;
	protected int updateType;
	protected boolean allowShareLocation;
	
	@Override
	public String getDeviceAlias() {
		return deviceAlias;
	}

	@Override
	public float getDistanceUpdate() {
		return distanceUpdate;
	}

	@Override
	public boolean getAllowShareLocation() {
		return allowShareLocation;
	}

	@Override
	public float getTimeUpdate() {
		return timeUpdate;
	}

	@Override
	public int getUpdateType() {
		return updateType;
	}
}
