package com.riverspart.gps;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.riverspart.nurse.R;

public class GPSConfigurationGMTB extends GPSConfigurationImpl implements GPSConfiguration {

	private static final String DEVICE_ALIAS_STORE_KEY = "com.riverspart.gps.device_alias";
	private static final String DISTANCE_UPDATE_STORE_KEY = "com.riverspart.gps.distance_update";
	private static final String TIME_UPDATE_STORE_KEY = "com.riverspart.gps.time_update";
	private static final String UPDATE_TYPE_STORE_KEY = "com.riverspart.gps.update_type";
	private static final String ALLOW_SHARE_LOCATION_STORE_KEY = "com.riverspart.gps.allow_share_location";
	
	public static final String CONFIG_FILE_NAME = "gps_configuration.dat";
	public static final String START_FILE_OK_COMMIT = "gps_config_start";
	public static final String END_FILE_OK_COMMIT = "gps_config_end";
	
	public static final int UPDATE_TYPE_DISTANCE = R.id.gps_update_type_distance;
	public static final int UPDATE_TYPE_TIME = R.id.gps_update_type_time;
	
	
	@Override
	public ViewGroup renderLayout(Activity activity) {

		ViewGroup v = (ViewGroup) activity.getLayoutInflater().inflate(R.layout.gps_configuration, null);
		return v;
	}

	@Override
	public View renderView(Activity activity) {
		ViewGroup layout = renderLayout(activity);
		View v = layout.findViewById(R.id.gps_configuration_layout);
		layout.removeView(v);
		return v;
	}

	@Override
	public GPSConfigurationGMTB loadDefault() {
		
		this.deviceAlias = "Android Kute";
		this.distanceUpdate = 1.0F;
		this.timeUpdate = 5.0F;
		this.updateType = UPDATE_TYPE_DISTANCE;
		this.allowShareLocation = true;
		return this;
	}
	
	@Override
	public void loadConfiguration() {
		loadDefault();
		retrieve();
	}

	@Override
	public void importFile(FileReader fileReader) throws IOException {
		BufferedReader in = new BufferedReader(fileReader);
		String commit = in.readLine();
		if(commit.equals(START_FILE_OK_COMMIT)) {
			String tmpDevice_alias = in.readLine();
			String tmpDistance_update = in.readLine();
			String tmpTime_update = in.readLine();
			String tmpUpdateType = in.readLine();
			String tmpAllow_sharelocation = in.readLine();
			commit = in.readLine();
			if(commit.equals(END_FILE_OK_COMMIT)) {
				
				deviceAlias = tmpDevice_alias;
				try{
					distanceUpdate = Float.parseFloat(tmpDistance_update);
					timeUpdate = Float.parseFloat(tmpTime_update);
					updateType = Integer.parseInt(tmpUpdateType);
				}catch(Exception e) { 
					e.printStackTrace(); 
				}
				allowShareLocation = ( tmpAllow_sharelocation.equals(String.valueOf(true)) );
			}
		}
		in.close();
	}

	@Override
	public void exportFile(FileWriter fileWriter) throws IOException {
		BufferedWriter out = new BufferedWriter(fileWriter);
		out.write(START_FILE_OK_COMMIT);
		out.newLine();
		out.write(deviceAlias);
		out.newLine();
		out.write(String.valueOf(distanceUpdate));
		out.newLine();
		out.write(String.valueOf(timeUpdate));
		out.newLine();
		out.write(String.valueOf(updateType));
		out.newLine();
		out.write(String.valueOf(allowShareLocation));
		out.newLine();
		out.write(END_FILE_OK_COMMIT);
		out.close();
	}

	@Override
	public void saveStorage(SharedPreferences prefs) {
		prefs.edit().putString(DEVICE_ALIAS_STORE_KEY, deviceAlias);
		prefs.edit().putFloat(DISTANCE_UPDATE_STORE_KEY, distanceUpdate);
		prefs.edit().putFloat(TIME_UPDATE_STORE_KEY, timeUpdate);
		prefs.edit().putInt(UPDATE_TYPE_STORE_KEY, updateType);
		prefs.edit().putBoolean(ALLOW_SHARE_LOCATION_STORE_KEY, allowShareLocation);
		prefs.edit().commit();
	}

