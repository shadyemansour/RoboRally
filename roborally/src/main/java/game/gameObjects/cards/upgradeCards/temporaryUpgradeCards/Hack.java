package game.gameObjects.cards.upgradeCards.temporaryUpgradeCards;

import game.gameObjects.cards.Card;
import networking.PlayerThread;
import game.gameObjects.robots.Robot;

/**
 * hack:
 * this class represents the card hack in the game
 */
@Deprecated
public class Hack extends Card implements TemporaryUpgradeCard {
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
