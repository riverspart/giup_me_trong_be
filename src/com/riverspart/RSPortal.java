package com.riverspart;


import com.riverspart.data.RSPortalControlPackage;
import com.riverspart.storage.RSFileStorable;


public interface RSPortal extends RSFileStorable {
	public RSPortalControlPackage getIntentPassControlPackage();
	public void route();
}
