package game.gameObjects.cards.programmingCards.regularProgrammingCards;

import game.gameLogic.Game;
import game.gameObjects.cards.Card;
import networking.PlayerThread;
import game.gameObjects.robots.Robot;

/**
 * BackUp:
 * this class represents the card Backup in the game
 */
public class BackUp extends Card implements ProgrammingCard {
    /**
     * effect():
     * applies the effect of the card
     *
     * @param player: PlayerThread of the player who played the card
     * @param robot: the robot on which the effect should be applied
     * @param round : current round
     * @author Vincent Oeller, Franziska Leitmeir, Vivien Pfeiffer
     */
    @Override
    public void effect(PlayerThread player, Robot robot, int round ) {
        robot.setCanMove(true);
        robot.setTaken(false);
        int originialOrientation = robot.getOrientation();       //1=norden, 2=osten, 3=sueden, 4=westen
        int orientation = -1;
        if(originialOrientation == 1){
            orientation = 3;
        }else if(originialOrientation == 2){
            orientation = 4;
        }else if(originialOrientation == 3) {
            orientation = 1;
        }else if(originialOrientation == 4){
            orientation = 2;
        }

        int currentX = robot.getxPosition();
        int currentY = robot.getyPosition();
        int newPosition;
        Game game = player.getServer().getGame();
        if(orientation == 1){
            int nextX = currentX;
            int nextY = currentY-1;
            if (game.checkWall(currentX,currentY,nextX,nextY,orientation)){
                player.getLogger().debug("Found Wall. Can't Move Back");
                //man bewegt sich nicht, verschickt auch keine JSON Datei ?
            } else if (game.checkRobot(nextX, nextY, orientation,player)) {
                if(!robot.isTaken()){
                    currentY--;
                    //eigene Position updaten und JSONDatei verschicken
                    robot.setxPosition(currentX);
                    robot.setyPosition(currentY);
                    newPosition = currentY*13 + currentX;
                    player.move(player.getUserId(),newPosition);
                    player.getLogger().debug("Player " + player.getUserId() + " moved back to " + newPosition);
                }
            } else if(game.checkMapEnd(nextX,nextY) || game.checkPit(nextX,nextY)) {          //oben von der map oder in Pit gefallen
                game.rebootRobot(robot, player);
                player.getLogger().debug("Player " + player.getUserId() + " moved back and fell outside the map or in a pit");
            } else{
                if(robot.getCanMove()){
                    player.getLogger().debug("Player " + player.getUserId() + " can move.");
                    currentY--;
                    //eigene Position updaten und JSONDatei verschicken
                    robot.setxPosition(currentX);
                    robot.setyPosition(currentY);
                    newPosition = currentY*13 + currentX;
                    player.move(player.getUserId(),newPosition);
                    player.getLogger().debug("Player " + player.getUserId() + " moved back to " + newPosition);
                }
            }

        } else if(orientation == 2){
            int nextX = currentX+1;
            int nextY = currentY;
            if (game.checkWall(currentX,currentY,nextX,nextY,orientation)) {
                //man bewegt sich nicht
                player.getLogger().debug("Found Wall. Can't Move Back");
            } else if (game.checkRobot(nextX, nextY, orientation,player)) {
                if(!robot.isTaken()){
                    currentX++;
                    //eigene Position updaten und JSONDatei verschicken
                    robot.setxPosition(currentX);
                    robot.setyPosition(currentY);
                    newPosition = currentY*13 + currentX;
                    player.move(player.getUserId(),newPosition);
                    player.getLogger().debug("Player " + player.getUserId() + " moved back to " + newPosition);
                }
            } else if(game.checkMapEnd(nextX,nextY) || game.checkPit(nextX,nextY)){          //oben von der map oder in Pit gefallen
                game.rebootRobot(robot, player);
                player.getLogger().debug("Player " + player.getUserId() + " moved back and fell outside the map or in a pit");
            }else{
                if(robot.getCanMove()){
                    player.getLogger().debug("Player " + player.getUserId() + " can move.");

                    currentX++;
                    //eigene Position updaten und JSONDatei verschicken
                    robot.setxPosition(currentX);
                    robot.setyPosition(currentY);
                    newPosition = currentY*13 + currentX;
                    player.move(player.getUserId(),newPosition);
                    player.getLogger().debug("Player " + player.getUserId() + " moved back to " + newPosition);
                }
            }

        } else if(orientation == 3){
            int nextX = currentX;
            int nextY = currentY+1;
            if (game.checkWall(currentX,currentY,nextX,nextY,orientation)){
                //man bewegt sich nicht
                player.getLogger().debug("Found Wall. Can't Move Back");
            }else if (game.checkRobot(nextX, nextY, orientation,player)) {
                if(!robot.isTaken()){
                    currentY++;
                    //eigene Position updaten und JSONDatei verschicken
                    robot.setxPosition(currentX);
                    robot.setyPosition(currentY);
                    newPosition = currentY*13 + currentX;
                    player.move(player.getUserId(),newPosition);
                    player.getLogger().debug("Player " + player.getUserId() + " moved back to " + newPosition);
                }
            } else if(game.checkMapEnd(nextX,nextY) || game.checkPit(nextX,nextY)){          //oben von der map oder in Pit gefallen
                game.rebootRobot(robot, player);
                player.getLogger().debug("Player " + player.getUserId() + " moved back and fell outside the map or in a pit");

            }else{
                if(robot.getCanMove()){
                    player.getLogger().debug("Player " + player.getUserId() + " can move.");

                    currentY++;
                    //eigene Position updaten und JSONDatei verschicken
                    robot.setxPosition(currentX);
                    robot.setyPosition(currentY);
                    newPosition = currentY*13 + currentX;
                    player.move(player.getUserId(),newPosition);
                    player.getLogger().debug("Player " + player.getUserId() + " moved back to " + newPosition);
                }
            }

        }else if(orientation == 4) {
            int nextX = currentX-1;
            int nextY = currentY;
            if (game.checkWall(currentX, currentY, nextX, nextY, orientation)) {
                //man bewegt sich nicht
                player.getLogger().debug("Found Wall. Can't Move Back");
            } else if (game.checkRobot(nextX, nextY, orientation,player)) {
                if(!robot.isTaken()){
                    currentX--;
                    //eigene Position updaten und JSONDatei verschicken
                    robot.setxPosition(currentX);
                    robot.setyPosition(currentY);
                    newPosition = currentY*13 + currentX;
                    player.move(player.getUserId(),newPosition);
                      player.getLogger().debug("Player " + player.getUserId() + " moved back to " + newPosition);
                }
            } else if (game.checkMapEnd(nextX, nextY) || game.checkPit(nextX, nextY)) {          //oben von der map oder in Pit gefallen
                game.rebootRobot(robot, player);
                player.getLogger().debug("Player " + player.getUserId() + " moved back and fell outside the map or in a pit");
            } else {
                if(robot.getCanMove()){
                    player.getLogger().debug("Player " + player.getUserId() + "can move.");
                    currentX--;
                    //eigene Position updaten und JSONDatei verschicken
                    robot.setxPosition(currentX);
                    robot.setyPosition(currentY);
                    newPosition = currentY*13 + currentX;
                    player.move(player.getUserId(),newPosition);
                    player.getLogger().debug("Player " + player.getUserId() + " moved back to " + newPosition);
                }
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
        return "BackUp";
    }
}
