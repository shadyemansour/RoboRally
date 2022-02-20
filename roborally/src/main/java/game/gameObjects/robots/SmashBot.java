package game.gameObjects.robots;

import networking.PlayerThread;

public class SmashBot extends Robot {
    public SmashBot(int id, PlayerThread playerThread) {
        super(id, playerThread);
    }
    @Override
    public String toString() {
        return "SmashBot";
    }
}
