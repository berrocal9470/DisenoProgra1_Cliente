/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Negocio;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author Berrocal
 */
public class DAOtxt {
    
    /**
     * Consulta el texto de un archivo
     * Es usado en DAOAlfabeto
     * @param archivo nombre del archivo
     * @return texto del archivo
     */
    public String consultar(String archivo){ 
        BufferedReader br;
        try { 
            br = new BufferedReader(new FileReader(new File(archivo)));
            String st;
            String result = "";
            while ((st = br.readLine()) != null){ 
                result += st + "\n";
            }
            result = result.replaceAll("\n", "");
            return result;
        } catch (FileNotFoundException ex) {
            return null;
        } catch (IOException ex) {
            return null;
        }
    }
}
