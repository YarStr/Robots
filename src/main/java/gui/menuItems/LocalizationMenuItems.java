package gui.menuItems;


public enum LocalizationMenuItems {
    RUSSIAN {
        public String getStringName() {
            return "Русский";
        }
    },
    ENGLISH {
        public String getStringName() {
            return "English";
        }
    };

    public abstract String getStringName();

}

