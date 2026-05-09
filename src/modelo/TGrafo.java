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
    
    public boolean agregaAristaPeso(String origen, String destino, int peso) {
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
        listaAdy.get(origen).insertarFinal(destino, peso);
        agregada = true;
    }
    if (!listaAdy.get(destino).buscar(origen)) {
        listaAdy.get(destino).insertarFinal(origen, peso);
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
    
    public boolean borrarArco(String origen, String destino) {
    origen = normalizar(origen);
    destino = normalizar(destino);

    if (origen.isEmpty() || destino.isEmpty()) {
        return false;
    }

    if (!listaAdy.containsKey(origen) || !listaAdy.containsKey(destino)) {
        return false;
    }

    boolean eliminado1 = listaAdy.get(origen).eliminar(destino);
    boolean eliminado2 = listaAdy.get(destino).eliminar(origen);

    return eliminado1 || eliminado2;
}
    
    public boolean eliminarNodo(String nombre) {
    nombre = normalizar(nombre);

    if (nombre.isEmpty()) {
        return false;
    }

    if (!listaAdy.containsKey(nombre)) {
        return false;
    }

    listaAdy.remove(nombre);
    visitados.remove(nombre);

    for (TLista lista : listaAdy.values()) {
        lista.eliminar(nombre);
    }

    return true;
}
    
    public boolean adyacente(String origen, String destino) {

    origen = normalizar(origen);
    destino = normalizar(destino);

    if (origen.isEmpty() || destino.isEmpty()) {
        return false;
    }

    if (!listaAdy.containsKey(origen) ||
        !listaAdy.containsKey(destino)) {

        return false;
    }

    return listaAdy.get(origen).buscar(destino);
}
    
    public boolean editarNodo(String nombreActual, String nombreNuevo) {
    nombreActual = normalizar(nombreActual);
    nombreNuevo = normalizar(nombreNuevo);

    if (nombreActual.isEmpty() || nombreNuevo.isEmpty()) {
        return false;
    }

    if (!listaAdy.containsKey(nombreActual)) {
        return false;
    }

    if (listaAdy.containsKey(nombreNuevo)) {
        return false;
    }

    TLista lista = listaAdy.remove(nombreActual);
    listaAdy.put(nombreNuevo, lista);

    Boolean visitado = visitados.remove(nombreActual);
    visitados.put(nombreNuevo, visitado != null ? visitado : false);

    for (TLista l : listaAdy.values()) {
        l.editarDato(nombreActual, nombreNuevo);
    }

    return true;
}

public boolean editarPeso(String origen, String destino, int nuevoPeso) {
    origen = normalizar(origen);
    destino = normalizar(destino);

    if (!listaAdy.containsKey(origen) || !listaAdy.containsKey(destino)) {
        return false;
    }

    boolean editado1 = listaAdy.get(origen).editarPeso(destino, nuevoPeso);
    boolean editado2 = listaAdy.get(destino).editarPeso(origen, nuevoPeso);

    return editado1 || editado2;
}

public int contarAristas() {
    int total = 0;

    for (TLista lista : listaAdy.values()) {
        total += lista.contar();
    }

    return total / 2;
}

public int contarNodos() {
    return listaAdy.size();
}

public int pesoTotal() {
    int total = 0;

    for (String origen : listaAdy.keySet()) {
        TNodo p = listaAdy.get(origen).getCabecera().getSiguiente();

        while (p != null) {
            String destino = p.getDato();

            if (origen.compareTo(destino) < 0) {
                total += p.getPeso();
            }

            p = p.getSiguiente();
        }
    }

    return total;
}

public String obtenerRutaDFS(String origen, String destino) {
    origen = normalizar(origen);
    destino = normalizar(destino);

    if (!listaAdy.containsKey(origen) || !listaAdy.containsKey(destino)) {
        return "";
    }

    for (String nodo : visitados.keySet()) {
        visitados.put(nodo, false);
    }

    return buscarRutaDFS(origen, destino, "");
}

private String buscarRutaDFS(String actual, String destino, String ruta) {
    visitados.put(actual, true);
    ruta += actual + " ";

    if (actual.equalsIgnoreCase(destino)) {
        return ruta;
    }

    TNodo p = listaAdy.get(actual).getCabecera().getSiguiente();

    while (p != null) {
        String vecino = p.getDato();

        if (!visitados.get(vecino)) {
            String resultado = buscarRutaDFS(vecino, destino, ruta);

            if (!resultado.isEmpty()) {
                return resultado;
            }
        }

        p = p.getSiguiente();
    }

    return "";
}
    
}//fin