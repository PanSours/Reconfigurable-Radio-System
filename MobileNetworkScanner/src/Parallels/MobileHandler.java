/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Parallels;

import MobileCore.*;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;
import javax.microedition.io.StreamConnection;

/**
 *
 * @author Kiddo
 */
public class MobileHandler extends Thread {

    private StreamConnection sc;
    private SharedMemory sm;
    public boolean shouldRun;
    private OutputStream os;
    private InputStream is;

    public MobileHandler(StreamConnection c, SharedMemory s) {

        this.sc = c;
        this.sm = s;
        this.shouldRun = true;
    }

    public void run() {

        try {
            is = sc.openInputStream();
            os = sc.openOutputStream();

            StringBuffer sb = new StringBuffer();
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

                }
                Message msg = new Message();
                msg.fromString(sb.toString());
                if (msg.Message_Type.equals("DISCOVER")) {
                    Discover dis = (Discover) msg;
                    Vector bss = sm.passFriendly(Integer.parseInt(dis.getX()), Integer.parseInt(dis.getY()));
                    Profiles prof = new Profiles();
                    os.write(prof.toString(bss).getBytes());
                }

            }
            is.close();
            os.close();
            sc.close();
        } catch (Exception e) {

        }
    }
}
