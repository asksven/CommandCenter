/*
 * Copyright (C) 2011-2012 asksven
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

package com.asksven.commandcenter.valueobjects;

import java.util.ArrayList;
import java.util.List;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


/**
 * DBHelper class.  
 * 
 * Database layer for cell log data
 */

public class CommandDBHelper
{
	private static final String DATABASE_NAME	= "command_center";
    private static final String TABLE_DBVERSION = "dbversion";
    private static final String TABLE_NAME 		= "commands";
    private static final int DATABASE_VERSION 	= 1;
    private static final String TAG 			= "CommandDBHelper";
    private static final String[] COLS 			= new String[] {
    		"id", "name", "command", "command_values", "command_status",
    		"tags", "suexec", "regexstatus", "matchregexison", "description", "processresult"};

    Context myCtx;

    private static final String DBVERSION_CREATE = 
    	"create table " + TABLE_DBVERSION + " ("
    		+ "version integer not null);";
    
    private static final String DBVERSION_DROP =
    	" drop table " + TABLE_DBVERSION + ";";

    private static final String TABLE_CREATE =
        "create table " + TABLE_NAME + " ("
    	    + "id integer primary key autoincrement, "
            + "name text not null, "
            + "command text, "
            + "command_values text, "
            + "command_status text, "
            + "command_group text, "
            + "command_set text,"
            + "favorite integer,"
            + "regexstatus text,"
            + "matchregexison int,"
            + "tags text,"
            + "suexec int,"
            + "description text,"
            + "processresult int"
            + ");";
    
    private static final String TABLE_MIGRATE_1_2 =
    	"alter table " + TABLE_NAME + " add column processresult int";

    private static final String TABLE_DROP =
    	"drop table " + TABLE_NAME + ";";

    private SQLiteDatabase db;

    /**
     * 
     * @param ctx
     */
    public CommandDBHelper(Context ctx)
    {
    	myCtx = ctx;
		try
		{
			db = myCtx.openOrCreateDatabase(DATABASE_NAME, 0,null);

			// Check for the existence of the DBVERSION table
			// If it doesn't exist than create the overall data,
			// otherwise double check the version
			Cursor c =
				db.query("sqlite_master", new String[] { "name" },
						"type='table' and name='"+TABLE_DBVERSION+"'", null, null, null, null);
			int numRows = c.getCount();
			if (numRows < 1)
			{
				CreateDatabase(db);
				populateDatabase();
			}
			else
			{
				int version=0;
				Cursor vc = db.query(true, TABLE_DBVERSION, new String[] {"version"},
						null, null, null, null, null,null);
				if(vc.getCount() > 0) {
				    vc.moveToLast();
				    version=vc.getInt(0);
				}
				vc.close();
				if (version!=DATABASE_VERSION)
				{
					Log.e(TAG,"database version mismatch");
					MigrateDatabase(db, version, DATABASE_VERSION);
//					deleteDatabase();
//					CreateDatabase(db);
//					populateDatabase();
				}
			}
			c.close();
			

		}
		catch (SQLException e)
		{
			Log.d(TAG,"SQLite exception: " + e.getLocalizedMessage());
		}
		finally 
		{
			db.close();
		}
    }

    private void CreateDatabase(SQLiteDatabase db)
    {
		try
		{
			db.execSQL(DBVERSION_CREATE);
			ContentValues args = new ContentValues();
			args.put("version", DATABASE_VERSION);
			db.insert(TABLE_DBVERSION, null, args);

			db.execSQL(TABLE_CREATE);
		}
		catch (SQLException e)
		{
			Log.d(TAG,"SQLite exception: " + e.getLocalizedMessage());
		} 
    }
    
    private void MigrateDatabase(SQLiteDatabase db, int fromVersion, int toVersion)
    {
		try
		{
			if ((fromVersion == 1)&&(toVersion == 2))
			{
				db.execSQL(TABLE_MIGRATE_1_2);
				ContentValues args = new ContentValues();
				args.put("version", DATABASE_VERSION);
				db.insert(TABLE_DBVERSION, null, args);
			}
			if ((fromVersion == 3)&&(toVersion == 4))
			{
				deleteDatabase();
				CreateDatabase(db);
				populateDatabase();
			}
		}
		catch (SQLException e)
		{
			Log.d(TAG,"SQLite exception: " + e.getLocalizedMessage());
		} 
    }

    public void deleteDatabase()
    {
        try
        {
			db = myCtx.openOrCreateDatabase(DATABASE_NAME, 0,null);
			db.execSQL(TABLE_DROP);
			db.execSQL(DBVERSION_DROP);
        }
        catch (SQLException e)
		{
			Log.d(TAG,"SQLite exception: " + e.getLocalizedMessage());
		}
        finally 
		{
			db.close();
		}    	
    }
    
