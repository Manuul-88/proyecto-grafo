package vista;

import java.awt.Color;

public class TemaApp {

    public String nombre;

    public Color fondoVentana;
    public Color fondoPanel;
    public Color fondoCanvas;

    public Color textoPrincipal;
    public Color textoSecundario;

    public Color boton;
    public Color botonHover;

    public Color campoFondo;
    public Color campoTexto;

    public Color nodo1;
    public Color nodo2;
    public Color arista;

    public TemaApp(
            String nombre,
            Color fondoVentana,
            Color fondoPanel,
            Color fondoCanvas,
            Color textoPrincipal,
            Color textoSecundario,
            Color boton,
            Color botonHover,
            Color campoFondo,
            Color campoTexto,
            Color nodo1,
            Color nodo2,
            Color arista
    ) {
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
        if (nombre.equals("Neon")) {
            return new TemaApp(
                    "Neon",
                    new Color(8, 8, 14),
                    new Color(15, 18, 28),
                    new Color(5, 5, 12),
                    new Color(230, 255, 255),
                    new Color(0, 255, 220),
                    new Color(20, 90, 100),
                    new Color(0, 180, 170),
                    new Color(12, 16, 25),
                    new Color(230, 255, 255),
                    new Color(0, 255, 220),
                    new Color(120, 80, 255),
                    new Color(0, 255, 220)
            );
        }

        if (nombre.equals("Clasico")) {
            return new TemaApp(
                    "Clasico",
                    new Color(235, 238, 242),
                    new Color(245, 247, 250),
                    Color.WHITE,
                    Color.BLACK,
                    Color.DARK_GRAY,
                    new Color(210, 225, 240),
                    new Color(185, 210, 235),
                    Color.WHITE,
                    Color.BLACK,
                    new Color(195, 215, 235),
                    new Color(220, 235, 250),
                    Color.BLACK
            );
        }

        if (nombre.equals("Sakura")) {
            return new TemaApp(
                    "Sakura",
                    new Color(255, 238, 245),
                    new Color(255, 225, 238),
                    new Color(255, 245, 250),
                    new Color(80, 30, 55),
                    new Color(160, 70, 110),
                    new Color(255, 170, 205),
                    new Color(255, 120, 170),
                    Color.WHITE,
                    new Color(80, 30, 55),
                    new Color(255, 185, 215),
                    new Color(255, 120, 175),
                    new Color(210, 90, 140)
            );
        }

        if (nombre.equals("Cyberpunk")) {
            return new TemaApp(
                    "Cyberpunk",
                    new Color(18, 0, 30),
                    new Color(35, 0, 55),
                    new Color(10, 0, 22),
                    new Color(255, 240, 80),
                    new Color(0, 255, 255),
                    new Color(180, 0, 140),
                    new Color(0, 200, 255),
                    new Color(25, 0, 40),
                    new Color(255, 240, 80),
                    new Color(255, 0, 180),
                    new Color(0, 255, 255),
                    new Color(255, 240, 80)
            );
        }

        if (nombre.equals("Halo")) {
            return new TemaApp(
                    "Halo",
                    new Color(8, 20, 18),
                    new Color(18, 40, 36),
                    new Color(5, 16, 18),
                    new Color(220, 255, 235),
                    new Color(120, 220, 190),
                    new Color(45, 95, 75),
                    new Color(70, 135, 100),
                    new Color(12, 30, 28),
                    new Color(220, 255, 235),
                    new Color(80, 170, 120),
                    new Color(20, 120, 160),
                    new Color(120, 220, 190)
            );
        }

        return new TemaApp(
                "Espacio",
                new Color(10, 14, 28),
                new Color(16, 22, 36),
                new Color(10, 14, 28),
                Color.WHITE,
                new Color(190, 205, 235),
                new Color(60, 80, 120),
                new Color(90, 120, 180),
                new Color(20, 28, 45),
                Color.WHITE,
                new Color(75, 130, 255),
                new Color(170, 90, 255),
                new Color(120, 180, 255)
        );
    }
}