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

/**
 * @author sven
 *
 */

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;

import com.asksven.controlcenter.exec.DataStorage;
import com.google.gson.Gson;

public class CommandReaderWriter
{
    
    private static final String TAG = "CommandReader";
    
    public static CommandCollection readUrl(String strUrl)
    {
		CommandCollection data = null;

		try
    	{
    		InputStream source = retrieveStream(strUrl);
    		
    		readStream(source);
	
    	}
    	catch (Exception e)
    	{
    		e.printStackTrace();
    	}
    	
    	return data;
    }
    
    public static CommandCollection readFile(Context ctx, String strFileName)
    {
		CommandCollection data = new CommandCollection();

		try
    	{
			FileInputStream source = new FileInputStream(DataStorage.getExternalStoragePath(ctx)
					+ "/" + strFileName);
    		
    		data = readStream(source);
    		
			source.close();
	
    	}
    	catch (Exception e)
    	{
    		e.printStackTrace();
    		// make sure the returned data is consistent
    		data.setEntries(new ArrayList<Command>());
    	}
    	
    	return data;
    }

    public static void writeFile(Context ctx, CommandCollection data, String strFileName)
    {
		try
    	{
			
			FileOutputStream target = new FileOutputStream(DataStorage.getExternalStoragePath(ctx)
					+ "/" + strFileName);
    		
    		writeStream(data, target);
    		
			target.close();
	
    	}
    	catch (FileNotFoundException e)
    	{
    		Log.e(TAG, "File could not be written: " + e.getMessage());
    		e.printStackTrace();
    	}
    	catch (IOException e)
    	{
    		Log.e(TAG, "File could not be closed: " + e.getMessage());
    		e.printStackTrace();
    	}

    }

    public static CommandCollection readStream(InputStream source) throws Exception
    {
		CommandCollection data = null;

    		
		if (source == null)
		{
			return null;
		}
		Gson gson = new Gson();

		Reader reader = new InputStreamReader(source);

		// Now do the magic.
		data = gson.fromJson(reader,
				CommandCollection.class);
    		
	    reader.close();
	    
    	return data;
    }

    public static void writeStream(CommandCollection data, OutputStream target) // throws Exception
    {
    		
		if (target == null)
		{
			return;
		}
		Gson gson = new Gson();

		try
		{
			Writer writer = new OutputStreamWriter(target);
	
			// Now do the magic.
			gson.toJson(data, CommandCollection.class, writer);
			writer.close();
		}
		catch (Exception e)
		{
			Log.e(TAG, "Error while marshalling json file: " + e.getMessage());
		}
    }

    private static InputStream retrieveStream(String url)
    {
        DefaultHttpClient client = new DefaultHttpClient();
        HttpGet getRequest = new HttpGet(url);
        try
        {
           HttpResponse getResponse = client.execute(getRequest);

           final int statusCode = getResponse.getStatusLine().getStatusCode();

           if (statusCode != HttpStatus.SC_OK)
           {
              Log.w(TAG,
                  "Error " + statusCode + " for URL " + url);
              return null;
           }
           HttpEntity getResponseEntity = getResponse.getEntity();
           return getResponseEntity.getContent();
        }
        catch (IOException e)
        {
           getRequest.abort();
           Log.w(TAG, "Error for URL " + url, e);
         }
        return null;
     }
    

}