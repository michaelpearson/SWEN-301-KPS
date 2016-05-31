package kps.gui.models;

import kps.xml.adapters.DateAdapter;
import kps.xml.objects.Simulation;
import kps.xml.objects.Route;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

public class RouteUpdateTableModel extends AbstractTableModel {
    private static final String DATE = "Date";
    private static final String COMPANY = "Company name";
    private static final String FROM = "From";
    private static final String TO = "To";
    private static final String TYPE = "Transport type";
    private static final String STATUS = "Route status";
    private final Simulation simulation;
    private List<Route> routes;
    private LinkedHashMap<String, FieldGetter> tableColumns = new LinkedHashMap<>();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat(DateAdapter.DATE_FORMAT);



    private interface FieldGetter {
        String getField(int row);
    }

    public RouteUpdateTableModel(Simulation simulation) {
        this.simulation = simulation;

        updateTable();

        tableColumns.put(DATE, row -> dateFormat.format(routes.get(row).getDate()));
        tableColumns.put(STATUS, row -> routes.get(row).isDiscontinued() ? "Discontinued" : "Active");
        tableColumns.put(COMPANY, row -> routes.get(row).getCompany());
        tableColumns.put(TYPE, row -> routes.get(row).getTransportType().toString());
        tableColumns.put(FROM, row -> routes.get(row).getFrom().getName());
        tableColumns.put(TO, row -> routes.get(row).getTo().getName());
    }

    public void updateTable() {
        this.routes = simulation.getUniqueRoutes();
        this.routes.sort((l, r) -> r.getDate().compareTo(l.getDate()));
    }

    @Override
    public int getRowCount() {
        return routes.size();
    }

    @Override
    public int getColumnCount() {
        return tableColumns.size();
    }

    @Override
    public String getColumnName(int columnIndex) {
        return new LinkedList<>(tableColumns.entrySet()).get(columnIndex).getKey();
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return tableColumns.get(getColumnName(columnIndex)).getField(rowIndex);
    }

    public void edit(int row, Frame owner) {
        routes.get(row).edit(owner);
    }

    public void update(int row, Frame owner) {
        routes.get(row).update(owner);
    }

    public TableCellRenderer getRenderer() {
        return new RouteCellRenderer();
    }

    private class RouteCellRenderer extends DefaultTableCellRenderer {
        private Color greyedOut = Color.decode("#333333");

        @Override public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if(row == 0){
                cellComponent.setBackground(Color.YELLOW);
            } else if ( row == 1){
                cellComponent.setBackground(Color.GRAY);
            } else {
                cellComponent.setBackground(Color.CYAN);
            }
            return cellComponent;
        }
    }

    public Route getRow(int row) {
        return routes.get(row);
    }
}
