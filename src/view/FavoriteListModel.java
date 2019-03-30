package view;

import controller.FavoriteListService;
import java.util.List;
import javax.swing.AbstractListModel;
import model.Favoritelist;

/**
 * Class for the JTable component for Favoritelist object
 *
 * @author
 */
public class FavoriteListModel extends AbstractListModel {

    //The list that holds the favoritelist objects
    private List<Favoritelist> fl;
    //The object that gives access to the favoritelist table of the database
    private FavoriteListService fs;

    /**
     * Constructor that fills the list field with the data from the database
     *
     * @param fs
     */
    public FavoriteListModel(FavoriteListService fs) {
        this.fs = fs;
        fl = fs.findAllFavoritelists();
    }

    /**
     * Method that gets favoritelist object of the selected row
     *
     * @param rowIndex the selected index
     * @return the favoritelist object
     */
    Favoritelist getFavoriteList(int rowIndex) {
        return fl.get(rowIndex);
    }

    /**
     * Updates the view if any change happened in the database
     */
    void databaseUpdated() {
        fl = fs.findAllFavoritelists();
        fireContentsChanged(this, 0, this.getSize());
        //AbstractListModel subclasses must call this method after one or more elements are removed from the model.
        fireIntervalRemoved(this, 0, getSize());
    }

    @Override
    public int getSize() {
        return fl.size();
    }

    @Override
    public Object getElementAt(int index) {
        return this.getFavoriteList(index);

    }

}
