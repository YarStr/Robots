package gui;

import gameLogic.GameField;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

public class GameVisualizer extends JPanel {
    private static Timer initTimer() {
        return new Timer("events generator", true);
    }
    private final GameField gameField = new GameField(getWidth(), getHeight());

    public GameVisualizer(int wight, int height) {
        Timer m_timer = initTimer();

        m_timer.schedule(new TimerTask() {
            @Override
            public void run() {
                onRedrawEvent();
            }
        }, 0, 50);

        m_timer.schedule(new TimerTask() {
            @Override
            public void run() {
                gameField.onModelUpdateEvent();
            }
        }, 0, 10);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                gameField.changeTargetPosition(e.getPoint());
                repaint();
            }
        });

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                gameField.target.correctPosition(GameVisualizer.this.getWidth(), GameVisualizer.this.getHeight());
            }
        });

        setDoubleBuffered(true);
    }
    protected void onRedrawEvent() {
        EventQueue.invokeLater(this::repaint);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        drawRobot(g2d, gameField.robot.getRoundedX(), gameField.robot.getRoundedY(), gameField.robot.direction);
        drawTarget(g2d, gameField.target.x, gameField.target.y);
    }

    private static void fillOval(Graphics g, int centerX, int centerY, int diam1, int diam2) {
        g.fillOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    private static void drawOval(Graphics g, int centerX, int centerY, int diam1, int diam2) {
        g.drawOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    private void drawRobot(Graphics2D g, int robotCenterX, int robotCenterY, double direction) {
        AffineTransform t = AffineTransform.getRotateInstance(direction, robotCenterX, robotCenterY);
        g.setTransform(t);
        g.setColor(Color.MAGENTA);
        fillOval(g, robotCenterX, robotCenterY, 30, 10);
        g.setColor(Color.BLACK);
        drawOval(g, robotCenterX, robotCenterY, 30, 10);
        g.setColor(Color.WHITE);
        fillOval(g, robotCenterX + 10, robotCenterY, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, robotCenterX + 10, robotCenterY, 5, 5);
    }

    private void drawTarget(Graphics2D g, int x, int y) {
        AffineTransform t = AffineTransform.getRotateInstance(0, 0, 0);
        g.setTransform(t);
        g.setColor(Color.GREEN);
        fillOval(g, x, y, 5, 5);
        g.setColor(Color.BLACK);
        drawOval(g, x, y, 5, 5);
    }
}
