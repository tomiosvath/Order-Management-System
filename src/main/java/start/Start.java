package start;

import presentation.controller.WarehouseController;

/**
 * @author Osvath Tamas
 * @since 2.04.2020
 *
 * Public Class for executing the application
 */
public class Start {
    public static void main(String[] args){
        String inFile = args[0];
        WarehouseController w = new WarehouseController(inFile);
    }
}
