package game.gameObjects.robots;

import networking.PlayerThread;

public class ZoomBot extends Robot {
    public ZoomBot(int id, PlayerThread playerThread) {
        super(id, playerThread);
    }
    @Override
    public String toString() {
        return "ZoomBot";
    }
}
