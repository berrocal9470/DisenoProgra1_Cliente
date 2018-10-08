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
public class FraseConsNoDup extends Frase{

    @Override
    public String generar() {
        String resultado = "";
        
        int tamannoPalabras[] = {4,5,6}; //Las palabras podrían ser de estos tamaños
        
        int largo;
        int posicion;
        while(resultado.length()<=longitud){
            largo = (int)(Math.random()*(tamannoPalabras.length));
            int largoPalabra = tamannoPalabras[largo]-1;
            for(int i=0;i<=largoPalabra;i++){
                posicion = (int)(Math.random()*(alfabeto.length()-1));
                if(resultado.contains(String.valueOf(alfabeto.charAt(posicion)))) //Verificar que los simbolos no se duplican
                    continue;             
                else
                resultado = resultado.concat(String.valueOf(alfabeto.charAt(posicion)));                         
            }
            resultado = resultado.concat(" ");
        }
        
        resultado = resultado.substring(0, longitud);
        if(String.valueOf(resultado.charAt(longitud-1)).equals(" ")){
            posicion = (int)(Math.random()*(alfabeto.length()-1));
            resultado = resultado.concat(String.valueOf(alfabeto.charAt(posicion))); 
        }
        System.out.println(resultado);
        return resultado;
    }
    
}
