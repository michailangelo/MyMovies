package view;

import controller.FavoriteListService;
import controller.GenreService;
import controller.MovieService;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import model.Favoritelist;
import model.Genre;
import model.Movie;

/**
 * Class for the JDialog of the third option of the main menu
 *
 * @author
 */
public class MovieDialog extends JDialog {

    //The JComboBox about genres
    private JComboBox cb;
    //The JComboBox about favoritelists
    private JComboBox cb2;
    //The textfield about the year
    private JTextField yearField;
    private JTable movieTable;
    private JButton searchButton;
    private JButton clearButton;
    private JButton removeButton;
    //The label about favoritelists
    private JLabel label;
    private EntityManager em;
    private MovieService ms;
    private GenreService gs;
    private FavoriteListService fs;
    private MovieTableModel mtm;

    /**
     * Constructor that builds the JDialog container. It uses a private method
     * to initialize JComponents
     *
     * @param parent
     * @param title
     * @param modal
     * @param em
     * @param ms
     * @param gs
     * @param fs
     */
    public MovieDialog(java.awt.Frame parent, String title, boolean modal, EntityManager em, MovieService ms, GenreService gs, FavoriteListService fs) {
        super(parent, title, modal);
        this.em = em;
        this.ms = ms;
        this.gs = gs;
        this.fs = fs;
        initComponents();
    }

    /**
     * Method that initializes the various JComponents, adds action listeners to
     * the buttons, sets the Layout manager and finally adds the JCompents to
     * the main JDialog
     */
    private void initComponents() {
        //Initialiazing the model for genre combobox
        GenreComboBox gcb = new GenreComboBox(gs);
        cb = new JComboBox(gcb);
        FavoriteListComboBox fcb = new FavoriteListComboBox(fs);
        cb2 = new JComboBox(fcb);
        //Initializing an empty JTable model
        mtm = new MovieTableModel(ms, new ArrayList<>());
        yearField = new JTextField(4);
        movieTable = new JTable(mtm);
        //Setting a sorting method for the JTable
        movieTable.setAutoCreateRowSorter(true);
        clearButton = new JButton();
        searchButton = new JButton();
        removeButton = new JButton();
        label = new JLabel("Προσθήκη στη λίστα:");
        //Disposes the window
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        clearButton.setText("Καθαρισμός Κριτηρίων");
        clearButton.addActionListener((ActionEvent) -> {
            clearButtonActionPerformed();
        });

        searchButton.setText("Αναζήτηση");
        searchButton.addActionListener((ActionEvent) -> {
            searchButtonActionPerformed();
        });

        removeButton.setText("Αφαίρεση από Λίστα");
        removeButton.addActionListener((ActionEvent) -> {
            removeButtonActionPerformed();
        });

        //Adds action listener for the JComboBox selection
        cb2.addActionListener((ActionEvent)
                -> {
            addCheckBoxActionPerfomed();
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(searchButton);
        buttonPanel.add(clearButton);

        JPanel labelTextPanel = new JPanel();
        labelTextPanel.setLayout(new FlowLayout());
        labelTextPanel.add(cb);
        labelTextPanel.add(new JLabel("Year:"));
        labelTextPanel.add(yearField);

        JPanel buttonLabelTextPanel = new JPanel();
        buttonLabelTextPanel.setLayout(new BorderLayout());
        buttonLabelTextPanel.add(labelTextPanel, BorderLayout.NORTH);
        buttonLabelTextPanel.add(buttonPanel, BorderLayout.SOUTH);

        JPanel comboButtonPanel = new JPanel();
        comboButtonPanel.setLayout(new FlowLayout());
        comboButtonPanel.add(label);
        comboButtonPanel.add(cb2);
        comboButtonPanel.add(removeButton);

        setLayout(new BorderLayout());
        add(buttonLabelTextPanel, BorderLayout.WEST);
        add(new JScrollPane(movieTable), BorderLayout.CENTER);
        add(comboButtonPanel, BorderLayout.SOUTH);
        pack();

    }

    /**
     * Method that adds a movie object from the JTable to the favoritelist table
     * of the database
     */
    private void addCheckBoxActionPerfomed() {

        if (movieTable.getSelectedRow() != -1 && cb2.getSelectedIndex() != -1) {

            Movie mv = mtm.getMovie(movieTable.getSelectedRow());
            Favoritelist fl = (Favoritelist) cb2.getSelectedItem();
            //Checks if the movie to be added is already on the favoritelist
            for (Movie m : fl.getMovieList()) {
                if (m.getTitle().equals(mv.getTitle())) {
                    return;
                }
            }
            //Before adding the movie to the new favoritelist,the movie must be removed from any old favoritelists
            removeButtonActionPerformed();
            em.getTransaction().begin();
            //Updating the foreign key favoritelistid of the movie table
            ms.setFavoriteList(mv.getMovieid(), fl);
            //Updating the favoritelist object with the new movie object
            fs.addMovie(fl.getFavoritelistid(), mv);
            em.getTransaction().commit();

        }

    }

    /**
     * The action listener of the clear button
     */
    private void clearButtonActionPerformed() {
        //It delets the comboBox selection and the text written 
        cb.setSelectedIndex(-1);
        yearField.setText("");

    }

    /**
     * The search button action listener. It uses a private method to validate
     * the data input from the user
     */
    private void searchButtonActionPerformed() {
        if (validateData()) {
            mtm.removeAllRows();
            for (Movie m : getMovies()) {
                mtm.addRow(m);
            }

        }

    }

    /**
     * The remove button action listener
     */
    private void removeButtonActionPerformed() {
        //Checks if something is selected
        if (movieTable.getSelectedRow() != -1) {
            //Gets the movie object out of the selected item
            Movie mv = mtm.getMovie(movieTable.getSelectedRow());
            //Checks if the movie belongs to a favoritelist
            if (mv.getFavoritelistid() != null) {
                Favoritelist fl = mv.getFavoritelistid();
                em.getTransaction().begin();
                ms.setFavoriteList(mv.getMovieid(), null);
                fs.removeMovie(fl.getFavoritelistid(), mv);
                em.getTransaction().commit();
            }
        }
    }

    /**
     * Method that checks the validity of user input
     *
     * @return false if user hasnt completed both the text field and the
     * combobox
     */
    private boolean validateData() {
        //Checks if nothing is selected or written
        if (cb.getSelectedIndex() == -1 || yearField.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Please select a genre and fill the year field!",
                    "Missing Criteria", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }

        return true;
    }

    /**
     * Method that gets the movies requested by the user
     *
     * @return the list of movies that fit the criteria set by the user
     */
    private List<Movie> getMovies() {
        List<Movie> movies = ms.findAllMovies();

        //The list that holds the movies that fit the criteria
        List<Movie> results = new ArrayList<>();
        //Gets the Genre object of the selected item of comboBox
        Genre g = (Genre) cb.getSelectedItem();
        //Parses the year input from user 
        int year = Integer.parseInt(yearField.getText());
        for (Movie m : movies) {
            //Checks the genreid and release year of the movie to match the selections of the user
            //The toInstant().atZone(ZoneId.systemDefault()).toLocalDate() chaining methods invocations convert the Date object to LocalDate object
            if (m.getGenreid().getGenreid().equals(g.getGenreid()) && m.getReleasedate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getYear() == year) {
                results.add(m);
            }
        }
        return results;

    }

}
