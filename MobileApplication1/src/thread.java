
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.SocketConnection;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Kiddo
 */
public class thread extends Thread {

    public boolean flag;

    SocketConnection sc;
    public OutputStream os;
    public InputStream is;

    public thread() {

        flag = true;
    }

    /* EKTYPOTIKI RUN */
    public void run() {

        while (flag) {
            System.out.println("OK");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

    }

//    /* SOCKET RUN */
//    public void run(){
//        
//        try{
//           sc = (SocketConnection) Connector.open("socket://localhost:"+port);
//           try{
//               os = sc.openOutputStream();
//               is = sc.openInputStream();
//               byte[] data = "lajklahkahlahl".getBytes();
//               os.write(data);
//                   StringBuffer sb = new StringBuffer();
//                   try{
//                       int chars= 0;
//                       while ((chars = is.read()) != -1){ 
//                          sb.append((char) chars);
//
//                       }
//                   }catch (Exception e){
//                   }
//                   System.out.println(sb);
//
//           }
//           catch(IOException ex){
//                ex.printStackTrace();
//           }
//           finally{
//                os.close();
//                is.close();
//           }
//        }catch (IOException ex) {
//        }
//        finally{
//            try {
//                sc.close();
//            } catch (Exception ex) {
//                System.err.println("UNABLE TO CLOSE CPC SOCKET!!!");
//            }
//        }
//        
//    }
}
