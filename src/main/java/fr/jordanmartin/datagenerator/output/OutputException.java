package fr.jordanmartin.datagenerator.output;

/**
 * Exception de génération
 */
public class OutputException extends RuntimeException {

    public OutputException(String message) {
        super(message);
    }

    public OutputException(String message, Throwable cause) {
        super(message, cause);
    }

    public OutputException(Throwable cause) {
        super(cause);
    }

}
