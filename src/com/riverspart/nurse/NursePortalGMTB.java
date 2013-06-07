/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.riverspart.nurse;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.riverspart.data.RSPortalControlPackage;
import com.riverspart.gcm.GCMPortalGMTB;
import com.riverspart.gps.GPSPortalGMTB;
import com.riverspart.gps.GPSSessionGMTB;
import com.riverspart.map.MapPortalGMTB;


/**
 * The main activity of the API library demo gallery.
 * <p>
 * The main layout lists the demonstrated features, with buttons to launch them.
 */
public final class NursePortalGMTB extends NursePortalImpl {

	public static final int CHECK_GCM_CONNECTION_CODE = 1;
	public static final int CHECK_GPS_CONNECTION_CODE = 2;
	public static final int CHECK_MAP_CONNECTION_CODE = 3;
	public static final int CHECK_CONFIGURATION_CODE = 4;
	
	public static final int CONFIGURATION_ROUTING_YES = 5;
	public static final int CONFIGURATION_ROUTING_NO = 6;
	public static final int GCM_ROUTING_YES = 7;
	public static final int GCM_ROUTING_NO = 8;
	
	public static final String CONFIGURATION_ROUTING_INDEX = "congiguration_nurse";
	public static final String GCM_ROUTING_INDEX = "gcm_portal";
	public static final String GPS_ROUTING_INDEX = "gps_portal";
	public static final String MAP_ROUTING_INDEX = "map_portal";
	public Bundle routingTable = new Bundle();
	
	public static final String CHECK_FIRST_RUN_INDEX_PASS_INTENT = "com.riverspart.nurse.nurseportal.check_first_run";
	public static final boolean checkFirstRun(Activity activity) {
		try {
			return activity.getIntent().getExtras().getBoolean(CHECK_FIRST_RUN_INDEX_PASS_INTENT);
		} catch(Exception e) { e.printStackTrace(); }
		return false;
	}
	public static final void setFirstRun(Intent intent, boolean state) {
		intent.putExtra(CHECK_FIRST_RUN_INDEX_PASS_INTENT, state);
	}
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nurse_main);
        //this.initRoutingTable();
    }
    
    public void onLayoutClick(View v) {
    	this.routingTable.putInt(MAP_ROUTING_INDEX, RSPortalControlPackage.STATE_READY);
    	onResume();
    }

//	private void initRoutingTable() {
//		this.routingTable.putInt(NursePortalGMTB.FIRST_LAUNCH_INDEX, NursePortalGMTB.FIRST_LAUNCH_YES);
//		this.routingTable.putInt(NursePortalGMTB.GCM_ROUTING_INDEX, RSPortalControlPackage.STATE_NOT_AVAILABLE);
//		this.routingTable.putInt(NursePortalGMTB.GPS_ROUTING_INDEX, RSPortalControlPackage.STATE_NOT_AVAILABLE);
//		this.routingTable.putInt(NursePortalGMTB.MAP_ROUTING_INDEX, RSPortalControlPackage.STATE_NOT_AVAILABLE);
//	}
	
    private boolean isOnline() {
        ConnectivityManager cm =
            (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }
    
    private boolean firstStartGps = true;
	@Override
	public void route() {
		if(this.routingTable.getInt(CONFIGURATION_ROUTING_INDEX, RSPortalControlPackage.STATE_NOT_AVAILABLE) != RSPortalControlPackage.STATE_READY){
			Intent nurseConfigIntent = new Intent(this, NurseConfigurationGMTB.class);
	        this.startActivityForResult(nurseConfigIntent, NursePortalGMTB.CHECK_CONFIGURATION_CODE);
		} else if(!isOnline()) {
			Toast.makeText(getApplicationContext(), R.string.nurse_no_internet_message, Toast.LENGTH_LONG).show();
			setContentView(R.layout.nurse_no_internet);
		} else if((this.routingTable.getInt(GCM_ROUTING_INDEX, RSPortalControlPackage.STATE_NOT_AVAILABLE) != RSPortalControlPackage.STATE_READY) || !checkSession()) {
			/**
	         * Activate GCM Portal
	         */
	        Intent gcmIntent = new Intent(this, GCMPortalGMTB.class);
	        this.startActivityForResult(gcmIntent, NursePortalGMTB.CHECK_GCM_CONNECTION_CODE);
		} else if(
				//GPSSessionGMTB has been initialized when activated GCMPortal
				GPSSessionGMTB.currentGPSConfiguration.getAllowShareLocation()
				
				// Need reactivate per starting application time due to be disable by some reasons
				// Our GPS receiver is binded to system provider in GPSPortalGMTB
				// not independently started!
				&& (
						this.routingTable.getInt(NursePortalGMTB.GPS_ROUTING_INDEX, RSPortalControlPackage.STATE_NOT_AVAILABLE) != RSPortalControlPackage.STATE_READY
						|| firstStartGps
				)
		) {
			firstStartGps = false;
	        /**
	         * Activate GPS Portal
	         */
	        Intent gpsIntent = new Intent(this, GPSPortalGMTB.class);
	        this.startActivityForResult(gpsIntent, NursePortalGMTB.CHECK_GPS_CONNECTION_CODE);
		}else if(this.routingTable.getInt(MAP_ROUTING_INDEX, RSPortalControlPackage.STATE_NOT_AVAILABLE)!= RSPortalControlPackage.STATE_FINISH) {
			startMap();
		}     

	}
	
	private void startMap() {
		/**
         * Activate Map Portal
         */
        Intent mapIntent = new Intent(this, MapPortalGMTB.class);
        this.startActivityForResult(mapIntent, CHECK_MAP_CONNECTION_CODE);
	}
	
	protected void onActivityResult(int requestCode, int resultCode,
            Intent data) {
        if (requestCode == NursePortalGMTB.CHECK_GCM_CONNECTION_CODE) {
        	
            this.routingTable.putInt(GCM_ROUTING_INDEX, resultCode);
            if(resultCode == RSPortalControlPackage.STATE_ERROR) {
            	
            	this.routingTable.putInt(CONFIGURATION_ROUTING_INDEX, RSPortalControlPackage.STATE_NOT_AVAILABLE);
            	this.routingTable.putInt(GPS_ROUTING_INDEX, RSPortalControlPackage.STATE_NOT_AVAILABLE);
            }
        } else if (requestCode == NursePortalGMTB.CHECK_GPS_CONNECTION_CODE) {
        	
        	this.routingTable.putInt(GPS_ROUTING_INDEX, resultCode);
    		if(resultCode == RSPortalControlPackage.STATE_WAITING) {
            	
            	this.routingTable.putInt(CONFIGURATION_ROUTING_INDEX, RSPortalControlPackage.STATE_NOT_AVAILABLE);
            }
        } else if (requestCode == NursePortalGMTB.CHECK_CONFIGURATION_CODE) {
        	
        	this.routingTable.putInt(CONFIGURATION_ROUTING_INDEX, resultCode);
        	//Need rerun GCM vs GPS after config changed
        	//if(resultCode != RSPortalControlPackage.STATE_READY) {
        		
        		this.routingTable.putInt(GCM_ROUTING_INDEX, RSPortalControlPackage.STATE_NOT_AVAILABLE);
        		this.routingTable.putInt(GPS_ROUTING_INDEX, RSPortalControlPackage.STATE_NOT_AVAILABLE);
        	//}
        } else if (requestCode == NursePortalGMTB.CHECK_MAP_CONNECTION_CODE) {
        	this.routingTable.putInt(MAP_ROUTING_INDEX, resultCode);
        }
        if(resultCode == CONFIGURATION_ROUTING_NO) {
    		this.routingTable.putInt(CONFIGURATION_ROUTING_INDEX, RSPortalControlPackage.STATE_NOT_AVAILABLE);
    	} else if(resultCode == GCM_ROUTING_NO) {
    		this.routingTable.putInt(GCM_ROUTING_INDEX, RSPortalControlPackage.STATE_NOT_AVAILABLE);
    	}
        
        store();
        /**
         * After finish, auto resume this activity
         * no need to manually call route here
         */
        //this.route();
    }
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.map_main_menu, menu);
        return true;
    }
	
	private final int SELF_RESULT = -100;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
        	case R.id.map_main_menu_config:
        		onActivityResult(SELF_RESULT, NursePortalGMTB.CONFIGURATION_ROUTING_NO, null);
        		onResume();
        		return true;
            case R.id.map_main_menu_gcm:
            	onActivityResult(SELF_RESULT, NursePortalGMTB.GCM_ROUTING_NO, null);
            	onResume();
                return true;
            case R.id.map_main_menu_map:
            	onActivityResult(CHECK_MAP_CONNECTION_CODE, RSPortalControlPackage.STATE_WAITING, null);
            	onResume();
                return true;
            case R.id.map_main_menu_quit:
