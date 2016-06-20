package kps.gui.windows;

import kps.gui.models.CriticalRoutesTableModel;
import kps.gui.models.LocationTableModel;
import kps.xml.objects.Simulation;
import org.jdesktop.swingx.JXTable;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

class LocationWindow extends JFrame {
    private final @NotNull Simulation simulation;

    LocationWindow(@NotNull Simulation simulation) {
        this.simulation = simulation;
        buildGui();
        setSize(700, 400);
        setTitle("Locations");
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void buildGui() {
        JPanel outerPanel = new JPanel();
        outerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        outerPanel.setLayout(new BorderLayout());

        JPanel tablePanel = new JPanel();
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 5));
        tablePanel.setLayout(new BorderLayout());

        LocationTableModel tableModel = new LocationTableModel(simulation);
        JXTable table = new JXTable(tableModel);
        table.setFillsViewportHeight(true);
        table.packAll();
        table.setBorder(BorderFactory.createEmptyBorder());
        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);
        outerPanel.add(tablePanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 5));
        GridLayout buttonLayout = new GridLayout(5, 1);
        buttonLayout.setVgap(10);
        buttonPanel.setLayout(buttonLayout);

        JButton addButton = new JButton("Add Location");
        addButton.addActionListener(e -> dispose());
        buttonPanel.add(addButton);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        buttonPanel.add(closeButton);

        outerPanel.add(buttonPanel, BorderLayout.EAST);
        add(outerPanel);
    }
}
