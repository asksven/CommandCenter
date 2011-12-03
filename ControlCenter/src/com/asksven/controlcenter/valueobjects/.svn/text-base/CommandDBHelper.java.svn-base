package com.asksven.systemsettings.valueobjects;

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
	private static final String DATABASE_NAME	= "systemsettings";
    private static final String TABLE_DBVERSION = "dbversion";
    private static final String TABLE_NAME 		= "commands";
    private static final int DATABASE_VERSION 	= 15;
    private static final String TAG 			= "CommandDBHelper";
    private static final String[] COLS 			= new String[] {
    		"id", "name", "command", "command_values", "command_status", "favorite",
    		"command_group", "command_set", "regexstatus", "regexison"};
    
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
            + "regexison int"
            + ");";

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
				    vc.moveToFirst();
				    version=vc.getInt(0);
				}
				vc.close();
				if (version!=DATABASE_VERSION)
				{
					Log.e(TAG,"database version mismatch");
					deleteDatabase();
					CreateDatabase(db);
					populateDatabase();
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
    	samples.add(new Command("Turn off SD", "echo 0 > /dbgfs/vreg/gp6", "", "", "Power", "xdandroid", 0, "", 0));
    	samples.add(new Command("Charger", "echo ?? > /dbgfs/htc_battery/charger_state", "0:off|1:slow|2:fast", "", "Power", "xdandroid", 0, "", 0));
    	samples.add(new Command("Underclock", "echo 19200 >/sys/devices/system/cpu/cpu0/cpufreq/scaling_min_freq", "", "", "Power", "xdandroid", 0, "", 0));
    	samples.add(new Command("Set CPU VDDs", "echo ?? > /dbgfs/acpu_dbg/acpu_vdd", "0,3,3,3,3,3,3,3,7:1|0,0,0,2,2,2,2,2,3,3:2|0,0,0,0,0,0,0,0,0:3", "", "Power", "xdandroid", 0, "", 0));
    	samples.add(new Command("Reset CPU VDDs", "echo 1 > /dbgfs/acpu_dbg/acpu_vdd_reset", "", "", "Power", "xdandroid", 0, "", 0));
    	samples.add(new Command("LedEffects", "echo ?? > /dbgfs/micropklt_dbg/effects", "0:off|5:rotate", "", "IO", "xdandroid", 0, "", 0));
    	samples.add(new Command("Dump dmesg", "dmesg > ??/dmesg_`date +\"%Y%m%d-%H%M\"`", "??pickdir??:/sdcard", "", "Logging", "xdandroid", 0, "", 0));
    	samples.add(new Command("Dump last_kmsg", "cat /proc/last_kmsg > /sdcard/last_kmsg_`date +\"%Y%m%d-%H%M\"`", "", "", "Logging", "xdandroid", 0, "", 0));
    	samples.add(new Command("Dump logcat radio", "logcat -d -b radio -f /sdcard/logcat_radio_`date +\"%Y%m%d-%H%M\"`", "", "", "Logging", "xdandroid", 0, "", 0));
    	samples.add(new Command("Dump logcat", "logcat -d -f /sdcard/logcat_radio_`date +\"%Y%m%d-%H%M\"`", "", "", "Logging", "xdandroid", 0, "", 0));
    	samples.add(new Command("Enable PM debugging", "echo ?? > /sys/module/pm/parameters/debug_mask", "0|1|2|4|8|16|32|64", "cat /sys/module/pm/parameters/debug_mask", "Debugging", "xdandroid", 0, "0", 0));
//    	samples.add(new Command("Enable gpio debugging", "echo ?? > /sys/module/gpio/parameters/debug_mask", "echo 0 > /sys/module/gpio/parameters/debug_mask", "cat /sys/module/gpio/parameters/debug_mask", "Debugging", "xdandroid", 0, "7", 1));
    	samples.add(new Command("Enable irq debugging", "echo ?? > /sys/module/irq/parameters/debug_mask", "0|1|2|4|8", "cat /sys/module/irq/parameters/debug_mask", "Debugging", "xdandroid", 0, "0", 0));
    	samples.add(new Command("Enable smd debugging", "echo ?? > /sys/module/smd/parameters/debug_mask", "0|3", "cat /sys/module/smd/parameters/debug_mask", "Debugging", "xdandroid", 0, "0", 0));
    	samples.add(new Command("Set mass storage", "echo ?? > /sys/devices/platform/usb_mass_storage/lun0/file", "??pickfile??:/dev", "", "Debugging", "xdandroid", 0, "", 0));
    	samples.add(new Command("delete a file", "rm ??", "??pickfile??:/sdcard", "", "Debugging", "xdandroid", 0, "", 0));
    	samples.add(new Command("Desire led on", "echo 1 > /sys/devices/platform/leds-microp/leds/??/brightness", "blue:blue|amber:amber|green:green|ledsbutton-backlight:backlight", "", "IO", "desire", 0, "", 0));
    	samples.add(new Command("Desire led off", "echo 0 > /sys/devices/platform/leds-microp/leds/??/brightness", "blue:blue|amber:amber|green:green|ledsbutton-backlight:backlight", "", "IO", "desire", 0, "", 0));
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
	 * 
	 * @param entry
	 */
	public void addCommand(Command entry)
	{
		ContentValues initialValues = new ContentValues();
	    initialValues.put("name", entry.getName());
	    initialValues.put("command", entry.getCommand());
	    initialValues.put("command_values", entry.getCommandValues());
	    initialValues.put("command_status", entry.getCommandStatus());
	    initialValues.put("command_group", entry.getGroup());
	    initialValues.put("command_set", entry.getSet());
	    initialValues.put("favorite", entry.getFavorite());
	    initialValues.put("regexstatus", entry.getRegexStatus());
	    initialValues.put("regexison", entry.getMatchRegexOn());
	
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
	            myRet = new Command(
	            				c.getInt(c.getColumnIndex("id")),
	            				c.getString(c.getColumnIndex("name")),
	            				c.getString(c.getColumnIndex("command")),
	            				c.getString(c.getColumnIndex("command_values")),
	            				c.getString(c.getColumnIndex("command_status")),
	            				c.getString(c.getColumnIndex("command_group")),
	            				c.getString(c.getColumnIndex("command_set")),
	            				c.getInt(c.getColumnIndex("favorite")),
	            				c.getString(c.getColumnIndex("regexstatus")),
	            				c.getInt(c.getColumnIndex("regexison")));
	           
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

	/**
	 * 
	 * @return
	 */
	public List<Command> fetchAllRows()
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
	            Command row = new Command(
	            				c.getInt(c.getColumnIndex("id")),
	            				c.getString(c.getColumnIndex("name")),
	            				c.getString(c.getColumnIndex("command")),
	            				c.getString(c.getColumnIndex("command_values")),
	            				c.getString(c.getColumnIndex("command_status")),
	            				c.getString(c.getColumnIndex("command_group")),
	            				c.getString(c.getColumnIndex("command_set")),
	            				c.getInt(c.getColumnIndex("favorite")),
	            				c.getString(c.getColumnIndex("regexstatus")),
	            				c.getInt(c.getColumnIndex("regexison")));
	           
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

	public List<Command> fetchFavoriteRows()
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
	            Command row = new Command(
        				c.getInt(c.getColumnIndex("id")),
        				c.getString(c.getColumnIndex("name")),
        				c.getString(c.getColumnIndex("command")),
        				c.getString(c.getColumnIndex("command_values")),
        				c.getString(c.getColumnIndex("command_status")),
        				c.getString(c.getColumnIndex("command_group")),
        				c.getString(c.getColumnIndex("command_set")),
        				c.getInt(c.getColumnIndex("favorite")),
        				c.getString(c.getColumnIndex("regexstatus")),
        				c.getInt(c.getColumnIndex("regexison")));

	            
	            if (row.getFavorite()==1)
	            {
	            	ret.add(row);
	            }
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
	
	
	/**
	 * Updates a command record
	 * @param Id the key
	 * @param entry a Command instance
	 */
	public void updateCommand(long Id, Command entry)
	{
	    ContentValues args = new ContentValues();
	    args.put("id", Id);
	    args.put("name", entry.getName());
	    args.put("command", entry.getCommand());
	    args.put("command_values", entry.getCommandValues());
	    args.put("command_status", entry.getCommandStatus());
	    args.put("command_group", entry.getGroup());
	    args.put("command_set", entry.getSet());
	    args.put("favorite", entry.getFavorite());
	    args.put("regexstatus", entry.getRegexStatus());
	    args.put("regexison", entry.getMatchRegexOn());
	    
	    
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
	
