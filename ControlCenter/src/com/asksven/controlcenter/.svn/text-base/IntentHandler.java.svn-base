package com.asksven.systemsettings;

import com.asksven.systemsettings.valueobjects.Command;
import com.asksven.systemsettings.valueobjects.Preferences;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

/**
 * General broadcast handler: handles event as registered on Manifest
 * @author sven
 *
 */
public class IntentHandler extends BroadcastReceiver
{
	
	/* (non-Javadoc)
	 * @see android.content.BroadcastReceiver#onReceive(android.content.Context, android.content.Intent)
	 */
	@Override
	public void onReceive(Context context, Intent intent)
	{
		Preferences myPrefs = new Preferences(context);
        
        
        if ((intent.getAction().equals(Intents.ACTION_COMMAND)) && (myPrefs.getAllowCommand()))
        {
        	Log.d(getClass().getSimpleName(), "Received Broadcast ACTION_COMMAND");
        	if ( intent.hasExtra(Intents.EXTRA_COMMAND) )
			{
        		String strCommand = intent.getStringExtra(Intents.EXTRA_COMMAND);
        		Log.d(getClass().getSimpleName(), "Executing remotely issued command " + strCommand + " ");
        		Command.exec(strCommand, true);
			}
        }
	}
}
