package modelo;

public class TGrafo {
    private TLista listaAdy[];
    private int n;
    private boolean visitados[];
    private String recorrido;
    
    public TGrafo(int n){
        this.n = n;
        listaAdy = new TLista[n];
        visitados = new boolean[n];
        recorrido = "";
        
        for (int i = 0; i < n; i++){
            listaAdy[i] = new TLista();
            visitados[i] = false;
        }
    }
    
    public boolean agregaArista(int origen, int destino){
        if(origen < 1 || origen > n || destino < 1 || destino > n){
            return false;
        }
        
        boolean agregada = false;
        
        if(!listaAdy[origen - 1].buscar(destino)){
            listaAdy[origen - 1].insertarFinal(destino);
            agregada = true;
        }
        
        if(!listaAdy[destino - 1].buscar(origen)){
            listaAdy[destino - 1].insertarFinal(origen);
            agregada = true;
        }
        
        return agregada;
    }
    
    public String obtieneListaAdy(){
        String cad = "\nListas de adyacencia:\n";
        
        for(int i = 0; i < n; i++){
            cad += (i + 1) + " -> " + listaAdy[i].cadenaLista() + "\n";
        }
        
        return cad;
    }

    private void bpf(int inicial){
        visitados[inicial] = true;
        recorrido += (inicial + 1) + " ";
        
        TNodo p = listaAdy[inicial].getCabecera().getSiguiente();
        
        while(p != null){
            int ady = p.getDato() - 1;
            
            if(!visitados[ady]){
                bpf(ady);
            }
            
            p = p.getSiguiente();
        }
    }

    public String busquedaProfundidad(int inicial){
        if(inicial < 1 || inicial > n){
            return "Vertice inicial fuera de rango";
        }
        
        for(int i = 0; i < n; i++){
            visitados[i] = false;
        }
        
        recorrido = "";
        bpf(inicial - 1);
        
        return recorrido;
    }
}