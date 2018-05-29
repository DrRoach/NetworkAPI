package NetworkAPI.Exceptions;

public class ClientConnectionFailedException extends RuntimeException {
    public ClientConnectionFailedException(String message) {
        super(message);
    }
}
