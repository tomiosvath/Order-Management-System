package dao;

import connection.ConnectionFactory;
import model.Product;

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
 * Class for implementing specific Data Access methods for the Product
 */
public class ProductDAO extends AbstractDAO<Product>{
    private static Logger LOGGER = Logger.getLogger(ProductDAO.class.getName());
    private static final String findProductByName = "select * from product where name = ?";

    /**
     * Constructor of the class, calls the parent's constructor
     * @param type Type of the class
     */
    public ProductDAO(Class type){
        super(type);
    }

    /**
     * Method for finding a product based on its name
     * @param name name of the product
     * @return the Product object
     */
    public Product findByName(String name){
        Product product = new Product();
        Connection connection = ConnectionFactory.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            preparedStatement = connection.prepareStatement(findProductByName);
            preparedStatement.setString(1, name);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                product.setId(resultSet.getInt("id"));
                product.setName(resultSet.getString("name"));
                product.setStock(resultSet.getInt("stock"));
                product.setPrice(resultSet.getDouble("price"));
            }
            else{
                LOGGER.log(Level.WARNING, "Non existing product");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Product find error");
            e.printStackTrace();
        }
        finally{
            ConnectionFactory.close(connection);
            ConnectionFactory.close(preparedStatement);
            ConnectionFactory.close(resultSet);
        }
        return product;
    }


    /*public void insert(Product product){
        Product existingProduct = findByName(product.getName());
        //if the id is -1, the product doesn't exist
        if (existingProduct.getId() == -1){
            super.insert(product);
        }
        else{
            existingProduct.setStock(existingProduct.getStock() + product.getStock());
            super.update(existingProduct, existingProduct.getId());
        }
        //if non existent, insert
        //otherwise update
    }*/
    /*private static Logger LOGGER = Logger.getLogger(ProductDAO.class.getName());
    private static final String findProductByName = "select * from product where name = ?";
    private static final String findProductById = "select * from product where productId = ?";
    private static final String insertStatement = "insert into product (name, stock, price) values (?, ?, ?)";

    public static Product findByName(String name){
        Product product = new Product();
        Connection connection = ConnectionFactory.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            preparedStatement = connection.prepareStatement(findProductByName);
            preparedStatement.setString(1, name);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                product.setProductId(resultSet.getInt("productId"));
                product.setName(resultSet.getString("name"));
                product.setStock(resultSet.getInt("stock"));
                product.setPrice(resultSet.getDouble("price"));
            }
            else{
                LOGGER.log(Level.WARNING, "Non existing product");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Product find error");
            e.printStackTrace();
        }
        finally{
            ConnectionFactory.close(connection);
            ConnectionFactory.close(preparedStatement);
            ConnectionFactory.close(resultSet);
        }
        return product;
    }

    public static Product findById(int id){
        Product product = new Product();
        Connection connection = ConnectionFactory.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            preparedStatement = connection.prepareStatement(findProductById);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                product.setProductId(resultSet.getInt("productId"));
                product.setName(resultSet.getString("name"));
                product.setStock(resultSet.getInt("stock"));
                product.setPrice(resultSet.getDouble("price"));
            }
            else{
                LOGGER.log(Level.WARNING, "Non existing product");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Product find error");
            e.printStackTrace();
        }
        finally{
            ConnectionFactory.close(connection);
            ConnectionFactory.close(preparedStatement);
            ConnectionFactory.close(resultSet);
        }
        return product;
    }

    public static void insert(Product product){
        Connection connection = ConnectionFactory.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            preparedStatement = connection.prepareStatement(insertStatement);
            preparedStatement.setString(1, product.getName());
            preparedStatement.setInt(2, product.getStock());
            preparedStatement.setDouble(3, product.getPrice());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Product insertion error");
            e.printStackTrace();
        }
        finally{
            ConnectionFactory.close(connection);
            ConnectionFactory.close(preparedStatement);
            ConnectionFactory.close(resultSet);
        }
    }

    public static void main(String[] args){

    }*/
}
