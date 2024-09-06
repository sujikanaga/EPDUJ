import java.net.*;
import java.util.*;

public class UDPServer {
    private static final int PORT = 9876;
    private static final Map<SocketAddress, String> clientNames = new HashMap<>();

    public static void main(String[] args) {
        try (DatagramSocket serverSocket = new DatagramSocket(PORT)) {
            byte[] receiveData = new byte[1024];

            System.out.println("Server started. Waiting for clients to connect...");

            while (true) {
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                serverSocket.receive(receivePacket);

                String message = new String(receivePacket.getData(), 0, receivePacket.getLength());
                SocketAddress clientAddress = receivePacket.getSocketAddress();

                if (message.startsWith("NAME:")) {
                    String clientName = message.substring(5);
                    clientNames.put(clientAddress, clientName);
                    System.out.println(clientName + " has joined the chat.");
                    broadcast(clientName + " has joined the chat.", clientAddress);
                } else {
                    String clientName = clientNames.get(clientAddress);
                    if (clientName != null) {
                        System.out.println(clientName + ": " + message);
                        broadcast(clientName + ": " + message, clientAddress);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void broadcast(String message, SocketAddress sender) {
        try (DatagramSocket socket = new DatagramSocket()) {
            byte[] sendData = message.getBytes();
            for (SocketAddress client : clientNames.keySet()) {
                if (!client.equals(sender)) {
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, client);
                    socket.send(sendPacket);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

