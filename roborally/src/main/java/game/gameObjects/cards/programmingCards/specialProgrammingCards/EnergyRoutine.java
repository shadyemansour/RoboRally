package game.gameObjects.cards.programmingCards.specialProgrammingCards;

import game.gameObjects.cards.Card;
import networking.PlayerThread;
import game.gameObjects.robots.Robot;
/**
 * energyRoutine:
 * this class represents the card energyRoutine in the game
 * when programmed: take one energy cube an place it on your player mat
 */
@Deprecated
public class EnergyRoutine extends Card implements specialProgrammingCard {

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
