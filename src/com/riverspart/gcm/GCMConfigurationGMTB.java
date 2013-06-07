package com.riverspart.gcm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import android.app.Activity;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.riverspart.nurse.R;


public class GCMConfigurationGMTB extends GCMConfigurationImpl {
	

	private static final String SERVER_URL_STORE_KEY = "com.riverspart.gcm.server_url";
	private static final String SENDER_ID_STORE_KEY = "com.riverspart.gcm.sender_id";
	private static final String MAX_ATTEMP_STORE_KEY = "com.riverspart.gcm.max_attemp";
	private static final String BACKOFF_STORE_KEY = "com.riverspart.gcm.backoff";
	private static final String REG_ID_STORE_KEY = "com.riverspart.gcm.reg_id";
	
	public static final String CONFIG_FILE_NAME = "gcm_configuration.dat";
	public static final String START_FILE_OK_COMMIT = "gcm_config_start";
	public static final String END_FILE_OK_COMMIT = "gcm_config_end";
	
	
	
	/**
	 * Index use to pass configuration between intents
	 */
	public static final String PASS_INTENT_DATA_INDEX = "gcm_configuration";

	@Override
	public GCMConfigurationGMTB loadDefault() {
		
		//this.server_url = "http://192.168.100.250:8080/gcm-demo";
		//this.server_url = "http://evn-1.jelastic.servint.net";
		this.server_url = "http://gcm.riverspart.cloudbees.net";
		this.sender_id = "1007506665563";
		this.max_attemp = 5;
		this.backoff_milli_seconds = 2000;
		this.regId = "";
		return this;
	}
	
	
	@Override
	public void loadConfiguration() {
		loadDefault();
		retrieve();
	}
	

	@Override
	public ViewGroup renderLayout(Activity activity) {

		ViewGroup v = (ViewGroup) activity.getLayoutInflater().inflate(R.layout.gcm_configuration, null);
		return v;
	}
	@Override
	public View renderView(Activity activity) {
		ViewGroup layout = renderLayout(activity);
		View v = layout.findViewById(R.id.gcm_configuration_layout);
		layout.removeView(v);
		return v;
	}
	@Override
	public void loadStorage(SharedPreferences prefs) {
		server_url = prefs.getString(SERVER_URL_STORE_KEY, server_url);
		sender_id = prefs.getString(SENDER_ID_STORE_KEY, sender_id);
		max_attemp = prefs.getInt(MAX_ATTEMP_STORE_KEY, max_attemp);
		backoff_milli_seconds = prefs.getInt(BACKOFF_STORE_KEY, backoff_milli_seconds);
		regId = prefs.getString(REG_ID_STORE_KEY, regId);
	}
	
	@Override
	public void saveStorage(SharedPreferences prefs) {
		prefs.edit().putString(SERVER_URL_STORE_KEY, server_url);
		prefs.edit().putString(SENDER_ID_STORE_KEY, sender_id);
		prefs.edit().putInt(MAX_ATTEMP_STORE_KEY, max_attemp);
		prefs.edit().putInt(BACKOFF_STORE_KEY, backoff_milli_seconds);
		prefs.edit().putString(REG_ID_STORE_KEY, regId);
		prefs.edit().commit();
	}
	
	@Override
	public void clearStorage(SharedPreferences prefs) {
		prefs.edit().remove(SERVER_URL_STORE_KEY);
		prefs.edit().remove(SENDER_ID_STORE_KEY);
		prefs.edit().remove(MAX_ATTEMP_STORE_KEY);
		prefs.edit().remove(BACKOFF_STORE_KEY);
		prefs.edit().remove(REG_ID_STORE_KEY);
		prefs.edit().commit();
	}

	@Override
	public void importFile(FileReader fileReader) throws IOException  {
		BufferedReader in = new BufferedReader(fileReader);
		String commit = in.readLine();
		if(commit.equals(START_FILE_OK_COMMIT)) {
			String tmpServer_url = in.readLine();
			String tmpSender_id = in.readLine();
			String tmpMaxAttemp = in.readLine();
			String tmpBackOff = in.readLine();
			String tmpRegId = in.readLine();
			commit = in.readLine();
			if(commit.equals(END_FILE_OK_COMMIT)) {
				
				server_url = tmpServer_url;
				sender_id = tmpSender_id;
				try{
					max_attemp = Integer.parseInt(tmpMaxAttemp);
					backoff_milli_seconds = Integer.parseInt(tmpBackOff);
				}catch(Exception e) { 
					e.printStackTrace(); 
				}
				regId = tmpRegId;
			}
		}
		in.close();
	}
	@Override
	public void exportFile(FileWriter fileWriter) throws IOException {
		BufferedWriter out = new BufferedWriter(fileWriter);
		out.write(START_FILE_OK_COMMIT);
		out.newLine();
		out.write(server_url);
		out.newLine();
		out.write(sender_id);
		out.newLine();
		out.write(String.valueOf(max_attemp));
		out.newLine();
		out.write(String.valueOf(backoff_milli_seconds));
		out.newLine();
		out.write(regId);
		out.newLine();
		out.write(END_FILE_OK_COMMIT);
		out.close();
	}
	
	@Override
	public String getFileName() {
		return CONFIG_FILE_NAME;
	}
	@Override
	protected void updateLayout() {

		EditText edT = (EditText) findViewById(R.id.gcm_configuration_server_url);
		edT.setText(this.server_url);
		edT = (EditText) findViewById(R.id.gcm_configuration_sender_id);
		edT.setText(this.sender_id);
		edT = (EditText) findViewById(R.id.gcm_max_attemp);
		edT.setText(String.valueOf(this.max_attemp));
		edT = (EditText) findViewById(R.id.gcm_backoff_delay);
		edT.setText(String.valueOf(this.backoff_milli_seconds));
		edT = (EditText) findViewById(R.id.gcm_reg_id);
		edT.setText(String.valueOf(this.regId));
	}
	@Override
	protected void saveLayout() {
		try {
			EditText edT = (EditText) findViewById(R.id.gcm_configuration_server_url);
			this.server_url = edT.getText().toString();
			edT = (EditText) findViewById(R.id.gcm_configuration_sender_id);
			this.sender_id = edT.getText().toString();
			edT = (EditText) findViewById(R.id.gcm_max_attemp);
			this.max_attemp = Integer.parseInt(edT.getText().toString());
			edT = (EditText) findViewById(R.id.gcm_backoff_delay);
			this.backoff_milli_seconds = Integer.parseInt(edT.getText().toString());
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
