package NetworkAPI.Communicators;

import java.io.DataOutputStream;
import java.io.IOException;

public class Send implements Runnable {
    private DataOutputStream _out;
    private volatile byte[] _message;

    public Send(DataOutputStream out) {
        _out = out;
    }

    public void run() {
    }

    public void send() {
        try {
            _message = addNullChar();
            _out.write(_message);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void setMessage(String message) {
        _message = message.getBytes();
    }

    public void setMessage(byte[] message) {
        _message = message;
    }

    /**
     * Add null byte to end of the byte array
     *
     * Take the message that is to be sent and append a null byte onto the end of it
     *  in order to make sure that we know when we're at the end of our message
     *
     * @return - Original message byte array with NULL byte appended at the end
     */
    private byte[] addNullChar() {
        // Create our result array with +1 size to account for NULL byte
        byte[] result = new byte[_message.length + 1];

        // Create our null byte array to be copied onto the end
        byte[] nullByte = new byte[1];
        nullByte[0] = (byte) -1;

        // Merge the message and null byte arrays together
        System.arraycopy(_message, 0, result, 0, _message.length);
        System.arraycopy(nullByte, 0, result, _message.length, 1);

        // Return our newly created array
        return result;
    }
}
