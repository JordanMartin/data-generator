package fr.jordanmartin.datagenerator.provider.core;

public class ValueProviderException extends RuntimeException {

    private transient ValueProvider<?> provider;

    public ValueProviderException() {
    }

    public ValueProviderException(ValueProvider<?> provider, String message, Throwable cause) {
        super(message, cause);
        this.provider = provider;
    }

    public ValueProviderException(ValueProvider<?> provider, String message) {
        this(provider, message, null);
    }

    public ValueProviderException(ValueProvider<?> provider, Throwable cause) {
        this(provider, null, cause);
    }

    @Override
    public String getMessage() {
        if (provider != null) {
            return "[Générateur : " + provider.getClass().getSimpleName() + "] " + super.getMessage();
        }
        return super.getMessage();
    }
}
