package connection;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Osvath Tamas
 * @since 2.04.2020
 *
 * Class for providing connection method between the project and a sql database
 */
public class ConnectionFactory {
    private static final Logger LOGGER = Logger.getLogger(ConnectionFactory.class.getName());
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/schooldb";
    private static final String USER = "root";
    private static final String PASS = "root";
    private static ConnectionFactory singleInstance = new ConnectionFactory();

    /**
     * Constructor for the the ConnectionFactory class
     */
    private ConnectionFactory() {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method for creating a connection to the specified database
     * @return the connection
     */
    private Connection createConnection(){
        Connection connection = null;
        try{
            connection = DriverManager.getConnection(URL, USER, PASS);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Error occured while trying to connect to the database");
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * Method for returning a connection
     * @return the connection
     */
    public static Connection getConnection(){
        return singleInstance.createConnection();
    }

    /**
     * Method for closing a connection
     * @param connection the Connection object
     */
    public static void close(Connection connection){
        if (connection != null){
            try {
                connection.close();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "An error occured while trying to close the connection");
                e.printStackTrace();
            }
        }
    }

    /**
     * Method for closing a statement
     * @param statement the Statement object
     */
    public static void close(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "An error occured while trying to close the statement");
            }
        }
    }

    /**
     * Method for closing a resultSet
     * @param resultSet the ResultSet object
     */
    public static void close(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "An error occured while trying to close the ResultSet");
            }
        }
    }

    /*public static void main(String []args){
        String SQL_Select = "Select * from client";

        try {
            Connection c = singleInstance.createConnection();
            PreparedStatement preparedStatement = c.prepareStatement(SQL_Select);
            ResultSet results = preparedStatement.executeQuery();
            while (results.next()) {
                Client client = new Client("", "");
                client.setId(results.getInt("id"));
                client.setName(results.getString("name"));
                client.setAddress(results.getString("address"));
                System.out.println(client.toString());
            }
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            //e.printStackTrace();
        }
        /*try(Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/schooldb", "root", "Vz6zcs@@")) {
            PreparedStatement preparedStatement = conn.prepareStatement(SQL_Select);
            ResultSet results = preparedStatement.executeQuery();

            while (results.next()){
                int id = results.getInt("clientID");
                String name = results.getString("name");
                String address = results.getString("address");
                System.out.println(id + " " + name + " " + address);
            }
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
            //e.printStackTrace();
        }*/
}
