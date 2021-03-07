import java.net.ServerSocket

// Creates a socket for the chat server.
class ChatServer {
    fun serve() {
        val socket = ServerSocket(0) // Creates a random socket
        println("Your port is: " + socket.localPort) // Prints out the port number used for connecting the chat
        while (true) { // Starts a loop, waiting for connections
            println("Waiting for connection")
            val listen = socket.accept() // When user connects, continues.
            println("Connected to " + listen.inetAddress.hostAddress + " " + listen.port)
            val t = Thread(CommandInterpreter(listen.getInputStream(), listen.getOutputStream())) // Creates a thread.
            t.start() // Start the thread.
        }
    }
}