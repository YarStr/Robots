package gui.menuItems;

import javax.swing.UIManager;

public enum LookAndFeels {
    NIMBUS {
        public String getStringName() {
            return "Схема Nimbus";
        }

        public String getClassName() {
            return "javax.swing.plaf.nimbus.NimbusLookAndFeel";
        }
    },
    METAL {
        public String getStringName() {
            return "Схема Metal";
        }

        public String getClassName() {
            return "javax.swing.plaf.metal.MetalLookAndFeel";
        }
    },
    SYSTEM {
        public String getStringName() {
            return "Системная схема";
        }

        public String getClassName() {
            return UIManager.getSystemLookAndFeelClassName();
        }
    },
    CROSS {
        public String getStringName() {
            return "Универсальная схема";
        }

        public String getClassName() {
            return UIManager.getCrossPlatformLookAndFeelClassName();
        }
    };

    public abstract String getStringName();

    public abstract String getClassName();
}
