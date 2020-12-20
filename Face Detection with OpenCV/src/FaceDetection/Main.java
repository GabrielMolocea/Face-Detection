package FaceDetection;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.opencv.core.*;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        
        try {
            // Loading the FXML resource
            FXMLLoader loader = new FXMLLoader(getClass().getResource("JFXOverlay.fxml"));
            BorderPane root = (BorderPane) loader.load();
            
            // Setting the background to white-smoke
            root.setStyle("-fx-background-color : whitesmoke");
            
            // Creating a scene and styling it
            Scene scene = new Scene(root, 800, 600);
            scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
            
            // Creating the stage with a custom title
            primaryStage.setTitle("Face Detection and Tracking");
            primaryStage.setScene(scene);
            
            // Showing GUI
            primaryStage.show();
            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        // Loading native library for OpenCV
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        
        launch(args);
    }
}
