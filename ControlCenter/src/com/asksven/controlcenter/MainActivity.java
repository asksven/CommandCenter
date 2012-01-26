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

package com.asksven.controlcenter;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;

import com.asksven.controlcenter.exec.Exec;
import com.asksven.controlcenter.exec.ExecResult;
import com.asksven.controlcenter.valueobjects.Command;
import com.asksven.controlcenter.valueobjects.CommandDBHelper;
import com.asksven.controlcenter.valueobjects.CommandListAdapter;
import com.asksven.controlcenter.R;

public class MainActivity extends ListActivity
{
	private CommandDBHelper m_myDB = null;
    private List<Command> m_myItems;
    private Command m_myCommand = null;
    
    static final int CONTEXT_EDIT_ID 		= 100;
    static final int CONTEXT_DELETE_ID 		= 101;
    static final int CONTEXT_EXECUTE_ID 	= 102;
    static final int CONTEXT_ADDFAV_ID	 	= 103;
    
    static final int REQUEST_CODE_PICK_FILE_OR_DIRECTORY = 1;
    
    @Override
	protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        unregisterForContextMenu(getListView()); 
        m_myDB = new CommandDBHelper(this);
        
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean bShowFavs = preferences.getBoolean("showOnlyFavorites", false);
        
        if (!bShowFavs)
        {
        	m_myItems = m_myDB.fetchAllRows();
        }
        else
        {
        	m_myItems = m_myDB.fetchFavoriteRows();
        }

        setListAdapter(new CommandListAdapter(this, m_myItems));
		
