package com.asksven.systemsettings.valueobjects;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceActivity;

public class Preferences
{
	public static final String PREFS_NAME = "CommandCenterPrefs";
	
	/** Prefs handler */
	SharedPreferences m_mySettings;
	
	/** shall we execute or edit command upon select */
	private boolean m_bExecOnSelect;
	
	/** show only favorite commands */
	private boolean m_bShowFavorites;
	
	/** allow execution of remote command */
	private boolean m_bAllowCommand;

	/** is the phone rooted */
	private boolean m_bHasRoot;

	public Preferences(Activity myActivity)
	{
		m_mySettings = myActivity.getSharedPreferences(PREFS_NAME, 0);
		this.init();
	}
	
	public Preferences(Context myContext)
	{
		m_mySettings = myContext.getSharedPreferences(PREFS_NAME, 0);
		this.init();
	}

	public Preferences(SharedPreferences myPrefs)
	{
		m_mySettings = myPrefs;
		this.init();
	}
	
	/** initialize value holder */
	private void init()
	{
	    boolean bExec 	= m_mySettings.getBoolean("execOnSelect", true);
	    boolean bShowFavorites = m_mySettings.getBoolean("showOnlyFavorites", false);
	    boolean bHasRoot = m_mySettings.getBoolean("hasRoot", false);
	    
	    setExecOnSelect(bExec);
	    setShowFavorites(bShowFavorites);
	    setHasRoot(bHasRoot);
	}
	
	public void save()
	{
		
		SharedPreferences.Editor editor = m_mySettings.edit();
		
	    editor.putBoolean("execOnSelect", getExecOnSelect());
	    editor.putBoolean("showOnlyFavorites", getShowFavorites());
	    editor.putBoolean("hasRoot", getHasRoot());
	    
	    editor.commit();
	}
	
	
	/**
	 * @return
	 */
	public boolean getExecOnSelect() {
		return m_bExecOnSelect;
	}
	/**
	 * @param 
	 */
	public void setExecOnSelect(boolean bExec) {
		m_bExecOnSelect = bExec;
	}

	/**
	 * @return
	 */
	public boolean getHasRoot() {
		return m_bHasRoot;
	}
	/**
	 * @param 
	 */
	public void setHasRoot(boolean bExec) {
		m_bHasRoot = bExec;
	}
	
	/**
	 * @return
	 */
	public boolean getShowFavorites() {
		return m_bShowFavorites;
	}
	/**
	 * @param logCells the logCells to set
	 */
	public void setShowFavorites(boolean bFilter) {
		m_bShowFavorites = bFilter;
	}

	/**
	 * @return
	 */
	public boolean getAllowCommand() {
		return m_bAllowCommand;
	}
	/**
	 * @param logCells the logCells to set
	 */
	public void setAllowCommands(boolean bValue) {
		m_bAllowCommand = bValue;
	}
	
}
