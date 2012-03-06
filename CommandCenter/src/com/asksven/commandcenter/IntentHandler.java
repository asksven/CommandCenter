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

import com.asksven.commandcenter.valueobjects.Command;

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
        		//Log.d(getClass().getSimpleName(), "Executing remotely issued command " + strCommand + " ");
        		//Command.execute(strCommand, true);
			}
        }
	}
}
