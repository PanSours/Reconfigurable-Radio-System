/*
 * This is the class, where we handle the Base Station. This operation has three different parts:
 *  -Set up the main connection, where Mobiles Phones apply for connections.
 *  -Set up CPCHandler in order to inform the whole Network System.
 *  -Listen for connection applications and create new thread in order to serve its one transactions.
 *  -Update GUI and properties(essentially Load Level).
 */
package basestation;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import javax.swing.JFileChooser;
import java.util.Vector;

public class BaseStationHandler extends Thread {

    public Graphic fm;
    public CPCHandler CPC;
    private String Network_ID;
    private String Basestation_ID;
    private String Network_Type;
    private String Frequency;
    private String Max_Bitrate;
    private String Guaranteed_Bitrate;
    private String Provider;
    private String Charge_Type;
    public int X;
    public int Y;
    public volatile int MCounts;
    public volatile int Load_Level;
    public int Radius_Coverage;
    public int Port;
    static int Signal_Strength;
    static int T_Period;
    public Properties properties;
    volatile public Vector<MobileHandler> mt;
    public boolean shouldRun;
    public ServerSocket bsSocket;

    BaseStationHandler(Graphic f) {

        this.fm = f;
        this.Load_Level = 0;
        this.MCounts = 0;
        this.shouldRun = true;
        this.mt = new Vector<MobileHandler>();

    }

    @Override
    public void run() {

        this.loadProperty(this.fm);
        this.fm.SetBaseStation(this.X, this.Y, this.properties);

        //Create Socket
        bsSocket = null;

        try {
            bsSocket = new ServerSocket(this.Port, 1, null);
            System.out.println("Wait for connection applications...");
        } catch (IOException e) {
            System.err.println("Could not listen on port:" + this.Port);
            System.exit(1);
        }

        // Start CPC information thread
        CPC = new CPCHandler(this.properties, this);
        CPC.start();

        Socket MobileSocket;
        while (this.shouldRun) {

            try {

                // Listen for new connections and handle its one in new thread
                MobileSocket = bsSocket.accept();

                if (MobileSocket != null) {

                    MobileHandler mh = new MobileHandler(MobileSocket, this);
                    mh.start();
                    this.mt.add(mh);

                    try {
                        Thread.currentThread().sleep(100);
                    } catch (InterruptedException e) {
                        System.err.println("Problem");
                        System.exit(1);
                    }

                }

            } catch (IOException e) {
                System.err.println("Problem with messages on  socket:" + bsSocket);
                //System.exit(1);
            }

        }

        // Stop communication with each Mobile Device
        if (!this.mt.isEmpty()) {

            for (int i = this.mt.size() - 1; i >= 0; i--) {

                try {
                    this.mt.get(i).shouldRun = false;
                    this.mt.remove(i);
                    this.Load_Level = this.MCounts * 5;
                    this.fm.updateProperties(this.properties, this.Load_Level);
                    Thread.currentThread().sleep(100);
                } catch (Exception e) {
                    System.err.println("Problem with massive disconnection!");
                    System.err.println(e);
                    System.exit(1);
                }

                try {
                    Thread.currentThread().sleep(1000);
                } catch (InterruptedException e) {
                    System.err.println("Problem with actionhandler");
                    System.err.println(e);
                    System.exit(1);
                }

            }

        }

    }

    //Load properties
    public void loadProperty(Graphic g) {

        String filename = "properties1";
        /*
         final JFileChooser fc = new JFileChooser();
         fc.showOpenDialog(g);

         try {
           
         filename= fc.getSelectedFile().getName();

         }catch(Exception e ){
         System.out.println("Error in getting input");
         System.exit(1);
         }
         */

        // Read properties file.
        properties = new Properties();

        try {
            properties.load(new FileInputStream(filename));
            Network_ID = properties.getProperty("Network_ID");
            Basestation_ID = properties.getProperty("Basestation_ID");
            Network_Type = properties.getProperty("Network_Type");
            Frequency = properties.getProperty("Frequency");
            Max_Bitrate = properties.getProperty("Max_Bitrate");
            Guaranteed_Bitrate = properties.getProperty("Guaranteed_Bitrate");
            Provider = properties.getProperty("Provider");
            Charge_Type = properties.getProperty("Charge_Type");
            X = Integer.parseInt(properties.getProperty("x"));
            Y = Integer.parseInt(properties.getProperty("y"));
            Radius_Coverage = Integer.parseInt(properties.getProperty("Radius_Coverage"));
            Port = Integer.parseInt(properties.getProperty("Port"));
            Signal_Strength = Integer.parseInt(properties.getProperty("Signal_Strength"));
            T_Period = Integer.parseInt(properties.getProperty("T_Period"));
        } catch (IOException e) {
            System.err.println("Properties file doesnt have right form!");
            System.exit(1);
        }

    }

}
