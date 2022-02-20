package game.gameObjects.otherObjects;

import game.gameObjects.cards.Card;
import networking.PlayerThread;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * DiscardedPile:
 * the cards the player discarded
 */
public class DiscardedPile {
    Logger logger = LogManager.getLogger("org.kursivekationen.roborally.Game");
    private PlayerThread player;
    private List<Card> discarded;

    public DiscardedPile(PlayerThread player) {
        this.player = player;
        this.discarded = new ArrayList<>();
    }

    /**
     * adds a card to the pile
     * @param card
     */
    public void add(Card card){
        discarded.add(card);
        logger.debug("Player " +player.getUserId()+"'s DiscardedPile: "+"Added " +card.toString() +" to discarded pile");

    }

    /**
     * pop()
     * pops a card from the pile
     * @return
     */
    public Card pop(){
        Card card = discarded.get(0);
        discarded.remove(card);
        return card;
    }


    /**
     * size()
     * @return pile's size
     */
    public int size(){
        return discarded.size();
    }

    /**
     * reset():
     * empties the pile
     */
    public void reset(){
        discarded.clear();
    }

    /**
     * isEmpty()
     * @return true if pile is empty
     */
    public boolean isEmpty(){
        return discarded.isEmpty();
    }

    public List<Card> getDiscarded() {
        return discarded;
    }
}
