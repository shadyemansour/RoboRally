package game.gameObjects.gamefield;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;

/**
 * ExtractJSON includes all necessary methods to extract the information from the JSONObject
 */
public class ExtractJSON {

    private static JSONObject msgBody;
    private static JSONArray map;

    /**
     * Contructor:
     *
      * @param file: JSONObject that should be extracted
     */
    public ExtractJSON (JSONObject file) {

        //extract the messageBody
        this.msgBody = (JSONObject) file.get("messageBody");

        //extract the map
        this.map = (JSONArray) msgBody.get("map");
    }

    /**
     * extractMap():
     * extracts a element by the index of the JSONArray and read information from the JSON
     *
     * @param index: position of the element that wants to be accessed
     * @return returns the FieldObject with all information
     * @author Franziska Leitmeir
     */
        public static FieldObject extractMap(int index){
            FieldObject fieldObject = null;
            JSONObject currentField = (JSONObject) map.get(index);

            //if the NextField is an Object (field with only one type)
                if (checkNextField(index)) {
                    JSONObject fieldObj = (JSONObject) currentField.get("field");
                    fieldObject = getJSONFieldObject(index, fieldObj);

                } else {
                    //if the NextField is an Array (field with more than one type)
                    JSONArray fieldArray = (JSONArray) currentField.get("field");

                    int length = fieldArray.size();

                    for (int i=0; i< length; i++) {

                        JSONObject arrayField = (JSONObject) fieldArray.get(i);
                        fieldObject = getJSONFieldObject(index, arrayField);
                        fieldObject.setType(checkTyp(fieldArray));
                        if (length ==3) {
                            JSONObject checkPoint = (JSONObject) fieldArray.get(2);
                            int counter = getCount(checkPoint);
                            fieldObject.setCounter(counter);
                        }
                        return fieldObject;
                    }
                }
            return fieldObject;
    }

    /**
     * getJSONPosition():
     * gets the "position" of an element in the JSONArray map
     *
     * @param index: the index of the element that is being accessed
     * @return the position of the Object as integer value
     * @author Franziska Leitmeir
     */
    public static int getJSONPosition(int index) {
        JSONObject currentField = (JSONObject) map.get(index);
        int positionInteger = Integer.parseInt(currentField.get("position").toString());
        return positionInteger;
    }

    /**
     * checkNextField():
     * checks if in the next JSONObject of the JSONArray map the Object with
     * the key "field" a JSONObject or a JSONArray is
     *
     * @param index: the position of the JSONObject that needs to be checked out
     * @return true if its JSONObject, false if its a JSONArray
     * @author Franziska Leitmeir
     */
    public static boolean checkNextField(int index) {
        JSONObject currentField = (JSONObject) map.get(index);
        Object field = currentField.get("field");
        boolean isObject = false;
        if (field instanceof JSONObject) {
            isObject = true;
        }
        return isObject;
    }

    /**
     * getJSONFieldObject():
     * filling the FieldObject instance with all necessary parameters
     *
     * @param index: the position of the JSONObject that needs to be transformed in a FieldObject
     * @return FieldObject with all parameters from JSONObject
     * @author Franziska Leitmeir
     */
    public static FieldObject getJSONFieldObject(int index, JSONObject field) {

        int x;
        int y;
        String type;
        int speed;
        double orientation;
        ArrayList<String> orientations = new ArrayList<>();
        int count;
        boolean isCrossing;

        //X-Value
        x = getXPosition(getJSONPosition(index));

        //Y-Value
        y = getYPosition(getJSONPosition(index));

        //type-Value
        type = getType(field);

        //if speed-Value or 0
        if(field.containsKey("speed")){
            speed = getSpeed(field);
        } else speed = 0;

        //if orientation-Value or 0
        if (field.containsKey("orientation")) {
            orientation = getOrientation(field);
        } else orientation = 0;

        //if orientations-Value and orientation-Value or null
        if (field.containsKey ("orientations")){
            orientations = (getOrientations(field));
            orientation = (getOrientation2(field));
        } else orientations = null;

        //if count-Value or 0
        if (field.containsKey("count")) {
            count = getCount(field);
        } else count = 0;

        //if isCrossing-Value or false
        if (field.containsKey ("isCrossing")) {
            isCrossing = getIsCrossing(field);
        }else isCrossing = false;


        FieldObject pField = new FieldObject(x, y, type, speed, orientation, orientations, count, isCrossing);

        return pField;

    }

    /**
     * checkType():
     * checks the Array for one field and selects a new type for this field
     *
     * @param jsonArray: of the field that should be checked
     * @return the new type of this field
     * @author Franziska Leitmeir
     */
    public static String checkTyp (JSONArray jsonArray) {
        int length = jsonArray.size();
        String type = "";
        JSONObject pos1 = (JSONObject) jsonArray.get(0);
        JSONObject pos2 = (JSONObject) jsonArray.get(1);
        if (length == 2) {
            if (pos1.get("type").equals("Wall") && pos2.get("type").equals("Laser") ||
                    pos1.get("type").equals("Laser") && pos2.get("type").equals("Wall")) {
                type = "LaserWall";
            }

            if (pos1.get("type").equals("EnergyCube") && pos2.get("type").equals("Wall") ||
                    pos1.get("type").equals("Wall") && pos2.get("type").equals("EnergyCube")) {
                type = "EnergySpaceWall";
            }

        }else if (length == 3) {
                JSONObject pos3 = (JSONObject) jsonArray.get(2);
                if (pos1.get("type").equals("Wall") && pos2.get("type").equals("Laser") && pos3.get("type").equals("ControlPoint") ||
                        pos1.get("type").equals("Laser") && pos2.get("type").equals("Wall") && pos3.get("type").equals("ControlPoint")||
                        pos1.get("type").equals("Laser") && pos2.get("type").equals("ControlPoint") && pos3.get("type").equals("Wall")||
                        pos1.get("type").equals("Wall") && pos2.get("type").equals("ControlPoint") && pos3.get("type").equals("Laser")||
                        pos1.get("type").equals("ControlPoint") && pos2.get("type").equals("Wall") && pos3.get("type").equals("Laser")||
                        pos1.get("type").equals("ControlPoint") && pos2.get("type").equals("Laser") && pos3.get("type").equals("Wall"))  {
                    type = "ControlPointWallLaser";
                }
        }
        return type;
    }

