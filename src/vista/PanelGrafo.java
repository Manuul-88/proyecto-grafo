package vista;

import modelo.TGrafo;
import modelo.TLista;
import modelo.TNodo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;

public class PanelGrafo extends JPanel {

    private TGrafo grafo;

    private HashMap<String, Point2D.Double> posiciones;
    private HashMap<String, Point2D.Double> velocidades;

    private boolean modoMover = false;
    private boolean fisicaActiva = true;

    private String nodoSeleccionado = null;

    private TemaApp tema;

    private final int RADIO = 58;
    private Timer timer;

    public PanelGrafo(TGrafo grafo) {
        this.grafo = grafo;
        this.posiciones = new HashMap<>();
        this.velocidades = new HashMap<>();
        this.tema = TemaApp.obtenerTema("Espacio");

        setBackground(tema.fondoCanvas);
        setBorder(BorderFactory.createLineBorder(new Color(50, 60, 90), 2));

        configurarMouse();

        timer = new Timer(16, e -> {
            if (fisicaActiva) {
                actualizarFisica();
            }
            repaint();
        });

        timer.start();
    }

    public void setModoMover(boolean modoMover) {
        this.modoMover = modoMover;
    }

    public void setFisicaActiva(boolean fisicaActiva) {
        this.fisicaActiva = fisicaActiva;
    }

    public void setTemaApp(TemaApp tema) {
        this.tema = tema;
        setBackground(tema.fondoCanvas);
        setBorder(BorderFactory.createLineBorder(tema.botonHover, 2));
        repaint();
    }

    public void agregarNodoVisual(String nombre) {
        if (posiciones.containsKey(nombre)) {
            return;
        }

        int cantidad = posiciones.size();

        double centroX = Math.max(getWidth() / 2.0, 250);
        double centroY = Math.max(getHeight() / 2.0, 250);

        double angulo = cantidad * 0.8;
        double distancia = 120 + cantidad * 8;

        double x = centroX + Math.cos(angulo) * distancia;
        double y = centroY + Math.sin(angulo) * distancia;

        posiciones.put(nombre, new Point2D.Double(x, y));
        velocidades.put(nombre, new Point2D.Double(0, 0));

        repaint();
    }

    private void configurarMouse() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (!modoMover) return;

                nodoSeleccionado = obtenerNodoEn(e.getX(), e.getY());

