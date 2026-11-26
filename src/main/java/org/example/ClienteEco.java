package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Cliente eco: lee líneas desde teclado, las envía al servidor y muestra
 * la respuesta (en mayúsculas). Termina cuando el usuario escribe "FIN".
 */
public class ClienteEco {

    private static final String HOST = "localhost";
    private static final int PUERTO = 5000;

    public static void main(String[] args) {
        // Conectamos a localhost:5000 y obtenemos los flujos (try-with-resources)
        try (Socket socket = new Socket(HOST, PUERTO);
             BufferedReader entrada = new BufferedReader(
                     new InputStreamReader(socket.getInputStream()));
             PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Conectado al servidor eco. Escribe mensajes (FIN para salir):");

            String mensaje;
            // Bucle: leer de teclado, enviar, recibir respuesta y mostrarla
            while ((mensaje = teclado.readLine()) != null) {
                salida.println(mensaje);

                // Si el usuario escribe "FIN", salimos sin esperar respuesta
                if (mensaje.equalsIgnoreCase("FIN")) {
                    break;
                }

                String respuesta = entrada.readLine();
                if (respuesta == null) {
                    System.out.println("El servidor ha cerrado la conexión.");
                    break;
                }
                System.out.println("Servidor: " + respuesta);
            }
            System.out.println("Cliente finalizado.");
        } catch (IOException e) {
            System.err.println("Error en el cliente: " + e.getMessage());
        }
    }
}
