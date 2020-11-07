package bll;

import bll.validators.OrderQuantityValidator;
import bll.validators.Validator;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import dao.OrderDAO;
import dao.ProductDAO;
import model.Client;
import model.Orders;
import model.Product;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author Osvath Tamas
 * @since 2.04.2020
 *
 * Class for implementing the Business Logic Layer for the Orders Class
 */
public class OrderBLL {
    private Validator<Orders> orderValidator;
    private OrderDAO orderDAO;
    private int nrBill, nrErrorBill;

    /**
     * Constructor
     * Creates a new instance of the OrderDAO object
     */
    public OrderBLL(){
        nrBill = 1;
        nrErrorBill = 1;
        orderValidator = new OrderQuantityValidator();
        orderDAO = new OrderDAO(OrderDAO.class);
    }

    /**
     * Method for inserting an Order to the DB. The method checks if the client and the product exists, and also the
     * ordered quantity is smaller or equal to the stock. If the requirements are fulfilled, the order is created and a Bill
     * is generated, otherwise an Error Bill is generated
     * @param clientName name of the client
     * @param productName name of the product
     * @param quantity quantity of the bought product
     */
    public void insertOrder(String clientName, String productName, int quantity){
        ClientBLL clientBLL = new ClientBLL();
        Client client = clientBLL.findClientByName(clientName);

        ProductBLL productBLL = new ProductBLL();
        Product product = productBLL.findProductByName(productName);

        if (product.getId() == -1 || client.getId() == -1 || product.getStock() < quantity){
            createErrorBill(product.getId(), client.getId());
        }
        else{
            Orders order = new Orders(client.getId(), product.getId(), quantity);
            orderValidator.validate(order);
            orderDAO.insert(order);

            //update the product
            ProductDAO productDAO = new ProductDAO(ProductDAO.class);
            product.setStock(product.getStock() - quantity);
            productDAO.update(product, product.getId());

            //generate the bill
            createBill(order, client, product);
        }
    }

    /**
     * Method for creating a PDF report on the orders in the database
     * @param nrReport the number of the report made
     */
    public void reportOrders(int nrReport){
        List<Orders> orders = orderDAO.selectAll();

        StringBuilder string = new StringBuilder();
        string.append("OrdersReport");
        string.append(nrReport);
        string.append(".pdf");

        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(string.toString()));
            document.open();
            PdfPTable table = new PdfPTable(4);
            addTableHeader(table);
            for (Orders o: orders)
                addRows(table, o);

            document.add(table);
            document.close();
        } catch (DocumentException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method for adding the column headers for the Report table in the PDF
     * @param table the table in which the data is added
     */
    private void addTableHeader(PdfPTable table) {
        Stream.of("ID", "ClientID", "ProductID", "Quantity")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(2);
                    header.setPhrase(new Phrase(columnTitle));
                    table.addCell(header);
                });
    }

    /**
     * Method for adding rows to the Report table
     * @param table the working table
     * @param o the Orders object
     */
    private void addRows(PdfPTable table, Orders o) {
        table.addCell(Integer.toString(o.getId()));
        table.addCell(Integer.toString(o.getClientId()));
        table.addCell(Integer.toString(o.getProductId()));
        table.addCell(Integer.toString(o.getQuantity()));
    }

    /**
     * Method for creating an ErrorBill
     * @param productId the id of the product to be ordered
     * @param clientId the id of the client who orders
     */
    private void createErrorBill(int productId, int clientId){
        StringBuilder fileName = new StringBuilder();
        fileName.append("ErrorBill");
        fileName.append(nrErrorBill);
        fileName.append(".pdf");
        nrErrorBill++;

        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(fileName.toString()));
            document.open();
            Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
            Chunk chunk;
            if (productId == -1)
                chunk = new Chunk("Error on creating order: product is non-existent", font);
            else if (clientId == -1)
                chunk = new Chunk("Error on creating order: client is non-existent", font);
            else
                chunk = new Chunk("Error on creating order: quantity is bigger than stock", font);
            document.add(chunk);
            document.close();

        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * Method for creating a Bill
     * @param order the order
     * @param client the client
     * @param product the product
     */
    private void createBill(Orders order, Client client, Product product){
        StringBuilder string = new StringBuilder();
        string.append("Bill" + nrBill + ".pdf");
        nrBill++;


        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(string.toString()));
            document.open();
            PdfPTable table = new PdfPTable(6);
            addBillTableHeader(table);
            addBillRows(table, order, client, product);
            document.add(table);
            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method for adding the column headers for the Bill table in the PDF
     * @param table the table in which the data is added
     */
    private void addBillTableHeader(PdfPTable table){
        Stream.of("No", "ClientName", "ProductName", "Quantity", "PricePerUnit", "TotalPrice")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(2);
                    header.setPhrase(new Phrase(columnTitle));
                    table.addCell(header);
                });
    }

    /**
     * Method for adding rows to the Bill table
     * @param table the working table
     * @param c the Client
     */
    private void addBillRows(PdfPTable table, Orders o, Client c, Product p) {
        table.addCell("1");
        table.addCell(c.getName());
        table.addCell(p.getName());
        table.addCell(Integer.toString(o.getQuantity()));
        table.addCell(Double.toString(p.getPrice()));
        table.addCell(Double.toString(o.getQuantity() * p.getPrice()));
    }
}
