package com.riverspart.storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import android.content.Context;
import android.os.Bundle;

import com.riverspart.RSConfigurationImpl;

public abstract class RSFileStorableImpl extends RSConfigurationImpl implements RSFileStorable {

	private String fileName;
	private File file;
	private Context context;
	
	/**
	 * This constructor for calling in non-activity runtime
	 * So we need check if(context!=null) to omit case of calling in activity runtime
	 * because when calling in non-activity runtime, content will be pass to here
	 * from caller by override getCurrentContext();
	 * also calling in activity runtime, getCurrentContext() will return null
	 * it will be initial in onCreate(...) method instead
	 */
	public RSFileStorableImpl() {
		this.fileName = getFileName();
		this.context = getCurrentContext();	
		if(context!=null) {
			/**
			 * When invoked in non-activity runtime
			 * we initial this time
			 */
			prepareFile();
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		this.fileName = getFileName();
		//this.context = getCurrentContext();
		this.context = getApplicationContext();
		prepareFile();	
	}
	
	//	Start process
	/*-----------------------------------------------------------*/
	//		Stop process
	
	/***********************************************************/
	
	
	/**
	 * Override the 2 method below in children classes
	 */
		/**
		 * Override when invoking in non-activity runtime
		 * @return
		 */
		public Context getCurrentContext() {
			//return this;
			return null;
		}
		
//		/**
//		 * Must send filename to intent extra when invoke this activity
//		 * @return
//		 */
//		public String getFileName() {
//			fileName = getIntent().getExtras().getString(PASS_FILE_NAME_INDEX);
//			return fileName != "" ? fileName : "configuration_file.dat";
//		}
		/**
		 * Children have to override to 
		 * omit case of same file for bunch of children
		 * @return
		 */
		public abstract String getFileName();
		
		
	/**
	 */
	
	/**
	 * For reference when put filename to intent for while invoking this kind of activity
	 */
	public static final String PASS_FILE_NAME_INDEX = "com.riverspart.storage.filestorable.filename_passindex";
	
	private void prepareFile() {
		try {
			file = new File(context.getFilesDir(), fileName);
		} catch(NullPointerException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void store() {
		
		try {
			FileWriter fw = new FileWriter(file, false);
			exportFile(fw);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		//super.store();
	}

	@Override
	public void retrieve() {
		super.retrieve();
		try {
			FileReader fr = new FileReader(file);
			importFile(fr);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		} catch (Exception e3) {
			e3.printStackTrace();
		}
	}

	@Override
	public void unstore() {
		
		cleanFile(fileName);
		super.unstore();
	}
	
	@Override
	public abstract void importFile(FileReader fileReader)  throws IOException;

	@Override
	public abstract void exportFile(FileWriter fileWriter)  throws IOException;

	@Override
	public void cleanFile(String fileName) {
		file.delete();
	}

}
