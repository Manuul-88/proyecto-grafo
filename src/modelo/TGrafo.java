//TGrafo
package modelo;

import java.util.*;

public class TGrafo {
    private HashMap<String, TLista> listaAdy;
    private HashMap<String, Boolean> visitados;
    private String recorrido;
    private boolean dirigido;
    private final int infinito = Integer.MAX_VALUE / 4;

    public TGrafo() {
        listaAdy = new HashMap<>();
        visitados = new HashMap<>();
        recorrido = "";
        dirigido = false;
    }

    public void setDirigido(boolean dirigido) {
        this.dirigido = dirigido;
    }

    public boolean isDirigido() {
        return dirigido;
    }

    public boolean agregarNodo(String nombre) {
        nombre = normalizar(nombre);

        if (nombre.isEmpty()) return false;
        if (listaAdy.containsKey(nombre)) return false;

        listaAdy.put(nombre, new TLista());
        visitados.put(nombre, false);

        return true;
    }

    public boolean existeNodo(String nombre) {
        nombre = normalizar(nombre);
        return listaAdy.containsKey(nombre);
    }

    public boolean agregaArista(String origen, String destino) {
        return agregaAristaPeso(origen, destino, 0);
    }

    public boolean agregaAristaPeso(String origen, String destino, int peso) {
        origen = normalizar(origen);
        destino = normalizar(destino);

        if (origen.isEmpty() || destino.isEmpty()) return false;
        if (!listaAdy.containsKey(origen) || !listaAdy.containsKey(destino)) return false;
        if (origen.equalsIgnoreCase(destino)) return false;

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

    public String obtieneListaAdy() {
        StringBuilder cad = new StringBuilder();

        cad.append("Modo ");
        cad.append(dirigido ? "DIRIGIDO" : "NO DIRIGIDO");
        cad.append("\n\nLista de adyacencia\n\n");

        for (Map.Entry<String, TLista> entry : listaAdy.entrySet()) {
            cad.append(entry.getKey())
                    .append(" -> ")
                    .append(entry.getValue().cadenaLista())
                    .append("\n");
        }

        return cad.toString();
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
            return "Nodo de inicio no existe";
        }

        reiniciarVisitados();
        recorrido = "";
        bpf(inicial);

        return recorrido.trim();
    }

    public String busquedaAmplitud(String inicial) {
        inicial = normalizar(inicial);

        if (!listaAdy.containsKey(inicial)) {
            return "Nodo de inicio no existe";
        }

        reiniciarVisitados();

        StringBuilder resultado = new StringBuilder();
        Queue<String> cola = new LinkedList<>();

        visitados.put(inicial, true);
        cola.add(inicial);

        while (!cola.isEmpty()) {
            String actual = cola.poll();
            resultado.append(actual).append(" ");

            TNodo p = listaAdy.get(actual).getCabecera().getSiguiente();

            while (p != null) {
                String vecino = p.getDato();

                if (!visitados.get(vecino)) {
                    visitados.put(vecino, true);
                    cola.add(vecino);
                }

                p = p.getSiguiente();
            }
        }

        return resultado.toString().trim();
    }

    public String dijkstra(String nodoInicial) {
        nodoInicial = normalizar(nodoInicial);

        if (!listaAdy.containsKey(nodoInicial)) {
            return "Nodo de inicio no existe";
        }

        if (!dirigido) {
            return "Para usar Dijkstra activa dirigido";
        }

        ArrayList<String> nodos = new ArrayList<>(listaAdy.keySet());

        int n = nodos.size();
        int inicio = nodos.indexOf(nodoInicial);

        int[] D = new int[n];
        int[] P = new int[n];
        boolean[] S = new boolean[n];

        Arrays.fill(D, infinito);
        Arrays.fill(P, -1);
        Arrays.fill(S, false);

        D[inicio] = 0;

        for (int i = 0; i < n - 1; i++) {
            int w = -1;
            int minimo = infinito;

            for (int j = 0; j < n; j++) {
                if (!S[j] && D[j] < minimo) {
                    minimo = D[j];
                    w = j;
                }
            }

            if (w == -1) break;

            S[w] = true;

            String nodoW = nodos.get(w);
            TNodo p = listaAdy.get(nodoW).getCabecera().getSiguiente();

            while (p != null) {
                int v = nodos.indexOf(p.getDato());
                int peso = p.getPeso();

                if (peso < 0) {
                    return "Dijkstra no acepta pesos negativos";
                }

                if (!S[v] && D[w] + peso < D[v]) {
                    D[v] = D[w] + peso;
                    P[v] = w;
                }

                p = p.getSiguiente();
            }
        }

        StringBuilder salida = new StringBuilder();

        salida.append("Dijkstra desde ").append(nodoInicial).append("\n\n");

        for (int i = 0; i < n; i++) {
            if (i == inicio) continue;

            salida.append("Camino ")
                    .append(nodoInicial)
                    .append(" a ")
                    .append(nodos.get(i))
                    .append(" ");

            if (D[i] >= infinito) {
                salida.append("No hay ruta\n");
            } else {
                Stack<String> pila = new Stack<>();

                int actual = i;

                while (actual != -1) {
                    pila.push(nodos.get(actual));
                    actual = P[actual];
                }

                while (!pila.isEmpty()) {
                    salida.append(pila.pop());

                    if (!pila.isEmpty()) {
                        salida.append(" -> ");
                    }
                }

                salida.append(" | Costo ").append(D[i]).append("\n");
            }
        }

        return salida.toString();
    }

