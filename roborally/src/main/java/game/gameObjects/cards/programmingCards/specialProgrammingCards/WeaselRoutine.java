package game.gameObjects.cards.programmingCards.specialProgrammingCards;

import game.gameObjects.cards.Card;
import networking.PlayerThread;
import game.gameObjects.robots.Robot;
/**
 * weaselRoutine:
 * this class represents the card weaselRoutine in the game
 * when programmed: choose one of the following actions:
 *                  turn left, turn right, U-turn
 */
@Deprecated
public class WeaselRoutine extends Card implements specialProgrammingCard {

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

