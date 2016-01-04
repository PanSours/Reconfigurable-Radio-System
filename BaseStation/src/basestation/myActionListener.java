/*
 * This class implements the famous ActionListener interface in order to take control of menu items such as Connect
 * and Disconnect. Selection of connect leads to the beginning of the Base Station Handler, which set up the 
 * connection and handles the Mobile Devices. Selection of disconnect leads to the disconnection of the Base 
 * Station and the closing of all opened threads, which handle Mobile Devices.
 */
package basestation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class myActionListener implements ActionListener {

    Graphic fm = null;
    BaseStationHandler bsh;
    boolean Disconnection;

    myActionListener(Graphic f) {

        this.fm = f;
        this.Disconnection = false;

    }

    @Override
    public void actionPerformed(ActionEvent me) {

        // Connect Menuitem selected
        if (me.getActionCommand().equals("Connect")) {

            this.Disconnection = false;
            this.fm.Start.setEnabled(false);
            this.fm.Stop.setEnabled(true);
            this.fm.Refresh.setEnabled(true);
            this.bsh = new BaseStationHandler(this.fm);
            this.bsh.start();

        } else if (me.getActionCommand().equals("Refresh")) {

            if (this.Disconnection = false) {
                this.fm.UpdateMobiles(this.bsh.mt);
                this.fm.updateProperties(this.bsh.properties, this.bsh.Load_Level);
            }
        } // Disconnect MenuItem selected
        else if (me.getActionCommand().equals("Disconnect")) {

            this.fm.Stop.setEnabled(false);
            this.DisconnectionPerformed();
            this.Disconnection = true;
            this.fm.Start.setEnabled(true);

        } // Exit MenuItem selected
        else if (me.getActionCommand().equals("Exit")) {

            if (this.Disconnection = false) {
                this.DisconnectionPerformed();
            }
            System.exit(1);
        } else if (me.getActionCommand().equals("Help...")) {

            this.fm.createHelpFrame();

        }

    }

    public void DisconnectionPerformed() {

        // Stop informing CPC
        this.bsh.CPC.shouldRun = false;
        this.bsh.shouldRun = false;
        // Stop listen to new connections
        try {
            this.bsh.bsSocket.close();
        } catch (IOException e) {
            System.err.println("Problem with Server Socket closing!");
            System.err.println(e);
            System.exit(1);
        }
        // Clear GUI from previus information
        //this.fm.UpdateMobiles(this.bsh.mt);
        this.fm.clearTable();
        this.fm.recreateText();
        this.fm.imagePanel.removeAll();
        System.out.println("All terminated OK");
    }

}
