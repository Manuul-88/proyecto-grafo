package vista;


import modelo.TGrafo;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class VentanaPrincipal extends JFrame {

    private TGrafo grafo;
    private JSpinner spnPeso;
    private JTextField txtNodo, txtOrigen, txtDestino, txtInicioBPF, txtDestinoAlgoritmo;
    private JTextArea txtAreaSalida;
    private PanelGrafo panelDibujo;
    private JPanel barraSuperior, panelResultados, panelControles;
    private JLabel lblTitulo;
    // 12. Checkbox para grafo dirigido
    private JCheckBox chkDirigido;

    public VentanaPrincipal() {
        grafo = new TGrafo();
        setTitle("Calculadora Visual de Grafos");
        setSize(1450, 850);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8, 8));

        panelDibujo = new PanelGrafo(grafo);
        barraSuperior = crearBarraSuperior();
        panelResultados = crearPanelLateral();
        panelControles = crearPanelControles();

        add(barraSuperior, BorderLayout.NORTH);
        add(panelResultados, BorderLayout.WEST);
        add(panelDibujo, BorderLayout.CENTER);
        add(panelControles, BorderLayout.SOUTH);

        aplicarTema(TemaApp.obtenerTema("Halo"));
    }

    private JPanel crearBarraSuperior() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(12, 18, 12, 18));

        lblTitulo = new JLabel("Calculadora Visual de Grafos");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));

        JPanel opciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        opciones.setOpaque(false);

        JComboBox<String> comboTema = new JComboBox<>(new String[]{"Halo", "DD", "Interestelar", "Ego", "Neon", "Cyberpunk"});
        
        // Inicialización de Checkboxes
        chkDirigido = new JCheckBox("Grafo Dirigido");
        JCheckBox chkBezier = new JCheckBox("Curvas Bézier");
        JCheckBox chkMover = new JCheckBox("Modo mover");
        JCheckBox chkFisica = new JCheckBox("Física", true);
        JCheckBox chkMinimapa = new JCheckBox("Minimapa", true);

        JButton btnZoomMas = new JButton("Zoom +");
        JButton btnZoomMenos = new JButton("Zoom -");
        JButton btnResetZoom = new JButton("Reset");

        // Listeners
        comboTema.addActionListener(e -> aplicarTema(TemaApp.obtenerTema((String) comboTema.getSelectedItem())));
        chkDirigido.addActionListener(e -> {
            grafo.setDirigido(chkDirigido.isSelected());
            panelDibujo.repaint();
        });
        chkBezier.addActionListener(e -> panelDibujo.setUsarBezier(chkBezier.isSelected()));
        chkMover.addActionListener(e -> panelDibujo.setModoMover(chkMover.isSelected()));
        chkFisica.addActionListener(e -> panelDibujo.setFisicaActiva(chkFisica.isSelected()));
        chkMinimapa.addActionListener(e -> panelDibujo.setMostrarMinimapa(chkMinimapa.isSelected()));

        btnZoomMas.addActionListener(e -> panelDibujo.acercarZoom());
        btnZoomMenos.addActionListener(e -> panelDibujo.alejarZoom());
        btnResetZoom.addActionListener(e -> panelDibujo.resetZoom());

        opciones.add(new JLabel("Tema:"));
        opciones.add(comboTema);
        opciones.add(chkDirigido);
        opciones.add(chkBezier);
        opciones.add(chkMinimapa);
        opciones.add(chkMover);
        opciones.add(chkFisica);
        opciones.add(btnZoomMas);
        opciones.add(btnZoomMenos);
        opciones.add(btnResetZoom);

        panel.add(lblTitulo, BorderLayout.WEST);
        panel.add(opciones, BorderLayout.EAST);
        return panel;
    }

    private JPanel crearPanelLateral() {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setPreferredSize(new Dimension(330, 0));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        JPanel contenido = new JPanel();
        contenido.setLayout(new BoxLayout(contenido, BoxLayout.Y_AXIS));
        contenido.setOpaque(false);

        // Card Resultados
        JPanel cardResultados = crearCard("RESULTADOS / ADYACENCIA");
        txtAreaSalida = new JTextArea();
        txtAreaSalida.setEditable(false);
        txtAreaSalida.setFont(new Font("Consolas", Font.PLAIN, 13));
        JScrollPane scroll = new JScrollPane(txtAreaSalida);
        scroll.setPreferredSize(new Dimension(295, 260));
        cardResultados.add(scroll, BorderLayout.CENTER);

        // Card Algoritmos
        JPanel cardAlgoritmos = crearCard("ALGORITMOS");
        JPanel gridAlgoritmos = new JPanel(new GridLayout(3, 2, 8, 8));
        gridAlgoritmos.setOpaque(false);
        JButton btnDFS = new JButton("BPF / DFS");
        JButton btnBFS = new JButton("BFS");
        JButton btnDijkstra = new JButton("Dijkstra");
        JButton btnPrim = new JButton("Prim");
        JButton btnKruskal = new JButton("Kruskal");
        
        btnDFS.addActionListener(e -> hacerBPF());
        gridAlgoritmos.add(btnDFS);
        gridAlgoritmos.add(btnBFS);
        gridAlgoritmos.add(btnDijkstra);
        gridAlgoritmos.add(btnPrim);
        gridAlgoritmos.add(btnKruskal);
        cardAlgoritmos.add(gridAlgoritmos, BorderLayout.CENTER);

        // Card Estadísticas
        JPanel cardEstadisticas = crearCard("ESTADÍSTICAS");
        JPanel datos = new JPanel(new GridLayout(5, 1, 4, 4));
        datos.setOpaque(false);
        datos.add(new JLabel("Nodos: dinámico"));
        datos.add(new JLabel("Aristas: dinámico"));
        JButton btnEstadisticas = new JButton("Ver estadísticas");
        btnEstadisticas.addActionListener(e -> mostrarEstadisticas());
        datos.add(btnEstadisticas);
        cardEstadisticas.add(datos, BorderLayout.CENTER);

        contenido.add(cardResultados);
        contenido.add(Box.createVerticalStrut(10));
        contenido.add(cardAlgoritmos);
        contenido.add(Box.createVerticalStrut(10));
        contenido.add(cardEstadisticas);

        panel.add(contenido, BorderLayout.NORTH);
        return panel;
    }

    private JPanel crearPanelControles() {
        JPanel panel = new JPanel(new GridLayout(1, 3, 12, 0));
        panel.setPreferredSize(new Dimension(0, 185));
        panel.setBorder(BorderFactory.createEmptyBorder(8, 10, 10, 10));

        txtNodo = new JTextField(8);
        txtOrigen = new JTextField(8);
        txtDestino = new JTextField(8);
        txtInicioBPF = new JTextField(8);
        txtDestinoAlgoritmo = new JTextField(8);
        spnPeso = new JSpinner(new SpinnerNumberModel(1, 0, 999, 1));

        // Nodos
        JPanel cardNodos = crearCard("NODOS");
        JButton btnAddN = new JButton("+ Nodo");
        btnAddN.addActionListener(e -> agregarNodo());
        JPanel filaN = crearFila();
        filaN.add(new JLabel("Nodo:")); filaN.add(txtNodo); filaN.add(btnAddN);
        cardNodos.add(filaN, BorderLayout.NORTH);

        // Aristas
        JPanel cardAristas = crearCard("ARISTAS");
        JButton btnAddA = new JButton("+ Arista");
        btnAddA.addActionListener(e -> agregarArista());
        JPanel filaA = crearFila();
        filaA.add(new JLabel("O:")); filaA.add(txtOrigen);
        filaA.add(new JLabel("D:")); filaA.add(txtDestino);
        filaA.add(new JLabel("P:")); filaA.add(spnPeso);
        cardAristas.add(filaA, BorderLayout.NORTH);
        cardAristas.add(btnAddA, BorderLayout.CENTER);

        // Recorridos
        JPanel cardRecorridos = crearCard("RECORRIDOS");
        JButton btnMostrar = new JButton("Lista Adyacencia");
        btnMostrar.addActionListener(e -> mostrarAdyacencia());
        cardRecorridos.add(btnMostrar, BorderLayout.SOUTH);

        panel.add(cardNodos);
        panel.add(cardAristas);
        panel.add(cardRecorridos);
        return panel;
    }

    // --- MÉTODOS DE LÓGICA ---
    private void agregarNodo() {
        String nombre = txtNodo.getText().trim();
        if (grafo.agregarNodo(nombre)) {
            panelDibujo.agregarNodoVisual(nombre);
            txtNodo.setText("");
        }
    }

    private void agregarArista() {
        String o = txtOrigen.getText().trim();
        String d = txtDestino.getText().trim();
        int p = (int) spnPeso.getValue();
        if (grafo.agregaAristaPeso(o, d, p)) panelDibujo.repaint();
    }

    private void mostrarAdyacencia() {
        txtAreaSalida.setText(grafo.obtieneListaAdy());
    }

    private void hacerBPF() {
        txtAreaSalida.setText("Recorrido BPF:\n" + grafo.busquedaProfundidad(txtInicioBPF.getText().trim()));
    }

    private void mostrarEstadisticas() {
        int n = grafo.contarNodos();
        int a = grafo.contarAristas();
        txtAreaSalida.setText("ESTADÍSTICAS\nNodos: " + n + "\nAristas: " + a + "\nDensidad: " + (n > 1 ? (2.0 * a) / (n * (n - 1)) : 0));
    }

    private JPanel crearCard(String titulo) {
        JPanel p = new JPanel(new BorderLayout(8, 8));
        p.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(90, 120, 180)), titulo));
        return p;
    }

    private JPanel crearFila() {
        JPanel f = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        f.setOpaque(false);
        return f;
    }

    private void aplicarTema(TemaApp tema) {
        getContentPane().setBackground(tema.fondoVentana);
        panelDibujo.setTemaApp(tema);
        aplicarTemaAComponentes(this, tema);
        repaint();
    }
    //ola//

    private void aplicarTemaAComponentes(Container contenedor, TemaApp tema) {
        for (Component c : contenedor.getComponents()) {
            if (c instanceof JPanel p) p.setBackground(tema.fondoPanel);
            if (c instanceof JLabel l) l.setForeground(tema.textoPrincipal);
            if (c instanceof JButton b) b.setBackground(tema.boton);
            if (c instanceof JTextArea ta) { ta.setBackground(tema.campoFondo); ta.setForeground(tema.campoTexto); }
            if (c instanceof Container con) aplicarTemaAComponentes(con, tema);
        }
    }
}