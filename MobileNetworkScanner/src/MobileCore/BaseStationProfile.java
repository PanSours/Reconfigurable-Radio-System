package MobileCore;

public class BaseStationProfile {

    public String Network_ID;
    public String Basestation_ID;
    public String Provider;
    public double Frequency;
    public double Max_Bitrate;
    public double Guaranteed_Bitrate;
    public double Load_Level;
    public double Signal_Strength;
    public int Network_Type;
    public int Radius_Coverage;
    public int Charge_Type;
    public int Port;
    public int X;
    public int Y;

    public boolean checkCoordinates(int x, int y) {
        double dx = this.X - x;
        double dy = this.Y - y;

        double d = dx * dx + dy * dy;

        double Distance = Math.sqrt(d);
        if ((int) Distance <= this.Radius_Coverage) {
            return true;
        }

        return false;
    }

}
