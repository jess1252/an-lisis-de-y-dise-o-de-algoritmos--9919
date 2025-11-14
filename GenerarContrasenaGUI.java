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

    // ------------------------------------------------------------
    //              MATRIZ Y BACKTRACKING (LABERINTO)
    // ------------------------------------------------------------
    static char[][] grid = {
        {'A','b','3','x'},
        {'#','C','9','Q'},
        {'z','8','%','d'},
        {'F','@','2','m'}
    };

    static boolean[][] visited = new boolean[4][4];
    static String contrasenaBacktracking = "";
    static int objetivoLongitud = 8;

    // algoritmo de backtracking
    static boolean generarBacktracking(int fila, int col) {
        if (fila < 0 || col < 0 || fila >= 4 || col >= 4) return false;
        if (visited[fila][col]) return false;

        if (contrasenaBacktracking.length() == objetivoLongitud) return true;

        visited[fila][col] = true;
        contrasenaBacktracking += grid[fila][col];

        int[][] movs = {{-1,0},{0,1},{1,0},{0,-1}}; // arriba, derecha, abajo, izquierda

        for (int[] m : movs) {
            if (generarBacktracking(fila + m[0], col + m[1])) return true;
        }

        // Retrocedemos (backtrack)
        contrasenaBacktracking =
                contrasenaBacktracking.substring(0, contrasenaBacktracking.length() - 1);
        visited[fila][col] = false;

        return false;
    }

    static String generarContrasenaLaberinto() {
        for (int i = 0; i < 4; i++)
            Arrays.fill(visited[i], false);

        contrasenaBacktracking = "";

        for (int f = 0; f < 4; f++) {
            for (int c = 0; c < 4; c++) {
                if (generarBacktracking(f, c)) {
                    return contrasenaBacktracking;
                }
            }
        }

        return "(No se pudo generar)";
    }


    // ------------------------------------------------------------
    //                        MAIN GUI
    // ------------------------------------------------------------
    public static void main(String[] args) {
        JFrame frame = new JFrame("Generador de Contraseñas - con búsqueda");
        frame.setSize(600, 450);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());

        JButton btnGenerar = new JButton("Generar Contraseña Aleatoria");
        JButton btnBacktracking = new JButton("Generar con Backtracking (Laberinto)");

        JLabel lblResultado = new JLabel("Haz clic en el botón para generar");
        JLabel lblOrdenada = new JLabel("");
        JLabel lblCombinaciones = new JLabel("");

        JTextField txtBuscar = new JTextField(20);
        JButton btnBuscar = new JButton("Buscar en Tabla Hash");
        JLabel lblBusquedaResultado = new JLabel("");

        JButton btnMostrarTabla = new JButton("Mostrar Tabla Hash");

        // ------------------------------------------------------------
        // BOTÓN CONTRASEÑA ALEATORIA
        // ------------------------------------------------------------
        btnGenerar.addActionListener((ActionEvent e) -> {
            String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                    + "abcdefghijklmnopqrstuvwxyz"
                    + "0123456789"
                    + "#$%&*";

            int longitud = 8;
            int cantidadCaracteres = caracteres.length();
            double combinaciones = Math.pow(cantidadCaracteres, longitud);

            StringBuilder contrasena = new StringBuilder();
            Random random = new Random();

            for (int i = 0; i < longitud; i++) {
                int indice = random.nextInt(cantidadCaracteres);
                contrasena.append(caracteres.charAt(indice));
            }

            lblResultado.setText("Contraseña: " + contrasena);

            char[] arreglo = contrasena.toString().toCharArray();
            Arrays.sort(arreglo);

            lblOrdenada.setText("Ordenada: " + new String(arreglo));
            lblCombinaciones.setText("Posibles combinaciones: " + String.format("%.0f", combinaciones));

            InsertResult res = insertarEnHash(contrasena.toString());
            if (res.inserted) {
                JOptionPane.showMessageDialog(frame,
                        "Insertado en tabla hash.\nÍndice: " + res.index +
                        "\nClave: " + contrasena +
                        "\nSondeos: " + res.probes);
            } else {
                JOptionPane.showMessageDialog(frame, "Tabla llena, no se pudo insertar.");
            }
        });

        // ------------------------------------------------------------
        // BOTÓN BACKTRACKING (LABERINTO)
        // ------------------------------------------------------------
        btnBacktracking.addActionListener((ActionEvent e) -> {
            String clave = generarContrasenaLaberinto();

            JOptionPane.showMessageDialog(frame,
                    "Contraseña generada por Backtracking:\n" + clave);

            insertarEnHash(clave);
        });


        // ------------------------------------------------------------
        // BÚSQUEDA
        // ------------------------------------------------------------
        btnBuscar.addActionListener((ActionEvent e) -> {
            String clave = txtBuscar.getText().trim();
            if (clave.isEmpty()) {
                lblBusquedaResultado.setText("Ingresa una cadena para buscar.");
                return;
            }

            SearchResult sres = buscarEnHash(clave);
            if (sres.found) {
                lblBusquedaResultado.setText("Encontrado en índice " + sres.index);
                JOptionPane.showMessageDialog(frame,
                        "Encontrado.\nÍndice: " + sres.index +
                                "\nClave: " + tablaHash[sres.index] +
                                "\nSondeos: " + sres.probes);
            } else {
                lblBusquedaResultado.setText("No encontrado");
                JOptionPane.showMessageDialog(frame,
                        "No encontrado.\nSondeos: " + sres.probes);
            }
        });

        // Mostrar tabla
        btnMostrarTabla.addActionListener((ActionEvent e) -> {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < tablaHash.length; i++) {
                sb.append(i).append(": ")
                        .append(tablaHash[i] == null ? "<vacío>" : tablaHash[i])
                        .append("\n");
            }
            JOptionPane.showMessageDialog(frame, sb.toString(), "Tabla Hash", JOptionPane.INFORMATION_MESSAGE);
        });

        // ------------------------------------------------------------
        // AGREGAR COMPONENTES A LA VENTANA
        // ------------------------------------------------------------
        frame.add(btnGenerar);
        frame.add(btnBacktracking);
        frame.add(lblResultado);
        frame.add(lblOrdenada);
        frame.add(lblCombinaciones);

        frame.add(new JLabel("Buscar en tabla hash:"));
        frame.add(txtBuscar);
        frame.add(btnBuscar);
        frame.add(lblBusquedaResultado);

        frame.add(btnMostrarTabla);

        frame.setVisible(true);
    }

    // ------------------------------------------------------------
    //                 HASHING
    // ------------------------------------------------------------
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

            if (tablaHash[idx].equals(valor)) {
                return new InsertResult(true, idx, probes);
            }
        }

        return new InsertResult(false, -1, probes);
    }

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

    static SearchResult buscarEnHash(String clave) {
        int size = tablaHash.length;
        int hash = hashIndex(clave, size);
        int probes = 0;

        for (int i = 0; i < size; i++) {
            int idx = (hash + i) % size;
            probes++;

            if (tablaHash[idx] == null) {
                return new SearchResult(false, -1, probes);
            }

            if (tablaHash[idx].equals(clave)) {
                return new SearchResult(true, idx, probes);
            }
        }
        return new SearchResult(false, -1, probes);
    }

    static int hashIndex(String key, int tableSize) {
        int raw = key.hashCode();
        int positive = raw & 0x7fffffff;
        return positive % tableSize;
    }
}