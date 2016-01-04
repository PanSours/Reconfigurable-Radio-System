package MobileCore;

/*
 * Shared Memory implements a shared memory segment. Use one semaphore to handle mutual exclusion 
 * between deleter-inserters and searchers.
 */
import java.util.Vector;

public class SharedMemory {

    private Vector storage;
    private boolean canAccess = true;

    public SharedMemory() {
        storage = new Vector();
    }

    public synchronized BaseStationProfile searchSolution(int charg, int[] nets, String old) {

        boolean found = false;
        int n = 0;
        BaseStationProfile bs = new BaseStationProfile();

        while (!canAccess) {
            try {
                wait();
            } catch (InterruptedException ex) {
                System.out.println("1. Problem on SharedMemory");
            }
        }
        canAccess = false;

        for (int i = 0; i < storage.size(); i++) {
            bs = (BaseStationProfile) storage.elementAt(i);
            if ((bs.Charge_Type == (charg + 1)) && (!bs.Basestation_ID.equals(old))) {
                for (int j = 0; j < nets.length; j++) {
                    if (bs.Network_Type == nets[j]) {
                        found = true;
                        i = storage.size() + 2;
                        break;
                    }
                }
            } else {
                System.out.println("AVOID");
            }
        }
        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
            System.out.println("2. Problem on SharedMemory");
        }
        notifyAll();
        canAccess = true;
        if (found == true) {
            return bs;
        } else {
            return null;
        }
    }

    public synchronized Vector getFriendly(int charg, int[] nets) {

        int n = 0;
        Vector bsp = new Vector();
        BaseStationProfile bs = new BaseStationProfile();

        while (!canAccess) {
            try {
                wait();
            } catch (InterruptedException ex) {
                System.out.println("1. Problem on SharedMemory");
            }
        }
        canAccess = false;
        // RETURN ELEMENTS

        for (int i = 0; i < storage.size(); i++) {
            bs = (BaseStationProfile) storage.elementAt(i);
            if (bs.Charge_Type == (charg + 1)) {
                for (int j = 0; j < nets.length; j++) {
                    if (bs.Network_Type == nets[j]) {
                        bsp.addElement(storage.elementAt(i));
                        break;
                    }
                }
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

    public synchronized Vector passFriendly(int x, int y) {

        Vector bsp = new Vector();
        BaseStationProfile bs = new BaseStationProfile();

        while (!canAccess) {
            try {
                wait();
            } catch (InterruptedException ex) {
                System.out.println("1. Problem on SharedMemory");
            }
        }
        canAccess = false;
        // RETURN ELEMENTS

        for (int i = 0; i < storage.size(); i++) {
            bs = (BaseStationProfile) storage.elementAt(i);
            if ((bs.checkCoordinates(x, y))) {
                bsp.addElement(storage.elementAt(i));
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

    public synchronized void update(Vector v) {
        int i;

        while (!canAccess) {
            try {
                wait();
            } catch (InterruptedException ex) {
                System.out.println("3. Problem on SharedMemory");
            }
        }
        canAccess = false;

        // DELETE OLD PROFILES
        storage.removeAllElements();
        // COPY NEW PROFILES
        if (!v.isEmpty()) {
            for (i = 0; i < v.size(); i++) {
                storage.addElement(v.elementAt(i));
            }
        }
        notifyAll();
        canAccess = true;
    }

}
