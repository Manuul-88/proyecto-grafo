package vista;

import modelo.TGrafo;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class VentanaPrincipal extends JFrame {

    private TGrafo grafo;
    private JSpinner spnPeso;

    private JTextField txtNodo;
    private JTextField txtOrigen;
    private JTextField txtDestino;
    private JTextField txtInicioBPF;
    private JTextField txtDestinoAlgoritmo;

    private JTextArea txtAreaSalida;
    private PanelGrafo panelDibujo;

    private JPanel barraSuperior;
    private JPanel panelResultados;
    private JPanel panelControles;

    private JLabel lblTitulo;

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

        JLabel lblTema = new JLabel("Tema:");

        JComboBox<String> comboTema = new JComboBox<>(new String[]{
        "Halo", "DD", "Interestelar", "Ego", "Neon", "Cyberpunk"
        });

        JCheckBox chkBezier = new JCheckBox("Curvas Bézier");
        JCheckBox chkMover = new JCheckBox("Modo mover");
        JCheckBox chkFisica = new JCheckBox("Física", true);
        JCheckBox chkMinimapa = new JCheckBox("Minimapa", true);

        JButton btnZoomMas = new JButton("Zoom +");
        JButton btnZoomMenos = new JButton("Zoom -");
        JButton btnResetZoom = new JButton("Reset");

        chkBezier.setOpaque(false);
        chkMover.setOpaque(false);
        chkFisica.setOpaque(false);
        chkMinimapa.setOpaque(false);

        comboTema.addActionListener(e -> {
            String nombreTema = (String) comboTema.getSelectedItem();
            TemaApp tema = TemaApp.obtenerTema(nombreTema);
            aplicarTema(tema);
        });

        chkBezier.addActionListener(e -> panelDibujo.setUsarBezier(chkBezier.isSelected()));
        chkMover.addActionListener(e -> panelDibujo.setModoMover(chkMover.isSelected()));
        chkFisica.addActionListener(e -> panelDibujo.setFisicaActiva(chkFisica.isSelected()));
        chkMinimapa.addActionListener(e -> panelDibujo.setMostrarMinimapa(chkMinimapa.isSelected()));

        btnZoomMas.addActionListener(e -> panelDibujo.acercarZoom());
        btnZoomMenos.addActionListener(e -> panelDibujo.alejarZoom());
        btnResetZoom.addActionListener(e -> panelDibujo.resetZoom());

        opciones.add(lblTema);
        opciones.add(comboTema);
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

        JPanel cardResultados = crearCard("RESULTADOS / ADYACENCIA");

        txtAreaSalida = new JTextArea();
        txtAreaSalida.setEditable(false);
        txtAreaSalida.setFont(new Font("Consolas", Font.PLAIN, 13));
        txtAreaSalida.setLineWrap(true);
        txtAreaSalida.setWrapStyleWord(true);

        JScrollPane scroll = new JScrollPane(txtAreaSalida);
        scroll.setPreferredSize(new Dimension(295, 260));
        cardResultados.add(scroll, BorderLayout.CENTER);

        JPanel cardAlgoritmos = crearCard("ALGORITMOS");
        JPanel gridAlgoritmos = new JPanel(new GridLayout(3, 2, 8, 8));
        gridAlgoritmos.setOpaque(false);

        JButton btnDFS = new JButton("BPF / DFS");
        JButton btnBFS = new JButton("BFS");
        JButton btnDijkstra = new JButton("Dijkstra");
        JButton btnPrim = new JButton("Prim");
        JButton btnKruskal = new JButton("Kruskal");

        gridAlgoritmos.add(btnDFS);
        gridAlgoritmos.add(btnBFS);
        gridAlgoritmos.add(btnDijkstra);
        gridAlgoritmos.add(btnPrim);
        gridAlgoritmos.add(btnKruskal);

        cardAlgoritmos.add(gridAlgoritmos, BorderLayout.CENTER);

        JPanel cardEstadisticas = crearCard("ESTADÍSTICAS");
        JPanel datos = new JPanel(new GridLayout(5, 1, 4, 4));
        datos.setOpaque(false);

        datos.add(new JLabel("Nodos: dinámico"));
        datos.add(new JLabel("Aristas: dinámico"));
        datos.add(new JLabel("Peso total: dinámico"));
        datos.add(new JLabel("Densidad: dinámico"));

        JButton btnEstadisticas = new JButton("Ver estadísticas");
        datos.add(btnEstadisticas);

        cardEstadisticas.add(datos, BorderLayout.CENTER);

        contenido.add(cardResultados);
        contenido.add(Box.createVerticalStrut(10));
        contenido.add(cardAlgoritmos);
        contenido.add(Box.createVerticalStrut(10));
        contenido.add(cardEstadisticas);

        panel.add(contenido, BorderLayout.NORTH);

        btnDFS.addActionListener(e -> hacerBPF());
        btnBFS.addActionListener(e -> txtAreaSalida.setText("BFS todavía no implementado."));
        btnDijkstra.addActionListener(e -> txtAreaSalida.setText("Dijkstra todavía no implementado."));
        btnPrim.addActionListener(e -> txtAreaSalida.setText("Prim todavía no implementado."));
        btnKruskal.addActionListener(e -> txtAreaSalida.setText("Kruskal todavía no implementado."));
        btnEstadisticas.addActionListener(e -> mostrarEstadisticas());

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

        spnPeso = new JSpinner(new SpinnerNumberModel(0, 0, 999, 1));
        spnPeso.setPreferredSize(new Dimension(65, 28));

        JPanel cardNodos = crearCard("NODOS");
        JPanel cardAristas = crearCard("ARISTAS");
        JPanel cardRecorridos = crearCard("RECORRIDOS / RUTAS");

        JButton btnAgregarNodo = new JButton("+ Nodo");
        JButton btnEliminarNodo = new JButton("- Nodo");
        JButton btnEditarNodo = new JButton("Editar nodo");
        JButton btnContar = new JButton("Contar conexiones");

        JPanel filaNodo = crearFila();
        filaNodo.add(new JLabel("Nodo:"));
        filaNodo.add(txtNodo);
        filaNodo.add(btnAgregarNodo);
        filaNodo.add(btnEliminarNodo);
        filaNodo.add(btnEditarNodo);

        JPanel filaNodo2 = crearFila();
        filaNodo2.add(btnContar);

        cardNodos.add(filaNodo, BorderLayout.NORTH);
        cardNodos.add(filaNodo2, BorderLayout.CENTER);

        JButton btnAgregarArista = new JButton("+ Arista");
        JButton btnBorrarArco = new JButton("- Arista");
        JButton btnEditarPeso = new JButton("Editar peso");
        JButton btnAdyacente = new JButton("¿Son adyacentes?");
        JButton btnConsultarPeso = new JButton("Consultar peso");

        JPanel filaArista = crearFila();
        filaArista.add(new JLabel("Origen:"));
        filaArista.add(txtOrigen);
        filaArista.add(new JLabel("Destino:"));
        filaArista.add(txtDestino);
        filaArista.add(new JLabel("Peso:"));
        filaArista.add(spnPeso);

        JPanel filaArista2 = crearFila();
        filaArista2.add(btnAgregarArista);
        filaArista2.add(btnBorrarArco);
        filaArista2.add(btnEditarPeso);

        JPanel filaArista3 = crearFila();
        filaArista3.add(btnAdyacente);
        filaArista3.add(btnConsultarPeso);

        cardAristas.add(filaArista, BorderLayout.NORTH);
        cardAristas.add(filaArista2, BorderLayout.CENTER);
        cardAristas.add(filaArista3, BorderLayout.SOUTH);

        JButton btnBPF = new JButton("BPF");
        JButton btnBFS = new JButton("BFS");
        JButton btnMostrar = new JButton("Lista de adyacencia");
        JButton btnResaltarRuta = new JButton("Resaltar ruta");
        JButton btnQuitarResaltado = new JButton("Quitar resaltado");

        JPanel filaRecorrido = crearFila();
        filaRecorrido.add(new JLabel("Inicio:"));
        filaRecorrido.add(txtInicioBPF);
        filaRecorrido.add(new JLabel("Destino:"));
        filaRecorrido.add(txtDestinoAlgoritmo);

        JPanel filaRecorrido2 = crearFila();
        filaRecorrido2.add(btnBPF);
        filaRecorrido2.add(btnBFS);
        filaRecorrido2.add(btnMostrar);
        filaRecorrido2.add(btnResaltarRuta);
        filaRecorrido2.add(btnQuitarResaltado);

        cardRecorridos.add(filaRecorrido, BorderLayout.NORTH);
        cardRecorridos.add(filaRecorrido2, BorderLayout.CENTER);

        panel.add(cardNodos);
        panel.add(cardAristas);
        panel.add(cardRecorridos);

        btnAgregarNodo.addActionListener(e -> agregarNodo());
        btnEliminarNodo.addActionListener(e -> eliminarNodo());
        btnEditarNodo.addActionListener(e -> editarNodo());
        btnContar.addActionListener(e -> contarConexiones());

        btnAgregarArista.addActionListener(e -> agregarArista());
        btnBorrarArco.addActionListener(e -> borrarArco());
        btnEditarPeso.addActionListener(e -> editarPeso());
        btnAdyacente.addActionListener(e -> verificarAdyacencia());
        btnConsultarPeso.addActionListener(e -> consultarPeso());

        btnBPF.addActionListener(e -> hacerBPF());
        btnBFS.addActionListener(e -> txtAreaSalida.setText("BFS todavía no implementado."));
        btnMostrar.addActionListener(e -> mostrarAdyacencia());
        btnResaltarRuta.addActionListener(e -> resaltarRuta());
        btnQuitarResaltado.addActionListener(e -> quitarResaltado());

        return panel;
    }

    private JPanel crearCard(String titulo) {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(90, 120, 180)),
                        titulo,
                        TitledBorder.LEFT,
                        TitledBorder.TOP,
                        new Font("Arial", Font.BOLD, 12)
                ),
                BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        return panel;
    }

    private JPanel crearFila() {
        JPanel fila = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        fila.setOpaque(false);
        return fila;
    }

    private void aplicarTema(TemaApp tema) {
        getContentPane().setBackground(tema.fondoVentana);

        barraSuperior.setBackground(tema.fondoPanel);
        panelResultados.setBackground(tema.fondoPanel);
        panelControles.setBackground(tema.fondoPanel);

        lblTitulo.setForeground(tema.textoPrincipal);

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

            if (c instanceof JPanel panel) {
                panel.setBackground(tema.fondoPanel);
            }

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

            if (c instanceof JSpinner spinner) {
                JComponent editor = spinner.getEditor();
                if (editor instanceof JSpinner.DefaultEditor defaultEditor) {
                    defaultEditor.getTextField().setBackground(tema.campoFondo);
                    defaultEditor.getTextField().setForeground(tema.campoTexto);
                    defaultEditor.getTextField().setCaretColor(tema.campoTexto);
                }
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

    private void eliminarNodo() {
        String nombre = txtNodo.getText().trim();

        if (grafo.eliminarNodo(nombre)) {
            panelDibujo.eliminarNodoVisual(nombre);
            txtAreaSalida.setText("Nodo eliminado correctamente:\n" + nombre);
            txtNodo.setText("");
            panelDibujo.repaint();
        } else {
            JOptionPane.showMessageDialog(this,
                    "No se pudo eliminar el nodo.\nVerifica que exista y que el campo Nodo no esté vacío.");
        }
    }

    private void editarNodo() {
        String actual = txtNodo.getText().trim();

        if (!grafo.existeNodo(actual)) {
            JOptionPane.showMessageDialog(this, "El nodo actual no existe.");
            return;
        }

        String nuevo = JOptionPane.showInputDialog(this, "Nuevo nombre para el nodo:");

        if (nuevo == null || nuevo.trim().isEmpty()) {
            return;
        }

        if (grafo.editarNodo(actual, nuevo.trim())) {
            panelDibujo.renombrarNodoVisual(actual, nuevo.trim());
            txtAreaSalida.setText("Nodo editado:\n" + actual + " -> " + nuevo.trim());
            txtNodo.setText("");
            panelDibujo.repaint();
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo editar el nodo.");
        }
    }

    private void agregarArista() {
        String origen = txtOrigen.getText().trim();
        String destino = txtDestino.getText().trim();
        int peso = (int) spnPeso.getValue();

        if (grafo.agregaAristaPeso(origen, destino, peso)) {
            panelDibujo.repaint();
            txtAreaSalida.setText("Arista con peso agregada correctamente:\n"
                    + origen + " ↔ " + destino + " | Peso: " + peso);

            txtOrigen.setText("");
            txtDestino.setText("");
            spnPeso.setValue(1);
        } else {
            JOptionPane.showMessageDialog(this,
                    "No se pudo agregar la arista con peso.\nVerifica que ambos nodos existan, no estén repetidos y no sean el mismo.");
        }
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

    private void editarPeso() {
        String origen = txtOrigen.getText().trim();
        String destino = txtDestino.getText().trim();
        int nuevoPeso = (int) spnPeso.getValue();

        if (grafo.editarPeso(origen, destino, nuevoPeso)) {
            txtAreaSalida.setText("Peso editado correctamente:\n"
                    + origen + " - " + destino + " | Nuevo peso: " + nuevoPeso);
            panelDibujo.repaint();
        } else {
            JOptionPane.showMessageDialog(this, "No se pudo editar el peso.");
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

    private void verificarAdyacencia() {
        String origen = txtOrigen.getText().trim();
        String destino = txtDestino.getText().trim();

        if (grafo.adyacente(origen, destino)) {
            txtAreaSalida.setText(origen + " y " + destino + " SÍ son adyacentes.");
        } else {
            txtAreaSalida.setText(origen + " y " + destino + " NO son adyacentes.");
        }
    }

    private void contarConexiones() {
        String nodo = txtNodo.getText().trim();

        if (!grafo.existeNodo(nodo)) {
            JOptionPane.showMessageDialog(this,
                    "El nodo no existe.");
            return;
        }

        int total = grafo.getListaAdy().get(nodo).contar();

        txtAreaSalida.setText("El nodo " + nodo + " tiene " + total + " conexiones.");
    }

    private void consultarPeso() {
        String origen = txtOrigen.getText().trim();
        String destino = txtDestino.getText().trim();

        if (!grafo.existeNodo(origen) || !grafo.existeNodo(destino)) {
            JOptionPane.showMessageDialog(this, "Algún nodo no existe.");
            return;
        }

        int peso = grafo.getListaAdy().get(origen).obtenerPeso(destino);

        if (peso == -1) {
            txtAreaSalida.setText("No existe conexión entre " + origen + " y " + destino);
        } else {
            txtAreaSalida.setText("Peso entre " + origen + " y " + destino + " = " + peso);
        }
    }

    private void resaltarRuta() {
        String origen = txtInicioBPF.getText().trim();
        String destino = txtDestinoAlgoritmo.getText().trim();

        String ruta = grafo.obtenerRutaDFS(origen, destino);

        if (ruta.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No se encontró ruta.");
            return;
        }

        panelDibujo.resaltarRuta(ruta);
        txtAreaSalida.setText("Ruta resaltada:\n" + ruta);
    }

    private void quitarResaltado() {
        panelDibujo.limpiarRutaResaltada();
        txtAreaSalida.setText("Resaltado de ruta eliminado.");
    }

    private void mostrarEstadisticas() {
        int nodos = grafo.contarNodos();
        int aristas = grafo.contarAristas();
        int pesoTotal = grafo.pesoTotal();

        double densidad = 0;

        if (nodos > 1) {
            densidad = (2.0 * aristas) / (nodos * (nodos - 1));
        }

        txtAreaSalida.setText(
                "ESTADÍSTICAS DEL GRAFO\n\n"
                        + "Nodos: " + nodos + "\n"
                        + "Aristas: " + aristas + "\n"
                        + "Peso total: " + pesoTotal + "\n"
                        + "Densidad: " + densidad
        );
    }
}