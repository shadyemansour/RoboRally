package game.gameObjects.cards.damageCards;

import game.gameLogic.Game;
import game.gameObjects.cards.Card;
import game.gameObjects.otherObjects.DamageCardDecks;
import game.gameObjects.robots.Robot;
import networking.PlayerThread;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Worm:
 * this class represents the card Worm in the game
 * when programmed: reboot robot
 */

public class Worm extends Card implements DamageCards {

    /**
     * effect():
     * applies the effect of the card
     *
     * @param player: PlayerThread of the player who played the card
     * @param robot:  the robot on which the effect should be applied
     * @param round : current round
     * @author Vincent Oeller
     */
    @Override
    public void effect(PlayerThread player, Robot robot, int round) {
        Game game = player.getServer().getGame();

        //reboot
        game.rebootRobot(robot,player);

        player.getLogger().info("Executed damage card "+ toString() + "'s effect");
    }

    /**
     * toString():
     *
     * @return name of the card as String
     */
    public String toString(){
        return "Worm";
    }
}
