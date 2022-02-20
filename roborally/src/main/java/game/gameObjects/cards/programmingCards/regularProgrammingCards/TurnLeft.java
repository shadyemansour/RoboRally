package game.gameObjects.cards.programmingCards.regularProgrammingCards;

import game.gameObjects.cards.Card;
import networking.PlayerThread;
import game.gameObjects.robots.Robot;

/**
 * TurnLeft:
 * this class represents the card Turn Left in the game
 */
public class TurnLeft extends Card implements ProgrammingCard {

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
    public void effect(PlayerThread player, Robot robot,int round) {
        robot.turnLeft();
        player.turning(player.getUserId(), "counterClockwise");

        player.getLogger().info("Executed programming card "+ toString() + "'s effect");
        player.getLogger().debug("Robot's new orientation: " + robot.getOrientation());
    }

    /**
     * toString():
     *
     * @return name of the card as String
     */
    public String toString(){
        return "TurnLeft";
    }
}
