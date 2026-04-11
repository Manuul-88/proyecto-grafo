//TGrafo
package modelo;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TGrafo {
    private HashMap<String, TLista> listaAdy;
    private HashMap<String, Boolean> visitados;
    private String recorrido;
    
    public TGrafo() {
        listaAdy = new HashMap<>();
        visitados = new HashMap<>();
        recorrido = "";
    }

    public boolean agregarNodo(String nombre) {
        nombre = normalizar(nombre);
        if (nombre.isEmpty()) {
            return false;
        }
        if (listaAdy.containsKey(nombre)) {
            return false;
        }
        listaAdy.put(nombre, new TLista());
        visitados.put(nombre, false);
        return true;
    }

    public boolean existeNodo(String nombre) {
        nombre = normalizar(nombre);
        return listaAdy.containsKey(nombre);
    }

    public boolean agregaArista(String origen, String destino) {
        origen = normalizar(origen);
        destino = normalizar(destino);
        if (origen.isEmpty() || destino.isEmpty()) {
            return false;
        }
        if (!listaAdy.containsKey(origen) || !listaAdy.containsKey(destino)) {
            return false;
        }
        if (origen.equalsIgnoreCase(destino)) {
            return false;
        }
        boolean agregada = false;
        if (!listaAdy.get(origen).buscar(destino)) {
            listaAdy.get(origen).insertarFinal(destino);
            agregada = true;
        }
        if (!listaAdy.get(destino).buscar(origen)) {
            listaAdy.get(destino).insertarFinal(origen);
            agregada = true;
        }
        return agregada;
    }

    public String obtieneListaAdy() {
        String cad = "Listas de adyacencia:\n\n";
        for (Map.Entry<String, TLista> entry : listaAdy.entrySet()) {
            cad += entry.getKey() + " -> " + entry.getValue().cadenaLista() + "\n";
        }

        return cad;
    }

    private void bpf(String actual) {
        visitados.put(actual, true);
        recorrido += actual + " ";
        TNodo p = listaAdy.get(actual).getCabecera().getSiguiente();
        while (p != null) {
            String ady = p.getDato();
            if (!visitados.get(ady)) {
                bpf(ady);
            }
            p = p.getSiguiente();
        }
    }

    public String busquedaProfundidad(String inicial) {
        inicial = normalizar(inicial);
        if (!listaAdy.containsKey(inicial)) {
            return "Vértice inicial inexistente";
        }
        for (String nodo : visitados.keySet()) {
            visitados.put(nodo, false);
        }
        recorrido = "";
        bpf(inicial);
        return recorrido;
    }

    public Set<String> obtenerNodos() {
        return listaAdy.keySet();
    }

    public HashMap<String, TLista> getListaAdy() {
        return listaAdy;
    }

    private String normalizar(String texto) {
        if (texto == null) {
            return "";
        }
        return texto.trim();
    }
}