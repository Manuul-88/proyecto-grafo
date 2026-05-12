package vista;

import java.awt.Color;

public class TemaApp {
    public final String nombre;
    public final Color fondoVentana, fondoPanel, fondoCanvas;
    public final Color textoPrincipal, textoSecundario;
    public final Color boton, botonHover;
    public final Color campoFondo, campoTexto;
    public final Color nodo1, nodo2, arista;

    private TemaApp(String nombre, Color fondoVentana, Color fondoPanel, Color fondoCanvas,
                    Color textoPrincipal, Color textoSecundario, Color boton, Color botonHover,
                    Color campoFondo, Color campoTexto, Color nodo1, Color nodo2, Color arista) {
        this.nombre = nombre;
        this.fondoVentana = fondoVentana;
        this.fondoPanel = fondoPanel;
        this.fondoCanvas = fondoCanvas;
        this.textoPrincipal = textoPrincipal;
        this.textoSecundario = textoSecundario;
        this.boton = boton;
        this.botonHover = botonHover;
        this.campoFondo = campoFondo;
        this.campoTexto = campoTexto;
        this.nodo1 = nodo1;
        this.nodo2 = nodo2;
        this.arista = arista;
    }

    public static TemaApp obtenerTema(String nombre) {
        if (nombre == null) return obtenerTema("Halo");

        switch (nombre) {
            case "DD":
                return tema("DD", c(22,0,0), c(42,5,5), c(10,0,0), c(255,230,230), c(255,70,70), c(100,15,15), c(190,20,20), c(25,0,0), c(255,230,230), c(180,20,20), c(255,60,60), c(255,40,40));
            case "Interestelar":
                return tema("Interestelar", c(3,6,18), c(9,14,32), c(2,4,14), c(235,245,255), c(130,190,255), c(25,55,100), c(55,115,190), c(8,14,30), c(235,245,255), c(70,130,255), c(180,100,255), c(120,190,255));
            case "Ego":
                return tema("Ego", c(4,8,24), c(8,18,45), c(2,7,22), c(235,245,255), c(60,170,255), c(10,65,130), c(0,130,255), c(5,16,38), c(235,245,255), c(0,115,255), c(0,255,220), c(0,180,255));
            case "GOT":
                return tema("GOT", c(18,18,18), c(32,24,20), c(10,10,10), c(212,175,55), c(120,120,120), c(80,15,15), c(150,30,30), c(25,20,18), c(212,175,55), c(120,20,20), c(180,40,40), c(245,220,140));
            case "Invencible":
                return tema("Invencible", c(0,55,128), c(0,67,150), c(0,132,228), Color.BLACK, c(255,223,0), c(0,150,240), c(255,211,0), c(0,38,88), Color.WHITE, c(255,211,0), c(255,211,0), Color.BLACK);
            default:
                return tema("Halo", c(8,20,18), c(18,40,36), c(5,16,18), c(220,255,235), c(120,220,190), c(45,95,75), c(70,135,100), c(12,30,28), c(220,255,235), c(80,170,120), c(20,120,160), c(120,220,190));
        }
    }

    private static TemaApp tema(String nombre, Color fondoVentana, Color fondoPanel, Color fondoCanvas,
                                Color textoPrincipal, Color textoSecundario, Color boton, Color botonHover,
                                Color campoFondo, Color campoTexto, Color nodo1, Color nodo2, Color arista) {
        return new TemaApp(nombre, fondoVentana, fondoPanel, fondoCanvas, textoPrincipal, textoSecundario,
                boton, botonHover, campoFondo, campoTexto, nodo1, nodo2, arista);
    }

    private static Color c(int r, int g, int b) {
        return new Color(r, g, b);
    }
}
