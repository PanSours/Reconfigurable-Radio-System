/*
 * This class basically implements the management of the messages that can be occurred from a Mobile Device.To do
 * that our class must communicate with classes such as Message and BaseStation.
 */
package basestation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class MobileHandler extends Thread {

    Socket MobileSocket = null;
    int X;
    int Y;
    String IMEI;
    String IMSI;
    public String Type;
    public BufferedReader in;
    public OutputStream os;
    public boolean shouldRun;
    BaseStationHandler bs;

    public MobileHandler(Socket ms, BaseStationHandler bs) {

        this.MobileSocket = ms;
        this.X = 0;
        this.Y = 0;
        this.bs = bs;
        this.Type = "UNKNOWN";
        this.IMEI = "UNKNOWN";
        this.IMSI = "UNKNOWN";
        this.shouldRun = true;
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

            while (this.shouldRun) {

                String input = "";
                String inputLine = "";

                char x;

                if (in.ready()) {
                    // Read input 
                    while ((x = (char) in.read()) != '%') {
                        inputLine += x;
                    }

                    String[] subMessages;
                    // Split messages if needed    
                    subMessages = inputLine.split("%");
                    subMessages[subMessages.length - 1].replace("$", "");

                    for (int i = 0; i < subMessages.length; i++) {
                        // Check Type of Message and respond properly
                        int flag = this.checkMessage(subMessages[i]);

                        if (flag == 1) {
                            OK ok = new OK();
                            String message = ok.toString();
                            os.write(message.getBytes());
                            System.out.println("OK to Socket:" + MobileSocket);
                            this.Type = "CONNECTED";
                            this.bs.MCounts++;
                            this.bs.Load_Level = this.bs.MCounts * 5;
                            //g.printOutput("OK to Socket:"+ MobileSocket);
                            this.bs.fm.UpdateMobiles(this.bs.mt);
                            this.bs.fm.updateProperties(this.bs.properties, this.bs.Load_Level);
                        } else if (flag == 2) {
                            OverLoaded ol = new OverLoaded();
                            String message = ol.toString();
                            os.write(message.getBytes());
                            System.out.println("OVERLOADED to Socket:" + MobileSocket);
                            //g.printOutput("OVERLOADED to Socket:"+ MobileSocket);
                            this.Type = "OVERLOADED";
                            this.bs.fm.updateProperties(this.bs.properties, this.bs.Load_Level);
                            this.bs.fm.UpdateMobiles(this.bs.mt);
                            this.shouldRun = false;
                        } else if (flag == 3) {
                            NoCoverage nc = new NoCoverage();
                            String message = nc.toString();
                            os.write(message.getBytes());
                            System.out.println("NO COVERAGE to Socket:" + MobileSocket);
                            //g.printOutput("NO COVERAGE to Socket:"+ MobileSocket);
                            this.Type = "NO COVERAGE";
                            this.bs.fm.UpdateMobiles(this.bs.mt);
                            this.bs.fm.updateProperties(this.bs.properties, this.bs.Load_Level);
                            this.shouldRun = false;
                        } else if (flag == 4) {
                            Disconnect dc = new Disconnect();
                            String message = dc.toString();
                            os.write(message.getBytes());
                            System.out.println("DISCONNECT to Socket:" + MobileSocket);
                            //g.printOutput("DISCONNECT to Socket:"+ MobileSocket);                        
                            this.Type = "DISCONNECTED";
                            this.bs.MCounts--;
                            this.bs.Load_Level = this.bs.MCounts * 5;
                            this.bs.fm.UpdateMobiles(this.bs.mt);
                            this.bs.fm.updateProperties(this.bs.properties, this.bs.Load_Level);
                            this.shouldRun = false;
                        } else {
                            os.write("UNKNOWN-WRONG MESSAGE".getBytes());
                            System.out.println("BULLSHIT to Socket:" + MobileSocket);
                            this.bs.fm.UpdateMobiles(this.bs.mt);
                            this.bs.fm.updateProperties(this.bs.properties, this.bs.Load_Level);
                            this.shouldRun = false;
                        }

                    }

                } // Input stream empty, sleep for 100 ms
                else {
                    try {
                        this.sleep(100);
                    } catch (InterruptedException e) {
                        System.err.println("Mobile Handler Interrupted");
                        System.exit(1);
                    }
                }
            }

        } catch (IOException e) {
            System.err.println("Problem with messages on Mobile with socket:" + MobileSocket);
            System.exit(1);
        }
        // Close connection and kill thread
        try {
            this.Type = "DISCONNECTED";
            os.write("DISCONNECT%".getBytes());
            in.close();
            os.close();
            MobileSocket.close();
        } catch (IOException e) {
            System.err.println("Problem with closing MobileHandler thread!");
            System.exit(1);
        }
    }

    public int checkMessage(String sm) {

        Message m = new Message();

        try {

            m = m.fromString(sm);

            if (m.Message_Type.equals("C")) {

                boolean flag;

                Connect c = (Connect) m;
                //Position check
                flag = c.checkCoordinates(this.bs.X, this.bs.Y, this.bs.Radius_Coverage);
                if (flag == true) {
                    //Load Level check
                    if (this.bs.Load_Level <= 85) {
                        this.X = Integer.parseInt(c.getX());
                        this.Y = Integer.parseInt(c.getY());
                        this.IMEI = c.getIMEI();
                        this.IMSI = c.getIMSI();
                        return 1;
                    } else {
                        this.X = Integer.parseInt(c.getX());
                        this.Y = Integer.parseInt(c.getY());
                        this.IMEI = c.getIMEI();
                        this.IMSI = c.getIMSI();
                        return 2;
                    }
                } else {
                    this.X = Integer.parseInt(c.getX());
                    this.Y = Integer.parseInt(c.getY());
                    this.IMEI = c.getIMEI();
                    this.IMSI = c.getIMSI();
                    return 3;
                }
            } else if (m.Message_Type.equals("D")) {
                Disconnect d = (Disconnect) m;
                // authentity check
                if (d.getIMEI().equals(this.IMEI)) {
                    return 4;
                } else {
                    return 5;
                }
            } else {
                return 5;
            }

        } catch (Exception e) {
            System.err.println("Check Time... Could not listen");
            System.exit(1);
        }

        return 5;

    }

}
