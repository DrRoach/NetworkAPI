# Java Network API
Always having to recreate your Java networking code? Well now you don't have to anymore thanks to this Java Network API. This library makes is easy and simple to implement networking, even if you have no knowledge or prior experience of working with networking in Java, you'll be able to implement a server-client architecture with a few simple lines of code.

Example uses for this range from messaging apps to games to many other applications that require communication between devices.

## This project has moved
This project has moved to [Gitlab](https://gitlab.com/DrRoach/NetworkAPI).

### Example

Server file:

```Java
public class Host extends Server {
    Host(int port) {
        super(port);
    }
        
    public static void main(String[] args) {
        Host host = new Host(2103); 
    }
}
```
Client file:

```Java
public class DevClient extends Client {
    public static void main(String[] args) {
        DevClient client = new DevClient("127.0.0.1", 2103); 
    }
}
```

It's as simple as that. With these two simple files, you can now run your server and connect with any devices that run your client file. It's simple to send messages between the connected devices too.

### Message Example

Server file:

```Java
public class Host extends Server {
    private Host _host;
        
    Host(int port) {
        super(port); 
    }
        
    public static void main(String[] args) {
        _host = new Host(2103); // Starts server on port 2103 with `useEncryption = true`
    }
        
    @Override
    public void newConnection(Connection connection) {
        // Send a message to our new client connection
        connection.send("Hello new client.");
            
        // Inform all new clients of the new connection
        _host.broadcast("There is a new connection."); 
    }
}
```
Client file:

```Java
public class DevClient extends Client {
    public static void main(String[] args) {
        DevClient client = new DevClient("127.0.0.1", 2103); // Connects to local server on port 2103 with `useEncryption = true`
    }
        
    @Override
    public void messageReceived(String message) {
        System.out.println("New message from server: " + message); 
    }
}
```

With these two simple files we can now connect our clients to our server and message both our new clients individually and broadcast to all connected clients when we get a new connection. We also print any new messages that we get from the server in the clients' output stream.

## Features

- Send messages from server to client
- Send messages from client to server
- Validate server using signature

- E2E encryption on both server and client
## Available Methods

There are a number of different methods available to call in both the server and client classes, the main focus of these methods are the `send()` methods which are used to communicate between the server and client.

### Server Methods

`broadcast(String message)` - Broadcast the given `message` to all of the connected clients.

`online()` - Returns boolean indicating whether or not a server is online.

`stop()` - Closes all connections and stops the server.

### Server Overrides

`newConnection(Connection connection)` - Whenever a new connection is made to the server this method is called.

`messageReceived(Connection connection, String message)` - Called when a new message is received from a connection.

### Client Methods

`getMessage()` - Returns message received as a `String`. It is advised to call this message from `messageReceived()` when using encryption to avoid the servers public key being mistaken for a message from the Server.

### Client Overrides

`messageReceived(String message)` - Whenever the client receives a new message this method is called.

`connectedToServer()` - When the connection to server is made, this method is called. __MUST__ call super method if using encryption.

### Connection Methods (Available for both Server and Client)

These methods are available in both the server and client. To call these methods from the server you must have an instance of the `connection`, this can be attained in the `newConnection(Connection connection)` callback as an example. Or you can call these methods directly from the `Client` class.

`send(message)` - Send the given message from calling instance to the connected intance.

`getAddress()` - Get the address of the connection. If called from the server this will return the address of the client connection. If called from the client this will return the address of the server.

`connectionClosed(int id)` - Whenever a connection is closed this method is called.

## Future Updates

There are a number of different updates in the works including but not limited to:

- Client-client messaging
- Improved code testing
- Improved code documentation
