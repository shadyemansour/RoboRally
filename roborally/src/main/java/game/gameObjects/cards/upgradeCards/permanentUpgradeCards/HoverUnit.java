package game.gameObjects.cards.upgradeCards.permanentUpgradeCards;

import game.gameObjects.cards.Card;
import networking.PlayerThread;
import game.gameObjects.robots.Robot;

/**
 * hoverUnit:
 * this class represents the card hoverUnit in the game
 */
@Deprecated
public class HoverUnit extends Card implements PermanentUpgradeCard {
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