package kps.gui.windows;

import kps.gui.models.CustomerPriceUpdateTableModel;
import kps.gui.models.RouteUpdateTableModel;
import kps.xml.objects.Simulation;
import org.jdesktop.swingx.JXTable;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class CustomerPriceUpdateTable extends JDialog {
    private Simulation simulation;
    private Frame owner;

    public CustomerPriceUpdateTable(Frame owner, Simulation simulation) {
        super(owner, true);
        this.simulation = simulation;
        this.owner = owner;
        buildTable();
        setLocationRelativeTo(null);
        setTitle("View all prices");
        setVisible(true);
    }

    private void buildTable() {
        setSize(900, 300);
        JPanel tablePanel = new JPanel();
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        tablePanel.setLayout(new BorderLayout());

        CustomerPriceUpdateTableModel model = new CustomerPriceUpdateTableModel(simulation);
        JTable table = new JXTable(model);

        table.setFillsViewportHeight(true);
        table.setBorder(BorderFactory.createEmptyBorder());
        table.addMouseListener(new MouseAdapter() {
            @Override public void mousePressed(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    JXTable table = (JXTable) e.getSource();
                    int row = table.convertRowIndexToModel(table.getSelectedRow());
                    ((CustomerPriceUpdateTableModel)table.getModel()).update(row, owner);
                    model.updateTable();
                    model.fireTableDataChanged();
                }
            }
        });
        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);
        add(tablePanel, BorderLayout.CENTER);
    }
}
