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
    public static Frase getFrase(int tipo){
        Frase f = null;
        switch(tipo){
            case 1 : f = new FraseConsDup();
            case 2 : f = new FraseConsNoDup();
            case 3 : f = new FraseNoConsNoDup();
            //case 4 : ?? leer?
        }
        return f;
    } 
    
}
