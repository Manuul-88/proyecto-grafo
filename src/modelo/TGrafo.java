package modelo;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

public class TGrafo {
    private static final int INFINITO = Integer.MAX_VALUE / 4;

    private final LinkedHashMap<String, TLista> listaAdy;
    private boolean dirigido;

    public TGrafo() {
        listaAdy = new LinkedHashMap<>();
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
        if (nombre.isEmpty() || existeNodo(nombre)) return false;

        listaAdy.put(nombre, new TLista());
        return true;
    }

    public boolean existeNodo(String nombre) {
        return listaAdy.containsKey(normalizar(nombre));
    }

    public boolean agregaArista(String origen, String destino) {
        return agregaAristaPeso(origen, destino, 0);
    }

    public boolean agregaAristaPeso(String origen, String destino, int peso) {
        origen = normalizar(origen);
        destino = normalizar(destino);
        
    

        if (!aristaValida(origen, destino)) return false;

        boolean agregada = insertarAristaSimple(origen, destino, peso);

        if (!dirigido) {
            agregada = insertarAristaSimple(destino, origen, peso) || agregada;
        }

        return agregada;
    }

    public boolean borrarArco(String origen, String destino) {
        origen = normalizar(origen);
        destino = normalizar(destino);

        if (!existenAmbos(origen, destino)) return false;

        boolean eliminado = listaAdy.get(origen).eliminar(destino);

        if (!dirigido) {
            eliminado = listaAdy.get(destino).eliminar(origen) || eliminado;
        }

        return eliminado;
    }

    public boolean eliminarNodo(String nombre) {
        nombre = normalizar(nombre);
        if (!existeNodo(nombre)) return false;

        listaAdy.remove(nombre);

        for (TLista lista : listaAdy.values()) {
            lista.eliminar(nombre);
        }

        return true;
    }

    public boolean editarNodo(String actual, String nuevo) {
        actual = normalizar(actual);
        nuevo = normalizar(nuevo);

        if (actual.isEmpty() || nuevo.isEmpty()) return false;
        if (!existeNodo(actual) || existeNodo(nuevo)) return false;

        TLista lista = listaAdy.remove(actual);
        listaAdy.put(nuevo, lista);

        for (TLista l : listaAdy.values()) {
            l.editarDato(actual, nuevo);
        }

        return true;
    }

    public boolean editarPeso(String origen, String destino, int nuevoPeso) {
        origen = normalizar(origen);
        destino = normalizar(destino);

        if (!existenAmbos(origen, destino)) return false;

        boolean editado = listaAdy.get(origen).editarPeso(destino, nuevoPeso);

        if (!dirigido) {
            editado = listaAdy.get(destino).editarPeso(origen, nuevoPeso) || editado;
        }

        return editado;
    }

    public boolean adyacente(String origen, String destino) {
        origen = normalizar(origen);
        destino = normalizar(destino);

        return existenAmbos(origen, destino) && listaAdy.get(origen).buscar(destino);
    }

    public String obtieneListaAdy() {
        StringBuilder sb = new StringBuilder();
        sb.append("Modo ").append(dirigido ? "DIRIGIDO" : "NO DIRIGIDO").append("\n\n");
        sb.append("Lista de adyacencia\n\n");

        for (Map.Entry<String, TLista> entry : listaAdy.entrySet()) {
            sb.append(entry.getKey()).append(" -> ").append(entry.getValue().cadenaLista()).append("\n");
        }

        return sb.toString();
    }

    public String busquedaProfundidad(String inicial) {
        inicial = normalizar(inicial);
        if (!existeNodo(inicial)) return "Nodo de inicio no existe";

        ArrayList<String> recorrido = new ArrayList<>();
        HashMap<String, Boolean> visitados = crearMapaVisitados();

        dfs(inicial, visitados, recorrido);
        return String.join(" ", recorrido);
    }

    public String busquedaAmplitud(String inicial) {
        inicial = normalizar(inicial);
        if (!existeNodo(inicial)) return "Nodo de inicio no existe";

        HashMap<String, Boolean> visitados = crearMapaVisitados();
        Queue<String> cola = new ArrayDeque<>();
        ArrayList<String> recorrido = new ArrayList<>();

        visitados.put(inicial, true);
        cola.add(inicial);

        while (!cola.isEmpty()) {
            String actual = cola.poll();
            recorrido.add(actual);

            TNodo vecino = listaAdy.get(actual).getCabecera().getSiguiente();
            while (vecino != null) {
                String dato = vecino.getDato();

                if (!visitados.get(dato)) {
                    visitados.put(dato, true);
                    cola.add(dato);
                }

                vecino = vecino.getSiguiente();
            }
        }

        return String.join(" ", recorrido);
    }

