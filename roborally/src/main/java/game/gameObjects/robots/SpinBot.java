package game.gameObjects.robots;

import networking.PlayerThread;

public class SpinBot extends Robot {
    public SpinBot(int id, PlayerThread playerThread) {
        super(id, playerThread);
    }
    @Override
    public String toString() {
        return "SpinBot";
    }
}
