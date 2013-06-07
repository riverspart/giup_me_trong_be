package com.riverspart;

import com.riverspart.storage.RSStorableImpl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

public abstract class RSConfigurationImpl extends RSStorableImpl implements RSConfiguration {
	
	@Override
	public abstract ViewGroup renderLayout(Activity activity);
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		View v = renderLayout(this);
		if( v != null)setContentView(v);
	}

//	@Override
//	public abstract void retrieve();
	@Override
	public void loadConfiguration(){
		retrieve();
	}
	@Override
	protected void onStart() {
		super.onStart();
		loadConfiguration();
		
		if(autoFinish()) finish();
	}
	
	protected abstract void updateLayout();
	@Override
	protected void onResume() {
		super.onResume();
		
//		loadConfiguration();
//		if(autoFinish()) finish();
		
		updateLayout();
	}

	// 		Start process
	/*-----------------------------------------------------------*/
	//		Stop process
	
	protected abstract void saveLayout();
	@Override
	public abstract void store();
	@Override
	protected void onPause() {
		saveLayout();
		store();
		super.onPause();
	}
	
	
	/***********************************************************/
	public static final String PASS_AUTO_FINISH_INDEX = "com.riverspart.rsconfigurationimpl.auto_finish_index";
	public static final int AUTO_FINISH_YES = 1;
	public static final int AUTO_FINISH_NO = 0;
	public Boolean autoFinish() {
		Intent inte = getIntent();
		Bundle extra = inte.getExtras();
		if(extra!=null) {
			return AUTO_FINISH_YES == extra.getInt(PASS_AUTO_FINISH_INDEX, AUTO_FINISH_NO) ? true : false;
		}
		return false;
//		
//		try{
//			return AUTO_FINISH_YES == getIntent().getExtras().getInt(PASS_AUTO_FINISH_INDEX) ? true : false;
//		}catch(Exception e) {
//			/**
//			 * When intent have no extra pass followed it.
//			 * It mean we simple start activity by default no specify AUTO_FINISH_YES or _NO
//			 * So we default it AUTO_FINISH_NO in here
//			 */
//			return false;
//		}
	}
	
}
