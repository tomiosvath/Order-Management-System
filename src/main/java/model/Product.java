package model;

/**
 * @author Osvath Tamas
 * @since 2.04.2020
 *
 * Public class for implementing a product object
 */
public class Product {
    private int id, stock;
    private double price;
    private String name;

    /**
     * Constructor for creating a product
     * @param name name of the product
     * @param stock stock of the product
     * @param price price of the product
     */
    public Product(String name, int stock, double price){
        id = 0;
        this.name = name;
        this.stock = stock;
        this.price = price;
    }

    /**
     * Constructor without parameters, creates only a "void" object
     * The id is set to -1 in order to be able to check further if a new id was assigned (id must be positive in a database)
     */
    public Product(){
        id = -1;
        name = "";
        stock = 0;
        price = 1.0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Method for converting the object to a String
     * @return the corresponding String for the object
     */
    public String toString(){
        return (name + " " + stock + " " + price);
    }
}
