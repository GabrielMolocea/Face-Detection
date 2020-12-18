package FaceDetection;

import javafx.scene.*;
import javafx.scene.image.*;
import javafx.stage.*;
import org.opencv.core.*;
import org.opencv.videoio.*;

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
        
        // Initializing VideoCapture class
        VideoCapture videoCapture = new VideoCapture(0);
        
        // Reading next frame from camera
        Mat matrix = new Mat();
        videoCapture.read(matrix);
        
        // Flag to determinate if camera is open
        if (!videoCapture.isOpened()) { // Messages are temporary. Will put dialog box
            System.out.println("Camera not detected");
        } else {
            System.out.println("Camera detected");
        }
        
        // If there is video stream
        if (videoCapture.read(matrix)) {
        
        }
    }
    
    public void saveImage() {
    
    }
}
