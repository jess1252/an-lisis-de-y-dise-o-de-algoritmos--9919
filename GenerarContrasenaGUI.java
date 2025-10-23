/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package generarcontrasenagui;

/**
 *
 * @author pc
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.Arrays;

public class GenerarContrasenaGUI {
    // Tabla hash simple (open addressing - linear probing)
    static String[] tablaHash = new String[10];

    public static void main(String[] args) {
        // Crear las ventanas
        JFrame frame = new JFrame("Generador de Contraseñas - con búsqueda");
        frame.setSize(520, 380);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());

        // Componentes
        JButton btnGenerar = new JButton("Generar Contraseña");
        JLabel lblResultado = new JLabel("Haz clic en el botón para generar");
        JLabel lblOrdenada = new JLabel("");
        JLabel lblCombinaciones = new JLabel("");

        // Nuevo apartado: búsqued externa
        JTextField txtBuscar = new JTextField(20);
        JButton btnBuscar = new JButton("Buscar en tabla hash");
        JLabel lblBusquedaResultado = new JLabel("");

        // Botón para mostrar tabla
        JButton btnMostrarTabla = new JButton("Mostrar Tabla Hash");

        btnGenerar.addActionListener((ActionEvent e) -> {
            // Configuraciónes de los caracteres
            String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                    + "abcdefghijklmnopqrstuvwxyz"
                    + "0123456789"
                    + "#$%&*";

            int longitud = 8;
            int cantidadCaracteres = caracteres.length(); // n
            double combinaciones = Math.pow(cantidadCaracteres, longitud); // n^k

            // Generar contraseña
            StringBuilder contrasena = new StringBuilder();
            Random random = new Random();

            for (int i = 0; i < longitud; i++) {
                int indice = random.nextInt(cantidadCaracteres);
                contrasena.append(caracteres.charAt(indice));
            }

            // Mostrar la contraseña generada
            lblResultado.setText("Contraseña: " + contrasena);

            // Ordenar la contraseña (solo como demostración)
            char[] arreglo = contrasena.toString().toCharArray();
            Arrays.sort(arreglo);
            lblOrdenada.setText("Ordenada: " + new String(arreglo));

            // Mostrar combinaciones posibles
            lblCombinaciones.setText("Posibles combinaciones: " + String.format("%.0f", combinaciones));

            // Inserción en tabla hash usando linear probing (manejo de colisiones)
            InsertResult res = insertarEnHash(contrasena.toString());
            if (res.inserted) {
                JOptionPane.showMessageDialog(frame,
                        "La contraseña se insertó en la tabla hash.\nÍndice: " + res.index +
                        "\nValor: " + contrasena +
                        "\nSondeos realizados: " + res.probes);
            } else {
                JOptionPane.showMessageDialog(frame,
                        "No se pudo insertar la contraseña: tabla hash llena.");
            }
        });

        btnBuscar.addActionListener((ActionEvent e) -> {
            String clave = txtBuscar.getText().trim();
            if (clave.isEmpty()) {
                lblBusquedaResultado.setText("Ingresa una cadena para buscar.");
                return;
            }

            SearchResult sres = buscarEnHash(clave);
            if (sres.found) {
                lblBusquedaResultado.setText("Encontrado en índice " + sres.index + " (sondeos: " + sres.probes + ")");
                JOptionPane.showMessageDialog(frame,
                        "Encontrado.\nÍndice: " + sres.index +
                        "\nValor: " + tablaHash[sres.index] +
                        "\nSondeos realizados: " + sres.probes);
            } else {
                lblBusquedaResultado.setText("No encontrado (sondeos: " + sres.probes + ")");
                JOptionPane.showMessageDialog(frame,
                        "No se encontró la clave en la tabla hash.\nSondeos realizados: " + sres.probes);
            }
        });

        btnMostrarTabla.addActionListener((ActionEvent e) -> {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < tablaHash.length; i++) {
                sb.append(i).append(": ").append(tablaHash[i] == null ? "<vacío>" : tablaHash[i]).append("\n");
            }
            JOptionPane.showMessageDialog(frame, sb.toString(), "Contenido de la tabla hash", JOptionPane.INFORMATION_MESSAGE);
        });

        // Agregar componentes a la ventana
        frame.add(btnGenerar);
        frame.add(lblResultado);
        frame.add(lblOrdenada);
        frame.add(lblCombinaciones);

        frame.add(new JSeparator(SwingConstants.HORIZONTAL), BorderLayout.CENTER);
        frame.add(new JLabel("Buscar en tabla hash:"));
        frame.add(txtBuscar);
        frame.add(btnBuscar);
        frame.add(lblBusquedaResultado);

        frame.add(new JSeparator(SwingConstants.HORIZONTAL), BorderLayout.CENTER);
        frame.add(btnMostrarTabla);

        // Mostrar ventana
        frame.setVisible(true);
    }

    // Resultado de inserción
    static class InsertResult {
        boolean inserted;
        int index;
        int probes;
        InsertResult(boolean inserted, int index, int probes) {
            this.inserted = inserted;
            this.index = index;
            this.probes = probes;
        }
    }

    // Insertar con linear probing (encadenamiento abierto)
    static InsertResult insertarEnHash(String valor) {
        int size = tablaHash.length;
        int hash = hashIndex(valor, size);
        int probes = 0;

        for (int i = 0; i < size; i++) {
            int idx = (hash + i) % size;
            probes++;
            if (tablaHash[idx] == null) {
                tablaHash[idx] = valor;
                return new InsertResult(true, idx, probes);
            }
            // si ya existe exactamente la misma clave, no duplicamos (opcional)
            if (tablaHash[idx].equals(valor)) {
                return new InsertResult(true, idx, probes); // ya existe
            }
        }
        return new InsertResult(false, -1, probes); // tabla llena
    }

    // Resultado de búsqueda
    static class SearchResult {
        boolean found;
        int index;
        int probes;
        SearchResult(boolean found, int index, int probes) {
            this.found = found;
            this.index = index;
            this.probes = probes;
        }
    }

    // Buscar en tabla hash usando linear probing (misma estrategia que la inserción)
    static SearchResult buscarEnHash(String clave) {
        int size = tablaHash.length;
        int hash = hashIndex(clave, size);
        int probes = 0;

        for (int i = 0; i < size; i++) {
            int idx = (hash + i) % size;
            probes++;
            if (tablaHash[idx] == null) {
                // Si encontramos un espacio vacío, la clave no está (por cómo funciona linear probing)
                return new SearchResult(false, -1, probes);
            }
            if (tablaHash[idx].equals(clave)) {
                return new SearchResult(true, idx, probes);
            }
        }
        return new SearchResult(false, -1, probes); // recorrido completo, no encontrada
    }

    // Índice hash seguros (evitando problema de Integer.MIN_VALUE)
    static int hashIndex(String key, int tableSize) {
        int raw = key.hashCode();
        int positive = raw & 0x7fffffff; // hace el valor no negativo
        return positive % tableSize;
    }
}
// NUEVO APARTO DE FUNCION mert SORT