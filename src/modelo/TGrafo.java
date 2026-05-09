//Tgrafo

package modelo;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class TGrafo {
    private HashMap<String, TLista> listaAdy;
    private HashMap<String, Boolean> visitados;
    private String recorrido;
    private boolean dirigido;

    public TGrafo() {
        listaAdy = new HashMap<>();
        visitados = new HashMap<>();
        recorrido = "";
        this.dirigido = false; 
    }

    public void setDirigido(boolean dirigido) {
        this.dirigido = dirigido;
    }
         //estoy harta//
    public boolean isDirigido() {
        return dirigido;
    }

    public boolean agregarNodo(String nombre) {
        nombre = normalizar(nombre);
        if (nombre.isEmpty() || listaAdy.containsKey(nombre)) return false;
        listaAdy.put(nombre, new TLista());
        visitados.put(nombre, false);
        return true;
    }

    public boolean agregaAristaPeso(String origen, String destino, int peso) {
        origen = normalizar(origen);
        destino = normalizar(destino);
        if (origen.isEmpty() || destino.isEmpty() || origen.equalsIgnoreCase(destino)) return false;
        if (!listaAdy.containsKey(origen) || !listaAdy.containsKey(destino)) return false;

        boolean agregada = false;
        if (!listaAdy.get(origen).buscar(destino)) {
            listaAdy.get(origen).insertarFinal(destino, peso);
            agregada = true;
        }

        if (!dirigido) {
            if (!listaAdy.get(destino).buscar(origen)) {
                listaAdy.get(destino).insertarFinal(origen, peso);
                agregada = true;
            }
        }
        return agregada;
    }

    public boolean eliminarNodo(String nombre) {
        nombre = normalizar(nombre);
        if (nombre.isEmpty() || !listaAdy.containsKey(nombre)) return false;

        listaAdy.remove(nombre);
        visitados.remove(nombre);

        // CORRECCIÓN AQUÍ: Iteración segura para evitar errores de tipo
        for (Map.Entry<String, TLista> entrada : listaAdy.entrySet()) {
            entrada.getValue().eliminar(nombre);
        }

        return true;
    }

    public boolean borrarArco(String origen, String destino) {
        origen = normalizar(origen);
        destino = normalizar(destino);
        if (!listaAdy.containsKey(origen) || !listaAdy.containsKey(destino)) return false;

        boolean e1 = listaAdy.get(origen).eliminar(destino);
        boolean e2 = false;
        if (!dirigido) {
            e2 = listaAdy.get(destino).eliminar(origen);
        }
        return e1 || e2;
    }

    public String obtieneListaAdy() {
        StringBuilder cad = new StringBuilder("Adyacencia (" + (dirigido ? "Dirigido" : "No Dirigido") + "):\n");
        for (Map.Entry<String, TLista> entry : listaAdy.entrySet()) {
            cad.append(entry.getKey()).append(" -> ").append(entry.getValue().cadenaLista()).append("\n");
        }
        return cad.toString();
    }

    public String busquedaProfundidad(String inicial) {
        inicial = normalizar(inicial);
        if (!listaAdy.containsKey(inicial)) return "Nodo no existe";
        listaAdy.keySet().forEach(n -> visitados.put(n, false));
        recorrido = "";
        bpf(inicial);
        return recorrido;
    }

    private void bpf(String actual) {
        visitados.put(actual, true);
        recorrido += actual + " ";
        TNodo p = listaAdy.get(actual).getCabecera().getSiguiente();
        while (p != null) {
            if (!visitados.get(p.getDato())) bpf(p.getDato());
            p = p.getSiguiente();
        }
    }

    public Set<String> obtenerNodos() { return listaAdy.keySet(); }
    public HashMap<String, TLista> getListaAdy() { return listaAdy; }
    private String normalizar(String t) { return (t == null) ? "" : t.trim(); }
}