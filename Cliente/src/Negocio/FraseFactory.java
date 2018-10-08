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
public class FraseFactory {
    public static Frase getFrase(TipoFrase tipo, int longitud){
        Frase f = null;
        f.setLongitud(longitud);
        switch(tipo){
            case FraseConsDup : f = new FraseConsDup();
            case FraseConsNoDup : f = new FraseConsNoDup();
            case FraseNoConsNoDup : f = new FraseNoConsNoDup();
            //case FraseArchivo : ?? leer?
        }
        return f;
    } 
    
}
