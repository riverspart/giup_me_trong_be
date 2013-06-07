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

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gcm.GCMRegistrar;
import com.riverspart.data.RSPortalControlPackage;
import com.riverspart.data.RSPortalControlPackageGMTB;
import com.riverspart.gps.GPSConfigurationGMTB;
import com.riverspart.gps.GPSSessionGMTB;
import com.riverspart.nurse.NursePortalGMTB;
import com.riverspart.nurse.R;

/**
 * Main UI for the demo app.
 */
public class GCMPortalGMTB extends GCMPortalImpl {

    TextView mDisplay;
    AsyncTask<Void, Void, Void> mRegisterTask;
    GCMConfiguration configuration;
    
    //private Bundle routingTable = new Bundle();
    public static final int CHECK_CONFIGURATION_LOADED = 1;
	
	public static final int CONFIGURATION_LOADED_YES = 1;
	public static final int CONFIGURATION_LOADED_NO = 0;
	
	private Activity getActivity() {
		return this;
	}
	private Context getContext() {
		return getApplicationContext();
	}
	private void setBackground() {
		ImageView radarImage = (ImageView) findViewById(R.id.gcm_radarbg);
		radarImage.setBackgroundResource(R.anim.radar);

		AnimationDrawable radartAnimation = (AnimationDrawable) radarImage.getBackground();
		radartAnimation.start();
	}
	
	@Override
	public void route() {
		
		/**
		 * Update to global session for sharing to GCMIntentService
		 */		
			if(this.configuration == null) {
				class Config extends GCMConfigurationGMTB {
					@Override
					public Activity getCurrentActivity() {
						return getActivity();
					}
					@Override
					public Context getCurrentContext() {
						return getContext();
					}
				} 
				this.configuration = new Config();
			}
			this.configuration.retrieve();
			GCMSessionGMTB.currentGCMConfiguration = configuration;
			
		/**
		 * Update Current live GPS Configuration to global session
		 */
			if(GPSSessionGMTB.currentGPSConfiguration == null) {
				class ConfigGPS extends GPSConfigurationGMTB {
					@Override
					public Activity getCurrentActivity() {
						return getActivity();
					}
					@Override
					public Context getCurrentContext() {
						return getContext();
					}
				} 
				GPSSessionGMTB.currentGPSConfiguration = new ConfigGPS();
			}
			
			GPSSessionGMTB.currentGPSConfiguration.retrieve();
			

		initialGcm();

	}
    
    protected void onActivityResult(int requestCode, int resultCode,
            Intent data) {
        if (requestCode == CHECK_CONFIGURATION_LOADED) {
            //this.routingTable.putInt(CHECK_CONFIGURATION_LOADED_ROUTING_INDEX, resultCode);
        }
        //this.route();
    }
    