    public String obtenerRutaDFS(String origen, String destino) {
        origen = normalizar(origen);
        destino = normalizar(destino);

        if (!existenAmbos(origen, destino)) return "";

        HashMap<String, Boolean> visitados = crearMapaVisitados();
        ArrayList<String> ruta = new ArrayList<>();

        return buscarRuta(origen, destino, visitados, ruta) ? String.join(" ", ruta) : "";
    }

    public String camino(String origen, String destino) {
        return obtenerRutaDFS(origen, destino);
    }

    public String dijkstra(String inicial) {
        inicial = normalizar(inicial);
        if (!existeNodo(inicial)) return "Nodo de inicio no existe";

        ArrayList<String> nodos = new ArrayList<>(listaAdy.keySet());
        int n = nodos.size();
        int inicio = nodos.indexOf(inicial);

        int[] distancia = new int[n];
        int[] padre = new int[n];
        boolean[] fijo = new boolean[n];

        Arrays.fill(distancia, INFINITO);
        Arrays.fill(padre, -1);
        distancia[inicio] = 0;

        for (int i = 0; i < n - 1; i++) {
            int actual = obtenerMenorNoFijo(distancia, fijo);
            if (actual == -1) break;

            fijo[actual] = true;
            String nodoActual = nodos.get(actual);
            TNodo vecino = listaAdy.get(nodoActual).getCabecera().getSiguiente();

            while (vecino != null) {
                if (vecino.getPeso() < 0) return "Dijkstra no acepta pesos negativos";

                int indiceVecino = nodos.indexOf(vecino.getDato());
                int nuevaDistancia = distancia[actual] + vecino.getPeso();

                if (!fijo[indiceVecino] && nuevaDistancia < distancia[indiceVecino]) {
                    distancia[indiceVecino] = nuevaDistancia;
                    padre[indiceVecino] = actual;
                }

                vecino = vecino.getSiguiente();
            }
        }

        return formatearDijkstra(inicial, nodos, distancia, padre, inicio);
    }
    public String kruskal() {
        if (dirigido) return "Kruskal se usa con grafo no dirigido";
        ArrayList<Arista> aristas = obtenerAristasUnicas();
        Collections.sort(aristas, Comparator.comparingInt(a -> a.peso));
        HashMap<String, String> padre = new HashMap<>();
        for (String nodo : listaAdy.keySet()) padre.put(nodo, nodo);
        StringBuilder sb = new StringBuilder("Kruskal arbol minimo\n\n");
        int costoTotal = 0;
        int tomadas = 0;
        for (Arista a : aristas) {
            String raizOrigen = encontrar(padre, a.origen);
            String raizDestino = encontrar(padre, a.destino);
            if (!raizOrigen.equals(raizDestino)) {
                padre.put(raizOrigen, raizDestino);
                costoTotal += a.peso;
                tomadas++;
                sb.append(a.origen).append(" - ").append(a.destino).append(" | Peso ").append(a.peso).append("\n");
            }
        }
        sb.append("\nCosto total ").append(costoTotal).append("\n");
        if (tomadas != Math.max(0, listaAdy.size() - 1)) {
            sb.append("Ojo: el grafo quedo separado\n");
        }
        return sb.toString();
    }
    public int contarNodos() {
        return listaAdy.size();
    }
    public int numeroNodos() {
        return contarNodos();
    }
    public int contarAristas() {
        int total = 0;
        for (TLista lista : listaAdy.values()) total += lista.contar();
        return dirigido ? total : total / 2;
    }
    public int gradoNodo(String nodo) {
        nodo = normalizar(nodo);
        if (!existeNodo(nodo)) return -1;
        int salida = listaAdy.get(nodo).contar();
        if (!dirigido) return salida;
        int entrada = 0;
        for (String origen : listaAdy.keySet()) {
            if (!origen.equalsIgnoreCase(nodo) && listaAdy.get(origen).buscar(nodo)) entrada++;
        }
        return entrada + salida;
    }
    public int pesoTotal() {
        int total = 0;
        for (String origen : listaAdy.keySet()) {
            TNodo actual = listaAdy.get(origen).getCabecera().getSiguiente();
            while (actual != null) {
                String destino = actual.getDato();
                if (dirigido || origen.compareTo(destino) < 0) {
                    total += actual.getPeso();
                }
                actual = actual.getSiguiente();
            }
        }
        return total;
    }
    public Set<String> obtenerNodos() {
        return listaAdy.keySet();
    }
    public LinkedHashMap<String, TLista> getListaAdy() {
        return listaAdy;
    }
    private boolean insertarAristaSimple(String origen, String destino, int peso) {
        if (listaAdy.get(origen).buscar(destino)) return false;
        listaAdy.get(origen).insertarFinal(destino, peso);
        return true;
    }
    private boolean aristaValida(String origen, String destino) {
        return !origen.isEmpty()
                && !destino.isEmpty()
                && existenAmbos(origen, destino)
                && !origen.equalsIgnoreCase(destino);
    }
    private boolean existenAmbos(String origen, String destino) {
        return existeNodo(origen) && existeNodo(destino);
    }
    private HashMap<String, Boolean> crearMapaVisitados() {
        HashMap<String, Boolean> visitados = new HashMap<>();
        for (String nodo : listaAdy.keySet()) visitados.put(nodo, false);
        return visitados;
    }
    private void dfs(String actual, HashMap<String, Boolean> visitados, ArrayList<String> recorrido) {
        visitados.put(actual, true);
        recorrido.add(actual);
        TNodo vecino = listaAdy.get(actual).getCabecera().getSiguiente();
        while (vecino != null) {
            String dato = vecino.getDato();
            if (!visitados.get(dato)) dfs(dato, visitados, recorrido);
            vecino = vecino.getSiguiente();
        }
    }
    private boolean buscarRuta(String actual, String destino, HashMap<String, Boolean> visitados, ArrayList<String> ruta) {
        visitados.put(actual, true);
        ruta.add(actual);
        if (actual.equalsIgnoreCase(destino)) return true;
        TNodo vecino = listaAdy.get(actual).getCabecera().getSiguiente();
        while (vecino != null) {
            String dato = vecino.getDato();

            if (!visitados.get(dato) && buscarRuta(dato, destino, visitados, ruta)) {
                return true;
            }
            vecino = vecino.getSiguiente();
        }
        ruta.remove(ruta.size() - 1);
        return false;
    }
    private int obtenerMenorNoFijo(int[] distancia, boolean[] fijo) {
        int menor = INFINITO;
        int indice = -1;
        for (int i = 0; i < distancia.length; i++) {
            if (!fijo[i] && distancia[i] < menor) {
                menor = distancia[i];
                indice = i;
            }
        }
        return indice;
    }
    private String formatearDijkstra(String inicial, ArrayList<String> nodos, int[] distancia, int[] padre, int inicio) {
        StringBuilder sb = new StringBuilder("Dijkstra desde " + inicial + "\n\n");
        for (int i = 0; i < nodos.size(); i++) {
            if (i == inicio) continue;
            sb.append("Camino ").append(inicial).append(" a ").append(nodos.get(i)).append(" ");
            if (distancia[i] >= INFINITO) {
                sb.append("No hay ruta\n");
                continue;
            }
            Stack<String> camino = new Stack<>();
            int actual = i;
            while (actual != -1) {
                camino.push(nodos.get(actual));
                actual = padre[actual];
            }
            while (!camino.isEmpty()) {
                sb.append(camino.pop());
                if (!camino.isEmpty()) sb.append(" -> ");
            }

            sb.append(" | Costo ").append(distancia[i]).append("\n");
        }

        return sb.toString();
    }
    private ArrayList<Arista> obtenerAristasUnicas() {
        ArrayList<Arista> aristas = new ArrayList<>();
        for (String origen : listaAdy.keySet()) {
            TNodo actual = listaAdy.get(origen).getCabecera().getSiguiente();

            while (actual != null) {
                String destino = actual.getDato();
                if (origen.compareTo(destino) < 0) {
                    aristas.add(new Arista(origen, destino, actual.getPeso()));
                }
                actual = actual.getSiguiente();
            }
        }
        return aristas;
    }
    private String encontrar(HashMap<String, String> padre, String nodo) {
        if (!padre.get(nodo).equals(nodo)) {
            padre.put(nodo, encontrar(padre, padre.get(nodo)));
        }
        return padre.get(nodo);
    }
    private String normalizar(String texto) {
        return texto == null ? "" : texto.trim();
    }
    private static class Arista {
        String origen;
        String destino;
        int peso;
        Arista(String origen, String destino, int peso) {
            this.origen = origen;
            this.destino = destino;
            this.peso = peso;
        }
    }
}