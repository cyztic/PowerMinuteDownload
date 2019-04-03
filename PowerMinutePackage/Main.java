//**********************************   CLASS DESCRIPTION  **************************************
//*								        	                                                   *
//*	         This is the main java driver class. It opens up the first fxml window             *
//*          which is the log in window.                                                       *
//*                                                                                            *
//**********************************************************************************************

package PowerMinutePackage;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        //start up the connection to database here:
        //String user = "stretchpp";
        String user = "root";
        //String pass = "stretchpp";
        String pass = "Mancity22!";
        //String url = "jdbc:mysql://lindenwoodcshome.ddns.net/stretchpp?useLegacyDatetimeCode=false&serverTimezone=America/Chicago";
        String url = "jdbc:mysql://localhost/stretchpp";
        // Create the database connection.
        DBConnector db = DBConnector.getInstance();
        db.setDBInfo(url, user, pass);


        /*// Test data to insert into database.
        String email = "newemail2@website.com";
        String originalPass = "password";
        String fname = "Dave";
        String lname = "Coop";
        int wgoal = 5;

        // Hash the test password.
        String hashedPass = BCrypt.hashpw(originalPass, BCrypt.gensalt(12));

        if (db.userSignUp(email, hashedPass, fname, lname, wgoal))
            System.out.println("New user " + fname + " " + lname + " added.");
        else
            System.out.println("Email already in use.");
        System.out.println(db.checkPW(email, originalPass));*/


        Parent root = FXMLLoader.load(getClass().getResource("LogInFXML.fxml"));
        primaryStage.setTitle("Log In");
        //set icon image
        Image iconImage = new Image("resources/test.png");
        primaryStage.getIcons().add(iconImage);
        //set to not resizable
        primaryStage.setResizable(false);
        primaryStage.setScene(new Scene(root, 838, 455));
        primaryStage.show();


    }


    public static void main(String[] args) {
        launch(args);
    }

}
