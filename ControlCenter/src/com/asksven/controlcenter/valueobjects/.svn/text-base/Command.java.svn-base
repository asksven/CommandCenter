package com.asksven.systemsettings.valueobjects;

import com.asksven.systemsettings.exec.Exec;
import com.asksven.systemsettings.exec.ExecResult;

public class Command
{
	private int m_iId;
	private String m_strName;
	private String m_strCommand;
	private String m_strCommandValues;
	private String m_strCommandStatus;
	private String m_strRegexStatus;
	private int m_iMatchRegexIsOn;
	private String m_strGroup;
	private String m_strSet;
	private int m_iFavorite;
	
	
	public Command(String strName, String strCommand, String strCommandValues, String strCommandStatus, String strGroup, String strSet, int iFavorite, String strRegexStatus, int iMatchRegexIsOn)
	{
		m_iId=-1;
		m_strName = strName;
		m_strCommand = strCommand;
		m_strCommandValues = strCommandValues;
		m_strCommandStatus = strCommandStatus;
		m_strGroup = strGroup;
		m_strSet = strSet;
		m_iFavorite = iFavorite;
		m_strRegexStatus = strRegexStatus;
		m_iMatchRegexIsOn = iMatchRegexIsOn;
		
	}
	
	public Command(int iId, String strName, String strCommand, String strCommandValues, String strCommandStatus, String strGroup, String strSet, int iFavorite, String strRegexStatus, int iMatchRegexIsOn)
	{
		m_iId=iId;
		m_strName = strName;
		m_strCommand = strCommand;
		m_strCommandValues = strCommandValues;
		m_strCommandStatus = strCommandStatus;
		m_strGroup = strGroup;
		m_strSet = strSet;
		m_iFavorite = iFavorite;
		m_strRegexStatus = strRegexStatus;
		m_iMatchRegexIsOn = iMatchRegexIsOn;

	}

	public int getId()
	{
		return m_iId;
	}

	public String getName()
	{
		return m_strName;
	}

	public String getCommand()
	{
		return m_strCommand;
	}

	public String getCommandValues()
	{
		return m_strCommandValues;
	}

	public String getCommandStatus()
	{
		return m_strCommandStatus;
	}

	public String getRegexStatus()
	{
		return m_strRegexStatus;
	}

	
	public String getGroup()
	{
		return m_strGroup;
	}

	public String getSet()
	{
		return m_strSet;
	}

	public int getFavorite()
	{
		return m_iFavorite;
	}

	public int getMatchRegexOn()
	{
		return m_iMatchRegexIsOn;
	}

	public void setFavorite(int iVal)
	{
		m_iFavorite = iVal;
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
			
			if (m_strCommandStatus.length() != 0)
			{
				strRet = Command.exec(m_strCommandStatus, false);
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
			
			if (m_strCommandStatus.length() != 0)
			{
				strRet = Command.exec(m_strCommandStatus, false);
			}
		}
		return strRet;
	}

	public String getStatus()
	{
		String strRet = "";
		if (m_strCommandStatus.length() != 0)
		{
			strRet = Command.exec(m_strCommandStatus, false);
		}

		return strRet;
	}

	public boolean hasValues()
	{
		if (m_strCommandValues.equals(""))
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
