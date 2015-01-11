package app.vegankitchen.app;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Adam on 2014-12-29.
 */
public class TipListViewAdapter extends ArrayAdapter<String> {

    private List<String> tips;
    private Activity context;

    public TipListViewAdapter( Activity context, List<String> tips) {
        super(context, R.layout.tip_item, tips);
        this.context = context;
        this.tips = tips;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String tip = tips.get( position );

        LayoutInflater inflater = context.getLayoutInflater();
        View tipView = inflater.inflate( R.layout.tip_item, null, false);

        TextView tipText = (TextView) tipView.findViewById( R.id.tip_item );
        tipText.setText( tip );

        return tipView;
    }
}
