package com.mycompany.servidor.agents;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Agent extends Thread {
    private int id;
    private String name, description, position;
    public Agent(int id, String name, String description, String position) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.position = position;
    }

    public void greetCustomer(String customerName) {
        System.out.println("Hola soy " + name + ". Bienvenido, " + customerName + ". ¿En qué puedo ayudarte?");
    }

    public void serverRequest() {
        try {
            Socket sc = new Socket("127.0.0.1", 5000);
            DataInputStream in = new DataInputStream(sc.getInputStream());
            DataOutputStream out = new DataOutputStream(sc.getOutputStream());

            // Enviar el nombre del agente al servidor
            out.writeUTF(name);
            out.writeUTF(description);
            // Leer el token del servidor
            String token = in.readUTF();
            System.out.println("Token recibido del servidor: " + token);

            // Cerrar la conexión
            sc.close();

            // Iniciar la comunicación con el cliente utilizando el token recibido
            communicateWithClient(token);

        } catch (IOException ex) {
            Logger.getLogger(Agent.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void communicateWithClient(String token) {
        System.out.println("Comunicacion con el cliente iniciada utilizando el token: " + token);
    }

    @Override
    public void run() {
        serverRequest();
    }
}
