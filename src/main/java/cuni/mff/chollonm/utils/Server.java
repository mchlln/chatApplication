package cuni.mff.chollonm.utils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cuni.mff.chollonm.utils.Colors.RESET;


/**
 * The Server class represents a server application managing client connections.
 * It stores information about active clients, maps users to their respective client handlers,
 * and maintains groups of clients.
 */
public class Server {

    /**
     * List to store active client handlers.
     */
    static List<ClientHandler> activeClients = new ArrayList<>();

    /**
     * Map to link a user to its corresponding client handler.
     */
    static Map<String,ClientHandler> reachClients = new HashMap<>();

    /**
     * Map to link a group name to its list of members.
     */
    static Map<String,List<ClientHandler>> groups =new HashMap<>();

    /**
     * Counter to keep track of connected clients.
     */
    static int clientCount = 0;

    /**
     * The port number on which the server listens for incoming connections.
     */
    static int port = 1234;

    /**
     * The main method starts the server and listens for client connections.
     * Creates a ClientHandler for each client to handle them.
     *
     * @param args command-line arguments (not used)
     * @throws IOException if an I/O error occurs when creating the server socket
     */
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server is listening on port " + port);
            // Running infinite loop for getting client requests
            while (true) {
                // Accept the incoming request
                Socket clientSocket = serverSocket.accept();

                ClientHandler ClientHandler = new ClientHandler(clientSocket);

                Thread thread = new Thread(ClientHandler);

                activeClients.add(ClientHandler);

                thread.start();

                clientCount++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (serverSocket != null) {
                    serverSocket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

/**
 * Represents a client handler for the server.
 */
class ClientHandler implements Runnable {

    /**
     * The socket associated with the client.
     */
    private Socket socket;

    /**
     * Input stream to receive data from the client.
     */
    private DataInputStream input;

    /**
     * Output stream to send data to the client.
     */
    private DataOutputStream output;

    /**
     * The name of the client.
     */
    private String name;

    /**
     * The color used for the client's messages in the chat.
     * Default color is RESET (no color).
     */
    private Colors color = RESET;

    /**
     * Constructs a new client handler with the specified socket.
     *
     * @param socket the socket associated with the client
     * @throws IOException if an I/O error occurs when creating the input or output streams
     */
    public ClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        this.input = new DataInputStream(socket.getInputStream());
        this.output = new DataOutputStream(socket.getOutputStream());
    }

    /**
     * Handles communication with a client.
     * Reads the client's name, welcomes the client to the chat, and broadcasts the client's join message.
     * Continuously listens for messages from the client and processes them until the client disconnects.
     */
    @Override
    public void run() {
        try {

            name = input.readUTF();
            System.out.println("Accepted connection from " + name);
            Server.reachClients.put(name,this);

            output.writeUTF("Thank you for joining the chat, " + name + ".");

            broadcast(name + " joined the chat.", this);

            while (true) {
                String received = input.readUTF();
                if (received.equals("exit")) {
                    System.out.println(name + " just left the chat");
                    broadcast(name + " left the chat.", this);
                    // Close resources and break out of the loop
                    try {
                        Server.activeClients.remove(this);
                        Server.clientCount--;
                        socket.close();
                        input.close();
                        output.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                if(Server.clientCount==1){
                    output.writeUTF("You are alone in the chat.");
                }
                sendMessage(received,this);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Processes and sends messages received from clients.
     * If the message starts with "-", it interprets it as a command, otherwise broadcasts it to all clients.
     *
     * @param message the message received from the client
     * @param sender  the client handler initiating the message
     * @throws IOException if an I/O error occurs while processing or sending the message
     */
    void sendMessage(String message,ClientHandler sender)throws IOException{
        if(message.startsWith("-")){
            switch (message.substring(1,3)){
                case "hp" -> sendHelp(sender);
                case "dm" -> privateChat(message, sender);
                case "cu" -> changeUsername(message, sender);
                case "mg" -> createGroup(message, sender);
                case "sg" -> sendGroup(message, sender);
                case "sc" -> setColor(message,sender);
                case "la" -> listArt(sender);
                case "pa" -> printArt(message, sender);
                default -> writeMessage("Wrong option, type -hp if you need help.",sender);
            }

        }else{
            broadcast(name + ": " + message, sender);
        }
    }

    /** Writes a formatted message to the specified client.
     *
     * @param message  The message to be sent.
     * @param receiver The client handler to which the message will be sent.
     * @throws IOException If an I/O error occurs while sending the message.
     */
    void writeMessage(String message, ClientHandler receiver) throws IOException {
        receiver.output.writeUTF(formatColor(message));
    }

    /**
     * Sends a help message to the specified client to explain the different commands available in the chat.
     *
     * @param sender the client handler to whom the help message is sent
     * @throws IOException if an I/O error occurs while sending the help message
     */
    void sendHelp(ClientHandler sender)throws IOException{
        String helpMessage = """
                Available commands:
                -hp: Display this help message.
                -dm [username] [message]: Send a private message to the specified user.
                -cu [new_username]: Change your username.
                -mg [group_name] [members ...]: Create a private group chat.
                -sg [group_name] [message]: Send a message to the specified group of users.
                -la: Display the names of the different ascii art available.
                -pa [name_of_ascii_art]: Send a reaction to all users via some predefined ascii art.
                -sc [color]: Change the color of the user in the chat.
                exit: Ens the chatting session.
                Type any message to send it to all users in the chat.
                """;
        writeMessage(helpMessage,sender);
    }

    /**
     * Broadcasts a message to all clients except the sender.
     *
     * @param message the message to be broadcasted
     * @param sender  the client handler who sent the message (excluded from the broadcast)
     * @throws IOException if an I/O error occurs while broadcasting the message
     */
    void broadcast(String message, ClientHandler sender) throws IOException {
        for (ClientHandler client : Server.activeClients) {
            if (client != sender) {
                writeMessage(message,client);
            }
        }
    }

    /**
     * Changes the username of the client handler and updates the reachClients map accordingly.
     * Broadcasts a message to all clients to notify them about the username change.
     *
     * @param message the message containing the new username
     * @param sender  the client handler initiating the username change
     * @throws IOException if an I/O error occurs while broadcasting the username change message
     */
    void changeUsername( String message, ClientHandler sender)throws IOException{
        String[] mess = message.split(" ");
        if(mess.length != 2){
            writeMessage("Invalid command. Usage: -cu [username]",sender);
            return;
        }
        String username = mess[1];
        Server.reachClients.remove(this.name);
        Server.reachClients.put(username,sender);
        broadcast(name +" changed its username to "+username,sender);
        name = username;
    }

    /**
     * Sends a private message to another client specified by the receiver's username.
     * If the receiver is not connected, sends a message to the sender indicating that the message was not sent.
     *
     * @param message the message containing the receiver's username and the private message
     * @param sender  the client handler sending the private message
     * @throws IOException if an I/O error occurs while sending the private message
     */
    void privateChat(String message, ClientHandler sender)throws IOException{
        String[] mess = message.split(" ");
        if(mess.length<3){
            writeMessage("Invalid command. Usage: -dm [receiver] [message]",sender);
            return;
        }
        String receiver = mess[1];
        if(Server.reachClients.containsKey(receiver)){
            writeMessage("[Private message from " + name + "] " +message.substring(4+mess[1].length()),Server.reachClients.get(receiver));
        }else{
            writeMessage("Message not sent, the receiver isn't connected.",sender);
        }
    }

    /**
     * Changes the color of the client's messages in the chat.
     * The change of color is visible in the received messages from the other users.
     * Accepts a color command in the format "-sc [color]" and updates the client's color attribute.
     * Notifies the client about the color change.
     *
     * @param message the message containing the color command
     * @param sender  the client handler changing color
     * @throws IOException if an I/O error occurs while processing or sending the color change notification
     */
    void setColor(String message, ClientHandler sender) throws IOException {
        String[] mess = message.split(" ");
        if (mess.length != 2) {
            writeMessage("Invalid color command. Usage: -sc [color]",sender);
            return;
        }
        String colorName = mess[1].toUpperCase();
        Colors newColor;

        try {
            newColor = Colors.valueOf(colorName);
        } catch (IllegalArgumentException e) {
            StringBuilder colorListMessage = new StringBuilder("Invalid color: " + colorName + ". Available colors are: ");
            for (Colors color : Colors.values()) {
                colorListMessage.append(color.getCode()).append(color.name()).append(" ");
            }
            writeMessage(colorListMessage.toString(),sender);
            return;
        }
        color=newColor;
        writeMessage("Color changed to " + newColor.name(),sender);
    }

    /**
     * Formats a message with the color specified by the {@link #color} attribute.
     *
     * @param message the message to format
     * @return the formatted message with color codes
     */
    String formatColor(String message){
        return color.getCode() + message + RESET.getCode();
    }

    /**
     * Creates a new group chat with the given name and adds the sender as a member.
     * If the group already exists, sends a message to the sender indicating that the group cannot be created.
     * If any member in the group list is not connected, sends a message to the sender indicating that the member is not connected.
     *
     * @param message the message containing the group name and member usernames
     * @param sender the client handler initiating the group creation
     * @throws IOException if an I/O error occurs while sending messages to clients
     */
    void createGroup(String message, ClientHandler sender)throws IOException{
        String[] mess = message.split(" ");
        if(mess.length<3){
            writeMessage("Invalid command. Usage: -mg [name_of_group] [members ...]",sender);
            return;
        }
        String groupName=mess[1];
        if(!Server.groups.containsKey(groupName)){
            List<ClientHandler> groupMembers = new ArrayList<>();
            groupMembers.add(sender);
            for(int i=2;i<mess.length;i++){
                if(Server.reachClients.containsKey(mess[i])){
                    ClientHandler member =Server.reachClients.get(mess[i]);
                    groupMembers.add(member);
                    writeMessage("You have been added to the group "+groupName +"by " + name,member);
                }else{
                    writeMessage("Member "+ mess[i]+ " is not connected, impossible to add him in the chat",sender);
                }
            }
            Server.groups.put(groupName,groupMembers);
            writeMessage("You have created the group "+ groupName,sender);
        }else{
            writeMessage("You cannot create the group "+groupName+", a group with the same name already exists.",sender);
        }
    }

    /**
     * Sends a message to all members of the specified group.
     * If the group does not exist, sends a message to the sender indicating that the group was not found.
     * If the sender is not a member of the group, sends a message to the sender indicating that they are not a member of the group. In that case, the message is not sent.
     *
     * @param message the message to send to the group, contains the group name
     * @param sender the client handler sending the message to the group
     * @throws IOException if an I/O error occurs while sending messages to clients
     */
    void sendGroup(String message,ClientHandler sender) throws IOException {
        String[] mess = message.split(" ");
        if(mess.length<3){
            writeMessage("Invalid command. Usage: -sg [name_of_group] [message]",sender);
            return;
        }
        String groupName = mess[1];
        List<ClientHandler> groupMembers = Server.groups.get(groupName);
        if (groupMembers != null) {
            if (groupMembers.contains(this)) {
                for (ClientHandler member : groupMembers) {
                    writeMessage(name + " [Group " + groupName + " from "+ name +"]: " + message.substring(groupName.length() + 5),member);
                }
            } else {
                writeMessage("You are not a member of Group " + groupName + ".",sender);
            }
        } else {
            writeMessage("Group " + groupName + " not found.", sender);
        }
    }

    /**
     * Lists all available ASCII art to the user.
     *
     * @param user The client handler to whom the list of ASCII art will be sent
     * @throws IOException If an I/O error occurs while sending the message
     */
    void listArt(ClientHandler user) throws IOException {
        StringBuilder artList = new StringBuilder("Available ascii art are: ");
        for (AsciiArt art : AsciiArt.values()) {
            artList.append(art.name()).append(" : ").append(art.getCode());
        }
        writeMessage(artList.toString(),user);

    }

    /**
     * Prints the requested ASCII art to the sender.
     *
     * @param message The message containing the command and the name of the ASCII art
     * @param sender  The client handler who requested the ASCII art
     * @throws IOException If an I/O error occurs while sending the message
     */
    void printArt(String message, ClientHandler sender) throws IOException {
        String[] parts = message.split(" ");
        if (parts.length != 2) {
            writeMessage("Invalid command. Usage: -pa [name_of_ascii_art]",sender);
            return;
        }
        String requestedArt = parts[1].toUpperCase();
        try {
            AsciiArt asciiArt = AsciiArt.valueOf(requestedArt);
            broadcast("["+name+"]\n"+asciiArt.getCode(),sender);
        } catch (IllegalArgumentException e) {
            writeMessage("ASCII art not found: " + requestedArt+". Write -la to list all ascii art available.", sender);
        }
    }

}
