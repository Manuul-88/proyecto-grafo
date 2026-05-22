//panel del grafo
package vista;

import modelo.TGrafo;
import modelo.TLista;
import modelo.TNodo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.awt.geom.QuadCurve2D;
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

    private double zoom = 1.0;
    private boolean mostrarMinimapa = true;
    private boolean usarBezier = false;

    private boolean parpadeoRuta = true;
    private int contadorParpadeo = 0;

    private boolean dirigido = false;

    private java.util.ArrayList<String> rutaResaltada = new java.util.ArrayList<>();

    public PanelGrafo(TGrafo grafo) {
        this.grafo = grafo;
        this.posiciones = new HashMap<>();
        this.velocidades = new HashMap<>();
        this.tema = TemaApp.obtenerTema("Halo");

        setBackground(tema.fondoCanvas);
        setBorder(BorderFactory.createLineBorder(new Color(50, 60, 90), 2));

        configurarMouse();

        timer = new Timer(16, e -> {
            if (fisicaActiva) {
                actualizarFisica();
            }

            contadorParpadeo++;
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

    public void setDirigido(boolean dirigido) {
        this.dirigido = dirigido;
        repaint();
    }

    public void agregarNodoVisual(String nombre) {
        if (posiciones.containsKey(nombre)) {
            return;
        }

        int cantidad = posiciones.size();

        double centroX = Math.max((getWidth() / zoom) / 2.0, 250);
        double centroY = Math.max((getHeight() / zoom) / 2.0, 250);

        double angulo = cantidad * 0.8;
        double distancia = 120 + cantidad * 8;

        double x = centroX + Math.cos(angulo) * distancia;
        double y = centroY + Math.sin(angulo) * distancia;

        posiciones.put(nombre, new Point2D.Double(x, y));
        velocidades.put(nombre, new Point2D.Double(0, 0));

        repaint();
    }

    public void eliminarNodoVisual(String nombre) {
        posiciones.remove(nombre);
        velocidades.remove(nombre);
        repaint();
    }

    public void renombrarNodoVisual(String actual, String nuevo) {
        Point2D.Double pos = posiciones.remove(actual);
        Point2D.Double vel = velocidades.remove(actual);

        if (pos != null) {
            posiciones.put(nuevo, pos);
        }

        if (vel != null) {
            velocidades.put(nuevo, vel);
        }

        repaint();
    }
    

    public void limpiarVisual() {
        posiciones.clear();
        velocidades.clear();
        rutaResaltada.clear();
        nodoSeleccionado = null;
        repaint();
    }

    private void configurarMouse() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (!modoMover) return;

                int mx = (int) (e.getX() / zoom);
                int my = (int) (e.getY() / zoom);

                nodoSeleccionado = obtenerNodoEn(mx, my);

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

                p.x = e.getX() / zoom;
                p.y = e.getY() / zoom;

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
        // movimiento mas suave
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

        double maxX = getWidth() / zoom;
        double maxY = getHeight() / zoom;

        if (p.x < margen) p.x = margen;
        if (p.y < margen) p.y = margen;

        if (p.x > maxX - margen) p.x = maxX - margen;
        if (p.y > maxY - margen) p.y = maxY - margen;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        dibujarFondo(g2);

        Graphics2D gZoom = (Graphics2D) g2.create();
        gZoom.scale(zoom, zoom);

        dibujarAristas(gZoom);
        dibujarNodos(gZoom);

        gZoom.dispose();

        dibujarIndicadorModo(g2);

        if (mostrarMinimapa) {
            dibujarMinimapa(g2);
        }
    }

    private void dibujarFondo(Graphics2D g2) {
        g2.setColor(tema.fondoCanvas);
        g2.fillRect(0, 0, getWidth(), getHeight());

        if (tema.nombre.equals("Halo")) {
            g2.setColor(new Color(120, 220, 190, 40));

            for (int i = 0; i < 9; i++) {
                int size = 180 + i * 85;
                g2.drawOval(getWidth() / 2 - size / 2, getHeight() / 2 - size / 2, size, size);
            }

            g2.setColor(new Color(90, 255, 180, 35));

            for (int i = 0; i < 35; i++) {
                int x = (i * 113) % Math.max(getWidth(), 1);
                int y = (i * 67) % Math.max(getHeight(), 1);
                g2.fillOval(x, y, 2, 2);
            }
        }

        if (tema.nombre.equals("DD")) {
            int centroY = getHeight() / 2;

            g2.setColor(new Color(255, 30, 30, 45));

            for (int y = 80; y < getHeight(); y += 90) {
                for (int x = 0; x < getWidth(); x += 8) {
                    int onda = (int) (Math.sin((x + y) * 0.035) * 18);
                    g2.fillOval(x, y + onda, 3, 3);
                }
            }

            g2.setColor(new Color(255, 0, 0, 80));

            for (int i = 0; i < 4; i++) {
                g2.drawOval(getWidth() / 2 - 140 - i * 55, centroY - 140 - i * 55,
                        280 + i * 110, 280 + i * 110);
            }
        }

        if (tema.nombre.equals("Interestelar")) {
            g2.setColor(new Color(180, 220, 255, 120));

            for (int i = 0; i < 120; i++) {
                int x = (i * 97) % Math.max(getWidth(), 1);
                int y = (i * 53) % Math.max(getHeight(), 1);
                int s = i % 5 == 0 ? 3 : 2;
                g2.fillOval(x, y, s, s);
            }

            g2.setColor(new Color(70, 120, 255, 40));

            for (int i = 0; i < 7; i++) {
                int size = 220 + i * 95;
                g2.drawOval(getWidth() / 2 - size / 2, getHeight() / 2 - size / 2, size, size / 2);
            }
        }

        if (tema.nombre.equals("Ego")) {
            g2.setColor(new Color(0, 120, 255, 45));

            for (int x = -getHeight(); x < getWidth(); x += 70) {
                g2.drawLine(x, 0, x + getHeight(), getHeight());
            }

            g2.setColor(new Color(0, 255, 220, 40));

            for (int i = 0; i < 8; i++) {
                int size = 130 + i * 75;
                g2.drawOval(getWidth() / 2 - size / 2, getHeight() / 2 - size / 2, size, size);
            }
        }
        
        if (tema.nombre.equals("Invencible")) {

            GradientPaint fondo = new GradientPaint(
                    0, 0,
                    new Color(0, 150, 240),
                    getWidth(), getHeight(),
                    new Color(0, 88, 185)
            );

            g2.setPaint(fondo);

            g2.fillRect(
                    0,
                    0,
                    getWidth(),
                    getHeight()
            );

            g2.setColor(new Color(0, 100, 185, 22));

            for (int y = 0; y < getHeight(); y += 22) {
                g2.drawLine(0, y, getWidth(), y);
            }

            for (int x = 0; x < getWidth(); x += 22) {
                g2.drawLine(x, 0, x, getHeight());
            }
        }
        

        if (tema.nombre.equals("Cyberpunk")) {
            GradientPaint fondo = new GradientPaint(
                    0, 0,
                    new Color(12, 8, 28),
                    getWidth(), getHeight(),
                    new Color(35, 0, 55)
            );

            g2.setPaint(fondo);
            g2.fillRect(0, 0, getWidth(), getHeight());

            g2.setColor(new Color(255, 230, 0, 170));
            g2.setStroke(new BasicStroke(3));

            int w = getWidth();
            int h = getHeight();

            g2.drawLine(0, h - 90, 180, h);
            g2.drawLine(45, h - 190, 290, h);

            g2.drawLine(w - 260, 0, w, 150);
            g2.drawLine(w - 420, 0, w, 240);

            g2.drawLine(35, 35, 210, 35);
            g2.drawLine(35, 35, 35, 105);

            g2.drawLine(w - 230, h - 35, w - 35, h - 35);
            g2.drawLine(w - 35, h - 120, w - 35, h - 35);

            g2.setColor(new Color(255, 0, 180, 110));
            g2.setStroke(new BasicStroke(2));

            g2.drawLine(70, 70, 155, 70);
            g2.drawLine(w - 180, 85, w - 75, 85);

            g2.setColor(new Color(0, 255, 255, 80));
            for (int y = 0; y < h; y += 32) {
                g2.drawLine(0, y, w, y);
            }
        }
        
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

                if (p2 != null && debeDibujarArista(origen, destino)) {

                    boolean resaltada = aristaEnRuta(origen, destino);

                    if (resaltada) {
                        boolean visible = !parpadeoRuta || (contadorParpadeo / 25) % 2 == 0;

                        if (visible) {
                            g2.setColor(Color.CYAN);
                            g2.setStroke(new BasicStroke(7, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                        } else {
                            g2.setColor(new Color(255, 255, 255, 80));
                            g2.setStroke(new BasicStroke(3, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                        }

                    } else {
                        if (peso == 0) {
                            g2.setColor(Color.WHITE);
                        } else if (peso <= 3) {
                            g2.setColor(new Color(0, 255, 100));
                        } else if (peso <= 7) {
                            g2.setColor(new Color(255, 230, 0));
                        } else {
                            g2.setColor(new Color(255, 60, 60));
                        }

                        float grosor = 2.0f + Math.min(peso, 10) * 0.35f;

                        g2.setStroke(new BasicStroke(
                                grosor,
                                BasicStroke.CAP_ROUND,
                                BasicStroke.JOIN_ROUND
                        ));
                    }

                    double mx = (p1.x + p2.x) / 2.0;
                    double my = (p1.y + p2.y) / 2.0;

                    double dx = p2.x - p1.x;
                    double dy = p2.y - p1.y;

                    double len = Math.sqrt(dx * dx + dy * dy);

                    if (len != 0) {
                        double ux = dx / len;
                        double uy = dy / len;

                        double gap = 22;

                        double gx1 = mx - ux * gap;
                        double gy1 = my - uy * gap;

                        double gx2 = mx + ux * gap;
                        double gy2 = my + uy * gap;

                        if (usarBezier) {
                            double nx = -uy;
                            double ny = ux;

                            double curva = 55;

                            double cx = mx + nx * curva;
                            double cy = my + ny * curva;

                            QuadCurve2D curvaBezier = new QuadCurve2D.Double(
                                    p1.x, p1.y,
                                    cx, cy,
                                    p2.x, p2.y
                            );

                            g2.draw(curvaBezier);

                        } else {
                            g2.drawLine((int) p1.x, (int) p1.y, (int) gx1, (int) gy1);
                            g2.drawLine((int) gx2, (int) gy2, (int) p2.x, (int) p2.y);
                        }

                        if (dirigido) {
                            dibujarFlecha(g2, p1.x, p1.y, p2.x, p2.y);
                        }

                        dibujarPesoCentro(g2, mx, my, peso);
                    }
                }

                aux = aux.getSiguiente();
            }
        }
    }
    
    private boolean debeDibujarArista(String origen, String destino) {
        if (!dirigido) {
            return origen.compareTo(destino) < 0;
        }

        TLista listaDestino = grafo.getListaAdy().get(destino);

        if (listaDestino != null && listaDestino.buscar(origen)) {
            return origen.compareTo(destino) < 0;
        }

        return true;
    }

    private void dibujarFlecha(Graphics2D g2, double x1, double y1, double x2, double y2) {
        double angulo = Math.atan2(y2 - y1, x2 - x1);

        double distanciaNodo = RADIO / 2.0 + 8;

        double puntaX = x2 - Math.cos(angulo) * distanciaNodo;
        double puntaY = y2 - Math.sin(angulo) * distanciaNodo;

        int largo = 16;
        int ancho = 8;

        Polygon flecha = new Polygon();

        flecha.addPoint((int) puntaX, (int) puntaY);

        flecha.addPoint(
                (int) (puntaX - largo * Math.cos(angulo) + ancho * Math.sin(angulo)),
                (int) (puntaY - largo * Math.sin(angulo) - ancho * Math.cos(angulo))
        );

        flecha.addPoint(
                (int) (puntaX - largo * Math.cos(angulo) - ancho * Math.sin(angulo)),
                (int) (puntaY - largo * Math.sin(angulo) + ancho * Math.cos(angulo))
        );

        g2.fillPolygon(flecha);
    }

    private void dibujarPesoCentro(Graphics2D g2, double x, double y, int peso) {
        // peso centrado
        String texto = String.valueOf(peso);

        g2.setFont(new Font("Arial", Font.BOLD, 16));

        FontMetrics fm = g2.getFontMetrics();

        int ancho = fm.stringWidth(texto);
        int alto = fm.getAscent();

        g2.setColor(new Color(0, 0, 0, 180));

        g2.drawString(
                texto,
                (int) (x - ancho / 2 + 2),
                (int) (y + alto / 2 + 2)
        );

        if (peso == 0) {
            g2.setColor(Color.WHITE);
        } else if (peso <= 3) {
            g2.setColor(new Color(0, 255, 100));
        } else if (peso <= 7) {
            g2.setColor(new Color(255, 230, 0));
        } else {
            g2.setColor(new Color(255, 60, 60));
        }

        g2.drawString(
                texto,
                (int) (x - ancho / 2),
                (int) (y + alto / 2)
        );
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
        String texto = modoMover ? "Modo mover ACTIVADO" : "Modo mover DESACTIVADO";

        g2.setFont(new Font("Arial", Font.BOLD, 13));
        g2.setColor(tema.textoSecundario);
        g2.drawString(texto, 18, 25);
    }

    private void dibujarMinimapa(Graphics2D g2) {
        // mini mapa
        int w = 170;
        int h = 110;
        int x = getWidth() - w - 20;
        int y = 20;

        g2.setColor(new Color(0, 0, 0, 140));
        g2.fillRoundRect(x, y, w, h, 12, 12);

        g2.setColor(new Color(120, 180, 255));
        g2.drawRoundRect(x, y, w, h, 12, 12);

        if (posiciones.isEmpty()) return;

        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        double maxX = -Double.MAX_VALUE;
        double maxY = -Double.MAX_VALUE;

        for (Point2D.Double p : posiciones.values()) {
            if (p.x < minX) minX = p.x;
            if (p.y < minY) minY = p.y;
            if (p.x > maxX) maxX = p.x;
            if (p.y > maxY) maxY = p.y;
        }

        double rangoX = Math.max(maxX - minX, 1);
        double rangoY = Math.max(maxY - minY, 1);

        for (Map.Entry<String, Point2D.Double> entry : posiciones.entrySet()) {
            Point2D.Double p = entry.getValue();

            int px = x + 15 + (int) ((p.x - minX) / rangoX * (w - 30));
            int py = y + 15 + (int) ((p.y - minY) / rangoY * (h - 30));

            g2.setColor(Color.CYAN);
            g2.fillOval(px - 3, py - 3, 6, 6);
        }
    }

    public void acercarZoom() {
        zoom += 0.1;
        if (zoom > 2.5) zoom = 2.5;
        repaint();
    }

    public void alejarZoom() {
        zoom -= 0.1;
        if (zoom < 0.4) zoom = 0.4;
        repaint();
    }

    public void resetZoom() {
        zoom = 1.0;
        repaint();
    }

    public void setMostrarMinimapa(boolean mostrarMinimapa) {
        this.mostrarMinimapa = mostrarMinimapa;
        repaint();
    }

    public void setUsarBezier(boolean usarBezier) {
        this.usarBezier = usarBezier;
        repaint();
    }

    public void resaltarRuta(String rutaTexto) {
        rutaResaltada.clear();

        if (rutaTexto == null || rutaTexto.trim().isEmpty()) {
            repaint();
            return;
        }

        String[] partes = rutaTexto.trim().split("\\s+");

        for (String p : partes) {
            rutaResaltada.add(p);
        }

        repaint();
    }

    public void limpiarRutaResaltada() {
        rutaResaltada.clear();
        repaint();
    }

    private boolean aristaEnRuta(String origen, String destino) {
        for (int i = 0; i < rutaResaltada.size() - 1; i++) {
            String a = rutaResaltada.get(i);
            String b = rutaResaltada.get(i + 1);

            if ((a.equalsIgnoreCase(origen) && b.equalsIgnoreCase(destino)) ||
                    (a.equalsIgnoreCase(destino) && b.equalsIgnoreCase(origen))) {
                return true;
            }
        }

        return false;
    }    
}