import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class DatagramTimeClient {
    private static final int TIMEOUT_MS = 2000; // 2 segundos de espera por respuesta
    private static final int UPDATE_INTERVAL_MS = 5000; // 5 segundos entre actualizaciones
    
    private String lastKnownTime = "Esperando primera conexión...";
    private boolean firstUpdate = true;
    
    public void start(String serverAddress, int serverPort) {
        DatagramSocket socket = null;
        
        try {
            // Crear socket del cliente
            socket = new DatagramSocket();
            socket.setSoTimeout(TIMEOUT_MS);
            
            InetAddress serverInetAddress = InetAddress.getByName(serverAddress);
            
            System.out.println("Cliente de hora iniciado");
            System.out.println("Conectando a servidor en " + serverAddress + ":" + serverPort);
            System.out.println("Actualizando hora cada " + UPDATE_INTERVAL_MS / 1000 + " segundos");
            System.out.println("-".repeat(50));
            
            while (true) {
                String currentTime = requestTime(socket, serverInetAddress, serverPort);
                
                if (currentTime != null) {
                    // Actualización exitosa
                    lastKnownTime = currentTime;
                    if (firstUpdate) {
                        System.out.println("Primera conexión exitosa!");
                        firstUpdate = false;
                    }
                    System.out.println("[" + getCurrentTimestamp() + "] Hora del servidor: " + lastKnownTime);
                } else {
                    // Fallo en la comunicación - mantener última hora conocida
                    System.out.println("[" + getCurrentTimestamp() + "] ⚠️  No se recibió respuesta del servidor");
                    System.out.println("    Manteniendo última hora conocida: " + lastKnownTime);
                }
                
                // Esperar antes de la siguiente actualización
                Thread.sleep(UPDATE_INTERVAL_MS);
            }
            
        } catch (SocketException e) {
            System.err.println("Error al crear el socket: " + e.getMessage());
        } catch (UnknownHostException e) {
            System.err.println("Servidor no encontrado: " + e.getMessage());
        } catch (InterruptedException e) {
            System.err.println("Cliente interrumpido");
            Thread.currentThread().interrupt();
        } finally {
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
        }
    }
    
    private String requestTime(DatagramSocket socket, InetAddress serverAddress, int serverPort) {
        try {
            // Enviar solicitud (datagrama vacío o con un simple mensaje)
            byte[] sendData = "TIME".getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverAddress, serverPort);
            socket.send(sendPacket);
            
            // Recibir respuesta
            byte[] receiveBuffer = new byte[256];
            DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
            socket.receive(receivePacket);
            
            // Procesar respuesta
            String timeReceived = new String(receivePacket.getData(), 0, receivePacket.getLength());
            return timeReceived;
            
        } catch (SocketTimeoutException e) {
            // Timeout esperado - servidor no respondió
            return null;
        } catch (IOException e) {
            System.err.println("Error de comunicación: " + e.getMessage());
            return null;
        }
    }
    
    private String getCurrentTimestamp() {
        return new java.text.SimpleDateFormat("HH:mm:ss").format(new java.util.Date());
    }
    
    public static void main(String[] args) {
        String serverAddress = "127.0.0.1";
        int serverPort = 4445;
        
        // Permitir configuración por argumentos de línea de comandos
        if (args.length > 0) {
            serverAddress = args[0];
        }
        if (args.length > 1) {
            try {
                serverPort = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.err.println("Puerto inválido, usando puerto 4445");
            }
        }
        
        DatagramTimeClient client = new DatagramTimeClient();
        client.start(serverAddress, serverPort);
    }
}