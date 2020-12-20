package FaceDetection;

import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.image.*;



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
    
    
}
