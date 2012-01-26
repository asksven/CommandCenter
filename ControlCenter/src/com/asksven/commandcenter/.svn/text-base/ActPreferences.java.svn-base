package com.asksven.systemsettings;

import com.asksven.systemsettings.R;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.asksven.systemsettings.valueobjects.Preferences;

// see example http://bestsiteinthemultiverse.com/2009/02/android-dialog-screen-example/
public class ActPreferences extends Activity 
{
	private Preferences m_myPrefs;
	
	/** Called when the activity is first created. */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dlg_preferences);
        m_myPrefs = new Preferences(this);
        readPreferences();
        // setup handler for Ok button
        Button btnOk = (Button) findViewById(R.id.ButtonOK);
        btnOk.setOnClickListener(new View.OnClickListener() {
           public void onClick(View arg0) {
        	   savePreferences();
	           setResult(RESULT_OK);
	           finish();
           }
        });
        // setup handler for the Cancel button
        Button btnCancel = (Button) findViewById(R.id.ButtonCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
 	           setResult(RESULT_CANCELED);
 	           finish();
            }
         });
    }

	/** load preferences to dialog */
    private void readPreferences()
    {
    	CheckBox myExec				= (CheckBox) findViewById(R.id.CheckBoxExec);
    	CheckBox myShowFavorites	= (CheckBox) findViewById(R.id.CheckBoxFavorites);
    	CheckBox myAllowCommands	= (CheckBox) findViewById(R.id.CheckBoxAllowCommands);
    	CheckBox myHasRoot			= (CheckBox) findViewById(R.id.CheckBoxHasRoot);
    	
    	myExec.setChecked(m_myPrefs.getExecOnSelect());
    	myShowFavorites.setChecked(m_myPrefs.getShowFavorites());
    	myAllowCommands.setChecked(m_myPrefs.getAllowCommand());
    	myHasRoot.setChecked(m_myPrefs.getHasRoot());
    }
    
    /** persist changed preferences */
    private void savePreferences()
    {
    	CheckBox myExec				= (CheckBox) findViewById(R.id.CheckBoxExec);
    	CheckBox myShowFavorites	= (CheckBox) findViewById(R.id.CheckBoxFavorites);
    	CheckBox myAllowCommands	= (CheckBox) findViewById(R.id.CheckBoxAllowCommands);
    	CheckBox myHasRoot			= (CheckBox) findViewById(R.id.CheckBoxHasRoot);
    	
    	m_myPrefs.setExecOnSelect(myExec.isChecked());
    	m_myPrefs.setShowFavorites(myShowFavorites.isChecked());
    	m_myPrefs.setAllowCommands(myAllowCommands.isChecked());
    	m_myPrefs.setHasRoot(myHasRoot.isChecked());
    	m_myPrefs.save();	
    }
    public void doTest()
    {
    	Uri uri = Uri.parse("content://command-goes-here");
    	Intent intent = new Intent("com.asksven.systemsettings.action.EXECUTE_COMMAND", uri);
    	intent.putExtra("command", "ls /");
    	this.sendBroadcast(intent);
    }    
}

