import java.io.*;
import java.net.*;
import java.util.*;

public class TCPServer {
    private static Set<Socket> clients = new HashSet<>();
    private static Map<Socket, String> clientNames = new HashMap<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(5555)) {
            System.out.println("Server started. Waiting for clients to connect...");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Connection from " + clientSocket.getInetAddress());

                DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
                DataInputStream in = new DataInputStream(clientSocket.getInputStream());

                out.writeUTF("NAME");
                String clientName = in.readUTF();

                synchronized (clients) {
                    clients.add(clientSocket);
                    clientNames.put(clientSocket, clientName);
                }

                System.out.println(clientName + " has joined the chat.");
                broadcast(clientName + " has joined the chat.", clientSocket);

                new Thread(() -> handleClient(clientSocket, clientName)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket, String clientName) {
        try (DataInputStream in = new DataInputStream(clientSocket.getInputStream())) {
            while (true) {
                String msg = in.readUTF();
                System.out.println(clientName + ": " + msg);
                broadcast(clientName + ": " + msg, clientSocket);
            }
        } catch (IOException e) {
            synchronized (clients) {
                clients.remove(clientSocket);
                clientNames.remove(clientSocket);
                System.out.println(clientName + " has left the chat.");
                broadcast(clientName + " has left the chat.", clientSocket);
            }
        }
    }

    private static void broadcast(String message, Socket sender) {
        synchronized (clients) {
            for (Socket client : clients) {
                if (client != sender) {
                    try {
                        DataOutputStream out = new DataOutputStream(client.getOutputStream());
                        out.writeUTF(message);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}

