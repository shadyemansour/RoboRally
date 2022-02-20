package game.gameObjects.cards.programmingCards.specialProgrammingCards;

import game.gameObjects.cards.Card;
import networking.PlayerThread;
import game.gameObjects.robots.Robot;

/**
 * speedRoutine:
 * this class represents the card speedRoutine in the game
 * when programmed: move your robot 3 spaces in direction it's facing
 */
@Deprecated
public class SpeedRoutine extends Card implements specialProgrammingCard {

    /**
     * effect():
     * applies the effect of the card
     * @param player: implemented in all cards
     * @param robot:  the robot on which the effect should be applied
     * @param round : current round
     */

    @Override
    public void effect(PlayerThread player, Robot robot, int round) {

    }
}
