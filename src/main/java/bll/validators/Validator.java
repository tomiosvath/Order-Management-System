package bll.validators;
/**
 * @author Osvath Tamas
 * @since 2.04.2020
 *
 * Public interface for validating input data
 */
public interface Validator<T> {
    /**
     * Method for validating an object
     * @param t the object to be validated
     */
    public void validate(T t);
}
