package dao;

import connection.ConnectionFactory;
import model.Client;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Osvath Tamas
 * @since 2.04.2020
 *
 * Class for implementing specific Data Access methods for the Client
 */
public class ClientDAO extends AbstractDAO<Client> {
    private static Logger LOGGER = Logger.getLogger(ClientDAO.class.getName());
    private static final String findClientByName = "select * from client where name = ?";
    private static final String findClient = "select * from client where name = ? and address = ?";

    /**
     * Constructor of the class, calls the parent's constructor
     * @param type Type of the class
     */
    public ClientDAO(Class type) {
        super(type);
    }

    /**
     * Method for finding a client based on its name
     * @param name name of the client
     * @return the Client object
     */
    public Client findByName(String name){
        Client client = new Client();
        Connection connection = ConnectionFactory.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            preparedStatement = connection.prepareStatement(findClientByName);
            preparedStatement.setString(1, name);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                client.setId(resultSet.getInt("id"));
                client.setName(resultSet.getString("name"));
                client.setAddress(resultSet.getString("address"));
            }
            else{
                LOGGER.log(Level.WARNING, "Non existing customer");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Customer find error");
            e.printStackTrace();
        }
        finally{
            ConnectionFactory.close(connection);
            ConnectionFactory.close(preparedStatement);
            ConnectionFactory.close(resultSet);
        }
        return client;
    }

    /**
     * Method for finding a client based on its name and address (in case two clients have the same name)
     * @param name name of the client
     * @param address address of the client
     * @return the Client object
     */
    public Client find(String name, String address){
        Client client = new Client();
        Connection connection = ConnectionFactory.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            preparedStatement = connection.prepareStatement(findClient);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, address);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                client.setId(resultSet.getInt("id"));
                client.setName(resultSet.getString("name"));
                client.setAddress(resultSet.getString("address"));
            }
            else{
                LOGGER.log(Level.WARNING, "Non existing customer");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Customer find error");
            e.printStackTrace();
        }
        finally{
            ConnectionFactory.close(connection);
            ConnectionFactory.close(preparedStatement);
            ConnectionFactory.close(resultSet);
        }
        return client;
    }

    /*
    public static void main(String []args){
        ClientDAO c = new ClientDAO(ClientDAO.class);
        Client c1 = c.findById(1);
        System.out.println(c1.toString());
        c1.setName("alex");
        c.insert(c1);
    }
    */
}

/*public class ClientDAO {
    private static Logger LOGGER = Logger.getLogger(ClientDAO.class.getName());
    private static final String findClientByName = "select * from client where name = ?";
    private static final String findClientById = "select * from client where clientId = ?";
    private static final String insertStatement = "insert into client (name, address) values (?, ?)";

    public static Client findByName(String name){
        Client client = new Client();
        Connection connection = ConnectionFactory.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            preparedStatement = connection.prepareStatement(findClientByName);
            preparedStatement.setString(1, name);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                client.setClientId(resultSet.getInt("clientId"));
                client.setName(resultSet.getString("name"));
                client.setAddress(resultSet.getString("address"));
            }
            else{
                LOGGER.log(Level.WARNING, "Non existing customer");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Customer find error");
            e.printStackTrace();
        }
        finally{
            ConnectionFactory.close(connection);
            ConnectionFactory.close(preparedStatement);
            ConnectionFactory.close(resultSet);
        }
        return client;
    }

    public static Client findById(int id){
        Client client = new Client();
        Connection connection = ConnectionFactory.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            preparedStatement = connection.prepareStatement(findClientById);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                client.setClientId(resultSet.getInt("clientId"));
                client.setName(resultSet.getString("name"));
                client.setAddress(resultSet.getString("address"));
            }
            else{
                LOGGER.log(Level.WARNING, "Non existing customer");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Customer find error");
            e.printStackTrace();
        }
        finally{
            ConnectionFactory.close(connection);
            ConnectionFactory.close(preparedStatement);
            ConnectionFactory.close(resultSet);
        }
        return client;
    }

    public static void insert(Client client){
        Connection connection = ConnectionFactory.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            preparedStatement = connection.prepareStatement(insertStatement);
            preparedStatement.setString(1, client.getName());
            preparedStatement.setString(2, client.getAddress());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally{
            ConnectionFactory.close(connection);
            ConnectionFactory.close(preparedStatement);
            ConnectionFactory.close(resultSet);
        }
    }

    public static void main(String[] args){
        //Client c = findByName("tom");
        Client c = findById(1);
        insert(c);
        System.out.print(c.toString());
    }
}*/
