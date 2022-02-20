package game.gameObjects.cards.programmingCards.regularProgrammingCards;

import game.gameLogic.Game;
import game.gameObjects.cards.Card;
import networking.PlayerThread;
import game.gameObjects.robots.Robot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

/**
 * MoveIII:
 * this class represents the card MoveThree in the game
 */
public class MoveIII extends Card implements ProgrammingCard{
    private static final Logger logger = LogManager.getLogger(Game.class);
    /**
     * effect():
     * applies the effect of the card
     *
     * @param player: PlayerThread of the player who played the card
     * @param robot:  the robot on which the effect should be applied
     * @author Vincent Oeller, Franziska Leitmeir, Vivien Pfeiffer
     */
    @Override
    public void effect(PlayerThread player, Robot robot, int round) {
        //3 times MoveI
        Card MoveI = new MoveI();
        MoveI.effect(player, robot, round);
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
        return "MoveIII";
    }
}
