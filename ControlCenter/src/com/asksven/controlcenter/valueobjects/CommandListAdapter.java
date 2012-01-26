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
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;



public class CommandListAdapter extends BaseAdapter
{

	private List m_myItems;
	private Context m_context;

	public CommandListAdapter(Context context, List items)
	{
		m_myItems = items;
		m_context = context;
	}

	public View getView(int position, View convertView, ViewGroup parent)
	{
        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) m_context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_command, null);
        }
		
		
		Command myCommand = (Command) m_myItems.get(position);
		
		TextView myCommandText=(TextView)convertView.findViewById(R.id.TextViewCommand);
		myCommandText.setText(myCommand.getName());


		TextView myCommandStateCmd=(TextView)convertView.findViewById(R.id.TextViewStateCommand);
		myCommandStateCmd.setText(myCommand.getCommandStatus());
		
		TextView myCommandState=(TextView)convertView.findViewById(R.id.TextViewState);
		myCommandState.setText(myCommand.getStatus());
		
		
		// determine status based on state and regex
		ToggleButton myCommandStatus=(ToggleButton)convertView.findViewById(R.id.ToggleButton);
		myCommandStatus.setClickable(false);

		// use state and regex to determine status
		myCommandStatus.setChecked(myCommand.isOn());
		
		myCommandText.setClickable(false);
		myCommandText.setEnabled(true);
		myCommandText.setFocusable(false);		
		
		return(convertView);
	}
	
    public int getCount()
    {
    	int ret = 0;
    	if (m_myItems != null)
    	{
    		ret = m_myItems.size();
    	}
    	return ret;
    }
    
    public Object getItem(int position)
    {
        return m_myItems.get(position);
    }

    public long getItemId(int position)
    {
        return position;
    }


}


