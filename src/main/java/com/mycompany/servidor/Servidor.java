package com.mycompany.servidor;

import com.mycompany.servidor.forms.Form;
import com.mycompany.servidor.tickets.Tickets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Servidor {
    private static List<Tickets> ticketsList = new ArrayList<>();
    private static String descripcion,nombreCliente;
    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(5000);
            Socket sc;
            System.out.println("Servidor iniciado");

            // Mostrar formulario
            Form obj = new Form();
            obj.setVisible(true);

            while (true) {
                sc = server.accept();
                DataInputStream in = new DataInputStream(sc.getInputStream());
                DataOutputStream out = new DataOutputStream(sc.getOutputStream());

                // Leer el nombre del cliente
                nombreCliente = in.readUTF();
                descripcion=in.readUTF();
                // Crear un ticket para el cliente
                Tickets ticket = new Tickets(nombreCliente,descripcion);

                // Agregar el ticket de manera segura
                agregarTicket(ticket);

                out.writeUTF("Tu token es " + ticket.toString());

                // Iniciar un hilo para manejar la conexión con el cliente
                ServidorHilo hilo = new ServidorHilo(in, out, nombreCliente);
                hilo.start();

                System.out.println("Creada la conexion con el cliente " + nombreCliente);
            }
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public static synchronized void agregarTicket(Tickets ticket) {
        ticketsList.add(ticket);
    }
}
