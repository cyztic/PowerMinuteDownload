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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;


public class LogInController implements Initializable {

    //*********************************GLOBAL VARIABLES******************************************

    //get database connector singleton object for this class
    DBConnector db_connector = DBConnector.getInstance();

    private TrayIcon tray_icon;
    java.awt.Image tray_image = null;

    //********************************GLOBAL FXML CONTROLS******************************************

    @FXML
    private Button login_button;
    @FXML
    private javafx.scene.control.TextField email_textfield;
    @FXML
    private javafx.scene.control.TextField password_textfield;

    //*********************************CLASS METHODS******************************************

    //INPUT: the home page stage
    //TASK: creates the tray icon and the fuctionality of it ie: the pop up menu and
    //OUTPUT: none
    public void createTrayIcon(final Stage stage) {
        if (SystemTray.isSupported()) {

            // get the SystemTray instance
            SystemTray tray = SystemTray.getSystemTray();

            //get image from file system
            try {
                InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream("resources/PNG_TrayIcon.png");
                tray_image = ImageIO.read(is);
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            //instead of closing the app, it will call hide method
            stage.setOnCloseRequest(t -> hide(stage));

            ActionListener showListener = new ActionListener() {
                @Override
                public void actionPerformed(java.awt.event.ActionEvent e) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            stage.show();
                        }
                    });
                }
            };

            // create a popup menu
            PopupMenu popupMenu = new PopupMenu();

            //add show button to pop up menu
            MenuItem showItem = new MenuItem("Show");
            showItem.addActionListener(showListener);
            popupMenu.add(showItem);

            //add close button to pop up menu
            MenuItem closeItem = new MenuItem("Close");
            closeItem.addActionListener(e -> System.exit(0));
            popupMenu.add(closeItem);

            // construct a TrayIcon with image uplaoded from the file
            tray_icon = new TrayIcon(tray_image, "Power Minute", popupMenu);

            // add listener for mouse clicks on tray icon
            tray_icon.addActionListener(showListener);

            // add the tray image
            try {
                tray.add(tray_icon);
            } catch (AWTException e) {
                System.err.println(e);
            }
        }
    }

    //INPUT: the home page stage
    //TASK: closes window and puts it in system tray
    //OUTPUT: none
    private void hide(final Stage stage) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (SystemTray.isSupported()) {
                    stage.hide();
                } else {
                    System.exit(0);
                }
            }
        });
    }

    //*********************************FXML METHODS******************************************

    //INPUT:    none
    //TASK:     called when log in button is clicked. Closes login page
    //          and opens homepage fxml.
    //OUTPUT:   none
    @FXML
    void logIn() {
        //if statement to check if text fields are empty
        if (!email_textfield.getText().isEmpty() && !password_textfield.getText().isEmpty()) {
            //check if user input matches credentials of user in database
            if (0 < db_connector.userSignIn(email_textfield.getText(), password_textfield.getText())) {
                //close log in stage
                Stage stage = (Stage) login_button.getScene().getWindow();
                stage.close();

                //create new stage for homepage
                Stage primaryStage = new Stage();
                Parent root = null;

                createTrayIcon(primaryStage);
                Platform.setImplicitExit(false);

                try {
                    root = FXMLLoader.load(getClass().getResource("HomePageFXML.fxml"));
                    //root.getStylesheets().add(getClass().getResource("stylesheet.css").toExternalForm());
                } catch (IOException e) {
                    e.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Failed Login");
                    alert.setHeaderText(null);
                    alert.setContentText(e.toString() + e.getMessage());
                    alert.showAndWait();
                }
                primaryStage.setTitle("Power Minute");
                //set icon image
                javafx.scene.image.Image iconImage = new javafx.scene.image.Image("resources/PNG_Icon.png");
                primaryStage.getIcons().add(iconImage);
                primaryStage.setResizable(false);
                primaryStage.setScene(new Scene(root, 1065, 582));
                primaryStage.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Failed Login");
                alert.setHeaderText(null);
                alert.setContentText("Invalid log in credentials provided. Please try again.");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Failed Login");
            alert.setHeaderText(null);
            alert.setContentText("Looks like you left some information blank. Please try again!");
            alert.showAndWait();
        }


    }

    //INPUT:    none
    //TASK:     called when reset button is clicked.
    //          Resets text fields to null.
    //OUTPUT:   none
    @FXML
    private void resetTextFields() {
        email_textfield.setText("");
        password_textfield.setText("");
    }

    //INPUT:    none
    //TASK:     called when sign up button is clicked. Closes login page
    //          and opens sign up fxml.
    //OUTPUT:   none
    @FXML
    void signUp() {
        Stage primaryStage = new Stage();
        Parent root = null;
        try {
            root = FXMLLoader.load(getClass().getResource("RegistrationFXML.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        primaryStage.setTitle("Sign up");
        //set icon image
        javafx.scene.image.Image iconImage = new javafx.scene.image.Image("resources/PNG_Icon.png");
        primaryStage.getIcons().add(iconImage);
        primaryStage.setScene(new Scene(root, 651, 710));
        primaryStage.show();

    }

    // INPUT:   url and Resource Bundle
    // TASK:    called when class is started, this makes it where we
    //          can call functions and perform actions when the controller
    //          is first called.
    // OUTPUT:  none
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                login_button.requestFocus();
            }
        });

        //allows user to just press enter to log in
        password_textfield.setOnKeyReleased(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                logIn();
            }
        });
    }
}
