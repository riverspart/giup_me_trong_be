package com.riverspart;

import android.os.Bundle;

import com.riverspart.storage.RSStorableImpl;

public abstract class RSNonActivityInitialImpl extends RSStorableImpl implements RSNonActivityInitial {

	public RSNonActivityInitialImpl(){
		super();
		if(checkNonActivityCondition()) {
			localInitial();
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		localInitial();
	}
	
	@Override
	public abstract void localInitial();

	@Override
	public abstract boolean checkNonActivityCondition();

}
