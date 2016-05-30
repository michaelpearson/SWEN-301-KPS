package kps.gui.windows.dialogs;

import kps.gui.FormDialog;
import kps.xml.objects.CalculatedRoute;
import kps.xml.objects.Location;
import kps.xml.objects.Mail;
import kps.xml.objects.Simulation;
import kps.xml.objects.enums.DayOfWeek;
import kps.xml.objects.enums.Priority;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.Set;

public class MailDialog extends FormDialog {
    private @NotNull Mail mailDeliveryEvent;
    private boolean isInDocument;
    private @Nullable CalculatedRoute calculatedRoute = null;

    private enum FieldNames {
        DayOfWeek,
        LocationTo,
        LocationFrom,
        Weight,
        Volume,
        Priority
    }

    public MailDialog(Frame owner, Simulation simulation) {
        this(owner, simulation, null);
    }

    public MailDialog(Frame owner, Simulation simulation, @Nullable Mail previousMailEvent) {
        super(owner, previousMailEvent == null ? "New mail Delivery" : "Edit mail delivery", true, simulation);
        this.isInDocument = previousMailEvent != null;
        this.mailDeliveryEvent = previousMailEvent == null ? new Mail(simulation) : previousMailEvent;
        buildDialog();
        setVisible(true);
    }

    @Override
    protected JComponent[][] getAllFields() {
        return new JComponent[][]{
                getField(FieldNames.DayOfWeek, "Day of the week", mailDeliveryEvent.getDay(), DayOfWeek.class),
                getField(FieldNames.LocationFrom, "Location from", mailDeliveryEvent.getFrom(), Location.class),
                getField(FieldNames.LocationTo, "Location to", mailDeliveryEvent.getTo(), Location.class),
                getField(FieldNames.Weight, "Weight (grams)", mailDeliveryEvent.getWeight(), Integer.class),
                getField(FieldNames.Volume, "Volume (cm^3)", mailDeliveryEvent.getVolume(), Integer.class),
                getField(FieldNames.Priority, "Priority", mailDeliveryEvent.getPriority(), Priority.class)
        };
    }

    @Override
    protected boolean validateFields() {
        boolean valid = super.validateFields();
        if(!valid) {
            return false;
        }
        Location from = (Location)getComponentValue(FieldNames.LocationFrom);
        Location to = (Location)getComponentValue(FieldNames.LocationTo);
        Priority priority = (Priority)getComponentValue(FieldNames.Priority);
        Set<CalculatedRoute> routes = simulation.buildCalculatedRoute(from, to, priority);
        if(routes == null) {
            JOptionPane.showMessageDialog(
                    this,
                    String.format("Sorry, no route between %s and %s using %s was found. Please change your origin and destination and try again", from.toString(), to.toString(), priority.toString()),
                    "No viable route", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        calculatedRoute = new RouteConfirmationDialog(null, routes).getConfirmedRoute();
        return calculatedRoute != null;
    }

    protected void save() {
        if (!validateFields()) {
            return;
        }
        Map<Object, Object> entries = getAllValues();

        mailDeliveryEvent.setCalculatedRoute(calculatedRoute);
        mailDeliveryEvent.setTo((Location)entries.get(FieldNames.LocationTo));
        mailDeliveryEvent.setFrom((Location)entries.get(FieldNames.LocationFrom));
        mailDeliveryEvent.setDay((DayOfWeek)entries.get(FieldNames.DayOfWeek));
        mailDeliveryEvent.setPriority((Priority)entries.get(FieldNames.Priority));
        mailDeliveryEvent.setVolume((Integer)entries.get(FieldNames.Volume));
        mailDeliveryEvent.setWeight((Integer)entries.get(FieldNames.Weight));

        if(!isInDocument) {
            simulation.getMail().add(mailDeliveryEvent);
        }
        cancel();
    }

    protected boolean cancel() {
        dispose();
        return true;
    }

}
