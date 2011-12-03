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



/**
 * @author sven
 *
 */
public class CommandCollection
{
    private String title;
    private Long version;
    private List<Command> entries;

    public String getTitle() { return title; }
    public Long getVersion() { return version; }
    public List<Command> getEntries() { return entries; }

    public void setTitle(String title) { this.title = title; }
    public void setVersion(Long version) { this.version = version; }
    public void setEntries(List<Command> entries) { this.entries = entries; }

    public String toString()
    {
        return String.format("title:%s,version:%d,entries:%s", title, version, entries);
    }
    
    public Command findByName(String name)
    {
    	Command ret = null;
    	List<Command> entries = getEntries();
    	
    	for (int i=0; i < entries.size(); i++)
    	{
    		Command comp = (Command) entries.get(i);
    		if (comp.getName().equals(name))
    		{
    			ret = comp;
    			break;
    		}
    	}
    	return ret;
    }
    
    public Command findById(int id)
    {
    	Command ret = null;

    	for (int i=0; i < entries.size(); i++)
    	{
    		Command comp = (Command) entries.get(i);
	    	if (comp.getId() == id)
			{
				ret = comp;
				break;
			}
    	}
    	return ret;
    }
    

}
