package view;

import controller.MovieService;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import model.Movie;

/**
 * Class for the model of JTable that shows the movies
 *
 * @author
 */
@SuppressWarnings("serial")
public class MovieTableModel extends AbstractTableModel {

    //The list that holds the movie objects
    private List<Movie> movies;
    //The 3 column names that will be used
    private static final String[] COLUMN_NAMES = {"Τίτλος ταινίας", "Βαθμολογία", "Περιγραφή"};
    //The object that gives access to the movie table of database
    private MovieService ms;

    /**
     * Constructor that initializes the list field from movie table
     *
     * @param ms
     */
    public MovieTableModel(MovieService ms) {
        this.ms = ms;
        movies = ms.findAllMovies();

    }

    /**
     * Constructor that initializes the list field with a given parameter
     *
     * @param ms
     * @param movies the parameter that fills up the list field
     */
    public MovieTableModel(MovieService ms, List<Movie> movies) {
        this.ms = ms;
        this.movies = movies;

    }

    /**
     * Gets the movie object of the selected row
     *
     * @param rowIndex the selected row
     * @return the movie object
     */
    Movie getMovie(int rowIndex) {
        return movies.get(rowIndex);
    }

    /**
     *
     * @return the list field
     */
    List<Movie> getMovies() {
        return movies;
    }

    /**
     * Updates the view should any change happened
     */
    void databaseUpdated() {
        movies = ms.findAllMovies();
        //Notifies all listeners that all cell values in the table's rows may have changed
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return movies.size();

    }

    @Override
    public int getColumnCount() {
        return COLUMN_NAMES.length;

    }

    @Override
    public String getColumnName(int columnIndex) {
        return COLUMN_NAMES[columnIndex];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return movies.get(rowIndex).getTitle();
            case 1:
                return movies.get(rowIndex).getRating();
            case 2:
                return movies.get(rowIndex).getOverview();
            default:
                return null;
        }

    }

    /**
     * Adds a row in the JTable dynamically given a movie object
     *
     * @param m the movie object that will be added in a new row
     */
    public void addRow(Movie m) {
        movies.add(m);
        //Notifies all listeners that rows in the range [i,i] , inclusive, have been added.
        fireTableRowsInserted(0, movies.size() - 1);

    }

    /**
     * Clears all rows of the JTable
     */
    public void removeAllRows() {
        if (!movies.isEmpty()) {
            for (int i = 0; i < movies.size(); i++) {

                movies.remove(i);
                //Notifies all listeners that rows in the range [i,i] , inclusive, have been deleted.
                fireTableRowsDeleted(i, i);
            }
            //Clears the movie list from movie objects
            movies.clear();

        }

    }

}
