/*
 * This class basically implements the management of the messages that can be occurred from a Mobile Device.
 * To do that our class must communicate with classes such as Message and SharedMemory.
 */
package CognitivePilotChannel;

import java.io.*;
import java.net.Socket;
import java.sql.Connection;
import java.util.Vector;

public class MobileHandler extends Thread {

    Socket MobileSocket = null;
    public BufferedReader in;
    public OutputStream os;
    public Connection conn;
    private SharedMemory sm;

    public MobileHandler(Socket ms, Connection c, SharedMemory sms) {

        this.MobileSocket = ms;
        this.conn = c;
        this.sm = sms;
    }

    @Override
    public void run() {
        try {
            // Open input and output streams to communicate with client
            InputStream is = MobileSocket.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            in = new BufferedReader(isr);

            os = MobileSocket.getOutputStream();
            PrintWriter out = new PrintWriter(os, true);

            String input = "";
            String inputLine = "";

            char x;

            // Read input
            while ((x = (char) in.read()) != '%') {
                inputLine += x;
            }
            // Check Type of Message and respond properly
            Vector<BaseStationProfile> v = this.checkMessage(inputLine);
            if (v != null) {
                Profiles pr = new Profiles();
                String message = pr.toString(v);
                try {
                    os.write(message.getBytes());
                } catch (IOException ex) {
                    System.out.println("Unable to send " + message + " to Socket:" + MobileSocket);
                }
                System.out.println(message + " to Socket:" + MobileSocket);
            } else {
                os.write("PROFILES#%".getBytes());
                System.out.println("NONE to Socket:" + MobileSocket);
            }
        } catch (IOException e) {
            System.err.println("Problem with messages on Mobile with socket:" + MobileSocket);
            System.exit(1);
        }
        // Close connection and kill thread
        try {
            in.close();
            os.close();
            MobileSocket.close();
        } catch (IOException e) {
            System.err.println("Problem with closing MobileHandler thread!");
            System.exit(1);
        }
    }

    public Vector<BaseStationProfile> checkMessage(String me) {

        Message m = new Message();

        try {
            m = m.fromString(me);
            if (m.Message_Type.equals("DISC")) {
                Discover d = new Discover();
                d = (Discover) m;
                return this.sm.search(d.getX(), d.getY());
            } else {
                return null;
            }

        } catch (Exception e) {
            System.err.println("Check Time... Could not listen");
        }

        return null;

    }

}
