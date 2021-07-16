package iot.unipi.it;

import java.sql.*;

public class SmartPoolDbManager {
	private static final ConfigurationReader configurationReader = ConfigurationReader.getInstance();
	private static SmartPoolDbManager instance = null;
	
	
    @SuppressWarnings("finally")
	private static Connection makeJDBCConnection() {
        Connection databaseConnection = null;
        
        String databaseIP = configurationReader.getDbIp();
        String databasePort = configurationReader.getDbPort();
        String databaseUsername = configurationReader.getDbUsername();
        String databasePassword = configurationReader.getDbPassword();
        String databaseName = configurationReader.getDbName();
        
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");//checks if the Driver class exists (correctly available)
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return databaseConnection;
        }
        
        try {
            // DriverManager: The basic service for managing a set of JDBC drivers.
            databaseConnection = DriverManager.getConnection(
                    "jdbc:mysql://" + databaseIP + ":" + databasePort +
                            "/" + databaseName + "?zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=CET",
                    databaseUsername,
                    databasePassword);
            //The Driver Manager provides the connection specified in the parameter string
            if (databaseConnection == null) {
                System.err.println("Connection to Db failed");
            }
        } catch (SQLException e) {
        	System.err.println("MySQL Connection Failed!\n");
            e.printStackTrace();
        }finally {
            return databaseConnection;
        }
    }
    
    
    public static void logHydromassageON(int power) {
    	
    	String insertQueryStatement = "INSERT INTO hydromassage_actuator (power) VALUES (?)";
        
        try (Connection smartPoolConnection = makeJDBCConnection();
        		PreparedStatement smartPoolPrepareStat = smartPoolConnection.prepareStatement(insertQueryStatement);
           ) {
        	                
        	smartPoolPrepareStat.setInt(1, power);
        	smartPoolPrepareStat.executeUpdate();
            
        } catch (SQLException sqlex) {
            sqlex.printStackTrace();
        }
    }
    
    
    public static void logHydromassageOFF() {
    	
    	logHydromassageON(0);
    }


    public static void logPersonInThePool(boolean present) {
    	
    	String insertQueryStatement = "INSERT INTO presence_detection (presence) VALUES (?)";
        
        try (Connection smartPoolConnection = makeJDBCConnection();
        		PreparedStatement smartPoolPrepareStat = smartPoolConnection.prepareStatement(insertQueryStatement);
           ) {
        	smartPoolPrepareStat.setBoolean(1, present);
        	                
        	smartPoolPrepareStat.executeUpdate();
            
        } catch (SQLException sqlex) {
            sqlex.printStackTrace();
        }
    }
    
    
    public static void logTemperatureActuator(boolean signal) {
    	String insertQueryStatement = "INSERT INTO temperature_actuator (signalVal) VALUES (?)";
        
        try (Connection smartPoolConnection = makeJDBCConnection();
        		PreparedStatement smartPoolPrepareStat = smartPoolConnection.prepareStatement(insertQueryStatement);
           ) {
        	smartPoolPrepareStat.setBoolean(1, signal);
        	                
        	smartPoolPrepareStat.executeUpdate();
            
        } catch (SQLException sqlex) {
            sqlex.printStackTrace();
        }
    }
    
    
    public static void logChlorineActuator(boolean signal) {
    	String insertQueryStatement = "INSERT INTO chlorine_actuator (signalVal) VALUES (?)";
        
        try (Connection smartPoolConnection = makeJDBCConnection();
        		PreparedStatement smartPoolPrepareStat = smartPoolConnection.prepareStatement(insertQueryStatement);
           ) {
        	smartPoolPrepareStat.setBoolean(1, signal);
        	                
        	smartPoolPrepareStat.executeUpdate();
            
        } catch (SQLException sqlex) {
            sqlex.printStackTrace();
        }
    }
    
    
    public static void logTemperatureSample(final String node, final double sample) {
    	String insertQueryStatement = "INSERT INTO water_temperature (nodeId, value) VALUES (?, ?)";
    	
    	try (Connection smartPoolConnection = makeJDBCConnection();
        		PreparedStatement smartPoolPrepareStat = smartPoolConnection.prepareStatement(insertQueryStatement);
           ) {
        	smartPoolPrepareStat.setString(1, node);
        	smartPoolPrepareStat.setFloat(2,(float)sample);
        	                
        	smartPoolPrepareStat.executeUpdate();
            
        } catch (SQLException sqlex) {
            sqlex.printStackTrace();
        }
    }
    
    
    public static void logChlorineSample(final String node, final double sample) {
    	String insertQueryStatement = "INSERT INTO chlorine_levels (nodeId, value) VALUES (?, ?)";
    	
    	try (Connection smartPoolConnection = makeJDBCConnection();
        		PreparedStatement smartPoolPrepareStat = smartPoolConnection.prepareStatement(insertQueryStatement);
           ) {
        	smartPoolPrepareStat.setString(1, node);
        	smartPoolPrepareStat.setFloat(2,(float)sample);
        	                
        	smartPoolPrepareStat.executeUpdate();
            
        } catch (SQLException sqlex) {
            sqlex.printStackTrace();
        }
    }
    
}
