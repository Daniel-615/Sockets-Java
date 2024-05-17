/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.servidor.tickets;

import java.io.Serializable;

/**
 *
 * @author suyan
 */
public class Tickets implements Serializable {
    private static int contadorTickets = 0;
    private int id;
    private String cliente;
    private String descripcion;
    private boolean resuelto;

    public Tickets(String cliente,String descripcion) {
        this.id=++contadorTickets;
        this.cliente = cliente;
        this.resuelto = false; 
        this.descripcion=descripcion;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isResuelto() {
        return resuelto;
    }

    public void setResuelto(boolean resuelto) {
        this.resuelto = resuelto;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "id=" + id +
                ", cliente='" + cliente + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", resuelto=" + resuelto +
                '}';
    }
    
}