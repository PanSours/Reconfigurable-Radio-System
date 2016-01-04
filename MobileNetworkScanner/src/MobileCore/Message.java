package MobileCore;

import java.util.Vector;

public class Message {

    public String Message_Type;

    //Given a String, returns the object of Message type
    public Message fromString(String S) {

        String[] subs;

        try {
            if (S.startsWith("DISCOVER")) {

                Discover d = new Discover();
                d.Message_Type = "DISCOVER";
                subs = stringToArray(S, "#");
                d.setIMEI(subs[1]);
                d.setX(subs[2]);
                d.setY(subs[3]);
                return d;

            } else if (S.startsWith("DISCONNECT")) {

                Disconnect d = new Disconnect();
                d.Message_Type = "DISCONNECT";
                return d;

            } else if (S.startsWith("OK")) {

                //OK ok = new OK();
                this.Message_Type = "OK";
                return this;

            } else if (S.startsWith("NO_COVERAGE")) {

                //NoCoverage nc = new NoCoverage();
                this.Message_Type = "NO";
                return this;

            } else if (S.startsWith("OVERLOADED")) {

                //OverLoaded ol = new OverLoaded();
                this.Message_Type = "OVER";
                return this;

            } else if (S.startsWith("PROFILES")) {

                Profiles pr = new Profiles();
                pr.Message_Type = "PRO";
                pr.profiles = new Vector();
                String[] profiles;
                String[] fields;

                if (!S.equals("PROFILES#%")) {

                    // SPLIT
                    profiles = stringToArray(S, "#BEGIN#");
                    for (int i = 1; i < profiles.length; i++) {
                        fields = stringToArray(profiles[i], "#");
                        BaseStationProfile bs = new BaseStationProfile();
                        bs.Basestation_ID = fields[0];
                        bs.Network_ID = fields[1];
                        bs.Provider = fields[2];
                        bs.Frequency = Double.parseDouble(fields[3]);
                        bs.Max_Bitrate = Double.parseDouble(fields[4]);
                        bs.Guaranteed_Bitrate = Double.parseDouble(fields[5]);
                        bs.Load_Level = Double.parseDouble(fields[6]);
                        bs.Signal_Strength = Double.parseDouble(fields[7]);
                        bs.Network_Type = Integer.parseInt(fields[8]);
                        bs.Port = Integer.parseInt(fields[9]);
                        bs.Radius_Coverage = Integer.parseInt(fields[10]);
                        bs.Charge_Type = Integer.parseInt(fields[11]);
                        bs.X = Integer.parseInt(fields[12]);
                        bs.Y = Integer.parseInt(fields[13]);
                        pr.profiles.addElement(bs);
                    }
                }
                return pr;
            } else {

                Message_Type = "BULLSHIT";
                return this;
            }

        } catch (Exception e) {
            System.err.println("Ouups");
            return this;
        }

    }

    public static String[] stringToArray(String a, String delimeter) {
        String c[] = new String[0];
        String b = a;
        while (true) {
            int i = b.indexOf(delimeter);
            String d = b;
            if (i >= 0) {
                d = b.substring(0, i);
            }
            String e[] = new String[c.length + 1];
            for (int k = 0; k < c.length; k++) {
                e[k] = c[k];
            }
            e[e.length - 1] = d;
            c = e;
            b = b.substring(i + delimeter.length(), b.length());
            if (b.length() <= 0 || i < 0) {
                break;
            }
        }
        return c;
    }

}
