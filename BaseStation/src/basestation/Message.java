/*
 * This class Message handle messages.
 */
package basestation;

public class Message {

    String Message_Type;

    //Given a String, returns the object of Message type
    public Message fromString(String S) {

        String[] subs;

        try {

            if (S.startsWith("DISCONNECT")) {

                Disconnect d = new Disconnect();
                d.Message_Type = "D";
                subs = S.split("#");
                d.setIMEI(subs[1]);
                return d;

            } else if (S.startsWith("CONNECT")) {

                Connect c = new Connect();
                c.Message_Type = "C";
                subs = S.split("#");
                c.setIMEI(subs[1]);
                c.setIMSI(subs[2]);
                c.setX(subs[3]);
                c.setY(subs[4]);
                return c;

            } else if (S.startsWith("OK")) {

                OK ok = new OK();
                ok.Message_Type = "OK";
                return ok;

            } else if (S.startsWith("NO_COVERAGE")) {

                NoCoverage nc = new NoCoverage();
                nc.Message_Type = "NO";
                return nc;

            } else if (S.startsWith("OVERLOADED")) {

                OverLoaded ol = new OverLoaded();
                ol.Message_Type = "OVER";
                return ol;

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

class Connect extends Message {

    private String IMEI;
    private String IMSI;
    private String X;
    private String Y;

    @Override
    public String toString() {

        return "CONNECT " + this.IMEI + " " + IMSI + " %";

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

class Disconnect extends Message {

    private String IMEI;

    @Override
    public String toString() {
        return "DISCONNECT%";
    }

    public void setIMEI(String imei) {
        this.IMEI = imei;
    }

    String getIMEI() {
        return this.IMEI;
    }

}

class OK extends Message {

    @Override
    public String toString() {
        return "OK%";
    }

}

class NoCoverage extends Message {

    @Override
    public String toString() {
        return "NO COVERAGE%";
    }

}

class OverLoaded extends Message {

    @Override
    public String toString() {
        return "OVERLOADED%";
    }

}
