/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package MobileCore;

import java.util.Vector;

public class Profiles extends Message {

    public Vector profiles;

    public String toString(Vector v) {

        BaseStationProfile bs = new BaseStationProfile();
        String profs = "PROFILES#";
        String prof;

        for (int i = 0; i < v.size(); i++) {
            bs = (BaseStationProfile) v.elementAt(i);
            prof = "BEGIN#" + bs.Basestation_ID + "#" + bs.Network_ID + "#" + bs.Provider + "#"
                    + bs.Frequency + "#" + bs.Max_Bitrate + "#" + bs.Guaranteed_Bitrate + "#"
                    + +bs.Load_Level + "#" + bs.Signal_Strength + "#"
                    + +bs.Network_Type + "#" + bs.Port + "#" + bs.Radius_Coverage + "#"
                    + bs.Charge_Type + "#" + bs.X + "#" + bs.Y + "#END#";
            profs += prof;
        }

        return profs + "%";
    }

}
