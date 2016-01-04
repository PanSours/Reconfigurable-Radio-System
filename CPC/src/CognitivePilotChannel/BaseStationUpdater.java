/*
 * In this class, we connect periodically with the MySQL Server in order to update our BaseStations records.
 * First of all, we delete old records. Secondly we read all records and update our shared memory.
 */
package CognitivePilotChannel;

import java.sql.Connection;
import java.sql.Timestamp;
import java.util.Calendar;

public class BaseStationUpdater extends Thread {

    Connection con;
    boolean shouldRun;
    private SharedMemory sm;

    public BaseStationUpdater(Connection c, SharedMemory sms) {

        this.con = c;
        this.shouldRun = true;
        this.sm = sms;

    }

    @Override
    public void run() {

        while (this.shouldRun) {
            try {
                // GET TIME OF MINIMUM LIMIT
                Timestamp tmsp = this.getTimeStamp();
                long Time = tmsp.getTime();
                Time -= 15000;
                tmsp.setTime(Time);
                // UPDATE STORAGE DUE TO STATUS
                sm.updateStorageToDb(con, tmsp);
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                System.err.println("Problem with CPC!");
                this.shouldRun = false;
            }
        }
    }

    public java.sql.Timestamp getTimeStamp() {
        Calendar calendar = Calendar.getInstance();
        java.util.Date now = calendar.getTime();
        Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());
        return currentTimestamp;
    }

}
