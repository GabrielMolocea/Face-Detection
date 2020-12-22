package FaceDetection;

import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import org.opencv.objdetect.*;
import org.opencv.videoio.*;

import java.util.*;
import java.util.concurrent.*;

    /**
     * The controller associated with the only view of our application.
     * The application logic is implemented here. It handles the button for starting/stopping the camera,
     * the acquired video stream, the relative controls and the face detection/tracking.
     */

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
    
    // Face cascade classifier
    private CascadeClassifier faceCascade;
    private  int absoluteFaceSize;
    
    public int index = 0;
    public int ind = 0;
    
    // New user String for training
    public String newName;
    
    // HashMap form training sets
    public HashMap<Integer, String> listOfNames = new HashMap<>();
    
    // Random number for training sets
    private int random = (int) (Math.random() * 50 + 3);
    
    /**
     *  Initializing init at the starting time
     */
    public void init() {
        this.capture = new VideoCapture();
        this.faceCascade = new CascadeClassifier();
        this.absoluteFaceSize = 0;
        
        // Disabling 'New User' functionality
        this.newUserName.setDisable(true);
        this.newUserNameSubmit.setDisable(true);
        // Takes some time thus use only when training set
        // was updated
        // trainModel();
        
    }

    /**
     * The action function when activating a button in GUI
     */
    
    @FXML
    protected void startCamera() {
        
        // Preserve imagine ratio
        cameraFrame.setPreserveRatio(true);
        
        // Checking if camera is active
        if (!this.cameraActive) {
            
        }
    }
    
}