        getListView().setTextFilterEnabled(true);
        registerForContextMenu(getListView()); 
        
    }
    @Override
    protected void onPause()
    {
    	// drop the database
    	m_myDB = null;
    	super.onPause();
    }
    
    @Override
    public void onStop()
    {
    	super.onStop();
                
    }
    
    @Override
    protected void onResume()
    {
    	// recover the database and reload the data
    	if (m_myDB == null)
    	{
    		refreshList(-1);
    	}
		
    	super.onResume();
                
    }
    
    /** 
     * Add menu items
     * 
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     */
    public boolean onCreateOptionsMenu(Menu menu)
    {  
    	MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        return true;
    }  

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo info)
    {
        menu.setHeaderTitle("Actions");
        MenuItem mItem = menu.add(Menu.NONE, CONTEXT_EDIT_ID, Menu.NONE, "Edit");
        mItem = menu.add(Menu.NONE, CONTEXT_DELETE_ID, Menu.NONE, "Delete");
        mItem = menu.add(Menu.NONE, CONTEXT_EXECUTE_ID, Menu.NONE, "Execute");
        mItem = menu.add(Menu.NONE, CONTEXT_ADDFAV_ID, Menu.NONE, "Add to Favorites");
        
   } 
    // handle menu selected
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {  
	        case R.id.preferences:  
	    		Intent intent = new Intent(this, PreferencesActivity.class);
	   			startActivityForResult(intent, item.getItemId());
	    		break;
	        case R.id.add:	
	        	Intent intent2 = new Intent(MainActivity.this, CommandDetailsActivity.class);
	        	// pass no data to the dialog -> add
	        	startActivity(intent2);   
	        	break;    		
	        case R.id.about:
	        	Intent intent3 = new Intent(MainActivity.this, AboutActivity.class);
    	    	// pass no data to the dialog -> add
    	    	startActivity(intent3);   
    	    	break;
//	        case R.id.fragment:
//	        	Intent intent4 = new Intent(MainActivity.this, BasicActivity.class);
//    	    	startActivity(intent4);   
//    	    	break;

	        case R.id.test:	
	    		ExecResult myRes = Exec.execPrint("su -c cat /data/dropbear/.ssh/authorized_keys");
	    		String strLine = "";
	    		if (myRes.getSuccess())
	    		{
	    			strLine = myRes.getResultLine();
	    		}
	    	    break;
    	}
        return true;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
    	AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) item.getMenuInfo(); 

    	m_myCommand = m_myItems.get(menuInfo.position);
    	switch(item.getItemId())
    	{
    		case CONTEXT_EDIT_ID:
    	    	
    	    	if (m_myCommand != null)
    	    	{
    	    		Log.i(getClass().getSimpleName(), "Command was edited: " + m_myCommand.getId());
    	    		Intent intent = new Intent(MainActivity.this, com.asksven.controlcenter.CommandDetailsActivity.class);
    	    	    // pass some extra data to the dialog
    	    	    intent.putExtra("key", m_myCommand.getId());
    	    	    startActivity(intent);    	    
    	    	}
    			return true;
     
    		case CONTEXT_DELETE_ID:
    			m_myDB.deleteCommand(m_myCommand.getId());
    			refreshList(-1); 
    			return true;
    			
    		case CONTEXT_EXECUTE_ID:
    	    	if (m_myCommand != null)
    	    	{
	    			Log.i(getClass().getSimpleName(), "Running command");
	    			executeCommand();
	    			refreshList(-1);
   	    		}

    		case CONTEXT_ADDFAV_ID:
    			m_myCommand.setFavorite(1);
    			m_myDB.updateCommand(m_myCommand.getId(), m_myCommand);
    			refreshList(-1); 
    			return true;

    			
    		default:
    			return false;
    				
    	}
    	
     } 
    
    private void refreshList(int position)
    {
		unregisterForContextMenu(getListView()); 
		if (m_myDB == null)
		{
			m_myDB = new CommandDBHelper(this);
		}

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean bShowFavs = preferences.getBoolean("showOnlyFavorites", false);

        if (!bShowFavs)
        {
        	m_myItems = m_myDB.fetchAllRows();
        }
        else
        {
        	m_myItems = m_myDB.fetchFavoriteRows();
        }
		setListAdapter(new CommandListAdapter(this, m_myItems));
		registerForContextMenu(getListView());
		
		// set the cursor to where it was before the refresh
		getListView().setSelection(position);
    }

    protected void onListItemClick  (ListView l, View v, int position, long id)
    {
    	m_myCommand = m_myItems.get(position);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean bExecOnTap = preferences.getBoolean("execOnSelect", true);

    	if (m_myCommand != null)
    	{
    		Log.i(getClass().getSimpleName(), "Command was clicked: " + m_myCommand.getId());
    		if (bExecOnTap)
    		{
    			executeCommand();
    			refreshList(position);
    		}
    		else
    		{
    			Log.i(getClass().getSimpleName(), "Editing command");
	    		Intent intent = new Intent(MainActivity.this, com.asksven.controlcenter.CommandDetailsActivity.class);
	    	    // pass some extra data to the dialog
	    	    intent.putExtra("key", position);
	    	    startActivity(intent);    	    
    		}
    	}
    }

    /** execute the selected command */
    private void executeCommand()
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final boolean bHasRoot = preferences.getBoolean("hasRoot", false);
        
		if (!m_myCommand.getCommandValues().equals(""))
		{
			// handle whatever values are defined for the command
			// values can be either a list of "|" separated items to pick from
			// or a sub-command to be executed first
			// sub-commands are of the form "??something??:somewhere"
			// allowed sub-commands are 
			String strPickFile = "??pickfile??";
			String strPickDir = "??pickdir??";
			// using OpenIntent's FileManager: http://www.openintents.org/en/node/159
			
			
			if ((m_myCommand.getCommandValues().startsWith(strPickFile)) || (m_myCommand.getCommandValues().startsWith(strPickDir)))
			{
				// check for additional params in the sub-command
				CharSequence[] tokens = m_myCommand.getCommandValues().split("\\:");
				String strSuggestion = "";
				if (tokens.length > 1)
				{
					strSuggestion = (String)tokens[1];
				}
				Intent myIntent = null;
				if (m_myCommand.getCommandValues().startsWith(strPickFile))
				{
					myIntent = new Intent("org.openintents.action.PICK_FILE");
					myIntent.putExtra("org.openintents.extra.TITLE", "Pick a file");
					myIntent.putExtra("org.openintents.extra.BUTTON_TEXT", "Pick");
					
				}
				if (m_myCommand.getCommandValues().startsWith(strPickDir))
				{
					myIntent = new Intent("org.openintents.action.PICK_DIRECTORY");
					myIntent.putExtra("org.openintents.extra.TITLE", "Pick a directory");
					myIntent.putExtra("org.openintents.extra.BUTTON_TEXT", "Pick");
				}
				
				if (myIntent == null)
				{
					Toast.makeText(this, "sub-command could not be resolved, check the syntax of your command", 
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (!strSuggestion.equals(""))
				{
					myIntent.setData(Uri.parse("file://" + strSuggestion));
				}
				
				try
				{
					startActivityForResult(myIntent, REQUEST_CODE_PICK_FILE_OR_DIRECTORY);
				}
				catch (ActivityNotFoundException e)
				{
					// No compatible file manager was found.
					Toast.makeText(this, "You must install OpenIntent's FileManager to use this feature", 
							Toast.LENGTH_SHORT).show();
				}
				
			}
			else
			{
				final CharSequence[] items = m_myCommand.getCommandValues().split("\\|");
	
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("Pick a Value");
				builder.setCancelable(false);
	
				builder.setItems(items, new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog, int item) {
				    	String strSelection = (String) items[item];
				    	CharSequence[] tokens = strSelection.split("\\:");
				    	strSelection = (String) tokens[0];
				    	
				        m_myCommand.execute(strSelection, bHasRoot);
				        Toast.makeText(MainActivity.this, "Executing " + m_myCommand.getCommand(), Toast.LENGTH_LONG).show();
		    			refreshList(-1);
				    }
				});
				AlertDialog alert = builder.show();
			}
		}
		else
		{
			m_myCommand.execute(bHasRoot);
			Toast.makeText(MainActivity.this, "Executing " + m_myCommand.getCommand(), Toast.LENGTH_LONG).show();

		}
	
    }

    /**
     * This is called after the file manager finished.
     */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode)
		{
			case REQUEST_CODE_PICK_FILE_OR_DIRECTORY:
				if (resultCode == RESULT_OK && data != null)
				{
					// obtain the filename
					String strFilename = data.getDataString();
					if (strFilename != null)
					{
						// Get rid of URI prefix:
						if (strFilename.startsWith("file://"))
						{
							strFilename = strFilename.substring(7);
						}
				        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
				        boolean bHasRoot = preferences.getBoolean("hasRoot", false);

						m_myCommand.execute(strFilename, bHasRoot);
						
					}				
					
				}
				break;
		}
	}
}