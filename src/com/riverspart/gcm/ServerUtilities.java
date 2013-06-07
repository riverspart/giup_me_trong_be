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

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import android.content.Context;
import android.util.Log;

import com.google.android.gcm.GCMRegistrar;
import com.riverspart.nurse.R;

/**
 * Helper class used to communicate with the demo server.
 */
public final class ServerUtilities {
	
    private static final Random random = new Random();

    /**
     * Register this account/device pair within the server.
     *
     * @return whether the registration succeeded or not.
     */
    static boolean register(final Context context, final GCMConfiguration gcmConfig) {
        Log.i(GCMConfiguration.TAG, "registering device (regId = " + gcmConfig.getRegId() + ")");
        String serverUrl = gcmConfig.getServer_url() + "/register";
        Map<String, String> params = new HashMap<String, String>();
        params.put("regId", gcmConfig.getRegId());
        long backoff = gcmConfig.getBackoff_milli_seconds() + random.nextInt(1000);
        // Once GCM returns a registration id, we need to register it in the
        // demo server. As the server might be down, we will retry it a couple
        // times.
        for (int i = 1; i <= gcmConfig.getMax_attemp(); i++) {
            Log.d(GCMConfiguration.TAG, "Attempt #" + i + " to register");
            try {
                gcmConfig.displayMessage(context, context.getString(
                        R.string.gcm_server_registering, i, gcmConfig.getMax_attemp()));
                post(serverUrl, params);
                GCMRegistrar.setRegisteredOnServer(context, true);
                String message = context.getString(R.string.gcm_server_registered);
                gcmConfig.displayMessage(context, message);
                return true;
            } catch (IOException e) {
                // Here we are simplifying and retrying on any error; in a real
                // application, it should retry only on unrecoverable errors
                // (like HTTP error code 503).
                Log.e(GCMConfiguration.TAG, "Failed to register on attempt " + i, e);
                if (i == gcmConfig.getMax_attemp()) {
                    break;
                }
                try {
                    Log.d(GCMConfiguration.TAG, "Sleeping for " + backoff + " ms before retry");
                    Thread.sleep(backoff);
                } catch (InterruptedException e1) {
                    // Activity finished before we complete - exit.
                    Log.d(GCMConfiguration.TAG, "Thread interrupted: abort remaining retries!");
                    Thread.currentThread().interrupt();
                    return false;
                }
                // increase backoff exponentially
                backoff *= 2;
            }
        }
        String message = context.getString(R.string.gcm_server_register_error,
                gcmConfig.getMax_attemp());
        gcmConfig.displayMessage(context, message);
        return false;
    }

    /**
     * Unregister this account/device pair within the server.
     */
    static void unregister(final Context context, final GCMConfiguration gcmConfig) {
        Log.i(GCMConfiguration.TAG, "unregistering device (regId = " + gcmConfig.getRegId() + ")");
        String serverUrl = gcmConfig.getServer_url() + "/unregister";
        Map<String, String> params = new HashMap<String, String>();
        params.put("regId", gcmConfig.getRegId());
        try {
            post(serverUrl, params);
            GCMRegistrar.setRegisteredOnServer(context, false);
            String message = context.getString(R.string.gcm_server_unregistered);
            gcmConfig.displayMessage(context, message);
        } catch (IOException e) {
            // At this point the device is unregistered from GCM, but still
            // registered in the server.
            // We could try to unregister again, but it is not necessary:
            // if the server tries to send a message to the device, it will get
            // a "NotRegistered" error message and should unregister the device.
            String message = context.getString(R.string.gcm_server_unregister_error,
                    e.getMessage());
            gcmConfig.displayMessage(context, message);
        }
    }
    
//    public static void uploadLocation(final Location location, final GCMConfiguration gcmConfig, final GPSConfiguration gpsConfig) {
//    	
//    	
//    	
//    	Log.i(GCMConfiguration.TAG, "upload new location to server...");
//        String serverUrl = gcmConfig.getServer_url() + "/sendAll";
//        Map<String, String> params = new HashMap<String, String>();
//        params.put(RSLocationData.ID, gpsConfig.getDeviceAlias().equals("") ? gcmConfig.getRegId() : gpsConfig.getDeviceAlias());
//        params.put(RSLocationData.LATITUDE, String.valueOf(location.getLatitude()));
//        params.put(RSLocationData.LONGTITUDE, String.valueOf(location.getLongitude()));
//        params.put(RSLocationData.TIME, String.valueOf(location.getTime()));
//        try {
//            post(serverUrl, params);
//        } catch (IOException e) {
//        	e.printStackTrace();
//        }
//    }
    

    /**
     * Issue a POST request to the server.
     *
     * @param endpoint POST address.
     * @param params request parameters.
     *
     * @throws IOException propagated from POST.
     */
    public static void post(String endpoint, Map<String, String> params)
            throws IOException {
        URL url;
        try {
            url = new URL(endpoint);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("invalid url: " + endpoint);
        }
        StringBuilder bodyBuilder = new StringBuilder();
        Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
        // constructs the POST body using the parameters
        while (iterator.hasNext()) {
            Entry<String, String> param = iterator.next();
            bodyBuilder.append(param.getKey()).append('=')
                    .append(param.getValue());
            if (iterator.hasNext()) {
                bodyBuilder.append('&');
            }
        }
        String body = bodyBuilder.toString();
        Log.v(GCMConfiguration.TAG, "Posting '" + body + "' to " + url);
        byte[] bytes = body.getBytes();
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setFixedLengthStreamingMode(bytes.length);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded;charset=UTF-8");
            // post the request
            OutputStream out = conn.getOutputStream();
            out.write(bytes);
            out.close();
            // handle the response
            int status = conn.getResponseCode();
            if (status != 200) {
              throw new IOException("Post failed with error code " + status);
            }
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
      }
}
