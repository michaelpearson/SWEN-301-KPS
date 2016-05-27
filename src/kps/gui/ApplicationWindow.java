package kps.gui;

import kps.gui.windows.AddRoute;
import kps.xml.objects.Simulation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ApplicationWindow extends JFrame {
    private final Simulation simulation;
    public static final String APPLICATION_NAME = "Swen 301 - KPS";

    public ApplicationWindow(GraphicsConfiguration gc) {
        throw new RuntimeException("Please use the correct constructor");
    }

    public ApplicationWindow(String title) throws HeadlessException {
        throw new RuntimeException("Please use the correct constructor");
    }

    public ApplicationWindow(String title, GraphicsConfiguration gc) {
        throw new RuntimeException("Please use the correct constructor");
    }

    public ApplicationWindow(Simulation simulation) {
        this.simulation = simulation;

        setSize(700, 500);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle(APPLICATION_NAME);

        JButton addRouteButton = new JButton("Add route");
        addRouteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddRoute(ApplicationWindow.this, simulation);
            }
        });
        add(addRouteButton);



        setVisible(true);
    }
}
