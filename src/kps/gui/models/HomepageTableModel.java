package kps.gui.models;

import kps.xml.adapters.DateAdapter;
import kps.xml.objects.abstracts.BusinessEvent;
import kps.xml.objects.abstracts.ModelObject;
import kps.xml.objects.Simulation;

import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class HomepageTableModel extends AbstractTableModel {
    private static final String DATE = "Date";
    private static final String EVENT_TYPE = "Event type";
    private static final String FROM = "From";
    private static final String TO = "To";
    private final Simulation simulation;
    private List<BusinessEvent> businessEvents;
    private LinkedHashMap<String, FieldGetter> tableColumns = new LinkedHashMap<>();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat(DateAdapter.DATE_FORMAT);



    private interface FieldGetter {
        String getField(int row);
    }

    public HomepageTableModel(Simulation simulation) {
        this.simulation = simulation;
        this.businessEvents = simulation.getAllBusinessEvents();
        tableColumns.put(EVENT_TYPE, row -> businessEvents.get(row).getEventType());
        tableColumns.put(DATE, row -> dateFormat.format(businessEvents.get(row).getDate()));
        tableColumns.put(FROM, row -> businessEvents.get(row).getFrom().getName());
        tableColumns.put(TO, row -> businessEvents.get(row).getTo().getName());
    }

    @Override
    public int getRowCount() {
        return businessEvents.size();
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
        businessEvents.get(row).edit(owner);
    }
}