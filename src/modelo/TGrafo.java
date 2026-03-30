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
        
        for (int i=0; i<n; i++){
        listaAdy[i] = new TLista();
        visitados[i] = false;
        }
    }
    
    public void agregaArista(int origen, int destino){
        if(origen <1 || origen>n || destino<1 ||destino>n){
            System.out.println("Vertice fuera de rango");
            return;
        }
        if(!listaAdy[origen-1].buscar(destino))
            listaAdy[origen-1].insertarFinal(destino);
        if(!listaAdy[destino-1].buscar(origen))
            listaAdy[destino-1].insertarFinal(origen);
    }
    
    public void muestraListaAdy(){
        System.out.println(obtieneListaAdy());
    }

    public String obtieneListaAdy(){
        String cad = "\nListas de adyacencia:\n";
        for(int i=0; i<n; i++){
            cad += (i+1) + " -> " + listaAdy[i].cadenaLista() + "\n";
        }
        return cad;
    }
    
    private void bpf(int inicial){
        visitados[inicial] = true;
        System.out.printf("%d\t", inicial + 1);
        
        TNodo p = listaAdy[inicial].getCabecera().getSiguiente();
        
        while(p!=null){
            int ady = p.getDato()-1;
            if (!visitados[ady])
                bpf(ady);
            p = p.getSiguiente();
        }
    }
    
    public void busquedaProfundidad(int inicial){
        if(inicial<1||inicial>n){
            System.out.println("Vertice inicial fuera de rango");
            return;
        }
        for(int i=0; i<n; i++)
            visitados[i] = false;
        recorrido="";
        bpf(inicial - 1);
        System.out.println();
    }
}