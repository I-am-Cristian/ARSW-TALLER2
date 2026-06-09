import java.io.*;
import java.net.*;

public class URLReader {

    @SuppressWarnings("deprecation")
    public static void main(String[] args) throws Exception {
        BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Ingrese la dirección URL: ");
        String urlString = consoleReader.readLine();

        URL url = new URL(urlString);
    
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
             PrintWriter fileWriter = new PrintWriter(new FileWriter("pagina.html"))){

            String inputLine = null;
            while ((inputLine = reader.readLine()) != null) {
                fileWriter.println(inputLine); 
            }
            System.out.println("\nArchivo 'pagina.html' guardado exitosamente.");
        } catch (IOException x) {
            System.err.println(x);
        }
    }
}