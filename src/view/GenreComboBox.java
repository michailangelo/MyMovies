/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import controller.GenreService;
import java.util.List;
import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import model.Genre;

/**
 * Class for JComboBox that shows the genre of the movies.
 *
 * @author
 */
public class GenreComboBox extends AbstractListModel implements ComboBoxModel {

    //The list that holds the genre objects
    private List<Genre> genres;
    //The object that gives access to the genre table of the database
    private GenreService gs;

    private Genre g;

    /**
     * Constructor that fills the list field from the data of the database
     *
     * @param gs
     */
    public GenreComboBox(GenreService gs) {
        this.gs = gs;
        genres = gs.findAllGenres();
        g = new Genre();
    }

    /**
     * Notifies the view
     */
    void databaseUpdated() {
        genres = gs.findAllGenres();
        //AbstractListModel subclasses must call this method after one or more elements of the list change
        fireContentsChanged(this, 0, this.getSize());
    }

    @Override
    public int getSize() {
        return genres.size();
    }

    @Override
    public Object getElementAt(int index) {

        return genres.get(index);
    }

    @Override
    public void setSelectedItem(Object anItem) {
        g = (Genre) anItem;
    }

    @Override
    public Object getSelectedItem() {
        return g;
    }

}
