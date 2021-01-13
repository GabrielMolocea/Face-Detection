package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.opencv.core.Core;

/**
 * The main class for a JavaFX application. It creates and handle the main
 * window with its resources.
 *
 * This application handles a video stream and try to find any possible human
 * face in a frame. It can use the Haar or the LBP classifier.
 *
 * @author Gabriel Molocea
 *
 */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Overlay.fxml"));
            BorderPane root = (BorderPane) loader.load();
            // set a white-smoke background
            root.setStyle("-fx-background-color: whitesmoke;");
            // Creating the scene and adding the css file to it
            Scene scene = new Scene(root, 800, 600);
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

            primaryStage.setTitle("Face Detection and Tracking");
            primaryStage.setScene(scene);
            // Showing the GUI
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {

        // Loading the OpenCV Library
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        launch(args);
    }
}
