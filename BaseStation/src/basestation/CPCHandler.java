/* 
 * In this class, we inform periodically the CPC about vital Base Station information. This operation is repeated
 * every T seconds, as it is mentioned in property file. Base Station sends an INFORMATION type message, with many
 * vital information such as port, coordinates, radius coverage etc.
 */
package basestation;

import java.util.Properties;

public class CPCHandler extends Thread {

    public String Network_ID;
    public String Basestation_ID;
    public String Network_Type;
    public String Frequency;
    public String Max_Bitrate;
    public String Guaranteed_Bitrate;
    public String Provider;
    public String Charge_Type;
    public int X;
    public int Y;
    public volatile int MCounts;
    public volatile int Load_Level;
    public int Radius_Coverage;
    public int Port;
    public int Signal_Strength;
    public int T_Period;
    public Properties properties;
    public boolean shouldRun;
    public boolean flag = false;
    BaseStationHandler bs;

    CPCHandler(Properties p, BaseStationHandler bs) {

        this.properties = p;
        this.shouldRun = true;
        this.bs = bs;

    }

    @Override
    public void run() {

        String s = null;
        this.loadProperty();

        while (this.shouldRun) {

            try {

                // WEB SERVICE
                try {
                    String s1 = setBSProfile(this.createBSProfile());
                    System.out.println(s1);
                } catch (Exception ex) {
                    System.err.println("Problem with CPC: " + ex);
                }

                Thread.sleep(this.T_Period * 1000);

            } catch (InterruptedException e) {
                System.err.println("Problem with CPC!");
                System.exit(1);
            }

        }

    }

    //Inform CPC for Base Station status
    public void informCPC() {

        System.out.println("PROFILE ");
        System.out.println("Network_ID= " + Network_ID);
        System.out.println("Basestation_ID= " + Basestation_ID);
        System.out.println("Network_Type= " + Network_Type);
        System.out.println("Frequency= " + Frequency);
        System.out.println("Max_Bitrate= " + Max_Bitrate);
        System.out.println("Guaranteed_Bitrate= " + Guaranteed_Bitrate);
        System.out.println("Provider= " + Provider);
        System.out.println("Charge_Type= " + Charge_Type);
        System.out.println("x= " + X);
        System.out.println("y= " + Y);
        System.out.println("Load_Level= " + Load_Level);
        System.out.println("Radius_Coverage= " + Radius_Coverage);
        System.out.println("Port= " + Port);
        System.out.println("Signal_Strength= " + Signal_Strength);
        System.out.println("Load_Level= " + this.bs.Load_Level);

    }

    //Load properties
    public void loadProperty() {

        try {
            Network_ID = properties.getProperty("Network_ID");
            Basestation_ID = properties.getProperty("Basestation_ID");
            Network_Type = properties.getProperty("Network_Type");
            Frequency = properties.getProperty("Frequency");
            Max_Bitrate = properties.getProperty("Max_Bitrate");
            Guaranteed_Bitrate = properties.getProperty("Guaranteed_Bitrate");
            Provider = properties.getProperty("Provider");
            Charge_Type = properties.getProperty("Charge_Type");
            X = Integer.parseInt(properties.getProperty("x"));
            Y = Integer.parseInt(properties.getProperty("y"));
            Radius_Coverage = Integer.parseInt(properties.getProperty("Radius_Coverage"));
            Port = Integer.parseInt(properties.getProperty("Port"));
            Signal_Strength = Integer.parseInt(properties.getProperty("Signal_Strength"));
            T_Period = Integer.parseInt(properties.getProperty("T_Period"));
        } catch (Exception e) {
            System.err.println("Properties file doesnt have right form!");
            System.exit(1);
        }

    }

    public cognitivepilotchannel.BaseStationProfile createBSProfile() {

        cognitivepilotchannel.BaseStationProfile p = new cognitivepilotchannel.BaseStationProfile();

        p.setNetworkID(this.Network_ID);
        p.setBasestationID(this.Basestation_ID);
        p.setChargeType(Integer.parseInt(this.Charge_Type));
        p.setFrequency(Double.parseDouble(this.Frequency));
        p.setLoadLevel(this.Load_Level);
        p.setGuaranteedBitrate(Double.parseDouble(this.Guaranteed_Bitrate));
        p.setMaxBitrate(Double.parseDouble(this.Max_Bitrate));
        p.setProvider(this.Provider);
        p.setSignalStrength(this.Signal_Strength);
        p.setPort(this.Port);
        p.setX(this.X);
        p.setY(this.Y);
        p.setRadiusCoverage(this.Radius_Coverage);
        p.setNetworkType(Integer.parseInt(this.Network_Type));

        return p;

    }

    private static String setBSProfile(cognitivepilotchannel.BaseStationProfile bsp) {
        cognitivepilotchannel.BaseStationInformWS_Service service = new cognitivepilotchannel.BaseStationInformWS_Service();
        cognitivepilotchannel.BaseStationInformWS port = service.getBaseStationInformWSPort();
        return port.setBSProfile(bsp);
    }

}
