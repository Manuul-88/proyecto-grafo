package vista;

import modelo.TGrafo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class VentanaPrincipal extends JFrame {

    private TGrafo grafo;

    private JTextField txtNodo;
    private JTextField txtOrigen;
    private JTextField txtDestino;
    private JTextField txtInicioBPF;

    private JTextArea txtAreaSalida;
    private PanelGrafo panelDibujo;

    private JPanel barraSuperior;
    private JPanel panelResultados;
    private JPanel panelControles;

    private JLabel lblTitulo;
    private JLabel lblResultados;

    public VentanaPrincipal() {
        grafo = new TGrafo();

        setTitle("Calculadora Visual de Grafos");
        setSize(1200, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        panelDibujo = new PanelGrafo(grafo);

        barraSuperior = crearBarraSuperior();
        panelResultados = crearPanelResultados();
        panelControles = crearPanelControles();

        add(barraSuperior, BorderLayout.NORTH);
        add(panelResultados, BorderLayout.WEST);
        add(panelDibujo, BorderLayout.CENTER);
        add(panelControles, BorderLayout.SOUTH);

        aplicarTema(TemaApp.obtenerTema("Espacio"));
    }

    private JPanel crearBarraSuperior() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(12, 18, 12, 18));

        lblTitulo = new JLabel("Calculadora Visual de Grafos");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));

        JPanel opciones = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        opciones.setOpaque(false);

        JLabel lblTema = new JLabel("Tema:");

        JComboBox<String> comboTema = new JComboBox<>(new String[]{
                "Espacio", "Neon", "Clasico", "Sakura", "Cyberpunk", "Halo"
        });

        JCheckBox chkMover = new JCheckBox("Modo mover");
        JCheckBox chkFisica = new JCheckBox("Física", true);

        chkMover.setOpaque(false);
        chkFisica.setOpaque(false);

        comboTema.addActionListener(e -> {
            String nombreTema = (String) comboTema.getSelectedItem();
            TemaApp tema = TemaApp.obtenerTema(nombreTema);
            aplicarTema(tema);
        });

        chkMover.addActionListener(e -> panelDibujo.setModoMover(chkMover.isSelected()));
        chkFisica.addActionListener(e -> panelDibujo.setFisicaActiva(chkFisica.isSelected()));

        opciones.add(lblTema);
        opciones.add(comboTema);
        opciones.add(chkMover);
        opciones.add(chkFisica);

        panel.add(lblTitulo, BorderLayout.WEST);
        panel.add(opciones, BorderLayout.EAST);

        return panel;
    }

    private JPanel crearPanelResultados() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(320, 0));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        lblResultados = new JLabel("Resultados / Adyacencia");
        lblResultados.setFont(new Font("Arial", Font.BOLD, 16));
        lblResultados.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        txtAreaSalida = new JTextArea();
        txtAreaSalida.setEditable(false);
        txtAreaSalida.setFont(new Font("Consolas", Font.PLAIN, 14));
        txtAreaSalida.setLineWrap(true);
        txtAreaSalida.setWrapStyleWord(true);

        JScrollPane scroll = new JScrollPane(txtAreaSalida);
        scroll.setBorder(BorderFactory.createLineBorder(new Color(210, 215, 225)));

        panel.add(lblResultados, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);

        return panel;
    }

    private JPanel crearPanelControles() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 12));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        txtNodo = new JTextField(8);
        txtOrigen = new JTextField(8);
        txtDestino = new JTextField(8);
        txtInicioBPF = new JTextField(8);

        JButton btnAgregarNodo = new JButton("+ Nodo");
        JButton btnAgregarArista = new JButton("+ Arista");
        JButton btnBorrarArco = new JButton("- Arista");
        JButton btnMostrar = new JButton("Adyacencia");
        JButton btnBPF = new JButton("BPF");

        panel.add(new JLabel("Nodo:"));
        panel.add(txtNodo);
        panel.add(btnAgregarNodo);

        panel.add(new JLabel("Origen:"));
        panel.add(txtOrigen);

        panel.add(new JLabel("Destino:"));
        panel.add(txtDestino);

        panel.add(btnAgregarArista);
        panel.add(btnBorrarArco);

        panel.add(new JLabel("Inicio:"));
        panel.add(txtInicioBPF);
        panel.add(btnBPF);
        panel.add(btnMostrar);

        btnAgregarNodo.addActionListener(e -> agregarNodo());
        btnAgregarArista.addActionListener(e -> agregarArista());
        btnBorrarArco.addActionListener(e -> borrarArco());
        btnMostrar.addActionListener(e -> mostrarAdyacencia());
        btnBPF.addActionListener(e -> hacerBPF());

        return panel;
    }

    private void aplicarTema(TemaApp tema) {
        getContentPane().setBackground(tema.fondoVentana);

        barraSuperior.setBackground(tema.fondoPanel);
        panelResultados.setBackground(tema.fondoPanel);
        panelControles.setBackground(tema.fondoPanel);

        lblTitulo.setForeground(tema.textoPrincipal);
        lblResultados.setForeground(tema.textoPrincipal);

        txtAreaSalida.setBackground(tema.campoFondo);
        txtAreaSalida.setForeground(tema.campoTexto);
        txtAreaSalida.setCaretColor(tema.campoTexto);

        aplicarTemaAComponentes(barraSuperior, tema);
        aplicarTemaAComponentes(panelResultados, tema);
        aplicarTemaAComponentes(panelControles, tema);

        panelDibujo.setTemaApp(tema);

        repaint();
    }

    private void aplicarTemaAComponentes(Container contenedor, TemaApp tema) {
        for (Component c : contenedor.getComponents()) {

            if (c instanceof JLabel label) {
                label.setForeground(tema.textoPrincipal);
            }

            if (c instanceof JCheckBox checkBox) {
                checkBox.setForeground(tema.textoPrincipal);
                checkBox.setBackground(tema.fondoPanel);
                checkBox.setFocusPainted(false);
            }

            if (c instanceof JTextField textField) {
                textField.setBackground(tema.campoFondo);
                textField.setForeground(tema.campoTexto);
                textField.setCaretColor(tema.campoTexto);
                textField.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(tema.botonHover),
                        BorderFactory.createEmptyBorder(4, 6, 4, 6)
                ));
            }

            if (c instanceof JButton button) {
                estilizarBoton(button, tema);
            }

            if (c instanceof JComboBox<?> comboBox) {
                comboBox.setBackground(tema.campoFondo);
                comboBox.setForeground(tema.campoTexto);
            }

            if (c instanceof JScrollPane scrollPane) {
                scrollPane.getViewport().setBackground(tema.campoFondo);
                scrollPane.setBorder(BorderFactory.createLineBorder(tema.botonHover));
            }

            if (c instanceof Container container) {
                aplicarTemaAComponentes(container, tema);
            }
        }
    }

    private void estilizarBoton(JButton boton, TemaApp tema) {
        boton.setBackground(tema.boton);
        boton.setForeground(tema.textoPrincipal);
        boton.setFocusPainted(false);
        boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(tema.botonHover),
                BorderFactory.createEmptyBorder(6, 12, 6, 12)
        ));

        for (MouseListener ml : boton.getMouseListeners()) {
            if (ml instanceof MouseAdapter) {
                boton.removeMouseListener(ml);
            }
        }

        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                boton.setBackground(tema.botonHover);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                boton.setBackground(tema.boton);
            }
        });
    }

    private void agregarNodo() {
        String nombre = txtNodo.getText().trim();

        if (grafo.agregarNodo(nombre)) {
            panelDibujo.agregarNodoVisual(nombre);
            txtAreaSalida.setText("Nodo agregado correctamente:\n" + nombre);
            txtNodo.setText("");
        } else {
            JOptionPane.showMessageDialog(this,
                    "No se pudo agregar el nodo.\nVerifica que no esté vacío o repetido.");
        }
    }

    private void agregarArista() {
        String origen = txtOrigen.getText().trim();
        String destino = txtDestino.getText().trim();

        if (grafo.agregaArista(origen, destino)) {
            panelDibujo.repaint();
            txtAreaSalida.setText("Arista agregada correctamente:\n" + origen + " ↔ " + destino);
            txtOrigen.setText("");
            txtDestino.setText("");
        } else {
            JOptionPane.showMessageDialog(this,
                    "No se pudo agregar la arista.\nVerifica que ambos nodos existan, no estén repetidos y no sean el mismo.");
        }
    }

    private void mostrarAdyacencia() {
        txtAreaSalida.setText(grafo.obtieneListaAdy());
    }

    private void hacerBPF() {
        String inicio = txtInicioBPF.getText().trim();
        String resultado = grafo.busquedaProfundidad(inicio);
        txtAreaSalida.setText("Recorrido en profundidad:\n" + resultado);
    }

    private void borrarArco() {
        String origen = txtOrigen.getText().trim();
        String destino = txtDestino.getText().trim();

        if (grafo.borrarArco(origen, destino)) {
            panelDibujo.repaint();
            txtAreaSalida.setText("Arista eliminada correctamente:\n" + origen + " - " + destino);
            txtOrigen.setText("");
            txtDestino.setText("");
        } else {
            JOptionPane.showMessageDialog(this,
                    "No se pudo borrar la arista.\nVerifica que ambos nodos existan y que estén conectados.");
        }
    }
}