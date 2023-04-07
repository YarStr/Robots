package gui.menuItems;


public enum LocalizationMenuItems {
    RUSSIAN {
        public String getStringName() {
            return "Русский";
        }

        public String getResourceName() {
            return "ru";
        }
    },
    ENGLISH {
        public String getStringName() {
            return "English";
        }

        public String getResourceName() {
            return "en";
        }
    };

    public abstract String getStringName();

    public abstract String getResourceName();

}

