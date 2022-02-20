package game.gameObjects.cards.damageCards;

import game.gameLogic.Game;
import game.gameObjects.cards.Card;
import game.gameObjects.robots.Robot;
import networking.PlayerThread;

/**
 * TrojanHorse:
 * this class represents the card TrojanHorse in the game
 * when programmed: take two SPAM cards
 */

public class Trojan extends Card implements DamageCards {
    /**
     * effect():
     * applies the effect of the card
     *
     * @param player: PlayerThread of the player who played the card
     * @param robot:  the robot on which the effect should be applied
     * @param round : current round
     * @author Franziska Leitmeir
     */
    @Override
    public void effect(PlayerThread player, Robot robot, int round) {

        //play the top card of the Programming Deck this register
        Game game = player.getServer().getGame();
        game.damageEffect(player,robot, "TrojanHorse");

        //draw two Spam-Cards
        player.getDamaged("TrojanHorse");
        player.getServer().getGame().getDamageCardDecks().pop("spamDeck", player);
        player.getServer().getGame().getDamageCardDecks().pop("spamDeck", player);

        player.getLogger().info("Executed damage card "+ toString() + "'s effect");
    }

    /**
     * toString():
     *
     * @return name of the card as String
     */
    public String toString(){
        return "Trojan";
    }
}
