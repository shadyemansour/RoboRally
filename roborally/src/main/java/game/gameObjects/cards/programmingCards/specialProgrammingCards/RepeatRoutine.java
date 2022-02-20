package game.gameObjects.cards.programmingCards.specialProgrammingCards;

import game.gameObjects.cards.Card;
import networking.PlayerThread;
import game.gameObjects.robots.Robot;

/**
 * repeatRoutine:
 * this class represents the repeatRoutine in the game
 * when programmed: repeat the programming in previous register
 *                  if previous register was damage card, draw card from top of deck and play that card
 *                  if you used a upgrade card in previous register, that allows you to play multiple
 *                      programming cards, re-execute the second card
 */
@Deprecated
public class RepeatRoutine extends Card implements specialProgrammingCard {

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
