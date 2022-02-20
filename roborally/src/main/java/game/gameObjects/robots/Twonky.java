package game.gameObjects.robots;

import networking.PlayerThread;

public class Twonky extends Robot {
    public Twonky(int id, PlayerThread playerThread) {
        super(id, playerThread);
    }
    @Override
    public String toString() {
        return "Twonky";
    }
}