    private void initialGcm() {
    	// Make sure the device has the proper dependencies.
        GCMRegistrar.checkDevice(this);
        // Make sure the manifest was properly set - comment out this line
        // while developing the app, then uncomment it when it's ready.
        GCMRegistrar.checkManifest(this);
        //if(GCMSessionGMTB.currentGCMConfiguration == null) {
	    //    try{
	        	registerReceiver(mHandleMessageReceiver,
	                new IntentFilter(GCMConfiguration.DISPLAY_MESSAGE_ACTION));
	    //    } catch(Exception e) {
	    //    	e.printStackTrace();
	    //    }
        //}
        
        //Update regId if regId has been refreshed
        String regId = GCMRegistrar.getRegistrationId(this);
        if(!this.configuration.getRegId().equals(regId)) {
        	this.configuration.setRegId(regId);
        	this.configuration.store();
        }
        
        if (this.configuration.getRegId().equals("")) {
            // Automatically registers application on startup.
            GCMRegistrar.register(getApplicationContext(), this.configuration.getSender_id());
            GCMRegistrar.setRegisterOnServerLifespan(getApplicationContext(), GCMRegistrar.DEFAULT_ON_SERVER_LIFESPAN_MS);
        } else {
            // Device is already registered on GCM, check server.
            if (GCMRegistrar.isRegisteredOnServer(this)) {
                // Skips registration.
            	
                mDisplay.append(getString(R.string.gcm_already_registered) + "\n");
                this.finishSession(RSPortalControlPackage.STATE_READY);
                
            } else {
                // Try to register again, but not in the UI thread.
                // It's also necessary to cancel the thread onDestroy(),
                // hence the use of AsyncTask instead of a raw thread.
                final Context context = this;
                mRegisterTask = new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... params) {
                        boolean registered =
                                ServerUtilities.register(context, configuration);
                        // At this point all attempts to register with the app
                        // server failed, so we need to unregister the device
                        // from GCM - the app will try to register again when
                        // it is restarted. Note that GCM will send an
                        // unregistered callback upon completion, but
                        // GCMIntentService.onUnregistered() will ignore it.
                        if (!registered) {
                            GCMRegistrar.unregister(context);
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        mRegisterTask = null;
                    }

                };
                mRegisterTask.execute(null, null, null);
            }
        }
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gcm_main);
        setBackground();
        mDisplay = (TextView) findViewById(R.id.display);
    }

    
    @Override
	protected void onStart() {
		super.onStart();
        this.finishSession(RSPortalControlPackage.STATE_READY);
	}

    private void finishSession(int status) {
    	
    	RSPortalControlPackage controlPackage = 
        		new RSPortalControlPackageGMTB(
        				this.getClass().getName(), 
        				status
				);
    	Intent currentIntent = this.getIntent();
    	NursePortalGMTB.passIntentControlPackage(currentIntent, controlPackage);
    	
    	this.setResult(controlPackage.getSenderState(), currentIntent);
    	//finish();
    }
    
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.gcm_options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            /*
             * Typically, an application registers automatically, so options
             * below are disabled. Uncomment them if you want to manually
             * register or unregister the device (you will also need to
             * uncomment the equivalent options on options_menu.xml).
             */
            case R.id.options_register:
                GCMRegistrar.register(this, this.configuration.getSender_id());
                return true;
            case R.id.options_unregister:
                GCMRegistrar.unregister(this);
                return true;
            case R.id.options_clear:
                mDisplay.setText(null);
                return true;
            case R.id.options_exit:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onDestroy() {
    	try{
        if (mRegisterTask != null) {
            mRegisterTask.cancel(true);
        }
        unregisterReceiver(mHandleMessageReceiver);
        GCMRegistrar.onDestroy(this);
    	}catch(Exception ex){}
        super.onDestroy();
    }

    //Get info from broadcast service that intent received data from server
    // then make broadcast signal
    private final BroadcastReceiver mHandleMessageReceiver =
            new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        	String newMessage = "";
	        newMessage = intent.getExtras().getString(GCMConfiguration.EXTRA_MESSAGE);
	        mDisplay.append(newMessage + "\n");
	        
	        decodeMessageToAction(newMessage);
        }
    };
    
    private void decodeMessageToAction(String message) {
//    	String a1 = getString(R.string.gcm_server_registering);
//    	String a2 = getString(R.string.gcm_server_registered);
//    	String a3 = getString(R.string.gcm_server_register_error);
//    	String a4 = getString(R.string.gcm_server_unregistered);
    	
    	if(message.equals(getString(R.string.gcm_server_registering))) {
    		setResult(RSPortalControlPackage.STATE_WAITING);
    	} else if(message.equals(getString(R.string.gcm_server_registered))) {
    		setResult(RSPortalControlPackage.STATE_READY);
    		finish();
    	} else if(message.equals(getString(R.string.gcm_server_register_error))) {
    		setResult(RSPortalControlPackage.STATE_ERROR);
    	} else if(message.equals(getString(R.string.gcm_server_unregistered))) {
    		setResult(RSPortalControlPackage.STATE_WAITING);
    	}
    	
    }

	public static final String GCMPORTAL_ROUTE_DATA_FILE = "com.riverspart.gcm.gcmportalgmtb.routetable_data.dat";
	public static final String START_FILE_OK_COMMIT = "gcmportal_routetable_data_config_start";
	public static final String END_FILE_OK_COMMIT = "gcmportal_routetable_data_config_end";
	
	@Override
	public String getFileName() {
		// TODO Auto-generated method stub
		return GCMPORTAL_ROUTE_DATA_FILE;
	}
	@Override
	public void importFile(FileReader fileReader) throws IOException {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void exportFile(FileWriter fileWriter) throws IOException {
		// TODO Auto-generated method stub
		
	}

}