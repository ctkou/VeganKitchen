package app.vegankitchen.kitchen;

/**
 * Created by Eden on 2014-12-27.
 */
public class Ingredient {

    private String ingredientName;
    private String description;

    public Ingredient(String ingredientName, String description) {
        this.ingredientName = ingredientName;
        this.description = description;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public String getDescription() {
        return description;
    }
}
