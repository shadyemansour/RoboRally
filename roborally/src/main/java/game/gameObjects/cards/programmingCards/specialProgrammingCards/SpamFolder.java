package game.gameObjects.cards.programmingCards.specialProgrammingCards;

import game.gameObjects.cards.Card;
import networking.PlayerThread;
import game.gameObjects.robots.Robot;

/**
 * spamFolder:
 * this class represents the card spamFolder in the game
 * when programmed: permanently discard one SPAM damage card from your discard pile
 *                  to SPAM damage draw pile
 */

@Deprecated
public class SpamFolder extends Card implements specialProgrammingCard {

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