	@Override
	public void loadStorage(SharedPreferences prefs) {
		deviceAlias = prefs.getString(DEVICE_ALIAS_STORE_KEY, deviceAlias);
		distanceUpdate = prefs.getFloat(DISTANCE_UPDATE_STORE_KEY, distanceUpdate);
		timeUpdate = prefs.getFloat(TIME_UPDATE_STORE_KEY, timeUpdate);
		updateType = prefs.getInt(UPDATE_TYPE_STORE_KEY, updateType);
		allowShareLocation = prefs.getBoolean(ALLOW_SHARE_LOCATION_STORE_KEY, allowShareLocation);
	}

	@Override
	public void clearStorage(SharedPreferences prefs) {
		prefs.edit().remove(DEVICE_ALIAS_STORE_KEY);
		prefs.edit().remove(DISTANCE_UPDATE_STORE_KEY);
		prefs.edit().remove(TIME_UPDATE_STORE_KEY);
		prefs.edit().remove(UPDATE_TYPE_STORE_KEY);
		prefs.edit().remove(ALLOW_SHARE_LOCATION_STORE_KEY);
		prefs.edit().commit();
	}
	
	@Override
	public String getFileName() {
		return CONFIG_FILE_NAME;
	}

	private void updateUpdateTypeLayout() {
		int type = ((RadioGroup) findViewById(R.id.gps_update_type)).getCheckedRadioButtonId();
		//Disable all edit texts
		EditText edT = (EditText) findViewById(R.id.gps_configuration_distance_reupdate);
		edT.setEnabled(false);
		edT = (EditText) findViewById(R.id.gps_configuration_time_reupdate);
		edT.setEnabled(false);
		
		//Enable active type
		switch (type) {
		case UPDATE_TYPE_DISTANCE:
			edT = (EditText) findViewById(R.id.gps_configuration_distance_reupdate);
			edT.setEnabled(true);
			break;
		case UPDATE_TYPE_TIME:
			edT = (EditText) findViewById(R.id.gps_configuration_time_reupdate);
			edT.setEnabled(true);
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void updateLayout() {
		EditText edT = (EditText) findViewById(R.id.gps_configuration_device_alias);
		edT.setText(this.deviceAlias);
		RadioGroup updateTypeGroup = (RadioGroup) findViewById(R.id.gps_update_type);
		updateTypeGroup.check(updateType);
		updateTypeGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				updateUpdateTypeLayout();
			}
		});
		edT = (EditText) findViewById(R.id.gps_configuration_distance_reupdate);
		edT.setText(String.valueOf(distanceUpdate));
		edT = (EditText) findViewById(R.id.gps_configuration_time_reupdate);
		edT.setText(String.valueOf(timeUpdate));
		CheckBox cbox = (CheckBox) findViewById(R.id.gps_configuration_allow_share_location);
		cbox.setChecked(this.allowShareLocation);
		updateUpdateTypeLayout();
	}

	@Override
	protected void saveLayout() {
		EditText edT = (EditText) findViewById(R.id.gps_configuration_device_alias);
		this.deviceAlias = edT.getText().toString();
		updateType = ((RadioGroup) findViewById(R.id.gps_update_type)).getCheckedRadioButtonId();
		try{
			edT = (EditText) findViewById(R.id.gps_configuration_distance_reupdate);
			this.distanceUpdate = Float.parseFloat(edT.getText().toString());
			edT = (EditText) findViewById(R.id.gps_configuration_time_reupdate);
			this.timeUpdate = Float.parseFloat(edT.getText().toString());
		}catch(Exception e){e.printStackTrace();}
		CheckBox cbox = (CheckBox) findViewById(R.id.gps_configuration_allow_share_location);
		this.allowShareLocation = cbox.isChecked();
	}

}
