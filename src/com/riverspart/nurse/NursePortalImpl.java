package com.riverspart.nurse;

import com.riverspart.RSPortalImpl;
import com.riverspart.gcm.GCMSessionGMTB;
import com.riverspart.gps.GPSSessionGMTB;

public abstract class NursePortalImpl extends RSPortalImpl implements NursePortal {
	public boolean checkSession() {
		return (GCMSessionGMTB.currentGCMConfiguration != null) && (GPSSessionGMTB.currentGPSConfiguration != null);
	}
}