    /**
     * getType():
     * access the "type"-key of the field
     *
     * @param field: current field
     * @return String with the type of the current field
     * @author Franziska Leitmeir
     */
    public static String getType(JSONObject field){
        String type = field.get("type").toString();
        return type;
    }

    /**
     * getOrientations():
     * access the "orientations"-key of the field
     *
     * @param field: current field
     * @return ArrayList with the orientations
     * @author Franziska Leitmeir
     */
    public static ArrayList<String> getOrientations(JSONObject field) {
        ArrayList<String> orientations = new ArrayList<>();
        JSONArray o = (JSONArray) field.get("orientations");
        for (int i = 0; i < o.size(); i++) {
            orientations.add(o.get(i).toString());
        }
        return orientations;
    }

    /**
     * getOrientation2():
     * converts the "orientations"-key into a orientation for the GUI
     *
     * @param field: current field
     * @return double value that will be used for the Rotation of the image
     * @author Franziska Leitmeir
     */
    public static double getOrientation2(JSONObject field) {
        double rotation = 0;
        ArrayList<String> orientations = new ArrayList<>();
        JSONArray o = (JSONArray) field.get("orientations");
        orientations.add(o.get(0).toString());
        orientations.add(o.get(1).toString());
        if (o.get(0).equals("left") && o.get(1).equals("right") ||
                o.get(0).equals("left") && o.get(1).equals("left")) {
            rotation = 180;
        } else if (o.get(0).equals("down") && o.get(1).equals("left") ||
                o.get(0).equals("down") && o.get(1).equals("right")) {
            rotation = 90;
        }
        else if (o.get(0).equals("up") && o.get(1).equals("right") ||
                o.get(0).equals("up") && o.get(1).equals("left") ) {
            rotation = -90;
        }
        else rotation = 0;
        return rotation;
    }

    /**
     * getOrientation():
     * access the "orientation"-key of the field and converts it to a double value
     *
     * @param field: current field
     * @return Double value that will be used for the Rotation of the image
     * @author Franziska Leitmeir
     */
    public static double getOrientation(JSONObject field) {
        double rotation = 0;
        String orientation = field.get("orientation").toString();
        switch (orientation) {
            case "right": rotation = 0;
                break;
            case "down": rotation = 90;
                break;
            case "left": rotation = 180;
                break;
            case "up": rotation = -90;
                break;
        }
        return rotation;
    }

    /**
     * getSpeed():
     * access the "speed"-key of the field
     *
     * @param field: current field
     * @return integer of the speed
     * @author Franziska Leitmeir
     */
    public static int getSpeed(JSONObject field) {
        int speed = Integer.parseInt(field.get("speed").toString());
        return speed;
    }

    /**
     * getCount():
     * access the "count"-key of the field
     *
     * @param field: current field
     * @return integer of the counter
     * @author Franziska Leitmeir
     */
    public static int getCount(JSONObject field) {
        int count = Integer.parseInt(field.get("count").toString());
        return count;
    }

    /**
     * getIsCrossing():
     * access the "isCrossing"-key of the field
     *
     * @param field: current field
     * @return boolean of the isCrossing
     * @author Franziska Leitmeir
     */
    public static boolean getIsCrossing(JSONObject field) {
        boolean isCrossing;
        String crossing = field.get("isCrossing").toString();
        if (crossing == "true") {
            isCrossing = true;
        }
            else isCrossing = false;

        return isCrossing;
    }

    /**
     * getXPosition():
     * gets the position of a field and converts it to the x-Value for the GUI-Grid
     *
     * @param position: the "position"-key of a field as integer value
     * @return integer of the x-Position
     * @author Franziska Leitmeir
     */
    public static int getXPosition(int position) {
        int x;
       if (position <=12) {
           x = position -3;
        }
       else if (position <= 25) {
           x = position -16;
       }
       else if (position <= 38) {
           x = position - 29;
       }
       else if (position <= 51) {
           x = position - 42;
       }
       else if (position <= 64) {
           x = position - 55;
       }
       else if (position <= 77) {
           x = position - 68;
       }
       else if (position <= 90) {
           x = position - 81;
       }
       else if (position <= 103) {
           x = position - 94;
       }
       else if (position <= 116) {
           x = position - 107;
       }
       else x = position - 120;

       return x;
    }

    /**
     * getXPosition():
     * gets the position of a field and converts it to the y-Value for the GUI-Grid
     *
     * @param position: the "position"-key of a field as integer value
     * @return integer of the y-Position
     * @author Franziska Leitmeir
     */
    public static int getYPosition(int position) {
    int y;

        if (position <=12) {
            y = 0;
        }
        else if (position <= 25) {
            y = 1;
        }
        else if (position <= 38) {
            y = 2;
        }
        else if (position <= 51) {
            y = 3;
        }
        else if (position <= 64) {
            y = 4;
        }
        else if (position <= 77) {
            y = 5;
        }
        else if (position <= 90) {
            y = 6;
        }
        else if (position <= 103) {
            y = 7;
        }
        else if (position <= 116) {
            y = 8;
        }
        else y = 9;

        return y;
    }

}

