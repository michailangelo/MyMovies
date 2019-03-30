package view;

import controller.FavoriteListService;
import controller.MovieService;
import java.awt.BorderLayout;
import java.util.ArrayList;
import javax.persistence.EntityManager;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import model.Favoritelist;
import model.Movie;

/**
 * Class for the frame of the second option of the main menu
 *
 * @author
 */
@SuppressWarnings("serial")
public class FavoriteListFrame extends JFrame {

    //The JList component that shows the favoritelist objects
    private JList favoriteList;
    //The JTable component to show the movies of the favoritelist
    private JTable movieTable;
    //The model of the favoritelist table
    private FavoriteListModel favoriteListModel;
    //The object that contains the favorite list objects of the database
    private FavoriteListService fs;
    //The object that contains the movie objects of the database
    private MovieService ms;
    //The object that is used as a tool to make changes in the database
    private EntityManager em;
    //The model of the movie table
    private MovieTableModel mtm;

    /**
     * Constructor that sets the frame's size and the look and feel, adds event
     * handling mechanisms for the JList and builds the JComponents
     *
     * @param fs
     * @param ms
     * @param em
     */
    public FavoriteListFrame(FavoriteListService fs, MovieService ms, EntityManager em) {
        //Sets the look and feel of the JFrame
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e) {
            System.out.println(e);
        }
        //Sets the title and the size 
        setTitle("FavoriteLists");
        setSize(600, 300);
        //Specify a new JFrame's position 
        setLocationByPlatform(true);

        this.fs = fs;
        this.ms = ms;
        this.em = em;
        //Initialize Table model and its JTable
        mtm = new MovieTableModel(ms, new ArrayList<>());
        movieTable = new JTable(mtm);
        //Adds the buttopanel to the frame
        add(buildButtonPanel(), BorderLayout.EAST);
        //Builds the JList of favoritelists
        favoriteList = buildFavoriteList(fs);
        //Adds the event handling mechanism when user clicks an item on the JList
        favoriteList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                //Clears the movies from the JTable 
                mtm.removeAllRows();

                if (e.getValueIsAdjusting() == false) {
                    Favoritelist fv = (Favoritelist) favoriteList.getSelectedValue();
                    if (fv != null) {
                        for (Movie m : fv.getMovieList()) {
                            //Adds the movies in the JTable dynamically for the particular favoritelist
                            mtm.addRow(m);
                        }
                    }
                }
            }
        });
        //Adds a scroll bar to the JList and JTable ands adds them to the JFrame
        add(new JScrollPane(favoriteList), BorderLayout.CENTER);
        add(new JScrollPane(movieTable), BorderLayout.SOUTH);
        //Sizes the frame so that all its contents are at or above their preferred sizes
        pack();
        //Makes the frame visible
        setVisible(true);

    }

    /**
     * Method that sets up a container with the buttons about the favoritelist.
     * It also adds the action listeners of the buttons
     *
     * @return the JPanel
     */
    private JPanel buildButtonPanel() {
        JPanel panel = new JPanel();
        JButton createButton = new JButton("Δημιουργία");
        createButton.setToolTipText("Add favoriteList");
        createButton.addActionListener((ActionEvent) -> {
            doAddButton();
        });
        panel.add(createButton);

        JButton editButton = new JButton("Επεξεργασία");
        editButton.setToolTipText("Edit selected favorite list");
        editButton.addActionListener((ActionEvent) -> {
            doEditButton();
        });
        panel.add(editButton);
        JButton deleteButton = new JButton("Διαγραφή");
        deleteButton.setToolTipText("Delete selected list");
        deleteButton.addActionListener((ActionEvent)
                -> {
            doDeleteButton();
        });
        panel.add(deleteButton);

        return panel;

    }

    /**
     * Method that setups a JDialog to add a new favoritelist to the database
     */
    private void doAddButton() {
        FavoriteListDialog favoriteDialog = new FavoriteListDialog(this, "Add Favorite List", true, em, fs, favoriteListModel);
        //Sets the location of dialog in respect of parent container
        favoriteDialog.setLocationRelativeTo(this);
        favoriteDialog.setVisible(true);

    }

    /**
     * Method that handles the edit of the name of the favoritelist in the
     * database. It setups a JDialog.
     */
    private void doEditButton() {
        //Gets the selected row of the JList
        int selectedIndex = favoriteList.getSelectedIndex();
        //Checks if nothing is selected
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(this, "No list is currently selected.", "No list selected", JOptionPane.ERROR_MESSAGE);

        } else {
            //Gets the object of the selected favoritelist
            Favoritelist list = favoriteListModel.getFavoriteList(selectedIndex);
            //Creates a new JDialog
            FavoriteListDialog favoriteDialog = new FavoriteListDialog(this, "Edit Favorite List", true, list, em, fs, favoriteListModel);

            favoriteDialog.setLocationRelativeTo(this);
            favoriteDialog.setVisible(true);

        }
    }

    /**
     * Method that deletes the selected favoritelist from the database. It also
     * gives warning messages for cases like if no list is selecting and
     * confirming the delete
     */
    private void doDeleteButton() {
        int selectedIndex = favoriteList.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(this, "No list is currently selected.", "No list selected", JOptionPane.ERROR_MESSAGE);

        } else {
            Favoritelist list = favoriteListModel.getFavoriteList(selectedIndex);
            int ask = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete " + list.getName() + " from the database?",
                    "Confirm delete", JOptionPane.YES_NO_OPTION
            );

            if (ask == JOptionPane.YES_OPTION) {

                em.getTransaction().begin();
                // Delete the value of foreign key favoritelistid of movietable
                for (Movie m : list.getMovieList()) {
                    ms.setFavoriteList(m.getMovieid(), null);
                }
                //Deleting the favoritelist record from the database
                fs.removeFavoritelist(list.getFavoritelistid());
                em.getTransaction().commit();
                //Updates the view
                favoriteListModel.databaseUpdated();
                //Removes the movies from the JTable
                mtm.removeAllRows();

            }
        }
    }

    /**
     * Updates the view if necessary
     */
    void fireDatabaseUpdatedEvent() {
        favoriteListModel.databaseUpdated();
    }

    /**
     * Method for creating the JList component for the favoritelist objects
     *
     * @param fs
     * @return the JList
     */
    private JList buildFavoriteList(FavoriteListService fs) {
        favoriteListModel = new FavoriteListModel(fs);
        JList list = new JList(favoriteListModel);
        //Sets the selection mode on the JList
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        return list;
    }

}
