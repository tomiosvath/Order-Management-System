package bll;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import dao.ClientDAO;
import model.Client;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author Osvath Tamas
 * @since 2.04.2020
 *
 * Class for implementing the Business Logic Layer for the Client Class
 */
public class ClientBLL {
    private ClientDAO clientDAO;

    /**
     * Constructor
     * Creates a new instance of the ClientDAO object
     */
    public ClientBLL() {
        clientDAO = new ClientDAO(ClientDAO.class);
    }

    /*public Client findClientById(int clientId){
        ClientDAO clientDAO = new ClientDAO(ClientDAO.class);
        return clientDAO.findById(clientId);
    }*/

    /**
     * Method for finding a Client by its name. Calls the findByName() method in the clientDAO class
     * @param clientName name of the client
     * @return the Client object
     */
    public Client findClientByName(String clientName){
        return clientDAO.findByName(clientName);
    }

    /**
     * Method for inserting a Client in the DB. Calls the insert() method from the DAO
     * @param client the Client to be inserted in the DB
     */
    public void insertClient(Client client){
        clientDAO.insert(client);
    }

    /*public void deleteClientByName(String clientName){
        Client client = findClientByName(clientName);
        clientDAO.deleteById(client.getId());
    }*/

    /**
     * Method for deleting a Client from the DB. Calls the find() method for finding the client, and the
     * deleteById() method from the DAO for deleting it
     * @param name name of the client
     * @param address address of the client
     */
    public void deleteClient(String name, String address){
        Client client = clientDAO.find(name, address);
        clientDAO.deleteById(client.getId());
    }

    /**
     * Method for creating a PDF report on the clients in the database
     * @param nrReport the number of the report made
     */
    public void reportClients(int nrReport){
        List<Client> clients = clientDAO.selectAll();

        StringBuilder string = new StringBuilder();
        string.append("ClientReport");
        string.append(nrReport);
        string.append(".pdf");

        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(string.toString()));
            document.open();
            PdfPTable table = new PdfPTable(3);
            addTableHeader(table);
            for (Client c: clients)
                addRows(table, c);

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
        Stream.of("ID", "Name", "Address")
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
     * @param c the Client
     */
    private void addRows(PdfPTable table, Client c) {
        table.addCell(Integer.toString(c.getId()));
        table.addCell(c.getName());
        table.addCell(c.getAddress());
    }

}
