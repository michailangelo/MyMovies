package view;

import controller.FavoriteListService;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.persistence.EntityManager;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import model.Favoritelist;

/**
 * Class for the JDialog menu of Favoritelist manipulation
 *
 * @author
 */
public class FavoriteListDialog extends JDialog {

    //The component that shows the name of the favoritelist
    private JTextField nameField;
    //Buttons that confirm and cancel operation
    private JButton confirmButton;
    private JButton cancelButton;
    //The object that can be used as a tool to make changes in the database
    private EntityManager em;
    //Grants access to favoritelist objects of the database
    private FavoriteListService fs;
    //Model that JList is built on
    private FavoriteListModel fm;
    //The field that holds the list of favoritelist objects
    private Favoritelist list;

    /**
     * Constructor for creating a JDialog.
     *
     * @param parent the JFrame that contains the JDialog to be created
     * @param title the title of the JDialog
     * @param modal specifies whether dialog blocks user input to other
     * top-level windows when shown
     * @param em the entity manages which grants access to the database
     * @param fs the object that grants access to the java objects of the
     * favoritelist table
     * @param fm the model of the JList
     */
    public FavoriteListDialog(java.awt.Frame parent, String title, boolean modal, EntityManager em, FavoriteListService fs, FavoriteListModel fm) {
        super(parent, title, modal);
        this.em = em;
        this.fs = fs;
        this.fm = fm;
        list = new Favoritelist(1);
        initComponents();
    }

    /**
     * Constructor that creates the JDialog of the edit button
     *
     * @param parent parent the JFrame that contains the JDialog to be created
     * @param title title the title of the JDialog
     * @param modal specifies whether dialog blocks user input to other
     * @param list the list to be edited
     * @param em the entity manages which grants access to the database
     * @param fs the object that grants access to the java objects of the
     * favoritelist table
     * @param fm the model of the JList
     */
    public FavoriteListDialog(java.awt.Frame parent, String title, boolean modal, Favoritelist list, EntityManager em, FavoriteListService fs, FavoriteListModel fm) {
        this(parent, title, modal, em, fs, fm);
        this.list = list;
        //Setting the title of the button
        confirmButton.setText("Αποθήκευση");
        //Setting the default input of the JTextField
        nameField.setText(list.getName());

    }

    /**
     * Initializing component parts
     */
    private void initComponents() {
        //Creating textfield and buttons
        nameField = new JTextField(50);
        cancelButton = new JButton();
        confirmButton = new JButton();
        //When user closes the window, it justs disposes the JDialog
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        //Setting the titles of the buttons and their action listeners 
        cancelButton.setText("Ακύρωση");
        cancelButton.addActionListener((ActionEvent) -> {
            cancelButtonActionPerformed();
        });
        confirmButton.setText("Δημιουργία");
        confirmButton.addActionListener((ActionEvent) -> {
            confirmButtonActionPerformed();
        });
        //Creating a JPanel container
        JPanel buttonPanel = new JPanel();
        //Setting its layout
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        //Adding the buttons to the panel
        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);

        //Setting the layout of the JDialog
        setLayout(new BorderLayout());
        //Adding the label ,the textfield and the panel to the JDialog
        add(new JLabel("Name:"), BorderLayout.CENTER);
        add(nameField);
        add(buttonPanel, BorderLayout.SOUTH);
        //Keeps the borders of various JComponents in check
        pack();

    }

    /**
     * Method that handles the cancel button
     */
    private void cancelButtonActionPerformed() {
        //Disposes the JDialog
        dispose();
    }

    /**
     * Method that handles the confirm button
     */
    private void confirmButtonActionPerformed() {
        setData();
        //Checks if the confirm button is about creating a new favorite list or editing an existing one
        if (confirmButton.getText().equals("Δημιουργία")) {
            doAdd();
        } else {
            doEdit();
        }

    }

    /**
     * Method for setting the name of the favoritelist
     */
    private void setData() {
        String name = nameField.getText();
        list.setName(name);

    }

    /**
     * Method for editing the favoritelist's name
     */
    private void doEdit() {
        ///Updating the name column of the favoritelist table in the database
        em.getTransaction().begin();
        fs.updateName(list.getFavoritelistid(), nameField.getText());
        em.getTransaction().commit();
        //Closes the dialog
        dispose();
        //Updates the view after the change in the favoritelist table
        fireDatabaseUpdatedEvent();
    }

    /**
     * Method for adding a new Favoritelist object in the database
     */
    private void doAdd() {
        //Adding a new favoritelist record in the favoritelist table of the database
        em.getTransaction().begin();
        fs.createFavoritelist(fm.getSize() + 1, nameField.getText());
        em.getTransaction().commit();
        //Closes the dialog
        dispose();
        //Updates the view after the change in the favoritelist table
        fireDatabaseUpdatedEvent();
    }

    /**
     * Method for updating the container of the JDialog should any change
     * happened.
     */
    private void fireDatabaseUpdatedEvent() {
        FavoriteListFrame mainWindow = (FavoriteListFrame) getOwner();
        mainWindow.fireDatabaseUpdatedEvent();
    }
}
