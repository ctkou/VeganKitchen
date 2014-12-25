package app.vegankitchen.app;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Keep check of fragment back stack count
     */
    private int mFragmentBackStackCount;

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;


    /**
     * Called when the activity is first created. This is where all of your normal static set up are done: create views,
     * bind data to lists, etc. This method also provides you with a Bundle containing the activity's previously
     * frozen state, if there was one.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Always call the super class' method
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getResources().getString( R.string.app_name );

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected( final long id) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance( id ))
                .addToBackStack(null)
                .commit();
    }

    /**
     * EFFECT: change update the ActionBar title to the currently visible page
     * @param id : page number
     */
    public void onSectionAttached( long id) {
        mTitle = getString( (int) id );
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if ( this.getSupportFragmentManager().getBackStackEntryCount() == 0 ) {
            this.finish();
        };
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_id";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(long sectionId) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putLong(ARG_SECTION_NUMBER, sectionId);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView;

            // Get the section name that this fragment is associated with
            long sectionId = this.getArguments().getLong( ARG_SECTION_NUMBER );
            String sectionName = getResources().getString( (int) sectionId );

            // inflate the correct layout based on the section name currently selected
            if ( sectionName.equals("Breakfast") )  {
                // TODO
                rootView = inflater.inflate(R.layout.fragment_main_breakfast, container, false);
            }
            else if ( sectionName.equals("Dinner") ) {
                // TODO
                rootView = inflater.inflate(R.layout.fragment_main_dinner, container, false);
            }
            else if ( sectionName.equals("Treat") ) {
                // TODO
                rootView = inflater.inflate(R.layout.fragment_main_treat, container, false);
            }
            else if ( sectionName.equals("Quick Lookup") ) {
                // TODO
                rootView = inflater.inflate(R.layout.fragment_main_search, container, false);
            }
            else if ( sectionName.equals("Sharing Place") ) {
                // TODO
                rootView = inflater.inflate(R.layout.fragment_main_sharing, container, false);
            }
            else {
                // TODO
                rootView = inflater.inflate(R.layout.fragment_main_home, container, false);
            }

            // making sure that the action bar display the correct title when the back button is pressed
            onAttach( getActivity() );
            ((MainActivity) getActivity()).restoreActionBar();

            return rootView;
        }

        @Override
        public void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getLong(ARG_SECTION_NUMBER));
        }

    }

}
