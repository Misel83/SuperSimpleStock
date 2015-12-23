package all.enums;

/**
 * Texts of err messages used in implementation
 */
public enum ErrorMessages {

    QUANTITY_VALUE_LESS_OR_EQUAL_ZERO(0, "Quantity parameter has illegal value, should be greater then 0."),
    PRICE_VALUE_LESS_OR_EQUAL_ZERO(1, "Price parameter has illegal value, should be greater then 0."),
    OBJECT_IS_NULL(2, "Following object is null: "),
    NO_SUCH_STOCK_EXISTS(3, "No such stock exists: "),
    NAME_OF_STOCK_IS_NULL(4, "Name of stock is null");

    private final int code;

    private final String description;

    ErrorMessages(int code, String description) {
        this.code = code;
        this.description = description;
    }

    @Override
    public String toString() {
        return code + ": " + description;
    }

}
