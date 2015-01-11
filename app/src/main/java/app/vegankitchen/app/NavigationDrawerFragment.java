package app.vegankitchen.app;

import android.app.ExpandableListActivity;
import android.support.v7.app.ActionBarActivity;
import android.app.Activity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Fragment used for managing interactions for and presentation of a navigation drawer.
 * See the <a href="https://developer.android.com/design/patterns/navigation-drawer.html#Interaction">
 * design guidelines</a> for a complete explanation of the behaviors implemented here.
 */
public class NavigationDrawerFragment extends Fragment {

    /**
     * Remember the position of the selected item.
     */
    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

    /**
     * Remember the id of the selected item.
     */
    private static final String STATE_SELECTED_ID = "selected_navigation_drawer_id";

    /**
     * Per the design guidelines, you should show the drawer on launch until the user manually
     * expands it. This shared preference tracks this.
     */
    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

    /**
     * A pointer to the current callbacks instance (the Activity).
     */
    private NavigationDrawerCallbacks mCallbacks;

    /**
     * Helper component that ties the action bar to the navigation drawer.
     */
    private ActionBarDrawerToggle mDrawerToggle;

    private DrawerLayout mDrawerLayout;
    private ExpandableListView mDrawerListView;
    private View mFragmentContainerView;

    private int mCurrentSelectedPosition = 0;
    private long mCurrentSelectedID = R.string.title_section1;
    private boolean mFromSavedInstanceState;
    private boolean mUserLearnedDrawer;

    public NavigationDrawerFragment() {
    }

