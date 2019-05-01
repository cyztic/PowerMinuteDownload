//**********************************   CLASS DESCRIPTION  **************************************
//*								        	                                                   *
//*	         This is the java controller for the PopUpFXML file. This class controls           *
//*          the pop up window that appears to give user an option to accept, snooze,          *
//           or decline a workout.                                                             *
//*                                                                                            *
//**********************************************************************************************

package PowerMinutePackage;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PopUpController implements Initializable {

    //*********************************GLOBAL VARIABLES******************************************
    //get database connector singleton object for this class
    DBConnector db_connector = DBConnector.getInstance();

    //********************************GLOBAL FXML CONTROLS***************************************
    @FXML
    private Button accept_button;
    @FXML
    private Button decline_button;
    @FXML
    private Button snooze_button;

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

    // INPUT:   none
    // TASK:    ON ACTION FOR SNOOZE_BUTTON
    //          Soozes the power minute for
    //          10 min then creates another pop up.
    //          This time only an option to do the
    //          power minute or decline.
    // OUTPUT:  none
    @FXML
    void snooze(){
        //Scheduled executor service to run the pop up every hour
        ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();

        Stage stage = (Stage) snooze_button.getScene().getWindow();
        stage.close();

        ses.schedule(new Runnable() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    FXMLLoader loader = null;
                    Stage myStage = new Stage();
                    Scene myScene;
                    try {
                        loader = new FXMLLoader(getClass().getResource("SnoozePopUpFXML.fxml"));
                        //loader.setController(new Notification());
                        myScene = new Scene(loader.load());
                    } catch (Exception e) {
                        System.out.println("Something went wrong while building new fxml");
                        System.out.println(e);
                        return;
                    }
                    myStage.setScene(myScene);
                    //set icon image
                    Image iconImage = new Image("resources/PNG_Icon.png");
                    myStage.getIcons().add(iconImage);
                    myStage.initStyle(StageStyle.UNDECORATED);
                    myStage.initModality(Modality.APPLICATION_MODAL);
                    myStage.setResizable(false);

                    //get visual bounds of the screen
                    Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();

                    //set Stage boundaries to the lower right corner of the visible bounds of the main screen
                    myStage.setX(primaryScreenBounds.getMinX() + primaryScreenBounds.getWidth() - 550);
                    myStage.setY(primaryScreenBounds.getMinY() + primaryScreenBounds.getHeight() - 131);
                    myStage.setWidth(550);
                    myStage.setHeight(131);
                    myStage.showAndWait();
                });
            }
        }, 600000, TimeUnit.MILLISECONDS);
    }

    // INPUT:   none
    // TASK:    pop up timeouts after 15 min
    //          if it times out it counts as a decline
    // OUTPUT:  none
    void timeout(){
        //Scheduled executor service to run the pop up every hour
        ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();

        ses.schedule(new Runnable() {
            @Override
            public void run() {
                Platform.runLater(() -> {

                    decline();

                });
            }
        }, 900000, TimeUnit.MILLISECONDS);
    }

    // INPUT:   url and Resource Bundle
    // TASK:    called when class is started, this makes it where we
    //          can call functions and perform actions when the controller
    //          is first called.
    // OUTPUT:  none
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        timeout();
    }
}
