/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Negocio;

import Modelo.Alfabeto;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author Berrocal
 */
public class Controlador {
    private Socket socket;
    
    public Controlador(){
        socket = new Socket();
    }
    
    public ArrayList<String> operar(DTO dto){
        return null;
    }
    
    public Alfabeto consultarAlfabeto(){
        return null;
    }
    
    public ArrayList<Alfabeto> consultarAlfabetos(){
        return null;
    }
    
    public ArrayList<String> consultarAlgoritmos(){
        return null;
    }
    
    public ArrayList<String> consultarArchivosSalida(){
        return null;
    }
}
