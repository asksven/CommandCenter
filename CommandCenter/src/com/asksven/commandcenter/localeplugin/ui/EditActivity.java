/*
 * Copyright 2012 two forty four a.m. LLC <http://www.twofortyfouram.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at <http://www.apache.org/licenses/LICENSE-2.0>
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package com.asksven.commandcenter.localeplugin.ui;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.asksven.commandcenter.localeplugin.Constants;
import com.asksven.commandcenter.localeplugin.bundle.BundleScrubber;
import com.asksven.commandcenter.localeplugin.bundle.PluginBundleManager;
import com.asksven.commandcenter.valueobjects.CollectionManager;
import com.twofortyfouram.locale.BreadCrumber;
import com.asksven.commandcenter.R;

/**
 * This is the "Edit" activity for a Locale Plug-in.
 */
public final class EditActivity extends Activity implements AdapterView.OnItemSelectedListener
{

	static final String TAG = "CommandCenterStatsLocalePlugin:EditActivity"; 
    /**
     * Help URL, used for the {@link com.twofortyfouram.locale.platform.R.id#twofortyfouram_locale_menu_help} menu item.
     */
    // TODO: Place a real help URL here
    private static final String HELP_URL = "http://blog.asksven.org"; //$NON-NLS-1$

    /**
     * Flag boolean that can only be set to true via the "Don't Save"
     * {@link com.twofortyfouram.locale.platform.R.id#twofortyfouram_locale_menu_dontsave} menu item in
     * {@link #onMenuItemSelected(int, MenuItem)}.
     * <p>
     * If true, then this {@code Activity} should return {@link Activity#RESULT_CANCELED} in {@link #finish()}.
     * <p>
     * If false, then this {@code Activity} should generally return {@link Activity#RESULT_OK} with extras
     * {@link com.twofortyfouram.locale.Intent#EXTRA_BUNDLE} and {@link com.twofortyfouram.locale.Intent#EXTRA_STRING_BLURB}.
     * <p>
     * There is no need to save/restore this field's state when the {@code Activity} is paused.
     */
    private boolean mIsCancelled = false;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        /*
         * A hack to prevent a private serializable classloader attack
         */
        BundleScrubber.scrub(getIntent());
        BundleScrubber.scrub(getIntent().getBundleExtra(com.twofortyfouram.locale.Intent.EXTRA_BUNDLE));

        setContentView(R.layout.locale_plugin_main);

        if (Build.VERSION.SDK_INT >= 11)
        {
            CharSequence callingApplicationLabel = null;
            try
            {
                callingApplicationLabel = getPackageManager().getApplicationLabel(getPackageManager().getApplicationInfo(getCallingPackage(), 0));
            }
            catch (final NameNotFoundException e)
            {
                if (Constants.IS_LOGGABLE)
                {
                    Log.e(Constants.LOG_TAG, "Calling package couldn't be found", e); //$NON-NLS-1$
                }
            }
            if (null != callingApplicationLabel)
            {
                setTitle(callingApplicationLabel);
            }
        }
        else
        {
            setTitle(BreadCrumber.generateBreadcrumb(getApplicationContext(), getIntent(), getString(R.string.plugin_name)));
        }

        // populate the spinner
		Spinner spinnerCommands = (Spinner) findViewById(R.id.spinnerCommands);
		
		String colors[] = {"", "Red","Blue","White","Yellow","Black", "Green","Purple","Orange","Grey"};
		ArrayList<String> myCommands = CollectionManager.getInstance(this).getAvailableCommands();

		ArrayAdapter spinnerCommandAdapter =
				new ArrayAdapter(this, android.R.layout.simple_spinner_item, myCommands);
		spinnerCommandAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	    
		spinnerCommands.setAdapter(spinnerCommandAdapter);

		// add listener for handling selections
		spinnerCommands.setOnItemSelectedListener(this);

