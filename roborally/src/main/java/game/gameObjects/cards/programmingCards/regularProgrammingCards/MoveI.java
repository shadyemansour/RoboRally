package game.gameObjects.cards.programmingCards.regularProgrammingCards;

import game.gameObjects.cards.Card;
import networking.PlayerThread;
import game.gameObjects.robots.Robot;
import game.gameLogic.Game;


/**
 * MoveI:
 * this class represents the card MoveOne in the game
 */
public class MoveI extends Card implements ProgrammingCard {
    /**
     * effect():
     * applies the effect of the card
     *
     * @param player: PlayerThread of the player who played the card
     * @param robot:  the robot on which the effect should be applied
     * @param round : current round
     * @author Vincent Oeller, Franziska Leitmeir, Vivien Pfeiffer
     */
    @Override
    public void effect(PlayerThread player, Robot robot, int round) {
        robot.setCanMove(true);     //doppelt ?
        robot.setTaken(false);
        int orientation = robot.getOrientation();       //1=norden, 2=osten, 3=sueden, 4=westen
        int currentX = robot.getxPosition();
        int currentY = robot.getyPosition();
        int newPosition;
        Game game = player.getServer().getGame();
        if(orientation == 1){
            int nextX = currentX;
            int nextY = currentY-1;
            if (game.checkWall(currentX,currentY,nextX,nextY,orientation)){
                //man bewegt sich nicht, verschickt auch keine JSON Datei
                player.getLogger().debug("Found Wall. Can't move forward");
            } else if (game.checkRobot(nextX, nextY, orientation,player)) {
                if(!robot.isTaken()){
                    currentY--;
                    //eigene Position updaten und JSONDatei verschicken
                    robot.setxPosition(currentX);
                    robot.setyPosition(currentY);
                    newPosition = currentY*13 + currentX;
                    player.move(player.getUserId(),newPosition);
                    player.getLogger().debug("Player " + player.getUserId() + " moved forward to " + newPosition);
                }
            } else if(game.checkMapEnd(nextX,nextY) || game.checkPit(nextX,nextY)) {          //oben von der map oder in Pit gefallen
                player.getLogger().debug("Player " + player.getUserId() + " moved forward and fell outside the map or in a pit");
                game.rebootRobot(robot, player);

            } else{
                if(robot.getCanMove()){
                    player.getLogger().debug("Player " + player.getUserId() + " can move.");
                    currentY--;
                    //eigene Position updaten und JSONDatei verschicken
                    robot.setxPosition(currentX);
                    robot.setyPosition(currentY);
                    newPosition = currentY*13 + currentX;
                    player.move(player.getUserId(),newPosition);
                    player.getLogger().debug("Player " + player.getUserId() + " moved forward to " + newPosition);

                }
                robot.setCanMove(true);
            }

        } else if(orientation == 2){
            int nextX = currentX+1;
            int nextY = currentY;
            if (game.checkWall(currentX,currentY,nextX,nextY,orientation)) {
                //man bewegt sich nicht
                player.getLogger().debug("Found Wall. Can't move forward");
            } else if (game.checkRobot(nextX, nextY, orientation,player)) {
                if(!robot.isTaken()){
                    currentX++;
                //eigene Position updaten und JSONDatei verschicken
                robot.setxPosition(currentX);
                robot.setyPosition(currentY);
                newPosition = currentY*13 + currentX;
                player.move(player.getUserId(),newPosition);
                player.getLogger().debug("Player " + player.getUserId() + " moved forward to " + newPosition);
                }
            } else if(game.checkMapEnd(nextX,nextY) || game.checkPit(nextX,nextY)){          //oben von der map oder in Pit gefallen
                player.getLogger().debug("Player " + player.getUserId() + " moved forward and fell outside the map or in a pit");
                game.rebootRobot(robot, player);
            }else{
                if(robot.getCanMove()){
                    player.getLogger().debug("Player " + player.getUserId() + " can move.");
                    currentX++;
                    //eigene Position updaten und JSONDatei verschicken
                    robot.setxPosition(currentX);
                    robot.setyPosition(currentY);
                    newPosition = currentY*13 + currentX;
                    player.move(player.getUserId(),newPosition);
                    player.getLogger().debug("Player " + player.getUserId() + " moved forward to " + newPosition);

                }
                robot.setCanMove(true);
            }

        } else if(orientation == 3){
            int nextX = currentX;
            int nextY = currentY+1;
            if (game.checkWall(currentX,currentY,nextX,nextY,orientation)){
                //man bewegt sich nicht
                player.getLogger().debug("Found Wall. Can't move forward");
            }else if (game.checkRobot(nextX, nextY, orientation,player)) {
                if(!robot.isTaken()){
                    currentY++;
                    //eigene Position updaten und JSONDatei verschicken
                    robot.setxPosition(currentX);
                    robot.setyPosition(currentY);
                    newPosition = currentY*13 + currentX;
                    player.move(player.getUserId(),newPosition);
                    player.getLogger().debug("Player " + player.getUserId() + " moved forward to " + newPosition);
                }
            } else if(game.checkMapEnd(nextX,nextY) || game.checkPit(nextX,nextY)){          //oben von der map oder in Pit gefallen
                player.getLogger().debug("Player " + player.getUserId() + " moved forward and fell outside the map or in a pit");
                game.rebootRobot(robot, player);
            }else{
                if(robot.getCanMove()){
                    player.getLogger().debug("Player " + player.getUserId() + " can move.");
                    currentY++;
                    //eigene Position updaten und JSONDatei verschicken
                    robot.setxPosition(currentX);
                    robot.setyPosition(currentY);
                    newPosition = currentY*13 + currentX;
                    player.move(player.getUserId(),newPosition);
                    player.getLogger().debug("Player " + player.getUserId() + " moved forward to " + newPosition);

                }
                robot.setCanMove(true);
            }

        }else if(orientation == 4) {
            int nextX = currentX-1;
            int nextY = currentY;
            if (game.checkWall(currentX, currentY, nextX, nextY, orientation)) {
                //man bewegt sich nicht
                player.getLogger().debug("Found Wall. Can't move forward");
            } else if (game.checkRobot(nextX, nextY, orientation,player)) {
                if(!robot.isTaken()){
                    currentX--;
                    //updating own Position and send JSONfile
                    robot.setxPosition(currentX);
                    robot.setyPosition(currentY);
                    newPosition = currentY*13 + currentX;
                    player.move(player.getUserId(),newPosition);
                    player.getLogger().debug("Player " + player.getUserId() + " moved forward to " + newPosition);
                }
            } else if (game.checkMapEnd(nextX, nextY) || game.checkPit(nextX, nextY)) {          //oben von der map oder in Pit gefallen
                player.getLogger().debug("Player " + player.getUserId() + "moved forward and fell outside the map or in a pit");
                game.rebootRobot(robot, player);
            } else {
                if(robot.getCanMove() == true ){
                    player.getLogger().debug("Player " + player.getUserId() + " can move.");
                    currentX--;
                    //updating own Position and send JSONfile
                    robot.setxPosition(currentX);
                    robot.setyPosition(currentY);
                    newPosition = currentY*13 + currentX;
                    player.move(player.getUserId(),newPosition);
                    player.getLogger().debug("Player " + player.getUserId() + " moved forward to " + newPosition);

                }
                robot.setCanMove(true);
            }
        }
        player.getLogger().info("Executed programming card "+ toString() + "'s effect");

    }

    /**
     * toString():
     *
     * @return name of the card as String
     */
    public String toString(){
        return "MoveI";
    }
}
