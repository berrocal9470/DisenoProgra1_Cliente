/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Negocio;

/**
 *
 * @author kathe
 */
public abstract class Frase {
    public int longitud;
    public String alfabeto;
    
    
    public abstract String generar();

    public void setLongitud(int longitud) {
        this.longitud = longitud;
    }

    public void setAlfabeto(String alfabeto) {
        this.alfabeto = alfabeto;
    }
       
}
