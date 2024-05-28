package com.mycompany.servidor;

import com.mycompany.servidor.tickets.Tickets;
import java.io.DataInputStream;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Servidor {
    private static List<Tickets> ticketsList = new ArrayList<>();
    static Buffer b = new Buffer(10);
    public static void main(String[] args) {
        DataInputStream dataIn = null;
        try {
            ServerSocket server = new ServerSocket(5000);
            System.out.println("Servidor iniciado");
            while (true) {
                Socket sc = server.accept();
                dataIn=new DataInputStream(sc.getInputStream());
                System.out.println("Cliente conectado desde " + sc.getInetAddress());
                
                String nombreAgente = dataIn.readUTF();
                System.out.println("Nombre del agente: " + nombreAgente);

                String descripcion = dataIn.readUTF();
                System.out.println("Descripcion: " + descripcion);
                //Crear el consumidor
                ServidorHilo[] servidor=new ServidorHilo[3];
                for(int i=0;i<3;i++){
                    servidor[i]=new ServidorHilo(sc,b,nombreAgente,descripcion);
                }
                //Producir
                Productor p=new Productor(b,nombreAgente,descripcion);
                p.start();
                //Consumir
                System.out.println("consumiendo");
                for(int i=0;i<3;i++){
                    servidor[i].start();
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static synchronized void agregarTicket(Tickets ticket) {
        ticketsList.add(ticket);
    }

    public static Tickets getTicket(int id) {
        synchronized (ticketsList) {
            for (Tickets ticket : ticketsList) {
                if (ticket.getId() == id) {
                    return ticket;
                }
            }
        }
        return null;
    }
}
