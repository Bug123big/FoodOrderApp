import javafx.application.Platform;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;
import java.util.concurrent.CountDownLatch;

public final class BackgroundMusic {
    private static MediaPlayer player;
    private static boolean javafxStarted;

    private BackgroundMusic() {
    }

    public static void playLoop() {
        startJavaFx();

        URL musicUrl = BackgroundMusic.class.getResource("/music/Music.mp3");
        if (musicUrl == null) {
            throw new IllegalStateException("Could not find /music/Music.mp3");
        }

        Platform.runLater(() -> {
            player = new MediaPlayer(new Media(musicUrl.toExternalForm()));
            player.setCycleCount(MediaPlayer.INDEFINITE);
            player.setVolume(0.1);
            player.play();
        });
    }

    public static void stop() {
        if (!javafxStarted) {
            return;
        }

        Platform.runLater(() -> {
            if (player != null) {
                player.stop();
                player.dispose();
                player = null;
            }
            Platform.exit();
            javafxStarted = false;
        });
    }

    private static void startJavaFx() {
        if (javafxStarted) {
            return;
        }

        CountDownLatch started = new CountDownLatch(1);
        Platform.startup(started::countDown);

        try {
            started.await();
            Platform.setImplicitExit(false);
            javafxStarted = true;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("JavaFX did not start.", e);
        }
    }
}
