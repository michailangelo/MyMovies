package view;

import controller.FavoriteListService;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import controller.GenreService;
import controller.MovieService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import model.Genre;
import model.Movie;

/**
 * Class for the main frame of the application
 *
 * @author
 */
@SuppressWarnings("serial")
public class Menu extends JFrame implements ActionListener {

    //Specifies the width and the height of the JFrame window
    public static final int WIDTH = 600;
    public static final int HEIGHT = 400;

    //The factory that gives the entity manager and determines the configuration parameter
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("MyMoviesPU");

    //The entity manager that gives access to persistent entities
    EntityManager em = emf.createEntityManager();

    //The object that gives access to the genre table of the database
    GenreService genreService = new GenreService(em);
    //The object that gives access to the movie table of the database
    MovieService movieService = new MovieService(em);
    //The object that gives access to the favoritelist table of the database
    FavoriteListService favoriteListService = new FavoriteListService(em);

    @SuppressWarnings("OverridableMethodCallInConstructor")
    /**
     * Constructor that builds the main menu of the application. It initializes
     * the JComponents and adds the action listeners to the menu items
     *
     */
    public Menu() {
        //Builds the JFrame,sets its name,size,location and behavor when user decides to close it.
        super("MyMoviesApp");
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationByPlatform(true);

        //Sets the look and feel of the frame
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException e) {
            System.err.println("Unsupported look and feel.");

        }

        //Creates the main menu of the application
        JMenu actionsMenu = new JMenu("Choose an action");

        //Creates the menu options and adds their action listeners
        JMenuItem option1 = new JMenuItem("Ανάκτηση Δεδομένων Ταινιών");
        option1.addActionListener(this);
        actionsMenu.add(option1);
        JMenuItem option2 = new JMenuItem("Διαχείριση Λιστών Αγαπημένων Ταινιών");
        option2.addActionListener(this);
        actionsMenu.add(option2);
        JMenuItem option3 = new JMenuItem("Αναζήτηση Ταινιών");
        option3.addActionListener(this);
        actionsMenu.add(option3);
        JMenuItem option4 = new JMenuItem("Στατιστικά");
        option4.addActionListener(this);
        actionsMenu.add(option4);
        JMenuItem option5 = new JMenuItem("Έξοδος");
        option5.addActionListener(this);
        actionsMenu.add(option5);
        //Creates the menu bar that contains the menu and adds it to the frame
        JMenuBar bar = new JMenuBar();
        bar.add(actionsMenu);
        setJMenuBar(bar);

    }

    /**
     * Method that performs the action listener of all the menu items
     *
     * @param e the event that is fired when user clicks on an item of the menu
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        //Gets the title of the item clicked
        String buttonString = e.getActionCommand();

        if (buttonString.equals("Ανάκτηση Δεδομένων Ταινιών")) {
            //Clears the movie and genre tables of the database
            em.getTransaction().begin();
            movieService.dropTable();
            genreService.dropTable();
            em.getTransaction().commit();
            //The url about the genres data
            String GenresURL = "https://api.themoviedb.org/3/genre/movie/list?language=en-US&api_key=0db43b41ed771f67aac5a66c36628494";

            try {
                //Allows to read data from a URL source
                URL url1 = new URL(GenresURL);
                InputStream is = url1.openStream();
                //Parsing Json data
                JsonReader rdr = Json.createReader(is);
                JsonObject obj = rdr.readObject();
                JsonArray results = obj.getJsonArray("genres");
                //Fills the genre table of database with records 
                for (JsonObject result : results.getValuesAs(JsonObject.class)) {
                    //Checks if the genres to be added are either action, romance or science fiction
                    if (result.getInt("id") == 28 || result.getInt("id") == 10749 || result.getInt("id") == 878) {
                        em.getTransaction().begin();
                        Genre gen = genreService.createGenre(result.getInt("id"), result.getString("name"));
                        em.getTransaction().commit();
                    }
                }

            } catch (IOException a) {
                a.printStackTrace();
            }
            //Gets the 50 pages from the url
            for (int i = 1; i < 50; i++) {
                //The url about the movies data specifing the page
                String MoviesURL = "https://api.themoviedb.org/3/discover/movie?api_key=0db43b41ed771f67aac5a66c36628494&language=en-US&sort_by=popularity.desc&include_adult=false&include_video=false&page=" + i + "&primary_release_date.gte=2000-01-01&with_genres=28%7C10749%7C878";
                try {
                    //Allows to read data from a URL source
                    URL url2 = new URL(MoviesURL);
                    InputStream is = url2.openStream();
                    //Parsing Json data
                    JsonReader rdr = Json.createReader(is);
                    JsonObject obj = rdr.readObject();
                    JsonArray results = obj.getJsonArray("results");
                    for (JsonObject result : results.getValuesAs(JsonObject.class)) {
                        //Fills the movie table of the database with records 
                        em.getTransaction().begin();
                        //To create a movie entity the format "YYY-MM-dd" is used to parse the date from Json data. Also, it stores only the first 500 characters as a value in the "Overview" column
                        Movie mov = movieService.createMovie(result.getInt("id"), result.getString("title"),
                                genreService.findGenre(getGenre(result.getJsonArray("genre_ids"))),
                                new SimpleDateFormat("YYY-MM-dd").parse(result.getString("release_date")),
                                (float) result.getJsonNumber("vote_average").doubleValue(),
                                (result.getString("overview").length() > 500)
                                ? result.getString("overview").substring(0, 500)
                                : result.getString("overview"));
                        em.getTransaction().commit();

                    }

                } catch (IOException a) {
                    a.printStackTrace();
                } catch (ParseException ex) {
                    Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
            JOptionPane.showMessageDialog(this, "Η ανάκτηση των δεδομένων ολοκληρώθηκε.", "Updated", JOptionPane.INFORMATION_MESSAGE);

        }
        if (buttonString.equals("Διαχείριση Λιστών Αγαπημένων Ταινιών")) {
            //Creates the JFrame for the second option
            FavoriteListFrame favoritelistFrame = new FavoriteListFrame(favoriteListService, movieService, em);

            favoritelistFrame.setLocationRelativeTo(this);
            favoritelistFrame.setVisible(true);

        }
        if (buttonString.equals("Αναζήτηση Ταινιών")) {
            //Creates the JDialog for the third option
            MovieDialog md = new MovieDialog(this, "Search Dialog", true, em, movieService, genreService, favoriteListService);
            md.setLocationRelativeTo(this);
            md.setVisible(true);

        }

        if (buttonString.equals("Στατιστικά")) {
            //Creates the JFrame for the fourth option
            Statistics stat = new Statistics(movieService, favoriteListService);
            stat.setLocationRelativeTo(this);
            stat.setVisible(true);

        }

        if (buttonString.equals("Έξοδος")) {
            //Closes the application
            System.exit(0);

        }

    }

    /**
     * Method that gets the first genre id out of the three possible values
     *
     * @param results the array of the three possible values
     * @return the first value of the array
     */
    static int getGenre(JsonArray results) {
        int result = 0;
        for (int i = 0; i < results.size(); i++) {
            if (results.getInt(i) == 28 || results.getInt(i) == 10749 || results.getInt(i) == 878) {
                result = results.getInt(i);
                break;
            }

        }
        return result;
    }

}
