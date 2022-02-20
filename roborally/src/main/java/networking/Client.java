package networking;

import view.RoboRallyApplication;

/**
 * Client
 * This class represents a client of the server.
 */


public class Client extends User{
    public Client() {
        setGroup( "Kursive Kationen");
        setServerProtocol(1.0);
        RoboRallyApplication.player = this;
    }

}
