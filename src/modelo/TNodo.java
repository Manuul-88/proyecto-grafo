//TNodo
package modelo;
public class TNodo {
    private String dato;
    private TNodo sig;
    public TNodo() {
        dato = "";
        sig = null;
    }
    public TNodo(String dato) {
        this.dato = dato;
        sig = null;
    }
    public void setDato(String dato) {
        this.dato = dato;
    }
    public void setSiguiente(TNodo sig) {
        this.sig = sig;
    }
    public String getDato() {
        return dato;
    }
    public TNodo getSiguiente() {
        return sig;
    }
    @Override
    public String toString() {
        return "Dato del nodo: " + dato;
    }
}