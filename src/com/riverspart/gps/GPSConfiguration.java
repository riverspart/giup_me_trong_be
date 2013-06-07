package com.riverspart.gps;

import com.riverspart.RSConfiguration;
import com.riverspart.recover.RSDefaultRecoverable;
import com.riverspart.storage.RSFileStorable;
import com.riverspart.storage.RSLazyStorable;

public interface GPSConfiguration extends RSConfiguration, RSDefaultRecoverable, RSFileStorable, RSLazyStorable {
	public String getDeviceAlias();
	public float getDistanceUpdate();
	public float getTimeUpdate();
	public int getUpdateType();
	public boolean getAllowShareLocation();
}
