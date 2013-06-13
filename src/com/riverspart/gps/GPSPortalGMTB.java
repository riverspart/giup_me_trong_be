package com.riverspart.gps;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.widget.Toast;

import com.riverspart.data.RSPortalControlPackage;
import com.riverspart.data.RSPortalControlPackageGMTB;
import com.riverspart.nurse.NursePortalGMTB;
import com.riverspart.nurse.R;

public class GPSPortalGMTB extends GPSPortalImpl {

	private void finishSession(int state) {
		RSPortalControlPackage controlPackage = 
        		new RSPortalControlPackageGMTB(
    				this.getClass().getName(), 
    				state
				);
		
    	Intent currentIntent = this.getIntent();
    	NursePortalGMTB.passIntentControlPackage(currentIntent, controlPackage);
    	this.setResult(controlPackage.getSenderState(), currentIntent);
    	finish();
    }

	public static final int GPS_ENABLE_CHECK_CODE = 100;
	@Override
	public void route() {

		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	    LocationListener ll = new GPSLocationListener();
	    try {
	    	// Bind System Location provider to LocationListener
	    	if(lm.isProviderEnabled (LocationManager.GPS_PROVIDER)) {
	    		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, ll);
	    	} else if(lm.isProviderEnabled (LocationManager.NETWORK_PROVIDER)) {
	    		lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, ll);
	    	}
//	    	else if(lm.isProviderEnabled (LocationManager.PASSIVE_PROVIDER))
//	    		lm.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 0, 0, ll);
	    } catch(Exception e) {
	    	e.printStackTrace();
	    }
	    
	    boolean isGPS = lm.isProviderEnabled (LocationManager.GPS_PROVIDER)
	    				|| lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
	    				;//|| lm.isProviderEnabled(LocationManager.PASSIVE_PROVIDER);
	    if(!isGPS) {
	    	//Raise an alert dialog
        	new AlertDialog.Builder(this)
        	.setTitle(R.string.gps_check_not_enable_dialog_title)
        	.setMessage(R.string.gps_check_not_enable_dialog_message)
        	.setNeutralButton(R.string.gps_check_not_enable_dialog_button_neutra_label, new DialogInterface.OnClickListener() {
	        	public void onClick(DialogInterface dlg, int sumthin) { 
	        		startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), GPS_ENABLE_CHECK_CODE);
	        	} 
        	})
        	.setNegativeButton(R.string.gps_check_not_enable_dialog_button_negative_label, new DialogInterface.OnClickListener() {
	        	public void onClick(DialogInterface dlg, int sumthin) { 
	        		requestChangeConfig();
	        	}
        	})
        	.show();
	    } else {
	    	finishSession(RSPortalControlPackage.STATE_READY);
	    }
	}
	
	private void requestChangeConfig() {
		Toast.makeText(getApplicationContext(), R.string.gps_please_disable_share_function, Toast.LENGTH_LONG).show();
    	finishSession(RSPortalControlPackage.STATE_WAITING);
	}
	
	protected void onActivityResult(int requestCode, int resultCode,
            Intent data) {
        if (requestCode == GPS_ENABLE_CHECK_CODE) {
        	
        	LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        	boolean isGPS = lm.isProviderEnabled (LocationManager.GPS_PROVIDER);
        	if(!isGPS) {
        		requestChangeConfig();
        	}
        } 
    }
	
	public static final String GPSPORTAL_ROUTE_DATA_FILE = "com.riverspart.gps.gpsportalgmtb.routetable_data.dat";
	public static final String START_FILE_OK_COMMIT = "gpsportal_routetable_data_config_start";
	public static final String END_FILE_OK_COMMIT = "gpsportal_routetable_data_config_end";
	
	@Override
	public String getFileName() {
		// TODO Auto-generated method stub
		return GPSPORTAL_ROUTE_DATA_FILE;
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
