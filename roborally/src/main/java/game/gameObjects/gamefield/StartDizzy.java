package game.gameObjects.gamefield;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class StartDizzy {
    /**
     * returnStartfield():
     *
     * @return a JSONObject with the position and the action of each field of the startingBoard
     * @author Franziska Leitmeir
     */

    public static JSONObject returnStartfield() {
        JSONObject startingBoardDizzy = new JSONObject();

        JSONObject msgBody = new JSONObject();

        JSONArray map = new JSONArray();

        //row 1
        JSONObject pos0 = new JSONObject();
        JSONObject pos1 = new JSONObject();
        JSONObject pos2 = new JSONObject();

        //row 2
        JSONObject pos13 = new JSONObject();
        JSONObject pos14 = new JSONObject();
        JSONObject pos15 = new JSONObject();

        //row 3
        JSONObject pos26 = new JSONObject();
        JSONObject pos27 = new JSONObject();
        JSONObject pos28 = new JSONObject();

        //row 4
        JSONObject pos39 = new JSONObject();
        JSONObject pos40 = new JSONObject();
        JSONObject pos41 = new JSONObject();

        //row 5
        JSONObject pos52 = new JSONObject();
        JSONObject pos53 = new JSONObject();
        JSONObject pos54 = new JSONObject();

        //row 6
        JSONObject pos65 = new JSONObject();
        JSONObject pos66 = new JSONObject();
        JSONObject pos67 = new JSONObject();

        //row 7
        JSONObject pos78 = new JSONObject();
        JSONObject pos79 = new JSONObject();
        JSONObject pos80 = new JSONObject();

        //row 8
        JSONObject pos91 = new JSONObject();
        JSONObject pos92 = new JSONObject();
        JSONObject pos93 = new JSONObject();

        //row 9
        JSONObject pos104 = new JSONObject();
        JSONObject pos105 = new JSONObject();
        JSONObject pos106 = new JSONObject();

        //row 10
        JSONObject pos117 = new JSONObject();
        JSONObject pos118 = new JSONObject();
        JSONObject pos119 = new JSONObject();



        //row 1
        JSONObject field0 = new JSONObject();
        JSONObject field1 = new JSONObject();
        JSONObject field2 = new JSONObject();

        //row 2
        JSONObject field13 = new JSONObject();
        JSONObject field14 = new JSONObject();
        JSONObject field15 = new JSONObject();

        //row 3
        JSONObject field26 = new JSONObject();
        JSONObject field27 = new JSONObject();
        JSONObject field28 = new JSONObject();

        //row 4
        JSONObject field39 = new JSONObject();
        JSONObject field40 = new JSONObject();
        JSONObject field41 = new JSONObject();

        //row 5
        JSONObject field52 = new JSONObject();
        JSONObject field53 = new JSONObject();
        JSONObject field54 = new JSONObject();

        //row 6
        JSONObject field65 = new JSONObject();
        JSONObject field66 = new JSONObject();
        JSONObject field67 = new JSONObject();

        //row 7
        JSONObject field78 = new JSONObject();
        JSONObject field79 = new JSONObject();
        JSONObject field80 = new JSONObject();

        //row 8
        JSONObject field91 = new JSONObject();
        JSONObject field92 = new JSONObject();
        JSONObject field93 = new JSONObject();

        //row 9
        JSONObject field104 = new JSONObject();
        JSONObject field105 = new JSONObject();
        JSONObject field106 = new JSONObject();

        //row 10
        JSONObject field117 = new JSONObject();
        JSONObject field118 = new JSONObject();
        JSONObject field119 = new JSONObject();

        //row 1
        field0.put("type", "Empty");

        field1.put("type", "Empty");

        field2.put("type", "Belt");
        field2.put("orientation", "right");
        field2.put("speed", 1);

        //row 2
        field13.put("type", "Empty");

        field14.put("type", "StartingPoint");

        field15.put("type", "Empty");

        //row 3
        field26.put("type", "Empty");

        field27.put("type", "Wall");
        field27.put("orientation", "up");

        field28.put("type", "Empty");

        //row 4
        field39.put("type", "StartingPoint");

        field40.put("type", "Empty");

        field41.put("type", "Empty");

        //row 5
        field52.put("type", "PriorityAntenna");

        field53.put("type", "StartingPoint");

        field54.put("type", "Wall");
        field54.put("orientation", "right");

        //row 6
        field65.put("type", "Empty");

        field66.put("type", "StartingPoint");

        field67.put("type", "Wall");
        field67.put("orientation", "right");

        //row 7
        field78.put("type", "StartingPoint");

        field79.put("type", "Empty");

        field80.put("type", "Empty");

        //row 8
        field91.put("type", "Empty");

        field92.put("type", "Wall");
        field92.put("orientation", "down");

        field93.put("type", "Empty");

        //row 9
        field104.put("type", "Empty");

        field105.put("type", "StartingPoint");

        field106.put("type", "Empty");

        //row 10
        field117.put("type", "Empty");

        field118.put("type", "Empty");

        field119.put("type", "Belt");
        field119.put("orientation", "right");
        field119.put("speed", 1);

        //row 1
        pos0.put("position", 0);
        pos0.put("field", field0);

        pos1.put("position", 1);
        pos1.put("field", field1);

        pos2.put("position", 2);
        pos2.put("field", field2);

        //row 2
        pos13.put("position", 13);
        pos13.put("field", field13);

        pos14.put("position", 14);
        pos14.put("field", field14);

        pos15.put("position", 15);
        pos15.put("field", field15);

        //row 3
        pos26.put("position", 26);
        pos26.put("field", field26);

        pos27.put("position", 27);
        pos27.put("field", field27);

        pos28.put("position", 28);
        pos28.put("field", field28);

        //row 4
        pos39.put("position", 39);
        pos39.put("field", field39);

        pos40.put("position", 40);
        pos40.put("field", field40);

        pos41.put("position", 41);
        pos41.put("field", field41);

        //row 5
        pos52.put("position", 52);
        pos52.put("field", field52);

        pos53.put("position", 53);
        pos53.put("field", field53);

        pos54.put("position", 54);
        pos54.put("field", field54);

        //row 6
        pos65.put("position", 65);
        pos65.put("field", field65);

        pos66.put("position", 66);
        pos66.put("field", field66);

        pos67.put("position", 67);
        pos67.put("field", field67);

        //row 7
        pos78.put("position", 78);
        pos78.put("field", field78);

        pos79.put("position", 79);
        pos79.put("field", field79);

        pos80.put("position", 80);
        pos80.put("field", field80);

        //row 8
        pos91.put("position", 91);
        pos91.put("field", field91);

        pos92.put("position", 92);
        pos92.put("field", field92);

        pos93.put("position", 93);
        pos93.put("field", field93);

        //row 9
        pos104.put("position", 104);
        pos104.put("field", field104);

        pos105.put("position", 105);
        pos105.put("field", field105);

        pos106.put("position", 106);
        pos106.put("field", field106);

        //row 10
        pos117.put("position", 117);
        pos117.put("field", field117);

        pos118.put("position", 118);
        pos118.put("field", field118);

        pos119.put("position", 119);
        pos119.put("field", field119);


        map.add(pos0);
        map.add(pos1);
        map.add(pos2);
        map.add(pos13);
        map.add(pos14);
        map.add(pos15);
        map.add(pos26);
        map.add(pos27);
        map.add(pos28);
        map.add(pos39);
        map.add(pos40);
        map.add(pos41);
        map.add(pos52);
        map.add(pos53);
        map.add(pos54);
        map.add(pos65);
        map.add(pos66);
        map.add(pos67);
        map.add(pos78);
        map.add(pos79);
        map.add(pos80);
        map.add(pos91);
        map.add(pos92);
        map.add(pos93);
        map.add(pos104);
        map.add(pos105);
        map.add(pos106);
        map.add(pos117);
        map.add(pos118);
        map.add(pos119);


        msgBody.put("map", map);

        startingBoardDizzy.put("messageBody", msgBody);

        return startingBoardDizzy;

    }
}
