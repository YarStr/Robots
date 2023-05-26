package gui.adapters;

import logic.game.GameController;
import logic.game.UserRobotDirection;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyPressAdapter extends KeyAdapter {
    private final GameController gameController;


    public KeyPressAdapter(GameController gameController) {
        this.gameController = gameController;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        changeState(e, true);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        changeState(e, false);
    }

    private void changeState(KeyEvent e, boolean state) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> gameController.updateDirection(UserRobotDirection.MOVE_UP, state);
            case KeyEvent.VK_A -> gameController.updateDirection(UserRobotDirection.MOVE_LEFT, state);
            case KeyEvent.VK_S -> gameController.updateDirection(UserRobotDirection.MOVE_DOWN, state);
            case KeyEvent.VK_D -> gameController.updateDirection(UserRobotDirection.MOVE_RIGHT, state);
        }
    }
}