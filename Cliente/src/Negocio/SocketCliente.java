/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Negocio;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import comunicacion.DTO;
import comunicacion.TipoOperacion;

/**
 *
 * @author Berrocal
 */
public class SocketCliente {
    private String HOST =  "localhost";
    private int PUERTO = 5000;

    private InputStream conexionEntrada;
    private ObjectInputStream flujoLectura;

    private OutputStream conexionSalida;
    private ObjectOutputStream flujoEscritura;
    
    //controlador
    public SocketCliente(){ }
    
    /**
     * Envía las peticiones al servidor
     * @param dto Objeto con la información a transmitir
     * @return DTO con la información de respuesta
     */
    public DTO realizarPeticion(DTO dto) {
        try {
            Socket cliente = new Socket(HOST, PUERTO);
            
            //Establece comunicacion con el servidor - Escritura
            System.out.println("Estableciendo comunicacion de escritura con el server...");
            conexionSalida = cliente.getOutputStream();
            flujoEscritura = new ObjectOutputStream(conexionSalida);
            
            //Establece comunicacion con el servidor - Lectura
            System.out.println("Estableciendo comunicacion de lectura con el server...");
            conexionEntrada = cliente.getInputStream();
            flujoLectura = new ObjectInputStream(conexionEntrada);

            // procesar la gestion a solicitar
            System.out.println("Saliendo...");
            flujoEscritura.writeObject(dto);
            flujoEscritura.flush();
            
            // recupera la respuesta del servidor...
            System.out.println("Recibiendo...");
            dto = (DTO) flujoLectura.readObject();

            flujoEscritura.close();
            flujoLectura.close();
            cliente.close();
        }catch (ClassNotFoundException | UnknownHostException  ex) {
            System.out.println("Conectandose a un servidor desconocido");
        }catch (IOException ex) {
            System.out.println("Problemas con los flujos de entrada / salida");
        }

        return dto;
    }

    // GETTERS Y SETTERS
    
    public String getHOST() {
        return HOST;
    }

    public void setHOST(String HOST) {
        this.HOST = HOST;
    }

    public int getPUERTO() {
        return PUERTO;
    }

    public void setPUERTO(int PUERTO) {
        this.PUERTO = PUERTO;
    }
    
}
