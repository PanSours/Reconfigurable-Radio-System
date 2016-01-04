/* BaseStationProfile has full information of a BaseStation and 
 * contains useful functions in order to handle DataBase executions.
 */
package CognitivePilotChannel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

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
    public long Timestamp;
    public int Status;

    BaseStationProfile() {
        this.Status = 0;
    }

    // ADD PROFILE RECORD TO DB
    public int addToDb(Connection con) {

        PreparedStatement addBSProfile;
        int num = 0;
        Timestamp tmsp = this.getTimeStamp();
        tmsp.setTime(this.Timestamp);

        String addString = "INSERT INTO basestations"
                + "(basestation_id, network_id, signalStrenth, frequency, networkType, maxBitrate, guaranteedBitrate, net_load, provider, R, x, y, port, charging, timestamp) "
                + "VALUES "
                + "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
                + "ON DUPLICATE KEY UPDATE net_load = ?, timestamp = ?";

        try {
            addBSProfile = con.prepareStatement(addString);
            addBSProfile.setString(1, this.Basestation_ID);
            addBSProfile.setString(2, this.Network_ID);
            addBSProfile.setDouble(3, this.Signal_Strength);
            addBSProfile.setDouble(4, this.Frequency);
            addBSProfile.setInt(5, this.Network_Type);
            addBSProfile.setDouble(6, this.Max_Bitrate);
            addBSProfile.setDouble(7, this.Guaranteed_Bitrate);
            addBSProfile.setDouble(8, this.Load_Level);
            addBSProfile.setString(9, this.Provider);
            addBSProfile.setInt(10, this.Radius_Coverage);
            addBSProfile.setInt(11, this.X);
            addBSProfile.setInt(12, this.Y);
            addBSProfile.setInt(13, this.Port);
            addBSProfile.setInt(14, this.Charge_Type);
            addBSProfile.setTimestamp(15, tmsp);
            addBSProfile.setDouble(16, this.Load_Level);
            addBSProfile.setTimestamp(17, tmsp);
            num = addBSProfile.executeUpdate();
            addBSProfile.close();
        } catch (java.sql.SQLException e) {
            System.err.println(e);
            return num;
        }

        return num;
    }

    // ADD PROFILE RECORD TO DB
    public int updateToDb(Connection con) {

        PreparedStatement updateBSProfile;
        int num = 0;
        Timestamp tmsp = this.getTimeStamp();
        tmsp.setTime(this.Timestamp);

        String updateString = "UPDATE basestations "
                + "SET net_load = ?, timestamp = ? "
                + "WHERE basestations.basestation_id = ? ";

        try {
            updateBSProfile = con.prepareStatement(updateString);
            updateBSProfile.setDouble(1, this.Load_Level);
            updateBSProfile.setTimestamp(2, tmsp);
            updateBSProfile.setString(3, this.Basestation_ID);
            num = updateBSProfile.executeUpdate();
            updateBSProfile.close();
        } catch (java.sql.SQLException e) {
            System.err.println(e);
            return num;
        }

        return num;
    }

    // DELETE PROFILE TO DB
    public int deleteToDb(Connection con) {

        PreparedStatement deleteBSProfile;
        ResultSet rs;
        int num = 0;

        String deleteString = "DELETE "
                + "FROM basestations "
                + "WHERE basestations.basestation_id = ?";

        try {
            deleteBSProfile = con.prepareStatement(deleteString);
            deleteBSProfile.setString(1, this.Basestation_ID);
            num = deleteBSProfile.executeUpdate();
            deleteBSProfile.close();
        } catch (java.sql.SQLException e) {
            System.err.println(e);
            return num;
        }

        return num;
    }

    // SEARCH  PROFILE RECORD FROM DB
    public int searchBSFromDb(Connection con) {

        PreparedStatement searchBSProfile;
        ResultSet rs;
        int num = 0;

        String searchString = "SELECT basestation_id "
                + "FROM basestations "
                + "WHERE basestations.basestation_id = ?";

        try {
            searchBSProfile = con.prepareStatement(searchString);
            searchBSProfile.setString(1, this.Basestation_ID);
            rs = searchBSProfile.executeQuery();
            // extract data from the ResultSet
            if (rs.next()) {
                num = 1;
            }
            searchBSProfile.close();
        } catch (java.sql.SQLException e) {
            System.err.println(e);
            return num;
        }

        return num;
    }

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

    public java.sql.Timestamp getTimeStamp() {
        Calendar calendar = Calendar.getInstance();
        Date now = calendar.getTime();
        Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());
        return currentTimestamp;
    }

}
