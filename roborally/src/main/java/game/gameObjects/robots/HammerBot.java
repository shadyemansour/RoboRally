package game.gameObjects.robots;

import networking.PlayerThread;

public class HammerBot extends Robot {
    public HammerBot(int id, PlayerThread playerThread) {
        super(id, playerThread);
    }
    @Override
    public String toString() {
        return "HammerBot";
    }
}
