package FaceDetection;

import javafx.scene.*;
import javafx.scene.image.*;
import javafx.stage.*;
import org.opencv.core.*;

import java.io.*;

/**
 * Created by Gabriel on 16/12/2020
 */

public class FaceDetectionJavaFX {
    Mat matrix =  null;
    
    
    public void start(Stage stage) throws FileNotFoundException, IOException {
        // Capturing from camera
        FaceDetectionJavaFX obj = new FaceDetectionJavaFX();
        WritableImage writableImage = obj.captureFrame();
        
        // Saving the image
        obj.saveImage;
        
        // Setting the image view
        ImageView imageView = new ImageView(writableImage);
        
        // Setting the height and width of image view
        imageView.setFitHeight(400);
        imageView.setFitWidth(600);
        
        // Setting the preserve ratio of image
        imageView.setPreserveRatio(true);
        
        // Creating a Group object
        Group root = new Group(imageView);
        
        // Creating a scene
        Scene scene = new Scene(root, 600, 400);
        
        // Setting tittle to Stage
        stage.setTitle("Capturing feed from camera");
        
        // Adding scene to Stage
        stage.setScene(scene);
   
        // Displaying contents of stage
        stage.show();
        
    }
    
    public WritableImage captureFrame() {
        WritableImage writableImage = null;
        
        // Loading OpenCV core library
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        
    }
    
    public void saveImage() {
    
    }
}
