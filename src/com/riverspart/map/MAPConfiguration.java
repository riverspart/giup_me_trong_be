package com.riverspart.map;

import com.riverspart.RSConfiguration;
import com.riverspart.recover.RSDefaultRecoverable;
import com.riverspart.storage.RSFileStorable;
import com.riverspart.storage.RSLazyStorable;

public interface MAPConfiguration extends RSConfiguration, RSDefaultRecoverable, RSFileStorable, RSLazyStorable {
	
	public int getDefaultMapType();
	public boolean getDisplayMySharing();
	public boolean getAlertOnNotifier();
	public int getMarkerAppearanceType();
}
