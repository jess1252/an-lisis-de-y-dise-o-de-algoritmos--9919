/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package mycompany.generarcontrasenagui;

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

    public static void main(String[] args) {
        // Crear ventana
        JFrame frame = new JFrame("Generador de Contraseñas");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new FlowLayout());

        // Botón para generar contraseña
        JButton btnGenerar = new JButton("Generar Contraseña");
        JLabel lblResultado = new JLabel("Haz clic en el botón para generar");
        JLabel lblOrdenada = new JLabel("");

        btnGenerar.addActionListener((ActionEvent e) -> {
            // Configuración de caracteres
            String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                    + "abcdefghijklmnopqrstuvwxyz"
                    + "0123456789"
                    + "#$%&*";
            
            int longitud = 8;
            StringBuilder contrasena = new StringBuilder();
            Random random = new Random();
            
            for (int i = 0; i < longitud; i++) {
                int indice = random.nextInt(caracteres.length());
                contrasena.append(caracteres.charAt(indice));
            }
            
            // Mostrar contraseña generada
            lblResultado.setText("Contraseña: " + contrasena);
            
            // Ordenar la contraseña
            char[] arreglo = contrasena.toString().toCharArray();
            Arrays.sort(arreglo);
            lblOrdenada.setText("Ordenada: " + new String(arreglo));
        });

        // Agregar componentes a la ventana
        frame.add(btnGenerar);
        frame.add(lblResultado);
        frame.add(lblOrdenada);

        // Mostrar ventana con jar
        frame.setVisible(true);
    }
}
