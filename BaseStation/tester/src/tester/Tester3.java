package tester;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
/**
 * Tester 3. Validates that all connect messages reach the basestation and a proper answer is received
 * Validates disconnect messages.
 */
public class Tester3 {
    
     String[] mobiles= {"CONNECT#1#1#2#5%","CONNECT#2#1#5#1%","CONNECT#3#1#1#5%","CONNECT#4#1#1#3%","CONNECT#5#1#3#5%","CONNECT#6#1#8#2%","CONNECT#7#1#2#5%","CONNECT#8#1#7#8%","CONNECT#9#1#9#10%","CONNECT#10#1#2#4%","CONNECT#11#6#1#9%","CONNECT#12#1#3#5%","CONNECT#13#1#4#4%","CONNECT#14#1#3#7%","CONNECT#15#1#8#10%","CONNECT#16#1#1#9%","CONNECT#17#1#10#8%","CONNECT#18#1#1#1%","CONNECT#19#1#1#1%"};
    
    public Tester3(int port, String ipAddress, int[] coord){
        int counter = 0;
        try {
           
            for(int i=0;i<mobiles.length;i++){
                Socket connection = new Socket(InetAddress.getByName(ipAddress), port);

                InputStream in = connection.getInputStream();
                OutputStream out = connection.getOutputStream();

                out.write(mobiles[i].getBytes());
                String s = "";
                char x = ' ';
                while( (x=(char)in.read())!= '%'){
                    s+=x;
                }
                System.out.println("[TESTER3 SENT]:"+mobiles[i]);
                System.out.println("[TESTER3 RECEIVED]:"+s);
              /*in.close();
                out.close();
                connection.close();*/
                if(s.contains("OK"))
                    counter++;
              /*connection = new Socket(InetAddress.getByName(ipAddress), port);
                in = connection.getInputStream();
                out = connection.getOutputStream();*/
                
                in.close();
                out.close();
                connection.close();
            }            
        
            if(counter==18){
                System.out.println("[TESTER3 RESULT]: Success.");
            }
            else
                System.out.println("[TESTER3 RESULT]: Failure. Please check your source code or the coordinates you have provided.");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public static void main(String args[]){
        int[] x = new int[2];
        x[0] = 0;
        x[1] = 0;        
        try{
        new Tester3(4444,"192.168.1.68",x);
        }
        catch (Exception e) {
        }
        
        
    }
}

