package mvh.app;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import mvh.enums.WeaponType;
import mvh.util.Reader;
import mvh.util.Writer;
import mvh.world.*;
import java.io.File;
import java.util.HashSet;

/**
 * Controller for MVH map editor program
 * @author Ryan Loi
 * @date (dd / mm / yr): 27/03/22
 * @version 1.0
 */

public class MainController {

    //Store the data of editor
    private static World world;

    // Stores the current file the user last loaded
    private static File fileObject;


    // A hashset the contains all the symbols previously used by other entities to ensure there are no duplicates
    private static HashSet<String> PastSymbols = new HashSet<String>();

    /**
     * Setup the window state
     */
    @FXML
    public void initialize() {
        // adding weapons to the monsters weapon combobox
        MonsterWeaponID.getItems().addAll("CLUB (2 DMG)",
                "AXE (3 DMG)", "SWORD (4 DMG)");
    }


    // ----------------------------------- Menu Bar Options --------------------------------------------------------- //


    /**
     * This creates the alert box when clicking on the about option under the help tab in the menu bar. The box will
     * display information about the author and about the program.
     * @param event The event is clicking on the about option under the help tab in the menu bar
     */
    @FXML
    void MenuHelpAbout(ActionEvent event) {
        // making the alert
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        // setting titles
        alert.setTitle("About");
        alert.setHeaderText("Message");
        // create message
        alert.setContentText("Author: Ryan Loi\nUCID: 30019520\nEmail: ryan.loi@ucalgary.ca\nProfessor/Author2: Jonathan Hudson\n" +
                "Tutorial: T05 - Anika Achari\nProgram " +
                "Version: 1.0\n\nThis is a simple GUI program that edits the World for the Monsters versus Heroes game");
        alert.show();

    }


    /**
     * This allows the program to end when the user clicks on the quit option under the File tab in the menu bar. The
     * program will end with error code 0 which means there is no issue
     * @param event The event is clicking on the quit option under the File tab in the menu bar
     */
    @FXML
    void MenuQuit(ActionEvent event) {
        // exiting the program when the user clicks on quit
        System.exit(0);

    }


    /**
     * This allows the program to load a file that the user selects, after clicking on the load file option under the
     * File tab in the menu bar.
     *  @param event The event is clicking on the Load File option under the File tab in the menu bar
     */
    @FXML
    void MenuLoadFile(ActionEvent event) {
        // create the file chooser
        FileChooser fileChooser = new FileChooser();
        //launch the file chooser and get the file path the user selects
        File LoadedFile = fileChooser.showOpenDialog(new Stage());


        // if the loaded file isn't null (meaning the user didn't choose anything)
        if (LoadedFile != null) {
            // assign create a new file object if the loaded file path isn't null and assign it to our field variable
            fileObject = new File(String.valueOf(LoadedFile));
            // check if the file is readable, exists, and is a file before sending it to the render to generate a world
            if(fileObject.canRead() && fileObject.exists() && fileObject.isFile()) {
                // displaying file name in the left status
                LeftStatus.setText("Loading file named: " + fileObject.getName());
                // letting the reader generate the world which will be assigned to our field world
                world = Reader.loadWorld(fileObject);

                // if the world isn't null print the world to be displayed
                if(world != null){
                    // display the world string (game map) to the label or view portion of the GUI using the worldString and
                    // toString override in world
                    MapBox.setText(world.toString());
                    //set right status saying the world is loaded in green
                    RightStatus.setTextFill(Color.GREEN);
                    RightStatus.setText("World Drawn!");
                }
                else {
                    // set right status saying couldn't load file if null was returned as that was the design of the reader
                    // to return null if a file couldn't be read. Also displays this error message in red
                    RightStatus.setTextFill(Color.RED);
                    RightStatus.setText("Error reading from the file named: " + fileObject.getName());

                    // making a popup alert
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    // setting titles
                    alert.setTitle("File Reading Error");
                    alert.setHeaderText("Error: Input file");
                    // create message
                    alert.setContentText("Error: The file could not be read, ensure that the file contains the correct" +
                            " Monster versus Heroes format and its contents are correct\n\nCommon errors include: \n" +
                            "- A non Hero or Monster type\n-An invalid Monster Weapon type\n-Negative Entity Health\n-Attack or Armor less than 0\n-Mismatching row and column" +
                            " data compared to the line in the file that the entity was contained in\n-Non Integer values\n");
                    // show alert message
                    alert.show();
                }
            }
        }
    }


    /**
     * Allows the user to click on the save button under the File tab in the menu bar, and this saves the current world
     * into a file that the user has already loaded. Save is the option for users that have a file already loaded and
     * want to save any edits they do to the world, to said loaded file.
     * @param event The event is clicking on the Save option under the File tab in the menu bar
     */
    public void MenuSave(ActionEvent event) {
        // setting left status to say saving
        LeftStatus.setText("Save menu option selected");
        // check if the file object and world are equal to null, if they aren't we can enter the if block because
        // there is a world to save and a file to save to
        if(fileObject!= null && world != null) {
            //Write world to the file and get the status of the writing back
            String status = Writer.createFile(fileObject, world);
            // check if the status returned is success, if so set right status text to green and print that the
            // save was successful and what the file name was
            if(status.equals("Successfully saved to file")){
                RightStatus.setTextFill(Color.GREEN);
                RightStatus.setText(status = status + "named: " + fileObject.getName());
            // check if the status returned is invalid weapon, if so set right status text to red and print that the
            // monsters weapon type was invalid and what the proper weapon types are
            }else if(status.equals("World contains invalid Monster Weapon Type, Weapon Type must be: (C)lub/(A)xe/(S)word)")){
                RightStatus.setTextFill(Color.RED);
                RightStatus.setText(status);
            // check if the status returned is Error, if so set right status text to red and print that the
            // file couldn't be written to
            }else if(status.equals("Error: File could not be written to"))
                RightStatus.setTextFill(Color.RED);
                RightStatus.setText(status);

        // if the file object is null, but there is a world present, then set the right status text color to orange as
        // a warning and tell the user that there is no file loaded and they should use the save as option instead
        // while also calling the save as option for them to use
        }else if(fileObject == null && world != null) {
            RightStatus.setTextFill(Color.ORANGE);
            RightStatus.setText("Warning: No file has been loaded! Use the Save As option that popped up instead!");
            // triggers save as menu so the user will be able to create their own save file
            MenuSaveAs(event);

        // if the file object is present but no world is created, then set the right status text color to Orange and
        // tell the user the world is empty and nothing was saved
        }else if(fileObject != null && world == null) {
            RightStatus.setTextFill(Color.ORANGE);
            RightStatus.setText("World is empty, nothing was saved!");
        // if the world is empty and the file object is empty, set the right status text color to red and tell the user
        // that they can't save unless a file is loaded, and the world is empty so nothing would be saved anyways
        }else if(fileObject == null && world == null) {
            RightStatus.setTextFill(Color.RED);
            RightStatus.setText("Error: Cannot save until a file has been loaded, and there is no world to save!");
        }
    }


