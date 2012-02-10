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
public class ActAbout extends Activity 
{
	
	/** Called when the activity is first created. */
	@Override
    public void onCreate(Bundle savedInstanceState) {
		
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dlg_about);
        
        // setup handler for Ok button
        Button btnOk = (Button) findViewById(R.id.ButtonOK);
        btnOk.setOnClickListener(new View.OnClickListener() {
           public void onClick(View arg0) {
	           setResult(RESULT_OK);
	           finish();
           }
        });
        
        TextView txtURL = (TextView) findViewById(R.id.TextViewURL);
        txtURL.setOnClickListener(new View.OnClickListener() {
           public void onClick(View arg0) {
        	   Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.app_home)));
        	   startActivity(intent); 
           }
        });

        TextView txtVersion = (TextView) findViewById(R.id.Version);
        try
        {
        	ComponentName comp = new ComponentName(this, ActAbout.class);
            PackageInfo oInfo = this.getPackageManager().getPackageInfo(comp.getPackageName(), 0);
            txtVersion.setText(oInfo.versionName);
        }
        catch (android.content.pm.PackageManager.NameNotFoundException e)
        {
        	txtVersion.setText("0");
        }

    }

}

