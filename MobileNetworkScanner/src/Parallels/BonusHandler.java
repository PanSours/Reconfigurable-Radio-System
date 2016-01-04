/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Parallels;

import MobileCore.SharedMemory;
import java.io.IOException;
import java.util.Vector;
import javax.microedition.io.Connector;
import javax.microedition.io.ServerSocketConnection;
import javax.microedition.io.SocketConnection;
import javax.microedition.io.StreamConnection;

public class BonusHandler extends Thread {

    StreamConnection sc;
    public boolean shouldRun;
    private Vector mobs;
    private SharedMemory sm;
    private ServerSocketConnection server;

    public BonusHandler(SharedMemory s) {

        sm = s;
        shouldRun = true;
        mobs = new Vector();
    }

    public void run() {

        try {
            server = (ServerSocketConnection) Connector.open("socket://localhost:8032");
            while (shouldRun) {
                sc = (SocketConnection) server.acceptAndOpen();
                MobileHandler mh = new MobileHandler(sc, sm);
                mobs.addElement(mh);
                mh.start();
            }
            for (int i = mobs.capacity() - 1; i > 0; i--) {
                MobileHandler mh2 = (MobileHandler) mobs.elementAt(i);
                mh2.shouldRun = false;
                mh2.join();

            }
            server.close();
        } catch (Exception e) {
            try {
                server.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

    }

}