                if (nodoSeleccionado != null) {
                    velocidades.get(nodoSeleccionado).x = 0;
                    velocidades.get(nodoSeleccionado).y = 0;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                nodoSeleccionado = null;
            }
        });

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (!modoMover || nodoSeleccionado == null) return;

                Point2D.Double p = posiciones.get(nodoSeleccionado);
                p.x = e.getX();
                p.y = e.getY();

                mantenerDentroDelCanvas(p);
                repaint();
            }
        });
    }

    private String obtenerNodoEn(int mouseX, int mouseY) {
        for (Map.Entry<String, Point2D.Double> entry : posiciones.entrySet()) {
            Point2D.Double p = entry.getValue();

            double dx = mouseX - p.x;
            double dy = mouseY - p.y;
            double distancia = Math.sqrt(dx * dx + dy * dy);

            if (distancia <= RADIO / 2.0) {
                return entry.getKey();
            }
        }

        return null;
    }

    private void actualizarFisica() {
        double repulsion = 8500;
        double atraccion = 0.008;
        double distanciaIdeal = 160;
        double friccion = 0.86;

        for (String a : posiciones.keySet()) {
            if (a.equals(nodoSeleccionado)) continue;

            Point2D.Double pa = posiciones.get(a);
            Point2D.Double va = velocidades.get(a);

            for (String b : posiciones.keySet()) {
                if (a.equals(b)) continue;

                Point2D.Double pb = posiciones.get(b);

                double dx = pa.x - pb.x;
                double dy = pa.y - pb.y;
                double distancia = Math.sqrt(dx * dx + dy * dy);

                if (distancia < 1) distancia = 1;

                double fuerza = repulsion / (distancia * distancia);

                va.x += (dx / distancia) * fuerza;
                va.y += (dy / distancia) * fuerza;
            }
        }

        aplicarAtraccionAristas(atraccion, distanciaIdeal);

        for (String nodo : posiciones.keySet()) {
            if (nodo.equals(nodoSeleccionado)) continue;

            Point2D.Double p = posiciones.get(nodo);
            Point2D.Double v = velocidades.get(nodo);

            v.x *= friccion;
            v.y *= friccion;

            p.x += v.x;
            p.y += v.y;

            mantenerDentroDelCanvas(p);
        }
    }

    private void aplicarAtraccionAristas(double atraccion, double distanciaIdeal) {
        for (Map.Entry<String, TLista> entry : grafo.getListaAdy().entrySet()) {
            String origen = entry.getKey();
            Point2D.Double p1 = posiciones.get(origen);

            if (p1 == null) continue;

            TNodo aux = entry.getValue().getCabecera().getSiguiente();

            while (aux != null) {
                String destino = aux.getDato();
                Point2D.Double p2 = posiciones.get(destino);

                if (p2 != null && origen.compareTo(destino) < 0) {
                    Point2D.Double v1 = velocidades.get(origen);
                    Point2D.Double v2 = velocidades.get(destino);

                    double dx = p2.x - p1.x;
                    double dy = p2.y - p1.y;
                    double distancia = Math.sqrt(dx * dx + dy * dy);

                    if (distancia < 1) distancia = 1;

                    double diferencia = distancia - distanciaIdeal;
                    double fuerza = diferencia * atraccion;

                    double fx = (dx / distancia) * fuerza;
                    double fy = (dy / distancia) * fuerza;

                    if (!origen.equals(nodoSeleccionado)) {
                        v1.x += fx;
                        v1.y += fy;
                    }

                    if (!destino.equals(nodoSeleccionado)) {
                        v2.x -= fx;
                        v2.y -= fy;
                    }
                }

                aux = aux.getSiguiente();
            }
        }
    }

    private void mantenerDentroDelCanvas(Point2D.Double p) {
        int margen = RADIO;

        if (p.x < margen) p.x = margen;
        if (p.y < margen) p.y = margen;

        if (p.x > getWidth() - margen) p.x = getWidth() - margen;
        if (p.y > getHeight() - margen) p.y = getHeight() - margen;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        dibujarFondo(g2);
        dibujarAristas(g2);
        dibujarNodos(g2);
        dibujarIndicadorModo(g2);
    }

    private void dibujarFondo(Graphics2D g2) {
        g2.setColor(tema.fondoCanvas);
        g2.fillRect(0, 0, getWidth(), getHeight());

        if (tema.nombre.equals("Espacio")) {
            g2.setColor(new Color(70, 90, 140, 90));
            for (int i = 0; i < 90; i++) {
                int x = (i * 97) % Math.max(getWidth(), 1);
                int y = (i * 53) % Math.max(getHeight(), 1);
                g2.fillOval(x, y, 2, 2);
            }
        }

        if (tema.nombre.equals("Sakura")) {
            g2.setColor(new Color(255, 170, 200, 100));
            for (int i = 0; i < 50; i++) {
                int x = (i * 83) % Math.max(getWidth(), 1);
                int y = (i * 47) % Math.max(getHeight(), 1);
                g2.fillOval(x, y, 7, 4);
            }
        }

        if (tema.nombre.equals("Cyberpunk")) {
            g2.setColor(new Color(255, 0, 180, 60));
            for (int y = 0; y < getHeight(); y += 45) {
                g2.drawLine(0, y, getWidth(), y);
            }

            g2.setColor(new Color(0, 255, 255, 45));
            for (int x = 0; x < getWidth(); x += 45) {
                g2.drawLine(x, 0, x, getHeight());
            }
        }

        if (tema.nombre.equals("Halo")) {
            g2.setColor(new Color(120, 220, 190, 40));
            for (int i = 0; i < 8; i++) {
                int size = 180 + i * 80;
                g2.drawOval(getWidth() / 2 - size / 2, getHeight() / 2 - size / 2, size, size);
            }
        }
    }

    
    private void dibujarPesoCentro(Graphics2D g2, double x, double y, int peso) {

    String texto = String.valueOf(peso);

    g2.setFont(new Font("Arial", Font.BOLD, 16));

    FontMetrics fm = g2.getFontMetrics();

    int ancho = fm.stringWidth(texto);
    int alto = fm.getAscent();

    // sombra
    g2.setColor(new Color(0,0,0,180));

    g2.drawString(
            texto,
            (int)(x - ancho / 2 + 2),
            (int)(y + alto / 2 + 2)
    );

    // color
    if (peso == 0) {
        g2.setColor(Color.WHITE);
    }
    else if (peso <= 3) {
        g2.setColor(new Color(0, 255, 100));
    }
    else if (peso <= 7) {
        g2.setColor(new Color(255, 230, 0));
    }
    else {
        g2.setColor(new Color(255, 60, 60));
    }

    // texto
    g2.drawString(
            texto,
            (int)(x - ancho / 2),
            (int)(y + alto / 2)
    );
}

    private void dibujarAristas(Graphics2D g2) {

    for (Map.Entry<String, TLista> entry : grafo.getListaAdy().entrySet()) {

        String origen = entry.getKey();
        Point2D.Double p1 = posiciones.get(origen);

        if (p1 == null) continue;

        TNodo aux = entry.getValue().getCabecera().getSiguiente();

        while (aux != null) {

            String destino = aux.getDato();
            int peso = aux.getPeso();

            Point2D.Double p2 = posiciones.get(destino);

            if (p2 != null && origen.compareTo(destino) < 0) {

                // COLOR
                if (peso == 0) {
                    g2.setColor(Color.WHITE);
                }
                else if (peso <= 3) {
                    g2.setColor(new Color(0, 255, 100));
                }
                else if (peso <= 7) {
                    g2.setColor(new Color(255, 230, 0));
                }
                else {
                    g2.setColor(new Color(255, 60, 60));
                }

                // GROSOR
                float grosor = 2.0f + Math.min(peso, 10) * 0.35f;

                g2.setStroke(new BasicStroke(
                        grosor,
                        BasicStroke.CAP_ROUND,
                        BasicStroke.JOIN_ROUND
                ));

                // CENTRO
                double mx = (p1.x + p2.x) / 2.0;
                double my = (p1.y + p2.y) / 2.0;

                // DIRECCIÓN
                double dx = p2.x - p1.x;
                double dy = p2.y - p1.y;

                double len = Math.sqrt(dx * dx + dy * dy);

                if (len == 0) continue;

                // NORMALIZAR
                double ux = dx / len;
                double uy = dy / len;

                // TAMAÑO DEL HUECO
                double gap = 22;

                // PUNTOS DEL HUECO
                double gx1 = mx - ux * gap;
                double gy1 = my - uy * gap;

                double gx2 = mx + ux * gap;
                double gy2 = my + uy * gap;

                // LINEA 1
                g2.drawLine(
                        (int) p1.x,
                        (int) p1.y,
                        (int) gx1,
                        (int) gy1
                );

                // LINEA 2
                g2.drawLine(
                        (int) gx2,
                        (int) gy2,
                        (int) p2.x,
                        (int) p2.y
                );

                // DIBUJAR PESO
                dibujarPesoCentro(g2, mx, my, peso);
            }

            aux = aux.getSiguiente();
        }
    }
}
    
    private void dibujarPesoArista(Graphics2D g2, Point2D.Double p1, Point2D.Double p2, int peso) {
    if (peso <= 0) {
        return;
    }

    int xMedio = (int) ((p1.x + p2.x) / 2);
    int yMedio = (int) ((p1.y + p2.y) / 2);

    String texto = String.valueOf(peso);

    g2.setFont(new Font("Arial", Font.BOLD, 16));
    FontMetrics fm = g2.getFontMetrics();

    int ancho = fm.stringWidth(texto);

    g2.setColor(Color.BLACK);
    g2.drawString(texto, xMedio - ancho / 2 + 2, yMedio + 7 + 2);

    if (peso <= 3) {
        g2.setColor(new Color(0, 255, 100));
    } else if (peso <= 7) {
        g2.setColor(new Color(255, 230, 0));
    } else {
        g2.setColor(new Color(255, 40, 40));
    }

    g2.drawString(texto, xMedio - ancho / 2, yMedio + 7);
    }

    private void dibujarNodos(Graphics2D g2) {
        for (Map.Entry<String, Point2D.Double> entry : posiciones.entrySet()) {
            String nombre = entry.getKey();
            Point2D.Double p = entry.getValue();

            boolean seleccionado = nombre.equals(nodoSeleccionado);

            int r = seleccionado ? RADIO + 8 : RADIO;
            int x = (int) p.x - r / 2;
            int y = (int) p.y - r / 2;

            dibujarSombra(g2, x, y, r);
            dibujarNodo(g2, x, y, r, seleccionado);
            dibujarTextoNodo(g2, nombre, x, y, r);
        }
    }

    private void dibujarSombra(Graphics2D g2, int x, int y, int r) {
        g2.setColor(new Color(0, 0, 0, 90));
        g2.fillOval(x + 5, y + 8, r, r);
    }

    private void dibujarNodo(Graphics2D g2, int x, int y, int r, boolean seleccionado) {
        GradientPaint gp = new GradientPaint(
                x, y, tema.nodo1,
                x + r, y + r, tema.nodo2
        );

        g2.setPaint(gp);
        g2.fillOval(x, y, r, r);

        if (tema.nombre.equals("Espacio")) {
            g2.setColor(new Color(255, 255, 255, 90));
            g2.drawArc(x + 6, y + 12, r - 12, r / 2, 180, 180);
        }

        if (tema.nombre.equals("Halo")) {
            g2.setColor(new Color(180, 255, 220, 90));
            g2.drawOval(x + 8, y + 8, r - 16, r - 16);
        }

        if (tema.nombre.equals("Cyberpunk")) {
            g2.setColor(new Color(255, 240, 80, 120));
            g2.drawLine(x + 10, y + r / 2, x + r - 10, y + r / 2);
        }

        g2.setColor(seleccionado ? Color.WHITE : tema.textoSecundario);
        g2.setStroke(new BasicStroke(seleccionado ? 4 : 2));
        g2.drawOval(x, y, r, r);
    }

    private void dibujarTextoNodo(Graphics2D g2, String nombre, int x, int y, int r) {
        g2.setFont(new Font("Arial", Font.BOLD, 13));
        g2.setColor(tema.textoPrincipal);

        FontMetrics fm = g2.getFontMetrics();

        int textoAncho = fm.stringWidth(nombre);
        int textoAlto = fm.getAscent();

        int xTexto = x + (r - textoAncho) / 2;
        int yTexto = y + (r + textoAlto) / 2 - 3;

        g2.drawString(nombre, xTexto, yTexto);
    }

    private void dibujarIndicadorModo(Graphics2D g2) {
        String texto = modoMover ? "Modo mover: ACTIVADO" : "Modo mover: DESACTIVADO";

        g2.setFont(new Font("Arial", Font.BOLD, 13));
        g2.setColor(tema.textoSecundario);
        g2.drawString(texto, 18, 25);
    }
    
    public void eliminarNodoVisual(String nombre) {
    posiciones.remove(nombre);
    velocidades.remove(nombre);
    repaint();
}
    
}