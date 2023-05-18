package gui.windows.game;

import gui.adapters.KeyPressAdapter;
import logic.game.GameField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.AffineTransform;
import java.util.Timer;
import java.util.TimerTask;


public class GameVisualizer extends JPanel {
    private static Timer initTimer() {
        return new Timer("events generator", true);
    }

    private final GameField gameField;

    private double zoomLevel = 2;

    public GameVisualizer(GameField gameField) {
        this.gameField = gameField;

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

        setBackground(new Color(255, 255, 255));

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                gameField.applyLimits(getWidth(), getHeight());
            }
        });

        addKeyListener(new KeyPressAdapter(gameField));

        setFocusable(true);
        requestFocus();
        setDoubleBuffered(true);
    }

    protected void onRedrawEvent() {
        EventQueue.invokeLater(this::repaint);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        Graphics2D graphics2D = (Graphics2D) g;
        applyZoomLevel(graphics2D);

        drawTarget(graphics2D);
        drawUserRobot(graphics2D);
        drawRobotEnemy(graphics2D);
    }

    private void applyZoomLevel(Graphics2D graphics) {
        int userPositionX = gameField.getUserRobotX();
        int userPositionY = gameField.getUserRobotY();

        AffineTransform transform = new AffineTransform();

        transform.translate(userPositionX, userPositionY);
        transform.scale(zoomLevel, zoomLevel);
        transform.translate(-userPositionX, -userPositionY);

        graphics.setTransform(transform);
    }

    private void drawRobotEnemy(Graphics2D graphics) {
        int robotCenterX = gameField.getRobotEnemyX();
        int robotCenterY = gameField.getRobotEnemyY();
        double direction = gameField.getRobotEnemyDirection();

        Graphics2D enemyRenderGraphics = (Graphics2D) graphics.create();
        enemyRenderGraphics.rotate(direction, robotCenterX, robotCenterY);

        enemyRenderGraphics.setColor(Color.MAGENTA);
        fillOval(enemyRenderGraphics, robotCenterX, robotCenterY, 30, 10);

        enemyRenderGraphics.setColor(Color.BLACK);
        drawOval(enemyRenderGraphics, robotCenterX, robotCenterY, 30, 10);

        enemyRenderGraphics.setColor(Color.WHITE);
        fillOval(enemyRenderGraphics, robotCenterX + 10, robotCenterY, 5, 5);

        enemyRenderGraphics.setColor(Color.BLACK);
        drawOval(enemyRenderGraphics, robotCenterX + 10, robotCenterY, 5, 5);

        enemyRenderGraphics.dispose();
    }


    private void drawUserRobot(Graphics2D graphics) {
        int robotCenterX = gameField.getUserRobotX();
        int robotCenterY = gameField.getUserRobotY();

        graphics.setColor(Color.MAGENTA);
        fillRect(graphics, robotCenterX, robotCenterY, 20, 20);

        graphics.setColor(Color.BLACK);
        drawRect(graphics, robotCenterX, robotCenterY, 20, 20);
    }

    private void drawTarget(Graphics2D graphics) {
        int x = gameField.getTargetX();
        int y = gameField.getTargetY();

        graphics.setColor(Color.GREEN);
        fillOval(graphics, x, y, 5, 5);

        graphics.setColor(Color.BLACK);
        drawOval(graphics, x, y, 5, 5);
    }

    private static void fillOval(Graphics graphics, int centerX, int centerY, int diam1, int diam2) {
        graphics.fillOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    private static void drawOval(Graphics graphics, int centerX, int centerY, int diam1, int diam2) {
        graphics.drawOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    private static void fillRect(Graphics g, int centerX, int centerY, int diam1, int diam2) {
        g.fillRect(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    private static void drawRect(Graphics g, int centerX, int centerY, int diam1, int diam2) {
        g.drawRect(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }
}