        /*
         * if savedInstanceState is null, then then this is a new Activity instance and a check for EXTRA_BUNDLE is needed
         */
        if (null == savedInstanceState)
        {
            final Bundle forwardedBundle = getIntent().getBundleExtra(com.twofortyfouram.locale.Intent.EXTRA_BUNDLE);

            if (PluginBundleManager.isBundleValid(forwardedBundle))
            {
            	// PluginBundleManager.isBundleValid must be changed if elements are added to the bundle
            	// 
                ((EditText) findViewById(R.id.command)).setText(forwardedBundle.getString(PluginBundleManager.BUNDLE_EXTRA_STRING_COMMAND));
                Log.i(TAG, "Retrieved from Bundle: " + forwardedBundle.getString(PluginBundleManager.BUNDLE_EXTRA_STRING_COMMAND));
            }
        }
        
        String command = ((EditText) findViewById(R.id.command)).getText().toString();

		int pos=((ArrayAdapter)spinnerCommands.getAdapter()).getPosition(command);
        spinnerCommands.setSelection(pos);

        /*
         * if savedInstanceState isn't null, there is no need to restore any Activity state directly via onSaveInstanceState(), as
         * the EditText object handles that automatically
         */
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void finish()
    {
        if (mIsCancelled)
        {
            setResult(RESULT_CANCELED);
        }
        else
        {
            // command is copied from spinner
            final String command = ((EditText) findViewById(R.id.command)).getText().toString();

            /*
             * If the message is of 0 length, then there isn't a setting to save.
             */
            if (0 == command.length())
            {
                setResult(RESULT_CANCELED);
            }
            else
            {
                /*
                 * This is the result Intent to Locale
                 */
                final Intent resultIntent = new Intent();

                /*
                 * This extra is the data to ourselves: either for the Activity or the BroadcastReceiver. Note that anything
                 * placed in this Bundle must be available to Locale's class loader. So storing String, int, and other standard
                 * objects will work just fine. However Parcelable objects must also be Serializable. And Serializable objects
                 * must be standard Java objects (e.g. a private subclass to this plug-in cannot be stored in the Bundle, as
                 * Locale's classloader will not recognize it).
                 */
                final Bundle resultBundle = new Bundle();
                resultBundle.putInt(PluginBundleManager.BUNDLE_EXTRA_INT_VERSION_CODE, Constants.getVersionCode(this));
                resultBundle.putString(PluginBundleManager.BUNDLE_EXTRA_STRING_COMMAND, command);
                
                Log.i(TAG, "Saved Bundle: " + command);

                resultIntent.putExtra(com.twofortyfouram.locale.Intent.EXTRA_BUNDLE, resultBundle);

                /*
                 * This is the blurb concisely describing what your setting's state is. This is simply used for display in the UI.
                 */
                if (command.length() > getResources().getInteger(com.twofortyfouram.locale.platform.R.integer.twofortyfouram_locale_maximum_blurb_length))
                {
                    resultIntent.putExtra(com.twofortyfouram.locale.Intent.EXTRA_STRING_BLURB, command.substring(0, getResources().getInteger(com.twofortyfouram.locale.platform.R.integer.twofortyfouram_locale_maximum_blurb_length)));
                }
                else
                {
                    resultIntent.putExtra(com.twofortyfouram.locale.Intent.EXTRA_STRING_BLURB, command);
                }

                setResult(RESULT_OK, resultIntent);
            }
        }

        super.finish();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onCreateOptionsMenu(final Menu menu)
    {
        super.onCreateOptionsMenu(menu);

        /*
         * inflate the default menu layout from XML
         */
        getMenuInflater().inflate(com.twofortyfouram.locale.platform.R.menu.twofortyfouram_locale_help_save_dontsave, menu);

        /*
         * Set up the breadcrumbs for the ActionBar
         */
        if (Build.VERSION.SDK_INT >= 11)
        {
            /*
             * Lazily instantiated class ensures compatibility with Dalvik on Android 1.6 devices
             */
            new Runnable()
            {
                public void run()
                {
                    getActionBar().setSubtitle(BreadCrumber.generateBreadcrumb(getApplicationContext(), getIntent(), getString(R.string.plugin_name)));
                }
            }.run();
        }
        /*
         * Dynamically load the home icon from the host package for Ice Cream Sandwich or later. Note that this leaves Honeycomb
         * devices without the host's icon in the ActionBar, but eventually all Honeycomb devices should receive an OTA to Ice
         * Cream Sandwich so this problem will go away.
         */
        if (Build.VERSION.SDK_INT >= 14)
        {
            /*
             * Lazily instantiated class ensures compatibility with Dalvik on Android 1.6 devices
             */
            new Runnable()
            {
                public void run()
                {
                    getActionBar().setDisplayHomeAsUpEnabled(true);

                    /*
                     * Note: There is a small TOCTOU error here, in that the host could be uninstalled right after launching the
                     * plug-in. That would cause getApplicationIcon() to return the default application icon. It won't fail, but
                     * it will return an incorrect icon.
                     *
                     * In practice, the chances that the host will be uninstalled while the plug-in UI is running are very slim.
                     */
                    try
                    {
                        getActionBar().setIcon(getPackageManager().getApplicationIcon(getCallingPackage()));
                    }
                    catch (final NameNotFoundException e)
                    {
                        if (Constants.IS_LOGGABLE)
                        {
                            Log.w(Constants.LOG_TAG, "An error occurred loading the host's icon", e); //$NON-NLS-1$
                        }
                    }
                }
            }.run();
        }

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onMenuItemSelected(final int featureId, final MenuItem item)
    {
        final int id = item.getItemId();

        /*
         * Royal pain in the butt to support the home button in SDK 11's ActionBar
         */
        if (Build.VERSION.SDK_INT >= 11)
        {
            try
            {
                if (id == android.R.id.class.getField("home").getInt(null)) //$NON-NLS-1$
                {
                    finish();
                    return true;
                }
            }
            catch (final NoSuchFieldException e)
            {
                // this should never happen under API 11 or greater
                throw new RuntimeException(e);
            }
            catch (final IllegalAccessException e)
            {
                // this should never happen under API 11 or greater
                throw new RuntimeException(e);
            }
        }

        if (id == com.twofortyfouram.locale.platform.R.id.twofortyfouram_locale_menu_help)
        {
            try
            {
                startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(HELP_URL)));
            }
            catch (final Exception e)
            {
                Toast.makeText(getApplicationContext(), com.twofortyfouram.locale.platform.R.string.twofortyfouram_locale_application_not_available, Toast.LENGTH_LONG).show();
            }

            return true;
        }
        else if (id == com.twofortyfouram.locale.platform.R.id.twofortyfouram_locale_menu_dontsave)
        {
            mIsCancelled = true;
            finish();
            return true;
        }
        else if (id == com.twofortyfouram.locale.platform.R.id.twofortyfouram_locale_menu_save)
        {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    
	/**
	 * Take the change of selection from the spinners into account and refresh the ListView
	 * with the right data
	 */
	public void onItemSelected(AdapterView<?> parent, View v, int position, long id)
	{
		// id is in the order of the spinners, 0 is stat, 1 is stat_type
        // command is copied from spinner
        String selectedCommand = ((Spinner) findViewById(R.id.spinnerCommands)).getSelectedItem().toString();
        Log.i(TAG, "Selection from spinner: " + selectedCommand);
        EditText etCommand = (EditText) findViewById(R.id.command);
        etCommand.setText(selectedCommand);
        
	}
	
	public void onNothingSelected(AdapterView<?> parent)
	{
		EditText etCommand = (EditText) findViewById(R.id.command);
		etCommand.setText("");
	}


}