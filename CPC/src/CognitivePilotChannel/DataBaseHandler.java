/*
 * DataBaseHandler has function to open a connection to DB, close a connection and pass this connection.
 */
package CognitivePilotChannel;

import java.io.FileInputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;

public class DataBaseHandler implements Serializable {

    // SET CONFIGURATION PROPERTIES 
    private String dbUrl = "jdbc:mysql://";
    private String dbDriver;
    private String userId;
    private String password;

    private Connection conn;

    public void setConn(Connection conn) {

        this.conn = conn;
    }

    public Connection getConn() {

        return this.conn;
    }

    //CREATE CONNECTION WITH DATABASE
    public void open() {
        try {
            Class.forName(dbDriver);
            setConn(DriverManager.getConnection(dbUrl, userId, password));
        } catch (Exception ex) {
            Logger.getLogger(DataBaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // READ PROPERTY FILE
    //Load properties
    public void loadProperty() {
        String filename = "";

        final JFileChooser fc = new JFileChooser();

        fc.showOpenDialog(null);

        try {
            filename = fc.getSelectedFile().getName();

        } catch (Exception e) {
            System.out.println("Error in getting input");
            System.exit(1);
        }
        // Read properties file.
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(filename));
            String IP = properties.getProperty("IP");
            int port = 3306;
            String DBname = properties.getProperty("DBname");
            this.dbDriver = properties.getProperty("dbDriver");
            this.dbUrl += IP + ":" + port + "/" + DBname;
            this.dbUrl += "?useUnicode=true&characterEncoding=UTF-8";
            this.userId = properties.getProperty("userId");
            this.password = properties.getProperty("password");
            System.out.println(dbUrl);
        } catch (Exception e) {
            System.err.println("Properties file doesnt have right form!");
            System.exit(1);
        }

    }

    // CLOSE DATABESE AND RESULTS
    public void close() {
        try {
            if (getConn() != null) {
                getConn().close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(DataBaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