//            	onActivityResult(SELF_RESULT, RSPortalControlPackage.STATE_FINISH, null);
//            	onResume();
            	finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

	public static final String NURSEPORTAL_ROUTE_DATA_FILE = "com.riverspart.nurse.nurseportalgmtb.routetable_data.dat";
	public static final String START_FILE_OK_COMMIT = "routetable_data_config_start";
	public static final String END_FILE_OK_COMMIT = "routetable_data_config_end";
	
	@Override
	public String getFileName() {
		return NURSEPORTAL_ROUTE_DATA_FILE;
	}

	@Override
	public void importFile(FileReader fileReader) throws IOException {
		BufferedReader in = new BufferedReader(fileReader);
		String commit = in.readLine();
		if(commit.equals(START_FILE_OK_COMMIT)) {
			String tmpGcmRoute = in.readLine();
			String tmpConfigRoute = in.readLine();
			String tmpGpsRoute = in.readLine();
			String tmpMapRoute = in.readLine();
			commit = in.readLine();
			if(commit.equals(END_FILE_OK_COMMIT)) {
				
				try{
					
					routingTable.putInt(GCM_ROUTING_INDEX, Integer.parseInt(tmpGcmRoute));
					routingTable.putInt(CONFIGURATION_ROUTING_INDEX, Integer.parseInt(tmpConfigRoute));
					routingTable.putInt(GPS_ROUTING_INDEX, Integer.parseInt(tmpGpsRoute));
					routingTable.putInt(MAP_ROUTING_INDEX, Integer.parseInt(tmpMapRoute));
				}catch(Exception e) { 
					e.printStackTrace(); 
				}
			}
		}
		in.close();
	}

	@Override
	public void exportFile(FileWriter fileWriter) throws IOException {
		BufferedWriter out = new BufferedWriter(fileWriter);
		out.write(START_FILE_OK_COMMIT);
		out.newLine();
		out.write(String.valueOf(routingTable.getInt(GCM_ROUTING_INDEX)));
		out.newLine();
		out.write(String.valueOf(routingTable.getInt(CONFIGURATION_ROUTING_INDEX)));
		out.newLine();
		out.write(String.valueOf(routingTable.getInt(GPS_ROUTING_INDEX)));
		out.newLine();
		out.write(String.valueOf(routingTable.getInt(MAP_ROUTING_INDEX)));
		out.newLine();
		out.write(END_FILE_OK_COMMIT);
		out.close();
	}
}
