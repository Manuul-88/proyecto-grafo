//VentanaPrincipal
package vista;
import modelo.TGrafo;
import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {
    private TGrafo grafo;
    private JTextField txtNodo;
    private JTextField txtOrigen;
    private JTextField txtDestino;
    private JTextField txtInicioBPF;
    private JTextArea txtAreaSalida;
    private PanelGrafo panelDibujo;

    public VentanaPrincipal() {
        grafo = new TGrafo();
        setTitle("Gestor de Grafos - Nodos Personalizables");
        setSize(950, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);
        construirComponentes();
    }

    private void construirComponentes() {
        JLabel lblNodo = new JLabel("Nodo:");
        lblNodo.setBounds(20, 20, 50, 25);
        add(lblNodo);
        txtNodo = new JTextField();
        txtNodo.setBounds(70, 20, 120, 25);
        add(txtNodo);

        JButton btnAgregarNodo = new JButton("Agregar Nodo");
        btnAgregarNodo.setBounds(200, 20, 140, 25);
        add(btnAgregarNodo);

        JLabel lblOrigen = new JLabel("Origen:");
        lblOrigen.setBounds(20, 60, 50, 25);
        add(lblOrigen);

        txtOrigen = new JTextField();
        txtOrigen.setBounds(70, 60, 120, 25);
        add(txtOrigen);

        JLabel lblDestino = new JLabel("Destino:");
        lblDestino.setBounds(200, 60, 60, 25);
        add(lblDestino);

        txtDestino = new JTextField();
        txtDestino.setBounds(260, 60, 120, 25);
        add(txtDestino);

        JButton btnAgregarArista = new JButton("Agregar Arista");
        btnAgregarArista.setBounds(390, 60, 140, 25);
        add(btnAgregarArista);

        JButton btnMostrar = new JButton("Mostrar Adyacencia");
        btnMostrar.setBounds(540, 60, 170, 25);
        add(btnMostrar);

        JLabel lblInicioBPF = new JLabel("Inicio BPF:");
        lblInicioBPF.setBounds(20, 100, 70, 25);
        add(lblInicioBPF);

        txtInicioBPF = new JTextField();
        txtInicioBPF.setBounds(90, 100, 120, 25);
        add(txtInicioBPF);

        JButton btnBPF = new JButton("Buscar en Profundidad");
        btnBPF.setBounds(220, 100, 190, 25);
        add(btnBPF);

        txtAreaSalida = new JTextArea();
        txtAreaSalida.setEditable(false);
        txtAreaSalida.setFont(new Font("Monospaced", Font.PLAIN, 14));

        JScrollPane scroll = new JScrollPane(txtAreaSalida);
        scroll.setBounds(20, 150, 350, 370);
        add(scroll);

        panelDibujo = new PanelGrafo(grafo);
        panelDibujo.setBounds(390, 150, 520, 370);
        add(panelDibujo);

        btnAgregarNodo.addActionListener(e -> agregarNodo());
        btnAgregarArista.addActionListener(e -> agregarArista());
        btnMostrar.addActionListener(e -> mostrarAdyacencia());
        btnBPF.addActionListener(e -> hacerBPF());
    }

    private void agregarNodo() {
        String nombre = txtNodo.getText().trim();

        if (grafo.agregarNodo(nombre)) {
            panelDibujo.agregarNodoVisual(nombre);
            panelDibujo.repaint();
            txtAreaSalida.setText("Nodo agregado correctamente: " + nombre);
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
            txtAreaSalida.setText("Arista agregada correctamente entre " + origen + " y " + destino);
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
}