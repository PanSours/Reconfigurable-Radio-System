package MobileCore;

import javax.microedition.rms.RecordListener;
import javax.microedition.rms.RecordStore;

public class RMSRecordListener implements RecordListener {

    public Midlet mid;

    public RMSRecordListener(Midlet m) {

        this.mid = m;
    }

    public void recordAdded(RecordStore rs, int id) {
        try {
            System.out.println("Record with id: " + id + " successfully added to RecordStore: " + rs.getName());
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public void recordDeleted(RecordStore rs, int id) {
        try {
            System.out.println("Record with id: " + id + " successfully deleted from RecordStore: " + rs.getName());
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public synchronized void recordChanged(RecordStore rs, int id) {

        if ((this.mid.manual == false) && (this.mid.user.ChargingModel_ID == id)) {
            System.out.println("Katalava charge");
            synchronized (this.mid.ac) {
                this.mid.ac.notify();
            }
        } else if ((this.mid.manual == true) && (this.mid.user.ChargingModel_ID == id)) {
            this.mid.disconnect();
        }

    }

}
