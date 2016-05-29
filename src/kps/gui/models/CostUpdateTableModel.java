package kps.gui.models;

import kps.xml.adapters.DateAdapter;
import kps.xml.objects.Simulation;
import kps.xml.objects.Cost;
import kps.xml.objects.abstracts.BusinessEvent;

import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class CostUpdateTableModel extends AbstractTableModel {
    private static final String DATE = "Date";
    private static final String COMPANY = "Company name";
    private static final String FROM = "From";
    private static final String TO = "To";
    private static final String TYPE = "Transport type";
    private final Simulation simulation;
    private List<Cost> costEvents;
    private LinkedHashMap<String, FieldGetter> tableColumns = new LinkedHashMap<>();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat(DateAdapter.DATE_FORMAT);



    private interface FieldGetter {
        String getField(int row);
    }

    public CostUpdateTableModel(Simulation simulation) {
        this.simulation = simulation;
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

        tableColumns.put(COMPANY, row -> costEvents.get(row).getCompany());
        tableColumns.put(DATE, row -> dateFormat.format(costEvents.get(row).getDate()));
        tableColumns.put(FROM, row -> costEvents.get(row).getFrom().getName());
        tableColumns.put(TO, row -> costEvents.get(row).getTo().getName());
        tableColumns.put(TYPE, row -> costEvents.get(row).getTransportType().toString());
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
}
