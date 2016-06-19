package kps.gui.models;

import kps.xml.adapters.DateAdapter;
import kps.xml.objects.CustomerPrice;
import kps.xml.objects.Mail;
import kps.xml.objects.Route;
import kps.xml.objects.Simulation;
import kps.xml.objects.abstracts.BusinessEvent;

import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * This class is the model for the home screens table. It adapts to the simulation object that is passed in.
 */
public class BusinessEventsTableModel extends AbstractTableModel {
    private static final String DATE = "Date";
    private static final String EVENT_TYPE = "Event type";
    private static final String FROM = "From";
    private static final String TO = "To";
    private static final String TRANSPORT_FIRM = "Company";
    private static final String PRIORITY = "Priority/Transport type";
    private final Simulation simulation;
    private List<BusinessEvent> businessEvents;
    private LinkedHashMap<String, FieldGetter> tableColumns = new LinkedHashMap<>();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat(DateAdapter.DATE_FORMAT);


    /**
     * Interface which is used to get a specific field at a given row
     */
    private interface FieldGetter {
        String getField(int row);
    }

    /**
     * Main constructor which sets up the columns
     * @param simulation the simulation object which the table will display.
     */
    public BusinessEventsTableModel(Simulation simulation) {
        this.simulation = simulation;
        this.businessEvents = simulation.getAllBusinessEvents();
        tableColumns.put(EVENT_TYPE, row -> businessEvents.get(row).getEventType());
        tableColumns.put(DATE, row -> dateFormat.format(businessEvents.get(row).getDate()));
        tableColumns.put(FROM, row -> businessEvents.get(row).getFrom());
        tableColumns.put(TO, row -> businessEvents.get(row).getTo());
        tableColumns.put(TRANSPORT_FIRM, row -> {
            BusinessEvent event = businessEvents.get(row);
            return Route.class.isAssignableFrom(event.getClass()) ? ((Route)event).getCompany() : "N/A";
        });
        tableColumns.put(PRIORITY, row -> {
            BusinessEvent event = businessEvents.get(row);
            if(Mail.class.isAssignableFrom(event.getClass())) {
                return String.valueOf(((Mail)event).getPriority());
            } else if (Route.class.isAssignableFrom(event.getClass())) {
                return ((Route)event).getTransportType().toString();
            } else if (CustomerPrice.class.isAssignableFrom(event.getClass())) {
                return ((CustomerPrice)event).getPriority().toString();
            } else {
                return "N/A";
            }
        });
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
        updateTable();
    }

    public void updateTable(){
        this.businessEvents = simulation.getAllBusinessEvents();
    }
}
