package presentation.controller;

import bll.ClientBLL;
import bll.OrderBLL;
import bll.ProductBLL;
import model.Client;
import model.Orders;
import model.Product;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * @author Osvath Tamas
 * @since 2.04.2020
 *
 * Class for controlling the warehouse, which includes initializing the BLLs, parsing the input file and execute
 * the corresponding operations
 */
public class WarehouseController {
    private ClientBLL clientBLL;
    private ProductBLL productBLL;
    private OrderBLL orderBLL;
    private File file;
    private int nrClientReport, nrOrderReport, nrProductReport;
    /**
     * Public constructor
     * @param fileName the name of the input file
     */
    public WarehouseController(String fileName){
        nrClientReport = 1;
        nrOrderReport = 1;
        nrProductReport = 1;
        clientBLL = new ClientBLL();
        productBLL = new ProductBLL();
        orderBLL = new OrderBLL();
        file = new File(fileName);

        String s = null;
        try {
            s = new Scanner(file).useDelimiter("\\Z").next();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        readCommands(s);
    }

    /**
     * Method for parsing the input file, and calling the corresponding functions
     */
    private void readCommands(String s){
        String arr[] = s.split("\\r?\\n");
        for (int i = 0; i < arr.length; i++) {
            int posSpace = arr[i].indexOf(" ");
            String op = arr[i].substring(0, posSpace);
            if (op.equals("Order:") || op.equals("order:"))
                createOrder(arr[i].substring(posSpace + 1));
            else if (op.equals("Insert") || op.equals("insert")){
                int posDots = arr[i].indexOf(":");
                String newOp = arr[i].substring(posSpace + 1, posDots);
                if (newOp.equals("product") || newOp.equals("Product"))
                    insertProduct(arr[i].substring(posDots + 2));
                else if (newOp.equals("client") || newOp.equals("Client"))
                    insertClient(arr[i].substring(posDots + 2));
            }
            else if (op.equals("Delete") || op.equals("delete")){
                int posDots = arr[i].indexOf(":");
                String newOp = arr[i].substring(posSpace + 1, posDots);
                if (newOp.equals("product") || newOp.equals("Product"))
                    deleteProduct(arr[i].substring(posDots + 2));
                else if (newOp.equals("client") || newOp.equals("Client"))
                    deleteClient(arr[i].substring(posDots + 2));
            }
            else if (op.equals("Report") || op.equals("report")){
                String newOp = arr[i].substring(posSpace + 1);
                createReport(newOp);
            }
        }
    }

    /**
     * Method for creating reports
     * @param command the command for which the report is created (could be client, product or order)
     */
    private void createReport(String command){
        if (command.equals("client") || command.equals("Client")){
            clientBLL.reportClients(nrClientReport);
            nrClientReport++;
        }
        else if (command.equals("product") || command.equals("Product")){
            productBLL.reportProducts(nrProductReport);
            nrProductReport++;
        }
        else if (command.equals("order") || command.equals("Order")){
            orderBLL.reportOrders(nrOrderReport);
            nrOrderReport++;
        }
    }

    /**
     * Method for creating a new order. Calls the orderBLL.insertOrder() method for inserting the order into the DB
     * @param command the String which contains the order information
     */
    private void createOrder(String command){
        Orders order = new Orders();

        int pos = command.indexOf(",");
        String clientName = command.substring(0, pos);

        command = command.substring(pos + 2);
        pos = command.indexOf(",");
        String productName = command.substring(0, pos);

        String quantity = command.substring(pos + 2);
        int intQuantity = Integer.parseInt(quantity);
        orderBLL.insertOrder(clientName, productName, intQuantity);
    }

    /**
     *  Method for creating a new Product. Calls the productBLL.insertProduct() method for inserting the product into the DB
     * @param command the String which contains the product information
     */
    private void insertProduct(String command){
        int pos = command.indexOf(",");
        String name = command.substring(0, pos);

        command = command.substring(pos + 2);
        pos = command.indexOf(",");
        int stock = Integer.parseInt(command.substring(0, pos));

        double price = Double.parseDouble(command.substring(pos + 2));

        Product product = new Product(name, stock, price);
        productBLL.insertProduct(product);
    }

    /**
     *  Method for deleting a Product. Calls the productBLL.deleteProductByName() method for deleting the product from the DB
     * @param command the String which contains the product information
     */
    private void deleteProduct(String command){
        productBLL.deleteProductByName(command);
    }

    /**
     *  Method for creating a new Client. Calls the productBLL.insertClient() method for inserting the client into the DB
     * @param command the String which contains the client information
     */
    private void insertClient(String command){
        //System.out.println(command);
        int pos = command.indexOf(",");
        String name = command.substring(0, pos);
        String address = command.substring(pos + 2);

        Client c = new Client(name, address);
        clientBLL.insertClient(c);
    }

    /**
     *  Method for deleting a Client. Calls the productBLL.deleteClient() method for deleting the product from the DB
     * @param command the String which contains the client information
     */
    private void deleteClient(String command){
        int pos = command.indexOf(",");
        String name = command.substring(0, pos);
        String address = command.substring(pos + 2);
        clientBLL.deleteClient(name, address);
    }

    /*public static void main(String[] args){
        WarehouseController w = new WarehouseController("in3.txt");
    }*/

}
