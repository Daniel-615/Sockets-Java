package com.mycompany.servidor;

import com.mycompany.servidor.tickets.Tickets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServidorHilo extends Thread {
    private final Socket socket;
    private Buffer buffer;
    private boolean status=true;
    private int agents_process=0, total_agents=0;
    private String nombreAgente,descripcion;
    public ServidorHilo(Socket socket,Buffer buffer,String nombreAgente,String descripcion) {
        this.socket = socket;
        this.buffer=buffer;
        this.nombreAgente=nombreAgente;
        this.descripcion=descripcion;
    }

    @Override
    public void run() {
        DataOutputStream dataOut = null;
        try {
            dataOut = new DataOutputStream(socket.getOutputStream());
            while (status) {
                AgentServidor agente = this.buffer.atenderAgente();
                if (agente != null) {
                    try {
                        sleep((int) (Math.random() * 6000));
                        System.out.println("Agente procesado");
                        Tickets ticket = new Tickets(nombreAgente, descripcion);
                        Servidor.agregarTicket(ticket);
                        // Enviar respuesta al cliente
                        //dataOut.writeUTF("Comunicación con el agente iniciada. Token: " + ticket.getId());
                        dataOut.writeUTF("Ticket: "+ticket.toString());
                        sleep((int) (Math.random()*2000));
                        System.out.println("Tarea procesada");
                        ticket.setCompletado(true);
                        //dataOut.writeUTF("Comunicacion con el agente terminada. Token: "+ticket.getId());
                        //dataOut.writeUTF("Ticket enviado: " + Servidor.getTicket(ticket.getId()));
                        dataOut.writeUTF("Ticket: "+ticket.toString());
                        agents_process++;
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ServidorHilo.class.getName()).log(Level.SEVERE, null, ex);
                        Thread.currentThread().interrupt(); 
                    } catch (IOException ex) {
                        Logger.getLogger(ServidorHilo.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                // Verificar si todas las tareas están completas
                if (agents_process == total_agents) {
                    status = false;
                }
            }
        } catch (IOException ex) {
            Logger.getLogger(ServidorHilo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void recibir(){
        DataInputStream dataIn = null;
        DataOutputStream dataOut = null;
        ObjectInputStream objectIn = null;
        ObjectOutputStream objectOut = null;

        try {
            // Configurar streams de entrada y salida
            dataIn = new DataInputStream(socket.getInputStream());
            dataOut = new DataOutputStream(socket.getOutputStream());
            objectIn = new ObjectInputStream(socket.getInputStream());
            objectOut = new ObjectOutputStream(socket.getOutputStream());

            // Leer datos del agente (datos individuales)
            /*String nombreAgente = dataIn.readUTF();
            System.out.println("Nombre del agente: " + nombreAgente);

            String descripcion = dataIn.readUTF();
            System.out.println("Descripción: " + descripcion);
            */
            // Crear un ticket con la información recibida
            Tickets ticket = new Tickets(nombreAgente, descripcion);
            Servidor.agregarTicket(ticket);

            // Enviar respuesta al cliente
            dataOut.writeUTF("Comunicación con el agente iniciada. Token: " + ticket.getId());
            dataOut.writeUTF("Ticket enviado: " + Servidor.getTicket(ticket.getId()));

        } catch (IOException ex) {
            Logger.getLogger(ServidorHilo.class.getName()).log(Level.SEVERE, "Error en la comunicación con el cliente", ex);
        } finally {
            try {
                // Cerrar todos los streams y el socket
                if (dataIn != null) {
                    dataIn.close();
                }
                if (dataOut != null) {
                    dataOut.close();
                }
                if (objectIn != null) {
                    objectIn.close();
                }
                if (objectOut != null) {
                    objectOut.close();
                }
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(ServidorHilo.class.getName()).log(Level.SEVERE, "Error al cerrar los streams y el socket", ex);
            }
        }
    }
}
