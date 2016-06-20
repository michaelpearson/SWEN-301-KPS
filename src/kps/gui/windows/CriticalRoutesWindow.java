package kps.gui.windows;

import kps.gui.models.CriticalRoutesTableModel;
import kps.xml.objects.Simulation;
import org.jdesktop.swingx.JXTable;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

class CriticalRoutesWindow extends JFrame {
    private final @NotNull Simulation simulation;

    CriticalRoutesWindow(@NotNull Simulation simulation) {
        this.simulation = simulation;
        buildGui();
        setSize(700, 200);
        setTitle("Critical Routes");
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

        CriticalRoutesTableModel tableModel = new CriticalRoutesTableModel(simulation);
        JXTable table = new JXTable(tableModel);
        table.setFillsViewportHeight(true);
        table.packAll();
        table.setBorder(BorderFactory.createEmptyBorder());
        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);
        outerPanel.add(tablePanel, BorderLayout.CENTER);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        outerPanel.add(closeButton, BorderLayout.SOUTH);
        add(outerPanel);
    }
}
