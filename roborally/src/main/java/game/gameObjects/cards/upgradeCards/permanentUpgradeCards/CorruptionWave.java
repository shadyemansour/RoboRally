package game.gameObjects.cards.upgradeCards.permanentUpgradeCards;

import game.gameObjects.cards.Card;
import networking.PlayerThread;
import game.gameObjects.robots.Robot;

/**
 * corruptionWave:
 * this class represents the card corruptionWave in the game
 */
@Deprecated
public class CorruptionWave extends Card implements PermanentUpgradeCard {
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