package com.riverspart.gcm;

import android.content.Context;

import com.google.android.gcm.GCMBroadcastReceiver;

/**
 * This class override getGCMItentServiceClassName method in GCMBroadcastReceiver
 * This is required because name of GCMIntentService has been changed to
 * com.riverspart.gcm.GCMIntentService then we need use this Receiver instead of default one 
 * to specify our new IntentService
 * @author nguyenvantuanrs
 *
 */
public class GCMReceiver extends GCMBroadcastReceiver {

	protected String getGCMIntentServiceClassName(Context context) { 
		return "com.riverspart.gcm.GCMIntentService"; 
	}
}
