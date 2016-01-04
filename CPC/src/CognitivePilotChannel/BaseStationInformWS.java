/*
 * This class contains the operation which informs DataBase when BaseStations apply for insertion
 */
package CognitivePilotChannel;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

/**
 *
 * @author Kiddo
 */
@WebService(serviceName = "BaseStationInformWS")
public class BaseStationInformWS {

    private SharedMemory sm;

    public BaseStationInformWS(SharedMemory m) {
        this.sm = m;

    }

    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "hello")
    public String hello(@WebParam(name = "name") String txt) {
        return "Hello " + txt + " !";
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "setBSProfile")
    public String setBSProfile(@WebParam(name = "bsp") BaseStationProfile bsp) {

        /* This operation accept BaseStation Profiles and execute update on DataBase via INSERT
         * with specification ON DUPLICATE KEY UPDATE. If you specify ON DUPLICATE KEY UPDATE, 
         * and a row is inserted that would cause a duplicate value in a UNIQUE index or PRIMARY KEY, 
         * an UPDATE of the old row is performed.
         */
        int num;

        num = this.sm.update(bsp);
        return "OK" + num;
    }

}
