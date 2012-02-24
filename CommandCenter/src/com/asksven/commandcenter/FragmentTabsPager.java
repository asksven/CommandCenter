package com.asksven.commandcenter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import com.asksven.commandcenter.ReadmeActivity;
import com.asksven.commandcenter.valueobjects.CollectionManager;
import com.asksven.commandcenter.R;

/**
 * Combining a TabHost with a ViewPager
 */
public class FragmentTabsPager extends FragmentActivity
{
    TabHost mTabHost;
    ViewPager  mViewPager;
    TabsAdapter mTabsAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_tabs);
        mTabHost = (TabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup();

        mViewPager = (ViewPager)findViewById(R.id.pager);

        mTabsAdapter = new TabsAdapter(this, mTabHost, mViewPager);

        Set<String> collections = CollectionManager.getInstance(this).getCollectionNames();
        Iterator<String> myIt = collections.iterator();
        Bundle tabArgs = null;
        int iIdForMenu = 0;
        while (myIt.hasNext())
        {
        	
        	String strName = myIt.next();
        	tabArgs = new Bundle();
        	tabArgs.putString("collection", strName);
        	// required to give each fragment a unique ID
        	tabArgs.putInt("id", iIdForMenu++);
        	mTabsAdapter.addTab(mTabHost.newTabSpec(strName).setIndicator(strName),
                BasicMasterFragment.class, tabArgs);
        }

        if (savedInstanceState != null)
        {
            mTabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
        }
        
        // Show release notes when first starting a new version
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		String strLastRelease	= sharedPrefs.getString("last_release", "0");
		String strCurrentRelease = "";

		try
		{
			PackageInfo pinfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			
	    	strCurrentRelease = Integer.toString(pinfo.versionCode);
		}
		catch (Exception e)
		{
			// nop strCurrentRelease is set to ""
		}

		if (!strLastRelease.equals(strCurrentRelease))
    	{
    		// show the readme
	    	Intent intentReleaseNotes = new Intent(this, ReadmeActivity.class);
	    	intentReleaseNotes.putExtra("filename", "readme.html");
	        this.startActivity(intentReleaseNotes);
	        
	        // save the current release to properties so that the dialog won't be shown till next version
	        SharedPreferences.Editor editor = sharedPrefs.edit();
	        editor.putString("last_release", strCurrentRelease);
	        editor.commit();
    	}

    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putString("tab", mTabHost.getCurrentTabTag());
    }

    /** 
     * Add menu items
     * 
     * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
     */
    public boolean onCreateOptionsMenu(Menu menu)
    {  
    	MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);
        return true;
    }  

    // handle menu selected
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {  
	        case R.id.preferences:  
	        	Intent intentPrefs = new Intent(this, PreferencesActivity.class);
	            this.startActivity(intentPrefs);
	        	break;	

//	        case R.id.refresh:
//            	// Refresh
////	        	doRefresh();
//            	break;
            case R.id.about:
            	// About
            	Intent intentAbout = new Intent(this, AboutActivity.class);
                this.startActivity(intentAbout);
            	break;
	        case R.id.release_notes:
            	// Release notes
            	Intent intentReleaseNotes = new Intent(this, ReadmeActivity.class);
            	intentReleaseNotes.putExtra("filename", "readme.html");
                this.startActivity(intentReleaseNotes);
            	break;	
	
//	        case R.id.test:	
//	        	CommandDBHelper db = new CommandDBHelper(this);
//	        	List<Command> myCommands = db.fetchAllRows();
//	        	CommandCollection myCollection = new CommandCollection();
//	        	myCollection.setTitle("This is a test collection");
//	        	myCollection.setVersion(1L);
//	        	myCollection.setHomepage("http://cri.ch/sven");
//	        	myCollection.setUpdaterUrl("http://cri.ch/sven/test.json");
//	        	myCollection.setIconFile("icon.gif");
//	        	myCollection.setEntries(myCommands);
//	        	
//	        	CommandReaderWriter.writeFile(this, myCollection, "commands.json");
//	        	break;
//	    		ExecResult myRes = Exec.execPrint("su -c cat /data/dropbear/.ssh/authorized_keys");
//	    		String strLine = "";
//	    		if (myRes.getSuccess())
//	    		{
//	    			strLine = myRes.getResultLine();
//	    		}
//	    	    break;

//	        case R.id.test2:	
//	        	CommandCollection myCollection2 = 
//	        		CommandReaderWriter.readFile(this, "commands.json");
//	        	int i = myCollection2.getEntries().size();
//	        	break;
            	
        }  
        return false;  
    }

    
    
    
    

    /** contrib class from com.example.android.supportv4 */
    /**
     * This is a helper class that implements the management of tabs and all
     * details of connecting a ViewPager with associated TabHost.  It relies on a
     * trick.  Normally a tab host has a simple API for supplying a View or
     * Intent that each tab will show.  This is not sufficient for switching
     * between pages.  So instead we make the content part of the tab host
     * 0dp high (it is not shown) and the TabsAdapter supplies its own dummy
     * view to show as the tab content.  It listens to changes in tabs, and takes
     * care of switch to the correct paged in the ViewPager whenever the selected
     * tab changes.
     */
    public static class TabsAdapter extends FragmentPagerAdapter
            implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener
            {
        private final Context mContext;
        private final TabHost mTabHost;
        private final ViewPager mViewPager;
        private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();

        static final class TabInfo
        {
            private final String tag;
            private final Class<?> clss;
            private final Bundle args;

            TabInfo(String _tag, Class<?> _class, Bundle _args)
            {
                tag = _tag;
                clss = _class;
                args = _args;
            }
        }

        static class DummyTabFactory implements TabHost.TabContentFactory
        {
            private final Context mContext;

            public DummyTabFactory(Context context)
            {
                mContext = context;
            }

            @Override
            public View createTabContent(String tag)
            {
                View v = new View(mContext);
                v.setMinimumWidth(0);
                v.setMinimumHeight(0);
                return v;
            }
        }

        public TabsAdapter(FragmentActivity activity, TabHost tabHost, ViewPager pager)
        {
            super(activity.getSupportFragmentManager());
            mContext = activity;
            mTabHost = tabHost;
            mViewPager = pager;
            mTabHost.setOnTabChangedListener(this);
            mViewPager.setAdapter(this);
            mViewPager.setOnPageChangeListener(this);
        }

        public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args)
        {
            tabSpec.setContent(new DummyTabFactory(mContext));
            String tag = tabSpec.getTag();

            TabInfo info = new TabInfo(tag, clss, args);
            mTabs.add(info);
            mTabHost.addTab(tabSpec);
            notifyDataSetChanged();
        }

        @Override
        public int getCount()
        {
            return mTabs.size();
        }

        @Override
        public Fragment getItem(int position)
        {
            TabInfo info = mTabs.get(position);
            return Fragment.instantiate(mContext, info.clss.getName(), info.args);
        }

        @Override
        public void onTabChanged(String tabId)
        {
            int position = mTabHost.getCurrentTab();
            mViewPager.setCurrentItem(position);
            notifyDataSetChanged();
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
        {
        }

        @Override
        public void onPageSelected(int position)
        {
            mTabHost.setCurrentTab(position);
        }

        @Override
        public void onPageScrollStateChanged(int state)
        {
        }
    }
    


}
