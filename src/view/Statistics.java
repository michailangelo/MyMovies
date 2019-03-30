package view;

import controller.FavoriteListService;
import controller.MovieService;
import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import model.Favoritelist;
import model.Movie;

/**
 * Class for the JFrame of the last option of the main menu
 *
 * @author
 */
public class Statistics extends JFrame {

    //The JButton component for showing the best movies, the first button
    private JButton bestMoviesButton;
    //The JButton component for showing the best movies per favoritelist,the second button
    private JButton bestPerListButton;
    //The JTable that holds the movie data 
    private JTable movieTable;
    //The object to use for accessing the Movie objects from the database 
    private MovieService ms;
    //The object to use for accessing the Favoritelist objects from the database 
    private FavoriteListService fs;
    //The TableModel object for the Movie data 
    private MovieTableModel mtm;

    /**
     * Constructor for the JFrame component
     *
     * @param ms the MovieService object to have access to the movie objects of
     * the database
     * @param fs the FavoritelistService object to have access to favoritelist
     * objects of the database
     */
    public Statistics(MovieService ms, FavoriteListService fs) {
        //Sets the look and feel of the JFrame
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e) {
            System.out.println(e);
        }
        //Sets the title of the JFrame
        setTitle("Statistics");
        //Sets the size of the JFrame
        setSize(600, 300);
        //Specify the new JFrame's position
        setLocationByPlatform(true);
        this.ms = ms;
        this.fs = fs;
        //Initializing a JTable containing an empty arraylist
        mtm = new MovieTableModel(ms, new ArrayList<>());
        movieTable = new JTable(mtm);

        //Initializing a TableColumnManager object to handle dynamic hide and show of columns
        TableColumnManager tcm = new TableColumnManager(movieTable);
        //Removes the columns Overview and Rating
        tcm.hideColumn(2);
        tcm.hideColumn(1);

        //Creating a JButton and adding action listener
        bestMoviesButton = new JButton("Οι Kαλύτερες 10 Tαινίες");
        bestMoviesButton.addActionListener((ActionEvent)
                -> {
            //Shows the column rating
            tcm.showColumn(1);
            mtm.removeAllRows();
            showBestMovies();
        });
        bestPerListButton = new JButton("Οι Καλύτερες Ταινίες ανά Λίστα");
        bestPerListButton.addActionListener((ActionEvent)
                -> {
            tcm.hideColumn(1);
            mtm.removeAllRows();
            showBestPerList();
        });

        //Creating a JPanel for the buttons 
        JPanel panel = new JPanel();
        panel.add(bestMoviesButton);
        panel.add(bestPerListButton);
        //Adding the panel to JFrame 
        add(panel, BorderLayout.EAST);
        //Adding scrollbar to the JTable
        add(new JScrollPane(movieTable), BorderLayout.SOUTH);
        //Sizes the frame so that all its contents are at or above their preferred sizes
        pack();
        //Makes the frame visible
        setVisible(true);
    }

    /**
     * Method that adds the rows for the JTable of the first button
     */
    private void showBestMovies() {
        List<Movie> movies = ms.findAllMovies();
        //Calls the sort method with a comparator object with a sort key of rating. It sorts in descending order
        movies.sort(Comparator.comparing(Movie::getRating).reversed());
        //Dynamically adding rows to the JTable
        for (int i = 0; i < 10; i++) {
            mtm.addRow(movies.get(i));
        }
    }

    /**
     * Method that adds the rows for the JTable of the second button
     */
    private void showBestPerList() {
        List<Favoritelist> fl = fs.findAllFavoritelists();
        List<Movie> temp;
        for (Favoritelist f : fl) {
            if (!f.getMovieList().isEmpty()) {
                //Copying the array contents to a temporary
                temp = new ArrayList<>(f.getMovieList());
                temp.sort(Comparator.comparing(Movie::getRating).reversed());
                //After the sort is done from descending order, it picks the first item to show 
                mtm.addRow(temp.get(0));
            }
        }
    }

}
