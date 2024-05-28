package com.mycompany.servidor;

public class Buffer {
    private AgentServidor buffer[];
    private int siguiente;     
    // Flags para saber el estado del buffer
    private boolean estaLlena;
    private boolean estaVacia;
    public Buffer(int tamanio) {
        buffer = new AgentServidor[tamanio];
        siguiente = 0;
        estaLlena = false;
        estaVacia = true;
    }
    // Método para retirar clientes del buffer
    public synchronized AgentServidor atenderAgente() {
        // No se puede consumir si el buffer está vacío
        while (estaVacia) {
            try {
                wait(); // Se sale cuando estaVacia cambia a false
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Restores interrupted state
            }
        }
        // Decrementa la cuenta para apuntar al cliente correcto
        siguiente--;
        // Obtiene el agente a ser atendido
        AgentServidor agente = buffer[siguiente];
        // Si se ha retirado el último cliente, el buffer está vacío
        if (siguiente == 0) {
            estaVacia = true;
        }
        // El buffer no puede estar lleno después de atender un agente
        estaLlena = false;
        notifyAll(); // Notifica a los productores que hay espacio disponible
        return agente;
    }
    // Método para añadir clientes al buffer
    public synchronized void agregarAgente(AgentServidor agente) {
        // Espera hasta que haya espacio para otro agente
        while (estaLlena) {
            try {
                wait(); // Espera hasta que haya espacio en el buffer
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Restores interrupted state
            }
        }
        // Añade el cliente al buffer en el primer lugar disponible si el índice siguiente es válido
        if (siguiente < buffer.length) {
            buffer[siguiente] = agente;
            siguiente++;
        }
        // Verifica si el buffer está lleno después de agregar el cliente
        if (siguiente == buffer.length) {
            estaLlena = true;
        }
        // Indica que el buffer no está vacío, ya que se ha agregado un cliente
        estaVacia = false;
        notifyAll(); 
    }
}