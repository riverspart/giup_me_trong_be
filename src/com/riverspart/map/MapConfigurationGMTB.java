package com.riverspart.map;

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
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.riverspart.nurse.R;
import com.riverspart.recover.RSDefaultRecoverable;

public class MapConfigurationGMTB extends MAPConfigurationImpl{

	private static final String DEFAULT_MAP_TYPE_STORE_KEY = "com.riverspart.map.configuration.default_map_type";
	private static final String DISPLAY_MY_SHARING_STORE_KEY = "com.riverspart.map.configuration.display_my_sharing";
	private static final String ALERT_ON_NOTIFIER_STORE_KEY = "com.riverspart.map.configuration.alert_on_notifier";
	private static final String MARKER_APPEARANCE_TYPE = "com.riverspart.map.configuration.marker_appearance_type";
	
	public static final String CONFIG_FILE_NAME = "map_configuration.dat";
	public static final String START_FILE_OK_COMMIT = "map_config_start";
	public static final String END_FILE_OK_COMMIT = "map_config_end";
	
	public static final int MAKER_APPEARANCE_TYPE_POINT = R.id.map_configuration_marker_appearance_point;
	public static final int MAKER_APPEARANCE_TYPE_LINE = R.id.map_configuration_marker_appearance_line;
	
	public static final int DEFAULT_MAP_TYPE_ASK_WHEN_USE = 0;
	public static final int DEFAULT_MAP_TYPE_BASIC = 1;
	public static final int DEFAULT_MAP_TYPE_UI = 2;
	
	@Override
	public ViewGroup renderLayout(Activity activity) {
		ViewGroup v = (ViewGroup) activity.getLayoutInflater().inflate(R.layout.map_configuration, null);
		return v;
	}

	@Override
	public View renderView(Activity activity) {
		ViewGroup layout = renderLayout(activity);
		View v = layout.findViewById(R.id.map_configuration_layout);
		layout.removeView(v);
		return v;
	}

	@Override
	public RSDefaultRecoverable loadDefault() {
		defaultMapType = DEFAULT_MAP_TYPE_ASK_WHEN_USE;
		displayMySharing = true;
		alertOnNotifier = true;
		markerAppearanceType = MAKER_APPEARANCE_TYPE_POINT;
		return this;
	}

	@Override
	public void loadConfiguration() {
		loadDefault();
		retrieve();
	}
	
	@Override
	public String getFileName() {
		
		return CONFIG_FILE_NAME;
	}

	@Override
	public void importFile(FileReader fileReader) throws IOException {
		BufferedReader in = new BufferedReader(fileReader);
		String commit = in.readLine();
		if(commit.equals(START_FILE_OK_COMMIT)) {
			String tmpDefaultMapType = in.readLine();
			String tmpDisplayMySharing = in.readLine();
			String tmpAlertOnNotifier = in.readLine();
			String tmpMarkerAppearanceType = in.readLine();
			commit = in.readLine();
			if(commit.equals(END_FILE_OK_COMMIT)) {
				
				try{
					defaultMapType = Integer.parseInt(tmpDefaultMapType);
					markerAppearanceType = Integer.parseInt(tmpMarkerAppearanceType);
				}catch(Exception e) { 
					e.printStackTrace(); 
				}
				displayMySharing = ( tmpDisplayMySharing.equals(String.valueOf(true)) );
				alertOnNotifier = ( tmpAlertOnNotifier.equals(String.valueOf(true)) );
			}
		}
		in.close();
	}

	@Override
	public void exportFile(FileWriter fileWriter) throws IOException {
		BufferedWriter out = new BufferedWriter(fileWriter);
		out.write(START_FILE_OK_COMMIT);
		out.newLine();
		out.write(String.valueOf(defaultMapType));
		out.newLine();
		out.write(String.valueOf(displayMySharing));
		out.newLine();
		out.write(String.valueOf(alertOnNotifier));
		out.newLine();
		out.write(String.valueOf(markerAppearanceType));
		out.newLine();
		out.write(END_FILE_OK_COMMIT);
		out.close();
	}

	@Override
	public void saveStorage(SharedPreferences prefs) {
		prefs.edit().putInt(DEFAULT_MAP_TYPE_STORE_KEY, defaultMapType);
		prefs.edit().putBoolean(DISPLAY_MY_SHARING_STORE_KEY, displayMySharing);
		prefs.edit().putBoolean(ALERT_ON_NOTIFIER_STORE_KEY, alertOnNotifier);
		prefs.edit().putInt(MARKER_APPEARANCE_TYPE, markerAppearanceType);
		prefs.edit().commit();
	}

	@Override
	public void loadStorage(SharedPreferences prefs) {
		defaultMapType = prefs.getInt(DEFAULT_MAP_TYPE_STORE_KEY, defaultMapType);
		displayMySharing = prefs.getBoolean(DISPLAY_MY_SHARING_STORE_KEY, displayMySharing);
		alertOnNotifier = prefs.getBoolean(ALERT_ON_NOTIFIER_STORE_KEY, alertOnNotifier);
		markerAppearanceType = prefs.getInt(MARKER_APPEARANCE_TYPE, markerAppearanceType);
	}

	@Override
	public void clearStorage(SharedPreferences prefs) {
		prefs.edit().remove(DEFAULT_MAP_TYPE_STORE_KEY);
		prefs.edit().remove(DISPLAY_MY_SHARING_STORE_KEY);
		prefs.edit().remove(ALERT_ON_NOTIFIER_STORE_KEY);
		prefs.edit().remove(MARKER_APPEARANCE_TYPE);
		prefs.edit().commit();
	}

	@Override
	protected void updateLayout() {
		Spinner sp = (Spinner) findViewById(R.id.map_configuration_defaulttype);
		sp.setSelection(defaultMapType, true);
		CheckBox cbox = (CheckBox) findViewById(R.id.map_configuration_display_myshare);
		cbox.setChecked(displayMySharing);
		cbox = (CheckBox) findViewById(R.id.map_configuration_alert_notifier);
		cbox.setChecked(alertOnNotifier);
		RadioGroup rg = (RadioGroup) findViewById(R.id.marker_appearance_type);
		rg.check(markerAppearanceType);
	}

	@Override
	protected void saveLayout() {
		Spinner sp = (Spinner) findViewById(R.id.map_configuration_defaulttype);
		defaultMapType = sp.getSelectedItemPosition();
		CheckBox cbox = (CheckBox) findViewById(R.id.map_configuration_display_myshare);
		displayMySharing = cbox.isChecked();
		cbox = (CheckBox) findViewById(R.id.map_configuration_alert_notifier);
		alertOnNotifier = cbox.isChecked();
		RadioGroup rg = (RadioGroup) findViewById(R.id.marker_appearance_type);
		markerAppearanceType = rg.getCheckedRadioButtonId();
	}
	
}
