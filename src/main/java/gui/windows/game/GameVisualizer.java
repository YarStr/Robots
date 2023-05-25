package gui.windows.game;

import gui.adapters.KeyPressAdapter;
import logic.game.GameField;
import logic.game.RobotType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.geom.AffineTransform;
import java.util.Timer;
import java.util.TimerTask;


public class GameVisualizer extends JPanel {
    private static Timer initTimer() {
        return new Timer("events generator", true);
    }

    private final GameField gameField;

    private double zoomLevel = 1;


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

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_EQUALS -> increaseZoomLevel();
                    case KeyEvent.VK_MINUS -> decreaseZoomLevel();
                }
            }
        });

        setFocusable(true);
        requestFocus();
        setDoubleBuffered(true);
    }

    private void increaseZoomLevel() {
        if (zoomLevel < 2)
            zoomLevel += 0.2;
    }

    private void decreaseZoomLevel() {
        if (zoomLevel > 1)
            zoomLevel -= 0.2;
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
        int userPositionX;
        int userPositionY;

        if (gameField.getZoomTarget().equals(RobotType.ENEMY)) {
            userPositionX = gameField.getRobotEnemyX();
            userPositionY = gameField.getRobotEnemyY();
        } else {
            userPositionX = gameField.getUserRobotX();
            userPositionY = gameField.getUserRobotY();
        }

        AffineTransform transform = new AffineTransform();

        transform.translate(userPositionX, userPositionY);
        transform.scale(zoomLevel, zoomLevel);
        transform.translate(-userPositionX, -userPositionY);

        graphics.setTransform(transform);
    }

    private void drawRobotEnemy(Graphics2D graphics) {
        int robotCenterX = gameField.getRobotEnemyX();
        int robotCenterY = gameField.getRobotEnemyY();
        int robotWidth = gameField.getRobotEnemyWidth();
        int robotHeight = gameField.getRobotEnemyHeight();
        double direction = gameField.getRobotEnemyDirection();

        Graphics2D enemyRenderGraphics = (Graphics2D) graphics.create();
        enemyRenderGraphics.rotate(direction, robotCenterX, robotCenterY);

        enemyRenderGraphics.setColor(Color.BLUE);
        fillRect(enemyRenderGraphics, robotCenterX, robotCenterY, robotWidth, robotHeight);

        enemyRenderGraphics.setColor(Color.BLACK);
        drawRect(enemyRenderGraphics, robotCenterX, robotCenterY, robotWidth, robotHeight);

        enemyRenderGraphics.dispose();
    }


    private void drawUserRobot(Graphics2D graphics) {
        int robotCenterX = gameField.getUserRobotX();
        int robotCenterY = gameField.getUserRobotY();
        int robotWidth = gameField.getUserRobotWidth();
        int robotHeight = gameField.getUserRobotHeight();

        graphics.setColor(Color.MAGENTA);
        fillRect(graphics, robotCenterX, robotCenterY, robotWidth, robotHeight);

        graphics.setColor(Color.BLACK);
        drawRect(graphics, robotCenterX, robotCenterY, robotWidth, robotHeight);
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

    private static void fillRect(Graphics graphics, int centerX, int centerY, int width, int height) {
        graphics.fillRect(centerX - width / 2, centerY - height / 2, width, height);
    }

    private static void drawRect(Graphics graphics, int centerX, int centerY, int width, int height) {
        graphics.drawRect(centerX - width / 2, centerY - height / 2, width, height);
    }
}
