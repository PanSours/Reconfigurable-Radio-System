/*
 * Message class construct messages depence on inherritance
 */
package CognitivePilotChannel;

import java.util.Vector;

public class Message {

    String Message_Type;

    //Given a String, returns the object of Message type
    public Message fromString(String S) {

        String[] subs;

        try {
            if (S.startsWith("DISCOVER")) {

                Discover d = new Discover();
                d.Message_Type = "DISC";
                subs = S.split("#");
                d.setIMEI(subs[1]);
                d.setX(subs[2]);
                d.setY(subs[3]);
                return d;
            } else {

                Message_Type = "BULLSHIT";
                return this;
            }

        } catch (Exception e) {

            System.err.println("Ouups");
            return this;

        }

    }

}

class Discover extends Message {

    private String IMEI;
    private int X;
    private int Y;

    @Override
    public String toString() {

        return "DISCOVER#" + this.IMEI + "#" + this.X + "#" + this.Y + "%";
    }

    public void setIMEI(String imei) {

        this.IMEI = imei;
    }

    public void setX(String x) {

        this.X = Integer.parseInt(x);
    }

    public void setY(String y) {

        this.Y = Integer.parseInt(y);
    }

    String getIMEI() {

        return this.IMEI;
    }

    int getX() {

        return this.X;
    }

    int getY() {

        return this.Y;
    }

}

class Profiles extends Message {

    public String toString(Vector<BaseStationProfile> v) {

        String profiles = "PROFILES#";
        String prof;

        for (int i = 0; i < v.size(); i++) {
            prof = "BEGIN#" + v.get(i).Basestation_ID + "#" + v.get(i).Network_ID + "#" + v.get(i).Provider + "#"
                    + v.get(i).Frequency + "#" + v.get(i).Max_Bitrate + "#" + v.get(i).Guaranteed_Bitrate + "#"
                    + +v.get(i).Load_Level + "#" + v.get(i).Signal_Strength + "#"
                    + +v.get(i).Network_Type + "#" + v.get(i).Radius_Coverage + "#" + v.get(i).Radius_Coverage + "#"
                    + v.get(i).Charge_Type + "#" + v.get(i).X + "#" + v.get(i).Y + "#END#";
            profiles += prof;
        }

        return profiles + "%";
    }

}
