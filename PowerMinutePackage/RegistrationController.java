package PowerMinutePackage;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class RegistrationController implements Initializable {
    //*********************************GLOBAL VARIABLES******************************************
    //get database connector singleton object for this class
    DBConnector db_connector = DBConnector.getInstance();
    //private fxml controls

    //********************************GLOBAL FXML CONTROLS******************************************
    @FXML
    private Button register_button;
    @FXML
    private TextField fname_textfield;
    @FXML
    private TextField lname_textfield;
    @FXML
    private TextField email1_textfield;
    @FXML
    private TextField email2_textfield;
    @FXML
    private TextField pw1_textfield;
    @FXML
    private TextField pw2_textfield;


    //*********************************FXML METHODS******************************************
    @FXML
    private void register() {
        //first if statement to check to make sure no textfield is empty
        if (!register_button.getText().isEmpty() && !fname_textfield.getText().isEmpty() &&
                !lname_textfield.getText().isEmpty() && !email1_textfield.getText().isEmpty() &&
                !email2_textfield.getText().isEmpty() && !pw1_textfield.getText().isEmpty() &&
                !pw2_textfield.getText().isEmpty()) {
            //second if statement to check if passwords match
            if (pw1_textfield.getText().equals(pw2_textfield.getText())) {
                //third if statement to check if emails match
                if (email1_textfield.getText().equals(email2_textfield.getText())) {

                    String email = email1_textfield.getText();
                    //test if its school's email
                    if (email.length() > 15 && email.toLowerCase().substring(email.length() - 15).equals("@lindenwood.edu")) {

                        String hashedPass = BCrypt.hashpw(pw1_textfield.getText(), BCrypt.gensalt(12));
                        //fourth if statement to check if user credentials are in database already
                        if (db_connector.userSignUp(email1_textfield.getText(), hashedPass,
                                fname_textfield.getText(), lname_textfield.getText())) {

                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Successful Registration");
                            alert.setHeaderText(null);
                            alert.setContentText("You have successfully registered for Power Minute. Please try to log in");
                            alert.showAndWait();

                            Stage stage = (Stage) register_button.getScene().getWindow();
                            stage.close();
                            //if user credentials are already in db then we do not add
                        } else {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Failed Registration");
                            alert.setHeaderText(null);
                            alert.setContentText("The email you have entered has already been registered with an account.");
                            alert.showAndWait();
                        }
                        //if not a school email
                    } else {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Failed Registration");
                        alert.setHeaderText(null);
                        alert.setContentText("Please use a Lindenwood email");
                        alert.showAndWait();
                    }
                }
                //if emails do not match
                else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Failed Registration");
                    alert.setHeaderText(null);
                    alert.setContentText("Emails do not match. Please try again.");
                    alert.showAndWait();
                }
            }
            //if pw do not match
            else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Failed Registration");
                alert.setHeaderText(null);
                alert.setContentText("Passwords do not match. Please try again.");
                alert.showAndWait();
            }
            //if empty text fields
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Failed Registration");
            alert.setHeaderText(null);
            alert.setContentText("Looks like you left some information blank. Please try again!");
            alert.showAndWait();
        }
    }

    // INPUT:   url and Resource Bundle
    // TASK:    called when class is started, this makes it where we
    //          can call functions and perform actions when the controller
    //          is first called.
    // OUTPUT:  none
    @Override
    public void initialize(URL url, ResourceBundle rb) {


        //allows user to just press enter to log in
        pw2_textfield.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                register();
            }
        });
    }
}
