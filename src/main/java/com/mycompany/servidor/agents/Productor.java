/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.servidor.agents;

import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

/**
 * 
* @author suyan
 */
public class Productor extends Thread {
    private Buffer buffer;
    String[][] clientes=new String[10][3];
    boolean estado=true;
    private DefaultTableModel modelo; 
    private JTextArea texto;
    public Productor(Buffer buffer,DefaultTableModel modelo,String[][] datos,JTextArea atencion) {
        this.buffer = buffer;
        this.clientes=datos;
        this.modelo=modelo;
        this.texto=atencion;
    }
    public void run() {
        while(estado) {
            for (String[] client : clientes) {
                if (client != null && client.length >= 3) {
                    String nombre = client[0];
                    String apellido = client[1];
                    String telefono = client[2];
                    if (nombre != null && apellido != null && telefono != null) {
                        try {
                            sleep((int)(Math.random() * 4000));
                            Cliente cliente = new Cliente(nombre, apellido, telefono);
                            buffer.agregarCliente(cliente);
                            eliminarFilaCliente(cliente);
                            SwingUtilities.invokeLater(() -> {
                                texto.append(cliente.getNombre()+" "+cliente.getApellido()+" ha entrado a la barberia. \n");
                            });
                            
                        } catch(InterruptedException e) {
                            System.out.println("Error: " + e);
                        }
                    } else {
                        //System.out.println("Error: Datos incompletos en la posición " + i);
                        estado=false;
                    }   } else {
                    //System.out.println("Error: Datos nulos o incompletos en la posición " + i);
                }
            }
        }
    }
    private void eliminarFilaCliente(Cliente cliente) {
        for (int i = 0; i < modelo.getRowCount(); i++) {
            String nombre = (String) modelo.getValueAt(i, 0);
            String apellido = (String) modelo.getValueAt(i, 1);
            String telefono = (String) modelo.getValueAt(i, 2);

            if (nombre.equals(cliente.getNombre()) && apellido.equals(cliente.getApellido()) && telefono.equals(cliente.getTelefono())) {
                modelo.removeRow(i);
                break; 
            }
        }
    }
}
