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

package com.asksven.controlcenter.valueobjects;

import com.asksven.controlcenter.exec.Exec;
import com.asksven.controlcenter.exec.ExecResult;

public class Command
{
	private int id;
	private String name;
	private String command;
	private String commandvalues;
	private String commandstatus;
	private String regexstatus;
	private int matchregexison;
	private String tags;
	private int m_iFavorite;
	
	
	public Command(String strName, String strCommand, String strCommandValues,
			String strCommandStatus, String strTags,
			int iFavorite, String strRegexStatus, int iMatchRegexIsOn)
	{
		id=-1;
		name = strName;
		command = strCommand;
		commandvalues = strCommandValues;
		commandstatus = strCommandStatus;
		tags = strTags;
		m_iFavorite = iFavorite;
		regexstatus = strRegexStatus;
		matchregexison = iMatchRegexIsOn;
		
	}
	
	public Command(int iId, String strName, String strCommand, String strCommandValues,
			String strCommandStatus, String strTags,
			int iFavorite, String strRegexStatus, int iMatchRegexIsOn)
	{
		id=iId;
		name = strName;
		command = strCommand;
		commandvalues = strCommandValues;
		commandstatus = strCommandStatus;
		tags = strTags;
		m_iFavorite = iFavorite;
		regexstatus = strRegexStatus;
		matchregexison = iMatchRegexIsOn;

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
	
	public String getTags()
	{
		return tags;
	}

	public void setGroup(String group)
	{
		this.tags = group;
	}

	public int getFavorite()
	{
		return m_iFavorite;
	}

	public void setFavorite(int favorite)
	{
		this.m_iFavorite = favorite;
	}

	public int getMatchRegexOn()
	{
		return matchregexison;
	}

	public void getMatchRegexOn(int match)
	{
		matchregexison = match;
	}

	
	
	public String execute(boolean bHasRoot)
	{
		String strRet="";
		
		if (this.getCommand().length() != 0)
		{
			if (bHasRoot)
			{
				Exec.suExec(this.getCommand());
			}
			else
			{
				Exec.shExec(this.getCommand());
			}
			
			if (commandstatus.length() != 0)
			{
				strRet = Command.exec(commandstatus, false);
			}
		}
		return strRet;
	}

	/** replace placeholder with strValue and execute */
	public String execute(String strValue, boolean bSu)
	{
		
		String strRet="";
		
		if (this.getCommand().length() != 0)
		{
			String strCommand = this.replace( this.getCommand(), "??", strValue);
			if (bSu)
			{
			  Exec.suExec(strCommand);
			}
			else
			{
				Exec.shExec(strCommand);
			}
			
			if (commandstatus.length() != 0)
			{
				strRet = Command.exec(commandstatus, false);
			}
		}
		return strRet;
	}

	public String getStatus()
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

	/** returns the state of the command: the state is false if the command is no toggle, else it depends on regex */
	public boolean isOn()
	{
		boolean bRet = false;
		if (!this.getStatus().equals(""))
		{
			if (this.getMatchRegexOn()==1)
			{
				bRet = this.getStatus().equals(this.getRegexStatus());
			}
			else
			{
				bRet = !this.getStatus().equals(this.getRegexStatus());
			}
		}
		return bRet;
	}

	public static String exec(String strCommand, boolean bSu)
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
	
}
