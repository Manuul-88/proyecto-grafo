package modelo;

public class TNodo {
    private int dato;
    private TNodo sig;
    
    public TNodo(){
        dato = 0;
        sig = null;
    }
    
    public TNodo(int dato){
        this.dato = dato;
        sig = null;
    }
    
    public void setDato(int dato){
        this.dato = dato;
    }
    
    public void setSiguiente(TNodo sig){
        this.sig = sig;
    }
    
    public int getDato(){
        return dato;
    }
    
    public TNodo getSiguiente(){
    return sig;
    }
    
    @Override
    public String toString(){
        String cadena;
        cadena = "Dato del nodo " + dato + "\n";
        return cadena;
    }
    
}
