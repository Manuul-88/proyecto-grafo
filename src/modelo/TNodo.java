package modelo;

public class TNodo {
    private String dato;
    private int peso;
    private TNodo siguiente;

    public TNodo() {
        this("", 0);
    }

    public TNodo(String dato) {
        this(dato, 0);
    }

    public TNodo(String dato, int peso) {
        this.dato = dato;
        this.peso = peso;
        this.siguiente = null;
    }

    public String getDato() {
        return dato;
    }

    public void setDato(String dato) {
        this.dato = dato;
    }

    public int getPeso() {
        return peso;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }

    public TNodo getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(TNodo siguiente) {
        this.siguiente = siguiente;
    }

    @Override
    public String toString() {
        return dato + "(" + peso + ")";
    }
}
