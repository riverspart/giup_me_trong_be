package com.riverspart.map.view;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.riverspart.data.RSLocationData;
import com.riverspart.gcm.GCMConfiguration;
import com.riverspart.nurse.R;

public abstract class RSMapGMTBImpl extends FragmentActivity implements RSMapGMTB {

	@Override
	public abstract void updateMarker(String id, MarkerOptions makerOption);
	
	public final BroadcastReceiver victimLocationReceiver =
            new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
        	
            String sender_id = intent.getExtras().getString(RSLocationData.ID);
            String sender_title = intent.getExtras().getString(RSLocationData.TITLE);
            String lat = intent.getExtras().getString(RSLocationData.LATITUDE);
            String lg = intent.getExtras().getString(RSLocationData.LONGTITUDE);
            //Toast.makeText(getApplicationContext(), "received victim", Toast.LENGTH_LONG).show();
            
            updateMarker(sender_id, new MarkerOptions().position(new LatLng(Double.parseDouble(lat), Double.parseDouble(lg))).title(sender_title));
        }
    };

	@Override
	protected void onDestroy() {

		super.onDestroy();
		unregisterReceiver(victimLocationReceiver);
	}

	@Override
	protected void onResume() {

		super.onResume();
		registerReceiver(victimLocationReceiver,
                new IntentFilter(GCMConfiguration.PASS_VICTIM_DATA_ACTION));
		
		//Only OpenGL 2.0 or above support Google map v2
		if(!checkGL20Support(getApplicationContext())) {
			//Raise an alert dialog
        	new AlertDialog.Builder(this)
        	.setTitle(R.string.map_unsupport_gmapv2_dialog_title)
        	.setMessage(R.string.map_unsupport_gmapv2_dialog_message)
        	.setNeutralButton(R.string.map_unsupport_gmapv2_dialog_button_stop_label, new DialogInterface.OnClickListener() {
	        	public void onClick(DialogInterface dlg, int sumthin) { 
	        		//Call finish of current map activity
	        		finish();
	        	} 
        	})
        	.setNegativeButton(R.string.map_unsupport_gmapv2_dialog_button_continue_label, new DialogInterface.OnClickListener() {
	        	public void onClick(DialogInterface dlg, int sumthin) { 
	        		// do nothing – it will close on its own
	        		// then continue
	        	}
        	})
        	.show();
		}
	}
	
	private boolean checkGL20Support( Context context )
	{
	    EGL10 egl = (EGL10) EGLContext.getEGL();       
	    EGLDisplay display = egl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);

	    int[] version = new int[2];
	    egl.eglInitialize(display, version);

	    int EGL_OPENGL_ES2_BIT = 4;
	    int[] configAttribs =
	    {
	        EGL10.EGL_RED_SIZE, 4,
	        EGL10.EGL_GREEN_SIZE, 4,
	        EGL10.EGL_BLUE_SIZE, 4,
	        EGL10.EGL_RENDERABLE_TYPE, EGL_OPENGL_ES2_BIT,
	        EGL10.EGL_NONE
	    };

	    EGLConfig[] configs = new EGLConfig[10];
	    int[] num_config = new int[1];
	    egl.eglChooseConfig(display, configAttribs, configs, 10, num_config);     
	    egl.eglTerminate(display);
	    return num_config[0] > 0;
	} 

}
