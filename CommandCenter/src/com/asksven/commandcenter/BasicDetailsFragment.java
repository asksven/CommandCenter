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

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.asksven.commandcenter.valueobjects.CollectionManager;
import com.asksven.commandcenter.valueobjects.Command;
import com.asksven.commandcenter.valueobjects.CommandCollection;
import com.asksven.commandcenter.valueobjects.CommandDBHelper;
//import com.asksven.commandcenter.valueobjects.CommandDBHelper;
import com.asksven.commandcenter.R;

/**
 * @author sven
 *
 */
/**
 * This is the secondary fragment, displaying the details of a particular
 * item.
 */

public class BasicDetailsFragment extends Fragment
{
    /**
     * Create a new instance of DetailsFragment, initialized to
     * show the text at 'index'.
     */
    public static BasicDetailsFragment newInstance(int index, String strCollectionName)
    {
        BasicDetailsFragment f = new BasicDetailsFragment();

        // Supply index input as an argument.
        Bundle args = new Bundle();
        args.putInt("index", index);
        args.putString("collection", strCollectionName);
        
        f.setArguments(args);

        return f;
    }

    public int getShownKey()
    {
        return getArguments().getInt("index", -1);
    }
    
    public String getCollectionName()
    {
    	Bundle args = getArguments();
    	try
    	{
    		return args.getString("collection", "none");
    	}
    	catch (NoSuchMethodError e)
    	{
    		// for versions not supporting getString("key", "default")
    		String s = args.getString("collection");
    		if (s == null)
    		{
    			s = "none";
    		}
    		return s;
    	}
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        if (container == null)
        {
            // We have different layouts, and in one of them this
            // fragment's containing frame doesn't exist.  The fragment
            // may still be created from its saved state, but there is
            // no reason to try to create its view hierarchy because it
            // won't be displayed.  Note this is not needed -- we could
            // just run the code below, where we would create and return
            // the view hierarchy; it would just never be used.
            return null;
        }

        View v = inflater.inflate(R.layout.dlg_command, container, false);

        final EditText myId = (EditText) v.findViewById(R.id.EditId);
        final EditText myName = (EditText) v.findViewById(R.id.EditName);
        final EditText myCommand = (EditText) v.findViewById(R.id.EditCommand);
        final EditText myCommandValues = (EditText) v.findViewById(R.id.EditCommandValues);
        final EditText myStatus = (EditText) v.findViewById(R.id.EditStatus);
        final EditText myStatusRegex = (EditText) v.findViewById(R.id.EditStatusRegex);
        final CheckBox myRegexIsOn = (CheckBox) v.findViewById(R.id.CheckBoxRegexIsOn);
        final EditText myTags = (EditText) v.findViewById(R.id.EditTags);
        final CheckBox mySuExec = (CheckBox) v.findViewById(R.id.CheckBoxSuExec);        
        final EditText myDescription = (EditText) v.findViewById(R.id.EditDescription);
        final CheckBox myProcessResult = (CheckBox) v.findViewById(R.id.CheckBoxProcessResult);
        Button   myButtonSave = (Button) v.findViewById(R.id.ButtonSave);
        
    	SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
    	boolean updateCache = preferences.getBoolean("autoRunStatus", true);

    	CommandCollection commands =
    			CollectionManager.
    			getInstance(getActivity()).
    			getCollectionByName(getCollectionName(), false);

    	final Command myRecord;
    	if ((commands != null) && (commands.findById(getShownKey()) != null))
		{
    		myRecord = commands.findById(getShownKey());
		}
    	else
    	{
    		myRecord = new Command();
    	}
     	
    	myId.setText(String.valueOf(myRecord.getId()));
    	myName.setText(myRecord.getName());
    	myCommand.setText(myRecord.getCommand());
    	myCommandValues.setText(myRecord.getCommandValues());
    	myStatus.setText(myRecord.getCommandStatus());
    	myStatusRegex.setText(myRecord.getRegexStatus());
    	myRegexIsOn.setChecked(myRecord.getMatchRegexOn()==1);
    	myTags.setText(myRecord.getTags());
    	mySuExec.setChecked(myRecord.getSuExec()==1);
    	myDescription.setText(myRecord.getDescription());
    	myProcessResult.setChecked(myRecord.getProcessResult()==1);
    	
    	// enable edition if user collection
    	if ((commands == null) || (!commands.getTitle().equals(CollectionManager.USER_COLLECTION_NAME)))
    	{
        	myName.setEnabled(false);
        	myCommand.setEnabled(false);
        	myCommandValues.setEnabled(false);
        	myStatus.setEnabled(false);
        	myStatusRegex.setEnabled(false);
        	myTags.setEnabled(false);
        	mySuExec.setEnabled(false);
        	myRegexIsOn.setEnabled(false);
        	myDescription.setEnabled(false);
        	myProcessResult.setEnabled(false);
    	}
    	
    	// enable save button if user collection
    	if ((commands != null) && (commands.getTitle().equals(CollectionManager.USER_COLLECTION_NAME)))
    	{
    		myButtonSave.setVisibility(Button.VISIBLE);
            myButtonSave.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                	// save changes and close

                	//myId.setText(String.valueOf(myRecord.getId()));
    	        	myRecord.setName(myName.getText().toString());
    	        	myRecord.setCommand(myCommand.getText().toString());
    	        	myRecord.setCommandValues(myCommandValues.getText().toString());
    	        	myRecord.setCommandStatus(myStatus.getText().toString());
    	        	myRecord.setRegexStatus(myStatusRegex.getText().toString());
    	        	myRecord.setMatchRegexOn(myRegexIsOn.isChecked());
    	        	myRecord.setTags(myTags.getText().toString());
    	        	myRecord.setSuExec(mySuExec.isChecked());            	        	
    	        	myRecord.setDescription(myDescription.getText().toString());
    	        	myRecord.setProcessResult(myProcessResult.isChecked());
            		
            		CommandDBHelper myDB = new CommandDBHelper(getActivity());
    	        	
                	if (myRecord.getId() != -1)
                	{
                		// updating
        	        	myDB.updateCommand(myRecord.getId(), myRecord);
                	}
                	else
                	{
                		// inserting
                		myDB.addCommand(myRecord);
                	}
                	
                	getActivity().finish();
                }
            });

    	}
    	else
    	{
    		myButtonSave.setVisibility(Button.INVISIBLE);

    	}
        
        // @todo add button handlers here
        return v;
        
    }
}
