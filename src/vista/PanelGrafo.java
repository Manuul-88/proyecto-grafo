//PanelGrafo
package vista;

import modelo.TGrafo;
import modelo.TLista;
import modelo.TNodo;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class PanelGrafo extends JPanel {
    private TGrafo grafo;
    private HashMap<String, Point> posiciones;
    public PanelGrafo(TGrafo grafo) {
        this.grafo = grafo;
        this.posiciones = new HashMap<>();
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLineBorder(Color.GRAY));
    }

    public void agregarNodoVisual(String nombre) {
        if (posiciones.containsKey(nombre)) {
            return;
        }
        int cantidad = posiciones.size();
        int x = 60 + (cantidad % 4) * 90;
        int y = 60 + (cantidad / 4) * 90;

        posiciones.put(nombre, new Point(x, y));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        dibujarAristas(g2);
        dibujarNodos(g2);
    }

    private void dibujarAristas(Graphics2D g2) {
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(2));
        for (Map.Entry<String, TLista> entry : grafo.getListaAdy().entrySet()) {
            String origen = entry.getKey();
            Point p1 = posiciones.get(origen);
            if (p1 == null) {
                continue;
            }
            TNodo aux = entry.getValue().getCabecera().getSiguiente();
            while (aux != null) {
                String destino = aux.getDato();
                Point p2 = posiciones.get(destino);
                if (p2 != null) {
                    if (origen.compareTo(destino) < 0) {
                        g2.drawLine(p1.x + 25, p1.y + 25, p2.x + 25, p2.y + 25);
                    }
                }
                aux = aux.getSiguiente();
            }
        }
    }

    private void dibujarNodos(Graphics2D g2) {
        for (Map.Entry<String, Point> entry : posiciones.entrySet()) {
            String nombre = entry.getKey();
            Point p = entry.getValue();
            g2.setColor(new Color(195, 215, 235));
            g2.fillOval(p.x, p.y, 50, 50);
            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(2));
            g2.drawOval(p.x, p.y, 50, 50);
            g2.setFont(new Font("Arial", Font.BOLD, 12));
            FontMetrics fm = g2.getFontMetrics();
            int textoAncho = fm.stringWidth(nombre);
            int xTexto = p.x + (50 - textoAncho) / 2;
            int yTexto = p.y + 30;
            g2.drawString(nombre, xTexto, yTexto);
        }
    }
}