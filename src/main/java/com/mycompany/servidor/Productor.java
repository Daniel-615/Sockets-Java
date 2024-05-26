package com.mycompany.servidor;

/**
 * 
* @author suyan
 */
public class Productor extends Thread {
    private Buffer buffer;
    boolean estado=true;
    String nombreAgente,descripcion;
    public Productor(Buffer buffer,String nombreAgente,String descripcion) {
        this.buffer = buffer;
        this.nombreAgente=nombreAgente;
        this.descripcion=descripcion;
    }
    public void run() {
        while(estado) {
                if(" ".equals(nombreAgente) ||" ".equals(descripcion)) {
                    try {
                        sleep((int)(Math.random() * 4000));
                        AgentServidor agente = new AgentServidor(nombreAgente, descripcion);
                        buffer.agregarAgente(agente);
                        } catch(InterruptedException e) {
                            System.out.println("Error: " + e);
                        }
                } else {
                    //System.out.println("Error: Datos incompletos en la posición " + i);
                    estado=false;
                }
            }
        }
    }
