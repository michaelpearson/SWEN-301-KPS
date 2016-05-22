package gui;

import javax.swing.*;

public class ApplicationWindow extends JFrame {
    public static final String APPLICATION_NAME = "Swen 301 - KPS";

    public ApplicationWindow() {
        setSize(700, 500);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle(APPLICATION_NAME);
        setVisible(true);
    }
}
