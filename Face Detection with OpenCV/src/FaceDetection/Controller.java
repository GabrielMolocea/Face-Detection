package FaceDetection;

import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import org.opencv.core.*;
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
    private int absoluteFaceSize;
    
    public int index = 0;
    public int ind = 0;
    
    // New user String for training
    public String newName;
    
    // HashMap form training sets
    public HashMap<Integer, String> listOfNames = new HashMap<>();
    
    // Random number for training sets
    private int random = (int) (Math.random() * 50 + 3);
    
    /**
     * Initializing init at the starting time
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
            
            // Disabling settings checkbox
            this.haarClassifier.setDisable(true);
            this.lbpClassifier.setDisable(true);
            
            // Disabling new User checkbox
            this.newUser.setDisable(true);
            
            // Start video capture
            // built-in laptop cam
            this.capture.open(0);
            
            // Checking if video stream is available
            if (this.capture.isOpened()) {
                
                this.cameraActive = true;
                
                // Grabbing a frame every 33 ms (30 frames/sec)
                Runnable frameGrabber = () -> {
                    Image imageToShow = grabFrame();
                    cameraFrame.setImage(imageToShow);
                };
                
                this.timer = Executors.newSingleThreadScheduledExecutor();
                this.timer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);
                
                // updating button content
                this.cameraButton.setText("Stop Camera");
                
            } else {
                System.err.println("Failed to open the camera connection...");
            }
            
            
        } else {
            // The camera is not active at this point
            this.cameraActive = false;
            
            // Updating button content
            this.cameraButton.setText("Start Camera");
            
            // Enabling classifier checkboxes
            this.haarClassifier.setDisable(false);
            this.lbpClassifier.setDisable(false);
            
            // Enabling new User checkbox
            this.newUser.setDisable(false);
            
            // stoping timer
            try {
                
                this.timer.shutdown();
                this.timer.awaitTermination(33, TimeUnit.MILLISECONDS);
                
                
            } catch (InterruptedException e) {
                System.err.println("Exception in stopping the frame capture, trying to release the camera now... " + e);
            }
            
            // Releasing camera
            this.capture.release();
            
            // Cleaning the frame
            this.cameraFrame.setImage(null);
            
            // Cleaning parameters for new User data collection
            index = 0;
            newName = "";
            
        }
    }
    
    /**
     * Getting frames from the opened video stream (if any)
     * @return the {@link Image} to show
     */
    
    private Image grabFrame() {
        
        // Init everything
        Image imageShow = null;
        Mat frame = new Mat();
        
        // Checking if capture is opened
        if (this.capture.isOpened()) {
            try {
                // Reading the current frame
                this.capture.read(frame);
                
                // If the frame in not empty process it
                if (!frame.empty()) {
                    // Face detection
                    this.detectAndDisplay(frame);
                    
                    // Converting Mat Object (OpenCV) to Image (JavaFX)
                    imageShow = mat2Image(frame);
                }
            } catch (Exception e) {
                // logg the full error
                System.err.println("ERROR: " + e);
            }
        }
        return imageShow;
    }
}
