package dao;

import connection.ConnectionFactory;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The type Abstract dao.
 *
 * @param <T> the type parameter
 */
public class AbstractDAO <T> {
    /**
     * The constant LOGGER.
     */
    private static final Logger LOGGER = Logger.getLogger(AbstractDAO.class.getName());

    private final Class<T> type;

    /**
     * Instantiates a new Abstract dao.
     *
     * @param type the type
     */
    public AbstractDAO(Class<T> type) {
        this.type = (Class <T>) ((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    private String createSelectQuery(String field){
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM ");
        sb.append(type.getSimpleName());
        sb.append(" WHERE " + field + " = ?");
        return sb.toString();
    }

    /**
     * Find by id t.
     *
     * @param id the id
     * @return the t
     */
    public T findById(int id){
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String query = createSelectQuery("id");
        try{
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            return createObjects(resultSet).get(0);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO: findByID: " + e.getMessage());
        }
        finally{
            ConnectionFactory.close(connection);
            ConnectionFactory.close(statement);
            ConnectionFactory.close(resultSet);
        }
        return null;
    }

    private String createInsertQuery(T object){
        StringBuilder sb = new StringBuilder();
        sb.append("INSERT INTO " + type.getSimpleName() + " ( ");

        boolean isFirst = true;
        for (Field field: type.getDeclaredFields()){
            if (!field.getName().equals("id")) {
                if (!isFirst) sb.append(", ");
                sb.append(field.getName());
                isFirst = false;
            }
        }

        sb.append(") values (");

        isFirst = true;
        for (Field field: type.getDeclaredFields()){
            if (!field.getName().equals("id")){
                if (!isFirst) sb.append(", ");
                sb.append("?");
                isFirst = false;
            }
        }
        sb.append(")");
        return sb.toString();
    }

    /**
     * Insert.
     *
     * @param object the object
     */
    public void insert(T object){
        Connection connection = null;
        PreparedStatement statement = null;
        String query = createInsertQuery(object);
        try{
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            int nrValue = 1;
            for (Field field: type.getDeclaredFields()){
                if (!field.getName().equals("id")){
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), type);
                    Method method = propertyDescriptor.getReadMethod();
                    Object writeData = method.invoke(object);
                    statement.setObject(nrValue, writeData);
                    nrValue++;
                }
            }
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO: insert: " + e.getMessage());
        } catch (IntrospectionException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        } finally{
            ConnectionFactory.close(connection);
            ConnectionFactory.close(statement);
        }
    }

    private String createDeleteQuery(String field){
        StringBuilder sb = new StringBuilder();
        sb.append("DELETE FROM ");
        sb.append(type.getSimpleName());
        sb.append(" WHERE " + field + " = ?");
        return sb.toString();
    }

    /**
     * Delete by id.
     *
     * @param id the id
     */
    public void deleteById(int id){
        Connection connection = null;
        PreparedStatement statement = null;
        String query = createDeleteQuery("id");
        try{
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            statement.setObject(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, type.getName() + "DAO: deleteByID: " + e.getMessage());
        } finally{
            ConnectionFactory.close(connection);
            ConnectionFactory.close(statement);
        }
    }

    private String createUpdateQuery(String newField){
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE ");
        sb.append(type.getSimpleName());
        sb.append(" SET ");
        for(Field field : type.getDeclaredFields()) {
            sb.append(field.getName() + "=?,");
        }

        sb.deleteCharAt(sb.length()-1);
        sb.append(" WHERE " + newField + "=?");
        return sb.toString();

    }

    /**
     * Update.
     *
     * @param object the object
     * @param id     the id
     */
    public void update(T object, int id){
        Connection connection = null;
        PreparedStatement statement = null;
        String query = createUpdateQuery("id");
        try{
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement(query);
            int pos = 1;
            for (Field field: type.getDeclaredFields()){
                field.setAccessible(true);
                Object value = field.get(object);
                statement.setString(pos, value.toString());
                pos++;
            }
            statement.setInt(pos, id);
            statement.executeUpdate();
        } catch (SQLException | IllegalAccessException e) {
            e.printStackTrace();
        }
        finally{
            ConnectionFactory.close(connection);
            ConnectionFactory.close(statement);
        }
    }

    private List<T> createObjects(ResultSet resultSet){
        List<T> list = new ArrayList<T>();
        try{
            while (resultSet.next()){
                T instance = type.newInstance();
                for (Field field: type.getDeclaredFields()){
                    Object value = resultSet.getObject(field.getName()); //gets the field's value
                    PropertyDescriptor propertyDescriptor = new PropertyDescriptor(field.getName(), type);//gets the field's type
                    Method method = propertyDescriptor.getWriteMethod();//gets the method for the writing
                    method.invoke(instance, value);//invokes the method
                }
                list.add(instance);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IntrospectionException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * Select all query string.
     *
     * @return the string
     */
    public String selectAllQuery(){
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM ");
        sb.append(type.getSimpleName());
        return sb.toString();
    }

    /**
     * Select all list.
     *
     * @return the list
     */
    public List<T> selectAll(){
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        String query = selectAllQuery();
        ResultSet resultSet = null;
        try{
            connection = ConnectionFactory.getConnection();
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            return createObjects(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally{
            ConnectionFactory.close(connection);
            ConnectionFactory.close(preparedStatement);
            ConnectionFactory.close(resultSet);
        }
        return null;
    }
}
