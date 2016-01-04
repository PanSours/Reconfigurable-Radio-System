/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Kiddo
 */
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class tester4 {

    public tester4(int port, String ipAddress, int[] coords) {

        int ok_counter = 0;
        int no_cov_counter = 0;

        try {
            //Send 20 requestes
            for (int i = 0; i < 40; i++) {
                if (i % 2 == 0 || i == 0) {
                    Socket connection = new Socket(InetAddress.getByName(ipAddress), port);
                    InputStream in = connection.getInputStream();
                    OutputStream out = connection.getOutputStream();

                    //Dummy messages - make sure that O(0,0) is served by your basestation
                    out.write(("DISCOVER#" + (i + 1) + "#" + coords[0] + "#" + coords[1] + "%").getBytes());
                    String s = "";
                    char x = ' ';
                    while ((x = (char) in.read()) != '%') {
                        s += x;
                    }

                    System.out.println("[TESTER4 SENT]:" + "DISCOVER#" + (i + 1) + "#" + coords[0] + "#" + coords[1] + "%");
                    System.out.println("[TESTER4 RECEIVED]:" + s);

                    if (s.contains("PROFILES")) {
                        ok_counter++;
                    }

                    in.close();
                    out.close();
                    connection.close();
                } else {
                    Socket connection = new Socket(InetAddress.getByName(ipAddress), port);
                    InputStream in = connection.getInputStream();
                    OutputStream out = connection.getOutputStream();

                    out.write(("DISCOVER#" + (i + 1) + "#" + (coords[0] + 100) + "#" + (coords[1] + 100) + "%").getBytes());
                    String s = "";
                    char x = ' ';
                    while ((x = (char) in.read()) != '%') {
                        s += x;
                    }

                    System.out.println("[TESTER4 SENT]:" + "DISCOVER#" + (i + 1) + "#" + (coords[0]+100) + "#" + (coords[1]+100) + "%");
                    System.out.println("[TESTER4 RECEIVED]:" + s);

                    //This should be used if the reply if of the form PROFILES#%
                    if (s.substring(0,s.length()-1).equalsIgnoreCase("PROFILES")) {
                        no_cov_counter++;
                    }
                    //Uncomment the following if you reply with something else, e.g. NO_COVERAGE
                    /**
                    if (!s.contains("PROFILES")) {
                        no_cov_counter++;
                    }
                    */

                    in.close();
                    out.close();
                    connection.close();
                }
            }
            if ((no_cov_counter == ok_counter) && ok_counter == 20) {
                System.out.println("[TESTER4 RESULT]: Success.");
            } else {
                System.out.println("[TESTER4 RESULT]: Failure. Please check your source code or the coordinates you have provided.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String args[]) {
        int[] x = new int[2];
        x[0] = 0;
        x[1] = 0;
        new tester4(8020,"192.168.1.67", x);
    }
}
