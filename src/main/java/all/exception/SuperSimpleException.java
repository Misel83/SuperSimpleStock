package all.exception;

/**
 * Exception used for situations where unsupported stock is requested.
 */
public class SuperSimpleException extends RuntimeException {

    private String nameOfStock;

    public SuperSimpleException(String message, String nameOfStock) {
        super(message);
        this.nameOfStock = nameOfStock;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public String getMessage() {
        return super.getMessage() + nameOfStock;
    }

}












