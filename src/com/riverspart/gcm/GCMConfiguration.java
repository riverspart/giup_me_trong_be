package com.riverspart.gcm;

import android.content.Context;
import android.content.Intent;

import com.riverspart.RSConfiguration;
import com.riverspart.recover.RSDefaultRecoverable;
import com.riverspart.storage.RSFileStorable;
import com.riverspart.storage.RSLazyStorable;

public interface GCMConfiguration extends RSConfiguration, RSDefaultRecoverable, RSFileStorable, RSLazyStorable {
	public String getServer_url();
	public String getSender_id();
	
	/**
     * Tag used on log messages.
     */
	public  String TAG = "GCMPortal";
	
	
	/**
     * Intent used to display a message in the screen.
     */
    static final String DISPLAY_MESSAGE_ACTION =
            "com.riverspart.gcm.DISPLAY_MESSAGE";
    /**
     * Intent's extra that contains the message to be displayed.
     */
    static final String EXTRA_MESSAGE = "message";
    
	/**
     * Intent used to display a message in the screen.
     */
    static final String PASS_VICTIM_DATA_ACTION =
            "com.riverspart.gcm.PASS_LOCATION_DATA";
    
    /**
     * Intent's extra that contains the message to be displayed.
     */
    static final String VICTIM_DATA = "location_data";
    
    /**
     * Id of this device provided after registered on server
     */
    public String getRegId();
    public void setRegId(String id);
    
    public int getBackoff_milli_seconds();
    public void setBackoff_milli_seconds(int seconds);
    
    public int getMax_attemp();
    public void setMax_attemp(int max);
    
    /**
     * Notifies UI to display a message.
     * <p>
     * This method is defined in the common helper because it's used both by
     * the UI and the background service.
     *
     * @param context application's context.
     * @param message message to be displayed.
     */
    public void displayMessage(Context context, String message);
    public void passLocationData(Context context, Intent intent);
}
