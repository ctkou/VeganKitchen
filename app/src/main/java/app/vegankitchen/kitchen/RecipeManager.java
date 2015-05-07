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

    public RecipeManager() {
        // check out the entire list of stored recipes
        // TODO: implement the actual database
        // TODO: This is data manually created for testing only

        String path = "/storage/emulated/0/Pictures/Testing/testcake1.jpg";
        String recipeName1 = "Viagra Cake";
        Bitmap image1 = BitmapFactory.decodeFile(path);
        String description1 = "Want more fun time in bed? This gluten free viagra laden cake is here to help. But make sure you don't have any medical problem with your heart before making this";
        String preparationTime1 = "About 3 months";
        String servingSizeDescription1 = "Enough for one person";
        Ingredient ingredient1 = new Ingredient("Viagra pills","A whole lot");
        Ingredient ingredient2 = new Ingredient("Some one to have sex with", "at least one");
        List<Ingredient> ingredients1 = new ArrayList<Ingredient>();
        ingredients1.add(ingredient1);
        ingredients1.add(ingredient2);
        List<String> cookingInstructions1 = new ArrayList<String>();
        cookingInstructions1.add("1. Add some viagra pills");
        cookingInstructions1.add("2. Add some more viagra pills");
        cookingInstructions1.add("3. Add even more viagra pills");
        cookingInstructions1.add("4. Put it in oven for 3 months until burn to very very dark in color");
        String string1 = "We take no responsibility of any medical complication.";
        List<String> tips1 = new ArrayList<String>();
        tips1.add(string1);
        Recipe recipe1 = new Recipe(10,recipeName1,image1,MealType.TREAT,description1,preparationTime1,
                servingSizeDescription1,ingredients1,cookingInstructions1,tips1,true,true,false,true,false);

        String path2 = "/res/drawable/testdish2.jpg";
        String recipeName2 = "Pancake";
        Bitmap image2 = BitmapFactory.decodeFile(path2);
        String description2 = "Just some normal pancake";
        String preparationTime2 = "An hour?";
        String servingSizeDescription2 = "I don't know?";
        Ingredient ingredient3 = new Ingredient("powder?", "some");
        Ingredient ingredient4 = new Ingredient("eggs as well?", "some");
        List<Ingredient> ingredients2 = new ArrayList<Ingredient>();
        ingredients2.add(ingredient3);
        ingredients2.add(ingredient4);
        List<String> cookingInstructions2 = new ArrayList<String>();
        cookingInstructions2.add("1. Add the powdery thing");
        cookingInstructions2.add("2. Add the eggs");
        String string2 = "Do somme more internet search first";
        List<String> tips2 = new ArrayList<String>();
        tips1.add(string2);
        Recipe recipe2 = new Recipe(20,recipeName2,image2,MealType.BREAKFAST,description2,preparationTime2,
                servingSizeDescription2,ingredients2,cookingInstructions2,tips2,false,false,false,true,false);

        String path3 = "/storage/emulated/0/Pictures/Testing/breakfast2.jpg";
        String recipeName3 = "Breast Milk";
        Bitmap image3 = BitmapFactory.decodeFile(path3);
        String description3 = "Not kidding, real human breast milk!";
        String preparationTime3 = "Within seconds";
        String servingSizeDescription3 = "Depends on the woman";
        Ingredient ingredient5 = new Ingredient("Woman with milk", "one");
        List<Ingredient> ingredients3 = new ArrayList<Ingredient>();
        ingredients3.add(ingredient5);
        List<String> cookingInstructions3 = new ArrayList<String>();
        cookingInstructions3.add("1. Just suck on the nipples.");
        String string3 = "Look out for aids";
        List<String> tips3 = new ArrayList<String>();
        tips3.add(string3);
        Recipe recipe3 = new Recipe(30,recipeName3,image3,MealType.BREAKFAST,description3,preparationTime3,
                servingSizeDescription3,ingredients3,cookingInstructions3,tips3,true,true,true,true,true);

        recipes = new ArrayList<Recipe>();

        recipes.add(recipe1);
        recipes.add(recipe2);
        recipes.add(recipe3);
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
     * Return a list of breakfast recipes
     */
    public List<Recipe> getBreakfastRecipes() {
        return getRecipesByMealType(recipes, MealType.BREAKFAST);
    }

    /**
     * Return a list of dinner recipes
     */
    public List<Recipe> getDinnerRecipes() {
        return getRecipesByMealType(recipes, MealType.DINNER);
    }

    /**
     * Return a list of treat recipes
     */
    public List<Recipe> getTreatRecipes() {
        return getRecipesByMealType(recipes, MealType.TREAT);
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
