/**
 * 
 */
package com.asksven.systemsettings;

import java.util.List;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.asksven.systemsettings.R;
import com.asksven.systemsettings.valueobjects.Command;
import com.asksven.systemsettings.valueobjects.CommandDBHelper;
import com.asksven.systemsettings.valueobjects.CommandListAdapter;
import com.asksven.systemsettings.valueobjects.SimpleData;

/**
 * @author sven
 *
 */
/**
 * This is the "top-level" fragment, showing a list of items that the
 * user can pick.  Upon picking an item, it takes care of displaying the
 * data to the user as appropriate based on the currrent UI layout.
 */
public class BasicMasterFragment extends ListFragment
{
    boolean mDualPane;
    int mCurCheckPosition = 0;
    
	private CommandDBHelper m_myDB = null;
    private List<Command> m_myItems;
    private Command m_myCommand = null;


    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        // populate list with our commands, based on preferences
        m_myDB = new CommandDBHelper(getActivity());
        
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        boolean bShowFavs = preferences.getBoolean("showOnlyFavorites", false);
        
        if (!bShowFavs)
        {
        	m_myItems = m_myDB.fetchAllRows();
        }
        else
        {
        	m_myItems = m_myDB.fetchFavoriteRows();
        }

        setListAdapter(new CommandListAdapter(getActivity(), R.layout.row_command, m_myItems));
		
        // Check to see if we have a frame in which to embed the details
        // fragment directly in the containing UI.
        View detailsFrame = getActivity().findViewById(R.id.details);
        mDualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;

        if (savedInstanceState != null)
        {
            // Restore last state for checked position.
            mCurCheckPosition = savedInstanceState.getInt("curChoice", 0);
        }

        if (mDualPane)
        {
            // In dual-pane mode, the list view highlights the selected item.
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            // Make sure our UI is in the correct state.
            showDetails(mCurCheckPosition);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putInt("curChoice", mCurCheckPosition);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id)
    {
    	m_myCommand = m_myItems.get(position);
        showDetails(m_myCommand.getId());
    }

    /**
     * Helper function to show the details of a selected item, either by
     * displaying a fragment in-place in the current UI, or starting a
     * whole new activity in which it is displayed.
     */
    void showDetails(int key)
    {
        mCurCheckPosition = key;

        if (mDualPane)
        {
            // We can display everything in-place with fragments, so update
            // the list to highlight the selected item and show the data.
            getListView().setItemChecked(key, true);

            // Check what fragment is currently shown, replace if needed.
            BasicDetailsFragment details = (BasicDetailsFragment)
                    getFragmentManager().findFragmentById(R.id.details);
            if (details == null || details.getShownKey() != m_myCommand.getId())
            {
                // Make new fragment to show this selection.
                details = BasicDetailsFragment.newInstance(key);

                // Execute a transaction, replacing any existing fragment
                // with this one inside the frame.
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.details, details);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
            }

        }
        else
        {
            // Otherwise we need to launch a new activity to display
            // the dialog fragment with selected text.
            Intent intent = new Intent();
            intent.setClass(getActivity(), BasicDetailsActivity.class);
            intent.putExtra("index", key);
            startActivity(intent);
        }
    }
}
