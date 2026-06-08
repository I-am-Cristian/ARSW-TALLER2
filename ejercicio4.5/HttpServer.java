import java.net.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class HttpServer {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(35000);
            System.out.println("Servidor HTTP iniciado en el puerto 35000");
            System.out.println("Directorio base: " + System.getProperty("user.dir"));
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }

        // Bucle principal para aceptar múltiples solicitudes
        while (true) {
            Socket clientSocket = null;
            try {
                System.out.println("\nListo para recibir ...");
                clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado desde: " + clientSocket.getInetAddress());
                
                // Procesar la solicitud
                handleRequest(clientSocket);
                
            } catch (IOException e) {
                System.err.println("Accept failed: " + e.getMessage());
            } finally {
                if (clientSocket != null && !clientSocket.isClosed()) {
                    clientSocket.close();
                }
            }
        }
    }
    
    private static void handleRequest(Socket clientSocket) throws IOException {
        BufferedReader in = new BufferedReader(
            new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        
        // Leer la primera línea de la solicitud HTTP (GET /ruta HTTP/1.x)
        String requestLine = in.readLine();
        if (requestLine == null) {
            return;
        }
        
        System.out.println("Solicitud: " + requestLine);
        
        // Leer el resto de las cabeceras HTTP (hasta línea vacía)
        String line;
        while ((line = in.readLine()) != null && !line.isEmpty()) {
            System.out.println("Header: " + line);
        }
        
        // Parsear la solicitud para obtener la ruta del archivo
        String[] parts = requestLine.split(" ");
        if (parts.length < 2) {
            sendErrorResponse(out, 400, "Bad Request");
            out.close();
            in.close();
            return;
        }
        
        String method = parts[0];
        String filePath = parts[1];
        
        // Solo soportamos GET por ahora
        if (!method.equals("GET")) {
            sendErrorResponse(out, 405, "Method Not Allowed");
            out.close();
            in.close();
            return;
        }
        
        // Si la ruta es "/", servir index.html por defecto
        if (filePath.equals("/")) {
            filePath = "/index.html";
        }
        
        // Eliminar el primer '/' para obtener la ruta relativa
        String relativePath = filePath.substring(1);
        File file = new File(relativePath);
        
        // Verificar si el archivo existe
        if (!file.exists()) {
            sendErrorResponse(out, 404, "File Not Found");
            System.out.println("Archivo no encontrado: " + relativePath);
            out.close();
            in.close();
            return;
        }
        
        // Determinar el tipo MIME basado en la extensión
        String mimeType = getMimeType(relativePath);
        
        // Leer y enviar el archivo
        try {
            byte[] fileContent = Files.readAllBytes(Paths.get(relativePath));
            
            // Enviar cabeceras HTTP
            out.println("HTTP/1.1 200 OK");
            out.println("Content-Type: " + mimeType);
            out.println("Content-Length: " + fileContent.length);
            out.println("Connection: close");
            out.println(); // Línea vacía que separa cabeceras del cuerpo
            
            // Enviar el contenido del archivo (usando OutputStream para datos binarios)
            OutputStream rawOut = clientSocket.getOutputStream();
            rawOut.write(fileContent);
            rawOut.flush();
            
            System.out.println("Archivo servido: " + relativePath + " (" + fileContent.length + " bytes)");
            
        } catch (IOException e) {
            sendErrorResponse(out, 500, "Internal Server Error");
            System.err.println("Error leyendo archivo: " + e.getMessage());
        }
        
        out.close();
        in.close();
    }
    
    private static void sendErrorResponse(PrintWriter out, int code, String message) {
        out.println("HTTP/1.1 " + code + " " + message);
        out.println("Content-Type: text/html");
        out.println("Connection: close");
        out.println();
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head><title>" + code + " " + message + "</title></head>");
        out.println("<body>");
        out.println("<h1>" + code + " " + message + "</h1>");
        out.println("<p>No se pudo encontrar el archivo solicitado.</p>");
        out.println("</body>");
        out.println("</html>");
        out.flush();
    }
    
    private static String getMimeType(String fileName) {
        if (fileName.endsWith(".html") || fileName.endsWith(".htm")) {
            return "text/html";
        } else if (fileName.endsWith(".css")) {
            return "text/css";
        } else if (fileName.endsWith(".js")) {
            return "application/javascript";
        } else if (fileName.endsWith(".png")) {
            return "image/png";
        } else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (fileName.endsWith(".gif")) {
            return "image/gif";
        } else if (fileName.endsWith(".txt")) {
            return "text/plain";
        } else {
            return "application/octet-stream";
        }
    }
}