package com.riverspart.data;

import com.riverspart.RSPortal;

public class RSPortalControlPackageGMTB implements RSPortalControlPackage {

	public RSPortalControlPackageGMTB(){this.sender = null; this.state = 1;};
	
	public RSPortalControlPackageGMTB(Class<RSPortal> sender, int state) {
		this.sender = sender;
		this.state = state;
	}
	
	public RSPortalControlPackageGMTB(String senderStr, int state) {
		try{
			@SuppressWarnings("unchecked")
			Class<RSPortal> sender = (Class<RSPortal>)(Class.forName(senderStr));
		    this.sender = sender;
		    this.state = state;
		}catch(Exception ex) {
			
		}
	}
	
	private Class<RSPortal> sender;
	private int state;
	
	@Override
	public Class<RSPortal> getSenderPortal() {
		return this.sender;
	}

	@Override
	public void setSenderPortal(Class<RSPortal> portal) {
		this.sender = portal;
	}

	@Override
	public int getSenderState() {
		return this.state;
	}

	@Override
	public void setSenderState(int state) {
		this.state = state;
	}

}
