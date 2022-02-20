package game.gameObjects.otherObjects;

import game.gameObjects.cards.Card;

/**
 * Register:
 * a card Register on a player's mat
 * @author Shady Mansour
 */
public class Register {
    private Card card;
    private int regNum;

    public Register(int regNum) {
        this.regNum = regNum;
        this.card = null;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    public Card getCard() {
        return card;
    }

    public Boolean isEmpty(){return card ==null;}
}

