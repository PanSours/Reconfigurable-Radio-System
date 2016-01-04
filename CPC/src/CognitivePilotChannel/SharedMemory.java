/*
 * Shared Memory implements a shared memory segment. Use one semaphore to handle mutual exclusion 
 * between deleter-inserters and searchers.
 */
package CognitivePilotChannel;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Vector;

public class SharedMemory {

    private Vector<BaseStationProfile> storage;
    private boolean canAccess = true;

    public SharedMemory() {
        storage = new Vector<BaseStationProfile>();
    }

    public synchronized Vector<BaseStationProfile> search(int x, int y) {

        Vector<BaseStationProfile> bsp = new Vector<BaseStationProfile>();

        while (!canAccess) {
            try {
                wait();
            } catch (InterruptedException ex) {
                System.out.println("1. Problem on SharedMemory");
            }
        }
        canAccess = false;
        for (int i = 0; i < storage.size(); i++) {
            if (storage.get(i).checkCoordinates(x, y)) {
                bsp.add(storage.get(i));
            }
        }
        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
            System.out.println("2. Problem on SharedMemory");
        }
        notifyAll();
        canAccess = true;
        return bsp;
    }

    public synchronized int update(BaseStationProfile bsp) {
        int i, count = 0;
        int num = 0;

        while (!canAccess) {
            try {
                wait();
            } catch (InterruptedException ex) {
                System.out.println("3. Problem on SharedMemory");
            }
        }
        canAccess = false;
        // SEARCH IF IT IS ALREADY IN
        for (i = 0; i < this.storage.size(); i++) {
            if (this.storage.get(i).Basestation_ID.equals(bsp.Basestation_ID)) {
                this.storage.get(i).Load_Level = bsp.Load_Level;
                this.storage.get(i).Timestamp = this.getTimeStamp().getTime();
                this.storage.get(i).Status = 1;
                num = 2;
                break;
            }
            count++;
        }
        // ADD IF NOT
        if (count == this.storage.size()) {
            bsp.Status = 0;
            this.storage.add(bsp);
            num = 1;
        }

        notifyAll();
        canAccess = true;
        return num;
    }

    public synchronized void updateStorageToDb(Connection con, Timestamp tmsp) {
        int i, count = 0;
        while (!canAccess) {
            try {
                wait();
            } catch (InterruptedException ex) {
                System.out.println("3. Problem on SharedMemory");
            }
        }
        canAccess = false;
        // UPDATETODB IF NEEDED
        if (!this.storage.isEmpty()) {
            for (i = this.storage.size() - 1; i >= 0; i--) {
                if (this.storage.get(i).Timestamp < tmsp.getTime()) {
                    this.storage.get(i).Status = 2;
                }
                if (this.storage.get(i).Status == 0) {
                    this.storage.get(i).addToDb(con);
                    this.storage.get(i).Status = 3;
                } else if (this.storage.get(i).Status == 1) {
                    this.storage.get(i).updateToDb(con);
                    this.storage.get(i).Status = 3;
                } else if (this.storage.get(i).Status == 2) {
                    this.storage.get(i).deleteToDb(con);
                    this.storage.remove(i);
                }
            }
        }
        notifyAll();
        canAccess = true;
    }

    public java.sql.Timestamp getTimeStamp() {
        Calendar calendar = Calendar.getInstance();
        java.util.Date now = calendar.getTime();
        Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());
        return currentTimestamp;
    }

}
