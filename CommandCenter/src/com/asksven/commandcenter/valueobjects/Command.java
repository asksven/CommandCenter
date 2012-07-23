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

package com.asksven.commandcenter.valueobjects;

import java.util.ArrayList;

import android.util.Log;

import com.asksven.commandcenter.exec.Exec;
import com.asksven.commandcenter.exec.ExecResult;

public class Command
{
	static transient final String TAG = "CommandCenter::Command";
	private int id;								// the command's key
	private String name;						// the command's name
	private String command;						// the command that will be executed
	private String commandvalues;				// the variable values in the form <value>:<label>|<value>:<label>, e.g. 1:On|0:Off (if the command uses variables)
	private String commandstatus;				// the command that will be executed to retrieve the status (optional)
	private String regexstatus;					// the regex that will be executed against the command status result (only relevant together with command status)
	private int matchregexison;					// 1 to match the commandstatus equal to regexstatus, 0 for non-equal
	private String tags;						// tags describing the command (for future use)
	private int suexec;							// 1 if the command should be executed with su rights, else 0
	private String description;					// a description of the command
	private int processresult;					// 1 if the result of the command should be displayed, else 0
	transient private String status_cached;
	transient private boolean ison_cached;
	
	public Command()
	{
		id = -1;
	}
	
	public Command(String strName, String strCommand, String strCommandValues,
			String strCommandStatus, String strTags,
			int iSuExec, String strRegexStatus, int iMatchRegexIsOn, String strDescription, int iProcessResult)
	{
		id				= -1;
		name 			= strName;
		command 		= strCommand;
		commandvalues 	= strCommandValues;
		commandstatus 	= strCommandStatus;
		tags 			= strTags;
		suexec 			= iSuExec;
		regexstatus 	= strRegexStatus;
		matchregexison 	= iMatchRegexIsOn;
		description		= strDescription;
		processresult	= iProcessResult;
		
	}
	
	public Command(int iId, String strName, String strCommand, String strCommandValues,
			String strCommandStatus, String strTags,
			int iSuExec, String strRegexStatus, int iMatchRegexIsOn, String strDescription, int iProcessResult)
	{
		id				=iId;
		name 			= strName;
		command 		= strCommand;
		commandvalues 	= strCommandValues;
		commandstatus 	= strCommandStatus;
		tags 			= strTags;
		suexec	 		= iSuExec;
		regexstatus 	= strRegexStatus;
		matchregexison 	= iMatchRegexIsOn;
		description		= strDescription;
		processresult	= iProcessResult;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getCommand()
	{
		return command;
	}

	public void setCommand(String command)
	{
		this.command = command;
	}

	public String getCommandValues()
	{
		return commandvalues;
	}

	public void setCommandValues(String commandValues)
	{
		this.commandvalues = commandValues;
	}

	public String getCommandStatus()
	{
		return commandstatus;
	}

	public void setCommandStatus(String status)
	{
		this.commandstatus = status;
	}

	public String getRegexStatus()
	{
		return regexstatus;
	}

	public void setRegexStatus(String status)
	{
		this.regexstatus = status;
	}

	public void setTags(String strTags)
	{
		tags = strTags;
	}
	
	public String getTags()
	{
		return tags;
	}

	public String getDescription()
	{
		return description;
	}

	public void setProcessResult(int iProcessResult)
	{
		this.processresult = iProcessResult;
	}

	public void setProcessResult(boolean bProcessResult)
	{
		if (bProcessResult)
		{
			this.processresult = 1;
		}
		else
		{
			this.processresult = 0;
		}
	}
	
	public int getProcessResult()
	{
		return processresult;
	}

	public int getSuExec()
	{
		return suexec;
	}

	public void setSuExec(int iSuExec)
	{
		this.suexec = iSuExec;
	}

	public void setSuExec(boolean bSuExec)
	{
		if (bSuExec)
		{
			this.suexec = 1;
		}
		else
		{
			this.suexec = 0;
		}
	}

	
	public int getMatchRegexOn()
	{
		return matchregexison;
	}

	public void setMatchRegexOn(int match)
	{
		matchregexison = match;
	}

	public void setMatchRegexOn(boolean bMatch)
	{
		if (bMatch)
		{
			matchregexison = 1;
		}
		else
		{
			matchregexison = 0;
		}
	}

	public void setDescription(String strDescription)
	{
		description = strDescription;
	}
	
	
	public ArrayList<String> execute(String strValue)
	{
		ArrayList<String> strRet = null;
		
		if (this.getCommand().length() != 0)
		{
			String strCommand = this.replace( this.getCommand(), "??", strValue);
			
			if (this.getSuExec() == 1)
			{
				if (this.processresult == 1)
				{
					strRet = Exec.suExecPrint(strCommand).getResultLines();
				}
				else
				{
					Exec.suExec(strCommand);
				}
			}
			else
			{
				if (this.processresult == 1)
				{
					strRet = Exec.shExecPrint(strCommand).getResultLines();
				}
				else
				{
					Exec.shExec(strCommand);
				}
			}			
		}
		
		updateCache();
		
		return strRet;
	}

	public ArrayList<String> execute()
	{
		return execute("");
	}

	private String getStatus()
	{
		String strRet = "";
		if (commandstatus.length() != 0)
		{
			strRet = Command.exec(commandstatus, false);
		}

		return strRet;
	}

	public boolean hasValues()
	{
		if (commandvalues.equals(""))
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public boolean isOnCached()
	{
		return ison_cached;
	}
	
	public String getStatusCached()
	{
		return status_cached;
	}
	
	/** returns the state of the command: the state is false if the command is no toggle, else it depends on regex */
	private boolean isOn()
	{
		boolean bRet = false;
		if (!this.getStatusCached().equals(""))
		{
			if (this.getMatchRegexOn()==1)
			{
				bRet = this.getStatusCached().equals(this.getRegexStatus());
			}
			else
			{
				bRet = !this.getStatusCached().equals(this.getRegexStatus());
			}
		}
		return bRet;
	}

	private static String exec(String strCommand, boolean bSu)
	{
		String strCmd = strCommand;
		if (bSu)
		{
			strCmd = "su -c " + strCmd;
		}
		
		ExecResult myRes = Exec.execPrint(strCmd);
		
		return myRes.getResultLine();
	}

	private String replace(String str, String pattern, String replace)
	{
		int s = 0;
		int e = 0;
		StringBuffer result = new StringBuffer();
		while ((e = str.indexOf(pattern, s)) >= 0)
		{
			result.append(str.substring(s, e));
			result.append(replace);
			s = e+pattern.length();
		}
		result.append(str.substring(s));
		return result.toString();
	} 
	
	/**
	 * Update the cached values. Note that this call must happen from a thread
	 */
	synchronized protected void updateCache()
	{
		Log.d(TAG, "updateCache called for command " + command);
		status_cached = getStatus();
		ison_cached = isOn();
	}
	
}
