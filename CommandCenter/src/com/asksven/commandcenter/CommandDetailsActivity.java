///*
// * Copyright (C) 2011 asksven
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// *      http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//
//package com.asksven.commandcenter;
//
//
//import com.asksven.commandcenter.valueobjects.CollectionManager;
//import com.asksven.commandcenter.valueobjects.Command;
//import com.asksven.commandcenter.R;
//
//import android.app.Activity;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.preference.PreferenceManager;
//import android.util.Log;
//import android.widget.CheckBox;
//import android.widget.EditText;
//
//
//public class CommandDetailsActivity extends Activity
//{
//	private String m_strCollection = "";
//	private int m_iPosition = -1;
//	
//	/** Called when the activity is first created. */
//	@Override
//    public void onCreate(Bundle savedInstanceState)
//	{
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.dlg_command);
//        
//        Bundle extras = getIntent().getExtras();
//
//        if (extras != null)
//        {
//        	m_strCollection = extras.getString("collection");
//        	m_iPosition = extras.getInt("key");
//        	Log.i(getClass().getSimpleName(), "Dialog was called with key=" + m_iPosition);
//        }
//        
//        EditText myId = (EditText) findViewById(R.id.EditId);
//        EditText myName = (EditText) findViewById(R.id.EditName);
//        EditText myCommand = (EditText) findViewById(R.id.EditCommand);
//        EditText myCommandValues = (EditText) findViewById(R.id.EditCommandValues);
//        EditText myStatus = (EditText) findViewById(R.id.EditStatus);
////        CheckBox myFavorite = (CheckBox) findViewById(R.id.CheckBoxFavorite);
//        EditText myStatusRegex = (EditText) findViewById(R.id.EditStatusRegex);
//        CheckBox myRegexIsOn = (CheckBox) findViewById(R.id.CheckBoxRegexIsOn);
//        
//        
//        if (m_iPosition != -1)
//        {
//        	SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
//        	boolean updateCache = preferences.getBoolean("autoRunStatus", true);
//
//        	Command myRecord = CollectionManager
//        			.getInstance(this).getCollectionByName(m_strCollection, updateCache)
//        			.findById(m_iPosition);
////        	CommandDBHelper myDB = new CommandDBHelper(CommandDetailsActivity.this);
////        	Command myRecord = myDB.fetchCommandByKey(m_iPosition);
//        	        	
//        	myId.setText(String.valueOf(myRecord.getId()));
//        	myName.setText(myRecord.getName());
//        	myCommand.setText(myRecord.getCommand());
//        	myCommandValues.setText(myRecord.getCommandValues());
//        	myStatus.setText(myRecord.getCommandStatus());
////        	myFavorite.setChecked(myRecord.getFavorite()==1);
//        	myStatusRegex.setText(myRecord.getRegexStatus());
//        	myRegexIsOn.setChecked(myRecord.getMatchRegexOn()==1);
//        	
//        }
//        
////        // setup handler for Ok button
////        Button btnOk = (Button) findViewById(R.id.ButtonOK);
////        btnOk.setOnClickListener(new View.OnClickListener() {
////           public void onClick(View arg0)
////           {   
////        	   // save to cell group database
////        	   EditText myId = (EditText) findViewById(R.id.EditId);
////	           EditText myName = (EditText) findViewById(R.id.EditName);
////	           EditText myCommand = (EditText) findViewById(R.id.EditCommand);
////	           EditText myCommandValues = (EditText) findViewById(R.id.EditCommandValues);
////	           EditText myStatus = (EditText) findViewById(R.id.EditStatus);
////	           CheckBox myFavorite = (CheckBox) findViewById(R.id.CheckBoxFavorite);
////	           EditText myStatusRegex = (EditText) findViewById(R.id.EditStatusRegex);
////	           CheckBox myRegexIsOn = (CheckBox) findViewById(R.id.CheckBoxRegexIsOn);
////	           
////	           int iFavorite = 0;
////	           int iRegexIsOn = 0;
////	           if (myFavorite.isChecked())
////	           {
////	        	   iFavorite = 1;
////	           }
////	           if (myRegexIsOn.isChecked())
////	           {
////	        	   iRegexIsOn = 1;
////	           }
////
////	           
////	           String strName = myName.getText().toString();
////	           if (!strName.equals(""))
////	           {
////	        	   // Save the result
////	        	   CommandDBHelper myDB = new CommandDBHelper(CommandDetailsActivity.this);
////	        	   if (m_iPosition != -1)
////	        	   {
////	        		   myDB.updateCommand(Integer.parseInt(myId.getText().toString()) , 
////	        				   			new Command(strName,
////	        				   			myCommand.getText().toString(),
////	        				   			myCommandValues.getText().toString(),
////	        				   			myStatus.getText().toString(),
////	        				   			"",
////	        				   			"user",
////	        				   			iFavorite,
////	        				   			myStatusRegex.getText().toString(),
////	        				   			iRegexIsOn));
////	        	   }
////	        	   else
////	        	   {
////	        		   myDB.addCommand(
////	        				   			new Command(strName,
////	        				   			myCommand.getText().toString(),
////	        				   			myCommandValues.getText().toString(),
////	        				   			myStatus.getText().toString(),
////	        				   			"",
////	        				   			"user",
////	        				   			iFavorite,
////	        				   			myStatusRegex.toString(),
////	        				   			iRegexIsOn));
////	        	   }
////	           }
////	           
////	           
////	           setResult(RESULT_OK);
////	           finish();
////           }
////        });
////
////        // setup handler for the Cancel button
////        Button btnCancel = (Button) findViewById(R.id.ButtonCancel);
////        btnCancel.setOnClickListener(new View.OnClickListener() {
////            public void onClick(View arg0) {
//// 	           setResult(RESULT_CANCELED);
//// 	           
//// 	           finish();
////            }
////         });
////
////        // setup handler for the Cancel button
////        Button btnTest = (Button) findViewById(R.id.ButtonTest);
////        btnTest.setOnClickListener(new View.OnClickListener() {
////            public void onClick(View arg0) {
////         	   EditText myId = (EditText) findViewById(R.id.EditId);
////	           EditText myName = (EditText) findViewById(R.id.EditName);
////	           EditText myCommand = (EditText) findViewById(R.id.EditCommand);
////	           EditText myCommandValues = (EditText) findViewById(R.id.EditCommandValues);
////	           EditText myStatus = (EditText) findViewById(R.id.EditStatus);
////	           EditText myStatusRegex = (EditText) findViewById(R.id.EditStatusRegex);
////	           CheckBox myRegexIsOn = (CheckBox) findViewById(R.id.CheckBoxRegexIsOn);
////	    
////	           String strCommand = myCommand.getText().toString();
////	           if (!strCommand.equals(""))
////	           {
////	        	   Command myCmd = new Command(
////	        			   myName.getText().toString(),
////				   			myCommand.getText().toString(),
////				   			myCommandValues.getText().toString(),
////				   			myStatus.getText().toString(),
////				   			"",
////				   			"user",
////				   			0,
////				   			myStatusRegex.toString(),
////				   			0);
////
////	        	   SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
////	               boolean bHasRoot = preferences.getBoolean("hasRoot", false);
////
////	        	   String s=myCmd.execute(bHasRoot);
////	        	   Toast.makeText(CommandDetailsActivity.this, "Result:" + s, Toast.LENGTH_SHORT).show();
////	        	   
////	           }
////            }
////         });
//        
//    }
//
//}
//