    /**
     * Allows the user to click on the save as button under the File tab in the menu bar, and this saves the current world
     * into a file that the user will have to create. Save as is the saving option in which the user will want to create
     * a new file or over write an already existing file
     * @param event The event is clicking on the Save As option under the File tab in the menu bar
     */
    @FXML
    void MenuSaveAs(ActionEvent event) {
        //status to ensure that there was no issue creating a new file
        boolean ErrorFree = true;

        // create the file chooser
        FileChooser fileChooser = new FileChooser();
        //launch the file chooser and get the file path the user selects
        File LoadedFile = fileChooser.showSaveDialog(new Stage());

        // if the loaded file isn't null (meaning the user didn't choose anything)
        if (LoadedFile != null) {
            // assign create a new file object if the loaded file path isn't null
            File fileObject2 = new File(String.valueOf(LoadedFile));
            // setting left status to say saving
            LeftStatus.setText("Save As menu option selected");
            // check if the file object and world are equal to null, if they aren't we can enter the if block because
            // there is a world to save and a file to save to

            // check if the file the user selected exists and if it doesn't exist enter this if block
            if(!fileObject2.exists()){
                // try to create a new file
                try {
                    fileObject2.createNewFile();
                 // catch any exceptions that can occur
                }catch (Exception e){
                    // If there is an exception display an error message in red
                    RightStatus.setTextFill(Color.RED);
                    RightStatus.setText("Error: Cannot create new file");
                    // making the alert
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    // setting titles
                    alert.setTitle("Save As Error");
                    alert.setHeaderText("Error: File creation");
                    // create message
                    alert.setContentText("File could not be created!");
                    alert.show();
                    // set the error free status to false
                    ErrorFree = false;
                }
            }
            // check if everything so far is error free (this only changes to false when creating a file failed) and
            // check if the world is not equal to null
            if (ErrorFree && world != null) {
                //Write world to the file and get the status of the writing back
                String status = Writer.createFile(fileObject2, world);
                // check if the status returned is success, if so set right status text to green and print that the
                // save was successful and what the file name was
                if (status.equals("Successfully saved to file")) {
                    RightStatus.setTextFill(Color.GREEN);
                    RightStatus.setText(status = status + "named: " + fileObject2.getName());
                    // set the static fileobject to fileobject 2 as this is now the last saved file
                    fileObject = fileObject2;

                    // check if the status returned is invalid weapon, if so set right status text to red and print that the
                    // monsters weapon type was invalid and what the proper weapon types are
                } else if (status.equals("World contains invalid Monster Weapon Type, Weapon Type must be: (C)lub/(A)xe/(S)word)")) {
                    RightStatus.setTextFill(Color.RED);
                    RightStatus.setText(status);

                    // check if the status returned is Error, if so set right status text to red and print that the
                    // file couldn't be written to
                } else if (status.equals("Error: File could not be written to"))
                    RightStatus.setTextFill(Color.RED);
                    RightStatus.setText(status);

            } else if (ErrorFree && world == null) {
                RightStatus.setTextFill(Color.RED);
                RightStatus.setText("World is empty, cannot create a save file until there is a world!");

            } else if (!ErrorFree){
                RightStatus.setTextFill(Color.RED);
                RightStatus.setText("Error cannot create File named: " + fileObject2.getName() + "!");
            }
        }
    }


    // ----------------------------------- Status Bar -------------------------------------------------------------- //

    /**
     * The bottom left label that displays the status for the user's actions
     */
    @FXML
    private Label LeftStatus;


    /**
     * The bottom right label that displays the status for the internal program operation and any errors that may occur
     */
    @FXML
    private Label RightStatus;


// --------------------------------------- Map ------------------------------------------------------------ //

    /**
     * A label in the view section of the GUI that allows for the game map to be displayed
     */
    @FXML
    private Label MapBox;


// ----------------------------------- Create New World  --------------------------------------------------------- //

    /**
     * A create new world button in the GUI that allows the user to create a new world map after entering in valid sizes
     * for the rows and columns
     * @param event The event is clicking on the create new world button in the GUI
     */
    public void CreateNewWorld(ActionEvent event) {
        // set left status to creating new world
        LeftStatus.setText("User Creating World");


        // get the row and column size from the text field and trim any white spaces
        String sRows = WorldRow.getText().trim();
        String sColumns = WorldColumn.getText().trim();

        //create the variables to hold the int version of rows and columns.
        int iRows;
        int iColumns;


        //check point boolean to help determine which integer parse failed.
        boolean checkpoint = false;


        // try to convert the string inputs for row and column into integers
        try {
            // convert the string value for the rows into an integer
            iRows = Integer.parseInt(sRows);

            // set check point to true, because it only gets to this point if there are no exceptions with the row parse
            checkpoint = true;

            // convert the string value for the columns into an integer
            iColumns = Integer.parseInt(sColumns);

            // check if column and rows were less than 1 if so that is invalid and trigger an error
            if(iColumns < 1 && iRows < 1){
                // setting right status color to red and printing an error message
                RightStatus.setTextFill(Color.RED);
                RightStatus.setText("Error: Row and Column size can't be less than 1!");
                // making a popup alert
                Alert alert = new Alert(Alert.AlertType.ERROR);
                // setting titles
                alert.setTitle("World Creation Error");
                alert.setHeaderText("Error: Invalid Row and Column value");
                // create message
                alert.setContentText("The World cannot have a Row and Column size of less than 1! Sizes must be" +
                        " an Integer greater than or equal to 1");
                // show alert message
                alert.show();

             // check if columns were less than 1, again if this is the case it is invalid and trigger an error
            }else if(iColumns < 1){
                // setting right status color to red and printing an error message
                RightStatus.setTextFill(Color.RED);
                RightStatus.setText("Error: Column size can't be less than 1!");
                // making a popup alert
                Alert alert = new Alert(Alert.AlertType.ERROR);
                // setting titles
                alert.setTitle("World Creation Error");
                alert.setHeaderText("Error: Invalid Column value");
                // create message
                alert.setContentText("The World cannot have a Column size of less than 1! Sizes must be an integer" +
                        " greater than or equal to 1");
                // show alert message
                alert.show();

             // check if the rows were less than 1, again if this is the case it is invalid and trigger an error
            }else if(iRows < 1){
                // setting right status color to red and printing an error message
                RightStatus.setTextFill(Color.RED);
                RightStatus.setText("Error: Row size can't be less than 1!");
                // making a popup alert
                Alert alert = new Alert(Alert.AlertType.ERROR);
                // setting titles
                alert.setTitle("World Creation Error");
                alert.setHeaderText("Error: Invalid Row value");
                // create message
                alert.setContentText("The World cannot have a Row size of less than 1! Sizes must be an integer" +
                        " greater than or equal to 1");
                // show alert message
                alert.show();

             // if row and column size is valid proceed to create the world
            }else{
            // create the world
            world = new World(iRows, iColumns);
            // display the world string (game map) to the label or view portion of the GUI using the worldString and
            // toString override in world
            MapBox.setText(world.toString());
            // set the right status color to green and display the world has been drawn
            RightStatus.setTextFill(Color.GREEN);
            RightStatus.setText("New World Drawn!");

            //clear the past symbol hashset
                PastSymbols.clear();
            }
        // if the string entered for row and column size couldn't be parsed into an integer catch the exception
        }catch (Exception e){

            // if the check point is true, we know the row parse is ok, and the column parse is the one that
            // caused the exception so something invalid was typed into the column text field
            if(checkpoint){
                // setting right status color to red and printing an error message
                RightStatus.setTextFill(Color.RED);
                RightStatus.setText("Error: Invalid Column value");
                // making a popup alert
                Alert alert = new Alert(Alert.AlertType.ERROR);
                // setting titles
                alert.setTitle("World Creation Error");
                alert.setHeaderText("Error: Invalid Column value");
                // create message
                alert.setContentText("Invalid entry for column number!\nThe Column number must have an integer value >=1! Please use" +
                        " the actual column number and not the index");
                // show alert message
                alert.show();

            }
            // if check point remained false we know the row parse failed, but we still need to check the column parse
            else {
                // this boolean will track to see if the parsing of the column string was successful, and will be
                // used to modify our error message depending on if the column parse failed or not
                boolean FailedTest = false;

                //check column parse
                try {
                    // convert the string value for the columns into an integer
                    iColumns = Integer.parseInt(sColumns);

                    // catch any potential column parsing exceptions
                }catch (Exception a){
                    // if there was an exception we can set the tracker to true
                    FailedTest = true;
                }

                // if the failed test is true we know both the row and column parse failed so display a message for
                // both
                if(FailedTest){
                    // setting right status color to red and printing an error message
                    RightStatus.setTextFill(Color.RED);
                    RightStatus.setText("Error: Invalid Row and Column value!");
                    // making a popup alert
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    // setting titles
                    alert.setTitle("World Creation Error");
                    alert.setHeaderText("Error: Invalid Row and Column value");
                    // create message
                    alert.setContentText("Invalid entry for Row and Column number!\nThe Row and Column number must have integer values >=1! Please use" +
                            " the actual Row and Column number and not the indices");
                    // show alert message
                    alert.show();

                }
                // if the Failed test was false we know only the row parse failed so only display an error message
                // for that
                else {
                    // setting right status color to red and printing an error message
                    RightStatus.setTextFill(Color.RED);
                    RightStatus.setText("Error: Invalid Row value");
                    // making a popup alert
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    // setting titles
                    alert.setTitle("World Creation Error");
                    alert.setHeaderText("Error: Invalid Row value");
                    // create message
                    alert.setContentText("Invalid entry for Row number!\nThe Row number must have an integer value >=1! Please use" +
                            " the actual Row number and not the index");
                    // show alert message
                    alert.show();
                    }
                }
            }
        }


    /**
     * Text field to take in size of rows for world creation
     */
    @FXML
    private TextField WorldRow;


    /**
     * Text field to take in size of Columns for world creation
     */
    @FXML
    private TextField WorldColumn;


    // ----------------------------------- Delete Entity --------------------------------------------------------- //

    /**
     * A text field for the column of the entity that the user wants to delete (not index value)
     */
    @FXML
    private TextField DeleteColumn;

