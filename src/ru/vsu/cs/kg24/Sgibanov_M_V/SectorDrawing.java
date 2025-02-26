package ru.vsu.cs.kg24.Sgibanov_M_V;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SectorDrawing extends JFrame {
    private JTextField centerXField, centerYField, radiusField, startAngleField, endAngleField;
    private JButton drawButton;
    private SectorPanel sectorPanel;

    public SectorDrawing() {
        setTitle("Sector Drawing");
        setSize(800, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Панель для ввода 
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(6, 2));

        inputPanel.add(new JLabel("Center X:"));
        centerXField = new JTextField("400");
        inputPanel.add(centerXField);

        inputPanel.add(new JLabel("Center Y:"));
        centerYField = new JTextField("400");
        inputPanel.add(centerYField);

        inputPanel.add(new JLabel("Radius:"));
        radiusField = new JTextField("300");
        inputPanel.add(radiusField);

        inputPanel.add(new JLabel("Start Angle:"));
        startAngleField = new JTextField("-150"); // Начальный угол
        inputPanel.add(startAngleField);

        inputPanel.add(new JLabel("End Angle:"));
        endAngleField = new JTextField("150");   // Конечный угол
        inputPanel.add(endAngleField);

        drawButton = new JButton("Draw Sector");
        inputPanel.add(drawButton);

        add(inputPanel, BorderLayout.NORTH);

        // Панель для отрисовки сектора
        sectorPanel = new SectorPanel();
        add(sectorPanel, BorderLayout.CENTER);

        // Обработчик кнопки
        drawButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int centerX = Integer.parseInt(centerXField.getText());
                int centerY = Integer.parseInt(centerYField.getText());
                int radius = Integer.parseInt(radiusField.getText());
                double startAngle = Double.parseDouble(startAngleField.getText());
                double endAngle = Double.parseDouble(endAngleField.getText());

                sectorPanel.setSectorParameters(centerX, centerY, radius, startAngle, endAngle);
                sectorPanel.repaint();
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SectorDrawing().setVisible(true);
            }
        });
    }
}

class SectorPanel extends JPanel {
    private int centerX, centerY, radius;
    private double startAngle, endAngle;
    private Color startColor = Color.RED;
    private Color endColor = Color.BLUE;

    public void setSectorParameters(int centerX, int centerY, int radius, double startAngle, double endAngle) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.radius = radius;
        // Преобразуем градусы в радианы
        this.startAngle = Math.toRadians(startAngle);
        this.endAngle = Math.toRadians(endAngle);
    }

    private boolean isPointInSector(int x, int y) {
        double dx = x - centerX;
        double dy = y - centerY;
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance > radius) return false;

        double angle = Math.atan2(dy, dx);
        if (angle < 0) angle += 2 * Math.PI; // Нормализация угла

        // Обработка перехода через ноль
        if (startAngle < 0) {
            startAngle += 2 * Math.PI;
        }
        if (endAngle < 0) {
            endAngle += 2 * Math.PI;
        }

        if (startAngle > endAngle) {
            return angle >= startAngle || angle <= endAngle;
        } else {
            return angle >= startAngle && angle <= endAngle;
        }
    }

    private Color interpolateColor(double t) {
        int red = (int) (startColor.getRed() + t * (endColor.getRed() - startColor.getRed()));
        int green = (int) (startColor.getGreen() + t * (endColor.getGreen() - startColor.getGreen()));
        int blue = (int) (startColor.getBlue() + t * (endColor.getBlue() - startColor.getBlue()));
        return new Color(red, green, blue);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int x = centerX - radius; x <= centerX + radius; x++) {
            for (int y = centerY - radius; y <= centerY + radius; y++) {
                if (isPointInSector(x, y)) {
                    double distance = Math.sqrt(Math.pow(x - centerX, 2) + Math.pow(y - centerY, 2));
                    double t = distance / radius;
                    g.setColor(interpolateColor(t));
                    g.drawLine(x, y, x, y);
                }
            }
        }
    }
}