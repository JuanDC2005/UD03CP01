package org.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Servidor eco: recibe líneas de texto de un cliente y las devuelve en mayúsculas.
 * Cuando el cliente envía "FIN" se cierra la conexión y el servidor vuelve a
 * accept() para atender al siguiente cliente.
 */
public class ServidorEco {

    private static final int PUERTO = 5000;

    public static void main(String[] args) {
        // Creamos el ServerSocket en el puerto indicado (se cierra solo con try-with-resources)
        try (ServerSocket servidor = new ServerSocket(PUERTO)) {
            System.out.println("Servidor eco escuchando en puerto " + PUERTO);

            // Bucle principal: tras atender a un cliente, volvemos a accept()
            while (true) {
                // Aceptamos una conexión y obtenemos los flujos de entrada/salida
                try (Socket cliente = servidor.accept();
                     BufferedReader entrada = new BufferedReader(
                             new InputStreamReader(cliente.getInputStream()));
                     PrintWriter salida = new PrintWriter(cliente.getOutputStream(), true)) {

                    System.out.println("Cliente conectado: " + cliente.getInetAddress());

                    String linea;
                    // Leemos líneas hasta que el cliente envíe "FIN" o cierre la conexión
                    while ((linea = entrada.readLine()) != null) {
                        if (linea.equalsIgnoreCase("FIN")) {
                            System.out.println("Cliente solicita cierre.");
                            break;
                        }
                        System.out.println("Recibido: " + linea);
                        // Convertimos a mayúsculas y enviamos la respuesta
                        salida.println(linea.toUpperCase());
                    }
                } catch (IOException e) {
                    System.err.println("Error con el cliente: " + e.getMessage());
                }
                System.out.println("Esperando nuevo cliente...");
            }
        } catch (IOException e) {
            System.err.println("Error en el servidor: " + e.getMessage());
        }
    }
}
