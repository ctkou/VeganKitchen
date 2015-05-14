package app.vegankitchen.app;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.*;
import app.vegankitchen.kitchen.Ingredient;
import app.vegankitchen.kitchen.MealType;
import app.vegankitchen.kitchen.Recipe;
import app.vegankitchen.kitchen.RecipeManager;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Storing all recipes in the database
     */
    private RecipeManager recipeManager;

    /**
     * Whether the currently visible PlaceholderFragment is holding a recipe page
     */
    private boolean isRecipePage = false;

    /**
     * The currently selected recipe id
     */
    private Long recipeId = null;

    /**
     * The currently selected recipe
     */
    private Recipe recipe = null;

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    /**
     * Provide easy access to the ARG_SECTION_NUMBER of the current PlaceholderFragment
     */
    private long currentArgSectionNumber;

    /**
     * Remember the previously saved selected section number of the PlaceholderFragment
     */
    private static final String LAST_SECTION_NUMBER = "last_placeholderfragment_section_number";

    /**
     * Remember whether the last visible PlaceholderFragment held a recipe page
     */
    private static final String LAST_IS_RECIPE = "last_is_recipe";

    /**
     * Remember the previously saved selected recipe id
     */
    private static final String LAST_RECIPE_ID = "last_recipe_id";

    /**
     * Called when the activity is first created. This is where all of your normal static set up are done: create views,
     * bind data to lists, etc. This method also provides you with a Bundle containing the activity's previously
     * frozen state, if there was one.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mTitle = getResources().getString(R.string.app_name);

        // Download data from database
        DownloadJson recipesDownloader = new DownloadJson();
        String recipeDataString = null;

        try {
            recipeDataString = recipesDownloader.execute("http://stark-mesa-5518.herokuapp.com/recipes/show").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        // Initialize the RecipeManager that maintain all recipes stored in database
        try {
            if ( recipeDataString != null )
                recipeManager = new RecipeManager( new JSONArray(recipeDataString) );
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        // Obtaining the last visible fragment and last selected recipe information if they exist
        if ( savedInstanceState != null ) {
            currentArgSectionNumber = savedInstanceState.getLong( LAST_SECTION_NUMBER );
            isRecipePage = savedInstanceState.getBoolean( LAST_IS_RECIPE );
            mTitle = getResources().getString( (int) currentArgSectionNumber );
            if (isRecipePage) {
                recipeId = savedInstanceState.getLong( LAST_RECIPE_ID);
                recipe = recipeManager.getRecipeById( recipeId );
            }
        }

        // Always call the super class' method
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

    }

    /**
     * EFFECT: change update the ActionBar title to the currently visible page
     * @param id : page number
     */
    public void onSectionAttached( long id) {
        mTitle = getString( (int) id );
    }

    /**
     * EFFECT: restoring the Action Bar
     */
    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    /**
     * Initialize the contents of the Activity's standard options menu (Actionbar).
     * @param menu : The options menu in which to place the items in.
     * @return Return true for the menu to be displayed
     */
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

    /**
     * EFFECT: Called when the activity has detected the user's press of the back key. The current implementation check
     *          whether there is remaining "content" in the back stack of the PlaceHolderFragment. If none is found, it
     *          finishes the MainActivity, thus terminating the application.
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if ( this.getSupportFragmentManager().getBackStackEntryCount() == 0 ) {
            this.finish();
        };
    }

    /**
     * This hook is called whenever an item in options menu (Actionbar) is selected.
     * @param item The menu item that was selected.
     * @return Return false to allow normal menu processing to proceed, true to consume it here.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            // TODO: implement this to show credits
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState( Bundle outState ) {

        outState.putLong(LAST_SECTION_NUMBER, currentArgSectionNumber);

        outState.putBoolean( LAST_IS_RECIPE, isRecipePage );
        if (isRecipePage) {
            outState.putLong( LAST_RECIPE_ID, recipeId );
        };

        super.onSaveInstanceState(outState);
    }

    /**
     * get the currently selected section id in the PlaceholderFragment
     */
    public long getCurrentArgSectionNumber() {
        return currentArgSectionNumber;
    }

    /**
     * get the currently selected recipe
     */
    public Recipe getRecipe() {
        return recipe;
    }

    /**
     * Called when an item in the navigation drawer is clicked
     * @param id : The corresponding id of the section name as define in strings.xml
     *              e.g. when "Quick Lookup", the id of title_section3 in strings.xml is passed
     */
    @Override
    public void onNavigationDrawerItemSelected( long id) {


        // set the currently selected recipe to be null, since it is the navigation drawer that got selected
        isRecipePage = false;
        recipeId = null;
        recipe = null;

        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance( id, false ))
                // add the current fragment to the back stack in order to ensure the previously visible fragment appear
                // when the back button is pressed
                .addToBackStack(null)
                .commit();

    }

    /**
     * This method is called when the ImageButton (id: dish_image) from dish_item.xml is pressed
     * @param view : the ImageButton that was pressed
     */
    public void onDishSelected( View view ) {
        // update the currently selected recipe field
        this.isRecipePage = true;
        this.recipe = (Recipe) view.getTag();
        this.recipeId = recipe.getRecipeId();

        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance( currentArgSectionNumber, true ))
                .addToBackStack(null)
                .commit();
    }

    private class DownloadJson extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {

            String jsonText = null;
            try {
                jsonText = getText( strings[0] );
            } catch (Exception e) {
                e.printStackTrace();
            }

            return jsonText;
        }

        @Override
        protected void onPostExecute(String jsonString ) {

        }

        private String getText( String url ) throws Exception {
            URL website = new URL(url);
            URLConnection connection = website.openConnection();
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            connection.getInputStream()));

            StringBuilder response = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null)
                response.append(inputLine);

            in.close();

            return response.toString();
        }
    }

    /**
     * A placeholder fragment.
     */
    public static class PlaceholderFragment extends Fragment {

        /**
         * The fragment argument representing the section number for this fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_id";

        /**
         * The fragment argument representing whether the fragment is a recipe page
         */
        private static final String ARG_IS_RECIPE_PAGE = "is_recipe_page";

        /**
         * Returns a new instance of this fragment for the given section number.
         */
        public static PlaceholderFragment newInstance( long sectionId, boolean isRecipePage ) {

            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putLong(ARG_SECTION_NUMBER, sectionId);
            args.putBoolean(ARG_IS_RECIPE_PAGE, isRecipePage);
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
            View rootView = null;

            // Get the section name that this fragment is associated with
            long sectionId = this.getArguments().getLong( ARG_SECTION_NUMBER );
            String sectionName = getResources().getString( (int) sectionId );

            // Get whether this fragment is associated with a recipe page
            boolean isRecipePage = this.getArguments().getBoolean( ARG_IS_RECIPE_PAGE );

            // Let the MainActivity.class know the currently selected section number
            // i.e. the unique id as based on R.id
            ((MainActivity) getActivity()).currentArgSectionNumber = sectionId;

            // inflate the correct layout based on the section name currently selected
            if (isRecipePage) {
                rootView = setupDishPage( inflater, container );
            }
            else {
                if ( sectionName.equals("Sweets") )  {
                    // Handling the sweets page
                    List<Recipe> sweetsRecipes = ((MainActivity) getActivity()).recipeManager.getSweetsRecipes();
                    rootView = setupRecipeSearchPage(inflater,container,R.layout.fragment_main_sweets,sweetsRecipes
                            ,MealType.SWEETS);
                }
                else if ( sectionName.equals("Small Dish") ) {
                    // Handling the Small Dish page
                    List<Recipe> smallDishRecipes = ((MainActivity) getActivity()).recipeManager.getSmallDishRecipes();
                    rootView = setupRecipeSearchPage(inflater,container,R.layout.fragment_main_small_dish,smallDishRecipes
                                                        ,MealType.SMALL_DISH);
                }
                else if ( sectionName.equals("Entree") ) {
                    // Handling the Entree page
                    List<Recipe> entreeRecipes = ((MainActivity) getActivity()).recipeManager.getEntreeRecipes();
                    rootView = setupRecipeSearchPage(inflater, container, R.layout.fragment_main_entree, entreeRecipes
                            ,MealType.ENTREE);
                }
                else if ( sectionName.equals("Drinks") ) {
                    // Handling the Drinks page
                    List<Recipe> drinksRecipes = ((MainActivity) getActivity()).recipeManager.getDrinksRecipes();
                    rootView = setupRecipeSearchPage(inflater, container, R.layout.fragment_main_drinks, drinksRecipes
                            ,MealType.DRINKS);
                }
                else if ( sectionName.equals("Quick Lookup") ) {
                    List<Recipe> allRecipes = ((MainActivity) getActivity()).recipeManager.getAllRecipes();
                    rootView = setupRecipeSearchPage(inflater, container, R.layout.fragment_main_search, allRecipes
                            , null);
                }
                else if ( sectionName.equals("Sharing Place") ) {
                    // TODO: handling the sharing place page
                    rootView = inflater.inflate(R.layout.fragment_main_sharing, container, false);
                }
                else if ( sectionName.equals("Main Kitchen") ) {
                    // handling the home page
                    rootView = inflater.inflate(R.layout.fragment_main_home, container, false);

                    List<Recipe> seasonalRecipes = ((MainActivity) getActivity()).recipeManager.getSeasonalRecipes();
                    ListView seasonalRecipesView = (ListView) rootView.findViewById( R.id.seasonal_dish );
                    seasonalRecipesView.setAdapter( new RecipeListViewAdapter( getActivity(), seasonalRecipes) );
                    setListViewHeightBasedOnChildren( seasonalRecipesView );
                    seasonalRecipesView.setFocusable( false );

                    // TODO: handling updates on home page
                }

            }

            // making sure that the action bar display the correct title when the back button is pressed
            onAttach( getActivity() );
            ((MainActivity) getActivity()).restoreActionBar();

            return rootView;
        }

        @Override
        public void onResume() {
            super.onResume();
            // Get the section name that this fragment is associated with
            View rootView = ((MainActivity) getActivity()).getSupportFragmentManager()
                    .findFragmentById(R.id.container)
                    .getView();
            long sectionId = this.getArguments().getLong( ARG_SECTION_NUMBER );
            String sectionName = getResources().getString( (int) sectionId );
            if ( !((MainActivity) getActivity()).isRecipePage )
                if ( sectionName.equals(getResources().getString(R.string.sub_title_section2_1)) ) {
                    updateRecipeSearchListView( MealType.SWEETS, rootView );
                }
                else if ( sectionName.equals(getResources().getString(R.string.sub_title_section2_2)) ) {
                    updateRecipeSearchListView( MealType.SMALL_DISH, rootView );
                }
                else if ( sectionName.equals(getResources().getString(R.string.sub_title_section2_3)) ) {
                    updateRecipeSearchListView( MealType.ENTREE, rootView );
                }
                else if ( sectionName.equals(getResources().getString(R.string.sub_title_section2_4)) ) {
                    updateRecipeSearchListView( MealType.DRINKS, rootView );
                }
                else if ( sectionName.equals(getResources().getString(R.string.title_section3)) ) {
                    updateRecipeSearchListView( null, rootView );
                }
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

        private View setupRecipeSearchPage ( LayoutInflater inflater, ViewGroup container, int layoutId,
                                             List<Recipe> recipes, MealType mealType ) {

            View rootView = inflater.inflate(layoutId, container, false);

            boolean isSoyFree = ((CheckBox) rootView.findViewById( R.id.is_soy_free)).isChecked();
            boolean isRaw = ((CheckBox) rootView.findViewById( R.id.is_raw)).isChecked();
            boolean isNutFree= ((CheckBox) rootView.findViewById( R.id.is_nut_free)).isChecked();
            boolean isGlutenFree = ((CheckBox) rootView.findViewById( R.id.is_gluten_free)).isChecked();

            recipes = RecipeManager.filteredRecipeList( recipes,
                    isRaw, isNutFree, isGlutenFree, isSoyFree);

            ListView recipesListView = (ListView) rootView.findViewById( R.id.recipe_list );
            recipesListView.setAdapter( new RecipeListViewAdapter( getActivity(), recipes) );
            setListViewHeightBasedOnChildren( recipesListView );

            if ( mealType == null ) {
                Spinner mealTypeSpinner = (Spinner) rootView.findViewById( R.id.meal_type_searchable );
                ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource( getActivity(),
                        R.array.meal_type_searchable, R.layout.support_simple_spinner_dropdown_item);
                adapter.setDropDownViewResource( R.layout.support_simple_spinner_dropdown_item);
                mealTypeSpinner.setAdapter( adapter );
                mealTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        View rootView = ((MainActivity) getActivity())
                                .getSupportFragmentManager()
                                .findFragmentById(R.id.container)
                                .getView();
                        updateRecipeSearchListView(null, rootView);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }

            setCheckBoxOnClickListener( rootView, mealType );

            return rootView;
        }

        private void setCheckBoxOnClickListener ( View rootView, final MealType mealType ) {
            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    View rootView = ((MainActivity) getActivity())
                            .getSupportFragmentManager()
                            .findFragmentById(R.id.container)
                            .getView();
                    updateRecipeSearchListView(mealType, rootView);
                }
            };

            CheckBox isNutFreeCheckBox = (CheckBox) rootView.findViewById( R.id.is_nut_free );
            isNutFreeCheckBox.setOnClickListener( listener );
            CheckBox isSoyFreeCheckbox = (CheckBox) rootView.findViewById( R.id.is_soy_free );
            isSoyFreeCheckbox.setOnClickListener( listener );
            CheckBox isGlutenFreeCheckBox = (CheckBox) rootView.findViewById( R.id.is_gluten_free );
            isGlutenFreeCheckBox.setOnClickListener( listener );
            CheckBox isRawCheckBox = (CheckBox) rootView.findViewById( R.id.is_raw );
            isRawCheckBox.setOnClickListener( listener );

        }

        private void updateRecipeSearchListView ( MealType mealType, View rootView ) {

            // Initializing the recipes list
            List<Recipe> recipes = null;
            if ( mealType == MealType.SWEETS ) {
                recipes = ((MainActivity) getActivity())
                        .recipeManager
                        .getSweetsRecipes();
            }
            else if ( mealType == MealType.SMALL_DISH ) {
                recipes = ((MainActivity) getActivity())
                        .recipeManager
                        .getSmallDishRecipes();
            }
            else if ( mealType == MealType.ENTREE ) {
                recipes = ((MainActivity) getActivity())
                        .recipeManager
                        .getEntreeRecipes();
            }
            else if ( mealType == MealType.DRINKS ) {
                recipes = ((MainActivity) getActivity())
                        .recipeManager
                        .getDrinksRecipes();
            }
            else if ( mealType == null ) {
                String selectedString = ((Spinner) rootView.findViewById(R.id.meal_type_searchable))
                        .getSelectedItem().toString();
                MealType selectedMealType = getSelectedMealType( selectedString );

                recipes = ((MainActivity) getActivity())
                        .recipeManager
                        .getAllRecipes();
                recipes = RecipeManager.getRecipesByMealType( recipes, selectedMealType );
            }

            // Filtering the recipe list based on inputs given in rootView
            recipes = getFilteredRecipes( recipes, rootView );

            // Update the Adapter of the recipes ListView
            ListView updateListView = (ListView) rootView.findViewById( R.id.recipe_list );
            updateListView.setAdapter(new RecipeListViewAdapter(getActivity(),
                    recipes));
            setListViewHeightBasedOnChildren(updateListView);

        }

        /**
         * Return the MealType that the given string represent:
         *  - "All" -> null
         *  - "Sweets" -> MealType.Sweets
         *  - "Small Dish" -> MealType.SMALL_DISH
         *  - "Entree" -> MealType.ENTREE
         *  - "Drinks" -> MealType.DRINKS
         * @param selectedString : the string representing a MealType
         * @return a MealType correspond to the given string
         */
        private MealType getSelectedMealType ( String selectedString ) {

            MealType selectedMealType = null;

            if ( selectedString.equals( "All" ))
                selectedMealType = null;
            else if ( selectedString.equals( "Sweets") )
                selectedMealType = MealType.SWEETS;
            else if ( selectedString.equals( "Small Dish" ) )
                selectedMealType = MealType.SMALL_DISH;
            else if ( selectedString.equals( "Entree" ) )
                selectedMealType = MealType.ENTREE;
            else if ( selectedString.equals( "Drinks" ) )
                selectedMealType = MealType.DRINKS;

            return selectedMealType;
        }

        /**
         * Return a filtered list of recipes based on the inputs on a recipe search page
         * @param recipes : the recipe list to be filtered
         * @param rootView : the View object for which filtering criteria reside
         * @return a filtered recipe list
         */
        private List<Recipe> getFilteredRecipes( List<Recipe> recipes, View rootView) {

            boolean isSoyFree = ((CheckBox) rootView.findViewById(R.id.is_soy_free))
                    .isChecked();
            boolean isRaw = ((CheckBox) rootView.findViewById(R.id.is_raw))
                    .isChecked();
            boolean isNutFree = ((CheckBox) rootView.findViewById(R.id.is_nut_free))
                    .isChecked();
            boolean isGlutenFree = ((CheckBox) rootView.findViewById(R.id.is_gluten_free))
                    .isChecked();

            return RecipeManager.filteredRecipeList(recipes, isRaw, isNutFree, isGlutenFree, isSoyFree);
        }

        /**
         * Set up a recipe page when an individual recipe is pressed
         * @param inflater
         * @param container
         * @return
         */
        public View setupDishPage ( LayoutInflater inflater, ViewGroup container ) {

            View view = inflater.inflate(R.layout.dish_page, container, false );

            Recipe selectedRecipe = ((MainActivity) getActivity()).getRecipe();

            // Setting up recipe image
            ImageView recipeImageView = (ImageView) view.findViewById( R.id.recipe_image );
            recipeImageView.setImageBitmap( selectedRecipe.getDishImage() );

            // Setting up recipe name
            TextView recipeNameView = (TextView) view.findViewById( R.id.recipe_name );
            recipeNameView.setText( selectedRecipe.getRecipeName() );

            // Setting up recipe description
            TextView recipeDescriptionView = (TextView) view.findViewById( R.id.recipe_description );
            recipeDescriptionView.setText( selectedRecipe.getDescription() );

            CheckBox recipeIsRawView = (CheckBox) view.findViewById( R.id.dish_is_raw );
            recipeIsRawView.setChecked( selectedRecipe.isRaw() );

            CheckBox recipeIsNutFreeView = (CheckBox) view.findViewById( R.id.dish_is_nut_free );
            recipeIsNutFreeView.setChecked( selectedRecipe.isNutFree() );

            CheckBox recipeIsGlutenFreeView = (CheckBox) view.findViewById( R.id.dish_is_gluten_free );
            recipeIsGlutenFreeView.setChecked( selectedRecipe.isGlutenFree() );

            CheckBox recipeIsSoyFreeView = (CheckBox) view.findViewById( R.id.dish_is_soy_free );
            recipeIsSoyFreeView.setChecked( selectedRecipe.isSoyFree() );

            // Setting up recipe Prep Time
            TextView recipePrepTimeView = (TextView) view.findViewById( R.id.recipe_prep_time );
            recipePrepTimeView.setText( selectedRecipe.getPreparationTime() );

            // Setting up recipe Serving Size
            TextView recipeServingSizeView = (TextView) view.findViewById( R.id.recipe_serving_size );
            recipeServingSizeView.setText( selectedRecipe.getServingSizeDescription() );

            // Setting up cooking instructions
            List<String> instructions = selectedRecipe.getCookingInstructions();
            ListView instructionView = (ListView) view.findViewById( R.id.instruction_item_list );
            instructionView.setAdapter( new InstructionListViewAdapter( getActivity(), instructions ));
            setListViewHeightBasedOnChildren( instructionView );

            // Setting up ingredient list
            List<Ingredient> ingredients = selectedRecipe.getIngredients();
            ListView ingredientView = (ListView) view.findViewById( R.id.ingredient_item_list );
            ingredientView.setAdapter( new IngredientListViewAdapter( getActivity(), ingredients ));
            setListViewHeightBasedOnChildren( ingredientView );

            // Setting up tips
            List<String> tips = selectedRecipe.getTips();
            ListView tipsView = (ListView) view.findViewById( R.id.tip_item_list );
            tipsView.setAdapter( new TipListViewAdapter( getActivity(), tips ) );
            setListViewHeightBasedOnChildren( tipsView );

            return view;

        }

        /**
         * EFFECT: Method for setting the height of the given ListView dynamically based on its children
         * @param listView : the ListView which height is to be set based on its child Views
         *
         * Credit to Arshad Parwez: https://plus.google.com/+ArshadParwezDev/posts
         */
        public static void setListViewHeightBasedOnChildren(ListView listView) {
            ListAdapter listAdapter = listView.getAdapter();
            if (listAdapter == null) {
                // pre-condition
                return;
            }

            int totalHeight = 0;
            for (int i = 0; i < listAdapter.getCount(); i++) {
                View listItem = listAdapter.getView(i, null, listView);
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            }

            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
            listView.setLayoutParams(params);
            listView.requestLayout();
        }
    }

}
