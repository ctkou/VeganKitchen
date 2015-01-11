package app.vegankitchen.kitchen;

import android.graphics.Bitmap;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by Adam on 2014-12-26.
 * Class representing a recipe. A recipe can either be for Breakfast, Dinner, or a Treat (dessert).
 */
public class Recipe {

    private long recipeId;

    /**
     * The name of the recipe
     */
    private String recipeName;

    /**
     * A picture of the finished dish
     */
    private Bitmap dishImage;

    /**
     * The meal type of the recipe, either breakfast, dinner or treat
     */
    private MealType mealType;

    /**
     * Brief description of the recipe
     */
    private String description;

    /**
     * Estimated preparation time
     */
    private String preparationTime;

    /**
     * Serving size description
     */
    private String servingSizeDescription;

    /**
     * A list of ingredient
     */
    private List<Ingredient> ingredients;

    /**
     * A list of cooking instructions
     */
    private List<String> cookingInstructions;

    /**
     * A list of cooking tips
     */
    private List<String> tips;

    private boolean isSeasonal; // There should be only one seasonal dish in the database
    private boolean isGlutenFree;
    private boolean isNutFree;
    private boolean isSoyFree;
    private boolean isRaw;

    public Recipe(long recipeId, String recipeName, Bitmap dishImage, MealType mealType, String description,
                  String preparationTime, String servingSizeDescription, List<Ingredient> ingredients,
                  List<String> cookingInstructions, List<String> tips, boolean isSeasonal, boolean isGlutenFree,
                  boolean isNutFree, boolean isSoyFree, boolean isRaw) {
        this.recipeId = recipeId;
        this.recipeName = recipeName;
        this.dishImage = dishImage;
        this.mealType = mealType;
        this.description = description;
        this.preparationTime = preparationTime;
        this.servingSizeDescription = servingSizeDescription;
        this.ingredients = ingredients;
        this.cookingInstructions = cookingInstructions;
        this.tips = tips;
        this.isSeasonal = isSeasonal;
        this.isGlutenFree = isGlutenFree;
        this.isNutFree = isNutFree;
        this.isSoyFree = isSoyFree;
        this.isRaw = isRaw;
    }

    public long getRecipeId() {
        return recipeId;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public Bitmap getDishImage() {
        return dishImage;
    }

    public MealType getMealType() {
        return mealType;
    }

    public String getDescription() {
        return description;
    }

    public String getPreparationTime() {
        return preparationTime;
    }

    public String getServingSizeDescription() {
        return servingSizeDescription;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public List<String> getCookingInstructions() {
        return cookingInstructions;
    }

    public List<String> getTips() {
        return tips;
    }

    public boolean isSeasonal() {
        return isSeasonal;
    }

    public boolean isGlutenFree() {
        return isGlutenFree;
    }

    public boolean isNutFree() {
        return isNutFree;
    }

    public boolean isSoyFree() {
        return isSoyFree;
    }

    public boolean isRaw() {
        return isRaw;
    }

}
