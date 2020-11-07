package bll;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import dao.ProductDAO;
import model.Product;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author Osvath Tamas
 * @since 2.04.2020
 *
 * Class for implementing the Business Logic Layer for the Product Class
 */
public class ProductBLL {
    private ProductDAO productDAO;

    /**
     * Constructor
     * Creates a new instance of the ProductDAO object
     */
    public ProductBLL(){
        productDAO = new ProductDAO(ProductDAO.class);
    }

    /**
     * Method for finding a Product by its name. Calls the findByName() method in the productDAO class
     * @param productName name of the client
     * @return the Product object
     */
    public Product findProductByName(String productName){
        return productDAO.findByName(productName);
    }

    /**
     * Method for deleting a Product from the DB based on its name. Calls the findProductByName() method from the current class
     * and after the deleteById() method from the DAO
     * @param productName the name of the product
     */
    public void deleteProductByName(String productName){
        Product product = findProductByName(productName);
        productDAO.deleteById(product.getId());
    }

    /**
     * Method for inserting a Product into the DB. If the product already exists, its stock is updated
     * @param product the Product to be added
     */
    public void insertProduct(Product product){
        Product existingProduct = productDAO.findByName(product.getName());
        if (existingProduct.getId() == -1){
            productDAO.insert(product);
        }
        else{
            existingProduct.setStock(existingProduct.getStock() + product.getStock());
            productDAO.update(existingProduct, existingProduct.getId());
        }
    }

    /**
     * Method for creating a PDF report on the products in the database
     * @param nrReport the number of the report made
     */
    public void reportProducts(int nrReport){
        List<Product> products = productDAO.selectAll();

        StringBuilder string = new StringBuilder();
        string.append("ProductReport");
        string.append(nrReport);
        string.append(".pdf");

        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(string.toString()));
            document.open();
            PdfPTable table = new PdfPTable(4);
            addTableHeader(table);
            for (Product p: products)
                addRows(table, p);

            document.add(table);
            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Method for adding the column headers for the table in the PDF
     * @param table the table in which the data is added
     */
    private void addTableHeader(PdfPTable table) {
        Stream.of("ID", "Name", "Stock", "Price")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(2);
                    header.setPhrase(new Phrase(columnTitle));
                    table.addCell(header);
                });
    }

    /**
     * Method for adding rows to the table
     * @param table the working table
     * @param p the Product
     */
    private void addRows(PdfPTable table, Product p) {
        table.addCell(Integer.toString(p.getId()));
        table.addCell(p.getName());
        table.addCell(Integer.toString(p.getStock()));
        table.addCell(Double.toString(p.getPrice()));
    }

}
