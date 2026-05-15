package modelo;

public class TLista {
    private final TNodo cabecera;
    private TNodo fin;

    public TLista() {
        cabecera = new TNodo();
        fin = null;
    }

    public TNodo getCabecera() {
        return cabecera;
    }

    public boolean esVacia() {
        return cabecera.getSiguiente() == null;
    }

    
    public void insertarFinal(String dato) {
        insertarFinal(dato, 0);
    }

    public void insertarFinal(String dato, int peso) {
        TNodo nuevo = new TNodo(dato, peso);

        if (esVacia()) {
            cabecera.setSiguiente(nuevo);
        } else {
            fin.setSiguiente(nuevo);
        }

        fin = nuevo;
    }

    public boolean buscar(String dato) {
        return obtenerNodo(dato) != null;
    }

    public int obtenerPeso(String dato) {
        TNodo nodo = obtenerNodo(dato);
        return nodo == null ? -1 : nodo.getPeso();
    }

    public boolean editarDato(String actual, String nuevo) {
        TNodo nodo = obtenerNodo(actual);
        if (nodo == null) return false;

        nodo.setDato(nuevo);
        return true;
    }

    public boolean editarPeso(String dato, int nuevoPeso) {
        TNodo nodo = obtenerNodo(dato);
        if (nodo == null) return false;

        nodo.setPeso(nuevoPeso);
        return true;
    }

    public boolean eliminar(String dato) {
        TNodo anterior = cabecera;
        TNodo actual = cabecera.getSiguiente();

        while (actual != null) {
            if (actual.getDato().equalsIgnoreCase(dato)) {
                anterior.setSiguiente(actual.getSiguiente());

                if (actual == fin) {
                    fin = anterior == cabecera ? null : anterior;
                }

                return true;
            }

            anterior = actual;
            actual = actual.getSiguiente();
        }

        return false;
    }

    public int contar() {
        int total = 0;
        TNodo actual = cabecera.getSiguiente();

        while (actual != null) {
            total++;
            actual = actual.getSiguiente();
        }

        return total;
    }

    public String cadenaLista() {
        StringBuilder sb = new StringBuilder();
        TNodo actual = cabecera.getSiguiente();

        while (actual != null) {
            sb.append(actual.getDato()).append("(").append(actual.getPeso()).append(")");

            if (actual.getSiguiente() != null) {
                sb.append(" -> ");
            }

            actual = actual.getSiguiente();
        }

        return sb.toString();
    }

    private TNodo obtenerNodo(String dato) {
        TNodo actual = cabecera.getSiguiente();

        while (actual != null) {
            if (actual.getDato().equalsIgnoreCase(dato)) {
                return actual;
            }

            actual = actual.getSiguiente();
        }

        return null;
    }
}