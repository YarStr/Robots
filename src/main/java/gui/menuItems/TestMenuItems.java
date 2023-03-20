package gui.menuItems;

public enum TestMenuItems {
    NEW_MESSAGE {
        public String getStringName() {
            return "Сообщение в лог";
        }

        public String getCommand() {
            return "Новая строка";
        }
    };

    public abstract String getStringName();

    public abstract String getCommand();
}
