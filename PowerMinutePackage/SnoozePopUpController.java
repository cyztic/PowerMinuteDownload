//**********************************   CLASS DESCRIPTION  **************************************
//*								        	                                                   *
//*	         This is the java controller for the PopUpFXML file. This class controls           *
//*          the pop up window that appears to give user an option to accept, snooze,          *
//           or decline a workout.                                                             *
//*                                                                                            *
//**********************************************************************************************

package PowerMinutePackage;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;

public class SnoozePopUpController {

    //*********************************GLOBAL VARIABLES******************************************
    //get database connector singleton object for this class
    DBConnector db_connector = DBConnector.getInstance();

    //********************************GLOBAL FXML CONTROLS***************************************
    @FXML
    private Button accept_button;
    @FXML
    private Button decline_button;

    //*********************************CLASS METHODS*********************************************

    //*********************************FXML METHODS******************************************

    // INPUT:   none
    // TASK:    ON ACTION FOR ACCEPT_BUTTON
    //          Loads the power minute fxml and controller
    //          and increments plus one to xp and updates
    //          file.
    // OUTPUT:  none
    @FXML
    void accept() {
        //add one accepted powerminute to database
        db_connector.addUserExercise(1);

        Stage stage = (Stage) accept_button.getScene().getWindow();
        stage.close();
        Stage primaryStage = new Stage();
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("PowerMinuteFXML.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        primaryStage.setTitle("Power Minute");
        javafx.scene.image.Image iconImage = new javafx.scene.image.Image("resources/PNG_Icon.png");
        primaryStage.getIcons().add(iconImage);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setScene(new Scene(root, 906, 520));
        primaryStage.show();
    }

    // INPUT:   none
    // TASK:    ON ACTION FOR DECLINE_BUTTON
    //          declines the workout and closes
    //          the pop up window and adds 1
    //          to declined workouts in database.
    // OUTPUT:  none
    @FXML
    void decline() {
        //add one declined powerminute to database
        db_connector.addUserExercise(0);

        Stage stage = (Stage) decline_button.getScene().getWindow();
        stage.close();
    }
}