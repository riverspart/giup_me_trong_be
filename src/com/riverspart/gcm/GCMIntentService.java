/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.riverspart.gcm;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMRegistrar;
import com.riverspart.data.RSLocationData;
import com.riverspart.map.MapSessionGMTB;
import com.riverspart.nurse.R;

/**
 * IntentService responsible for handling GCM messages.
 */
public class GCMIntentService extends GCMBaseIntentService {

    @SuppressWarnings("hiding")
    private static final String TAG = "com.riverspart.gcm.GCMIntentService";
    private GCMConfiguration configuration;
    
    public GCMIntentService() {
        super();
		this.configuration = GCMSessionGMTB.currentGCMConfiguration;
    }

    protected String[] getSenderIds(Context context) {
		return new String[]{this.configuration.getSender_id()};
    	
    }
    
    @Override
    protected void onRegistered(Context context, String registrationId) {
        Log.i(TAG, "Device registered: regId = " + registrationId);
        this.configuration.displayMessage(context, getString(R.string.gcm_gcm_registered));
        this.configuration.setRegId(registrationId);
        ServerUtilities.register(context, this.configuration);
    }

    @Override
    protected void onUnregistered(Context context, String registrationId) {
        Log.i(TAG, "Device unregistered");
        this.configuration.displayMessage(context, getString(R.string.gcm_gcm_unregistered));
        if (GCMRegistrar.isRegisteredOnServer(context)) {
            ServerUtilities.unregister(context, this.configuration);
        } else {
            // This callback results from the call to unregister made on
            // ServerUtilities when the registration to the server failed.
            Log.i(TAG, "Ignoring unregister callback");
        }
    }

    @Override
    protected void onMessage(Context context, Intent intent) {

    	if(
    			!MapSessionGMTB.currentMapConfiguration.getDisplayMySharing() 
    			&& GCMSessionGMTB.currentGCMConfiguration.getRegId().equals(intent.getExtras().getString(RSLocationData.ID))
		) {
    		return;
    	}
        Log.i(TAG, "Received message");
        String message = intent.getExtras().getString(RSLocationData.TITLE);
        message += ": " + intent.getExtras().getString(RSLocationData.LONGTITUDE);
        message += " * " + intent.getExtras().getString(RSLocationData.LATITUDE);
        
        Date date = new Date(Long.parseLong(intent.getExtras().getString(RSLocationData.TIME)));
        SimpleDateFormat sDateFormat = new SimpleDateFormat("HH:mm:ss a - dd/MM/yyyy");
        sDateFormat.setTimeZone(TimeZone.getDefault());
        message += " (" + sDateFormat.format(date) + ")";
        if(this.configuration!=null) {
	        this.configuration.displayMessage(context, message);
	        //Pass victim data to map
	        this.configuration.passLocationData(context, intent);
        }
        // notifies user
        if(MapSessionGMTB.currentMapConfiguration.getAlertOnNotifier()) {
        	generateNotification(context, message);
    	}
    }

    @Override
    protected void onDeletedMessages(Context context, int total) {
        Log.i(TAG, "Received deleted messages notification");
        String message = getString(R.string.gcm_gcm_deleted, total);
        this.configuration.displayMessage(context, message);
        // notifies user
        generateNotification(context, message);
    }

    @Override
    public void onError(Context context, String errorId) {
        Log.i(TAG, "Received error: " + errorId);
        this.configuration.displayMessage(context, getString(R.string.gcm_gcm_error, errorId));
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        // log message
        Log.i(TAG, "Received recoverable error: " + errorId);
        this.configuration.displayMessage(context, getString(R.string.gcm_gcm_recoverable_error,
                errorId));
        return super.onRecoverableError(context, errorId);
    }

    /**
     * Issues a notification to inform the user that server has sent a message.
     */
    private static void generateNotification(Context context, String message) {
        int icon = R.drawable.ic_stat_gcm;
        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification(icon, message, when);
        String title = context.getString(R.string.gcm_notify_title);
        
        Intent notificationIntent = new Intent(context, com.riverspart.gcm.GCMPortalGMTB.class);
        
        // set intent so it does not start a new activity
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent =
                PendingIntent.getActivity(context, 0, notificationIntent, 0);
        notification.setLatestEventInfo(context, title, message, intent);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(0, notification);
    }

}
