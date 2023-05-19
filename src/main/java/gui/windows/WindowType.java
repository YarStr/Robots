package gui.windows;

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
    },

    ROBOT_COORDINATE {
        public String getTitleKey() {
            return "robotsCoordinatesWindow.title";
        }
    },
    DISTANCE_TO_TARGET {
        public String getTitleKey() {
            return "distanceToTargetWindow.title";
        }
    };

    public abstract String getTitleKey();
}