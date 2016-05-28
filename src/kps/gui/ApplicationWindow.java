package kps.gui;

import kps.gui.windows.AddMail;
import kps.gui.windows.AddRoute;
import kps.gui.windows.Login;
import kps.xml.objects.Simulation;

import javax.swing.*;
import java.awt.*;

public class ApplicationWindow extends JFrame {
    private final Simulation simulation;
    private final Login.UserName user;

    @SuppressWarnings("WeakerAccess")
    public static final String APPLICATION_NAME = "Swen 301 - KPS";

    public ApplicationWindow(Simulation simulation, Login.UserName user) {
        this.simulation = simulation;
        this.user = user;

        setSize(700, 500);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle(APPLICATION_NAME);
        setLayout(new GridLayout(0, 1));

        JButton addRouteButton = new JButton("Add route");
        addRouteButton.addActionListener(e -> new AddRoute(ApplicationWindow.this, simulation));
        add(addRouteButton);

        JButton addMailDeliveryButton = new JButton("Add mail delivery");
        addMailDeliveryButton.addActionListener(e -> new AddMail(ApplicationWindow.this, simulation));
        add(addMailDeliveryButton);

        setLocationRelativeTo(null);
        setVisible(true);
    }
}
