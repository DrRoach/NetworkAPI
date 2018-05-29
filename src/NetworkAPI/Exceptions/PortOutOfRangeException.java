package NetworkAPI.Exceptions;

public class PortOutOfRangeException extends RuntimeException {
    public PortOutOfRangeException(String message) {
        super(message);
    }
}
