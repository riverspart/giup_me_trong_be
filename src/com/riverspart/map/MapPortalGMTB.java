/*
 * Copyright (C) 2012 The Android Open Source Project
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

package com.riverspart.map;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.riverspart.data.RSPortalControlPackage;
import com.riverspart.map.view.FeatureView;
import com.riverspart.nurse.R;

/**
 * The main activity of the API library demo gallery.
 * <p>
 * The main layout lists the demonstrated features, with buttons to launch them.
 */
public final class MapPortalGMTB extends MapPortalImpl {

    /**
     * A simple POJO that holds the details about the demo that are used by the List Adapter.
     */
    private static class DemoDetails {
        /**
         * The resource id of the title of the demo.
         */
        private final int titleId;

        /**
         * The resources id of the description of the demo.
         */
        private final int descriptionId;

        /**
         * The demo activity's class.
         */
        private final Class<? extends FragmentActivity> activityClass;

        public DemoDetails(
                int titleId, int descriptionId, Class<? extends FragmentActivity> activityClass) {
            super();
            this.titleId = titleId;
            this.descriptionId = descriptionId;
            this.activityClass = activityClass;
        }
    }

    /**
     * A custom array adapter that shows a {@link FeatureView} containing details about the demo.
     */
    private static class CustomArrayAdapter extends ArrayAdapter<DemoDetails> {

        /**
         * @param demos An array containing the details of the demos to be displayed.
         */
        public CustomArrayAdapter(Context context, DemoDetails[] demos) {
            super(context, R.layout.feature, R.id.title, demos);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            FeatureView featureView;
            if (convertView instanceof FeatureView) {
                featureView = (FeatureView) convertView;
            } else {
                featureView = new FeatureView(getContext());
            }

            DemoDetails demo = getItem(position);

            featureView.setTitleId(demo.titleId);
            featureView.setDescriptionId(demo.descriptionId);

            return featureView;
        }
    }

    private static final DemoDetails[] demos = {new DemoDetails(
            R.string.map_basic_view_title, R.string.map_basic_view_description, MapBasicActivity.class),
            
//            new DemoDetails(R.string.camera_demo, R.string.camera_description,
//                    MapCameraActivity.class),
//                    
//            new DemoDetails(R.string.events_demo, R.string.events_description,
//                    MapEventsActivity.class),
//            new DemoDetails(R.string.layers_demo, R.string.layers_description,
//                    MapLayersActivity.class),
//            new DemoDetails(
//                    R.string.locationsource_demo, R.string.locationsource_description,
//                    MapLocationSourceActivity.class),
//                    
            new DemoDetails(R.string.map_uisettings_view_title, R.string.map_uisettings_view_description,
                    MapUiSettingsActivity.class)//,
//                    
//            new DemoDetails(R.string.groundoverlay_demo, R.string.groundoverlay_description,
//                    MapGroundOverlayActivity.class),
//            new DemoDetails(R.string.marker_demo, R.string.marker_description,
//                    MapMarkerActivity.class),
//            new DemoDetails(R.string.polygon_demo, R.string.polygon_description,
//                    MapPolygonActivity.class),
//            new DemoDetails(R.string.polyline_demo, R.string.polyline_description,
//                    MapPolylineActivity.class),
//            new DemoDetails(R.string.circle_demo, R.string.circle_description,
//                    MapCircleActivity.class),
//            new DemoDetails(R.string.tile_overlay_demo, R.string.tile_overlay_description,
//                    MapTileOverlayActivity.class),
//            new DemoDetails(R.string.options_demo, R.string.options_description,
//                    MapOptionsActivity.class),
//            new DemoDetails(R.string.multi_map_demo, R.string.multi_map_description,
//                    MapMultiMapActivity.class),
//            new DemoDetails(R.string.retain_map, R.string.retain_map_description,
//                    MapRetainMapActivity.class),
//            new DemoDetails(R.string.raw_mapview_demo, R.string.raw_mapview_description,
//                    MapRawMapViewActivity.class),
//            new DemoDetails(R.string.programmatic_demo, R.string.programmatic_description,
//                    MapProgrammaticActivity.class)
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_main);

        ListAdapter adapter = new CustomArrayAdapter(this, demos);

        setListAdapter(adapter);
    }


    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        DemoDetails demo = (DemoDetails) getListAdapter().getItem(position);
        startActivity(new Intent(this, demo.activityClass));
    }
    
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.map_main_menu, menu);
//        return true;
//    }
//	
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch(item.getItemId()) {
//        	case R.id.map_main_menu_config:
//        		setResult(NursePortalGMTB.CONFIGURATION_ROUTING_NO);
//        		finish();
//        		return true;
//            case R.id.map_main_menu_gcm:
//            	setResult(NursePortalGMTB.GCM_ROUTING_NO);
//            	finish();
//                return true;
//            case R.id.map_main_menu_quit:
//            	setResult(RSPortalControlPackage.STATE_FINISH);
//            	finish();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }

	@Override
	public void onBackPressed() {
		setResult(RSPortalControlPackage.STATE_FINISH);
		super.onBackPressed();
	}


	public static final int ROUTE_MAP_ACTIVITY = 19;
	@Override
	public void route() {
		switch(MapSessionGMTB.currentMapConfiguration.getDefaultMapType()) {
		
			case MapConfigurationGMTB.DEFAULT_MAP_TYPE_BASIC :
				Intent mapBasicIntent = new Intent(this, MapBasicActivity.class);
		        this.startActivityForResult(mapBasicIntent, ROUTE_MAP_ACTIVITY);
		        break;
			case MapConfigurationGMTB.DEFAULT_MAP_TYPE_UI :
				Intent mapUiIntent = new Intent(this, MapUiSettingsActivity.class);
		        this.startActivityForResult(mapUiIntent, ROUTE_MAP_ACTIVITY);
		        break;
		    default: 
		    	break;
	    	
		}
		
		setResult(RSPortalControlPackage.STATE_FINISH);
	}
	
	protected void onActivityResult(int requestCode, int resultCode,
            Intent data) {
        if (requestCode == ROUTE_MAP_ACTIVITY) {
        	
        	setResult(RSPortalControlPackage.STATE_FINISH);
        	finish();
        } 
    }

	@Override
	protected void onResume() {
		super.onResume();
		route();
	}

	
	
	@Override
	protected void onStop() {
		super.onStop();
		onDestroy();
	}

	@Override
	public void cleanFile(String fileName) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void exportFile(FileWriter fileWriter) throws IOException {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void importFile(FileReader fileReader) throws IOException {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void store() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void retrieve() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void unstore() {
		// TODO Auto-generated method stub
		
	}

 
}
