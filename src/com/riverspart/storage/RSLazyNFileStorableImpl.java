package com.riverspart.storage;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;


public abstract class RSLazyNFileStorableImpl extends RSLazyStorableImpl implements
		RSFileStorable {
	private RSFileStorable fileStorer;

	
	public RSLazyNFileStorableImpl() {
		if(getCurrentContext() != null) {
			localInitial();
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		localInitial();
	}
	
	//		Start process
	/*-----------------------------------------------------------*/
	//		Stop process
	
	/***********************************************************/
	

	public void localInitial() {
		this.fileStorer = new RSFileStorableImpl() {
			
			//Constructor override missing
			
			@Override
			public void importFile(FileReader fileReader)  throws IOException {
				transferImportFile(fileReader);
			}
			
			@Override
			public void exportFile(FileWriter fileWriter)  throws IOException {
				transferExportFile(fileWriter);
			}

			/**
			 * RSFileStorableImpl overriding 
			 * because we call in non-activity runtime
			 */
				public Context getCurrentContext() {
					return transferContext();
				}
				
				public String getFileName() {
					return transferFileName();
				}
			/**
			 */

				/**
				 * Due to call non-activity runtime
				 * following methods are expected not to launched
				 * so no need to do anything
				 */
					@Override
					public View renderView(Activity activity) {
						// TODO Auto-generated method stub
						return null;
					}
	
					@Override
					public ViewGroup renderLayout(Activity activity) {
						// TODO Auto-generated method stub
						return null;
					}
	
					@Override
					protected void updateLayout() {
						// TODO Auto-generated method stub
						
					}
	
					@Override
					protected void saveLayout() {
						// TODO Auto-generated method stub
						
					}
				/**
				 */
		};
	}
	
	/**
	 * Make equivalent methods with different names 
	 * for transfering to RSFileStorableImpl overriding 
	 * because we call in non-activity runtime
	 */
		private Context transferContext() {
			//return getCurrentContext();
			return getCurrentContext() != null ? getCurrentContext() : this.getApplicationContext();
		}
		private String transferFileName() {
			return getFileName();
		}
		private void transferImportFile(FileReader fileReader)  throws IOException {
			importFile(fileReader);
		}
		private void transferExportFile(FileWriter fileWriter)  throws IOException {
			exportFile(fileWriter);
		}
	/**
	 */
	
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
//			String fileName = getIntent().getExtras().getString(RSFileStorableImpl.PASS_FILE_NAME_INDEX);
//			return fileName != "" ? fileName : "configuration_file.dat";
//		}
		public abstract String getFileName();
		
	/**
	 */
	
	@Override
	public void store() {
		super.store();
		fileStorer.store();
	}

	@Override
	public void retrieve() {
		fileStorer.retrieve();
		super.retrieve();
	}

	@Override
	public void unstore() {
		super.unstore();
		fileStorer.unstore();
	}
	
	@Override
	public void cleanFile(String fileName) {
		fileStorer.cleanFile(fileName);
	}
	
	@Override
	public abstract void importFile(FileReader fileReader)  throws IOException;

	@Override
	public abstract void exportFile(FileWriter fileWriter) throws IOException;
}
