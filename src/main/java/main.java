package main.java;

import javax.swing.SwingUtilities;

import main.java.Controllers.ScreenController;



public class main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ScreenController().setVisible(true);
            }
        });
    }
}
