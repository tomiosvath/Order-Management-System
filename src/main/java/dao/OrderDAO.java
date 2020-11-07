package dao;

import model.Orders;

import java.util.logging.Logger;

/**
 * @author Osvath Tamas
 * @since 2.04.2020
 *
 * Class for implementing specific Data Access methods for the Order
 */
public class OrderDAO extends  AbstractDAO<Orders>{
    private static Logger LOGGER = Logger.getLogger(OrderDAO.class.getName());

    /**
     * Constructor of the class, calls the parent's constructor
     * @param type Type of the class
     */
    public OrderDAO(Class type) {
        super(type);
    }

    /*private static Logger LOGGER = Logger.getLogger(OrderDAO.class.getName());
    private static final String findProductByCustomerId = "select * from order where customerId = ?";
    private static final String insertStatement = "insert into order (customerId, productId, quantity) values (?, ?, ?)";

    public static Order findById(int id){
        Order order = new Order();
        Connection connection = ConnectionFactory.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try{
            preparedStatement = connection.prepareStatement(findProductByCustomerId);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                order.setOrderId(resultSet.getInt("orderId"));
                order.setCustomerId(resultSet.getInt("customerId"));
                order.setProductId(resultSet.getInt("productId"));
                order.setQuantity(resultSet.getInt("quantity"));
            }
            else{
                LOGGER.log(Level.WARNING, "The order doesn't exist");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Error finding the order");
            e.printStackTrace();
        }
        finally {
            ConnectionFactory.close(connection);
            ConnectionFactory.close(preparedStatement);
            ConnectionFactory.close(resultSet);
        }

        return order;
    }

    public static void insert(Order order){
        Connection connection = ConnectionFactory.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            preparedStatement = connection.prepareStatement(insertStatement);
            preparedStatement.setInt(1, order.getCustomerId());
            preparedStatement.setInt(2, order.getProductId());
            preparedStatement.setDouble(3, order.getQuantity());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Order insertion error");
            e.printStackTrace();
        }
        finally{
            ConnectionFactory.close(connection);
            ConnectionFactory.close(preparedStatement);
            ConnectionFactory.close(resultSet);
        }
    }*/

}
