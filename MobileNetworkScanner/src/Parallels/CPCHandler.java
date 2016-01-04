package Parallels;

import MobileCore.Discover;
import MobileCore.Message;
import MobileCore.Profiles;
import MobileCore.SharedMemory;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.SocketConnection;

public class CPCHandler extends Thread {

    SocketConnection sc;
    public OutputStream os;
    public InputStream is;
    public boolean shouldRun;
    public int port;
    public int x;
    public int y;
    public String IMEI;
    public SharedMemory sm;

    public CPCHandler(SharedMemory m, String imei, int x1, int y1) {

        port = 8020;
        shouldRun = true;
        x = x1;
        y = y1;
        IMEI = imei;
        sm = m;

    }

    public void run() {

        while (shouldRun) {
            CPCService();
        }
    }

    public void CPCService() {
        try {
            sc = (SocketConnection) Connector.open("socket://localhost:" + port);
            try {
                os = sc.openOutputStream();
                is = sc.openInputStream();
                Discover dis = new Discover();
                dis.setIMEI(IMEI);
                dis.setX(Integer.toString(x));
                dis.setY(Integer.toString(y));
                os.write(dis.toString().getBytes());

                StringBuffer sb = new StringBuffer();
                try {
                    int chars, i = 0;
                    while ((chars = is.read()) != -1) {
                        sb.append((char) chars);
                        char c = (char) chars;
                        if (c == '%') {
                            break;
                        }
                    }
                } catch (Exception e) {
                }
                Message m = new Message();
                m = m.fromString(sb.toString());
                if (m.Message_Type.equals("PRO")) {
                    Profiles p = (Profiles) m;
                    sm.update(p.profiles);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                os.close();
                is.close();
            }
        } catch (IOException ex) {
            System.err.println("CPC NOT WORKING!!!");
//            MobileService();

        } finally {
            try {
                sc.close();
            } catch (Exception ex) {
                System.err.println("UNABLE TO CLOSE CPC SOCKET!!!");
            }
        }
        try {
            Thread.sleep(5000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

    }

//    public void MobileService(){
//        
//        boolean found = false;    
//        Vector mobs = readMobiles();
//        
//        for(int j=0; j<mobs.capacity();j++){
//            
//            MobileProfile mp = (MobileProfile) mobs.elementAt(j);
//            
//            try{
//                sc = (SocketConnection) Connector.open("socket://" + mp.IP + ":"+ mp.port);
//                try{
//                    os = sc.openOutputStream();
//                    is = sc.openInputStream();
//                    Discover dis = new Discover();
//                    dis.setIMEI(IMEI);
//                    dis.setX(Integer.toString(x));
//                    dis.setY(Integer.toString(y));
//                    os.write(dis.toString().getBytes());
//
//                        StringBuffer sb = new StringBuffer();
//                        try{
//                            int chars, i = 0;
//                            while ((chars = is.read()) != -1){ 
//                                sb.append((char) chars);
//                                char c = (char) chars;
//                                    if (c == '%'){
//                                        break;
//                                    }
//                            }
//                        }catch (Exception e){
//                        }
//                        Message m = new Message();
//                        m = m.fromString(sb.toString());
//                        if(m.Message_Type.equals("PRO")){
//                            Profiles p = (Profiles) m;
//                            sm.update(p.profiles);   
//                        }
//                        found = true;
//                        break;
//                }
//                catch(IOException ex){
//                        ex.printStackTrace();
//                }
//                finally{
//                        os.close();
//                        is.close();
//                }
//            }catch (IOException ex) {
//                ex.printStackTrace();
//            }
//            finally{
//                try {
//                    sc.close();
//                } catch (IOException ex) {
//                    ex.printStackTrace();
//                }
//            }
//            try {
//                Thread.sleep(5000);
//            } catch (InterruptedException ex) {
//                ex.printStackTrace();
//            } 
//        }
//        if(found==true){
//            try{
//            os.close();
//            is.close();
//            sc.close();
//            }
//            catch(IOException ex) {
//                ex.printStackTrace();
//            }
//        }
//    
//    }
//        
//    // READ PROPERTY FILE
//    //Load properties
//    private Vector readMobiles(){       
//        String[] substrings;
//        String[] subs;
//        String[] els;
//        Vector v = new Vector();
//        
//        InputStream isp = getClass().getResourceAsStream("./mobiles.txt");
//        StringBuffer sb = new StringBuffer();
//        try{
//            int chars, i = 0;
//            while ((chars = isp.read()) != -1){ 
//                sb.append((char) chars);
//            }
//        }
//        catch (Exception e){
//            System.err.println(e);
//            System.err.println("Problem with property file!");
//        }
//       substrings = stringToArray(sb.toString(),"\n");   
//       
//       System.out.println("MOBILES SHARING NETWORK");
//       System.out.println("=======================");
//       for(int i=0;i<substrings.length;i++){
//           subs = stringToArray(substrings[i],"=");
//           els  = stringToArray(subs[1],":");
//           MobileProfile mp = new MobileProfile();
//           if(els.length==2){
//           mp.IP= els[0];
//           mp.port = els[1]; 
//           v.addElement(subs[0]);
//           }
//           System.out.println("Mobile"+(i+1)+"="+mp.IP+":"+mp.port);
//       }
//       
//       return v; 
//       
//    }   
//    
//    public static String[] stringToArray(String a,String delimeter){
//        String c[]=new String[0];
//        String b=a;
//        while (true){ 
//            int i=b.indexOf(delimeter);
//            String d=b;
//            if (i>=0)
//            d=b.substring(0,i);
//            String e[]=new String[c.length+1];
//            System.arraycopy(c, 0, e, 0, c.length);
//            e[e.length-1]=d;
//            c=e;
//            b=b.substring(i+delimeter.length(),b.length());
//            if (b.length()<=0 || i<0 )
//            break;
//        }
//        return c;
//    }
}