    /**
     * A text field for the row of the entity that the user wants to delete (not index value)
     */
    @FXML
    private TextField DeleteRow;


    /**
     * A button to delete an entity in the world, allows the user to enter a row and column of the entity they want to
     * delete into the text fields and deletes the corresponding entity.
     * @param event The event when the user clicks the delete entity button
     */
    @FXML
    void DeleteEntity(ActionEvent event) {
        // check if the world exists before trying to delete an entity
        if (world == null) {
            // set left status to deleting an entity
            LeftStatus.setText("User Deleting an Entity");
            // set right status to red and display error message
            RightStatus.setTextFill(Color.RED);
            RightStatus.setText("Error: Can't delete an entity as No world exists!");
            // making a popup alert
            Alert alert = new Alert(Alert.AlertType.ERROR);
            // setting titles
            alert.setTitle("Entity Deletion Error");
            alert.setHeaderText("No world exists");
            // create message
            alert.setContentText("Cannot delete Entity because no world currently exists, please either load a " +
                    "world or create one before trying to delete an entity");
            // show alert message
            alert.show();
        }
        // proceed with trying to delete an entity
        else {
            // set left status to deleting an entity
            LeftStatus.setText("User Deleting an Entity");

            // get the row and column size from the text field and trim any white spaces
            String sRows = DeleteRow.getText().trim();
            String sColumns = DeleteColumn.getText().trim();

            //create the variables to hold the int version of rows and columns.
            int iRows;
            int iColumns;

            //check point boolean to help determine which integer parse failed.
            boolean checkpoint = false;

            // try to convert the string inputs for row and column into integers
            try {
                // convert the string value for the rows into an integer
                iRows = Integer.parseInt(sRows);

                // set check point to true, because it only gets to this point if there are no exceptions with the row parse
                checkpoint = true;

                // convert the string value for the columns into an integer
                iColumns = Integer.parseInt(sColumns);

                // check if column and rows were less than 1 if so that is invalid and trigger an error
                if (iColumns < 1 && iRows < 1) {
                    // setting right status color to red and printing an error message
                    RightStatus.setTextFill(Color.RED);
                    RightStatus.setText("Error: Row and Column number can't be less than 1!");
                    // making a popup alert
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    // setting titles
                    alert.setTitle("Entity Deletion Error");
                    alert.setHeaderText("Error: Invalid Row and Column value");
                    // create message
                    alert.setContentText("The Row and Column of the Entity to be deleted cannot be less than 1. Please use" +
                            " the actual Row and Column number and not the indices");
                    // show alert message
                    alert.show();

                    // check if columns were less than 1, again if this is the case it is invalid and trigger an error
                } else if (iColumns < 1) {
                    // setting right status color to red and printing an error message
                    RightStatus.setTextFill(Color.RED);
                    RightStatus.setText("Error: Column number can't be less than 1!");
                    // making a popup alert
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    // setting titles
                    alert.setTitle("Entity Deletion Error");
                    alert.setHeaderText("Error: Invalid Column value");
                    // create message
                    alert.setContentText("The Column of the Entity to be deleted cannot be less than 1. Please use" +
                            " the actual Column number and not the index");
                    // show alert message
                    alert.show();

                    // check if the rows were less than 1, again if this is the case it is invalid and trigger an error
                } else if (iRows < 1) {
                    // setting right status color to red and printing an error message
                    RightStatus.setTextFill(Color.RED);
                    RightStatus.setText("Error: Row number can't be less than 1!");
                    // making a popup alert
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    // setting titles
                    alert.setTitle("Entity Deletion Error");
                    alert.setHeaderText("Error: Invalid Row value");
                    alert.setContentText("The Row of the Entity to be deleted cannot be less than 1. Please use" +
                            " the actual Row number and not the index");
                    // show alert message
                    alert.show();
                    // check if rows and columns are bigger than the world
                }else if (iRows > world.getRows() && iColumns > world.getColumns()) {
                    // setting right status color to red and printing an error message
                    RightStatus.setTextFill(Color.RED);
                    RightStatus.setText("Error: Row and Column value exceeded world size!");
                    // making a popup alert
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    // setting titles
                    alert.setTitle("Deletion Error");
                    alert.setHeaderText("Error: Invalid Row and Column value");
                    alert.setContentText("The Row and Column value exceeded the world size. Please use the actual Row and Column number and not the index");
                    // show alert message
                    alert.show();

                    // check if the row value exceeded the world size
                } else if (iRows > world.getRows()) {

                    // setting right status color to red and printing an error message
                    RightStatus.setTextFill(Color.RED);
                    RightStatus.setText("Error: Row value exceeded world size!");
                    // making a popup alert
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    // setting titles
                    alert.setTitle("Deletion Error");
                    alert.setHeaderText("Error: Invalid Row value");
                    alert.setContentText("The Row value exceeded the world size. Please use the actual Row number and not the index");
                    // show alert message
                    alert.show();


                } else if (iColumns > world.getColumns()) {
                    // setting right status color to red and printing an error message
                    RightStatus.setTextFill(Color.RED);
                    RightStatus.setText("Error: Column number exceeded world size!");
                    // making a popup alert
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    // setting titles
                    alert.setTitle("Deletion Error");
                    alert.setHeaderText("Error: Invalid Column value");
                    alert.setContentText("The Column value exceeded the world size. Please use the actual Row number and not the index");
                    // show alert message
                    alert.show();
                }

                // if row and column size is valid proceed to delete the entity
                else {

                    // check to make sure an entity is there
                    if (world.isMonster(iRows - 1, iColumns - 1) || world.isHero(iRows - 1, iColumns - 1)) {
                        //remove the entity symbol from the previous symbol hashset so it can be reused, but need to add it to a string to convert char to string (because the hashset is string type)
                        PastSymbols.remove(""  + world.getEntity(iRows-1, iColumns-1).getSymbol());

                        // delete the entity at the user selected location and replace it with null, subtracting 1 to correct
                        // it to index values
                        world.addEntity(iRows - 1, iColumns - 1, null);

                        // display the world string (game map) to the label or view portion of the GUI using the worldString and
                        // toString override in world
                        MapBox.setText(world.toString());
                        // set the right status color to green and display the world has been drawn
                        RightStatus.setTextFill(Color.GREEN);
                        RightStatus.setText("New World Drawn!");

                    }
                    // if no entity was present in the user selected spot print a message telling them that
                    else {
                        // setting right status color to Orange and print a warning
                        RightStatus.setTextFill(Color.ORANGE);
                        RightStatus.setText("No Entity Found");
                        // making a popup alert
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        // setting titles
                        alert.setTitle("No Entity Found Warning");
                        alert.setHeaderText("No Entity Found");
                        // create message
                        alert.setContentText("There was no Entity at the user entered Row and Column");
                        // show alert message
                        alert.show();
                    }
                }
                // if the string entered for row and column size couldn't be parsed into an integer catch the exception
            } catch (Exception e) {

                // if the check point is true, we know the row parse is ok, and the column parse is the one that
                // caused the exception so something invalid was typed into the column text field
                if(checkpoint){
                    // setting right status color to red and printing an error message
                    RightStatus.setTextFill(Color.RED);
                    RightStatus.setText("Error: Invalid Column value");
                    // making a popup alert
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    // setting titles
                    alert.setTitle("Entity Deletion Error");
                    alert.setHeaderText("Error: Invalid Column value");
                    // create message
                    alert.setContentText("Invalid entry for column number!\nThe Column number must have an integer value >=1! Please use" +
                            " the actual column number and not the index");
                    // show alert message
                    alert.show();

                }
                // if check point remained false we know the row parse failed, but we still need to check the column parse
                else {
                    // this boolean will track to see if the parsing of the column string was successful, and will be
                    // used to modify our error message depending on if the column parse failed or not
                    boolean FailedTest = false;

                    //check column parse
                    try {
                        // convert the string value for the columns into an integer
                        iColumns = Integer.parseInt(sColumns);

                     // catch any potential column parsing exceptions
                    }catch (Exception a){
                        // if there was an exception we can set the tracker to true
                        FailedTest = true;
                    }

                    // if the failed test is true we know both the row and column parse failed so display a message for
                    // both
                    if(FailedTest){
                        // setting right status color to red and printing an error message
                        RightStatus.setTextFill(Color.RED);
                        RightStatus.setText("Error: Invalid Row and Column value!");
                        // making a popup alert
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        // setting titles
                        alert.setTitle("Entity Deletion Error");
                        alert.setHeaderText("Error: Invalid Row and Column value");
                        // create message
                        alert.setContentText("Invalid entry for Row and Column number!\nThe Row and Column number must have integer values >=1! Please use" +
                                " the actual Row and Column number and not the indices");
                        // show alert message
                        alert.show();

                    }
                    // if the Failed test was false we know only the row parse failed so only display an error message
                    // for that
                    else {
                        // setting right status color to red and printing an error message
                        RightStatus.setTextFill(Color.RED);
                        RightStatus.setText("Error: Invalid Row value");
                        // making a popup alert
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        // setting titles
                        alert.setTitle("Entity Deletion Error");
                        alert.setHeaderText("Error: Invalid Row value");
                        // create message
                        alert.setContentText("Invalid entry for Row number!\nThe Row number must have an integer value >=1! Please use" +
                                " the actual Row number and not the index");
                        // show alert message
                        alert.show();
                    }
                }
            }
        }
    }


