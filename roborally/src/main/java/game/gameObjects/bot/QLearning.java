package game.gameObjects.bot;

import game.gameObjects.gamefield.FieldObject;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;

/**
 * QLearning:
 * This class uses Q-learning (value-based learning algorithm) to
 * study the map given and provide the best path from a given
 * starting point to a given target.
 *
 * @author Shady Mansour
 */
public class QLearning implements Cloneable {

    private static final Logger logger = LogManager.getLogger("org.kursivekationen.roborally.Bot");
    private final double lr = 0.1; // Learning rate
    private final double gamma = 0.99; // Discount factor: normally between 0.8 and 0.99

    private int mapWidth;
    private int mapHeight;
    private int statesCount;
    private int startState;
    private LinkedList<Integer> bestPath = new LinkedList();
    private int checkpoint;
    private String mapType;
    private ArrayList<FieldObject> blueBelts;
    private ArrayList<FieldObject> greenBelts;
    private ArrayList<FieldObject> pits;
    private ArrayList<Integer> mod;


    private final int reward = 100;
    private final int penalty = -10;

    private FieldObject[][] map;

    private int[][] R;      // rewards table
    private double[][] Q;  //Q-Table contains q-values which help find the best action for each state

    public QLearning(FieldObject[][] fullMap, int startingState, int checkpoint, String mapType) {
        this.mapWidth = 13;
        this.mapHeight = 10;
        this.map = fullMap;
        this.checkpoint = checkpoint;
        this.mapType=mapType;

        this.statesCount = mapHeight * mapWidth;

        R = new int[statesCount][statesCount];
        Q = new double[statesCount][statesCount];

        this.startState = startingState;
        this.greenBelts = new ArrayList<>();
        this.blueBelts = new ArrayList<>();
        this.pits = new ArrayList<>();
        int[] array = {0, 1, 2};
        this.mod = new ArrayList(Arrays.asList(array));

        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 13; j++) {
                String t = map[i][j].getType();
                int s = map[i][j].getSpeed();
                if (t.contains("Belt")) {
                    if (s == 1) {
                        greenBelts.add(map[i][j]);
                    } else if (s == 2) {
                        blueBelts.add(map[i][j]);
                    }
                }
                else if (t.contains("Pit")){
                    pits.add(map[i][j]);
                }
            }
        }
        fillRewardsMatrix();
    }


    /**
     * fillRewardsMatrix():
     * goes through all possible states and adds a value to
     * the rewards matrix accordingly. Walls, Lasers and Pits get
     * a penalty (-10), checkpoints get a reward (100) and other
     * field types get 0. initializeQ() is automatically initiated
     * after the matrix is filled.
     */
    private void fillRewardsMatrix() {
        for (int k = 0; k < statesCount; k++) {

            int i = k / mapWidth;
            int j = k - i * mapWidth;


            // Fill in the reward matrix with -1
            for (int s = 0; s < statesCount; s++) {
                R[k][s] = -1;
            }
            String currentType = map[i][j].getType();
            logger.debug("Current state: " + k + ": " + currentType);
            if (k != checkpoint && !currentType.toLowerCase().contains("pit")) {
                // move left
                moveLeft(k, i, j, currentType);
                // move right
                moveRight(k, i, j, currentType);
                // move up
                moveUp(k, i, j, currentType);
                //move down
                moveDown(k, i, j, currentType);
            }

        }
        initializeQ();
    }

    /**
     * moveLeft():
     * checks if moving left from current state is possible
     * and if the target state is a checkpoint.
     *
     * @param k:           current state
     * @param i:           row number
     * @param j:           column number
     * @param currentType: current state's type
     */
    public void moveLeft(int k, int i, int j, String currentType) {
        logger.debug("Trying to move left");
        int left = j - 1;
        if (left >= 0) {
            int target = i * mapWidth + left;
            String targetType = map[i][left].getType();
            if (targetType.toLowerCase().contains("antenna") || targetType.toLowerCase().contains("laser") || (targetType.toLowerCase().contains("pit")) || (mapType.equals("ExtraCrispy")&& mod.contains(target%13) &&targetType.toLowerCase().contains("belt") && map[i][left].getSpeed()==1)) {
                R[k][target] = penalty;
                logger.debug("State " + target + " received penalty. Reason: Laser -> " + targetType.toLowerCase().contains("laser") + " Antenna -> " + targetType.toLowerCase().contains("antenna") + " Pit -> " + targetType.toLowerCase().contains("pit") + " ExtraCrispy GB: " + (!targetType.toLowerCase().contains("pit") && !targetType.toLowerCase().contains("laser")&&!targetType.toLowerCase().contains("antenna")));
            } else if (targetType.toLowerCase().contains("wall") || currentType.toLowerCase().contains("wall")) {
                if (map[i][left].getOrientation() == 0) {
                    R[k][target] = penalty;
                    logger.debug("State " + target + " received penalty. Reason: Wall in target State.");
                    return;
                } else if (map[i][j].getOrientation() == 180) {
                    R[k][target] = penalty;
                    logger.debug("State " + target + " received penalty. Reason: Wall in current State.");
                    return;
                }
            }else if(targetType.toLowerCase().contains("belt")&&map[i][left].getSpeed()==2){
                if(blueBeltMovementBot(target,1).getValue()==-1){
                    R[k][target] = penalty;
                    logger.debug("State " + target + " received penalty. Reason: Belt pushes in pit");
                    return;
                }else{
                    R[k][target] = 0;
                    logger.debug("State " + target + " is Belt.");
                }
            } else {
                R[k][target] = 0;
                logger.debug("State " + target + " is a Empty.");
            }
            if (target == checkpoint) {
                R[k][target] = reward;
                logger.debug("State " + target + " is a checkpoint. Received award");

            }
        }
        logger.debug("Can't move left, " + (j - 1) + " is outside the map");
    }

    /**
     * moveRight():
     * checks if moving right from current state is possible
     * and if the target state is a checkpoint.
     *
     * @param k:           current state
     * @param i:           row number
     * @param j:           column number
     * @param currentType: current state's type
     */
    public void moveRight(int k, int i, int j, String currentType) {
        logger.debug("Trying to move right");
        int right = j + 1;
        if (right < mapWidth) {
            int target = i * mapWidth + right;
            String targetType = map[i][right].getType();
            if (targetType.toLowerCase().contains("antenna") || targetType.toLowerCase().contains("laser") || (targetType.toLowerCase().contains("pit")) || (mapType.equals("ExtraCrispy")&& mod.contains(target%13)&& targetType.toLowerCase().contains("belt") && map[i][right].getSpeed()==1)) {
                R[k][target] = penalty;
                logger.debug("State " + target + " received penalty. Reason: Laser -> " + targetType.toLowerCase().contains("laser") + " Antenna -> " + targetType.toLowerCase().contains("antenna") + " Pit -> " + targetType.toLowerCase().contains("pit") + " ExtraCrispy GB: " + (!targetType.toLowerCase().contains("pit") && !targetType.toLowerCase().contains("laser")&&!targetType.toLowerCase().contains("antenna")));

            } else if (targetType.toLowerCase().contains("wall") || currentType.toLowerCase().contains("wall")) {
                if (map[i][right].getOrientation() == 180) {
                    R[k][target] = penalty;
                    logger.debug("State " + target + " received penalty. Reason: Wall in target State.");

                    return;
                } else if (map[i][j].getOrientation() == 0) {
                    R[k][target] = penalty;
                    logger.debug("State " + target + " received penalty. Reason: Wall in current State.");
                    return;
                }
            }else if(targetType.toLowerCase().contains("belt")&&map[i][right].getSpeed()==2){
                if(blueBeltMovementBot(target,1).getValue()==-1){
                    R[k][target] = penalty;
                    logger.debug("State " + target + " received penalty. Reason: Belt pushes in pit");
                    return;
                }else{
                    R[k][target] = 0;
                    logger.debug("State " + target + " is Belt.");
                }
            } else {
                R[k][target] = 0;
                logger.debug("State " + target + " is a Empty.");
            }
            if (target == checkpoint) {
                R[k][target] = reward;
                logger.debug("State " + target + " is a checkpoint. Received award");

            }
        }
        logger.debug("Can't move right, " + (j + 1) + " is outside the map");
    }

    /**
     * moveUp():
     * checks if moving up from current state is possible
     * and if the target state is a checkpoint.
     *
     * @param k:           current state
     * @param i:           row number
     * @param j:           column number
     * @param currentType: current state's type
     */
    public void moveUp(int k, int i, int j, String currentType) {
        logger.debug("Trying to move up");
        int up = i - 1;
        if (up >= 0) {
            int target = up * mapWidth + j;
            String targetType = map[up][j].getType();
            if (targetType.toLowerCase().contains("antenna") || targetType.toLowerCase().contains("laser") || (targetType.toLowerCase().contains("pit")) || (mapType.equals("ExtraCrispy")&& mod.contains(target%13)&& targetType.toLowerCase().contains("belt") && map[up][j].getSpeed()==1)) {
                R[k][target] = penalty;
                logger.debug("State " + target + " received penalty. Reason: Laser -> " + targetType.toLowerCase().contains("laser") + " Antenna -> " + targetType.toLowerCase().contains("antenna") + " Pit -> " + targetType.toLowerCase().contains("pit") + " ExtraCrispy GB: " + (!targetType.toLowerCase().contains("pit") && !targetType.toLowerCase().contains("laser")&&!targetType.toLowerCase().contains("antenna")));

            } else if (targetType.toLowerCase().contains("wall") || currentType.toLowerCase().contains("wall")) {
                if (map[up][j].getOrientation() == 90) {
                    R[k][target] = penalty;
                    logger.debug("State " + target + " received penalty. Reason: Wall in target State.");
                    return;
                } else if (map[i][j].getOrientation() == -90) {
                    R[k][target] = penalty;
                    logger.debug("State " + target + " received penalty. Reason: Wall in current State.");
                    return;
                }
            }else if(targetType.toLowerCase().contains("belt")&&map[up][j].getSpeed()==2){
                if(blueBeltMovementBot(target,1).getValue()==-1){
                    R[k][target] = penalty;
                    logger.debug("State " + target + " received penalty. Reason: Belt pushes in pit");
                    return;
                }else{
                    R[k][target] = 0;
                    logger.debug("State " + target + " is Belt.");
                }
            } else {
                R[k][target] = 0;
                logger.debug("State " + target + " is Empty.");
            }
            if (target == checkpoint) {
                R[k][target] = reward;
                logger.debug("State " + target + " is a checkpoint. Received award");
            }
        }
        logger.debug("Can't move up, " + (i - 1) + " is outside the map");
    }

    /**
     * moveDown():
     * checks if moving down from current state is possible
     * and if the target state is a checkpoint.
     *
     * @param k:           current state
     * @param i:           row number
     * @param j:           column number
     * @param currentType: current state's type
     */
    public void moveDown(int k, int i, int j, String currentType) {
        logger.debug("Trying to move down");
        int down = i + 1;
        if (down < mapHeight) {
            int target = down * mapWidth + j;
            String targetType = map[down][j].getType();
            if (targetType.toLowerCase().contains("antenna") || targetType.toLowerCase().contains("laser") || targetType.toLowerCase().contains("pit") || (mapType.equals("ExtraCrispy")&& mod.contains(target%13)&& targetType.toLowerCase().contains("belt") && map[down][j].getSpeed()==1)) {
                R[k][target] = penalty;
                logger.debug("State " + target + " received penalty. Reason: Laser -> " + targetType.toLowerCase().contains("laser") + " Antenna -> " + targetType.toLowerCase().contains("antenna") + " Pit -> " + targetType.toLowerCase().contains("pit") + " ExtraCrispy GB: " + (!targetType.toLowerCase().contains("pit") && !targetType.toLowerCase().contains("laser")&&!targetType.toLowerCase().contains("antenna")));

            }
            if (targetType.toLowerCase().contains("wall") || currentType.toLowerCase().contains("wall")) {
                if (map[down][j].getOrientation() == -90) {
                    R[k][target] = penalty;
                    logger.debug("State " + target + " received penalty. Reason: Wall in target State.");

                    return;
                } else if (map[i][j].getOrientation() == 90) {
                    R[k][target] = penalty;
                    logger.debug("State " + target + " received penalty. Reason: Wall in current State.");
                    return;
                }else{
                    R[k][target] = 0;
                    logger.debug("State " + target + " is Belt.");
                }
            }else if(targetType.toLowerCase().contains("belt")&&map[down][j].getSpeed()==2){
                if(blueBeltMovementBot(target,1).getKey()==-1){
                    R[k][target] = penalty;
                    logger.debug("State " + target + " received penalty. Reason: Belt pushes in pit");
                    return;
                }
            } else {
                R[k][target] = 0;
                logger.debug("State " + target + " is a Empty.");

            }
            if (target == checkpoint) {
                R[k][target] = reward;
                logger.debug("State " + target + " is a checkpoint. Received award");

            }
        }
        logger.debug("Can't move down, " + (i + 1) + " is outside the map");
    }

    /**
     * initializeQ():
     * initializes the Q-Matrix by filling it up with the
     * values of the rewards matrix. calculateQ() is then
     * called automatically.
     */
    private void initializeQ() {
        for (int i = 0; i < statesCount; i++) {
            for (int j = 0; j < statesCount; j++) {
                Q[i][j] = (double) R[i][j];
            }
        }
        calculateQ();
    }

    /**
     * calculateQ():
     * trains for 1000 cycles to find the best path
     * to tne checkpoint. The values of the Q-Matrix
     * are updated accordingly.
     */
    private void calculateQ() {
        Random rand = new Random();

        for (int i = 0; i < 1000; i++) {
            // Select random initial state
            int crtState = rand.nextInt(statesCount);

            while (!isFinalState(crtState)) {
                int[] actionsFromCurrentState = possibleMovesFromState(crtState);
                if (actionsFromCurrentState.length == 0)
                    break;
                // Pick a random move from possible moves
                int index = rand.nextInt(actionsFromCurrentState.length);
                int nextState = actionsFromCurrentState[index];

                // Q(state,action)= Q(state,action) + alpha * (R(state,action) + gamma * Max(next state, all actions) - Q(state,action))
                double q = Q[crtState][nextState];
                Q[crtState][nextState] = q + lr * (R[crtState][nextState] + gamma * maxQ(nextState) - q);

                crtState = nextState;
            }
        }
    }

    private boolean isPit(int crtState) {
        int i = crtState / mapWidth;
        int j = crtState - i * mapWidth;
        return map[i][j].getType().toLowerCase().contains("pit");
    }

    /**
     * isFinalState():
     * checks if a state is a checkpoint
     *
     * @param state: state nnumber
     * @return true if state is checkpoint otherwise false
     */
    private boolean isFinalState(int state) {
        return state == checkpoint;
    }

    /**
     * possibleMovesFromState():
     *
     * @param state: state number for whom the reachable states
     *               should be calculated
     * @return an array containing all reachable states  from
     * a given state
     */
    private int[] possibleMovesFromState(int state) {
        ArrayList<Integer> result = new ArrayList<>();
        for (int i = 0; i < statesCount; i++) {
            if (R[state][i] != -1) {
                result.add(i);
            }
        }

        return result.stream().mapToInt(i -> i).toArray();
    }

    /**
     * maxQ():
     * finds the highest Q-Value of all possible moves from given state
     *
     * @param nextState: state to be visited
     * @return maximum Q-Value of given state
     */
    private double maxQ(int nextState) {
        int[] movesFromState = possibleMovesFromState(nextState);

        double maxValue = -10;
        for (int nextAction : movesFromState) {
            double value = Q[nextState][nextAction];

            if (value > maxValue)
                maxValue = value;
        }
        return maxValue;
    }

    /**
     * findBestPath():
     * finds best Path from  given state
     *
     * @param i: starting state
     * @return list containing best path from given starting point
     * to the checkpoint.
     */
    public LinkedList<Integer> findBestPath(int i) {
        bestPath.clear();
        bestPath.addFirst(i);
        while (!isFinalState(i)) {
            int k = getBestMove(i);
            bestPath.addLast(k);
            i = k;
        }
        return bestPath;
    }


    /**
     * getBestMove():
     * returns the number of the state that has the highest
     * Q-Value.
     *
     * @param state: current state
     * @return best move from state
     */
    private int getBestMove(int state) {
        int[] movesFromState = possibleMovesFromState(state);

        double maxValue = Double.MIN_VALUE;
        int bestMove = state;

        for (int nextState : movesFromState) {
            double value = Q[state][nextState];

            if (value > maxValue) {
                maxValue = value;
                bestMove = nextState;
            }
        }
        return bestMove;
    }

    /**
     * printR():
     * prints the rewards matrix. Originally used for testing purposes.
     */
    private void printR() {
        System.out.printf("%25s", "States: ");
        for (int i = 0; i <= 99; i++) {
            System.out.printf("%4s", i);
        }
        System.out.println();

        for (int i = 0; i < statesCount; i++) {
            System.out.print("Possible states from " + i + " :[");
            for (int j = 0; j < statesCount; j++) {
                System.out.printf("%4s", R[i][j]);
            }
            System.out.println("]");
        }
    }

    /**
     * printQ():
     * prints the Q-matrix. Originally used for testing purposes.
     */
    private void printQ() {
        System.out.println("Q matrix");
        for (int i = 0; i < Q.length; i++) {
            System.out.print("From state " + i + ":  ");
            for (int j = 0; j < Q[i].length; j++) {
                System.out.printf("%6.2f ", (Q[i][j]));
            }
            System.out.println();
        }
    }

    /**
     * printPath():
     * prints the best path. Originally used for testing purposes.
     */
    public void printPath() {
        System.out.println("\nBest Path:");
        LinkedList path = findBestPath(startState);
        for (int i = 0; i < path.size() - 1; i++) {
            System.out.println("From state " + path.get(i) + " go to state " + path.get(i + 1));
        }
    }


    public LinkedList<Integer> getBestPath(int start) {
        LinkedList path = findBestPath(start);
        return path;
    }

    public FieldObject[][] getMap() {
        return map;
    }

    public QLearning clone() throws CloneNotSupportedException {
        return (QLearning) super.clone();
    }


    /**
     * blueBeltMovementBot():
     * calculates the Position and Orientation of the Bot, when moving onto a BlueBelt
     *
     * @param currentPosition    the Position, the Bot moves to
     * @param currentOrientation Orientation of the Bot
     * @return a Pair (newPosition, newOrientation)
     * @author Vincent Oeller
     */
    public Pair<Integer, Integer> blueBeltMovementBot(int currentPosition, int currentOrientation) {
        boolean isRebooting = false;
        Pair<Integer, String> pair = new Pair<>(-2, null);
        //  <newPosition, rotationDirection>
        for (FieldObject belt1 : blueBelts) {
            int yRobot = currentPosition / 13;
            int xRobot = currentPosition - yRobot * 13;

            if (belt1.getX()+3 == xRobot && belt1.getY() == yRobot) {
                //if the Bot is standing on a blue belt it is moved 1 in the direction of the belt
                int belt1Dir = -1;
                if (belt1.getType().equals("Belt")) {
                    double beltDirectionDeg = belt1.getOrientation();
                    belt1Dir = calculateDirection(beltDirectionDeg);
                } else {      //if it is a rotating Belt
                    String beltDirectionStr = belt1.getOrientations().get(0);
                    switch (beltDirectionStr) {
                        case "up":
                            belt1Dir = 1;
                            break;
                        case "right":
                            belt1Dir = 2;
                            break;
                        case "down":
                            belt1Dir = 3;
                            break;
                        case "left":
                            belt1Dir = 4;
                            break;
                    }
                }
                int nextX = belt1.getX()+3;
                int nextY = belt1.getY();
                switch (belt1Dir) {
                    case 1:
                        nextY--;
                        break;
                    case 2:
                        nextX++;
                        break;
                    case 3:
                        nextY++;
                        break;
                    case 4:
                        nextX--;
                        break;
                }
                //         ---------------------------- the player moves (1/2) --------------------------------
                if (checkPit(nextX, nextY)) {
                    isRebooting = true;
                    pair = new Pair(-1, "rebooting");
                    // --> Pit
                }
                if(!isRebooting){
                    int blockedBeltX = -1;
                    int blockedBeltY = -1;
                    for (FieldObject belt2 : blueBelts) {
                        if (nextY == belt2.getY() && nextX == belt2.getX()+3
                                && nextX != blockedBeltX && nextY != blockedBeltY) {
                            if (belt2.getType().equals("RotatingBelt")) {
                                //if it is a rotating belt...
                                String rBelt2OrientationStr = belt2.getOrientations().get(0);
                                int rotatingBelt2Dir = -1;
                                switch (rBelt2OrientationStr) {
                                    case "up":
                                        rotatingBelt2Dir = 1;
                                        break;
                                    case "right":
                                        rotatingBelt2Dir = 2;
                                        break;
                                    case "down":
                                        rotatingBelt2Dir = 3;
                                        break;
                                    case "left":
                                        rotatingBelt2Dir = 4;
                                        break;
                                }
                                if (belt1Dir == rotatingBelt2Dir) {
                                    //          --- the player is not rotated, only moved (2/2) ---
                                    switch (rotatingBelt2Dir) {
                                        case 1:
                                            nextY--;
                                            break;
                                        case 2:
                                            nextX++;
                                            break;
                                        case 3:
                                            nextY++;
                                            break;
                                        case 4:
                                            nextX--;
                                            break;
                                    }
                                    blockedBeltX = nextX;
                                    blockedBeltY = nextY;
                                    for (FieldObject belt3 : blueBelts) {
                                        if (nextX == belt3.getX()+3 && nextY == belt3.getY()) {
                                            if (belt3.getType().equals("RotatingBelt")) {
                                                int desiredPosition = nextY * 13 + nextX;
                                                String rotationDirection3 = belt3.getOrientations().get(1);       //left, right
                                                pair = new Pair(desiredPosition, rotationDirection3);

                                            } else {
                                                int desiredPosition = 13 * nextY + nextX;
                                                pair = new Pair(desiredPosition, null);
                                                // --> RotatingBelt(no rotation) --> BlueBelt 2
                                            }
                                        }
                                    }
                                } else {
                                    //          --- the player is rotated and moved (2/2) ---
                                    switch (rotatingBelt2Dir) {
                                        case 1:
                                            nextY--;
                                            break;
                                        case 2:
                                            nextX++;
                                            break;
                                        case 3:
                                            nextY++;
                                            break;
                                        case 4:
                                            nextX--;
                                            break;
                                    }
                                    blockedBeltX = nextX;
                                    blockedBeltY = nextY;
                                    for (FieldObject belt3 : blueBelts) {
                                        if (nextX == belt3.getX()+3 && nextY == belt3.getY()) {
                                            if (belt3.getType().equals("RotatingBelt")) {
                                                int desiredPosition = nextY * 13 + nextX;
                                                pair = new Pair(desiredPosition, null);
                                                // --> RotatingBelt(with rotation) --> RotatingBelt(with rotation) 3
                                            } else {
                                                int desiredPosition = nextY * 13 + nextX;
                                                String rBelt2DirectionStr = belt2.getOrientations().get(1);   //left,right
                                                pair = new Pair(desiredPosition, rBelt2DirectionStr);
                                                // --> RotatingBelt(with rotation) --> BlueBelt 4
                                            }
                                        }
                                    }
                                }
                            } else if (belt2.getType().equals("Belt")) {
                                int bBelt2Orientation = calculateDirection(belt2.getOrientation());
                                switch (bBelt2Orientation) {
                                    case 1:
                                        nextY--;
                                        break;
                                    case 2:
                                        nextX++;
                                        break;
                                    case 3:
                                        nextY++;
                                        break;
                                    case 4:
                                        nextX--;
                                        break;
                                }
                                blockedBeltX = nextX;
                                blockedBeltY = nextY;
                                // if there is a pit --> reboot
                                if (checkPit(nextX, nextY)) {
                                    isRebooting = true;
                                    pair = new Pair(-1, "rebooting");
                                }
                                if (!isRebooting){
                                    for (FieldObject belt3 : blueBelts) {
                                        if (nextX == belt3.getX()+3 && nextY == belt3.getY()) {
                                            if (belt3.getType().equals("RotatingBelt")) {
                                                int belt2Orientation = calculateDirection(belt2.getOrientation());
                                                // belt2Orientation == belt3Orientation
                                                String rbelt3OrientationStr = belt3.getOrientations().get(0);
                                                int rbelt3Orientation = -1;
                                                switch (rbelt3OrientationStr) {
                                                    case "up":
                                                        rbelt3Orientation = 1;
                                                        break;
                                                    case "right":
                                                        rbelt3Orientation = 2;
                                                        break;
                                                    case "down":
                                                        rbelt3Orientation = 3;
                                                        break;
                                                    case "left":
                                                        rbelt3Orientation = 4;
                                                        break;
                                                }
                                                if (belt2Orientation == rbelt3Orientation) {
                                                    int desiredPosition = nextY * 13 + nextX;
                                                    pair = new Pair(desiredPosition, null);
                                                    // --> BlueBelt --> RotatingBelt(no rotation) 5
                                                } else {
                                                    int desiredPosition = nextY * 13 + nextX;
                                                    String rBelt3Direction = belt3.getOrientations().get(1);      //left, right
                                                    pair = new Pair(desiredPosition, rBelt3Direction);
                                                    // --> BlueBelt --> RotatingBelt(with rotation) 6
                                                }
                                            } else if (belt3.getType().equals("Belt")) {
                                                int desiredPosition = nextY * 13 + nextX;
                                                pair = new Pair(desiredPosition, null);
                                                // --> BlueBelt --> BlueBelt 7
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        Pair<Integer, Integer> result = new Pair<>(-1, -1);
        if (pair.getValue() == null) {   //no rotation --> currentOrientation stays the same
            result = new Pair(pair.getKey(), currentOrientation);
        } else if (pair.getValue().equals("left")) {
            if (currentOrientation == 1) {
                result = new Pair(pair.getKey(), 4);
            } else {
                result = new Pair(pair.getKey(), (currentOrientation - 1));
            }
        } else if (pair.getValue().equals("right")) {
            if (currentOrientation == 4) {
                result = new Pair(pair.getKey(), 1);
            } else {
                result = new Pair(pair.getKey(), (currentOrientation + 1));
            }
        }
        return result;
    }

    /**
     * checks if there is a pit at the next player position
     *
     * @param nextX xPosition of the field the player wants to go
     * @param nextY yPosition of the field the player wants to go
     * @return true, if there is a pit on that position
     * @author Vincent Oeller, Franziska Leitmeir, Vivien Pfeiffer
     */
    public boolean checkPit(int nextX, int nextY){
        for (FieldObject pit : pits){
            if(pit.getX()+3 == nextX && pit.getY() == nextY){
                return true;
            }
        }
        return false;
    }

    /**
     * calculateDirection():
     *
     * @param directionDeg the orientation of a FieldObject, -90,0,90,180
     * @return the direction as an int 1,2,3,4
     * @author Vincent Oeller
     */
    public int calculateDirection(double directionDeg) {
        switch ((int) directionDeg) {    //up=-90, right=0, down=90, left = 180
            case -90:
                return 1;
            case 0:
                return 2;
            case 90:
                return 3;
            case 180:
                return 4;
        }
        //this case happens if the function is called incorrectly
        return -1;
    }

//    public static void main(String args[]) {
//        QLearning qlearning = new QLearning((new ExtraCrispy()).returnGamefield(),105,31)
//
//        qlearning.printR();
//        qlearning.printQ();
//
//        qlearning.printPath();
//    }


}
