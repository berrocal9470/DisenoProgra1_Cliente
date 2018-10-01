package Negocio;

import Modelo.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;

public class DTO implements Serializable{

    //fecha y hora de la consulta
    private Calendar fechaHora;
    //texto a encriptar/desencriptar
    private String textoInicial;
    //modo encriptación=true, desencriptar=false
    private boolean modo;
    //nombres de los algoritmos para mostrar al usuario
    private ArrayList<Integer> algoritmosSeleccionados;
    //guarda el nombre de los algoritmos para guardar en el archivo
    private ArrayList<String> nombresAlgoritmos;
    //identificadores de los alfabetos para mostrar al usuario
    private ArrayList<String> idAlfabetos;
    //los resultados de las operaciones con los algoritmos
    private ArrayList<String> resultados;
    //tipos de archivos de salida
    private ArrayList<String> tiposArchivos;
    //nuevo alfabeto para agregar/modificar
    private String alfabeto;
    
    //tipo de operación a realizar
    private TipoOperacion tipoOperacion;
    
    public DTO(){ }
    
    // GETTERS Y SETTERS

    public Calendar getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Calendar fechaHora) {
        this.fechaHora = fechaHora;
    }

    public String getTextoInicial() {
        return textoInicial;
    }

    public void setTextoInicial(String textoInicial) {
        this.textoInicial = textoInicial;
    }

    public boolean isModo() {
        return modo;
    }

    public void setModo(boolean modo) {
        this.modo = modo;
    }

    public ArrayList<Integer> getAlgoritmosSeleccionados() {
        return algoritmosSeleccionados;
    }

    public void setAlgoritmosSeleccionados(ArrayList<Integer> algoritmos) {
        this.algoritmosSeleccionados = algoritmos;
    }

    public ArrayList<String> getIdAlfabetos() {
        return idAlfabetos;
    }

    public void setIdAlfabetos(ArrayList<String> idAlfabetos) {
        this.idAlfabetos = idAlfabetos;
    }

    public ArrayList<String> getResultados() {
        return resultados;
    }

    public void setResultados(ArrayList<String> resultados) {
        this.resultados = resultados;
    }

    public ArrayList<String> getTiposArchivos() {
        return tiposArchivos;
    }

    public void setTiposArchivos(ArrayList<String> tiposArchivos) {
        this.tiposArchivos = tiposArchivos;
    }

    public String getAlfabeto() {
        return alfabeto;
    }

    public void setAlfabeto(String alfabeto) {
        this.alfabeto = alfabeto;
    }

    public ArrayList<String> getNombresAlgoritmos() {
        return nombresAlgoritmos;
    }

    public void setNombresAlgoritmos(ArrayList<String> nombresAlgoritmos) {
        this.nombresAlgoritmos = nombresAlgoritmos;
    }

    public TipoOperacion getTipoOperacion() {
        return tipoOperacion;
    }

    public void setTipoOperacion(TipoOperacion tipoOperacion) {
        this.tipoOperacion = tipoOperacion;
    }
    
}