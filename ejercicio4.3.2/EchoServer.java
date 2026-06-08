import java.net.*;
import java.io.*;

public class EchoServer {
    public static void main(String[] args) throws IOException {
        
        ServerSocket serverSocket = null;
        String currentFunction = "cos"; // Por defecto comienza con coseno

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

            // Verificar si es un comando para cambiar función
            if (inputLine.startsWith("fun:")) {
                String functionName = inputLine.substring(4); // Extraer "sin", "cos" o "tan"

                switch (functionName.toLowerCase()) {
                    case "sin":
                        currentFunction = "sin";
                        outputLine = "Función cambiada a: SENO";
                        System.out.println("Función cambiada a: SENO");
                        break;
                    case "cos":
                        currentFunction = "cos";
                        outputLine = "Función cambiada a: COSENO";
                        System.out.println("Función cambiada a: COSENO");
                        break;
                    case "tan":
                        currentFunction = "tan";
                        outputLine = "Función cambiada a: TANGENTE";
                        System.out.println("Función cambiada a: TANGENTE");
                        break;
                    default:
                        outputLine = "Función no reconocida. Las funciones disponibles son: sin, cos, tan.";
                }
                out.println(outputLine);
                continue; // Saltar al siguiente mensaje
            }

            try{
                double numero = Double.parseDouble(inputLine);
                double resultado = 0.0;

                // Calcular según la función actual
                switch (currentFunction) {
                    case "sin":
                        resultado = Math.sin(numero);
                        outputLine = String.format("sin(%.4f) = %.4f", numero, resultado);
                        break;
                    case "cos":
                        resultado = Math.cos(numero);
                        outputLine = String.format("cos(%.4f) = %.4f", numero, resultado);
                        break;
                    case "tan":
                        resultado = Math.tan(numero);
                        outputLine = String.format("tan(%.4f) = %.4f", numero, resultado);
                        break;
                    default:
                        outputLine = "Función no reconocida.";
                }

                System.out.println("Calculando " + currentFunction + "(" + numero + ") = " + resultado);

            }catch (NumberFormatException e){
                outputLine = "Error: No se ha ingresado un número válido o comando reconocido.";
                System.out.println("Error de formato: " + inputLine);
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
