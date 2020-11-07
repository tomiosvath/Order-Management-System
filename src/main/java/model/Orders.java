package model;

/**
 * @author Osvath Tamas
 * @since 2.04.2020
 *
 * Public class for implementing an order object
 * It was used the name orders instead of order because order is a reserved word in sql
 */
public class Orders {
    private int id, clientId, productId, quantity;

    /**
     * Constructor for creating a given order
     * @param clientId id of the client
     * @param productId id of the product
     * @param quantity quantity of the product
     */
    public Orders(int clientId, int productId, int quantity){
        id = 0;
        this.clientId = clientId;
        this.productId = productId;
        this.quantity = quantity;
    }

    /**
     * Constructor without parameters, creates only a "void" object
     * The id is set to -1 in order to be able to check further if a new id was assigned (id must be positive in a database)
     */
    public Orders(){
        id = -1;
        clientId = -1;
        productId = -1;
        quantity = -1;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
