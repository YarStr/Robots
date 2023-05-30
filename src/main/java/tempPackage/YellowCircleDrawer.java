package tempPackage;

import java.awt.*;

public class YellowCircleDrawer implements Drawer {

    @Override
    public void draw(Graphics2D graphics2D, int x, int y, int width, int height) {
        graphics2D.setColor(Color.YELLOW);
        graphics2D.fillOval(x, y, width, height);

        graphics2D.setColor(Color.BLACK);
        graphics2D.drawOval(x, y, width, height);
    }
}
