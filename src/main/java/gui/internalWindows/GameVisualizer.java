package gui.internalWindows;

import gameLogic.GameField;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.util.Timer;
import java.util.TimerTask;

import static java.awt.event.KeyEvent.*;
import static java.awt.event.KeyEvent.VK_W;

public class GameVisualizer extends JPanel {
    private static Timer initTimer() {
        return new Timer("events generator", true);
    }
    private final GameField gameField = new GameField(getWidth(), getHeight());

    public GameVisualizer() {
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
//                gameField.changeTargetPosition(e.getPoint());
//                repaint();
                gameField.startGame();
            }
        });

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                gameField.applyLimits(getWidth(), getHeight());
            }
        });

        addKeyListener(new KeyAdapter(){
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()){
                    case VK_A -> gameField.userRobot.turnLeft();
                    case VK_S -> gameField.userRobot.goBack();
                    case VK_D -> gameField.userRobot.turnRight();
                    case VK_W -> gameField.userRobot.goForward();
                    default -> gameField.userRobot.stop();
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {
                gameField.userRobot.stop();
            }
        });

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
        Graphics2D g2d = (Graphics2D) g;
        drawRobotEnemy(g2d, gameField.getRobotEnemyX(), gameField.getRobotEnemyY(), gameField.getRobotEnemyDirection());
        drawUserRobot(g2d, gameField.getUserRobotX(), gameField.getUserRobotY(), gameField.getUserRobotDirection());
        drawTarget(g2d, gameField.getTargetX(), gameField.getTargetY());
    }

    private static void fillOval(Graphics g, int centerX, int centerY, int diam1, int diam2) {
        g.fillOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    private static void drawOval(Graphics g, int centerX, int centerY, int diam1, int diam2) {
        g.drawOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    private void drawRobotEnemy(Graphics2D g, int robotCenterX, int robotCenterY, double direction) {
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

    private static void fillRect(Graphics g, int centerX, int centerY, int diam1, int diam2) {
        g.fillRect(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    private static void drawRect(Graphics g, int centerX, int centerY, int diam1, int diam2) {
        g.drawRect(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    private void drawUserRobot(Graphics2D g, int robotCenterX, int robotCenterY, double direction) {
        AffineTransform t = AffineTransform.getRotateInstance(direction, robotCenterX, robotCenterY);
        g.setTransform(t);
        g.setColor(Color.MAGENTA);
        fillRect(g, robotCenterX, robotCenterY, 30, 10);
        g.setColor(Color.BLACK);
        drawRect(g, robotCenterX, robotCenterY, 30, 10);
        g.setColor(Color.WHITE);
        fillRect(g, robotCenterX + 10, robotCenterY, 5, 5);
        g.setColor(Color.BLACK);
        drawRect(g, robotCenterX + 10, robotCenterY, 5, 5);
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
