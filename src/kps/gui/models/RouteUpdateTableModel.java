package kps.gui.models;

import kps.xml.adapters.DateAdapter;
import kps.xml.objects.Simulation;
import kps.xml.objects.Cost;
import kps.xml.objects.abstracts.BusinessEvent;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class RouteUpdateTableModel extends AbstractTableModel {
    private static final String DATE = "Date";
    private static final String COMPANY = "Company name";
    private static final String FROM = "From";
    private static final String TO = "To";
    private static final String TYPE = "Transport type";
    private static final String STATUS = "Route status";
    private final Simulation simulation;
    private List<Cost> costEvents;
    private LinkedHashMap<String, FieldGetter> tableColumns = new LinkedHashMap<>();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat(DateAdapter.DATE_FORMAT);



    private interface FieldGetter {
        String getField(int row);
    }

    public RouteUpdateTableModel(Simulation simulation) {
        this.simulation = simulation;

        updateTable();

        tableColumns.put(DATE, row -> dateFormat.format(costEvents.get(row).getDate()));
        tableColumns.put(STATUS, row -> costEvents.get(row).isDiscontinued() ? "Discontinued" : "Active");
        tableColumns.put(COMPANY, row -> costEvents.get(row).getCompany());
        tableColumns.put(TYPE, row -> costEvents.get(row).getTransportType().toString());
        tableColumns.put(FROM, row -> costEvents.get(row).getFrom().getName());
        tableColumns.put(TO, row -> costEvents.get(row).getTo().getName());
    }

    public void updateTable() {
        this.costEvents = simulation.getCosts();
        List<Cost> ce = new ArrayList<>();
        for(Cost c1 : costEvents) {
            boolean allBefore = true;
            for(Cost c2 : costEvents) {
                if(c1 == c2 || !c1.getCompany().equals(c2.getCompany()) || c1.getTransportType() != c2.getTransportType()) {
                    continue;
                }
                if(!c1.getDate().after(c2.getDate())) {
                    allBefore = false;
                }
            }
            if(allBefore) {
                ce.add(c1);
            }
        }
        this.costEvents = ce;
        this.costEvents.sort((l, r) -> r.getDate().compareTo(l.getDate()));
    }

    @Override
    public int getRowCount() {
        return costEvents.size();
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
        costEvents.get(row).edit(owner);
    }

    public void update(int row, Frame owner) {
        costEvents.get(row).update(owner);
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

    public Cost getRow(int row) {
        return costEvents.get(row);
    }
}
