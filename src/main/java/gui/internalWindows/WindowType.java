package gui.internalWindows;

public enum WindowType {
    GAME {
        @Override
        public String getTitleKey() {
            return "gameWindow.title";
        }
    },
    LOG {
        public String getTitleKey() {
            return "logWindow.title";
        }
    },

    SCORE_BOARD {
        public String getTitleKey() {
            return "scoreBoardWindow.title";
        }
    };

    public abstract String getTitleKey();
}