    public String kruskal() {
        if (dirigido) {
            return "Kruskal se usa con grafo no dirigido";
        }

        ArrayList<AristaK> aristas = new ArrayList<>();

        for (String origen : listaAdy.keySet()) {
            TNodo p = listaAdy.get(origen).getCabecera().getSiguiente();

            while (p != null) {
                String destino = p.getDato();

                if (origen.compareTo(destino) < 0) {
                    aristas.add(new AristaK(origen, destino, p.getPeso()));
                }

                p = p.getSiguiente();
            }
        }

        Collections.sort(aristas, Comparator.comparingInt(a -> a.peso));

        HashMap<String, String> padre = new HashMap<>();

        for (String nodo : listaAdy.keySet()) {
            padre.put(nodo, nodo);
        }

        StringBuilder salida = new StringBuilder();

        salida.append("Kruskal arbol minimo\n\n");

        int costoTotal = 0;
        int tomadas = 0;

        for (AristaK a : aristas) {
            String raizO = encontrar(padre, a.origen);
            String raizD = encontrar(padre, a.destino);

            if (!raizO.equals(raizD)) {
                padre.put(raizO, raizD);

                costoTotal += a.peso;
                tomadas++;

                salida.append(a.origen)
                        .append(" - ")
                        .append(a.destino)
                        .append(" | Peso ")
                        .append(a.peso)
                        .append("\n");
            }
        }

        salida.append("\nCosto total ").append(costoTotal).append("\n");

        if (tomadas != Math.max(0, listaAdy.size() - 1)) {
            salida.append("Ojo el grafo quedo separado\n");
        }

        return salida.toString();
    }

    private String encontrar(HashMap<String, String> padre, String nodo) {
        if (!padre.get(nodo).equals(nodo)) {
            padre.put(nodo, encontrar(padre, padre.get(nodo)));
        }

        return padre.get(nodo);
    }

    private static class AristaK {
        String origen;
        String destino;
        int peso;

        AristaK(String origen, String destino, int peso) {
            this.origen = origen;
            this.destino = destino;
            this.peso = peso;
        }
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

    private void reiniciarVisitados() {
        for (String nodo : visitados.keySet()) {
            visitados.put(nodo, false);
        }
    }

    public boolean borrarArco(String origen, String destino) {
        origen = normalizar(origen);
        destino = normalizar(destino);

        if (origen.isEmpty() || destino.isEmpty()) return false;
        if (!listaAdy.containsKey(origen) || !listaAdy.containsKey(destino)) return false;

        boolean eliminado1 = listaAdy.get(origen).eliminar(destino);
        boolean eliminado2 = false;

        if (!dirigido) {
            eliminado2 = listaAdy.get(destino).eliminar(origen);
        }

        return eliminado1 || eliminado2;
    }

    public boolean eliminarNodo(String nombre) {
        nombre = normalizar(nombre);

        if (nombre.isEmpty()) return false;
        if (!listaAdy.containsKey(nombre)) return false;

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

        if (origen.isEmpty() || destino.isEmpty()) return false;
        if (!listaAdy.containsKey(origen) || !listaAdy.containsKey(destino)) return false;

        return listaAdy.get(origen).buscar(destino);
    }

    public boolean editarNodo(String nombreActual, String nombreNuevo) {
        nombreActual = normalizar(nombreActual);
        nombreNuevo = normalizar(nombreNuevo);

        if (nombreActual.isEmpty() || nombreNuevo.isEmpty()) return false;
        if (!listaAdy.containsKey(nombreActual)) return false;
        if (listaAdy.containsKey(nombreNuevo)) return false;

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

        if (!listaAdy.containsKey(origen) || !listaAdy.containsKey(destino)) return false;

        boolean editado1 = listaAdy.get(origen).editarPeso(destino, nuevoPeso);
        boolean editado2 = false;

        if (!dirigido) {
            editado2 = listaAdy.get(destino).editarPeso(origen, nuevoPeso);
        }

        return editado1 || editado2;
    }

    public int contarAristas() {
        int total = 0;

        for (TLista lista : listaAdy.values()) {
            total += lista.contar();
        }

        if (dirigido) {
            return total;
        }

        return total / 2;
    }

    public int contarNodos() {
        return listaAdy.size();
    }

    public int numeroNodos() {
        return contarNodos();
    }

    public int gradoNodo(String nodo) {
        nodo = normalizar(nodo);

        if (!listaAdy.containsKey(nodo)) {
            return -1;
        }

        int salida = listaAdy.get(nodo).contar();

        if (!dirigido) {
            return salida;
        }

        int entrada = 0;

        for (String origen : listaAdy.keySet()) {
            if (!origen.equalsIgnoreCase(nodo) && listaAdy.get(origen).buscar(nodo)) {
                entrada++;
            }
        }

        return entrada + salida;
    }

    public int pesoTotal() {
        int total = 0;

        for (String origen : listaAdy.keySet()) {
            TNodo p = listaAdy.get(origen).getCabecera().getSiguiente();

            while (p != null) {
                String destino = p.getDato();

                if (dirigido || origen.compareTo(destino) < 0) {
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

        reiniciarVisitados();

        return buscarRutaDFS(origen, destino, "").trim();
    }

    public String camino(String origen, String destino) {
        return obtenerRutaDFS(origen, destino);
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
}