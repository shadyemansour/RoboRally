package networking;

import game.gameObjects.bot.ServerBot;
import game.gameObjects.cards.Card;
import game.gameObjects.cards.damageCards.DamageCards;
import game.gameObjects.otherObjects.*;
import game.gameObjects.robots.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.*;

/**
 * PlayerThread:
 * A thread deployed by server dedicated for a player's communication
 * contains all player information
 */
public class PlayerThread extends Thread {
    private static final Logger logger = LogManager.getLogger("org.kursivekationen.roborally.Server");
    // 1.) Variables
    private Socket socket;
    private Server server;
    private ObjectOutputStream output;
    private JSONParser parser = new JSONParser();

    private String username;
    private int id;
    private String group;
    private Boolean isAi;
    private Boolean isServerAi;
    private Boolean connected;
    private Boolean selectionFinished;
    private int figure;
    private Boolean ready;
    private ProgrammingCardDeck programmingCardDeck;
    private DiscardedPile discardedPile;
    private Card[] drawnCards;          //the cards you place on the registers
    private int order;
    private ServerBot bot;
    private Robot robot;
    private JSONObject playerMessage;
    private int pickDamage;
    private ArrayList<String> drawDamage = new ArrayList<>();
    private ArrayList<Register> registers;
    private ArrayList<Boolean> emptyRegisters;
    private Boolean playIt;
    private Boolean damageSelected;


    public PlayerThread(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
        this.connected = true;
        this.programmingCardDeck = new ProgrammingCardDeck(this);
        this.selectionFinished = false;
        this.registers = new ArrayList<>();
        this.isServerAi = false;
        this.ready =false;
        this.playIt=false;
        this.damageSelected = false;

        Register r1 = new Register(1);
        registers.add(r1);
        Register r2 = new Register(2);
        registers.add(r2);
        Register r3 = new Register(3);
        registers.add(r3);
        Register r4 = new Register(4);
        registers.add(r4);
        Register r5 = new Register(5);
        registers.add(r5);

        resetEmptyRegisters();

        this.discardedPile = new DiscardedPile(this);
    }

    /**
     * run():
     * receives messages from client and calls getPayload()
     * @author Shady Mansour
     */
    public void run() {
    try{
        // Output
        output = new ObjectOutputStream(socket.getOutputStream());
        // Input
        ObjectInputStream input = new ObjectInputStream(socket.getInputStream());

        JSONObject msgbody = new JSONObject();
        msgbody.put("protocol", server.getProtocol());
        sendMessage("HelloClient", msgbody);

        String response;
        do {
            response = (String) input.readObject();
            playerMessage = (JSONObject) parser.parse(response);
            getPayload(playerMessage);


        } while (connected);
        rageQuit();
        server.removeUser(id);
        logger.info("Player disconnected");
        logger.info("Currently connected players: " + server.getClients().keySet().size());

    }catch(EOFException oops){
        rageQuit();
        logger.info("Player disconnected");
        logger.info("Currently connected players: " + server.getClients().keySet().size());
    } catch(Exception oops){
        oops.printStackTrace();
        rageQuit();
        logger.info("Player disconnected"); //toJSON
        logger.info("Currently connected players: " + server.getClients().keySet().size());
    }

}

