package NetworkAPI.Communicators;

import java.io.DataOutputStream;
import java.io.IOException;

public class Send implements Runnable {
    private DataOutputStream _out;
    private volatile String _message = "";

    public Send(DataOutputStream out) {
        _out = out;
    }

    public void run() {
        try {
            _out.writeUTF(_message);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void setMessage(String message) {
        _message = message;
    }
}
