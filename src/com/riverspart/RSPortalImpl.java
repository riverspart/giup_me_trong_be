package com.riverspart;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.riverspart.data.RSPortalControlPackage;
import com.riverspart.data.RSPortalControlPackageGMTB;
import com.riverspart.storage.RSFileStorableImpl;

public abstract class RSPortalImpl extends RSFileStorableImpl implements RSPortal {

	@Override
	public RSPortalControlPackage getIntentPassControlPackage() {
		try{
			Bundle extras = getIntent().getExtras();
			if (extras != null) {
				String senderStr = extras.getString("sender_portal", "");
				if(senderStr!="")
					return new RSPortalControlPackageGMTB(senderStr, extras.getInt("sender_state"));
			}
		}catch(Exception ex) {
			
		}
		return null;
	}
	
	public static void passIntentControlPackage(Intent intent, RSPortalControlPackage controlPackage) {
		intent.putExtra("sender_portal", controlPackage.getSenderPortal().getClass().getName());
		intent.putExtra("sender_state", controlPackage.getSenderState());
	}

	@Override
	protected void onResume() {
		super.onResume();
		route();
	}

	
	
	@Override
	protected void onStop() {
		super.onStop();
		onDestroy();
	}

	@Override
	public abstract void route();


	/***********************************
	 * 
	 * Omitted methods
	 */
	@Override
	public ViewGroup renderLayout(Activity activity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void updateLayout() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void saveLayout() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public View renderView(Activity activity) {
		// TODO Auto-generated method stub
		return null;
	}
}
 