    // ----------------------------------- Entity Detail --------------------------------------------------------- //

    /**
     * Label to print the details of newly added entities or searched entities to. This will be displayed for the user
     * to see on the GUI
     */
    @FXML
    private Label DetailWindow;


    /**
     * Text field to take the user input on what column an entity resides, and we will get this entity for further
     * examination of its details (health, symbol, etc)
     */
    @FXML
    private TextField DetailColumn;


    /**
     * Text field to take the user input on what row an entity resides, and we will get this entity for further
     * examination of its details (health, symbol, etc)
     */
    @FXML
    private TextField DetailRow;


    /**
     * This function will allow the user to enter the row and column of an entity that they want to see more details about
     * into the appropriate text fields. This function will then gather the information on the entity and display it in
     * the window.
     * @param event The event is clicking on the view entity button after entering in the row and column we want to look
     *              at into the text field
     */
    @FXML
    void EntityDetail(ActionEvent event) {

        // check if the world exists before trying to gather more details on an entity
        if (world == null) {
            // set left status to viewing details on an entity
            LeftStatus.setText("User viewing more details about an Entity");
            // set right status to red and display error message
            RightStatus.setTextFill(Color.RED);
            RightStatus.setText("Error: Can't view an entity as No world exists!");
            // making a popup alert
            Alert alert = new Alert(Alert.AlertType.ERROR);
            // setting titles
            alert.setTitle("Entity Detail Error");
            alert.setHeaderText("No world exists");
            // create message
            alert.setContentText("Cannot view more details about an Entity because no world currently exists, please either load a " +
                    "world or create one before trying to view more details on an entity");
            // show alert message
            alert.show();
        }
        // proceed with trying to view more details about an entity
        else {
            // set left status to viewing details on an entity
            LeftStatus.setText("User viewing more details about an Entity");

            // get the row and column size from the text field and trim any white spaces
            String sRows = DetailRow.getText().trim();
            String sColumns = DetailColumn.getText().trim();

            //create the variables to hold the int version of rows and columns.
            int iRows;
            int iColumns;

           //check point boolean to help determine which integer parse failed.
            boolean checkpoint = false;

            // try to convert the string inputs for row and column into integers
            try {
                // convert the string value for the rows into an integer
                iRows = Integer.parseInt(sRows);

                // set check point to true, because it only gets to this point if there are no exceptions with the row parse
                checkpoint = true;

                // convert the string value for the columns into an integer
                iColumns = Integer.parseInt(sColumns);

                // check if column and rows were less than 1 if so that is invalid and trigger an error
                if (iColumns < 1 && iRows < 1) {
                    // setting right status color to red and printing an error message
                    RightStatus.setTextFill(Color.RED);
                    RightStatus.setText("Error: Row and Column number can't be less than 1!");
                    // making a popup alert
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    // setting titles
                    alert.setTitle("Entity Detail Error");
                    alert.setHeaderText("Error: Invalid Row and Column value");
                    // create message
                    alert.setContentText("The Row and Column of the Entity to be examined for more details cannot be less than 1. Please use" +
                            " the actual Row and Column number and not the indices");
                    // show alert message
                    alert.show();

                    // check if columns were less than 1, again if this is the case it is invalid and trigger an error
                } else if (iColumns < 1) {
                    // setting right status color to red and printing an error message
                    RightStatus.setTextFill(Color.RED);
                    RightStatus.setText("Error: Column number can't be less than 1!");
                    // making a popup alert
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    // setting titles
                    alert.setTitle("Entity Detail Error");
                    alert.setHeaderText("Error: Invalid Column value");
                    // create message
                    alert.setContentText("The Column of the Entity to be examined for more detail cannot be less than 1. Please use" +
                            " the actual Column number and not the index");
                    // show alert message
                    alert.show();

                    // check if the rows were less than 1, again if this is the case it is invalid and trigger an error
                } else if (iRows < 1) {
                    // setting right status color to red and printing an error message
                    RightStatus.setTextFill(Color.RED);
                    RightStatus.setText("Error: Row number can't be less than 1!");
                    // making a popup alert
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    // setting titles
                    alert.setTitle("Entity Detail Error");
                    alert.setHeaderText("Error: Invalid Row value");
                    alert.setContentText("The Row of the Entity to be examined for more detail cannot be less than 1. Please use" +
                            " the actual Row number and not the index");
                    // show alert message
                    alert.show();
                }
                // check if the row and column size exceeds the world
                else if (iRows > world.getRows() && iColumns > world.getColumns()) {
                    // setting right status color to red and printing an error message
                    RightStatus.setTextFill(Color.RED);
                    RightStatus.setText("Error: Row and Column value exceeded world size!");
                    // making a popup alert
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    // setting titles
                    alert.setTitle("Entity Detail Error");
                    alert.setHeaderText("Error: Invalid Row and Column value");
                    alert.setContentText("The Row and Column value exceeded the world size. Please use the actual Row and Column number and not the index");
                    // show alert message
                    alert.show();

                    // check if the row value exceeded the world size
                } else if (iRows > world.getRows()) {

                    // setting right status color to red and printing an error message
                    RightStatus.setTextFill(Color.RED);
                    RightStatus.setText("Error: Row value exceeded world size!");
                    // making a popup alert
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    // setting titles
                    alert.setTitle("Entity Detail Error");
                    alert.setHeaderText("Error: Invalid Row value");
                    alert.setContentText("The Row value exceeded the world size. Please use the actual Row number and not the index");
                    // show alert message
                    alert.show();


                } else if (iColumns > world.getColumns()) {
                    // setting right status color to red and printing an error message
                    RightStatus.setTextFill(Color.RED);
                    RightStatus.setText("Error: Column number exceeded world size!");
                    // making a popup alert
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    // setting titles
                    alert.setTitle("Entity Detail Error");
                    alert.setHeaderText("Error: Invalid Column value");
                    alert.setContentText("The Column value exceeded the world size. Please use the actual Row number and not the index");
                    // show alert message
                    alert.show();
                }
                // if row and column size is valid proceed to examine the entity
                else {

                    // first check if there is a monster, and if it is display the monster's details in to the GUI
                    if (world.isMonster(iRows - 1, iColumns - 1)) {
                        // string to store the monster's info
                        String EntityInfo = "Type: MONSTER\n";

                        // get the monster
                        Monster monster = (Monster) world.getEntity(iRows-1, iColumns-1);

                        // add the Monster's symbol
                        EntityInfo = EntityInfo + "Symbol: " + monster.getSymbol() + "\n";

                        // add the monster's health
                        EntityInfo = EntityInfo + "Health: " +monster.getHealth() + "\n";

                        // add the monster's weapon
                        EntityInfo = EntityInfo + "Weapon Type: "+ monster.getWeaponType() + "\n";

                        // add the weapon strength
                        EntityInfo = EntityInfo + "Weapon Strength: " + monster.getWeaponType().getWeaponStrength();

                        // display the monster's details to the detail window in the GUI
                        DetailWindow.setText(EntityInfo);
                        // set the right status color to green and display the details have been loaded
                        RightStatus.setTextFill(Color.GREEN);
                        RightStatus.setText("Monster Detail Loaded!");
                    }
                    else if(world.isHero(iRows - 1, iColumns - 1)){
                        // string to store the Hero's info
                        String EntityInfo = "Type: HERO\n";

                        // get the hero
                        Hero hero = (Hero) world.getEntity(iRows-1, iColumns-1);

                        // add the Hero's symbol
                        EntityInfo = EntityInfo + "Symbol: " + hero.getSymbol() + "\n";

                        // add the Hero's health
                        EntityInfo = EntityInfo + "Health: " + hero.getHealth() + "\n";

                        // add the Hero's attack damage
                        EntityInfo = EntityInfo + "Weapon Strength: "+ hero.weaponStrength() + "\n";

                        // add the Hero's armor strength
                        EntityInfo = EntityInfo + "Armor Strength: " + hero.armorStrength();

                        // display the Heros's details to the detail window in the GUI
                        DetailWindow.setText(EntityInfo);
                        // set the right status color to green and display the details have been loaded
                        RightStatus.setTextFill(Color.GREEN);
                        RightStatus.setText("Hero Detail Loaded!");

                    }
                    // if no entity was present in the user selected spot print a message telling them that
                    else {
                        // setting right status color to Orange and print a warning
                        RightStatus.setTextFill(Color.ORANGE);
                        RightStatus.setText("No Entity Found");
                        // making a popup alert
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        // setting titles
                        alert.setTitle("No Entity Found Warning");
                        alert.setHeaderText("No Entity Found");
                        // create message
                        alert.setContentText("There was no Entity at the user entered Row and Column");
                        // show alert message
                        alert.show();
                    }
                }
                // if the string entered for row and column size couldn't be parsed into an integer catch the exception
            } catch (Exception e) {

                // if the check point is true, we know the row parse is ok, and the column parse is the one that
                // caused the exception so something invalid was typed into the column text field
                if(checkpoint){
                    // setting right status color to red and printing an error message
                    RightStatus.setTextFill(Color.RED);
                    RightStatus.setText("Error: Invalid Column value");
                    // making a popup alert
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    // setting titles
                    alert.setTitle("Entity Detail Error");
                    alert.setHeaderText("Error: Invalid Column value");
                    // create message
                    alert.setContentText("Invalid entry for column number!\nThe Column number must have an integer value >=1! Please use" +
                            " the actual column number and not the index");
                    // show alert message
                    alert.show();

                }
                // if check point remained false we know the row parse failed, but we still need to check the column parse
                else {
                    // this boolean will track to see if the parsing of the column string was successful, and will be
                    // used to modify our error message depending on if the column parse failed or not
                    boolean FailedTest = false;

                    //check column parse
                    try {
                        // convert the string value for the columns into an integer
                        iColumns = Integer.parseInt(sColumns);

                        // catch any potential column parsing exceptions
                    }catch (Exception a){
                        // if there was an exception we can set the tracker to true
                        FailedTest = true;
                    }

                    // if the failed test is true we know both the row and column parse failed so display a message for
                    // both
                    if(FailedTest){
                        // setting right status color to red and printing an error message
                        RightStatus.setTextFill(Color.RED);
                        RightStatus.setText("Error: Invalid Row and Column value!");
                        // making a popup alert
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        // setting titles
                        alert.setTitle("Entity Detail Error");
                        alert.setHeaderText("Error: Invalid Row and Column value");
                        // create message
                        alert.setContentText("Invalid entry for Row and Column number!\nThe Row and Column number must have integer values >=1! Please use" +
                                " the actual Row and Column number and not the indices");
                        // show alert message
                        alert.show();

                    }
                    // if the Failed test was false we know only the row parse failed so only display an error message
                    // for that
                    else {
                        // setting right status color to red and printing an error message
                        RightStatus.setTextFill(Color.RED);
                        RightStatus.setText("Error: Invalid Row value");
                        // making a popup alert
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        // setting titles
                        alert.setTitle("Entity Detail Error");
                        alert.setHeaderText("Error: Invalid Row value");
                        // create message
                        alert.setContentText("Invalid entry for Row number!\nThe Row number must have an integer value >=1! Please use" +
                                " the actual Row number and not the index");
                        // show alert message
                        alert.show();
                    }
                }
            }
        }
    }


