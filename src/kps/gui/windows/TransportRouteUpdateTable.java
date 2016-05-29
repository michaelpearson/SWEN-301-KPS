package kps.gui.windows;

import kps.gui.models.CostUpdateTableModel;
import kps.xml.objects.Simulation;
import org.jdesktop.swingx.JXTable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TransportRouteUpdateTable extends JDialog {
    private Simulation simulation;
    private Frame owner;

    public TransportRouteUpdateTable(Frame owner, Simulation simulation) {
        super(owner, true);
        this.simulation = simulation;
        this.owner = owner;
        buildTable();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void buildTable() {
        setSize(900, 300);
        JPanel tablePanel = new JPanel();
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        tablePanel.setLayout(new BorderLayout());
        JXTable table = new JXTable(new CostUpdateTableModel(simulation));
        table.setFillsViewportHeight(true);
        table.packColumn(0, 6);
        table.setBorder(BorderFactory.createEmptyBorder());
        table.addMouseListener(new MouseAdapter() {
            @Override public void mousePressed(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    JXTable table = (JXTable) e.getSource();
                    int row = table.convertRowIndexToModel(table.getSelectedRow());
                    ((CostUpdateTableModel)table.getModel()).update(row, owner);
                }
            }
        });
        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);
        add(tablePanel, BorderLayout.CENTER);
    }
}
