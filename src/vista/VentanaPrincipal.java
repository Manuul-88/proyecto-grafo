package vista;

import modelo.TGrafo;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class VentanaPrincipal extends JFrame {
    TGrafo grafo;
    JTextField txtOrigen, txtDestino;
    JTextArea txtAreaAdyacencia;
    PanelGrafo panelDibujo;
    ArrayList<int[]> lineas = new ArrayList<>();

    public VentanaPrincipal() {
        grafo = new TGrafo(4); 
        
        setTitle("Gestor de Grafos");
        setSize(850, 550);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);

        // --- ENTRADAS ---
        JLabel lblOrigen = new JLabel("Origen:");
        lblOrigen.setBounds(370, 25, 60, 25);
        add(lblOrigen);
        
        txtOrigen = new JTextField();
        txtOrigen.setBounds(425, 25, 100, 25);
        add(txtOrigen);

        JLabel lblDestino = new JLabel("Destino:");
        lblDestino.setBounds(370, 55, 60, 25);
        add(lblDestino);
        
        txtDestino = new JTextField();
        txtDestino.setBounds(425, 55, 100, 25);
        add(txtDestino);

        // --- BOTONES ---
        JButton btnAgregarArista = new JButton("Agregar Arista");
        btnAgregarArista.setBounds(540, 35, 130, 35);
        add(btnAgregarArista);

        JButton btnMostrar = new JButton("Mostrar Adyacencia");
        btnMostrar.setBounds(685, 35, 145, 35);
        add(btnMostrar);

        // --- ÁREAS DE VISUALIZACIÓN ---
        txtAreaAdyacencia = new JTextArea();
        txtAreaAdyacencia.setEditable(false);
        txtAreaAdyacencia.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scroll = new JScrollPane(txtAreaAdyacencia);
        scroll.setBounds(20, 130, 350, 350);
        add(scroll);

        panelDibujo = new PanelGrafo(grafo, lineas);
        panelDibujo.setBounds(390, 130, 430, 350);
        add(panelDibujo);

        // --- LÓGICA DE EVENTOS ---
        btnAgregarArista.addActionListener(e -> {
            int o = letraANumero(txtOrigen.getText());
            int d = letraANumero(txtDestino.getText());
            
            if(grafo.agregaArista(o, d)) {
                lineas.add(new int[]{o - 1, d - 1});
                panelDibujo.repaint(); 
            } else {
                JOptionPane.showMessageDialog(this, "Error: Vértices inválidos o repetidos.");
            }
        });

        btnMostrar.addActionListener(e -> {
            txtAreaAdyacencia.setText(grafo.obtieneListaAdy());
        });
    }

    private int letraANumero(String letra) {
        if (letra == null || letra.trim().isEmpty()) return 0;
        return letra.trim().toUpperCase().charAt(0) - 'A' + 1;
    }
}