package com.riverspart.data;

import com.riverspart.RSPortal;

public interface RSPortalControlPackage {
	public Class<RSPortal> getSenderPortal();
	public void setSenderPortal(Class<RSPortal> portal);
	public int getSenderState();
	public void setSenderState(int state);
	
	public static final int STATE_READY = 1;
	public static final int STATE_ERROR = -1;
	public static final int STATE_WAITING = 0;
	public static final int STATE_NOT_AVAILABLE = -2;
	public static final int STATE_FINISH = -4;
}
