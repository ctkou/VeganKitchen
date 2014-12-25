package app.vegankitchen.app;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by PC on 20-Dec-14.
 */
public class DrawerCategory {

    private final int ID;
    private final String NAME;

    private List<DrawerItem> drawerItems = new ArrayList<DrawerItem>();

    public DrawerCategory( int id, String name ) {
        ID = id;
        NAME = name;
    }

    public int getID() {
        return ID;
    }

    public String getNAME() {
        return NAME;
    }

    public List<DrawerItem> getDrawerItems() {
        return drawerItems;
    }

    /**
     * EFFECT: if the drawerItems does not contain the drawerItem, add it to the list, else does nothing
     * @param drawerItem
     */
    public void addDrawerItem( DrawerItem drawerItem ) {
        if ( !drawerItems.contains(drawerItem) )
            drawerItems.add(drawerItem);
    }

}
