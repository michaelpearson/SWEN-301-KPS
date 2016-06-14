package kps.gui.models;

import kps.xml.adapters.DateAdapter;
import kps.xml.objects.Route;
import kps.xml.objects.Simulation;

import javax.swing.table.AbstractTableModel;
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
        tableColumns.put(FROM, row -> routes.get(row).getFrom());
        tableColumns.put(TO, row -> routes.get(row).getTo());
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

    public void update(int row, Frame owner) {
        routes.get(row).update(owner);
    }

    public Route getRow(int row) {
        return routes.get(row);
    }
}
