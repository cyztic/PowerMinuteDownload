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
import javafx.stage.Stage;

import java.io.File;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class PowerMinuteController implements Initializable {

    //*********************************GLOBAL VARIABLES******************************************

    private MediaPlayer media_player;
    private Media video_media;
    private MediaView media_view;

    //********************************GLOBAL FXML CONTROLS***************************************
    @FXML
    private Pane holder_pane;
    //*********************************FXML METHODS******************************************

    //@FXML
    private void quit(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Power Minute Completed");
        alert.setHeaderText(null);
        alert.setContentText("Great Job!");
        alert.showAndWait();

        Stage stage = (Stage) holder_pane.getScene().getWindow();
        stage.close();
        media_player.stop();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String path = "resources/test.mp4";

        video_media = new Media(new File(path).toURI().toString());

        media_player = new MediaPlayer(video_media);

        media_view = new MediaView(media_player);

        media_view.setFitHeight(600);
        media_view.setFitWidth(880);

        media_player.setAutoPlay(true);
        media_player.setCycleCount(1);
        media_player.setOnEndOfMedia(() -> quit());

        holder_pane.getChildren().addAll(media_view);
    }

}
