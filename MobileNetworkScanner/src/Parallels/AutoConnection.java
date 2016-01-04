package Parallels;

import MobileCore.BaseStationProfile;
import MobileCore.Midlet;

public class AutoConnection extends Thread {

    public Midlet mid;
    public BaseStationProfile bsp;
    int[] Nets;
    String IMEI;
    String IMSI;
    boolean shouldRun;
    boolean manual;
    boolean canAccess;
    boolean interrupted;

    public AutoConnection(Midlet m, String imei, String imsi, int[] nets, int x1, int y1) {

        this.mid = m;
        this.IMEI = imei;
        this.IMSI = imsi;
        this.Nets = nets;
        this.bsp = new BaseStationProfile();
        this.shouldRun = true;
        this.canAccess = true;
        this.interrupted = false;

    }

    public void run() {

        while (shouldRun) {
            synchronized (this) {
                try {
                    System.out.println("Perimenw");
                    wait();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
            System.out.println("Mpika sto autoconnection");
            connect();
        }
    }

    public synchronized void connect() {
        String old = "";
        while (!canAccess) {
            try {
                wait();
            } catch (InterruptedException ex) {
                System.out.println("1. Problem on SharedMemory");
            }
        }
        canAccess = false;
        if (interrupted == true) {
            old = this.mid.BSC.bsp.Basestation_ID;
        }
        if (mid.BSC.isAlive()) {
            mid.BSC.disconnect = true;
            try {
                mid.BSC.join();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        bsp = this.mid.sm.searchSolution(this.mid.ChargingModel.getSelectedIndex(), Nets, old);
        if (bsp != null) {
            mid.BSC = new BaseStationConnection(this.IMEI, this.IMSI, this.mid.x, this.mid.y, bsp, false);
            mid.BSC.ac = this;
            mid.BSC.start();
            while ((mid.BSC.connected == false) && (mid.BSC.disconnected == false)) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
            interrupted = false;
            System.out.println("Vgika eksw");
        }
        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
            System.out.println("2. Problem on SharedMemory");
        }
        notifyAll();
        canAccess = true;
    }

}
