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

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.asksven.commandcenter.exec.Exec;
import com.asksven.commandcenter.exec.ExecResult;
import com.asksven.commandcenter.valueobjects.Command;
import com.asksven.commandcenter.valueobjects.CommandCollection;
import com.asksven.commandcenter.valueobjects.CommandDBHelper;
import com.asksven.commandcenter.valueobjects.CommandReaderWriter;
import com.asksven.commandcenter.R;

/**
 * @author sven
 *
 */
/**
 * Activity shows two fragments, either both (landscape)
 * or as two activities
 */

public class BasicActivity  extends FragmentActivity
{
    static final int CONTEXT_EDIT_ID 		= 100;
    static final int CONTEXT_DELETE_ID 		= 101;
    static final int CONTEXT_EXECUTE_ID 	= 102;
    static final int CONTEXT_ADDFAV_ID	 	= 103;
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.basic_fragment);
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
	   			startActivity(intent);
	    		break;
	        case R.id.add:	
	        	Intent intent2 = new Intent(this, CommandDetailsActivity.class);
	        	// pass no data to the dialog -> add
	        	startActivity(intent2);   
	        	break;    		
	        case R.id.about:
	        	Intent intent3 = new Intent(this, AboutActivity.class);
    	    	// pass no data to the dialog -> add
    	    	startActivity(intent3);   
    	    	break;
    	    	
	        case R.id.test:	
	        	CommandDBHelper db = new CommandDBHelper(this);
	        	List<Command> myCommands = db.fetchAllRows();
	        	CommandCollection myCollection = new CommandCollection();
	        	myCollection.setTitle("This is a test collection");
	        	myCollection.setVersion(1L);
	        	myCollection.setHomepage("http://cri.ch/sven");
	        	myCollection.setUpdaterUrl("http://cri.ch/sven/test.json");
	        	myCollection.setIconFile("icon.gif");
	        	myCollection.setEntries(myCommands);
	        	
	        	CommandReaderWriter.writeFile(this, myCollection, "commands.json");
	        	break;
//	    		ExecResult myRes = Exec.execPrint("su -c cat /data/dropbear/.ssh/authorized_keys");
//	    		String strLine = "";
//	    		if (myRes.getSuccess())
//	    		{
//	    			strLine = myRes.getResultLine();
//	    		}
//	    	    break;

	        case R.id.test2:	
	        	CommandCollection myCollection2 = 
	        		CommandReaderWriter.readFile(this, "commands.json");
	        	int i = myCollection2.getEntries().size();
	        	break;

        }
        return true;
    }

}
