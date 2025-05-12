package mvh.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Random;

/**
 * Main for MVH map editor program
 * @author Ryan Loi 
 * @date (dd / mm / yr): 27/03/22
 * @version 1.0
 */

public class Main extends Application {

    public static final String version = "1.0";

    /**
     * A program-wide random number generator
     */
    public static Random random = new Random(12345);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Main.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        //Students edit here to set up the scene

        //setting the title of the program and version
        stage.setTitle("Monsters versus Heroes World Editor v1.0");
        stage.setScene(scene);
        stage.show();
    }
}
