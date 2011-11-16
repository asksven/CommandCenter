/**
 * 
 */
package com.asksven.systemsettings;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.asksven.systemsettings.R;

/**
 * @author sven
 *
 */
/**
 * Activity shows two fragments, either both (landscape)
 * or as two activities
 */

public class BasicActivity  extends FragmentActivity
{


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.basic_fragment);
    }
}
