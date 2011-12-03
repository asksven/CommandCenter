package com.asksven.controlcenter.exec;

import java.util.ArrayList;

public class ExecResult
{
	protected boolean m_bSuccess;
	protected ArrayList<String> m_oResult;
	protected ArrayList<String> m_oError;
	
	public ExecResult()
	{
		m_oResult = new ArrayList<String>();
		m_oError = new ArrayList<String>();
		
	}
	
	public boolean getSuccess()
	{
		return m_bSuccess;
	}
	
	public String getResultLine()
	{
		String strRes = "";
		if (!m_oResult.isEmpty()) 
		{
			strRes = m_oResult.get(0);
		}
		
		return strRes;
	}
}
