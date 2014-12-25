package app.vegankitchen.app;

/**
 * Created by PC on 20-Dec-14.
 */
public class DrawerItem {

    private final int ID;
    private final String NAME;

    public DrawerItem ( int id, String name ) {
        ID = id;
        NAME = name;
    }

    public int getID() {
        return ID;
    }

    public String getNAME() {
        return NAME;
    }

    /**
     * Two DrawerItem are considered equal if they have the same NAME
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DrawerItem that = (DrawerItem) o;

        if (NAME != null ? !NAME.equals(that.NAME) : that.NAME != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return NAME != null ? NAME.hashCode() : 0;
    }
}