    /**
     * Called to do initial creation of the fragment
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        Log.e("NavigationDrawerFragment - onCreate (s)", getResources().getString((int) mCurrentSelectedID));

        super.onCreate(savedInstanceState);

        // Read in the flag indicating whether or not the user has demonstrated awareness of the
        // drawer. See PREF_USER_LEARNED_DRAWER for details.
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mCurrentSelectedID = savedInstanceState.getLong(STATE_SELECTED_ID);
            mFromSavedInstanceState = true;
        }

        // Select either the default item (0) or the last selected item.
        long mainPlaceholderSectionID = ((MainActivity) getActivity()).getCurrentArgSectionNumber();
        if ( ((MainActivity) getActivity()).getRecipe() == null ) {
            if ( mainPlaceholderSectionID != R.string.sub_title_section2_1 &&
                    mainPlaceholderSectionID != R.string.sub_title_section2_2 &&
                    mainPlaceholderSectionID != R.string.sub_title_section2_3 &&
                    mainPlaceholderSectionID != R.string.title_section3 )
                selectItem(mCurrentSelectedPosition, mCurrentSelectedID);
        }
        Log.e("NavigationDrawerFragment - onCreate (e)", getResources().getString((int) mCurrentSelectedID));
    }

    /**
     * Tells the fragment that its activity has completed its own Activity.onCreate()
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // Indicate that this fragment would like to influence the set of actions in the action bar.
        setHasOptionsMenu(true);
    }

    /**
     * Creates and returns the view hierarchy associated with the fragment.
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return View
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.e("NavigationDrawerFragment - onCreateView (s)", getResources().getString((int) mCurrentSelectedID));

        mDrawerListView = (ExpandableListView) inflater.inflate(
                R.layout.fragment_navigation_drawer, container, false);

        // Initializing the data to the navigation drawer
        List<DrawerCategory> drawerCategories = initializeDrawerData();

        // Initializing the ExpandableLstAdapter to provide data and Views to an expandable list view.
        ExpandableListAdapter expandableListAdapter = new ExpandableListAdapter( getActionBar().getThemedContext(),
                drawerCategories);

        // Setting the ExpandableIstAdapter
        mDrawerListView.setAdapter( expandableListAdapter );

        // Setting listener to handle child text being clicked
        mDrawerListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            /**
             * Callback method to be invoked when a child in this expandable list has been clicked.
             * @param parent : The ExpandableListView where the click happened
             * @param view : The view within the expandable list/ListView that was clicked
             * @param groupPosition : The group position that contains the child that was clicked
             * @param childPosition : The child position within the group
             * @param id : The row id of the child that was clicked
             * @return
             */
            @Override
            public boolean onChildClick(ExpandableListView parent, View view, int groupPosition, int childPosition,
                                        long id) {
                int position = parent.getFlatListPosition(
                        ExpandableListView.getPackedPositionForChild(groupPosition, childPosition));
                selectItem(position, id);
                return true;
            }
        });

        // Setting listener to handle parent text being clicked
        mDrawerListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            /**
             *
             * @param parent : The ExpandableListView where the click happened
             * @param view : The view within the expandable list/ListView that was clicked
             * @param groupPosition : The group position that contains the child that was clicked
             * @param id : The row id of the child that was clicked
             * @return
             */
            @Override
            public boolean onGroupClick(ExpandableListView parent, View view, int groupPosition, long id) {
                int position = parent.getFlatListPosition(
                        ExpandableListView.getPackedPositionForGroup(groupPosition));
                selectItem(position, id);
                return false;
            }
        });

        //mDrawerListView.setItemChecked(mCurrentSelectedPosition, true);

        Log.e("NavigationDrawerFragment - onCreateView (e)", getResources().getString((int) mCurrentSelectedID));

        return mDrawerListView;
    }

    /**
     *
     * @return
     */
    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    /**
     * Users of this fragment must call this method to set up the navigation drawer interactions.
     *
     * @param fragmentId   The android:id of this fragment in its activity's layout.
     * @param drawerLayout The DrawerLayout containing this fragment's UI.
     */
    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the navigation drawer and the action bar app icon.
        mDrawerToggle = new ActionBarDrawerToggle(
                getActivity(),                    /* host Activity */
                mDrawerLayout,                    /* DrawerLayout object */
                R.drawable.ic_drawer,             /* nav drawer image to replace 'Up' caret */
                R.string.navigation_drawer_open,  /* "open drawer" description for accessibility */
                R.string.navigation_drawer_close  /* "close drawer" description for accessibility */
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) {
                    return;
                }

                getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) {
                    return;
                }

                if (!mUserLearnedDrawer) {
                    // The user manually opened the drawer; store this flag to prevent auto-showing
                    // the navigation drawer automatically in the future.
                    mUserLearnedDrawer = true;
                    SharedPreferences sp = PreferenceManager
                            .getDefaultSharedPreferences(getActivity());
                    sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).apply();
                }

                getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }
        };

        // If the user hasn't 'learned' about the drawer, open it to introduce them to the drawer,
        // per the navigation drawer design guidelines.
        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(mFragmentContainerView);
        }

        // Defer code dependent on restoration of previous instance state.
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    /**
     *
     * @param position
     * @param id
     */
    private void selectItem(int position, long id) {
        mCurrentSelectedPosition = position;
        mCurrentSelectedID = id;
        Log.e("NavigationDrawerFragment - selectItem (s)", getResources().getString((int) mCurrentSelectedID));
        if (mDrawerListView != null) {
            mDrawerListView.setItemChecked(position, true);
        }
        if (mDrawerLayout != null && !isNonEmptyGroup(id) ) {
            mDrawerLayout.closeDrawer(mFragmentContainerView);
        }
        if (mCallbacks != null && !isNonEmptyGroup(id) ) {
            mCallbacks.onNavigationDrawerItemSelected( id );
        }
        Log.e("NavigationDrawerFragment - selectItem (e)", getResources().getString((int) mCurrentSelectedID));
    }

    /**
     * Called once the fragment is associated with its activity
     * @param activity
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }

    /**
     * Called immediately prior to the fragment no longer being associated with its activity
     */
    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
        outState.putLong(STATE_SELECTED_ID, mCurrentSelectedID);

        Log.e("onSaveInstanceState", getResources().getString((int) mCurrentSelectedID));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Forward the new configuration the drawer toggle component.
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // If the drawer is open, show the global app actions in the action bar. See also
        // showGlobalContextActionBar, which controls the top-left area of the action bar.
        if (mDrawerLayout != null && isDrawerOpen()) {
            inflater.inflate(R.menu.global, menu);
            showGlobalContextActionBar();
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Per the navigation drawer design guidelines, updates the action bar to show the global app
     * 'context', rather than just what's in the current screen.
     */
    private void showGlobalContextActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setTitle(R.string.app_name);
    }

    private ActionBar getActionBar() {
        return ((ActionBarActivity) getActivity()).getSupportActionBar();
    }

    /**
     * Return true if the given id associates with a non-empty group
     * @param id
     * @return boolean
     */
    private boolean isNonEmptyGroup ( long id ) {
        String string = getResources().getString( (int) id );
        List<DrawerCategory> drawerCategories = initializeDrawerData();

        for ( DrawerCategory drawerCategory : drawerCategories ) {
            // Check that the id is associated with a group and not a child
            if ( drawerCategory.getNAME().equals(string) ) {
                // Check that it is indeed non-empty
                if ( drawerCategory.getDrawerItems().size() > 0 ) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * EFFECT: return a list of DrawCategory based on the drawer definition defined in strings.xml
     * @return List<DrawerCategory>
     */
    private List<DrawerCategory> initializeDrawerData() {

        DrawerCategory drawerCategory1 = new DrawerCategory( R.string.title_section1,
                getResources().getString( R.string.title_section1 ) );

        DrawerCategory drawerCategory2 = new DrawerCategory( R.string.title_section2,
                getResources().getString( R.string.title_section2 ) );
        drawerCategory2.addDrawerItem( new DrawerItem( R.string.sub_title_section2_1,
                getResources().getString( R.string.sub_title_section2_1 )));
        drawerCategory2.addDrawerItem( new DrawerItem( R.string.sub_title_section2_2,
                getResources().getString( R.string.sub_title_section2_2 )));
        drawerCategory2.addDrawerItem( new DrawerItem( R.string.sub_title_section2_3,
                getResources().getString( R.string.sub_title_section2_3 )));

        DrawerCategory drawerCategory3 = new DrawerCategory( R.string.title_section3,
                getResources().getString( R.string.title_section3 ) );

        DrawerCategory drawerCategory4 = new DrawerCategory( R.string.title_section4,
                getResources().getString( R.string.title_section4 ) );

        List<DrawerCategory> drawerCategories = new ArrayList<DrawerCategory>();
        drawerCategories.add(drawerCategory1);
        drawerCategories.add(drawerCategory2);
        drawerCategories.add(drawerCategory3);
        drawerCategories.add(drawerCategory4);

        return drawerCategories;

    }

    /**
     * Callbacks interface that all activities using this fragment must implement.
     */
    public static interface NavigationDrawerCallbacks {
        /**
         * Called when an item in the navigation drawer is selected.
         */
        void onNavigationDrawerItemSelected(long id);
    }
}
