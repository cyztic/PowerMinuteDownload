package PowerMinutePackage;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class VideoViewerController implements Initializable {

    //*********************************GLOBAL VARIABLES******************************************

    private MediaPlayer media_player;
    private Media video_media;
    private MediaView media_view;
    String video_file_name;

    //********************************GLOBAL FXML CONTROLS***************************************
    @FXML
    private Pane holder_pane;

    //*********************************CLASS METHODS*********************************************

    VideoViewerController(String fileName){
        video_file_name = fileName;}

    //*********************************FXML METHODS******************************************

    @FXML
    private void quit(){
        Stage stage = (Stage) holder_pane.getScene().getWindow();
        stage.close();
        media_player.stop();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        String path = video_file_name;

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