    // ----------------------------------- Entity Creation  --------------------------------------------------------- //

    // variable which determines which radio button in the GUI was clicked, true = monster
    // false = hero, null = nothing was clicked yet.

    Boolean MonsterButton = null;


    /**
     * Switches the MonsterButton Boolean to false if the hero radio button is clicked in the GUI
     * @param event the event is clicking on the hero radio button
     */
    @FXML
    void HeroRadioButton(ActionEvent event) {
        MonsterButton = false;

    }


    /**
     * Switches the MonsterButton Boolean to true when the monster radio button is clicked in
     * the GUI
     * @param event the event is clicking on the Monster radio button
     */
    @FXML
    void MonsterRadioButton(ActionEvent event) {
        MonsterButton = true;
    }


    /**
     * A text field that allows the user to enter the column that they want their newly created entity to be placed
     */
    @FXML
    private TextField EntityColumn;


    /**
     * A text field that allows the user to enter the column that they want their newly created entity to be placed
     */
    @FXML
    private TextField EntityRow;


    /**
     * A text field that will allow the user to enter an HP (health) for a monster they want to create
     */
    @FXML
    private TextField MonsterHP;


    /**
     * A text field that allows the user to enter what symbol for the monster they want to create
     */
    @FXML
    private TextField MonsterSYMB;

    /**
     * A combobox menu that allows the user to pick which weapon their monster would possess, from a Sword, Axe, or Club
     */
    @FXML
    private ComboBox MonsterWeaponID;


    /**
     * A text field that will allow the user to enter an HP (health) for a hero they want to create
     */
    @FXML
    private TextField HeroHP;


    /**
     * A text field that allows the user to enter what symbol for the hero they want to create
     */
    @FXML
    private TextField HeroSYMB;


    /**
     * A text field that allows the user to enter how much damage they want a hero (that they want to create) to have
     */
    @FXML
    private TextField HeroDMG;


    /**
     * A text field that allows the user to enter how much defense they want a hero (that they want to create) to have
     */
    @FXML
    private TextField HeroDEF;