    public void populateDatabase()
    {
    	List<Command> samples = new ArrayList<Command>();
//    	samples.add(new Command("Turn off SD", "echo 0 > /dbgfs/vreg/gp6", "", "", "Power, xdandroid", 0, "", 0));
//    	samples.add(new Command("Charger", "echo ?? > /dbgfs/htc_battery/charger_state", "0:off|1:slow|2:fast", "", "Power, xdandroid", 0, "", 0));
//    	samples.add(new Command("Underclock", "echo 19200 >/sys/devices/system/cpu/cpu0/cpufreq/scaling_min_freq", "", "", "Power, xdandroid", 0, "", 0));
//    	samples.add(new Command("Set CPU VDDs", "echo ?? > /dbgfs/acpu_dbg/acpu_vdd", "0,3,3,3,3,3,3,3,7:1|0,0,0,2,2,2,2,2,3,3:2|0,0,0,0,0,0,0,0,0:3", "", "Power, xdandroid", 0, "", 0));
//    	samples.add(new Command("Reset CPU VDDs", "echo 1 > /dbgfs/acpu_dbg/acpu_vdd_reset", "", "", "Power, xdandroid", 0, "", 0));
//    	samples.add(new Command("LedEffects", "echo ?? > /dbgfs/micropklt_dbg/effects", "0:off|5:rotate", "", "IO, xdandroid", 0, "", 0));
    	samples.add(new Command("Dump dmesg", "dmesg > ??/dmesg_`date +\"%Y%m%d-%H%M\"`", "??pickdir??:/sdcard", "", "Logging, xdandroid", 0, "", 0, "", 0));
    	samples.add(new Command("Dump last_kmsg", "cat /proc/last_kmsg > /sdcard/last_kmsg_`date +\"%Y%m%d-%H%M\"`", "", "", "Logging, xdandroid", 0, "", 0, "", 0));
//    	samples.add(new Command("Dump logcat radio", "logcat -d -b radio -f /sdcard/logcat_radio_`date +\"%Y%m%d-%H%M\"`", "", "", "Logging, xdandroid", 0, "", 0));
//    	samples.add(new Command("Dump logcat", "logcat -d -f /sdcard/logcat_radio_`date +\"%Y%m%d-%H%M\"`", "", "", "Logging, xdandroid", 0, "", 0));
//    	samples.add(new Command("Enable PM debugging", "echo ?? > /sys/module/pm/parameters/debug_mask", "0|1|2|4|8|16|32|64", "cat /sys/module/pm/parameters/debug_mask", "Debugging, xdandroid", 0, "0", 0));
////    	samples.add(new Command("Enable gpio debugging", "echo ?? > /sys/module/gpio/parameters/debug_mask", "echo 0 > /sys/module/gpio/parameters/debug_mask", "cat /sys/module/gpio/parameters/debug_mask", "Debugging", "xdandroid", 0, "7", 1));
//    	samples.add(new Command("Enable irq debugging", "echo ?? > /sys/module/irq/parameters/debug_mask", "0|1|2|4|8", "cat /sys/module/irq/parameters/debug_mask", "Debugging, xdandroid", 0, "0", 0));
//    	samples.add(new Command("Enable smd debugging", "echo ?? > /sys/module/smd/parameters/debug_mask", "0|3", "cat /sys/module/smd/parameters/debug_mask", "Debugging, xdandroid", 0, "0", 0));
//    	samples.add(new Command("Set mass storage", "echo ?? > /sys/devices/platform/usb_mass_storage/lun0/file", "??pickfile??:/dev", "", "Debugging, xdandroid", 0, "", 0));
//    	samples.add(new Command("delete a file", "rm ??", "??pickfile??:/sdcard", "", "Debugging, xdandroid", 0, "", 0));
//    	samples.add(new Command("Desire led on", "echo 1 > /sys/devices/platform/leds-microp/leds/??/brightness", "blue:blue|amber:amber|green:green|ledsbutton-backlight:backlight", "", "IO, desire", 0, "", 0));
//    	samples.add(new Command("Desire led off", "echo 0 > /sys/devices/platform/leds-microp/leds/??/brightness", "blue:blue|amber:amber|green:green|ledsbutton-backlight:backlight", "", "IO, desire", 0, "", 0));
    	save(samples);
    }
    
    
    /**
     * Close database connection
     */
    public void close()
    {
    /*
    	try {
    		db.close();
	    } catch (SQLException e)
	    {
	    	Log.d(TAG,"close exception: " + e.getLocalizedMessage());
	    }
	    */
    }
	
    /**
     * Populates a database buffer from a value object
     * @param val the database buffer
     * @param record a value object
     */
    void populateValues(ContentValues val, Command record)
    {
    	val.put("name", record.getName());
    	val.put("command", record.getCommand());
    	val.put("command_values", record.getCommandValues());
    	val.put("command_status", record.getCommandStatus());
    	val.put("tags", record.getTags());
    	val.put("regexstatus", record.getRegexStatus());
    	val.put("matchregexison", record.getMatchRegexOn());
    	val.put("tags", record.getTags());
    	val.put("suexec", record.getSuExec());
    	val.put("description", record.getDescription());
    	val.put("processresult", record.getProcessResult());

    }
	/**
	 * 
	 * @param entry
	 */
	public void addCommand(Command entry)
	{
		ContentValues initialValues = new ContentValues();
		populateValues(initialValues, entry);
	
	    try
	    {
			db = myCtx.openOrCreateDatabase(DATABASE_NAME, 0,null);
	        long lRes =db.insert(TABLE_NAME, null, initialValues);
	        if (lRes == -1)
	        {
	        	Log.d(TAG,"Error inserting row");
	        }
		}
	    catch (SQLException e)
		{
			Log.d(TAG,"SQLite exception: " + e.getLocalizedMessage());
		}
		finally 
		{
			db.close();
		}
	}
	