    /**
     * getPayload():
     * checks the message type and redirects the message body
     * to the appropriate method
     * @param file: received JSONObject
     * @author Shady Mansour
     */
    public void getPayload(JSONObject file){
        String msgType =file.get("messageType").toString();
        if(!msgType.equals("HelloServer"))
            logger.info("Received " + msgType + "from player " +getUserId() );
        switch (msgType){
            case "HelloServer":
                server.checkWelcome((JSONObject) file.get("messageBody"), this);
                break;
            case "PlayerValues":
                server.checkPlayerValues((JSONObject) file.get("messageBody"), this);

                break;
            case "SetStatus":
                server.checkStatus((JSONObject) file.get("messageBody"), this);
                break;
            case "SendChat":
                JSONObject body= (JSONObject) file.get("messageBody");
                JSONObject msg;
                if (checkForCheat(body) && server.getGameIsOn()) {
                    server.getGame().cheating((String) body.get("message"), this);
                }
                int to = ((Long) body.get("to")).intValue();
                       // body.get("to").toString();

                if (to == -1) {
                    msg = sendToReceived((JSONObject)file.get("messageBody"),this,false);
                    server.broadcast("ReceivedChat", msg, this);
                }else{
                    msg = sendToReceived((JSONObject)file.get("messageBody"),this,true);
                    server.chatWithUser("ReceivedChat",msg,to);
                }
                break;
            case "PlayCard":
                playCard((JSONObject)file.get("messageBody"));
                break;

            case "SetStartingPoint":
                server.checkStartingPoint((JSONObject)file.get("messageBody"), this);
                break;

            case "SelectCard":
                JSONObject msgBody = (JSONObject) file.get("messageBody");
                server.getGame().placeOnRegister( Integer.parseInt(msgBody.get("register").toString()), whichCard((String) msgBody.get("card")), this);
                logger.debug("Player " + getUserId() + " selected: " + whichCard((String) msgBody.get("card")));
                break;

            case "SelectDamage":
                JSONObject message = (JSONObject) file.get("messageBody");
                server.selectDamage(message, this);
                break;

            case "PlayIt":
                playIt = true;
                synchronized (server.getGame()) {
                    server.getGame().notifyAll();
                }
                break;

            case "MapSelected":
                JSONArray map = (JSONArray) file.get("messageBody");
                logger.info("Selected map: " + map.get(0));
                JSONObject bdy = new JSONObject();
                bdy.put("map", map);
                server.broadcast("MapSelected", bdy, null);
                if ((server.getReadyPlayers() >= 2 && server.getReadyPlayers() == server.getPlayersInLobby())) {
                    server.setMap(map);
                    server.startGameField(map);
                } else {
                    server.setMap(map);
                }


        }
    }


    /**
     * playCard():
     * used to play UpgradeCards, when the message PlayCard
     * is received
     * @param messageBody
     * @author Shady Mansour
     */
    public void playCard(JSONObject messageBody) {
        String cardtype = (String) messageBody.get("card");
        Card card =  whichCard(cardtype);

        if (card == null){
            //invalid card name
            JSONObject msgBody =new JSONObject();
            msgBody.put("error", "Invalid card selected");
        }else{
            JSONObject msgBody = new JSONObject();
            msgBody.put("playerID", id);
            msgBody.put("card", cardtype);
            server.broadcast("CardPlayed", msgBody, this);
        }
    }

