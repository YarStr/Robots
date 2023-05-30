package gui.windows.game;

import gui.adapters.ConfirmationWindow;
import logic.Dispatcher;
import logic.game.GameController;
import ru.robot.interfaces.RobotType;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ResourceBundle;

public class ConfirmGameRestartWindow implements ConfirmationWindow, PropertyChangeListener {

    private final int CONFIRM_VALUE = 0;

    private final Dispatcher dispatcher;
    private final GameWindow gameWindow;


    public ConfirmGameRestartWindow(Dispatcher dispatcher, GameWindow gameWindow, GameController gameController) {
        this.dispatcher = dispatcher;
        this.gameWindow = gameWindow;
        gameController.addGameOverListener(this);
    }
    @Override
    public Object[] getOptions(ResourceBundle bundle) {
        return new Object[]{
                bundle.getString("stateRecoveryOptions.name1"),
                bundle.getString("stateRecoveryOptions.name2")
        };
    }

    @Override
    public String getMessage(ResourceBundle bundle, String additionInfo) {
        return additionInfo + "!\n" + bundle.getString("confirmRestartGame.text") + "?";
    }

    @Override
    public String getTitle(ResourceBundle bundle) {
        return bundle.getString("confirmRestartGame.title");
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals(GameController.GAME_OVER)) {
            ResourceBundle bundle = dispatcher.getBundle();

            RobotType winner = (RobotType) evt.getNewValue();
            String gameOverMessage = switch (winner) {
                case USER -> bundle.getString("confirmRestartGame.winText");
                case ENEMY -> bundle.getString("confirmRestartGame.loseText");
            };

            int option = getOptionForWindow(bundle, gameOverMessage);
            if (option == CONFIRM_VALUE) {
                gameWindow.startGame();
            }
        }
    }
}
