/**
 * 
 */
package com.asksven.systemsettings;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.asksven.systemsettings.R;
import com.asksven.systemsettings.exec.Exec;
import com.asksven.systemsettings.exec.ExecResult;

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
	        case R.id.oldmain:
	        	Intent intent5 = new Intent(this, MainActivity.class);
    	    	startActivity(intent5);   
    	    	break;

    	    	
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

}
