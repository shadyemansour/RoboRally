package game.gameObjects.otherObjects;
import game.gameObjects.cards.*;
import java.util.*;

/**
 * UpgradeShop
 * this class is a representation of the game's upgrade shop
 */
@Deprecated
public class UpgradeShop extends CardDeck{
    public ArrayList<Card> deck;

    public void remove(int i) {
        deck.remove(i);
    }

    public Card get(int i) {
        return deck.get(i);
    }
}
