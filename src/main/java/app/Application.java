package app;

import game_object.item.entity.Factory;
import game_object.item.entity.android.Android;
import UI.GameFrame;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Application {
    private final ScheduledExecutorService exec;
    private final GameFrame gameFrame;

    private final List<Factory> factories;
    private final List<Android> androids;


    public static void main(String[] args) {
        new Application();
    }

    private Application() {
        this.exec = Executors.newScheduledThreadPool(3);

        Data data = Data.getInstance();
        this.factories = data.getFactories();
        this.androids = data.getAndroids();

        new Test();

        this.gameFrame = new GameFrame();
        startGameLoop();
    }

    private void startGameLoop() {
        exec.scheduleAtFixedRate(() -> factories.forEach(Factory::work),
                0, 1, TimeUnit.SECONDS);

        exec.scheduleAtFixedRate(() -> androids.forEach(Android::work),
                0, 1, TimeUnit.SECONDS);

        exec.scheduleAtFixedRate(gameFrame::refresh,
                0, 1, TimeUnit.SECONDS);
    }

}
