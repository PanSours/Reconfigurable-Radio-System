/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MobileCore;

public class Discover extends Message {

    private String IMEI;
    private String X;
    private String Y;

    public String toString() {

        return "DISCOVER#" + this.IMEI + "#" + this.X + "#" + this.Y + "%";
    }

    public void setIMEI(String imei) {

        this.IMEI = imei;
    }

    public void setX(String x) {

        this.X = x;
    }

    public void setY(String y) {

        this.Y = y;
    }

    public String getIMEI() {

        return this.IMEI;
    }

    public String getX() {

        return this.X;
    }

    public String getY() {

        return this.Y;
    }

}
