package com.mycompany.servidor;

/**
 * Productor de agentes que añade agentes a un buffer de manera asíncrona.
 * @author suyan
 */
public class Productor extends Thread {
    private Buffer buffer;
    private volatile boolean estado = true;  // Volatile para asegurar la visibilidad entre hilos
    private String nombreAgente;
    private String descripcion;

    public Productor(Buffer buffer, String nombreAgente, String descripcion) {
        this.buffer = buffer;
        this.nombreAgente = nombreAgente;
        this.descripcion = descripcion;
    }

    @Override
    public void run() {
        while (estado) {
            // Verificar si nombreAgente y descripcion no están vacíos
            if (nombreAgente != null && !nombreAgente.trim().isEmpty() && descripcion != null && !descripcion.trim().isEmpty()) {
                try {
                    sleep((int) (Math.random() * 4000));  // Simular tiempo de procesamiento
                    AgentServidor agente = new AgentServidor(nombreAgente, descripcion);
                    buffer.agregarAgente(agente);  // Agregar agente al buffer
                } catch (InterruptedException e) {
                    System.out.println("Error: " + e);
                    Thread.currentThread().interrupt();  // Restablecer el estado de interrupción
                } catch (Exception e) {
                    System.out.println("Error al agregar agente al buffer: " + e);
                }
            } else {
                estado = false;
                System.out.println("Error: Datos incompletos para el agente");
            }
        }
    }

    // Método para detener el hilo externamente
    public void detener() {
        estado = false;
    }
}
