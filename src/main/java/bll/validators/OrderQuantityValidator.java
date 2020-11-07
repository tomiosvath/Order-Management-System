package bll.validators;

import model.Orders;

/**
 * @author Osvath Tamas
 * @since 2.04.2020
 *
 * Public class for validating the ordered quantity of a product
 * Class implements the Validator interface
 */
public class OrderQuantityValidator implements Validator<Orders> {

    /**
     * Overriden method for validating quantity
     * Throws an exception if the quantity is smaller than 1
     * @param order the Orders object to be validated
     */
    @Override
    public void validate(Orders order) {
        if (order.getQuantity() < 1){
            throw new IllegalArgumentException("Invalid quantity");
        }
    }
}
