package model;

/**
 * @author Osvath Tamas
 * @since 2.04.2020
 *
 * Public class for implementing a client object
 */
public class Client {
    private int id;
    private String name, address;

    /**
     * Constructor for creating a given client
     * The id will be automatically generated when introduced in the database
     * @param name the client's name
     * @param address the client's address
     */
    public Client(String name, String address){
        id = 0;
        this.name = name;
        this.address = address;
    }

    /**
     * Constructor without parameters, creates only a "void" object
     * The id is set to -1 in order to be able to check further if a new id was assigned (id must be positive in a database)
     */
    public Client(){
        id = -1;
        name = "";
        address = "";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Method for converting the object to a String
     * @return the corresponding String for the object
     */
    public String toString(){
        return (getId() + " " + getName() + " " + getAddress());
    }
}
