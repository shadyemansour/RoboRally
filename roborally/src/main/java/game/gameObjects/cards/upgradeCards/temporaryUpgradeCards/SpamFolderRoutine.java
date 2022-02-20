package game.gameObjects.cards.upgradeCards.temporaryUpgradeCards;

import game.gameObjects.cards.Card;
import networking.PlayerThread;
import game.gameObjects.robots.Robot;

/**
 * spamFolderRoutine:
 * this class represents the card spamFolderRoutine in the game
 */
@Deprecated
public class SpamFolderRoutine extends Card implements TemporaryUpgradeCard {
    /**
     * effect():
     * applies the effect of the card
     * @param player: implemented in all cards
     * @param robot:  the robot on which the effect should be applied
     * @param round : current round* @param robot:  the robot on which the effect should be applied

     */
    @Override
    public void effect(PlayerThread player, Robot robot, int round) {

    }
}
