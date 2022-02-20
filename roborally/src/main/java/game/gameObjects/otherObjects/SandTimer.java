package game.gameObjects.otherObjects;

import networking.PlayerThread;
import org.json.simple.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

/**
 * SandTimer:
 * a 30 sec timer
 * @author Shady Mansour
 */
public class SandTimer extends Timer {
    private Timer timer;
    private PlayerThread player;
    private JSONObject msgBody;

    /**
     * startTimer():
     * starts a 30 sec timer and sends JSONObjects accordingly
     * @param player: the player who started the timer
     */
    public void startTimer(PlayerThread player) {
        this.player= player;
        this.timer = new Timer();
        msgBody = new JSONObject();
        player.getServer().broadcast( "TimerStarted",msgBody,null);

        timer.schedule(new RemindTask(),30000);

    }

    /**
     * cancel():
     * stops the timer
     */
    public void cancel(){
        timer.cancel();
        timer.purge();
    }

    /**
     * Task to be executed once timer ends
     */
    class RemindTask extends TimerTask {

        /**
         * run():
         * -is called after the time is up.
         * -Calls the method timerEnded() in Server
         *  to notify players that the time is up.
         * -Kills the timer Thread.
         */
        public void run() {
            player.getServer().timerEnded();
            timer.cancel();
            timer.purge();
            }
        }

}
