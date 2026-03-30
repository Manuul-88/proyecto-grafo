package modelo;

public class TLista {
    private TNodo cab, fin;
    
    public TLista(){
        cab = new TNodo();
        fin = null;
    }
    
    public boolean esVacia(){
        return cab.getSiguiente() == null;
    }
    
    public TNodo getCabecera(){
        return cab;
    }
    
    public void insertarInicio(int dato){
        TNodo p = new TNodo(dato);
        if(cab.getSiguiente() == null)
            fin = p;
        p.setSiguiente(cab.getSiguiente());
        cab.setSiguiente(p);
    }
    
    public void insertarFinal(int dato){
        TNodo p = new TNodo(dato);
        
        if(fin == null)
            cab.setSiguiente(p);
        else
            fin.setSiguiente(p);
        
        fin = p;
    }
    
    public boolean buscar(int dato){
        TNodo p = cab.getSiguiente();
        
        while (p!=null){
            if (p.getDato()==dato)
                return true;
            p= p.getSiguiente();
        }
        return false;
    }
    
    public void muestraLista(){
        TNodo p=cab.getSiguiente();
        while (p!=null){
            System.out.println(p.getDato());
            if (p.getSiguiente()!=null)
                System.out.print(" -> ");
            p=p.getSiguiente();
        }
        System.out.println();
    }
    
    public String cadenaLista() {
        String cad = "";
        TNodo p = cab.getSiguiente();

        while (p != null) {
            cad += p.getDato();
            if (p.getSiguiente() != null)
                cad += " -> ";
            p = p.getSiguiente();
        }

        return cad;
    }
    
}