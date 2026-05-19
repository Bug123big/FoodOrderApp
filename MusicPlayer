import javafx.application.Application;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.File;

public class Main extends Application {

    @Override
    public void start(Stage stage) {

        String path = "music/song.mp3";

        Media media = new Media(new File(path).toURI().toString());
        MediaPlayer player = new MediaPlayer(media);

        // loop forever
        player.setCycleCount(MediaPlayer.INDEFINITE);

        // volume (0.0 to 1.0)
        player.setVolume(0.3);

        // autoplay
        player.play();

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
