package com.asksven.systemsettings;

import com.asksven.systemsettings.valueobjects.Command;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean bAllowRemote = preferences.getBoolean("allowRemoteCommands", false);

        if ((intent.getAction().equals(Intents.ACTION_COMMAND)) && (bAllowRemote))
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
