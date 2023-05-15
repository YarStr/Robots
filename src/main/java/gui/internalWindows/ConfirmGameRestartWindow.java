package gui.internalWindows;

import gameLogic.GameField;
import gameLogic.RobotType;
import gui.DataModel;
import gui.windowAdapters.ConfirmWindow;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ResourceBundle;

public class ConfirmGameRestartWindow implements ConfirmWindow, PropertyChangeListener {

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
    public String getMessage(ResourceBundle bundle) {
        return "! " + bundle.getString("confirmRestartGame.text");
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
            String message = switch (winner) {
                case USER -> bundle.getString("confirmRestartGame.winText");
                case ENEMY -> bundle.getString("confirmRestartGame.loseText");
            };

            int option = getOptionForWindowAndBundle(message, bundle);
            if (option == CONFIRM_VALUE) {
                gameWindow.startGame();
            }
        }
    }
}
