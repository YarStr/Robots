package gui.windows.game;

import gui.adapters.ConfirmationWindow;
import logic.DataModel;
import logic.game.GameField;
import logic.game.RobotType;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ResourceBundle;

public class ConfirmGameRestartWindow implements ConfirmationWindow, PropertyChangeListener {

    private int CONFIRM_VALUE = 0;

    private final DataModel dataModel;
    private final GameWindow gameWindow;


    public ConfirmGameRestartWindow(DataModel dataModel, GameWindow gameWindow, GameField gameField) {
        this.dataModel = dataModel;
        this.gameWindow = gameWindow;
        gameField.addGameOverListener(this);
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
        if (evt.getPropertyName().equals(GameField.GAME_OVER)) {
            ResourceBundle bundle = dataModel.getBundle();

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
