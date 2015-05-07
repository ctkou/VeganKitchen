package app.vegankitchen.app;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import app.vegankitchen.kitchen.Recipe;

import java.util.List;

/**
 * Created by Adam on 2014-12-28.
 */
public class RecipeListViewAdapter extends ArrayAdapter<Recipe> {

    private List<Recipe> recipes;
    private Activity context;

    public RecipeListViewAdapter(Activity context, List<Recipe> recipes) {
        super(context, R.layout.dish_item, recipes);
        this.context = context;
        this.recipes = recipes;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Recipe recipe = recipes.get(position);

        LayoutInflater inflater = context.getLayoutInflater();
        View recipeView = inflater.inflate(R.layout.dish_item, null, false);

        ImageView recipeImage = (ImageView) recipeView.findViewById( R.id.dish_image );
        recipeImage.setImageBitmap( recipe.getDishImage() );
        recipeImage.setTag( recipe );

        TextView recipeName = (TextView) recipeView.findViewById( R.id.dish_name );
        recipeName.setText( recipe.getRecipeName() );

        TextView recipeDescription = (TextView) recipeView.findViewById( R.id.dish_description );
        recipeDescription.setText(recipe.getDescription());

        return recipeView;
    }
}
