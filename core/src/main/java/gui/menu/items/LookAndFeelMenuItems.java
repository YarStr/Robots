package gui.menu.items;

import javax.swing.*;
import java.util.ResourceBundle;

public enum LookAndFeelMenuItems {

    NIMBUS {
        public String getStringName(ResourceBundle bundle) {
            return bundle.getString("schematic.nimbus");
        }

        public String getClassName() {
            return "javax.swing.plaf.nimbus.NimbusLookAndFeel";
        }
    },
    METAL {
        public String getStringName(ResourceBundle bundle) {
            return bundle.getString("schematic.metal");
        }

        public String getClassName() {
            return "javax.swing.plaf.metal.MetalLookAndFeel";
        }
    },
    SYSTEM {
        public String getStringName(ResourceBundle bundle) {
            return bundle.getString("schematic.system");
        }

        public String getClassName() {
            return UIManager.getSystemLookAndFeelClassName();
        }
    },
    CROSS {
        public String getStringName(ResourceBundle bundle) {
            return bundle.getString("schematic.cross");
        }

        public String getClassName() {
            return UIManager.getCrossPlatformLookAndFeelClassName();
        }
    };


    public abstract String getStringName(ResourceBundle bundle);

    public abstract String getClassName();
}
