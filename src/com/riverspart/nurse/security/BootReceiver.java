/*
 * Copyright 2011 Google Inc.
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

package com.riverspart.nurse.security;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.riverspart.location.PlacesConstants;

/**
 * This Receiver class is designed to listen for system boot.
 * 
 * If the app has been run at least once, the passive location
 * updates should be enabled after a reboot.
 */
public class BootReceiver extends BroadcastReceiver {
  @Override
  public void onReceive(Context context, Intent intent) {
    SharedPreferences prefs = context.getSharedPreferences(PlacesConstants.SHARED_PREFERENCE_FILE, Context.MODE_PRIVATE);
  	boolean runOnce = prefs.getBoolean(PlacesConstants.SP_KEY_RUN_ONCE, false);
  	
  	if (runOnce) {
  		
  	}
  }
}