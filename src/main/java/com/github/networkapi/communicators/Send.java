package main.java.com.github.networkapi.communicators;

import java.io.DataOutputStream;
import java.io.IOException;

public class Send implements Runnable {
    private DataOutputStream out;
    private volatile byte[] message;

    public Send(DataOutputStream out) {
        this.out = out;
    }

    public void run() {
    }

    public void send() {
        try {
            String messageToSend = new String(message);
            out.writeUTF(messageToSend);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void setMessage(String message) {
        this.message = message.getBytes();
    }

    public void setMessage(byte[] message) {
        this.message = message;
    }
}
