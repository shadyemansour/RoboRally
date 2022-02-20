package game.gameObjects.cards.damageCards;

import game.gameLogic.Game;
import game.gameObjects.cards.Card;
import game.gameObjects.robots.Robot;
import networking.PlayerThread;

/**
 * Spam:
 * this class represents the card SPAM in the game:
 * taken, when Robot shot by a board or robot laser
 * when programmed: play the top card of your programming deck this register
 *
 */
public class Spam extends Card implements DamageCards {
    /**
     * effect():
     * applies the effect of the card
     *
     * @param player: PlayerThread of the player who played the card
     * @param robot: the robot on which the effect should be applied
     * @param round: current round
     * @author Vincent Oeller, Vivien Pfeiffer, Franziska Leitmeir
     */
    @Override
    public void effect(PlayerThread player, Robot robot, int round) {
        Game game = player.getServer().getGame();

        //play the top card of the Programming Deck this register
        game.damageEffect(player,robot, "Spam");

        player.getLogger().info("Executed damage card "+ toString() + "'s effect");

    }

    /**
     * toString():
     *
     * @return name of the card as String
     */
    public String toString(){
        return "Spam";
    }
}
