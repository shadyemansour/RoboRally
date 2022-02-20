package game.gameObjects.cards.programmingCards.regularProgrammingCards;

import game.gameObjects.cards.Card;
import networking.PlayerThread;
import game.gameObjects.robots.Robot;

/**
 * MoveII:
 * this class represents the card MoveTwo in the game
 */
public class MoveII extends Card implements ProgrammingCard {

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
        //2 times MoveI
        Card MoveI = new MoveI();
        MoveI.effect(player, robot, round);
        MoveI.effect(player, robot, round);

        player.getLogger().info("Executed programming card "+ toString() + "'s effect");

    }

    /**
     * toString():
     *
     * @return name of the card as String
     */
    public String toString(){
        return "MoveII";
    }
}
