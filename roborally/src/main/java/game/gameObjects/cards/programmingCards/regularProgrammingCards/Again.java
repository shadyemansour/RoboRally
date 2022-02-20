package game.gameObjects.cards.programmingCards.regularProgrammingCards;

import game.gameObjects.cards.Card;
import game.gameObjects.otherObjects.Register;
import networking.PlayerThread;
import game.gameObjects.robots.Robot;

/**
 * Again:
 * this class represents the card Again in the game
 */
public class Again extends Card implements ProgrammingCard {

    /**
     * effect():
     * applies the effect of the card
     * @param player: PlayerThread of the player who played the card
     * @param robot:  the robot on which the effect should be applied
     * @param round   : current round
     * @author Vincent Oeller, Franziska Leitmeir, Vivien Pfeiffer
     */
    @Override
    public void effect(PlayerThread player, Robot robot, int round) {
        int playerID = player.getUserId();
        if (round == 1) {
            player.getLogger().info("Again was placed on first register. No Action taken");
            return;
        }
        Register register = player.getServer().getClients().get(playerID).getRegisters().get(round - 2);
        Card card = register.getCard();
        player.getLogger().debug("Got card from previous register -> " +card.toString() );
        if (card instanceof Again) {
            effect(player, robot, round - 1);
            return;
        }
        card.effect(player, robot, round);

        player.getLogger().info("Executed programming card " + toString() + "'s effect");

    }
    
    /**
     * toString():
     *
     * @return name of the card as String
     */
    public String toString() {
        return "Again";
    }

}
