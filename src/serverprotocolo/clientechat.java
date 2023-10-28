package serverprotocolo;
import java.net.*;
import java.io.*;
import java.text.Normalizer;

public class clientechat {
    private static final String SERVER_IP = "127.0.0.1"; 
    private static final int SERVER_PORT = 12000;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_IP, SERVER_PORT)) {
            System.out.println("Te Conectaste");
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader consoleInput = new BufferedReader(new InputStreamReader(System.in));

            Thread sendMessageThread = new Thread(() -> {
                try {
                    System.out.println("Tu: ");
                    String userInput;
                    while (true) {
                        userInput = consoleInput.readLine();
                        if ("Adios".equalsIgnoreCase(userInput)) {
                            out.println(userInput);
                            break;
                        } else if (!userInput.isEmpty()) {
                            String mensajeCodificado = cifrado(userInput);
                            out.println(mensajeCodificado);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            sendMessageThread.start();

            String response;
            while ((response = in.readLine()) != null) {
                String mensajeDecodificado = descifrado(response);
                System.out.println("Un Dos: " + mensajeDecodificado);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String cifrado(String mensaje) {
        StringBuilder mensajeCifrado = new StringBuilder();
        for (int i = 0; i < mensaje.length(); i++) {
            char c = mensaje.charAt(i);
            if (Character.isLetter(c)) {
                char base = Character.isUpperCase(c) ? 'A' : 'a';
                char letraCifrada = (char) (base + ('Z' - Character.toUpperCase(c)));
                mensajeCifrado.append(letraCifrada);
            } else {
                mensajeCifrado.append(c);
            }
        }
        return mensajeCifrado.toString();
    }

    private static String descifrado(String mensajeCifrado) {
        StringBuilder mensajeDescifrado = new StringBuilder();
        for (int i = 0; i < mensajeCifrado.length(); i++) {
            char c = mensajeCifrado.charAt(i);
            if (Character.isLetter(c)) {
                char base = Character.isUpperCase(c) ? 'A' : 'a';
                char letraDescifrada = (char) (base + ('Z' - Character.toUpperCase(c)));
                mensajeDescifrado.append(letraDescifrada);
            } else {
                mensajeDescifrado.append(c);
            }
        }
        return mensajeDescifrado.toString();
    }
}
