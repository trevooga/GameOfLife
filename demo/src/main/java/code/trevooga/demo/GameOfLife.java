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
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.HashSet;
import java.util.Set;

public class GameOfLife extends Application {
    Set<Point> points; // Изменено на Set для уникальности
    private Point cursor;
    private Timeline timeline;
    private final int TILE_SIZE = 10; // размер клетки
    private final int WIDTH = 100; // ширина поля
    private final int HEIGHT = 100; // высота поля
    private final int millis = 34;
    private boolean isStarted = false;

    @Override
    public void start(Stage primaryStage) throws Exception {
        points = new HashSet<>(); // Инициализация Set
        cursor = new Point(WIDTH / 2, HEIGHT / 2);
        Canvas canvas = new Canvas(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE);
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        primaryStage.setTitle("Game Of Life");
        timeline = new Timeline(new KeyFrame(Duration.millis(millis), event -> { //это кадры, время каждого кадра 34мс
            if (isStarted) { //предусматривает изначальный редактор, а так же стоп/паузу игры
                update();
            }
            draw(graphicsContext);
        }));

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        StackPane root = new StackPane(canvas);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();

        scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> { //слушатель инпута
            switch (event.getCode()) {
                case UP -> cursor.setY(Math.max(0, cursor.getY() - 1));
                case DOWN -> cursor.setY(Math.min(HEIGHT - 1, cursor.getY() + 1));
                case LEFT -> cursor.setX(Math.max(0, cursor.getX() - 1));
                case RIGHT -> cursor.setX(Math.min(WIDTH - 1, cursor.getX() + 1));
                case SPACE -> {
                    if (!isStarted) {
                        toggleCell(cursor); //переключает состояние клетки
                    }
                }
                case ESCAPE -> {
                    timeline.stop();
                    primaryStage.close();
                }
                case ENTER -> isStarted = !isStarted; // переключет состояние игры
            }
        });
    }

    private void toggleCell(Point cursor) {
        Point cell = new Point(cursor.getX(), cursor.getY()); // Нужен для проверки,
        // тк курсор всегда мертвая клетка
        if (points.contains(cell)) {
            points.remove(cell); // Удаляем клетку, если она жива
        } else {
            points.add(new Point(cursor.getX(), cursor.getY())); // Добавляем новую живую клетку
        }
    }


    private void update() {
        Set<Point> newPoints = new HashSet<>(); //клетки, которые выживут(перейдут на следующий цикл)
        Set<Point> checkedPoints = new HashSet<>(points); // клетки, которые уже проверены
        for (Point point : points) {
            int aliveNeighbors = countAliveNeighbors(point); //считает количество живых клеток вокруг изначальной клетки
            if (aliveNeighbors == 2 || aliveNeighbors == 3) { //если не проходит условие - умирает
                newPoints.add(point); // Остается живой и переходит на след. цикл
            }
        }

        // Проверяем соседние клетки, которые не были живыми
        for (Point point : points) { //проверка на то, где родится клетка и родится ли вообще
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    if (dx == 0 && dy == 0) continue; // Пропускаем саму клетку
                    Point neighbor = new Point(point.getX() + dx, point.getY() + dy);
                    //фантомная клетка, которая создается для проверки(как возможная живая клетка)
                    if (!checkedPoints.contains(neighbor)) { //если клетки нет в уже проверенных
                        int aliveNeighbors = countAliveNeighbors(neighbor);
                        if (aliveNeighbors == 3) {//и у этой клетки 3 соседа -> клетка рождается
                            newPoints.add(new Point(neighbor.getX(), neighbor.getY()));
                            // добавляем клетку на отображение в следующей итерации
                        }
                    }
                }
            }
        }

        points.clear(); // Очищаем старые клетки
        points.addAll(newPoints); // Добавляем новые
    }

    private void draw(GraphicsContext graphicsContext) {
        graphicsContext.clearRect(0, 0, WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE);//полное очищение окна

        //зарисовка черным живых клеток и белым - неживых(умершие клетки)
        for (Point point : points) {
            graphicsContext.setFill(Color.BLACK);
            graphicsContext.fillRect(point.getX() * TILE_SIZE, point.getY() * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }

        if (!isStarted) {
            // Рисуем курсор
            graphicsContext.setFill(Color.GREEN);
            graphicsContext.fillOval(cursor.getX() * TILE_SIZE, cursor.getY() * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }
        // Отображаем FPS
        graphicsContext.setFont(new Font(24));
        graphicsContext.setFill(Color.BLACK);
        graphicsContext.fillText("FPS: " + 1000 / millis, 40, 50);
    }

    private int countAliveNeighbors(Point point) {
        int count = 0;
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue; // Пропускаем саму клетку
                Point neighbor = new Point(point.getX() + dx, point.getY() + dy);
                if (points.contains(neighbor)) {
                    count++;
                }
            }
        }
        return count;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
