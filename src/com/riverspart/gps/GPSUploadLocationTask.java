package com.riverspart.gps;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import android.location.Location;
import android.os.AsyncTask;
import android.util.Log;

import com.riverspart.data.RSLocationData;
import com.riverspart.gcm.GCMConfiguration;
import com.riverspart.gcm.ServerUtilities;

public class GPSUploadLocationTask extends AsyncTask<Object, Integer, Boolean> {

	@Override
	protected Boolean doInBackground(Object... iparams) {
		
		GCMConfiguration gcmConfig = (GCMConfiguration)iparams[0];
		GPSConfiguration gpsConfig = (GPSConfiguration)iparams[1];
		Location location = (Location)iparams[2];


		Log.i(GCMConfiguration.TAG, "upload new location to server...");

		
        String serverUrl = gcmConfig.getServer_url() + "/sendAll";

        Map<String, String> params = new HashMap<String, String>();
        params.put(RSLocationData.ID, gcmConfig.getRegId());
        params.put(RSLocationData.TITLE, gpsConfig.getDeviceAlias());
        params.put(RSLocationData.LATITUDE, String.valueOf(location.getLatitude()));
        params.put(RSLocationData.LONGTITUDE, String.valueOf(location.getLongitude()));
        params.put(RSLocationData.TIME, String.valueOf(location.getTime()));
        try {
            ServerUtilities.post(serverUrl, params);
            GPSSessionGMTB.lastUpdateLocation = location;
        } catch (IOException e) {
        }
        
		return true;
	}

}
