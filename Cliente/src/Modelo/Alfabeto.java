package Modelo;

public class Alfabeto {

    private String elementos;
    private String id;

    //Alfabeto default
    public Alfabeto() {
        this.id = "default";
        this.elementos = "abcdefghijklmnopqrstuvwxyz ";
    }
    
    
    //Alfabeto distinto
    public Alfabeto(String id, String elementos) {
        this.elementos = elementos;
        this.id = id;
    }

    //GETTERS Y SETTERS
    
    public String getElementos() {
        return elementos;
    }

    public void setElementos(String elementos) {
        this.elementos = elementos;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    

}