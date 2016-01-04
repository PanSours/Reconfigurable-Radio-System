/*
 * This class implements the famous ActionListener interface in order to take control of 
 * buttons(Start, Stop, Exit). Selection of Start leads to the beginning of the CPC Handler, 
 * which set up the web service and starts communication with mobile phones and the MySQL Server. 
 * Selection of Stop leads to the disconnection of the CPC and the closing of all opened threads, 
 * which handle Mobile Devices and Memory synchronization with DataBase.
 */
package CognitivePilotChannel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JFrame;

public class myActionListener implements ActionListener {

    private CPCHandler CPC;
    private boolean stopped;
    private boolean started;
    public JFrame fm;

    public myActionListener(JFrame f) {
        this.stopped = false;
        this.started = false;
        this.fm = f;
    }

    @Override
    public void actionPerformed(ActionEvent ae) {

        if (ae.getActionCommand().equals("Start")) {
            CPC = new CPCHandler();
            CPC.start();
            this.started = true;

        } else if (ae.getActionCommand().equals("Stop")) {
            if (this.started == true) {
                try {
                    CPC.shouldRun = false;
                    try {
                        CPC.cpcSocket.close();
                    } catch (IOException ex) {
                        System.out.println("Couldnt close ServerSocket:" + CPC.cpcSocket);
                    }
                    CPC.join();
                } catch (InterruptedException ex) {
                    System.out.println("Main thread Interrupted");
                }
                this.stopped = true;
            }
        } else if (ae.getActionCommand().equals("Exit")) {

            if ((this.stopped == false) && (this.started == true)) {
                try {
                    CPC.shouldRun = false;
                    try {
                        CPC.cpcSocket.close();
                    } catch (IOException ex) {
                        System.out.println("Couldnt close ServerSocket:" + CPC.cpcSocket);
                    }
                    CPC.join();
                } catch (InterruptedException ex) {
                    System.out.println("Main thread Interrupted");
                }
            }
            System.exit(1);
        }

    }

}
