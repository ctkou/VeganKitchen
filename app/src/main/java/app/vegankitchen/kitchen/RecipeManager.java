package app.vegankitchen.kitchen;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import app.vegankitchen.app.R;
import app.vegankitchen.exception.NoSeasonalDishException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adam on 2014-12-27.
 */
public class RecipeManager {

    /**
     * Storing all recipes in the database
     */
    List<Recipe> recipes;

    public RecipeManager( JSONArray recipesData ) throws NoSeasonalDishException{
        RecipeJSONParser recipeJSONParser = new RecipeJSONParser();
        try {
            recipes = recipeJSONParser.parseRecipes( recipesData );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Return the recipes stored in database
     * @return List<Recipes> : a list containing all recipes stored in database
     */
    private List<Recipe> getRecipes() {
        return recipes;
    }

    /**
     * Return the Seasonal Dish recipes
     */
    public List<Recipe> getSeasonalRecipes() {
        return getSeasonalRecipes( recipes );
    }

    /**
     * Return all recipes
     */
    public List<Recipe> getAllRecipes() {
        return new ArrayList<Recipe>(recipes);
    }

    /**
     * Return the recipe with the given id, if none is found, return null
     */
    public Recipe getRecipeById ( long id ) {
        for ( Recipe recipe : recipes ) {
            if ( recipe.getRecipeId() == id ) {
                return  recipe;
            }
        }
        return null;
    }

    /**
     * Return a list of sweets recipes
     */
    public List<Recipe> getSweetsRecipes() {
        return getRecipesByMealType(recipes, MealType.SWEETS);
    }

    /**
     * Return a list of small dish recipes
     */
    public List<Recipe> getSmallDishRecipes() {
        return getRecipesByMealType(recipes, MealType.SMALL_DISH);
    }

    /**
     * Return a list of Entree recipes
     */
    public List<Recipe> getEntreeRecipes() {
        return getRecipesByMealType(recipes, MealType.ENTREE);
    }

    /**
     * Return a list of Drinks recipes
     */
    public List<Recipe> getDrinksRecipes() {
        return getRecipesByMealType(recipes, MealType.DRINKS);
    }

    /**
     * Return a list of filtered recipes based on criteria provided, the filterable criteria are the following:
     * @param recipes : The list recipes to be filtered
     * @param isRaw : filtering criteria - whether a recipe is raw
     * @param isNutFree : filtering criteria - whether a recipe is nut free
     * @param isGlutenFree : filtering criteria - whether a recipe is gluten free
     * @param isSoyFree : filtering criteria - whether a recipe is soy free
     * @return a filtered list of recipes
     */
    public static List<Recipe> filteredRecipeList( List<Recipe> recipes, boolean isRaw,
                                                   boolean isNutFree, boolean isGlutenFree, boolean isSoyFree ) {

        if ( recipes == null )
            return null;

        List<Recipe> filteredRecipes = recipes;

        if ( isRaw )
            filteredRecipes = getRawRecipes( recipes );
        if ( isNutFree )
            filteredRecipes = getNutFreeRecipes( filteredRecipes );
        if ( isGlutenFree )
            filteredRecipes = getGlutenFreeRecipes(filteredRecipes);
        if ( isSoyFree )
            filteredRecipes = getSoyFreeRecipes( filteredRecipes );

        return filteredRecipes;

    }

    /**
     * Extract a list of recipes of the given meal type from the given recipes, if the given MealType is null, simply
     * return the given recipes
     * @param recipes : a list of recipes to be filtered
     * @return a list of recipes of the given MealType
     */
    public static List<Recipe> getRecipesByMealType( List<Recipe> recipes, MealType mealType) {
        if ( mealType == null )
            return recipes;
        List<Recipe> recipesByMealType = new ArrayList<Recipe>();
        for ( Recipe recipe : recipes ) {
            if ( recipe.getMealType() == mealType )
                recipesByMealType.add( recipe );
        }
        return recipesByMealType;
    }

    /**
     * Extract a list of raw recipes from the given recipes
     * @param recipes : a list of recipes to be filtered
     * @return a list of raw food recipes
     */
    public static List<Recipe> getRawRecipes( List<Recipe> recipes ) {
        List<Recipe> rawRecipes = new ArrayList<Recipe>();
        for ( Recipe recipe : recipes ) {
            if ( recipe.isRaw() )
                rawRecipes .add(recipe);
        }
        return rawRecipes ;
    }

    /**
     * Extract a list of gluten free recipes from the given recipes
     * @param recipes : a list of recipes to be filtered
     * @return a list of gluten free recipes
     */
    public static List<Recipe> getGlutenFreeRecipes( List<Recipe> recipes ) {
        List<Recipe> glutenFreeRecipes = new ArrayList<Recipe>();
        for ( Recipe recipe : recipes ) {
            if ( recipe.isGlutenFree() )
                glutenFreeRecipes .add(recipe);
        }
        return glutenFreeRecipes ;
    }

    /**
     * Extract a list of nut free recipes from the given recipes
     * @param recipes : a list of recipes to be filtered
     * @return a list of nut free recipes
     */
    public static List<Recipe> getNutFreeRecipes( List<Recipe> recipes ) {
        List<Recipe> nutFreeRecipes = new ArrayList<Recipe>();
        for ( Recipe recipe : recipes ) {
            if ( recipe.isNutFree() )
                nutFreeRecipes.add( recipe );
        }
        return nutFreeRecipes;
    }

    /**
     * Extract a list of soy free recipes from the given recipes
     * @param recipes : a list of recipes to be filtered
     * @return a list of soy free recipes
     */
    public static List<Recipe> getSoyFreeRecipes( List<Recipe> recipes ) {
        List<Recipe> soyFreeRecipes = new ArrayList<Recipe>();
        for ( Recipe recipe : recipes ) {
            if ( recipe.isSoyFree() )
                soyFreeRecipes.add(recipe);
        }
        return soyFreeRecipes ;
    }

    /**
     * Extract a list of seasonal recipes from the given recipes
     * @param recipes : a list of recipes to be filtered
     * @return a list of seasonal recipes
     */
    public static List<Recipe> getSeasonalRecipes( List<Recipe> recipes ) {
        List<Recipe> seasonalRecipes = new ArrayList<Recipe>();
        for ( Recipe recipe : recipes ) {
            if ( recipe.isSeasonal() )
                seasonalRecipes.add( recipe );
        }
        return seasonalRecipes;
    }

}
