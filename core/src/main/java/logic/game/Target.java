package logic.game;

public class Target {
    public volatile int x;
    public volatile int y;

    public Target(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void correctPosition(int width, int height) {
        if (x > width) x = width;
        if (y > height) y = height;
    }
}
