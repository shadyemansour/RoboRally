package game.gameObjects.cards.damageCards;

import game.gameLogic.Game;
import game.gameObjects.cards.Card;
import game.gameObjects.robots.Robot;
import networking.PlayerThread;


/**
 * Virus:
 * this class represents the card Virus in the game
 * when programmed: any robot within a six-space radius of you must
 *                  immediately take a virus card from draw pile
 */

public class Virus extends Card implements DamageCards {
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

        //play the top card of the Programming Deck this register
        game.damageEffect(player,robot, "Virus");

        //any robot within a six-space radius must take a virus card
        game.virusRange(robot, player);
        player.getLogger().info("Executed damage card "+ toString() + "'s effect");
    }

    /**
     * toString():
     *
     * @return name of the card as String
     */
    public String toString(){
        return "Virus";
    }
}
