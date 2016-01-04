/*
 * This is the class, where we handle the CPC. This operation has three different parts:
 • Set up the main connection, where Mobiles Phones apply for suggestions.
 • Listen for suggestion applications and create new thread in order to serve its one transactions.
 • Set up Web Service in order to communicate with Base Stations.
 • Set up connection with DataBase.
 */
package CognitivePilotChannel;

import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.xml.ws.Endpoint;

/**
 *
 * @author Kiddo
 */
public class CPCHandler extends Thread {

    public boolean shouldRun;
    public ServerSocket cpcSocket;
    private BaseStationInformWS BSIWS;
    private BaseStationUpdater BSU;
    private ArrayList<MobileHandler> mhl;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        //Create new frame
        JFrame f = new JFrame("Cognitive Pilot Channel");
        f.setSize(300, 80);
        Container content = f.getContentPane();
        content.setBackground(Color.white);
        content.setLayout(new FlowLayout());
        myActionListener al = new myActionListener(f);
        JButton Start = new JButton("Start");
        JButton Stop = new JButton("Stop");
        JButton Exit = new JButton("Exit");
        Start.addActionListener(al);
        Stop.addActionListener(al);
        Exit.addActionListener(al);
        content.add(Start);
        content.add(Stop);
        content.add(Exit);
        f.setVisible(true);
    }

    public CPCHandler() {

        this.shouldRun = true;
    }

    @Override
    public void run() {

        int Port = 8020;
        mhl = new ArrayList<MobileHandler>();

        //Create new Shared Memory segment
        SharedMemory sm = new SharedMemory();

        // Create DBHandler instance and open DB
        DataBaseHandler DBH = new DataBaseHandler();
        DBH.loadProperty();
        DBH.open();
        Connection conn = DBH.getConn();

        // Deploy WebService and give same connection
        BSIWS = new BaseStationInformWS(sm);
        Endpoint endPoint = Endpoint.create(BSIWS);
        endPoint.publish(loadUrl());

        // Create BaseStationUpdater to update DataBase
        BSU = new BaseStationUpdater(conn, sm);
        BSU.start();

        //Create Socket and Listen
        this.cpcSocket = null;

        try {
            this.cpcSocket = new ServerSocket(Port, 1, null);
            System.out.println("Wait for connection applications...");
        } catch (IOException e) {
            System.err.println("Could not listen on port:" + Port);
            System.exit(1);
        }

        Socket MobileSocket;
        while (this.shouldRun) {
            if (mhl.size() >= 50) {
                for (int i = mhl.size() - 1; i >= 0; i--) {
                    try {
                        mhl.get(i).join();
                        //System.out.println("MBHandler Thread["+ i +"]: "+ mhl.get(i) + "stopped.");
                    } catch (InterruptedException ex) {
                        System.out.println("Main thread Interrupted");
                    }
                }
                mhl.clear();
            }
            try {
                // Listen for new connections and handle its one in new thread
                MobileSocket = this.cpcSocket.accept();
                if (MobileSocket != null) {
                    MobileHandler mh = new MobileHandler(MobileSocket, conn, sm);
                    mhl.add(mh);
                    mh.start();
                }
            } catch (IOException e) {
                System.out.println("Stop listen on  socket:" + cpcSocket);
            }
        }

        // Stop publishing this endpoint
        endPoint.stop();

        // Close ServerSocket, threads and DataBase Connection 
        try {
            this.cpcSocket.close();
        } catch (IOException ex) {
            System.err.println("Problem closing socket:" + cpcSocket);
        }

        System.out.println("Waiting for threads to finish...");
        if (!mhl.isEmpty()) {
            for (int i = mhl.size() - 1; i >= 0; i--) {
                try {
                    mhl.get(i).join();
                    System.out.println("MBHandler Thread[" + i + "]: " + mhl.get(i) + "stopped.");
                } catch (InterruptedException ex) {
                    System.out.println("Main thread Interrupted");
                }
            }
        }
        mhl.clear();
        try {
            BSU.shouldRun = false;
            BSU.join();
            System.out.println("BSUpdater Thread[1]: " + BSU + "stopped.");
        } catch (InterruptedException ex) {
            System.out.println("Main thread Interrupted");
        }

        DBH.close();

    }

    // READ PROPERTY FILE
    static String loadUrl() {
        String Url = "http://";
        int port = 0;
        try {
            InetAddress IP = InetAddress.getLocalHost();
            Url += IP.getHostAddress() + ":8081/";
            Url += "WebServices/BaseStationInformWS";
            System.out.println("Please inform clients, wsdl is := " + Url + "?wsdl");
            return Url;
        } catch (IOException e) {
            System.err.println("Properties file doesnt have right form!");
            System.exit(1);
        }
        return null;
    }
}
