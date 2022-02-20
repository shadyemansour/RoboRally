package game.gameObjects.otherObjects;

import game.gameObjects.cards.Card;

import java.util.Collections;
import java.util.List;


public class CardDeck {

    private List<Integer> deck;

    private int numOfCards = 20;

    /**
     * pop():
     * Method for popping the first card from the list (taking a card)
     * @return the first Card of the CardDeck
     */
    @Deprecated
    public Card pop(UpgradeShop deck) {
        Card card =deck.get(0);
        deck.remove(0);
        return card;
    }
    /**
     * shuffle():
     * Method for shuffling the Cards (at the beginning of each round)
     */
    public void shuffle(CardDeck deck) {
        Collections.shuffle(deck.deck);
    }

    /**
     * getNumCards():
     * Method for returning the Number of Cards, that are still available in the deck
     * @return number of Cards that are left on the CardDeck
     */
    public Integer getNumOfCards(CardDeck deck) {
        numOfCards = deck.deck.size();
        return numOfCards;
    }

    /**
     * resetDeck():
     * Refills the card deck with the starting list of cards
     * @return new CardDeck with all cards in
     */
    public CardDeck resetDeck(CardDeck deck){
        //deck = bla;
        numOfCards = 16;

        return this;
    }

    public int getNumOfCards() {
        return numOfCards;
    }

    public void setNumOfCards(int numOfCards) {
        this.numOfCards = numOfCards;
    }
}
