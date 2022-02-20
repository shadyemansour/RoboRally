package game.gameObjects.cards.programmingCards.regularProgrammingCards;

import game.gameObjects.cards.Card;
import networking.PlayerThread;
import game.gameObjects.robots.Robot;

/**
 * PowerUp:
 * this class represents the card Powerup in the game
 */
public class PowerUp extends Card implements ProgrammingCard {

    /**
     * effect():
     * applies the effect of the card
     *
     * @param player: PlayerThread of the player who played the card
     * @param robot:  the robot on which the effect should be applied
     * @param round : current round
     * @author Vincent Oeller, Franziska Leitmeir, Vivien Pfeiffer
     */
    @Override
    public void effect(PlayerThread player, Robot robot, int round) {
        robot.incrementEnergyCubes();
        player.energy(player.getUserId(), 1);
        player.getLogger().info(toString() + " effect executed");
    }

    /**
     * toString():
     *
     * @return name of the card as String
     */
    public String toString(){
        return "PowerUp";
    }
}
