
import java.net.MalformedURLException;
import java.net.URL;

public class URLModel {

    @SuppressWarnings("deprecation")
    public static void URLModel() throws MalformedURLException {
        URL github = new URL("https://github.com:443/usuario/repo/issues?estado=abierto&autor=user#cristian");

        System.out.println("getProtocol:" + github.getProtocol());
        System.out.println("getAuthority:" + github.getAuthority());
        System.out.println("getHost:" + github.getHost());
        System.out.println("getPort: " + github.getPort());
        System.out.println("getPath:" + github.getPath());
        System.out.println("getQuery:" + github.getQuery());
        System.out.println("getFile: " + github.getFile());
        System.out.println("getRef:" + github.getRef());
    }

    public static void main(String[] args) throws MalformedURLException {
        URLModel();
    }

}