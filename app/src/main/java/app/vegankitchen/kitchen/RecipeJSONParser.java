package app.vegankitchen.kitchen;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adam on 2015-01-04.
 */
public class RecipeJSONParser {
    // Storing field names
    private final String recipes = "recipes";
    private final String recipe_id = "id";
    private final String recipe_image = "image";
    private final String recipe_name = "name";
    private final String recipe_description = "description";
    private final String recipe_prep_time = "prep_time";
    private final String recipe_serving_size = "serving_size";
    private final String recipe_is_seasonal = "is_seasonal";
    private final String recipe_is_raw = "is_raw";
    private final String recipe_is_gluten_free = "is_gluten_free";
    private final String recipe_is_soy_free = "is_soy_free";
    private final String recipe_is_nut_free = "is_nut_free";
    private final String recipe_meal_type = "meal_type";
    private final String recipe_instructions = "instructions";
    private final String recipe_ingredients = "ingredients";
    private final String recipe_tips = "tips";

    private final String instruction = "instruction";

    private final String ingredient = "name";
    private final String ingredient_quantity = "quantity";

    private final String tip = "tip";

    public List<Recipe> parseRecipes( JSONArray recipeData ) throws JSONException {

        JSONArray allRecipesJSON = recipeData;
        List<Recipe> recipeList = new ArrayList<Recipe>();
        for ( int i = 0; i < allRecipesJSON.length(); i++ ) {
            JSONObject recipeJSON = allRecipesJSON.getJSONObject(i);
            int recipeId = (Integer) recipeJSON.get( recipe_id );
            String recipeName = (String) recipeJSON.get( recipe_name );
                String img64EncodingString = (String) recipeJSON.get(recipe_image);
                byte[] imageAsBytes = Base64.decode(img64EncodingString.getBytes(), Base64.DEFAULT);
            Bitmap recipeImage = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
            MealType mealType = getMealType(recipeJSON);
            String recipeDescription = (String) recipeJSON.get( recipe_description );
            String recipePrepTime = (String) recipeJSON.get( recipe_prep_time );
            String recipeServingSize = (String) recipeJSON.get( recipe_serving_size );
            List<Ingredient> ingredients = getIngredients(recipeJSON);
            List<String> instructions = getInstructions(recipeJSON);
            List<String> tips = getTip(recipeJSON);
            boolean isSeasonal = (Boolean) recipeJSON.get(recipe_is_seasonal);
            boolean isGlutenFree = (Boolean) recipeJSON.get(recipe_is_gluten_free);
            boolean isNutFree = (Boolean) recipeJSON.get(recipe_is_nut_free);
            boolean isSoyFree = (Boolean) recipeJSON.get(recipe_is_soy_free);
            boolean isRaw = (Boolean) recipeJSON.get( recipe_is_raw );

            recipeList.add(new Recipe( recipeId, recipeName, recipeImage, mealType, recipeDescription, recipePrepTime,
                    recipeServingSize, ingredients, instructions, tips, isSeasonal, isGlutenFree, isNutFree, isSoyFree,
                    isRaw));
        }
        return recipeList;

    }

    private MealType getMealType ( JSONObject recipeJSON ) throws JSONException {
        MealType mealType = null;
        if ( recipeJSON.get(recipe_meal_type).equals("Breakfast") ) {
            mealType = MealType.BREAKFAST;
        }
        else if ( recipeJSON.get(recipe_meal_type).equals("Dinner") ) {
            mealType = MealType.DINNER;
        }
        else if ( recipeJSON.get(recipe_meal_type).equals("Treat") ) {
            mealType = MealType.TREAT;
        }
        else {
            // TODO: testing only
            mealType = MealType.TREAT;
        }
        return mealType;
    }

    private List<String> getInstructions ( JSONObject recipeJSON ) throws JSONException {
        JSONArray instructionsJSONArray = recipeJSON.getJSONArray( recipe_instructions );
        List<String> instructions = new ArrayList<String>();
        for ( int i = 0; i < instructionsJSONArray.length(); i++ ) {
            JSONObject instructionJSON = instructionsJSONArray.getJSONObject(i);
            instructions.add( (String) instructionJSON.get( instruction ));
        }
        return instructions;
    }

    private List<Ingredient> getIngredients ( JSONObject recipeJSON ) throws JSONException {
        JSONArray ingredientJSONArray = recipeJSON.getJSONArray( recipe_ingredients );
        List<Ingredient> ingredients = new ArrayList<Ingredient>();
        for ( int i = 0; i < ingredientJSONArray.length(); i++ ) {
            JSONObject ingredientJSON = ingredientJSONArray.getJSONObject(i);
            ingredients.add( new Ingredient( (String) ingredientJSON.get( ingredient ),
                                             (String) ingredientJSON.get( ingredient_quantity ) ) );
        }
        return ingredients;
    }

    private List<String> getTip ( JSONObject recipeJSON ) throws JSONException {
        JSONArray tipsJSONArray = recipeJSON.getJSONArray( recipe_tips );
        List<String> tips = new ArrayList<String>();
        for ( int i = 0; i < tipsJSONArray.length(); i++ ) {
            JSONObject tipsJSON = tipsJSONArray.getJSONObject(i);
            tips.add((String) tipsJSON.get( tip ));
        }
        return tips;
    }

    private boolean getBoolean ( int i ) {
        boolean b = false;
        if ( i > 0 ) {
            b = true;
        }
        return b;
    }
}
