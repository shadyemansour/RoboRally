package game.gameObjects.robots;

import networking.PlayerThread;

public class HulkX90 extends Robot {
    public HulkX90(int id, PlayerThread playerThread) {
        super(id, playerThread);
    }
    @Override
    public String toString() {
        return "HulkX90";
    }
}
