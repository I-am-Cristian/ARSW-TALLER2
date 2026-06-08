import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Date;

public class DatagramTimeServer {
    private DatagramSocket socket;
    private boolean running;

    public DatagramTimeServer(int port) {
        try {
            socket = new DatagramSocket(port);
            running = true;
            System.out.println("Servidor de hora iniciado en puerto " + port);
        } catch (SocketException e) {
            System.err.println("Error al crear el socket: " + e.getMessage());
        }
    }

    public void start() {
        byte[] receiveBuffer = new byte[256];
        
        while (running) {
            try {
                // Recibir solicitud del cliente (puede estar vacía)
                DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
                socket.receive(receivePacket);
                
                // Obtener información del cliente
                InetAddress clientAddress = receivePacket.getAddress();
                int clientPort = receivePacket.getPort();
                
                // Obtener hora actual
                String currentTime = new Date().toString();
                byte[] sendData = currentTime.getBytes();
                
                // Enviar respuesta
                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientAddress, clientPort);
                socket.send(sendPacket);
                
                System.out.println("Hora enviada a " + clientAddress + ":" + clientPort + " - " + currentTime);
                
            } catch (IOException e) {
                if (running) {
                    System.err.println("Error en el servidor: " + e.getMessage());
                }
            }
        }
        socket.close();
    }

    public void stop() {
        running = false;
        if (socket != null && !socket.isClosed()) {
            socket.close();
        }
    }

    public static void main(String[] args) {
        int port = 4445; // Puerto por defecto
        if (args.length > 0) {
            try {
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.err.println("Puerto inválido, usando puerto 4445");
            }
        }
        
        DatagramTimeServer server = new DatagramTimeServer(port);
        
        // Agregar shutdown hook para cerrar correctamente
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("\nApagando servidor...");
            server.stop();
        }));
        
        server.start();
    }
}