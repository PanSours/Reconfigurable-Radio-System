package Parallels;

import MobileCore.BaseStationProfile;
import MobileCore.Connect;
import MobileCore.Disconnect;
import MobileCore.Message;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.SocketConnection;

public class BaseStationConnection extends Thread {

    SocketConnection sc;
    private OutputStream os;
    private InputStream is;
    public boolean shouldRun;
    public int x;
    public int y;
    private String IMEI;
    private String IMSI;
    public BaseStationProfile bsp;
    public boolean connected;
    public boolean disconnected;
    public boolean disconnect;
    public boolean manual;
    public AutoConnection ac;

    public BaseStationConnection(String imei, String imsi, int x1, int y1, BaseStationProfile b, boolean m) {

        shouldRun = true;
        x = x1;
        y = y1;
        IMEI = imei;
        IMSI = imsi;
        bsp = b;
        this.connected = false;
        this.disconnected = false;
        this.disconnect = false;
        this.manual = true;
    }

    public void run() {
        this.connected = false;
        this.disconnected = false;
        this.disconnect = false;
        this.shouldRun = true;

        try {
            sc = (SocketConnection) Connector.open("socket://localhost:" + bsp.Port);
            os = sc.openOutputStream();
            is = sc.openInputStream();
            Connect con = new Connect();
            con.setIMEI(IMEI);
            con.setIMSI(IMSI);
            con.setX(Integer.toString(x));
            con.setY(Integer.toString(y));
            os.write(con.toString().getBytes());
            transmit();
        } catch (Exception ex) {
            //ex.printStackTrace();
            disconnected = true;
            connected = false;
            System.out.println("Paw gia manual");
            if (manual == false) {
                synchronized (ac) {
                    ac.interrupted = true;
                    ac.notify();
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException ex1) {
                        ex1.printStackTrace();
                    }
                    ac.notify();
                }
            }
        } finally {
            try {
                if (sc != null) {
                    is.close();
                    os.close();
                    sc.close();
                }
            } catch (IOException ex) {
                //ex.printStackTrace();
                disconnected = true;
                connected = false;
            }
        }

    }

    private void transmit() {

        byte[] data;
        String msg;

        while (shouldRun) {
            StringBuffer sb = new StringBuffer();
            try {
                if (is.available() != 0) {
                    try {
                        int chars, i = 0;
                        while ((chars = is.read()) != -1) {
                            sb.append((char) chars);
                            char c = (char) chars;
                            if (c == '%') {
                                break;
                            }
                        }
                        System.out.println(sb.toString());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        disconnected = true;
                        connected = false;
                    }
                    Message res = new Message();
                    res.fromString(sb.toString());
                    if (res.Message_Type == null) {
                        disconnected = true;
                        connected = false;
                        synchronized (ac) {
                            ac.interrupted = true;
                            ac.notify();
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException ex1) {
                                ex1.printStackTrace();
                            }

                        }
    //                    synchronized(ac){
                        //                            ac.notify();
                        //                            
                        //                    }
                        break;

                    }
                    if ((res.Message_Type.equals("NO")) || (res.Message_Type.equals("OVER")) || (res.Message_Type.equals("DISCONNECT"))) {
                        shouldRun = false;
                        disconnected = true;
                        connected = false;
                    } else if (res.Message_Type.equals("OK")) {
                        if (disconnect == false) {
                            System.out.println("Connect!");
                            connected = true;
                            disconnected = false;
                        } else {
                            System.out.println("Disconnect!");
                            shouldRun = false;
                            connected = false;
                            disconnect = false;
                        }
                    } else {
                        shouldRun = false;
                        disconnected = true;
                        connected = false;
                    }
                } else {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
            } catch (IOException ex) {
                if (manual == false) {
                    synchronized (ac) {
                        ac.interrupted = true;
                        ac.notify();
                    }
                }
                ex.printStackTrace();
            }
            if ((disconnect) && (shouldRun)) {
                Disconnect dis = new Disconnect();
                dis.setIMEI(IMEI);
                try {
                    os.write(dis.toString().getBytes());
                } catch (Exception ex) {
                    ex.printStackTrace();
                    disconnected = true;
                    connected = false;
                    shouldRun = false;
                }
            }
        }
    }

}
