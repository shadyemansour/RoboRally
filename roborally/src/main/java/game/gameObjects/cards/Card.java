package game.gameObjects.cards;

import networking.*;
import game.gameObjects.robots.Robot;

/**
 * Card:
 * abstract class for all different cards
 */
public abstract class Card {

    public int cost;
    private Server server;
    /**
     * effect():
     * abstract method for all different cards
     * @param player: implemented in all cards except Countess
     * @param robot:  the robot on which the effect should be applied
     * @param round : current round
     */
    public abstract void effect(PlayerThread player, Robot robot, int round);
}
