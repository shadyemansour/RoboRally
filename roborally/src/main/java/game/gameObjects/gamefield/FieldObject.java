package game.gameObjects.gamefield;

import java.util.ArrayList;

/**
 * Object of a Field
 * This class represents a Field in the Gamemap.
 * The values come from the JSONObject.
 * @author Franziska Leitmeir
 */

public class FieldObject {
    int x;
    int y;
    String type;
    int speed;
    double orientation;
    ArrayList<String> orientations;
    int counter;
    boolean isCrossing;

    /**
     * Constructor:
     *
     * @param x: X-Position in the GridPane
     * @param y: Y-Position in the GridPane
     * @param type: Type of the field (e.g. "Empty", "Belt",...)
     * @param speed: Speed of a Belt or RotatingBelt (to choose between green or blue Arrows on the Belt)
     * @param orientation: for rotating the image in the right direction
     * @param orientations: for rotating and selecting the right RotatingBelt because of two Values that are transferred
     * @param counter: number of lasers ore energy cubes on the EnergySpace
     * @param isCrossing: false if the RotatingBelt is a curve, true if its a crossing
     */
    public FieldObject(int x, int y, String type, int speed, double orientation, ArrayList<String> orientations, int counter, boolean isCrossing){
        this.x = x;
        this.y = y;
        this.type = type;
        this.speed = speed;
        this.orientation = orientation;
        this.orientations = orientations;
        this.counter = counter;
        this.isCrossing = isCrossing;
    }

    /**
     * Constructor:
     *
     * @param x: X-Position in the GridPane
     * @param y: Y-Position in the GridPane
     * @param type: Type of the field (e.g. "Empty", "Belt",...)
     */
    public FieldObject(int x, int y,String type){
        this.x = x;
        this.y = y;
        this.type = type;
    }

    /**
     * getX():
     * Getter of the X-Value
     * @return x-Value of the field Object
     */
    public int getX(){
        return x;
    }

    /**
     * setX():
     * Setter of the X-Value
     * @param x: new x-Value
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * getY():
     * Getter of the Y-Value
     * @return y-Value of the field Object
     */
    public int getY(){
        return y;
    }

    /**
     * getType():
     * Getter of the Type
     * @return type of the field Object
     */
    public String getType(){
        return type;
    }

    /**
     * setType():
     * Setter of the Type
     * @param type: new type-Value
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * getSpeed():
     * Getter of the Speed-Value
     * @return speed-Value of the field Object
     */
    public int getSpeed(){
        return speed;
    }

    /**
     * getOrientation():
     * Getter of the Orientation-Value
     * @return orientation-Value of the field Object
     */
    public double getOrientation(){
        return orientation;
    }

    /**
     * setOrientation():
     * Setter of the Orientation-Value
     * @param orientation: new orientation-Value
     */
    public void setOrientation(double orientation){
        this.orientation = orientation;
    }

    /**
     * getOrientations():
     * Getter of the Orientations-Value
     * @return List with the Orientations-Values of the field Object
     */
    public ArrayList<String> getOrientations(){
        return orientations;
    }

    /**
     * getCounter():
     * Getter of the Counter-Value
     * @return counter-Value of the field Object
     */
    public int getCounter(){
        return counter;
    }

    /**
     * setCounter():
     * Setter of the Counter-Value
     * @param counter: new counter-Value
     */
    public void setCounter(int counter) {
        this.counter = counter;
    }


    /**
     * getIsCrossing():
     * Getter of the isCrossing-Value
     * @return isCrossing-Value of the field Object
     */
    public boolean getIsCrossing(){
        return isCrossing;
    }

}


