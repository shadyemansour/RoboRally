package game.gameObjects.otherObjects;


import game.gameObjects.cards.*;
import game.gameObjects.cards.programmingCards.regularProgrammingCards.*;
import networking.PlayerThread;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * ProgrammingCardDeck
 * this class is a representation of a programming card deck
 */
public class ProgrammingCardDeck extends CardDeck{
    Logger logger = LogManager.getLogger("org.kursivekationen.roborally.Game");

    private List<Card> deck;
    private PlayerThread player;


    /**
     * Constructor:
     * initializes a deck
     */
    public ProgrammingCardDeck(PlayerThread player) {
        this.player = player;
        initiateDeck();
    }

    /**
     * initiateDeck()
     * initiates a new programming cards deck
     */
    private void initiateDeck() {
        Card[] cards = {new MoveI(),new MoveI(),new MoveI(),new MoveI(), new MoveI(),new MoveII(),new MoveII(),new MoveII(),new MoveIII(),new TurnRight(),
                new TurnRight(),new TurnRight(),new TurnLeft(),new TurnLeft(),new TurnLeft(), new BackUp(), new PowerUp(), new Again(), new Again(),new UTurn()};
        deck = new ArrayList(Arrays.asList(cards));
        shuffle();
        logger.debug("Player " +player.getUserId()+"'s programmingCardDeck: Initiated");

    }

    /**
     * pop()
     * @return
     */
    public Card pop(){
        if(deck.isEmpty()){
            logger.debug("Player " +player.getUserId()+"'s programmingCardDeck: Deck isEmpyty");
            discardedToDeck();
        }
        Card card = deck.get(0);
        deck.remove(0);
        logger.debug("Player " +player.getUserId()+"'s programmingCardDeck: Popped Card");

        return card;
    }
    /**
     * add():
     * adds a card back to the deck
    */
    public void add(Card card){
        deck.add(card);
        logger.debug("Player " +player.getUserId()+"'s programmingCardDeck: "+"Added " +card.toString() +" back to deck.");
    }

    /**
     * takes the cards out of the discardPile and puts them in
     * the programmingCardDeck, shuffles the cards and sends
     * out the JSON Object
     */
    public void discardedToDeck() {
        List discardedPile = player.getDiscardedPile().getDiscarded();
        setNumOfCards(discardedPile.size());
        while(!player.getDiscardedPile().isEmpty()){
            deck.add(player.getDiscardedPile().pop());
        }

        shuffle();
        JSONObject msgBody = new JSONObject();
        msgBody.put("playerID", player.getUserId());
        player.getServer().broadcast("ShuffleCoding", msgBody,null);
        System.out.println("Shuffle for " + player.getUserId());
    }

    /**
     * shuffle():
     * shuffles the deck
     */
    public void shuffle(){
        Collections.shuffle(deck);
        logger.debug("Player " +player.getUserId()+"'s programmingCardDeck: Shuffled");

    }

    /**
     *
     * @param i
     */
    public void remove(int i){
        deck.remove(i);
    }


    public Boolean isEmpty(){
        return getNumOfCards() == 0;
    }
}
