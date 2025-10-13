/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.generarcontrasenagui;

/**
 *
 * @author LAB-USR-AREQUIPA
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.Arrays;

public class GenerarContrasenaGUI {
    static String[] tablaHash = new String[10];

    public static void main(String[] args) {
        // Crear las ventanas
        JFrame frame = new JFrame("Generador de Contraseñas");
        frame.setSize(400, 350);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());

        // Botón para generar las contraseña
        JButton btnGenerar = new JButton("Generar Contraseña");
        JLabel lblResultado = new JLabel("Haz clic en el botón para generar");
        JLabel lblOrdenada = new JLabel("");
        JLabel lblCombinaciones = new JLabel("");

        btnGenerar.addActionListener((ActionEvent e) -> {
            // Configuraciónes de los caracteres
            String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                    + "abcdefghijklmnopqrstuvwxyz"
                    + "0123456789"
                    + "#$%&*";

            int longitud = 8;
            int cantidadCaracteres = caracteres.length(); // n
            double combinaciones = Math.pow(cantidadCaracteres, longitud); // n^k

            // Generar contraseña +
            StringBuilder contrasena = new StringBuilder();
            Random random = new Random();

            for (int i = 0; i < longitud; i++) {
                int indice = random.nextInt(cantidadCaracteres);
                contrasena.append(caracteres.charAt(indice));
            }

            // Mostrar las contraseñass generadas
            lblResultado.setText("Contraseña: " + contrasena);

            // Ordenar las contraseñas
            char[] arreglo = contrasena.toString().toCharArray();
            Arrays.sort(arreglo);
            lblOrdenada.setText("Ordenada: " + new String(arreglo));

            // Mostrar combinaciones posibles
            lblCombinaciones.setText("Posibles combinaciones: " + String.format("%.0f", combinaciones));
        // === Nuevo apartado Inserción en tablas hash (dispersión) ===
            int hash = Math.abs(contrasena.toString().hashCode()) % tablaHash.length;
            tablaHash[hash] = contrasena.toString();

            // Mostrar dónde se guardó las contraseñas
            JOptionPane.showMessageDialog(frame,
                    "La contraseña se insertó en la tabla hash.\nÍndice: " + hash +
                    "\nValor: " + contrasena);
        });

        // Agregar componentes a la ventana
        frame.add(btnGenerar);
        frame.add(lblResultado);
        frame.add(lblOrdenada);
        frame.add(lblCombinaciones);

        // Mostrar ventana
        frame.setVisible(true);
    }
}
//nuevo aparto usando la herramienta hashing de busqueda externa 
 Public void 