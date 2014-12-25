package app.vegankitchen.app;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * ExpandableListAdapter used to provide data and Views from some data to an expandable list view.
 * Adapters inheriting this class should verify that the base implementations of getCombinedChildId(long, long) and
 * getCombinedGroupId(long) are correct in generating unique IDs from the group/children IDs.
 */
public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<DrawerCategory> drawerCategories;

    /**
     * Constructor
     */
    public ExpandableListAdapter ( Context context, List<DrawerCategory> drawerCategories) {
        this.context = context;
        this.drawerCategories = drawerCategories;
    }

    /**
     * EFFECT: Gets the number of groups.
     * @return
     */
    @Override
    public int getGroupCount() {
        return drawerCategories.size();
    }

    /**
     * EFFECT: Gets the number of children in a specified group.
     * REQUIRES: groupPosition is valid (i.e. >0 and < getGroupCount() )
     * @param groupPosition
     * @return
     */
    @Override
    public int getChildrenCount(int groupPosition) {
        return drawerCategories.get( groupPosition ).getDrawerItems().size();
    }

    /**
     * EFFECT: Gets the data associated with the given group.
     * @param groupPosition
     * @return
     */
    @Override
    public Object getGroup(int groupPosition) {
        return drawerCategories.get(groupPosition);
    }

    /**
     * EFFECT: Gets the data associated with the given child within the given group.
     * @param groupPosition
     * @param childPosition
     * @return
     */
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return drawerCategories.get( groupPosition ).getDrawerItems().get( childPosition );
    }

    /**
     * EFFECT: Gets the ID for the group at the given position.
     * @param groupPosition
     * @return
     */
    @Override
    public long getGroupId(int groupPosition) {
        return (long) drawerCategories.get( groupPosition ).getID();
    }

    /**
     * EFFECT: Gets the ID for the given child within the given group.
     * @param groupPosition
     * @param childPosition
     * @return
     */
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return (long) drawerCategories.get( groupPosition ).getDrawerItems().get( childPosition ).getID();
    }

    /**
     * EFFECT: Indicates whether the child and group IDs are stable across changes to the underlying data.
     * @return
     */
    @Override
    public boolean hasStableIds() {
        return true;
    }

    /**
     * EFFECT: Gets a View that displays the given group.
     * @param groupPosition
     * @param isExpanded
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        DrawerCategory drawerCategory = drawerCategories.get(groupPosition);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = (View) inflater.inflate( R.layout.drawer_category, parent, false);
        TextView textView = (TextView) view.findViewById( R.id.drawer_category );
        ImageView imageView = (ImageView) view.findViewById( R.id.indicator );

        textView.setText( drawerCategory.getNAME() );
        textView.setTextSize( 25 );
        textView.setPadding( 30, 20, 0, 10);
        textView.setTextColor( Color.WHITE );

        if ( drawerCategory.getDrawerItems().size() > 0 ) {
            if (isExpanded)
                imageView.setImageResource(R.drawable.ic_action_expand);
            else
                imageView.setImageResource(R.drawable.ic_action_collapse);
        }

        return view;
    }

    /**
     * EFFECT: Gets a View that displays the data for the given child within the given group.
     * @param groupPosition
     * @param childPosition
     * @param isLastChild
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
                             ViewGroup parent) {

        DrawerCategory drawerCategory = drawerCategories.get(groupPosition);
        DrawerItem drawerItem = drawerCategory.getDrawerItems().get( childPosition );

        TextView textView = new TextView(context);
        textView.setText( drawerItem.getNAME() );
        textView.setTextSize( 20 );
        textView.setPadding( 60, 20, 0, 10);
        textView.setTextColor(Color.WHITE);

        return textView;
    }

    /**
     * EFFECT: Whether the child at the specified position is selectable.
     * @param groupPosition
     * @param childPosition
     * @return
     */
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
