package com.gitlab.networkapi;

public class DevClient extends Client {
    // Simplest constructor implementation which would be used by most people
    //public DevClient(String host, int port) {
    //    super(host, port);
    //}

    public DevClient(String host, int port, int timeout) {
        super(host, port, timeout);
    }

    public static void main(String[] args) {
        DevClient client = new DevClient("localhost", 2103, 1000);
    }

    @Override
    public void messageReceived(String message) {
        super.messageReceived(message);

        System.out.println(message);
    }
}
