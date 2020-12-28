package FaceDetection;


import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.face.Face;
import org.opencv.face.FaceRecognizer;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.objdetect.Objdetect;
import org.opencv.videoio.VideoCapture;


import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    
    private void trainModel() {
        
        File root = new File("resources" + File.separator + "trainingset" +File.separator + "combined" + File.separator);
        
        FilenameFilter imgFilter = (dir, name) -> {
            name = name.toLowerCase();
            return name.endsWith(".png");
        };
        
        File[] imageFiles = root.listFiles(imgFilter);
        
        List<Mat> images = new ArrayList<>();
    
        System.out.println("THE NUMBER OF IMAGES READ IS: " + imageFiles.length);
    
        Mat labels = new Mat(imageFiles.length, 1, CvType.CV_32SC1);
        
        int counter = 0;
        
        for (File image : imageFiles) {
            
            // Parsing training set folder files
            Mat img = Imgcodecs.imread(image.getAbsolutePath());
            
            // Changing the Grayscale end equalize the histogram
            Imgproc.cvtColor(img, img, Imgproc.COLOR_BGR2GRAY);
            Imgproc.equalizeHist(img, img);
            
            // Extracting the label from file name
            int label = Integer.parseInt(image.getName().split("\\-")[0]);
            
            // Extracting name from file name and adding it to names HasMap
            String labName = image.getName().split("\\_")[0];
            String name = labName.split("\\-")[1];
            listOfNames.put(label, name);
            
            // Adding training set images to images Mat
            images.add(img);
            
            labels.put(counter, 0, label);
            counter++;
        }
        FaceRecognizer faceRecognizer = Face.createFisherFaceRecognizer(0,1500);
        faceRecognizer.train(images, labels);
        faceRecognizer.save("traineddata");
        
    }
    
    /**
     * Method for face recognition
     *  grabs the detected face and matches it with
     *  the training set. If recognized the name of
     * 	 the person is printed below the face rectangle
     * 	 @return
     */
    
    public double[] faceRecognition(Mat currentFace) {
        int[] predLabel = new int[1];
        double[] confidence = new double[1];
        int result = -1;
        
        // Create a face recognizer. We need to specify the threshold so that
        // unknown faces would return '-1' label
        // unknown faces would return '-1' label
        FaceRecognizer faceRecognizer = Face.createFisherFaceRecognizer(0,1500);
        faceRecognizer.load("traineddata");
        faceRecognizer.predict(currentFace,predLabel,confidence);
        result = predLabel[0];
    
        return new double[] {result,confidence[0]};
    }
}
