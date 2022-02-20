package game.gameObjects.otherObjects;

import game.gameObjects.cards.Card;
import game.gameObjects.cards.damageCards.Spam;
import game.gameObjects.cards.damageCards.Trojan;
import game.gameObjects.cards.damageCards.Virus;
import game.gameObjects.cards.damageCards.Worm;
import networking.PlayerThread;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * DamageCardDecks:
 * contains all damage cards decks of the game
 */
public class DamageCardDecks extends CardDeck{
    private static final Logger logger = LogManager.getLogger("org.kursivekationen.roborally.Game");
    public static ArrayList<Card> spamDeck;
    public static ArrayList<Card> wormDeck;
    public static ArrayList<Card> trojanDeck;
    public static ArrayList<Card> virusDeck;

    /**
     * this function is called at the start of a game
     * it initiated all damageCardDecks with the correct amount of cards
     * @author Vincent Oeller, Franziska Leitmeir, Vivien Pfeiffer
     */
    public void initiateDecks(){

        Card[] spamCards = new Card[38];
        for (int i = 0; i < spamCards.length; i++){
            spamCards[i] = new Spam();
        }
        spamDeck = new ArrayList<>(Arrays.asList(spamCards));
        logger.debug("Initiated Spam Deck");
        Card[] wormCards = new Card[6];
        for (int i = 0; i < wormCards.length; i++){
            wormCards[i] = new Worm();
        }
        wormDeck = new ArrayList<>(Arrays.asList(wormCards));
        logger.debug("Initiated Worm Deck");

        Card[] trojanCards = new Card[12];
        for (int i = 0; i < trojanCards.length; i++){
            trojanCards[i] = new Trojan();
        }
        trojanDeck = new ArrayList<>(Arrays.asList(trojanCards));
        logger.debug("Initiated Trojan Horse Deck");

        Card[] virusCards = new Card[18];
        for (int i = 0; i < virusCards.length; i++){
            virusCards[i] = new Virus();
        }
        virusDeck = new ArrayList<>(Arrays.asList(virusCards));
        logger.debug("Initiated Virus Deck");
        logger.debug("All damage decks initiated");
    }

    /**
     * this function checks if a given damageCardDeck is empty
     * @param damageDeck the deck from which you want to know if its empty (as a STRING)
     * @return boolean: whether the deck is empty
     * @author Vincent Oeller, Franziska Leitmeir, Vivien Pfeiffer
     */
    public boolean deckIsEmpty(String damageDeck){
        return switch (damageDeck) {
            case "spamDeck" -> spamDeck.isEmpty();
            case "wormDeck" -> wormDeck.isEmpty();
            case "trojanDeck" -> trojanDeck.isEmpty();
            case "virusDeck" -> virusDeck.isEmpty();
            case "all" -> virusDeck.isEmpty() && trojanDeck.isEmpty() && wormDeck.isEmpty() && spamDeck.isEmpty();
            //this case is only reached if the function is called incorrectly
            default -> true;
        };
    }

    /**
     * this function is called when a player receives a damageCard
     * @param damageDeck the deck from which the card is taken as a STRING
     * @author Vincent Oeller, Franziska Leitmeir, Vivien Pfeiffer
     */
    public void pop(String damageDeck, PlayerThread player){
        if (deckIsEmpty("all")){
            player.setPickDamage(0);
            return;
        }
        switch (damageDeck) {
            case "spamDeck":
                if (!deckIsEmpty("spamDeck")) {
                    Card card = spamDeck.get(0);
                    spamDeck.remove(0);
                    player.getDrawDamage().add(card.toString());
                    logger.debug("Adding Spam card to player "+ player.getUserId()+"'s discardedPile" );
                    player.getDiscardedPile().add(card);
                    break;
                } else {
                    player.incrementPickDamage();
                    logger.debug("Incremented Pick Damage -> ID: " +player.getUserId() );
                }
                break;
            case "wormDeck":
                if (!deckIsEmpty("wormDeck")) {
                    Card card = wormDeck.get(0);
                    wormDeck.remove(0);
                    player.getDrawDamage().add(card.toString());
                    player.getDiscardedPile().add(card);
                    logger.debug("Adding Worm card to player "+ player.getUserId()+"'s discardedPile");
                    System.out.println("2");
                } else {
                    player.incrementPickDamage();
                    logger.debug("Incremented Pick Damage -> ID: " +player.getUserId() );
                }
                break;
            case "trojanDeck":
                if (!deckIsEmpty("trojanDeck")) {
                    Card card = trojanDeck.get(0);
                    trojanDeck.remove(0);
                    player.getDrawDamage().add(card.toString());
                    player.getDiscardedPile().add(card);
                    logger.debug("Adding Trojan card to player "+ player.getUserId()+"'s discardedPile");
                } else {
                    player.incrementPickDamage();
                    logger.debug("Incremented Pick Damage -> ID: " +player.getUserId() );
                }
                break;
            case "virusDeck":
                if (!deckIsEmpty("virusDeck")) {
                    Card card = virusDeck.get(0);
                    virusDeck.remove(0);
                    player.getDrawDamage().add(card.toString());
                    player.getDiscardedPile().add(card);
                    logger.debug("Adding Virus card to player "+ player.getUserId()+"'s discardedPile" );
                } else {
                    player.incrementPickDamage();
                    logger.debug("Incremented Pick Damage -> ID: " +player.getUserId() );
                }
                break;
        }
    }

    /**
     * this function is called when a damage card is played and returned to
     * the damageCardDeck
     * @param damageCard the Card that is added back to its damageCardDeck
     *                   after it has been played
     * @author Vincent Oeller, Franziska Leitmeir, Vivien Pfeiffer
     */
    public void add(String damageCard){
        switch (damageCard){
            case "Spam":
                spamDeck.add(new Spam());
                logger.debug("Added Spam card to Damage CardDeck");
                break;
            case "Worm":
                wormDeck.add(new Worm());
                logger.debug("Added Worm card to Damage CardDeck");
                break;
            case "trojan":
                trojanDeck.add(new Trojan());
                logger.debug("Added Trojan Horse card to Damage CardDeck");
                break;
            case "Virus":
                virusDeck.add(new Virus());
                logger.debug("Added Virus card to Damage CardDeck");
                break;
        }
    }

}
