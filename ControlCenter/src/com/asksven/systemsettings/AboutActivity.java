package com.asksven.systemsettings;


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
        setContentView(R.layout.dlg_about);
        
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