    /**
     * This button allows the user to generate an entity (hero or monster depending on their radio button selection) and
     * place it into the world
     * @param event The event is clicking on the create entity button in the GUI
     */
    @FXML
    void CreateEntityButton(ActionEvent event) {
        // boolean to ensure nothing failed before continuing to create the monster or hero
        boolean SuccessStatus = true;

        // check if the world is empty, if it is create an alert
        if (world == null) {
            // set left status to creating a new entity
            LeftStatus.setText("User creating an Entity");
            // set right status to red and display error message
            RightStatus.setTextFill(Color.RED);
            RightStatus.setText("Error: Can't create an entity as No world exists!");
            // making a popup alert
            Alert alert = new Alert(Alert.AlertType.ERROR);
            // setting titles
            alert.setTitle("Entity creation Error");
            alert.setHeaderText("No world exists");
            // create message
            alert.setContentText("Cannot create an Entity because no world currently exists, please either load a " +
                    "world or create one before trying to create an entity");
            // show alert message
            alert.show();

            // check if the radio button for monster or hero was selected, if not (null) trigger an error
        } else if (MonsterButton == null) {
            // set left status to creating a new entity
            LeftStatus.setText("User creating an Entity");
            // set right status to red and display error message
            RightStatus.setTextFill(Color.RED);
            RightStatus.setText("Error: Please choose monster or hero before trying to create an entity");
            // making a popup alert
            Alert alert = new Alert(Alert.AlertType.ERROR);
            // setting titles
            alert.setTitle("Entity creation Error");
            alert.setHeaderText("No Entity selected");
            // create message
            alert.setContentText("Cannot create an Entity until either a monster or hero is selected via the radio buttons");
            // show alert message
            alert.show();

            // Proceed to try and create the entities
        } else {

            //create a hash set containing the letters from A-Z to check if the symbol entry was valid
            HashSet<String>Alphabet = new HashSet<String>();
            Alphabet.add("A"); Alphabet.add("B"); Alphabet.add("C"); Alphabet.add("D"); Alphabet.add("E"); Alphabet.add("F");
            Alphabet.add("G"); Alphabet.add("H"); Alphabet.add("I"); Alphabet.add("J"); Alphabet.add("K"); Alphabet.add("L");
            Alphabet.add("M"); Alphabet.add("N"); Alphabet.add("O"); Alphabet.add("P"); Alphabet.add("Q"); Alphabet.add("R");
            Alphabet.add("S"); Alphabet.add("T"); Alphabet.add("U"); Alphabet.add("V"); Alphabet.add("W"); Alphabet.add("X");
            Alphabet.add("Y"); Alphabet.add("Z");


            // processing the row and column information first

            //get the row and column values
            String sRows = EntityRow.getText().trim();
            String sColumns = EntityColumn.getText().trim();

            // create variables to hold the int versions of rows and columns after checking has been performed
            int iRows = 0;
            int iColumns = 0;

            // a boolean to mark if we passed a certain point in the code, this will be used to see if parsing has
            // successfully passed the row parse.
            boolean checkpointrc = false;

            try {
                // convert the string value for the rows into an integer
                iRows = Integer.parseInt(sRows);

                // set check point to true, because it only gets to this point if there are no exceptions with the row parse
                checkpointrc = true;

                // convert the string value for the columns into an integer
                iColumns = Integer.parseInt(sColumns);

                // check if column and rows were less than 1 if so that is invalid and trigger an error
                if (iColumns < 1 && iRows < 1) {
                    // setting right status color to red and printing an error message
                    RightStatus.setTextFill(Color.RED);
                    RightStatus.setText("Error: Row and Column number can't be less than 1!");
                    // making a popup alert
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    // setting titles
                    alert.setTitle("Entity creation Error");
                    alert.setHeaderText("Error: Invalid Row and Column value");
                    // create message
                    alert.setContentText("The Row and Column value cannot be less than 1. Please use the actual Row and Column number and not the indices");
                    // show alert message
                    alert.show();

                    // change success status to false
                    SuccessStatus = false;

                    // check if columns were less than 1, again if this is the case it is invalid and trigger an error
                } else if (iColumns < 1) {
                    // setting right status color to red and printing an error message
                    RightStatus.setTextFill(Color.RED);
                    RightStatus.setText("Error: Column number can't be less than 1!");
                    // making a popup alert
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    // setting titles
                    alert.setTitle("Entity Creation Error");
                    alert.setHeaderText("Error: Invalid Column value");
                    // create message
                    alert.setContentText("The Column value cannot be less than 1. Please use the actual Column number and not the index");
                    // show alert message
                    alert.show();

                    // change success status to false
                    SuccessStatus = false;

                    // check if the rows were less than 1, again if this is the case it is invalid and trigger an error
                } else if (iRows < 1) {
                    // setting right status color to red and printing an error message
                    RightStatus.setTextFill(Color.RED);
                    RightStatus.setText("Error: Row number can't be less than 1!");
                    // making a popup alert
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    // setting titles
                    alert.setTitle("Entity Creation Error");
                    alert.setHeaderText("Error: Invalid Row value");
                    alert.setContentText("The Row value cannot be less than 1. Please use the actual Row number and not the index");
                    // show alert message
                    alert.show();

                    // change success status to false
                    SuccessStatus = false;

                    // check if the user entered a row and column value that was greater than what the world contained
                } else if (iRows > world.getRows() && iColumns > world.getColumns()) {
                    // setting right status color to red and printing an error message
                    RightStatus.setTextFill(Color.RED);
                    RightStatus.setText("Error: Row and Column value exceeded world size!");
                    // making a popup alert
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    // setting titles
                    alert.setTitle("Entity Creation Error");
                    alert.setHeaderText("Error: Invalid Row and Column value");
                    alert.setContentText("The Row and Column value exceeded the world size. Please use the actual Row and Column number and not the index");
                    // show alert message
                    alert.show();

                    // change success status to false
                    SuccessStatus = false;

                    // check if the row value exceeded the world size
                } else if (iRows > world.getRows()) {

                    // setting right status color to red and printing an error message
                    RightStatus.setTextFill(Color.RED);
                    RightStatus.setText("Error: Row value exceeded world size!");
                    // making a popup alert
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    // setting titles
                    alert.setTitle("Entity Creation Error");
                    alert.setHeaderText("Error: Invalid Row value");
                    alert.setContentText("The Row value exceeded the world size. Please use the actual Row number and not the index");
                    // show alert message
                    alert.show();

                    // change success status to false
                    SuccessStatus = false;


                } else if (iColumns > world.getColumns()) {
                    // setting right status color to red and printing an error message
                    RightStatus.setTextFill(Color.RED);
                    RightStatus.setText("Error: Column number exceeded world size!");
                    // making a popup alert
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    // setting titles
                    alert.setTitle("Entity Creation Error");
                    alert.setHeaderText("Error: Invalid Column value");
                    alert.setContentText("The Column value exceeded the world size. Please use the actual Row number and not the index");
                    // show alert message
                    alert.show();

                    // change success status to false
                    SuccessStatus = false;
                }

                // if the string entered for row and column size couldn't be parsed into an integer catch the exception
            } catch (Exception e) {

                // if the check point is true, we know the row parse is ok, and the column parse is the one that
                // caused the exception so something invalid was typed into the column text field
                if (checkpointrc) {
                    // setting right status color to red and printing an error message
                    RightStatus.setTextFill(Color.RED);
                    RightStatus.setText("Error: Invalid Column value");
                    // making a popup alert
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    // setting titles
                    alert.setTitle("Entity Creation Error");
                    alert.setHeaderText("Error: Invalid Column value");
                    // create message
                    alert.setContentText("Invalid entry for column number!\nThe Column number must have an integer value >=1! Please use" +
                            " the actual column number and not the index");
                    // show alert message
                    alert.show();

                    // change success status to false
                    SuccessStatus = false;

                }
                // if check point remained false we know the row parse failed, but we still need to check the column parse
                else {
                    // this boolean will track to see if the parsing of the column string was successful, and will be
                    // used to modify our error message depending on if the column parse failed or not
                    boolean FailedTest = false;

                    //check column parse
                    try {
                        // convert the string value for the columns into an integer
                        iColumns = Integer.parseInt(sColumns);

                        // catch any potential column parsing exceptions
                    } catch (Exception a) {
                        // if there was an exception we can set the tracker to true
                        FailedTest = true;
                    }

                    // if the failed test is true we know both the row and column parse failed so display a message for
                    // both
                    if (FailedTest) {
                        // setting right status color to red and printing an error message
                        RightStatus.setTextFill(Color.RED);
                        RightStatus.setText("Error: Invalid Row and Column value!");
                        // making a popup alert
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        // setting titles
                        alert.setTitle("Entity Creation Error");
                        alert.setHeaderText("Error: Invalid Row and Column value");
                        // create message
                        alert.setContentText("Invalid entry for Row and Column number!\nThe Row and Column number must have integer values >=1! Please use" +
                                " the actual Row and Column number and not the indices");
                        // show alert message
                        alert.show();

                        // change success status to false
                        SuccessStatus = false;

                    }
                    // if the Failed test was false we know only the row parse failed so only display an error message
                    // for that
                    else {
                        // setting right status color to red and printing an error message
                        RightStatus.setTextFill(Color.RED);
                        RightStatus.setText("Error: Invalid Row value");
                        // making a popup alert
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        // setting titles
                        alert.setTitle("Entity Creation Error");
                        alert.setHeaderText("Error: Invalid Row value");
                        // create message
                        alert.setContentText("Invalid entry for Row number!\nThe Row number must have an integer value >=1! Please use" +
                                " the actual Row number and not the index");
                        // show alert message
                        alert.show();

                        // change success status to false
                        SuccessStatus = false;
                    }
                }
            }
            // if the user clicked on the monster radio button, and nothing failed yet according to the success status,
            // progress to making the monster
            if (MonsterButton && SuccessStatus) {

                // get the monster's symbol and make it uppercase
                String symbol = MonsterSYMB.getText().trim().toUpperCase();

                // check to see if the symbol is a single character
                if(symbol.length() != 1){
                    // create an error alert and status
                    // set right status color to red and display error
                    RightStatus.setTextFill(Color.RED);
                    RightStatus.setText("Error: A monster's symbol must be a unique character from A-Z");
                    // making a popup alert
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    // setting titles
                    alert.setTitle("Monster creation Error");
                    alert.setHeaderText("Invalid Symbol warning");
                    // create message
                    alert.setContentText("Error, the Monster's symbol cannot be longer than 1 character. The symbol must also be " +
                            "a single never used before character from A-Z");
                    // show alert message
                    alert.show();

                    // change success status to false
                    SuccessStatus = false;

                    // check if the symbol is a character from A-Z and if it doesn't display an error
                }else if(!Alphabet.contains(symbol)) {
                    // create an error alert and status
                    // set right status color to red and display error
                    RightStatus.setTextFill(Color.RED);
                    RightStatus.setText("Error: A Monster's symbol must be a unique character from A-Z");
                    // making a popup alert
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    // setting titles
                    alert.setTitle("Monster creation Error");
                    alert.setHeaderText("Invalid Symbol warning");
                    // create message
                    alert.setContentText("Error, the Monster's symbol must be a character from the alphabet A-Z. The symbol " +
                            "must also be a single never used before character");
                    // show alert message
                    alert.show();

                    // change success status to false
                    SuccessStatus = false;

                    // check if the symbol was used before and if was display an error
                }else if(PastSymbols.contains(symbol)) {
                    // create an error alert and status
                    // set right status color to red and display error
                    RightStatus.setTextFill(Color.RED);
                    RightStatus.setText("Error: An Monster's symbol must be a unique character from A-Z");
                    // making a popup alert
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    // setting titles
                    alert.setTitle("Monster creation Error");
                    alert.setHeaderText("Invalid Symbol warning");
                    // create message
                    alert.setContentText("Error, the Monster's symbol cannot be reused. The symbol must also be a single never used before character from A-Z");
                    // show alert message
                    alert.show();

                    // change success status to false
                    SuccessStatus = false;
                }



                // get the monster's health
                String HP = MonsterHP.getText().trim();

                //create a variable to hold the health as an int after parsing is done in the checks
                int iHP = 0;

                // try to parse the health string into an integer
                try{
                    iHP = Integer.parseInt(HP);

                    // if the parsing was successful check if the HP was valid, i.e. >= 0, with 0 being a dead entity, if invalid
                    // trigger an error
                    if(iHP < 0){
                        // set right status color to red and display error

                        RightStatus.setText("Error: A Monster's health cannot be less than 0!");
                        // making a popup alert
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        // setting titles
                        alert.setTitle("Monster creation Error");
                        alert.setHeaderText("Invalid Health warning");
                        // create message
                        alert.setContentText("A Monster cannot have a health less than 0! A health of 0 would create a dead Monster.");
                        // show alert message
                        alert.show();

                        // change success status to false
                        SuccessStatus = false;
                    }
                    // catch exceptions that can occur from parsing a string into integer
                }catch (Exception e){
                    // set right status color to red and display error
                    RightStatus.setTextFill(Color.RED);
                    RightStatus.setText("Error: A Monster's health must be a valid integer >=0");
                    // making a popup alert
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    // setting titles
                    alert.setTitle("Monster creation Error");
                    alert.setHeaderText("Invalid Health warning");
                    // create message
                    alert.setContentText("Error, a Monster's health must be an integer >= 0. With 0 creating a dead Monster");
                    // show alert message
                    alert.show();

                    // change success status to false
                    SuccessStatus = false;
                }
                // boolean to mark if the user has picked the monster's weapon type
                boolean WeaponPickStatus = true;

                //getItems returns a list, getValue gets users choice from the list, index of determines the index of the
                // users choice from the list of weapons (not picking anything is index -1 so no exception can occur)
                int WeaponIndex = MonsterWeaponID.getItems().indexOf(MonsterWeaponID.getValue());

                // if the monster's weapon was not picked the value will be null
                if(MonsterWeaponID.getValue() == null){
                    // change success to false
                    SuccessStatus = false;
                    // make weapon pick status false because no weapon was picked yet
                    WeaponPickStatus = false;
                }

                // check if the weapon has been picked if it hasn't been then trigger an error
                if (!WeaponPickStatus){
                // set right status color to red and display error
                RightStatus.setTextFill(Color.RED);
                RightStatus.setText("Monster Weapon has not been picked!");
                // making a popup alert
                Alert alert = new Alert(Alert.AlertType.ERROR);
                // setting titles
                alert.setTitle("Monster creation Error");
                alert.setHeaderText("Weapon Choice Error");
                // create message
                alert.setContentText("Error, the monster's weapon must be picked before the creation can proceed");
                // show alert message
                alert.show();

                // change success status to false
                SuccessStatus = false;
                }
                // create a variable to hold the monster's weapon
                WeaponType WeaponChoice = null;

                // only proceed if no errors so far
                if(SuccessStatus) {

                    // Weapon index 0 is club as that is the first thing in the list
                    if (WeaponIndex == 0) {
                        WeaponChoice = WeaponType.CLUB;

                        // Weapon index 1 is Axe as that is the second thing in the list
                    } else if (WeaponIndex == 1) {
                        WeaponChoice = WeaponType.AXE;

                        // Weapon index 2 is Sword as that is the third thing in the list
                    } else if (WeaponIndex == 2) {
                        WeaponChoice = WeaponType.SWORD;
                    }
                }
                // only create the monster and add it to the world if nothing has failed so far
                if(SuccessStatus) {
                    // check if an entity already exists at the spot, if so tell the user they must delete it first before adding a new one
                    if (world.getEntity(iRows - 1, iColumns - 1) != null) {
                        // create an error alert and status
                        // set right status color to red and display error
                        RightStatus.setTextFill(Color.RED);
                        RightStatus.setText("Error: An entity already exists at this spot!");
                        // making a popup alert
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        // setting titles
                        alert.setTitle("Monster creation Error");
                        alert.setHeaderText("Occupied space warning");
                        // create message
                        alert.setContentText("Error, an entity already resides in this spot, please delete it before trying to add a new monster to this location in the world!");
                        // show alert message
                        alert.show();
                        // if nothing is in this spot then create the new monster here
                    } else {
                        // create monster
                        Monster monster = new Monster(iHP, symbol.charAt(0), WeaponChoice);
                        // add to world, subtract 1 to get index values
                        world.addEntity(iRows - 1, iColumns - 1, monster);
                        // display details

                        // string to store the monster's info
                        String EntityInfo = "Type: MONSTER\n";

                        // add the Monster's symbol
                        EntityInfo = EntityInfo + "Symbol: " + symbol + "\n";

                        // add the monster's health
                        EntityInfo = EntityInfo + "Health: " + iHP + "\n";

                        // add the monster's weapon
                        EntityInfo = EntityInfo + "Weapon Type: " + WeaponChoice + "\n";

                        // add the weapon strength
                        EntityInfo = EntityInfo + "Weapon Strength: " + WeaponChoice.getWeaponStrength();

                        // display the monster's details to the detail window in the GUI
                        DetailWindow.setText(EntityInfo);
                        // set the right status color to green and display the details have been loaded
                        RightStatus.setTextFill(Color.GREEN);
                        RightStatus.setText("Newly created Monster Detail Loaded!");

                        // update world map
                        MapBox.setText(world.toString());

                        // add symbol to hashmap of past symbols
                        PastSymbols.add(symbol);
                    }
                }

                // if the user clicked on the hero radio button and nothing failed yet (judge by the Success status),
                // progress to creating the hero entity
            } else if (!MonsterButton && SuccessStatus) {

                // get the hero's symbol and make it uppercase
                String symbol = HeroSYMB.getText().trim().toUpperCase();

                // check to see if the symbol is a single character
                if(symbol.length() != 1){
                    // create an error alert and status
                    // set right status color to red and display error
                    RightStatus.setTextFill(Color.RED);
                    RightStatus.setText("Error: A Hero's symbol must be a unique character from A-Z");
                    // making a popup alert
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    // setting titles
                    alert.setTitle("Hero creation Error");
                    alert.setHeaderText("Invalid Symbol warning");
                    // create message
                    alert.setContentText("Error, the Hero's symbol cannot be longer than 1 character. The symbol must also be " +
                            "a single never used before character from A-Z");
                    // show alert message
                    alert.show();

                    // change success status to false
                    SuccessStatus = false;

                    // check if the symbol is a character from A-Z and if it doesn't display an error
                }else if(!Alphabet.contains(symbol)) {
                    // create an error alert and status
                    // set right status color to red and display error
                    RightStatus.setTextFill(Color.RED);
                    RightStatus.setText("Error: A Hero's symbol must be a unique character from A-Z");
                    // making a popup alert
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    // setting titles
                    alert.setTitle("Hero creation Error");
                    alert.setHeaderText("Invalid Symbol warning");
                    // create message
                    alert.setContentText("Error, the Hero's symbol must be a character from the alphabet A-Z. The symbol " +
                            "must also be a single never used before character");
                    // show alert message
                    alert.show();

                    // change success status to false
                    SuccessStatus = false;

                    // check if the symbol was used before and if was display an error
                }else if(PastSymbols.contains(symbol)) {
                    // create an error alert and status
                    // set right status color to red and display error
                    RightStatus.setTextFill(Color.RED);
                    RightStatus.setText("Error: An Hero's symbol must be a unique character from A-Z");
                    // making a popup alert
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    // setting titles
                    alert.setTitle("Hero creation Error");
                    alert.setHeaderText("Invalid Symbol warning");
                    // create message
                    alert.setContentText("Error, the Hero's symbol cannot be reused. The symbol must also be a single never used before character from A-Z");
                    // show alert message
                    alert.show();

                    // change success status to false
                    SuccessStatus = false;
                }


                // get the hero's health
                String HP = HeroHP.getText().trim();

                //create a variable to hold the health as an int after parsing is done in the checks
                int iHP = 0;

                // try to parse the health string into an integer
                try{
                    iHP = Integer.parseInt(HP);

                    // if the parsing was successful check if the HP was valid, i.e. >= 0, with 0 being a dead entity, if invalid
                    // trigger an error
                    if(iHP < 0){
                        // set right status color to red and display error

                        RightStatus.setText("Error: A Hero's health cannot be less than 0!");
                        // making a popup alert
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        // setting titles
                        alert.setTitle("Hero creation Error");
                        alert.setHeaderText("Invalid Health warning");
                        // create message
                        alert.setContentText("A Hero cannot have a health less than 0! A health of 0 would create a dead Hero.");
                        // show alert message
                        alert.show();

                        // change success status to false
                        SuccessStatus = false;
                    }
                    // catch exceptions that can occur from parsing a string into integer
                }catch (Exception e){
                    // set right status color to red and display error
                    RightStatus.setTextFill(Color.RED);
                    RightStatus.setText("Error: A Hero's health must be a valid integer >=0");
                    // making a popup alert
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    // setting titles
                    alert.setTitle("Hero creation Error");
                    alert.setHeaderText("Invalid Health warning");
                    // create message
                    alert.setContentText("Error, a Hero's health must be an integer >= 0. With 0 creating a dead Hero");
                    // show alert message
                    alert.show();

                    // change success status to false
                    SuccessStatus = false;
                }

                // create a variable for the hero's attack (weapon) and defense (armor)
                int iWeapon = 0;
                int iArmor = 0;

                //get the user input for attack (weapon) and defense (armor) and trim any white spaces
                String Weapon = HeroDMG.getText().trim();
                String Armor = HeroDEF.getText().trim();


                //check point boolean to help determine which integer parse failed.
                boolean checkpoint = false;

                // try to convert the string inputs for weapon and armor into integers
                try {
                    // convert the string value for weapon into an integer
                    iWeapon = Integer.parseInt(Weapon);

                    // set check point to true, because it only gets to this point if there are no exceptions with the weapon parse
                    checkpoint = true;

                    // convert the string value for the Armor into an integer
                    iArmor = Integer.parseInt(Armor);

                    // check if weapon and armor were less than 0 if so that is invalid and trigger an error
                    if (iWeapon < 0 && iArmor < 0) {
                        // setting right status color to red and printing an error message
                        RightStatus.setTextFill(Color.RED);
                        RightStatus.setText("Error: Weapon DMG and Armor DEF can't be less than 0!");
                        // making a popup alert
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        // setting titles
                        alert.setTitle("Entity Creation Error");
                        alert.setHeaderText("Error: Invalid weapon attack and armor defense value");
                        // create message
                        alert.setContentText("The Weapon attack damage and Armor defense of the Hero to be created cannot be less than 0");
                        // show alert message
                        alert.show();

                        // change success status to false
                        SuccessStatus = false;

                        // check if weapon was less than 0, again if this is the case it is invalid and trigger an error
                    } else if (iWeapon < 0) {
                        // setting right status color to red and printing an error message
                        RightStatus.setTextFill(Color.RED);
                        RightStatus.setText("Error: Weapon DMG can't be less than 0!");
                        // making a popup alert
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        // setting titles
                        alert.setTitle("Entity Creation Error");
                        alert.setHeaderText("Error: Invalid Weapon attack value");
                        // create message
                        alert.setContentText("The Weapon attack damage cannot be less than 0");
                        // show alert message
                        alert.show();

                        // change success status to false
                        SuccessStatus = false;

                        // check if the armor defense was less than 0, again if this is the case it is invalid and trigger an error
                    } else if (iArmor < 0) {
                        // setting right status color to red and printing an error message
                        RightStatus.setTextFill(Color.RED);
                        RightStatus.setText("Error: armor defense can't be less than 0!");
                        // making a popup alert
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        // setting titles
                        alert.setTitle("Entity Creation Error");
                        alert.setHeaderText("Error: Invalid Armor value");
                        alert.setContentText("The Armor defense of the Hero to be created cannot be less than 0");
                        // show alert message
                        alert.show();
                        // change success status to false
                        SuccessStatus = false;
                    }

                    // if the string entered for weapon and armor couldn't be parsed into an integer catch the exception
                } catch (Exception e) {

                    // if the check point is true, we know the weapon parse is ok, and the armor parse is the one that
                    // caused the exception so something invalid was typed into the armor text field
                    if(checkpoint){
                        // setting right status color to red and printing an error message
                        RightStatus.setTextFill(Color.RED);
                        RightStatus.setText("Error: Invalid armor value");
                        // making a popup alert
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        // setting titles
                        alert.setTitle("Entity Creation Error");
                        alert.setHeaderText("Error: Invalid Armor value");
                        // create message
                        alert.setContentText("Invalid entry for armor defense!\nThe armor defense must be an integer value >=0!");
                        // show alert message
                        alert.show();

                        // change success status to false
                        SuccessStatus = false;

                    }
                    // if check point remained false we know the weapon parse failed, but we still need to check the armor parse
                    else {
                        // this boolean will track to see if the parsing of the armor defense string was successful, and will be
                        // used to modify our error message depending on if the armor parse failed or not
                        boolean FailedTest = false;

                        //check armor parse
                        try {
                            // convert the string value for the armor defense into an integer
                            iArmor = Integer.parseInt(Armor);

                            // catch any potential column parsing exceptions
                        }catch (Exception a){
                            // if there was an exception we can set the tracker to true
                            FailedTest = true;
                        }

                        // if the failed test is true we know both the weapon and armor parse failed so display a message for
                        // both
                        if(FailedTest){
                            // setting right status color to red and printing an error message
                            RightStatus.setTextFill(Color.RED);
                            RightStatus.setText("Error: Invalid Weapon attack and armor defense values!");
                            // making a popup alert
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            // setting titles
                            alert.setTitle("Entity Creation Error");
                            alert.setHeaderText("Error: Invalid Weapon attack and Armor value");
                            // create message
                            alert.setContentText("Invalid entry for Weapon attack and Armor Defense!\nThe Weapon attack and Armor defense be an integer value >=0!");
                            // show alert message
                            alert.show();

                            // change success status to false
                            SuccessStatus = false;

                        }
                        // if the Failed test was false we know only the weapon parse failed so only display an error message
                        // for that
                        else {
                            // setting right status color to red and printing an error message
                            RightStatus.setTextFill(Color.RED);
                            RightStatus.setText("Error: Invalid Weapon attack value");
                            // making a popup alert
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            // setting titles
                            alert.setTitle("Entity Creation Error");
                            alert.setHeaderText("Error: Invalid Weapon attack value");
                            // create message
                            alert.setContentText("Invalid entry for Weapon attack!\nThe Weapon attack must be an integer value >=0!");
                            // show alert message
                            alert.show();

                            // change success status to false
                            SuccessStatus = false;
                        }
                    }
                }

                // only create the hero and add it to the world if nothing has failed so far
                if(SuccessStatus){
                    // check if an entity already exists at the spot, if so tell the user they must delete it first before adding a new one
                    if (world.getEntity(iRows - 1, iColumns - 1) != null) {
                        // create an error alert and status
                        // set right status color to red and display error
                        RightStatus.setTextFill(Color.RED);
                        RightStatus.setText("Error: An entity already exists at this spot!");
                        // making a popup alert
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        // setting titles
                        alert.setTitle("Hero creation Error");
                        alert.setHeaderText("Occupied space warning");
                        // create message
                        alert.setContentText("Error, an entity already resides in this spot, please delete it before trying to add a new Hero to this location in the world!");
                        // show alert message
                        alert.show();
                        // if nothing is there then create the hero
                    }else {
                        // create hero
                        Hero hero = new Hero(iHP, symbol.charAt(0), iWeapon, iArmor);
                        // add to world, subtract 1 to get index values
                        world.addEntity(iRows - 1, iColumns - 1, hero);
                        // display details

                        // string to store the Hero's info
                        String EntityInfo = "Type: HERO\n";

                        // add the Hero's symbol
                        EntityInfo = EntityInfo + "Symbol: " + hero.getSymbol() + "\n";

                        // add the Hero's health
                        EntityInfo = EntityInfo + "Health: " + hero.getHealth() + "\n";

                        // add the Hero's attack damage
                        EntityInfo = EntityInfo + "Weapon Strength: " + hero.weaponStrength() + "\n";

                        // add the Hero's armor strength
                        EntityInfo = EntityInfo + "Armor Strength: " + hero.armorStrength();

                        // display the Heros's details to the detail window in the GUI
                        DetailWindow.setText(EntityInfo);
                        // set the right status color to green and display the details have been loaded
                        RightStatus.setTextFill(Color.GREEN);
                        RightStatus.setText("Hero Detail Loaded!");

                        // update world map
                        MapBox.setText(world.toString());

                        // add symbol to hashmap of past symbols
                        PastSymbols.add(symbol);
                    }                }
            }
        }
    }
}
