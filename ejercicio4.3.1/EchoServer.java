import java.net.*;
import java.io.*;

public class EchoServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(35000);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }

        Socket clientSocket = null;
        try {
            clientSocket = serverSocket.accept();
        } catch (IOException e) {
            System.err.println("Accept failed.");
            System.exit(1);
        }

        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        BufferedReader in = new BufferedReader(
            new InputStreamReader(
            clientSocket.getInputStream()));
        
        String inputLine, outputLine;

        while ((inputLine = in.readLine()) != null) {
            System.out.println("Mensaje:" + inputLine);

            try{
                // Convertir el mensaje a número
                 double numero = Double.parseDouble(inputLine);
                 // Calcular el cuadrado
                 double cuadrado = numero * numero;
                 outputLine = "El cuadrado de " + numero + " es: " + cuadrado;
            }catch (NumberFormatException e){
                outputLine = "Error: No se ha ingresado un número válido.";
            }

            out.println(outputLine);

            if (inputLine.equalsIgnoreCase("bye")){
                System.out.println("Cliente solicitó terminar la conexión");
                break;
            }
        }
        out.close();
        in.close();
        clientSocket.close();
        serverSocket.close();
    } 
}