	/**
	 * 
	 * @param Id
	 */
	public void deleteCommand(long Id)
	{
	    try
	    {
			db = myCtx.openOrCreateDatabase(DATABASE_NAME, 0,null);
			db.delete(TABLE_NAME, "id=" + Id, null);
		}
	    catch (SQLException e)
		{
			Log.d(TAG,"SQLite exception: " + e.getLocalizedMessage());
		}
	    finally 
		{
			db.close();
		}
	}

	/**
	 * 
	 * @return
	 */
	public Command fetchCommandByKey(int iKey)
	{
	    Command myRet = null;
	    try
	    {
			db = myCtx.openOrCreateDatabase(DATABASE_NAME, 0,null);
	        Cursor c;
	        c = db.query(TABLE_NAME, COLS, "id=" + iKey, null, null, null, null);
	        int numRows = c.getCount();
	        c.moveToFirst();
	        if (numRows == 1)
	        {

	        	// cctor with id, name, command, command_status
	            myRet = createCommandFromRow(c);	           
	        }
	        c.close();
		}
	    catch (SQLException e)
		{
			Log.d(TAG,"SQLite exception: " + e.getLocalizedMessage());
		}
	    finally 
		{
			db.close();
		}
	    return myRet;
	}

	public CommandCollection getCommandCollection()
	{
		CommandCollection myRet = new CommandCollection();
		myRet.setTitle(CollectionManager.USER_COLLECTION_NAME);
		myRet.setEntries(fetchAllRows());
		
		return myRet;
	}
	/**
	 * 
	 * @return
	 */
	private List<Command> fetchAllRows()
	{
	    ArrayList<Command> ret = new ArrayList<Command>();
	    try
	    {
			db = myCtx.openOrCreateDatabase(DATABASE_NAME, 0,null);
	        Cursor c;
	        c = db.query(TABLE_NAME, COLS, null, null, null, null, null);
	        int numRows = c.getCount();
	        c.moveToFirst();
	        for (int i = 0; i < numRows; ++i)
	        {

	        	// cctor with id, name, command, command_status
	            Command row = createCommandFromRow(c);
	           
	            ret.add(row);
	            c.moveToNext();
	        }
	        c.close();
		}
	    catch (SQLException e)
		{
			Log.d(TAG,"SQLite exception: " + e.getLocalizedMessage());
		}
	    finally 
		{
			db.close();
		}
	    return ret;
	}

	Command createCommandFromRow(Cursor c)
	{
        Command myRet = new Command(
				c.getInt(c.getColumnIndex("id")),
				c.getString(c.getColumnIndex("name")),
				c.getString(c.getColumnIndex("command")),
				c.getString(c.getColumnIndex("command_values")),
				c.getString(c.getColumnIndex("command_status")),
				c.getString(c.getColumnIndex("tags")),
				c.getInt(c.getColumnIndex("suexec")),
				c.getString(c.getColumnIndex("regexstatus")),
				c.getInt(c.getColumnIndex("matchregexison")),
				c.getString(c.getColumnIndex("description")),
				c.getInt(c.getColumnIndex("processresult")));
        return myRet;
	}

	
	/**
	 * Updates a command record
	 * @param Id the key
	 * @param entry a Command instance
	 */
	public void updateCommand(long Id, Command entry)
	{
	    ContentValues args = new ContentValues();
	    populateValues(args, entry);
	    args.put("id", Id);
	    
	    try
	    {
			db = myCtx.openOrCreateDatabase(DATABASE_NAME, 0,null);
			int iRes = db.update(TABLE_NAME, args, "id=" + Id, null);
			if (iRes == 0)
			{
				Log.e(getClass().getSimpleName(), "Update failed: " + iRes + " rows were updated");
			}
		}
	    catch (SQLException e)
		{
			Log.d(TAG,"SQLite exception: " + e.getLocalizedMessage());
		}
	    finally 
		{
			db.close();
		}
	}
	
	public void save(List<Command> items)
	{
		for (int i=0; i< items.size(); i++)
		{
			if (items.get(i).getId() == -1)
			{
				// add
				Log.i(getClass().getSimpleName(), "adding commands to the database: " + items.get(i).toString());
				addCommand(items.get(i));
			}
			else
			{
				// update
				Log.i(getClass().getSimpleName(), "updating command to the database: " + items.get(i).toString());
				updateCommand(items.get(i).getId(), items.get(i));
			}
		}
	}

}
	
