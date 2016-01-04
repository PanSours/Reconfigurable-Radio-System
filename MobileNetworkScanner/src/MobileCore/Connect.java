/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MobileCore;

public class Connect extends Message {

    private String IMEI;
    private String IMSI;
    private String X;
    private String Y;

    public void Message() {
    }

    ;
        
    public String toString() {

        return "CONNECT#" + this.IMEI + "#" + this.IMSI + "#" + this.X + "#" + this.Y + "%";
    }

    public boolean checkCoordinates(int x, int y, int rc) {

        double dx = Integer.parseInt(X) - x;
        double dy = Integer.parseInt(Y) - y;

        double d = dx * dx + dy * dy;

        double Distance = Math.sqrt(d);
        if ((int) Distance <= rc) {
            return true;
        }

        return false;

    }

    public void setIMEI(String imei) {

        this.IMEI = imei;
    }

    public void setIMSI(String imsi) {

        this.IMSI = imsi;
    }

    public void setX(String x) {

        this.X = x;
    }

    public void setY(String y) {

        this.Y = y;
    }

    String getIMEI() {

        return this.IMEI;
    }

    String getIMSI() {

        return this.IMSI;
    }

    String getX() {

        return this.X;
    }

    String getY() {

        return this.Y;
    }
}
