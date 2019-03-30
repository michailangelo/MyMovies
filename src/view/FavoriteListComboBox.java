package view;

import controller.FavoriteListService;
import java.util.List;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import model.Favoritelist;

/**
 * Class for the JComboBox that shows the favoritelist objects
 *
 * @author
 */
public class FavoriteListComboBox extends AbstractListModel implements ComboBoxModel {

    //List to hold the favoritelist objects
    private List<Favoritelist> favorites;
    //Object that gives access to favoritelist data of the database
    private FavoriteListService fs;
    private Favoritelist f;

    public FavoriteListComboBox(FavoriteListService fs) {
        this.fs = fs;
        favorites = fs.findAllFavoritelists();
        f = new Favoritelist();
    }

    /**
     * Method for updating the view should any change happened.
     */
    void databaseUpdated() {
        favorites = fs.findAllFavoritelists();
        //AbstractListModel subclasses must call this method after one or more elements of the list change
        fireContentsChanged(this, 0, this.getSize());
    }

    @Override
    public int getSize() {
        return favorites.size();
    }

    @Override
    public Object getElementAt(int index) {

        return favorites.get(index);
    }

    @Override
    public void setSelectedItem(Object anItem) {
        f = (Favoritelist) anItem;
    }

    @Override
    public Object getSelectedItem() {
        return f;
    }
}
