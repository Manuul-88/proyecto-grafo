//TLista 
package modelo;
public class TLista {
    private TNodo cab, fin;
    public TLista() {
        cab = new TNodo();
        fin = null;
    }
    public boolean esVacia() {
        return cab.getSiguiente() == null;
    }
    public TNodo getCabecera() {
        return cab;
    }
    public void insertarInicio(String dato) {
        TNodo p = new TNodo(dato);
        if (cab.getSiguiente() == null) {
            fin = p;
        }
        p.setSiguiente(cab.getSiguiente());
        cab.setSiguiente(p);
    }
    public void insertarFinal(String dato) {
        TNodo p = new TNodo(dato);
        
        if (fin == null) {
            cab.setSiguiente(p);
        } else {
            fin.setSiguiente(p);
        }

        fin = p;
    }
    public void insertarFinal(String dato, int peso) {
        TNodo p = new TNodo(dato, peso);

        if (fin == null) {
            cab.setSiguiente(p);
        } else {
            fin.setSiguiente(p);
        }

        fin = p;
        }
    
    public boolean buscar(String dato) {
        TNodo p = cab.getSiguiente();
        while (p != null) {
            if (p.getDato().equalsIgnoreCase(dato)) {
                return true;
            }
            p = p.getSiguiente();
        }
        return false;
    }
    public String cadenaLista() {
        String cad = "";
        TNodo p = cab.getSiguiente();
        while (p != null) {
            cad += p.getDato() + "(" + p.getPeso() + ")";
            if (p.getSiguiente() != null) {
                cad += " -> ";
            }
            p = p.getSiguiente();
        }
        return cad;
    }
    
    public boolean eliminar(String dato) {
    TNodo anterior = cab;
    TNodo actual = cab.getSiguiente();

    while (actual != null) {
        if (actual.getDato().equalsIgnoreCase(dato)) {
            anterior.setSiguiente(actual.getSiguiente());

            if (actual == fin) {
                fin = anterior == cab ? null : anterior;
            }

            return true;
        }

        anterior = actual;
        actual = actual.getSiguiente();
    }

    return false;
}
    
   public int obtenerPeso(String destino) {

    TNodo p = cab.getSiguiente();

    while (p != null) {

        if (p.getDato().equalsIgnoreCase(destino)) {
            return p.getPeso();
        }

        p = p.getSiguiente();
    }

    return -1;
    }

    public int contar() {

        int contador = 0;

        TNodo p = cab.getSiguiente();

        while (p != null) {

            contador++;
            p = p.getSiguiente();
        }

        return contador;
    } 
    
    public boolean editarDato(String actual, String nuevo) {
    TNodo p = cab.getSiguiente();

    while (p != null) {
        if (p.getDato().equalsIgnoreCase(actual)) {
            p.setDato(nuevo);
            return true;
        }
        p = p.getSiguiente();
    }

    return false;
}

public boolean editarPeso(String destino, int nuevoPeso) {
    TNodo p = cab.getSiguiente();

    while (p != null) {
        if (p.getDato().equalsIgnoreCase(destino)) {
            p.setPeso(nuevoPeso);
            return true;
        }
        p = p.getSiguiente();
    }

    return false;
}
    
    
}//fin