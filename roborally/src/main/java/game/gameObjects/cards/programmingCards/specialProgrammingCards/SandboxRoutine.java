package game.gameObjects.cards.programmingCards.specialProgrammingCards;

import game.gameObjects.cards.Card;
import networking.PlayerThread;
import game.gameObjects.robots.Robot;

/**
 * sandboxRoutine:
 * this class represents the card sandboxRoutine in the game
 * when programmed: choose on of the following actions:
 *                  move 1, 2 or 3, back uo, turn left, turn right, U-turn
 */

@Deprecated
public class SandboxRoutine extends Card implements specialProgrammingCard {

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
