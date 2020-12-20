package FaceDetection;

import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import org.opencv.videoio.*;

import java.util.concurrent.*;


public class Controller {
    
    @FXML
    private Button cameraButton;
    
    @FXML
    private ImageView cameraFrame;
    
    // Checking for enabling/disabling classifier
    @FXML
    private CheckBox haarClassifier;
    
    @FXML
    private CheckBox lbpClassifier;
    
    @FXML
    private CheckBox newUser;
    
    @FXML
    private TextField newUserName;
    
    @FXML
    private Button newUserNameSubmit;
    
    // Acquiring the video stream
    private ScheduledExecutorService timer;
    
    // Adding OpenCV object that performs video capture;
    private VideoCapture capture;
    
    // Checking if camera is active or disabled
    private boolean cameraActive;
    
    //
    
}
