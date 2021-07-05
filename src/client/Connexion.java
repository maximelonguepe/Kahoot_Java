package client;

import pootp1.Joueur;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * cette classe permet d'instancier une connexion avec le serveur
 *
 */
public class Connexion {

    Socket socket;


    private ObjectInputStream ois;
    private ObjectOutputStream oos ;


    private String iD;



    public Connexion(Socket socket) throws IOException {

        this.socket=socket;
        ois = new ObjectInputStream(socket.getInputStream());
        oos = new ObjectOutputStream(socket.getOutputStream());
    }

    public ObjectInputStream getOis() {
        return ois;
    }

    public ObjectOutputStream getOos() {
        return oos;
    }

    public void close(){
        try {
            ois.close();
            oos.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
