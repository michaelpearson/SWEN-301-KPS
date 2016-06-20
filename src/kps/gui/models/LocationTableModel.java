package kps.gui.models;

import kps.xml.objects.Location;
import kps.xml.objects.Mail;
import kps.xml.objects.Simulation;
import kps.xml.objects.enums.Priority;
import org.jetbrains.annotations.NotNull;

import javax.swing.table.AbstractTableModel;
import java.util.*;
import java.util.stream.Collectors;

public class LocationTableModel extends AbstractTableModel {
    private static final String LOCATION = "Location";
    private static final String DOMESTIC = "Domestic";
    private static final String AVAILABLE = "Available";

    private final Simulation simulation;
    private List<Location> locations = new ArrayList<>();
    private Map<String, FieldGetter> tableColumns = new LinkedHashMap<>();

    private interface FieldGetter {
        String getField(int row);
    }

    public LocationTableModel(@NotNull Simulation simulation) {
        this.simulation = simulation;
        updateTable();

        tableColumns.put(LOCATION, row -> locations.get(row).getName());
        tableColumns.put(DOMESTIC, row -> locations.get(row).isDomestic()? "Domestic" : "International");
        tableColumns.put(AVAILABLE, row -> locations.get(row).isAvailable()? "Yes" : "No");
    }

    private void updateTable() {
        this.locations = simulation.getLocations();
        this.locations.sort((l, r) -> r.getDate().compareTo(l.getDate()));
    }

    @Override
    public int getRowCount() {
        return locations.size();
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
}
