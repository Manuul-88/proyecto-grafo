package vista;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import modelo.TGrafo;

public class PanelGrafo extends JPanel {
    private TGrafo grafo;
    private ArrayList<int[]> lineas;

    public PanelGrafo(TGrafo grafo, ArrayList<int[]> lineas) {
        this.grafo = grafo;
        this.lineas = lineas;
        this.setBackground(Color.WHITE);
        this.setBorder(BorderFactory.createLineBorder(Color.GRAY));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Coordenadas paraA, B, C, D
        int[][] pos = {{220, 40}, {80, 130}, {340, 80}, {260, 200}};

       
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(2));
        for (int[] l : lineas) {
            int x1 = pos[l[0]][0] + 25;
            int y1 = pos[l[0]][1] + 25;
            int x2 = pos[l[1]][0] + 25;
            int y2 = pos[l[1]][1] + 25;
            g2.drawLine(x1, y1, x2, y2);
        }

        
        for (int i = 0; i < pos.length; i++) {
            g2.setColor(new Color(195, 215, 235)); 
            g2.fillOval(pos[i][0], pos[i][1], 50, 50);
            
            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(2));
            g2.drawOval(pos[i][0], pos[i][1], 50, 50);
            
            
            g2.setFont(new Font("Arial", Font.BOLD, 24)); 
            g2.drawString(String.valueOf((char) ('A' + i)), pos[i][0] + 16, pos[i][1] + 34);
        }
    }
}