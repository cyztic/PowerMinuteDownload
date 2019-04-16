//**********************************   CLASS DESCRIPTION  **************************************
//*								        	                                                   *
//*	         This is the java controller for the PowerMinuteFXML file. This class              *
//*          controls the actual power minute window which displays the video to               *
//*          the user.                                                                         *
//*                                                                                            *
//**********************************************************************************************

package PowerMinutePackage;

import javafx.beans.Observable;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.media.MediaView;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.File;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class PowerMinuteController implements Initializable {

    //*********************************GLOBAL VARIABLES******************************************

    private Videos video_retriever = Videos.getInstance();

    //********************************GLOBAL FXML CONTROLS***************************************
    @FXML
    private Button exit;

    @FXML
    private WebView web_view;
    //*********************************FXML METHODS******************************************

    @FXML
    private void quit(){
        web_view.getEngine().load(null);
        Stage stage = (Stage) exit.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        web_view.getEngine().load(video_retriever.getRandomVideo());
    }

}
