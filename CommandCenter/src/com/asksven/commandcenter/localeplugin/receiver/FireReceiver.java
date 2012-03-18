/*
 * Copyright (C) 2012 asksven
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
 * 
 * This file was contributed by two forty four a.m. LLC <http://www.twofortyfouram.com>
 * unter the terms of the Apache License, Version 2.0
 */

package com.asksven.commandcenter.localeplugin.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.asksven.commandcenter.localeplugin.Constants;
import com.asksven.commandcenter.localeplugin.bundle.BundleScrubber;
import com.asksven.commandcenter.localeplugin.bundle.PluginBundleManager;
import com.asksven.commandcenter.localeplugin.ui.EditActivity;
import com.asksven.commandcenter.valueobjects.CollectionManager;
import com.asksven.commandcenter.valueobjects.Command;

/**
 * This is the "fire" BroadcastReceiver for a Locale Plug-in setting.
 */
public final class FireReceiver extends BroadcastReceiver
{

    /**
     * @param context {@inheritDoc}.
     * @param intent the incoming {@link com.twofortyfouram.locale.Intent#ACTION_FIRE_SETTING} Intent. This should contain the
     *            {@link com.twofortyfouram.locale.Intent#EXTRA_BUNDLE} that was saved by {@link EditActivity} and later broadcast
     *            by Locale.
     */
    @Override
    public void onReceive(final Context context, final Intent intent)
    {
        /*
         * Always be sure to be strict on input parameters! A malicious third-party app could always send an empty or otherwise
         * malformed Intent. And since Locale applies settings in the background, the plug-in definitely shouldn't crash in the
         * background.
         */

        /*
         * Locale guarantees that the Intent action will be ACTION_FIRE_SETTING
         */
        if (!com.twofortyfouram.locale.Intent.ACTION_FIRE_SETTING.equals(intent.getAction()))
        {
            if (Constants.IS_LOGGABLE)
            {
                Log.e(Constants.LOG_TAG, String.format("Received unexpected Intent action %s", intent.getAction())); //$NON-NLS-1$
            }
            return;
        }

        /*
         * A hack to prevent a private serializable classloader attack
         */
        BundleScrubber.scrub(intent);
        BundleScrubber.scrub(intent.getBundleExtra(com.twofortyfouram.locale.Intent.EXTRA_BUNDLE));

        final Bundle bundle = intent.getBundleExtra(com.twofortyfouram.locale.Intent.EXTRA_BUNDLE);

        // TODO add processing code here
        Log.i(Constants.LOG_TAG, "Preparing to execute command: bundle.getString(PluginBundleManager.BUNDLE_EXTRA_STRING_COMMAND");
        Command command = CollectionManager.getInstance(context).getCommandByString(bundle.getString(PluginBundleManager.BUNDLE_EXTRA_STRING_COMMAND));
        if (command != null)
        {
        	Log.i(Constants.LOG_TAG, "Cound command with name " + command.getName() + ", will execute command:" + command.getCommand());
        	if (command.getCommandValues().equals(""))
        	{
        		command.execute();
        	}
        	else
        	{
        		Log.e(Constants.LOG_TAG, "Error, the command requires values to be set. Aborting");
        	}
        }
        else
        {
        	Log.e(Constants.LOG_TAG, "Error, the command could not be found");
        }
        
//        Toast.makeText(context, "Executing command: " + bundle.getString(PluginBundleManager.BUNDLE_EXTRA_STRING_COMMAND), Toast.LENGTH_LONG).show();
    }
}