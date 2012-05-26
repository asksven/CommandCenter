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

import java.util.List;



/**
 * @author sven
 *
 */
public class CommandCollection
{
    private String title; 			// the name of the command set
    private String author;			// the author's name
    private Long version;			// the version number (for future use with the updater)
    private String homepage;		// the URL of the doc
    private String updaterurl;		// the URL to check for updates (for future use)
    private String iconfile;		// an icon (for future use)
    private String maintainer;		// the maintainer's name
    transient boolean m_bEditable; 
    
    private List<Command> entries;

    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public Long getVersion() { return version; }
    public String getHomepage() { return homepage; }
    public String getUpdaterUrl() { return updaterurl; }
    public String getIconFile() { return iconfile; }
    public String getMaintainer() { return maintainer; }
    

    public List<Command> getEntries() { return entries; }

    public void setTitle(String title) { this.title = title; }
    public void SetAuthor(String author) { this.author = author; }
    public void setVersion(Long version) { this.version = version; }
    public void setHomepage(String homepage) { this.homepage = homepage; }
    public void setUpdaterUrl(String updaterurl) { this.updaterurl = updaterurl; }
    public void setIconFile(String iconfile) { this.iconfile = iconfile; }
    public void setMaintainer(String maintainer) { this.maintainer = maintainer; }

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
    
    public void setEditable(boolean bEditable)
    {
    	m_bEditable = bEditable;
    }
    
    public boolean isEditable()
    {
    	return m_bEditable;
    }

    protected void updateCache()
    {
    	for (int i=0; i < entries.size(); i++)
    	{
    		((Command) entries.get(i)).updateCache();
    	}
    	
    }
}
