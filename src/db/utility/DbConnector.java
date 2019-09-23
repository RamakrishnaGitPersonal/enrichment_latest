package db.utility;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverAction;
import java.sql.DriverManager;

public class DbConnector implements DriverAction,Cloneable {

    private DbConnector(){}

    static {
       /* init()*/
    }
    public static Connection getDBObject(){

       try {

        Driver driver = new com.mysql.jdbc.Driver();
        // Creating Action Driver
        DriverAction da = new DbConnector();
        // Registering driver by passing driver and driverAction
        DriverManager.registerDriver(driver, da);
        // Creating connection
           Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/enrichment","root","");

       return con;
       }catch (Exception e){
            e.printStackTrace();
       }
       return null;
    }

    @Override
    public void deregister() {

    }
}
