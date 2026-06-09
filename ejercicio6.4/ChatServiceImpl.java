package chatrmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ChatServiceImpl extends UnicastRemoteObject implements ChatService {

    private String nombre;

    public ChatServiceImpl(String nombre) throws RemoteException {
        super();
        this.nombre = nombre;
    }

    @Override
    public void recibirMensaje(String mensaje) throws RemoteException {
        System.out.println("\n" + mensaje);
        System.out.print(">> ");
        System.out.flush();
    }
}