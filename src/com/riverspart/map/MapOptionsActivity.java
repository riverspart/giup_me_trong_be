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

import com.google.android.gms.maps.model.MarkerOptions;
import com.riverspart.map.view.RSMapGMTBImpl;
import com.riverspart.nurse.R;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

/**
 * An activity that creates a map with some initial options.
 *
 * @author hearnden@google.com (David Hearnden)
 */
public final class MapOptionsActivity extends RSMapGMTBImpl {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.options_demo);
    }

	@Override
	public void updateMarker(String id, MarkerOptions makerOption) {
		// TODO Auto-generated method stub
		
	}
}