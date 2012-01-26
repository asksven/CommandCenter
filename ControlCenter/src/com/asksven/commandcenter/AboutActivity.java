/*
 * Copyright (C) 2011 asksven
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
package com.asksven.commandcenter;


import com.asksven.commandcenter.R;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * About Dialog
 * @author sven
 *
 */
public class AboutActivity extends Activity 
{
	
	/** Called when the activity is first created. */
	@Override
    public void onCreate(Bundle savedInstanceState) {
		
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        
        TextView txtVersion = (TextView) findViewById(R.id.textViewVersion);
        try
        {
        	ComponentName comp = new ComponentName(this, AboutActivity.class);
            PackageInfo oInfo = this.getPackageManager().getPackageInfo(comp.getPackageName(), 0);
            txtVersion.setText(oInfo.versionName);
        }
        catch (android.content.pm.PackageManager.NameNotFoundException e)
        {
        	txtVersion.setText("0");
        }

    }

}

