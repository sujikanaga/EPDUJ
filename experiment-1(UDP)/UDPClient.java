import java.net.*;
import java.io.IOException;
import java.util.Scanner;

public class UDPClient {
    private static final int PORT = 9876;

    public static void main(String[] args) {
        try (DatagramSocket clientSocket = new DatagramSocket()) {
            InetAddress serverAddress = InetAddress.getByName("localhost");
            Scanner scanner = new Scanner(System.in);

            System.out.print("Enter your name: ");
            String name = scanner.nextLine();
            sendData(clientSocket, serverAddress, "NAME:" + name);

            new Thread(() -> {
                try {
                    byte[] receiveData = new byte[1024];
                    while (true) {
                        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                        clientSocket.receive(receivePacket);
                        String message = new String(receivePacket.getData(), 0, receivePacket.getLength());
                        System.out.println(message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();

            while (true) {
                String message = scanner.nextLine();
                sendData(clientSocket, serverAddress, message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void sendData(DatagramSocket socket, InetAddress address, String message) throws IOException {
        byte[] sendData = message.getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, address, PORT);
        socket.send(sendPacket);
    }
}

