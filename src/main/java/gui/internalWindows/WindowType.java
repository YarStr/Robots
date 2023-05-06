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
    };

    public abstract String getTitleKey();
}