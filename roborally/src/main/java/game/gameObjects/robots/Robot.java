package game.gameObjects.robots;

import networking.PlayerThread;
import game.gameObjects.cards.programmingCards.regularProgrammingCards.ProgrammingCard;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Robot {
    private Logger logger;
    private int id;
    private PlayerThread player;
    private int orientation;            // 1=Norden, 2=Osten, 3=Sueden, 4=Westen
    private boolean checkpointTokens [];
    private int priorityValue;
    /*  Game class will call the getDistanceToAntenna() function for all playing robots.
        It will then decide which robot has the priority and simultaneously set the priorityValue of the robot.
        The robot then performs its register according to this priority determination.
     */

    private int damage;
    private int energyCubes;
    private int xPosition;
    private int yPosition;
    private int startingPosition;
    private boolean isRebooting;
    private int checkPointsReached;

    private boolean canMove;       //used for checkRobot() -> checkWall()
    private boolean isTaken;        //used for checkRobot() -> checkMapEnd()

    public Robot(int id, PlayerThread player) {
        this.id = id;
        this.player = player;
        this.orientation = 2;
        startingPosition = -1;
        this.energyCubes = 5;
        this.checkPointsReached = 0;

        this.canMove = true;
        this.logger = LogManager.getLogger("org.kursivekationen.roborally.Game");
    }

    public void turnRight(){
        if (orientation==4){
            orientation=0;
        }
        orientation++;
        logger.debug(toString() + " turned right -> "+orientation);

    }
    public void turnLeft(){
        if (orientation==1){
            orientation=5;
        }
        orientation--;
        logger.debug(toString() + " turned left -> "+orientation);
    }

    public int getxPosition() {
        return xPosition;
    }
    public void setxPosition(int xPosition) {
        this.xPosition = xPosition;
    }

    public int getyPosition() {
        return yPosition;
    }
    public void setyPosition(int yPosition) {
        this.yPosition = yPosition;
    }

    public int getStartingPosition() { return startingPosition;}
    public void setStartingPosition(int startingPosition) {
        this.startingPosition = startingPosition;
        yPosition = startingPosition/13;
        xPosition = startingPosition-yPosition*13;
    }

    public int getPosition() {return yPosition*13 + xPosition;}
    public void setPosition(int position) {
       yPosition = position/13;
       xPosition = position-yPosition*13;

    }

    public int getOrientation() {
        return orientation;
    }
    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public boolean getIsRebooting() {
        return isRebooting;
    }
    public void setIsRebooting(boolean rebooting) {
        isRebooting = rebooting;
    }

    public int getEnergyCubes() {
        return energyCubes;
    }
    public void setEnergyCubes(int energyCubes) {
        this.energyCubes = energyCubes;
    }
    public void incrementEnergyCubes(){
        this.energyCubes++;
    }

    public int getCheckPointsReached() {
        return checkPointsReached;
    }
    public void setCheckPointsReached(int checkPointsReached) {
        this.checkPointsReached = checkPointsReached;
    }
    public void incrementCheckPointsReached(){
        this.checkPointsReached++;
    }

    public boolean getCanMove() {
        return canMove;
    }
    public void setCanMove(boolean canMove) {
        this.canMove = canMove;
    }


    @Override
    public String toString() {
        return "Robot";
    }
    public PlayerThread getPlayer() {
        return player;
    }

    public void setPlayer(PlayerThread player) {
        this.player = player;
    }

    public boolean isTaken() {
        return isTaken;
    }

    public void setTaken(boolean taken) {
        isTaken = taken;
    }
}
