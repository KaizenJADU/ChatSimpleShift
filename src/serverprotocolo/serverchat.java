package serverprotocolo;
import java.io.*;
import java.net.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class serverchat {
    private static final int PORT = 12000;
    private static CopyOnWriteArrayList<ClientHandler> clients = new CopyOnWriteArrayList<>();

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Servidor En Linea, Puerto: " + PORT);
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Se unio un dos");
                ClientHandler client = new ClientHandler(clientSocket);
                clients.add(client);
                client.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler extends Thread {
        private Socket clientSocket;
        private PrintWriter out;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String message;
            while (true) {
                message = in.readLine();
                if (message == null || "Adios".equalsIgnoreCase(message)) {
                    break;
                }
                System.out.println("Mensaje Recibido: " + message);
                serverchat.broadcastMessage(message, this);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

        public void sendMessage(String message) {
            out.println(message);
        }
    }

   public static void broadcastMessage(String message, ClientHandler sender) {
        for (ClientHandler client : clients) {
            if (client != sender) {
                client.sendMessage(message);
            }
        }
    }
}
