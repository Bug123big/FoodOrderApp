import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.net.URL;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        URL musicUrl = Main.class.getResource("/Music.mp3");

        if (musicUrl == null) {
            throw new IllegalStateException("Music.mp3 was not found in the program resources.");
        }

        Media media = new Media(musicUrl.toExternalForm());
        MediaPlayer player = new MediaPlayer(media);

        player.setCycleCount(MediaPlayer.INDEFINITE);
        player.setVolume(0.3);
        player.play();

        stage.setTitle("Music Player");
        stage.setScene(new Scene(new Label("Playing Music.mp3"), 260, 100));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
