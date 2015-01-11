package app.vegankitchen.app;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import app.vegankitchen.kitchen.Ingredient;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adam on 2014-12-29.
 */
public class IngredientListViewAdapter extends ArrayAdapter<Ingredient> {

    private List<Ingredient> ingredients;
    private Activity context;

    public IngredientListViewAdapter( Activity context, List<Ingredient> ingredients) {
        super(context, R.layout.ingredient_item, ingredients);
        this.context = context;
        this.ingredients = ingredients;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Ingredient ingredient = ingredients.get( position );

        LayoutInflater inflater = context.getLayoutInflater();
        View ingredientView = inflater.inflate( R.layout.ingredient_item, null, false);

        TextView ingredientText = (TextView) ingredientView.findViewById( R.id.ingredient_item);
        ingredientText.setText( ingredient.getIngredientName() );

        TextView ingredientQuantityText = (TextView) ingredientView.findViewById( R.id.ingredient_quantity );
        ingredientQuantityText.setText( ingredient.getDescription() );

        return ingredientView;
    }

}
