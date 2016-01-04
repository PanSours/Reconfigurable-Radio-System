/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MobileCore;

public class Disconnect extends Message {

    private String IMEI;

    public String toString() {

        return "DISCONNECT#" + IMEI + "%";
    }

    public void setIMEI(String imei) {

        this.IMEI = imei;
    }

    String getIMEI() {

        return this.IMEI;
    }

}
