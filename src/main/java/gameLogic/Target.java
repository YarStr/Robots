package gameLogic;

public class Target {
    public volatile int x;
    public volatile int y;

    public Target(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