    /**
     * whichCard():
     * returns a Card object for the specified cardType
     * @param cardtype: name of the card
     * @return Card object for the specified type
     * @author Shady Mansour
     */
    public Card whichCard(String cardtype) {
        String cardstr = "";

        switch (cardtype) {

            case "Spam", "Trojan", "Virus", "Worm":

                cardstr = "game.gameObjects.cards.damageCards." + cardtype;
                break;

            case "MoveI", "MoveII", "MoveIII", "TurnLeft", "TurnRight", "UTurn", "BackUp", "Again", "PowerUp":

                cardstr = "game.gameObjects.cards.programmingCards.regularProgrammingCards." + cardtype;
                break;

            case "EnergyRoutine", "RepeatRoutine", "SandboxRoutine", "SpamFolder", "WeaselRoutine":

                cardstr = "game.gameObjects.cards.programmingCards.specialProgrammingCards." + cardtype;
                break;

            case "AdminPrivilege", "CorruptionWave", "TrojanNeedler", "PressorBeam", "DefragGizmo", "ModularChassis"
                    , "DoubleBarrelLaser", "VirusModule", "Scrambler", "BlueScreenOfDeath", "Teleporter", "CrabLegs"
                    , "Brakes", "DeflectorShield", "MemoryStick", "RailGun", "CacheMemory", "MiniHowitzer", "RammingGear"
                    , "Firewall", "TractorBeam", "SideArms", "RearLaser", "HoverUnit":

                cardstr = "game.gameObjects.cards.upgradeCards.permanentUpgradeCards." + cardtype;
                break;

            case "RepeatRoutineTemp", "Boink", "Reboot", "Recharge", "SpamBlocker", "WeaselRoutineTemp"
                    , "SpeedRoutineTemp", "Zoop", "Recompile", "ManualSort", "SpamFolderRoutine", "Refresh"
                    , "Hack", "SandboxRoutineTemp", "EnergyRoutineTemp", "MemorySwap":

                cardstr = "game.gameObjects.cards.upgradeCards.temporaryUpgradeCards." + cardtype;
                break;
        }
        try {
            Object object = Class.forName(cardstr).getDeclaredConstructor().newInstance();
            return (Card) object;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(),e);
            return null;
        }
    }


    /**
     * sendToReceived():
     * converts an incoming chat message to an outgoing one
     * @param send: chat message
     * @param player: playerThread of the player who sent the message
     * @param direct: true if a message is private false if not
     * @return JSONObject message body of outgoing message
     * @author Shady Mansour
     */
    public JSONObject sendToReceived(JSONObject send,PlayerThread player, boolean direct) {
        JSONObject body =  new JSONObject();
        body.put("message",send.get("message"));
        body.put("from", player.getUsername());
        body.put("private",direct);

        return body;
    }

    /**
     * resetEmptyRegisters():
     * resets emptyRegisters Array.
     * called after each activation phase.
     * @author Shady Mansour
     */
    public void resetEmptyRegisters(){
        this.emptyRegisters = new ArrayList<>();

        for (int i = 0; i < registers.size(); i++) {
            emptyRegisters.add(registers.get(i).isEmpty());
        }
    }

    /**
     * sendMessage():
     * sends message to player
     * @param type essage type
     * @param body message body
     * @author Shady Mansour
     */
    public void sendMessage(String type, JSONObject body) {
        JSONObject msg = new JSONObject();
        msg.put("messageType", type);
        msg.put("messageBody", body);

        if(!isServerAi){
        try {
            output.writeObject(msg.toString());
            logger.debug("Sent " +type + " to " + getUserId());
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("output stream error at sendMessage: "+e.getMessage(),e);
        }
        }else{
            logger.debug("Sent " +type + " to BOT");
            bot.checkMessage(type, body);
        }
    }

    /**
     * rageQuit():
     * is called when a player's connection is lost
     * if a game is being played, the player will be replaced by a bot
     * if players are still in lobby, he'll be removed
     * @author Shady Mansour
     */
    public void rageQuit(){
        logger.debug("Player "+getUserId() + "rage quited.");
        try {
            socket.close();
            logger.debug("Closed Socket");
        } catch (IOException e) {
            logger.error("unable to close socket: "+e.getMessage(),e);
            e.printStackTrace();
        }
       // server.removeUser(username);
        connected = false;
        String action;

        if(server.getGameIsOn()){
            bot = new ServerBot(this, server);
            isServerAi = true;
            action = "AIControl";
        }else{
            action = "Remove";
            server.removeUser(id);
        }

        JSONObject msgBody = new JSONObject();
        msgBody.put("playerID", id);
        msgBody.put("connected", connected);
        msgBody.put("action", action);
        server.broadcast("ConnectionUpdate", msgBody, this);

        if(action.equals("AIControl"))
            bot.start();

        logger.info("Player " +id + "quited. Action taken -> " +action);
    }

    /**
     * fillRegisters():
     * called by drawCards() to fill player registers with a forced hand
     * @param cards cards to placed on registers
     * @author Shady Mansour
     */
    public void fillRegisters(Card[] cards){
        emptyDrawnCards();
        for (int i = 0; i <cards.length; i++) {
            drawnCards[i] = cards[i];
        }

        JSONArray jarray = new JSONArray();
        for (int i = 0; i < 5; i++) {
            registers.get(i).setCard(cards[i]);
            jarray.add(cards[i].toString());
        }
        for(Register reg : registers){
            Card car = reg.getCard();
            if (car instanceof DamageCards){
                for (int j = 0; j < drawnCards.length; j++) {
                    if(drawnCards[j] !=null && drawnCards[j].toString().equals(car.toString())) {
                        setOneCard(j);
                        server.getGame().getDamageCardDecks().add(car.toString());
                        break;
                    }
                }
            }
        }
        JSONObject msgBody = new JSONObject();
        msgBody.put("cards", jarray);
        sendMessage("CardsYouGotNow", msgBody);

    }


    /**
     * checkForCheat():
     * @param message
     * @return
     * @author Franziska Leitmeir, Vivien Pfeiffer
     */
     public boolean checkForCheat(JSONObject message){
        String msg = (String) message.get("message");
        boolean cheat = false;
        if (msg.contains("#")) {
            cheat = true;
        }
        return cheat;
     }

    /**
     * move():
     * movement of a player will be sent by the server
      * @param id: id of the player that moves the roboter
     * @param to: position of the field the roboter moves to
     * @author Franziska Leitmeir
     */
    public void move(int id, int to){

        JSONObject msgBody = new JSONObject();
        msgBody.put("playerID", id);
        msgBody.put("to", to);
        server.broadcast("Movement", msgBody,null);
        if(id ==this.id){
            robot.setPosition(to);
        }
    }

    /**
     * drawDamage():
     * sends all of the damageCards a player must draw
     * @param id: id of the player that has to draw a damage card
     * @param dmgCards: Array of cards, that the player has to draw
     * @author Franziska Leitmeir
     */
    public void drawDamage (int id, List<String> dmgCards) {
        JSONArray cards = new JSONArray();
        for (String card : dmgCards){
            cards.add(card);
        }
        JSONObject msgBody = new JSONObject();
        msgBody.put("playerID", id);
        msgBody.put("cards", cards);
        server.broadcast("DrawDamage", msgBody,null);
        //clears the drawDamage List after it has been sent out
        getDrawDamage().clear();
    }

    /**
     * pickDamage():
     * if all damage cards of one type are assigned the player needs to choose which one he wanted instead
     * @param count: number of cards the player has to draw
     * @author Franziska Leitmeir
     */
    public void pickDamage(int count) {
        JSONObject msgBody = new JSONObject();
        msgBody.put("count", count);
        if(!isServerAi){
        sendMessage("PickDamage", msgBody);
        }else{
            bot.selectDamage(count);
        }

        //resets the pickDamage Counter after it has been sent out
        pickDamage = 0;
    }

    /**
     * shooting():
     * will be send when a roboter is shooting (only for animation purpose)
     * @author Franziska Leitmeir
     */
    public void shooting() {
        JSONObject msgBody = new JSONObject();
        server.broadcast("PlayerShooting", msgBody,null);

    }

    /**
     * reboot():
     * @param playerID
     * @author Franziska Leitmeir
     */
    public void reboot(int playerID){
        JSONObject msgBody = new JSONObject();
        msgBody.put("playerID", playerID);
        server.broadcast("Reboot", msgBody,null);
        logger.debug("Reboot Message broadcasted");
    }

    /**
     * turning():
     * is called, when a roboter changes its direction
     *
     * @param id: id of the player whos roboter changes direction
     * @param direction: String of the direction: "clockwise" or "counterClockwise"
     * @author Franziska Leitmeir
     */
    public void turning(int id, String direction) {
        JSONObject msgBody = new JSONObject();
        msgBody.put("playerID", id);
        msgBody.put("direction", direction);
        server.broadcast("PlayerTurning", msgBody,null);

    }

    /**
     * energy():
     * has no special logic but is send as information
     *
     * @param id: id of the player who has the energycubes
     * @param count: integer value of the number of energy the player has
     * @author Franziska Leitmeir
     */
    public void energy (int id, int count) {
        JSONObject msgBody = new JSONObject();
        msgBody.put("playerID", id);
        msgBody.put("count", count);
        server.broadcast("Energy", msgBody,null);
    }

    /**
     * checkPointReached():
     * sends the number of the checkpoint the player he has reached at last
     *
     * @param id: id of the player
     * @param number: number of checkpoints the player has reached (2 -> the player reached the first two checkpoints)
     * @author Franziska Leitmeir
     */
    public void checkPointReached (int id, int number) {
        JSONObject msgBody = new JSONObject();
        msgBody.put("playerID", id);
        msgBody.put("number", number);
        server.broadcast("CheckpointReached", msgBody,null);
        if(id == getUserId() && isServerAi){
            bot.checkPointReached();
        }
    }

    /**
     * gameWon():
     * will be called when a player reached the last Checkpoint
     *
     * @param id: id of the player who won
     * @author Franziska Leitmeir
     */
    public void gameWon(int id) {
        JSONObject msgBody = new JSONObject();
        msgBody.put ("playerID", id);
        server.broadcast("GameWon", msgBody,null );
        logger.info("GameWon Message sent.");
    }

    /**
     * getDamaged():
     * will be called, when a roboter gets a damageCard (by a laser, rebooting, virus, trojanHorse)
     */
    public void getDamaged(String cause){
        JSONObject msgBody = new JSONObject();
        msgBody.put("playerID", id);
        msgBody.put("by", cause);

        server.broadcast("Damaged", msgBody, null);
    }

    /**
     * emptyDrawnCard():
     * empties drawnCards list
     */
    private void emptyDrawnCards(){
        for (int i = 0; i < drawnCards.length; i++) {
            drawnCards[i]=null;
        }
    }


    //Getter and Setter
    public ProgrammingCardDeck getProgrammingCardDeck() {
        return programmingCardDeck;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) { this.username = username; }

    public int getUserId() {
        return id;
    }
    public void setUserId(int id) {
        this.id = id;
    }

    public Robot getRobot() { return robot;}
    public void setRobot(Robot robot) {
        this.robot = robot;
    }

    public int getOrder() {
        return order;
    }
    public void setOrder(int order) { this.order = order;}

    public Boolean getReady() { return ready; }
    public void setReady(Boolean ready) { this.ready = ready; }

    public void setGroup(String group) { this.group = group; }

    public Boolean getAi() { return isAi;}
    public void setAi(Boolean ai) { isAi = ai; }


    public int getFigure() { return figure; }
    public void setFigure(int figure) {
        this.figure = figure;
        switch (figure){
            case 1:
                this.robot = new Twonky(id, this);
                break;
            case 2:
                this.robot = new HulkX90(id, this);
                break;
            case 3:
                this.robot = new HammerBot(id, this);
                break;
            case 4:
                this.robot = new SmashBot(id, this);
                break;
            case 5:
                this.robot = new ZoomBot(id, this);
                break;
            case 6:
                this.robot = new SpinBot(id, this);
                break;
        }
    }

    public Boolean getDamageSelected() { return damageSelected; }
    public void setDamageSelected(Boolean damageSelected) { this.damageSelected = damageSelected; }


    public DiscardedPile getDiscardedPile() { return discardedPile; }

    public Server getServer() { return server; }

    public ArrayList<Register> getRegisters() { return registers; }

    public Boolean getSelectionFinished() { return selectionFinished;}

    public int getStartingPoint() { return robot.getStartingPosition(); }

    public void setStartingPoint(int startingPoint) { this.robot.setStartingPosition(startingPoint); }

    public void setSelectionFinished(Boolean selectionFinished) { this.selectionFinished = selectionFinished; }

    public ServerBot getBot() { return bot; }
    public void setBot(ServerBot bot) { this.bot = bot; }

    public Boolean getPlayIt() { return playIt; }


    public void setPlayIt(Boolean playIt) { this.playIt = playIt; }

    public Card[] getDrawnCards() { return drawnCards; }

    public void setOneCard(int index){
        this.drawnCards[index] = null;
    }
    public void setDrawnCards(Card[] drawnCards) {

        this.drawnCards = drawnCards;

        JSONObject msgBody  = new JSONObject();
        JSONObject broadcastBody = new JSONObject();

        String[] strings = new String[drawnCards.length];
        JSONArray jarray = new JSONArray();

        for (int i = 0; i < drawnCards.length; i++) {
            String card = drawnCards[i].toString();
            strings[i]=card;
            jarray.add(card);
        }

        msgBody.put("cards", jarray);
        broadcastBody.put("playerID",id);
        broadcastBody.put("cards", drawnCards.length);


        server.broadcast("NotYourCards", broadcastBody, this);

        if(!isServerAi){
            sendMessage("YourCards", msgBody);
        }else{
            bot.setDrawnCards(strings);
        }
    }
    public int getPickDamage() {
        return pickDamage;
    }
    public void setPickDamage(int i) {
        this.pickDamage = i;
    }
    public void incrementPickDamage(){
        this.pickDamage++;
    }

    public ArrayList<String> getDrawDamage() {
        return drawDamage;
    }

    public Logger getLogger(){return logger;}

    public void setRegisters(ArrayList<Register> registers) {
        this.registers = registers;
    }

    public ArrayList<Boolean> getEmptyRegisters() {
        return emptyRegisters;
    }

}