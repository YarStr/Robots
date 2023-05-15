package gui.internalWindows;

import gameLogic.GameField;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;

import static java.awt.event.KeyEvent.*;

class MultiKeyPressAdapter extends KeyAdapter {
    private final Set<Integer> pressedKeys = new HashSet<>();
    private final GameField gameField;

    MultiKeyPressAdapter(GameField gameField) {
        this.gameField = gameField;
    }

    @Override
    public synchronized void keyPressed(KeyEvent e) {
        pressedKeys.add(e.getKeyCode());
        for (int keyCode : pressedKeys) {
            switch (keyCode) {
                case VK_A -> gameField.userRobot.turnLeft();
                case VK_S -> gameField.userRobot.goBack();
                case VK_D -> gameField.userRobot.turnRight();
                case VK_W -> gameField.userRobot.goForward();
            }
        }
    }

    @Override
    public synchronized void keyReleased(KeyEvent e) {
        pressedKeys.remove(e.getKeyCode());
    }
}