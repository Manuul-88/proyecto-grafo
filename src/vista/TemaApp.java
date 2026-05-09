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

        if (nombre.equals("DD")) {
            return new TemaApp(
                    "DD",
                    new Color(22, 0, 0),
                    new Color(42, 5, 5),
                    new Color(10, 0, 0),
                    new Color(255, 230, 230),
                    new Color(255, 70, 70),
                    new Color(100, 15, 15),
                    new Color(190, 20, 20),
                    new Color(25, 0, 0),
                    new Color(255, 230, 230),
                    new Color(180, 20, 20),
                    new Color(255, 60, 60),
                    new Color(255, 40, 40)
            );
        }

        if (nombre.equals("Interestelar")) {
            return new TemaApp(
                    "Interestelar",
                    new Color(3, 6, 18),
                    new Color(9, 14, 32),
                    new Color(2, 4, 14),
                    new Color(235, 245, 255),
                    new Color(130, 190, 255),
                    new Color(25, 55, 100),
                    new Color(55, 115, 190),
                    new Color(8, 14, 30),
                    new Color(235, 245, 255),
                    new Color(70, 130, 255),
                    new Color(180, 100, 255),
                    new Color(120, 190, 255)
            );
        }

        if (nombre.equals("Ego")) {
            return new TemaApp(
                    "Ego",
                    new Color(4, 8, 24),
                    new Color(8, 18, 45),
                    new Color(2, 7, 22),
                    new Color(235, 245, 255),
                    new Color(60, 170, 255),
                    new Color(10, 65, 130),
                    new Color(0, 130, 255),
                    new Color(5, 16, 38),
                    new Color(235, 245, 255),
                    new Color(0, 115, 255),
                    new Color(0, 255, 220),
                    new Color(0, 180, 255)
            );
        }

        if (nombre.equals("Neon")) {
            return new TemaApp(
                    "Neon",
                    new Color(3, 3, 10),
                    new Color(10, 12, 24),
                    new Color(0, 0, 8),
                    new Color(240, 255, 255),
                    new Color(0, 255, 220),
                    new Color(20, 30, 80),
                    new Color(0, 255, 220),
                    new Color(5, 8, 20),
                    new Color(240, 255, 255),
                    new Color(0, 255, 220),
                    new Color(180, 0, 255),
                    new Color(0, 255, 220)
            );
        }

        if (nombre.equals("Cyberpunk")) {
            return new TemaApp(
                    "Cyberpunk",
                    new Color(14, 0, 28),
                    new Color(32, 0, 50),
                    new Color(8, 0, 20),
                    new Color(255, 245, 90),
                    new Color(0, 255, 255),
                    new Color(120, 0, 120),
                    new Color(255, 0, 180),
                    new Color(22, 0, 38),
                    new Color(255, 245, 90),
                    new Color(255, 0, 180),
                    new Color(0, 255, 255),
                    new Color(255, 245, 90)
            );
        }

        return obtenerTema("Halo");
    }
}