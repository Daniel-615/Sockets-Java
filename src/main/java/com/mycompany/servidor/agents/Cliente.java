/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.servidor.agents;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author suyan
 */
public class Cliente {
    private String nombre,apellido;
    private String telefono;
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    public Cliente(String nom,String ape,String tel){
        this.nombre=nom;
        this.apellido=ape;
        this.telefono=tel;
    }
    public static void main(String[] args){
        try {
            Scanner sn=new Scanner(System.in);
            sn.useDelimiter("\n");
            Socket sc=new Socket("127.0.0.1",5000);
            DataInputStream in=new DataInputStream(sc.getInputStream());
            DataOutputStream out=new DataOutputStream(sc.getOutputStream());
            String mensaje=in.readUTF();
            System.out.println(mensaje);
            String nombre=sn.next();
            out.writeUTF(nombre);
            ClienteHilo hilo=new ClienteHilo(in,out);
            hilo.start();
            hilo.join();
            
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
                
    }
}
