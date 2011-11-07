package com.asksven.systemsettings;


import java.util.List;

import com.asksven.systemsettings.valueobjects.Command;
import com.asksven.systemsettings.valueobjects.CommandDBHelper;
import com.asksven.systemsettings.valueobjects.Preferences;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;
import android.widget.RatingBar.OnRatingBarChangeListener;


public class ActCommandDetails extends Activity
{
	private int m_iPosition = -1;
	
	/** Called when the activity is first created. */
	@Override
    public void onCreate(Bundle savedInstanceState)
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dlg_command);
        final Preferences myPrefs = new Preferences(this);
        
        Bundle extras = getIntent().getExtras();

        if (extras != null)
        {
        	m_iPosition = extras.getInt("key");
        	Log.i(getClass().getSimpleName(), "Dialog was called with key=" + m_iPosition);
        }
        
        EditText myId = (EditText) findViewById(R.id.EditId);
        EditText myName = (EditText) findViewById(R.id.EditName);
        EditText myCommand = (EditText) findViewById(R.id.EditCommand);
        EditText myCommandValues = (EditText) findViewById(R.id.EditCommandValues);
        EditText myStatus = (EditText) findViewById(R.id.EditStatus);
        CheckBox myFavorite = (CheckBox) findViewById(R.id.CheckBoxFavorite);
        EditText myStatusRegex = (EditText) findViewById(R.id.EditStatusRegex);
        CheckBox myRegexIsOn = (CheckBox) findViewById(R.id.CheckBoxRegexIsOn);
        
        
        if (m_iPosition != -1)
        {
        	CommandDBHelper myDB = new CommandDBHelper(ActCommandDetails.this);
        	Command myRecord = myDB.fetchCommandByKey(m_iPosition);
        	        	
        	myId.setText(String.valueOf(myRecord.getId()));
        	myName.setText(myRecord.getName());
        	myCommand.setText(myRecord.getCommand());
        	myCommandValues.setText(myRecord.getCommandValues());
        	myStatus.setText(myRecord.getCommandStatus());
        	myFavorite.setChecked(myRecord.getFavorite()==1);
        	myStatusRegex.setText(myRecord.getRegexStatus());
        	myRegexIsOn.setChecked(myRecord.getMatchRegexOn()==1);
        	
        }
        
        // setup handler for Ok button
        Button btnOk = (Button) findViewById(R.id.ButtonOK);
        btnOk.setOnClickListener(new View.OnClickListener() {
           public void onClick(View arg0)
           {   
        	   // save to cell group database
        	   EditText myId = (EditText) findViewById(R.id.EditId);
	           EditText myName = (EditText) findViewById(R.id.EditName);
	           EditText myCommand = (EditText) findViewById(R.id.EditCommand);
	           EditText myCommandValues = (EditText) findViewById(R.id.EditCommandValues);
	           EditText myStatus = (EditText) findViewById(R.id.EditStatus);
	           CheckBox myFavorite = (CheckBox) findViewById(R.id.CheckBoxFavorite);
	           EditText myStatusRegex = (EditText) findViewById(R.id.EditStatusRegex);
	           CheckBox myRegexIsOn = (CheckBox) findViewById(R.id.CheckBoxRegexIsOn);
	           
	           int iFavorite = 0;
	           int iRegexIsOn = 0;
	           if (myFavorite.isChecked())
	           {
	        	   iFavorite = 1;
	           }
	           if (myRegexIsOn.isChecked())
	           {
	        	   iRegexIsOn = 1;
	           }

	           
	           String strName = myName.getText().toString();
	           if (!strName.equals(""))
	           {
	        	   // Save the result
	        	   CommandDBHelper myDB = new CommandDBHelper(ActCommandDetails.this);
	        	   if (m_iPosition != -1)
	        	   {
	        		   myDB.updateCommand(Integer.parseInt(myId.getText().toString()) , 
	        				   			new Command(strName,
	        				   			myCommand.getText().toString(),
	        				   			myCommandValues.getText().toString(),
	        				   			myStatus.getText().toString(),
	        				   			"",
	        				   			"user",
	        				   			iFavorite,
	        				   			myStatusRegex.getText().toString(),
	        				   			iRegexIsOn));
	        	   }
	        	   else
	        	   {
	        		   myDB.addCommand(
	        				   			new Command(strName,
	        				   			myCommand.getText().toString(),
	        				   			myCommandValues.getText().toString(),
	        				   			myStatus.getText().toString(),
	        				   			"",
	        				   			"user",
	        				   			iFavorite,
	        				   			myStatusRegex.toString(),
	        				   			iRegexIsOn));
	        	   }
	           }
	           
	           
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

        // setup handler for the Cancel button
        Button btnTest = (Button) findViewById(R.id.ButtonTest);
        btnTest.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
         	   EditText myId = (EditText) findViewById(R.id.EditId);
	           EditText myName = (EditText) findViewById(R.id.EditName);
	           EditText myCommand = (EditText) findViewById(R.id.EditCommand);
	           EditText myCommandValues = (EditText) findViewById(R.id.EditCommandValues);
	           EditText myStatus = (EditText) findViewById(R.id.EditStatus);
	           EditText myStatusRegex = (EditText) findViewById(R.id.EditStatusRegex);
	           CheckBox myRegexIsOn = (CheckBox) findViewById(R.id.CheckBoxRegexIsOn);
	    
	           String strCommand = myCommand.getText().toString();
	           if (!strCommand.equals(""))
	           {
	        	   Command myCmd = new Command(
	        			   myName.getText().toString(),
				   			myCommand.getText().toString(),
				   			myCommandValues.getText().toString(),
				   			myStatus.getText().toString(),
				   			"",
				   			"user",
				   			0,
				   			myStatusRegex.toString(),
				   			0);
	        	   
	        	   String s=myCmd.execute(myPrefs.getHasRoot());
	        	   Toast.makeText(ActCommandDetails.this, "Result:" + s, Toast.LENGTH_SHORT).show();
	        	   
	           }
            }
         });
        
    }

}

