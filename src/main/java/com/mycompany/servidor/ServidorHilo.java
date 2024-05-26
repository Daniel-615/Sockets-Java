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
        while(status){
            System.out.println("atendiendo el cliente");
            AgentServidor agente=buffer.atenderAgente();
            System.out.println("clase "+agente);
            DataOutputStream dataOut = null;
            System.out.println("estas dentro del hilo");
            if(agente!=null){
                try{
                    dataOut = new DataOutputStream(socket.getOutputStream());
                    sleep((int) (Math.random() * 6000));
                    System.out.println("Agente procesado");
                    agents_process++;
                    Tickets ticket = new Tickets(nombreAgente, descripcion);
                    Servidor.agregarTicket(ticket);
                    // Enviar respuesta al cliente
                    dataOut.writeUTF("Comunicación con el agente iniciada. Token: " + ticket.getId());
                    dataOut.writeUTF("Ticket enviado: " + Servidor.getTicket(ticket.getId()));
                } catch (InterruptedException ex) {
                    Logger.getLogger(ServidorHilo.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(ServidorHilo.class.getName()).log(Level.SEVERE, null, ex);
                }finally{
                    try{
                        if (dataOut != null) {
                            dataOut.close();
                        }
                        if (socket != null) {
                            socket.close();
                        }
                    } catch (IOException ex) {
                       Logger.getLogger(ServidorHilo.class.getName()).log(Level.SEVERE, "Error al cerrar los streams y el socket", ex);
                    }
                }
            }
            //colocar tambien que la tarea esta completa en el if
            if (agents_process == total_agents) {
                status = false;  
            }
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
