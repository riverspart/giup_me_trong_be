package com.riverspart.map;

import com.riverspart.data.RSPortalControlPackage;
import com.riverspart.data.RSPortalControlPackageGMTB;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;

public abstract class MapPortalImpl extends ListActivity implements MapPortal {
	
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
	
}
