/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Negocio;

import Modelo.Alfabeto;
import java.util.ArrayList;

import comunicacion.DTO;
import comunicacion.TipoOperacion;

/**
 *
 * @author Berrocal
 */
public class Controlador {
    private SocketCliente cliente;
    private DAOtxt txt;
    
    public Controlador(){
        cliente = new SocketCliente();
        txt = new DAOtxt();
    }
    
    public String leerArchivo(String nombreArchivo){
        return txt.consultar(nombreArchivo);
    }
    
    /**
     * Se encarga de encriptar o desencriptar
     * @param dto Objeto con la información necesaria para la operación
     * @return ArrayList de resultados de la encriptación/desencriptación
     */
    public ArrayList<String> operar(DTO dto){
        dto = realizarPeticion(dto, TipoOperacion.OPERAR);
        return dto.getResultados();
    }
    
    /**
     * Consulta el actual alfabeto por defecto
     * @return Alfabeto por defecto en el servidor
     */
    public String consultarAlfabeto(){
        DTO dto = realizarPeticion(new DTO(), TipoOperacion.CONSULTAR_ALFABETO);
        return dto.getElementosAlfabeto();
    }
    
    /**
     * Consulta los alfabetos existentes en el servidor
     * @return ArrayList de alfabetos existentes en el servidor
     */
    public ArrayList<Alfabeto> consultarAlfabetos(){
        DTO dto = realizarPeticion(new DTO(), TipoOperacion.CONSULTAR_ALFABETOS);
        
        if(dto.getAlfabetos() == null){
            return null;
        }else{
            ArrayList<Alfabeto> alfabetos = new ArrayList<>();
            
            for(Object o : dto.getAlfabetos()){
                alfabetos.add((Alfabeto)o);
            }
            
            return alfabetos;
        }
    }
    
    /**
     * Consulta los algoritmos existentes en el servidor
     * @return ArrayList de algoritmos existentes en el servidor
     */
    public ArrayList<String> consultarAlgoritmos(){
        DTO dto = realizarPeticion(new DTO(), TipoOperacion.CONSULTAR_ALGORITMOS);
        return dto.getNombresAlgoritmos();
    }
    
    /**
     * Consulta los archivos de salida existentes en el servidor
     * @return ArrayList de archivos de salida existentes en el servidor
     */
    public ArrayList<String> consultarArchivosSalida(){
        DTO dto = realizarPeticion(new DTO(), TipoOperacion.CONSULTAR_ARCHIVOS);
        return dto.getTiposArchivos();
    }
    
    /**
     * Solicita establecer el alfabeto default
     * Toma el resultado de el ArrayList resultados
     * Si es nulo no tuvo éxito, si tiene un ArrayList vacío tuvo éxito
     * @param dto objeto con el id del alfabeto a establecer por defecto
     * @return true establecido por defecto, false no establecido por defecto
     */
    public boolean establecerAlfabetoDefault(DTO dto){
        dto = realizarPeticion(dto, TipoOperacion.ESTABLECER_ALFABETO_DEFAULT);
        return dto.getResultados() != null;
    }
    
    /**
     * Se encarga de realizar todas las peticiones al servidor
     * @param dto Objeto con la información necesaria para la petición
     * @param operacion TipoOperacion que diferencia cada operación a realizar
     * @return DTO con los resultados de la operación en el servidor
     */
    private DTO realizarPeticion(DTO dto, TipoOperacion operacion){
        dto.setTipoOperacion(operacion);
        return cliente.realizarPeticion(dto);
    }
    
    //Generar la frase
    public String generacionFrase(String tipo,int longitud,String alfabeto){
        FraseFactory f = new FraseFactory();
        Frase f2 = FraseFactory.getFrase(TipoFrase.valueOf(tipo), longitud, alfabeto);
        String resultado = f2.generar();
        return resultado;
    }
    
}
