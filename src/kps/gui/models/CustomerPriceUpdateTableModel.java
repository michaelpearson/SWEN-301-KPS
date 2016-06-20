package kps.gui.models;

import kps.xml.adapters.DateAdapter;
import kps.xml.objects.CustomerPrice;
import kps.xml.objects.Route;
import kps.xml.objects.Simulation;

import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * This class is the table model for the update transport route page. It displays a list of unique transport routes
 * It also greys out discontinued routes.
 */
public class CustomerPriceUpdateTableModel extends AbstractTableModel {
    private static final String DATE = "Date";
    private static final String TO = "To";
    private static final String WEIGHT_COST = "Weight cost";
    private static final String VOLUME_COST = "Volume cost";
    private static final String PRIORITY = "Priority";

    private final Simulation simulation;
    private List<CustomerPrice> prices;
    private LinkedHashMap<String, FieldGetter> tableColumns = new LinkedHashMap<>();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat(DateAdapter.DATE_FORMAT);

    /**
     * This is a getter columns at a given row.
     */
    private interface FieldGetter {
        String getField(int row);
    }

    /**
     * This is the main constructor which sets up the table.
     * @param simulation the simulation to populate the table from
     */
    public CustomerPriceUpdateTableModel(Simulation simulation) {
        this.simulation = simulation;

        updateTable();

        tableColumns.put(DATE, row -> dateFormat.format(prices.get(row).getDate()));
        tableColumns.put(TO, row -> prices.get(row).getTo());
        tableColumns.put(PRIORITY, row -> prices.get(row).getPriority().toString());
        tableColumns.put(WEIGHT_COST, row -> String.valueOf(prices.get(row).getWeightCost()));
        tableColumns.put(VOLUME_COST, row -> String.valueOf(prices.get(row).getVolumeCost()));

    }

    /**
     * This fires the update method and redraws the table.
     */
    public void updateTable() {
        this.prices = simulation.getUniqueCustomerPrices();
        this.prices.sort((l, r) -> r.getDate().compareTo(l.getDate()));
    }

    @Override public int getRowCount() {
        return prices.size();
    }

    @Override public int getColumnCount() {
        return tableColumns.size();
    }

    @Override public String getColumnName(int columnIndex) {
        return new LinkedList<>(tableColumns.entrySet()).get(columnIndex).getKey();
    }

    @Override public Object getValueAt(int rowIndex, int columnIndex) {
        return tableColumns.get(getColumnName(columnIndex)).getField(rowIndex);
    }

    public void update(int row, Frame owner) {
        prices.get(row).update(owner);
    }

    public CustomerPrice getRow(int row) {
        return prices.get(row);
    }
}
