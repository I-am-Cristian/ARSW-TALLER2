package chatrmi;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class ChatPeer {

    private static final String SERVICE_NAME = "ChatService";

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        try {
            System.out.print("Tu nombre: ");
            String nombre = scanner.nextLine();

            System.out.print("Puerto donde publicarás tu servicio: ");
            int puertoLocal = Integer.parseInt(scanner.nextLine());

            // Crear y publicar servicio local
            Registry registry = LocateRegistry.createRegistry(puertoLocal);
            ChatServiceImpl servicioLocal = new ChatServiceImpl(nombre);
            registry.rebind(SERVICE_NAME, servicioLocal);

            System.out.println("Servicio publicado en puerto " + puertoLocal);
            System.out.println("Tu servicio está listo para recibir mensajes.\n");

            System.out.print("IP del otro usuario: ");
            String ipRemota = scanner.nextLine();

            System.out.print("Puerto del otro usuario: ");
            int puertoRemoto = Integer.parseInt(scanner.nextLine());

            // Intentar conectar al otro usuario con reintentos
            ChatService servicioRemoto = null;
            while (servicioRemoto == null) {
                try {
                    System.out.println("Intentando conectar a " + ipRemota + ":" + puertoRemoto + "...");
                    Registry remoteRegistry = LocateRegistry.getRegistry(ipRemota, puertoRemoto);
                    servicioRemoto = (ChatService) remoteRegistry.lookup(SERVICE_NAME);
                    System.out.println("Conectado al otro usuario.");
                } catch (Exception e) {
                    System.out.println("Esperando al otro usuario...)");
                    Thread.sleep(2000);
                }
            }

            System.out.println("\n CHAT INICIADO ");
            System.out.println("Escribe 'salir' para terminar.\n");

            final ChatService remoto = servicioRemoto;
            final String nombreFinal = nombre;

            // Hilo para leer del teclado y enviar mensajes
            Thread lecturaTeclado = new Thread(() -> {
                Scanner teclado = new Scanner(System.in);
                while (true) {
                    System.out.print(">> ");
                    String mensaje = teclado.nextLine();
                    if (mensaje.equalsIgnoreCase("salir")) {
                        System.out.println("Cerrando chat...");
                        System.exit(0);
                    }
                    try {
                        remoto.recibirMensaje(nombreFinal + ": " + mensaje);
                    } catch (Exception e) {
                        System.err.println("Error al enviar: " + e.getMessage());
                    }
                }
            });
            lecturaTeclado.start();
            lecturaTeclado.join();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}