package com.riverspart.gcm;

import android.content.Context;
import android.content.Intent;

import com.riverspart.storage.RSLazyNFileStorableImpl;


public abstract class GCMConfigurationImpl extends RSLazyNFileStorableImpl implements GCMConfiguration {


	/**
     * Base URL of the Demo Server (such as http://my_host:8080/gcm-demo)
     */
	protected String server_url;
	
	public String getServer_url() {
		return this.server_url;
	}
	public void setServer_url(String url) {
		this.server_url = url;
	}
	
	/**
     * Google API project id registered to use GCM.
     */
	protected String sender_id;
	public String getSender_id() {
		return this.sender_id;
	}
	public void setSender_id(String id) {
		this.sender_id = id;
	}
	
	protected int max_attemp;
	public int getMax_attemp() {
		return this.max_attemp;
	}
	public void setMax_attemp(int max) {
		this.max_attemp = max;
	}
	
	protected int backoff_milli_seconds;
	public int getBackoff_milli_seconds() {
		return this.backoff_milli_seconds;
	}
	public void setBackoff_milli_seconds(int seconds) {
		this.backoff_milli_seconds = seconds;
	}
	
	protected String regId;
	@Override
	public String getRegId() {
		return this.regId;
	}
	@Override
	public void setRegId(String id) {
		this.regId = id;
	}
	
	/**
     * Notifies UI to display a message.
     * <p>
     * This method is defined in the common helper because it's used both by
     * the UI and the background service.
     *
     * @param context application's context.
     * @param message message to be displayed.
     */
	@Override
    public void displayMessage(Context context, String message) {
		
        Intent intent = new Intent(GCMConfiguration.DISPLAY_MESSAGE_ACTION);
        intent.putExtra(GCMConfiguration.EXTRA_MESSAGE, message);
        context.sendBroadcast(intent);
    }
	
	@Override
	public void passLocationData(Context context, Intent intent) {

		Intent bcintent = new Intent(GCMConfiguration.PASS_VICTIM_DATA_ACTION);
        bcintent.putExtras(intent);
        context.sendBroadcast(bcintent);
	}
}
