package app.vegankitchen.app;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Eden on 2014-12-29.
 */
public class InstructionListViewAdapter extends ArrayAdapter<String> {

    private List<String> instructions;
    private Activity context;

    public InstructionListViewAdapter( Activity context, List<String> instructions ) {
        super(context, R.layout.instruction_item, instructions);
        this.context = context;
        this.instructions = instructions;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String instruction = instructions.get( position );

        LayoutInflater inflater = context.getLayoutInflater();
        View instructionView = inflater.inflate( R.layout.instruction_item, null, false);

        TextView instructionText = (TextView) instructionView.findViewById( R.id.instruction_item );
        instructionText.setText( instruction );

        return instructionView;
    }
}
