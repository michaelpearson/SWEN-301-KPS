package kps.gui.windows.form.dialogs;

import kps.xml.objects.CalculatedRoute;
import org.jdesktop.swingx.JXTable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

class RouteConfirmationDialog extends JDialog {

    private final List<CalculatedRoute> possibleRoutes;
    private CalculatedRoute selectedRoute = null;

    RouteConfirmationDialog(@Nullable Frame owner, @NotNull Set<CalculatedRoute> possibleRoutes) {
        super(owner, "Confirm route", true);
        this.possibleRoutes = new ArrayList<>();
        this.possibleRoutes.addAll(possibleRoutes);
        this.possibleRoutes.sort((l, r) -> l.getRoutes().size() > r.getRoutes().size() ? 1 : l.getRoutes().size() == r.getRoutes().size() ? 0 : -1);
        if(this.possibleRoutes.size() == 1) {
            selectedRoute = this.possibleRoutes.get(0);
        } else {
            buildDialog();
            setLocationRelativeTo(null);
            setVisible(true);
        }
    }

    private void buildDialog() {
        setLayout(new BorderLayout());
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(0, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JXTable table = new JXTable(new RouteSelectionTableModel());

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        add(panel, BorderLayout.CENTER);

        JButton okbutton;
        panel.add(okbutton = new JButton("Ok"), BorderLayout.SOUTH);

        okbutton.addActionListener(e -> {
            if(table.getSelectedRow() == -1) {
                JOptionPane.showMessageDialog(this, "Please select a single route.", "No route selected", JOptionPane.WARNING_MESSAGE);
                return;
            }
            selectedRoute = possibleRoutes.get(table.getSelectedRow());
            setVisible(false);
        });
        setSize(500, 300);
        table.packAll();
    }

    private class RouteSelectionTableModel extends AbstractTableModel {

        @Override public int getRowCount() {
            return possibleRoutes.size();
        }

        @Override public int getColumnCount() {
            return 2;
        }

        @Override public Object getValueAt(int rowIndex, int columnIndex) {
            if(columnIndex == 0) {
                return possibleRoutes.get(rowIndex).getRoutes().size();
            }
            return possibleRoutes.get(rowIndex).toString();
        }

        @Override public String getColumnName(int column) {
            switch(column) {
                case 0:
                    return "Hops";
                case 1:
                default:
                    return "Calculated Route";
            }
        }
    }

    CalculatedRoute getConfirmedRoute() {
        return selectedRoute;
    }
}
