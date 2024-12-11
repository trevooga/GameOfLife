package code.trevooga.demo;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class GameOfLife extends Application {
    List<Point> points;
    private Point cursor;
    private KeyCode direction;
    private Timeline timeline;
    private final int TILE_SIZE = 10; //размер клетки
    private final int WIDTH = 100; //ширина поля
    private final int HEIGHT = 100; //высота поля


    @Override
    public void start(Stage primaryStage) throws Exception {
        points = new ArrayList<>();
        cursor = new Point(WIDTH / 2, HEIGHT / 2);
        Canvas canvas = new Canvas(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE);
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

        timeline = new Timeline(new KeyFrame(Duration.millis(17), event -> {
            //update();
            draw(graphicsContext);
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();


        primaryStage.setScene(graphicsContext.getCanvas().getScene());
        primaryStage.show();

        StackPane root = new StackPane(canvas);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            switch (event.getCode()) {
                case UP -> cursor.setY(cursor.getY() - 1);
                case DOWN -> cursor.setY(cursor.getY() + 1);
                case LEFT -> cursor.setX((cursor.getX() - 1));
                case RIGHT -> cursor.setX(cursor.getX() + 1);
                case SPACE -> points.add(new Point(cursor.getX(), cursor.getY()));
                case BACK_SPACE -> points.remove(new Point(cursor.getX(), cursor.getY()));
                case ESCAPE -> {
                    timeline.stop();
                    primaryStage.close();
                }
            }
        });
    }

    private void update() {
        for (Point point : points) {
            int resultOfScan = scan(point, points);
            if (resultOfScan == 2) {
            }
        }
    }

    private void draw(GraphicsContext graphicsContext) {
        graphicsContext.clearRect(0, 0, WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE);
        graphicsContext.setFill(Color.BLACK);
        if (!(points.isEmpty())) {
            for (Point point : points) {
                graphicsContext.fillRect(point.getX() * TILE_SIZE, point.getY() * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }
        graphicsContext.setFill(Color.GREEN);
        graphicsContext.fillRect(cursor.getX() * TILE_SIZE, cursor.getY() * TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }

    private int scan(Point point, List<Point> points) {
        int countOfPoints = 0;
        for (Point metaPoint : points) {
            if ((new Point(point.getX() + 1, point.getY())).equals(metaPoint) ||
                    (new Point(point.getX() - 1, point.getY())).equals(metaPoint) ||
                    (new Point(point.getX() + 1, point.getY() + 1)).equals(metaPoint) ||
                    (new Point(point.getX() - 1, point.getY() + 1)).equals(metaPoint) ||
                    (new Point(point.getX(), point.getY() + 1)).equals(metaPoint) ||
                    (new Point(point.getX(), point.getY() - 1)).equals(metaPoint) ||
                    (new Point(point.getX() - 1, point.getY() - 1)).equals(metaPoint) ||
                    (new Point(point.getX() + 1, point.getY() - 1)).equals(metaPoint)
            ) {
                countOfPoints++;
            }
        }
        return 0;
    }
}
