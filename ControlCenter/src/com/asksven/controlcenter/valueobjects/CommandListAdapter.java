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


import java.util.List;

import com.asksven.controlcenter.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.ToggleButton;



public class CommandListAdapter extends ArrayAdapter
{

	private final LayoutInflater m_myInflater;
	private final int m_iLayoutRes;
	private List m_myItems;

	public CommandListAdapter(Context context, int resource, List items)
	{
		super(context, R.layout.row_command, items);
		m_myItems = items;
		m_iLayoutRes = resource;
		m_myInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public View getView(int position, View convertView, ViewGroup parent)
	{
		View view;
		if (convertView == null)
		{
			view = m_myInflater.inflate(m_iLayoutRes, parent, false);
		}
		else
		{
			view = convertView;
		}
		
		
		Command myCommand = (Command) m_myItems.get(position);
		
		TextView myCommandText=(TextView)view.findViewById(R.id.TextViewCommand);
		myCommandText.setText(myCommand.getName());


		TextView myCommandStateCmd=(TextView)view.findViewById(R.id.TextViewStateCommand);
		myCommandStateCmd.setText(myCommand.getCommandStatus());
		
		TextView myCommandState=(TextView)view.findViewById(R.id.TextViewState);
		myCommandState.setText(myCommand.getStatus());
		
		
		// determine status based on state and regex
		ToggleButton myCommandStatus=(ToggleButton)view.findViewById(R.id.ToggleButton);
		myCommandStatus.setClickable(false);

		// use state and regex to determine status
		myCommandStatus.setChecked(myCommand.isOn());
		
		myCommandText.setClickable(false);
		myCommandText.setEnabled(true);
		myCommandText.setFocusable(false);		
		
		return(view);
	}
}


