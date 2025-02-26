package ru.vsu.cs.kg24.Sgibanov_M_V;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class SectorFilling extends Application {
//алгоритмом брезенхема заполнить контур окружности или тупо пробежаться по всему прямоугольнику и проверять входит ли точка в окружность и дополнительная проверка на нахождение в углу
    private static final int WIDTH = 800;
    private static final int HEIGHT = 800;

    private TextField centerXField;
    private TextField centerYField;
    private TextField radiusField;
    private TextField startAngleField;
    private TextField endAngleField;
    private ColorPicker startColorPicker;
    private ColorPicker endColorPicker;

    @Override
    public void start(Stage primaryStage) {
        GridPane inputPane = new GridPane();
        inputPane.setPadding(new Insets(10));
        inputPane.setHgap(10);
        inputPane.setVgap(10);

        centerXField = new TextField("400");
        centerYField = new TextField("400");
        radiusField = new TextField("300");
        startAngleField = new TextField("0");
        endAngleField = new TextField("360");
        startColorPicker = new ColorPicker(Color.RED);
        endColorPicker = new ColorPicker(Color.BLUE);

        inputPane.add(new Label("Центр X:"), 0, 0);
        inputPane.add(centerXField, 1, 0);
        inputPane.add(new Label("Центр Y:"), 0, 1);
        inputPane.add(centerYField, 1, 1);
        inputPane.add(new Label("Радиус:"), 0, 2);
        inputPane.add(radiusField, 1, 2);
        inputPane.add(new Label("Стартовый угол:"), 0, 3);
        inputPane.add(startAngleField, 1, 3);
        inputPane.add(new Label("Конечный угол:"), 0, 4);
        inputPane.add(endAngleField, 1, 4);
        inputPane.add(new Label("Стартовый цвет:"), 0, 5);
        inputPane.add(startColorPicker, 1, 5);
        inputPane.add(new Label("Конечный цвет:"), 0, 6);
        inputPane.add(endColorPicker, 1, 6);

        Button drawButton = new Button("Нарисовать сектор");
        inputPane.add(drawButton, 0, 7, 2, 1);

        Button clearButton = new Button("Очистить");
        inputPane.add(clearButton, 0, 8, 2, 1);

        Pane root = new Pane();
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        root.getChildren().add(canvas);

        drawButton.setOnAction(e -> {
            int centerX = Integer.parseInt(centerXField.getText());
            int centerY = Integer.parseInt(centerYField.getText());
            int radius = Integer.parseInt(radiusField.getText());
            double startAngle = Double.parseDouble(startAngleField.getText());
            double endAngle = Double.parseDouble(endAngleField.getText());
            Color startColor = startColorPicker.getValue();
            Color endColor = endColorPicker.getValue();

            gc.clearRect(0, 0, WIDTH, HEIGHT);
            drawSector(gc, centerX, centerY, radius, startAngle, endAngle, startColor, endColor);
        });

        clearButton.setOnAction(e -> {
            gc.clearRect(0, 0, WIDTH, HEIGHT);
        });

        root.getChildren().add(inputPane);

        primaryStage.setScene(new Scene(root, WIDTH, HEIGHT));
        primaryStage.setTitle("Sector Filling with Color Interpolation");
        primaryStage.show();
    }

    private void drawSector(GraphicsContext gc, int centerX, int centerY, int radius, double startAngle, double endAngle, Color startColor, Color endColor) {
        // Нормализация углов
        startAngle = normalizeAngle(startAngle);
        endAngle = normalizeAngle(endAngle);

        // Если начальный угол равен конечному, рисуем полный круг
        if (startAngle == endAngle) {
            endAngle += 360;
        }


        radius = Math.min(radius, Math.min(WIDTH - centerX, centerX));
        radius = Math.min(radius, Math.min(HEIGHT - centerY, centerY));

        // Заполняем сектор с интерполяцией цвета
        fillSectorWithInterpolation(gc, centerX, centerY, radius, startAngle, endAngle, startColor, endColor);
    }

    private double normalizeAngle(double angle) {
        // Приводим угол к диапазону от 0 до 360 градусов
        while (angle < 0) {
            angle += 360;
        }
        while (angle >= 360) {
            angle -= 360;
        }
        return angle;
    }

    private void fillSectorWithInterpolation(GraphicsContext gc, int centerX, int centerY, int radius, double startAngle, double endAngle, Color startColor, Color endColor) {
        double startRad = Math.toRadians(startAngle);
        double endRad = Math.toRadians(endAngle);

        int numSteps = 100; // Количество треугольников для заливки сектора
        for (int i = 0; i < numSteps; i++) {
            double angle1 = startRad + (endRad - startRad) * i / numSteps;
            double angle2 = startRad + (endRad - startRad) * (i + 1) / numSteps;

            int x1 = (int) (centerX + radius * Math.cos(angle1));
            int y1 = (int) (centerY + radius * Math.sin(angle1));
            int x2 = (int) (centerX + radius * Math.cos(angle2));
            int y2 = (int) (centerY + radius * Math.sin(angle2));

            // Интерполируем цвет для каждого треугольника
            double t1 = (double) i / numSteps;
            double t2 = (double) (i + 1) / numSteps;
            Color color1 = interpolateColor(startColor, endColor, t1);
            Color color2 = interpolateColor(startColor, endColor, t2);

            // Рисуем треугольник с интерполированным цветом
            drawTriangle(gc, centerX, centerY, x1, y1, x2, y2, color1, color2);
        }
    }

    private void drawTriangle(GraphicsContext gc, int x0, int y0, int x1, int y1, int x2, int y2, Color color1, Color color2) {
        gc.setFill(color1);
        gc.fillPolygon(new double[]{x0, x1, x2}, new double[]{y0, y1, y2}, 3);
    }

    private Color interpolateColor(Color startColor, Color endColor, double t) {
        double r = clamp(startColor.getRed() + t * (endColor.getRed() - startColor.getRed()));
        double g = clamp(startColor.getGreen() + t * (endColor.getGreen() - startColor.getGreen()));
        double b = clamp(startColor.getBlue() + t * (endColor.getBlue() - startColor.getBlue()));
        return new Color(r, g, b, 1.0);
    }

    private double clamp(double value) {
        return Math.max(0.0, Math.min(1.0, value));
    }

    public static void main(String[] args) {
        launch(args);
    }
}