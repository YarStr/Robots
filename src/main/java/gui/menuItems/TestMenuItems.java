package gui.menuItems;

import java.util.ResourceBundle;

public enum TestMenuItems {
    NEW_MESSAGE {
        public String getStringName(ResourceBundle bundle) {
            return bundle.getString("testMenuItems.StringName");
        }

        public String getCommand(ResourceBundle bundle) {
            return bundle.getString("testMenuItems.Command");
        }
    };

    public abstract String getStringName(ResourceBundle bundle);

    public abstract String getCommand(ResourceBundle bundle);
}
