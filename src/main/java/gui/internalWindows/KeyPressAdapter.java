package gui.internalWindows;

import gameLogic.GameField;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

class KeyPressAdapter extends KeyAdapter {
    private final GameField gameField;


    KeyPressAdapter(GameField gameField) {
        this.gameField = gameField;
    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        changeState(e, true);
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
        changeState(e, false);
    }

    private void changeState(KeyEvent e, boolean state){
        switch (e.getKeyCode())
        {
            case KeyEvent.VK_W -> gameField.updateDirection(UserRobotDirection.MOVE_UP, state);
            case KeyEvent.VK_A -> gameField.updateDirection(UserRobotDirection.MOVE_LEFT, state);
            case KeyEvent.VK_S -> gameField.updateDirection(UserRobotDirection.MOVE_DOWN, state);
            case KeyEvent.VK_D -> gameField.updateDirection(UserRobotDirection.MOVE_RIGHT, state);
        }
    }
}