package gui.windows.game;

import gui.adapters.KeyPressAdapter;
import logic.game.GameController;
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

    private final GameController gameController;

    private double zoomLevel = 1;


    public GameVisualizer(GameController gameController) {
        this.gameController = gameController;

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
                gameController.onModelUpdateEvent();
            }
        }, 0, 10);

        setBackground(new Color(255, 255, 255));

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                gameController.applyLimits(getWidth(), getHeight());
            }
        });

        addKeyListener(new KeyPressAdapter(gameController));

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

        if (gameController.getZoomTarget().equals(RobotType.ENEMY)) {
            userPositionX = gameController.getRobotEnemyX();
            userPositionY = gameController.getRobotEnemyY();
        } else {
            userPositionX = gameController.getUserRobotX();
            userPositionY = gameController.getUserRobotY();
        }

        AffineTransform transform = new AffineTransform();

        transform.translate(userPositionX, userPositionY);
        transform.scale(zoomLevel, zoomLevel);
        transform.translate(-userPositionX, -userPositionY);

        graphics.setTransform(transform);
    }

    private void drawRobotEnemy(Graphics2D graphics) {
        int robotCenterX = gameController.getRobotEnemyX();
        int robotCenterY = gameController.getRobotEnemyY();
        int robotWidth = gameController.getRobotEnemyWidth();
        int robotHeight = gameController.getRobotEnemyHeight();
        double direction = gameController.getRobotEnemyDirection();

        Graphics2D enemyRenderGraphics = (Graphics2D) graphics.create();
//        enemyRenderGraphics.rotate(direction, robotCenterX, robotCenterY);

        enemyRenderGraphics.setColor(Color.BLUE);
        fillRect(enemyRenderGraphics, robotCenterX, robotCenterY, robotWidth, robotHeight);

        enemyRenderGraphics.setColor(Color.BLACK);
        drawRect(enemyRenderGraphics, robotCenterX, robotCenterY, robotWidth, robotHeight);

        enemyRenderGraphics.dispose();
    }


    private void drawUserRobot(Graphics2D graphics) {
        int robotCenterX = gameController.getUserRobotX();
        int robotCenterY = gameController.getUserRobotY();
        int robotWidth = gameController.getUserRobotWidth();
        int robotHeight = gameController.getUserRobotHeight();

        graphics.setColor(Color.MAGENTA);
        fillRect(graphics, robotCenterX, robotCenterY, robotWidth, robotHeight);

        graphics.setColor(Color.BLACK);
        drawRect(graphics, robotCenterX, robotCenterY, robotWidth, robotHeight);
    }

    private void drawTarget(Graphics2D graphics) {
        int x = gameController.getTargetX();
        int y = gameController.getTargetY();

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
