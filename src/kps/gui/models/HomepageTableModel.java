package kps.gui.models;

import kps.xml.objects.Simulation;

import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import java.util.HashMap;
import java.util.Map;

public class HomepageTableModel extends AbstractTableModel {
    private static final String DATE = "Date";


    private final Simulation simulation;

    private Map<String, FieldGetter> tableColumns = new HashMap<>();

    private interface FieldGetter {
        String getField(int row);
    }

    public HomepageTableModel(Simulation simulation) {
        this.simulation = simulation;

        //tableColumns.put(DATE, (r) -> simulation.get);
    }

    @Override
    public int getRowCount() {
        return 1;
    }

    @Override
    public int getColumnCount() {
        return 5;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return "Test " + columnIndex;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return "HI";
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {}

    @Override
    public void addTableModelListener(TableModelListener l) {}

    @Override
    public void removeTableModelListener(TableModelListener l) {}
}
