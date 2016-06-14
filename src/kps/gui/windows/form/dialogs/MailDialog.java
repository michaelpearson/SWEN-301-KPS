package kps.gui.windows.form.dialogs;

import kps.gui.windows.form.FormBuilder;
import kps.gui.windows.form.FormDialog;
import kps.xml.objects.CalculatedRoute;
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

    @Override protected void initializeForm(FormBuilder builder) {
        builder.addEnumField(FieldNames.DayOfWeek, "Day of the week", mailDeliveryEvent.getDay(), DayOfWeek.class);
        builder.addLocationField(FieldNames.LocationFrom, "Location from", mailDeliveryEvent.getFrom());
        builder.addLocationField(FieldNames.LocationTo, "Location to", mailDeliveryEvent.getTo());
        builder.addIntegerField(FieldNames.Weight, "Weight (grams)", mailDeliveryEvent.getWeight());
        builder.addIntegerField(FieldNames.Volume, "Volume (cm^3)", mailDeliveryEvent.getVolume());
        builder.addEnumField(FieldNames.Priority, "Priority", mailDeliveryEvent.getPriority(), Priority.class);
    }

    @Override
    protected boolean validateFields() {
        boolean valid = super.validateFields();
        if(!valid) {
            return false;
        }
        String from = (String) getValue(FieldNames.LocationFrom);
        String to = (String) getValue(FieldNames.LocationTo);
        Priority priority = (Priority) getValue(FieldNames.Priority);
        Set<CalculatedRoute> routes = simulation.buildCalculatedRoute(from, to, priority);
        if(routes == null) {
            JOptionPane.showMessageDialog(
                    this,
                    String.format("Sorry, no route between %s and %s using %s was found. Please change your origin and destination and try again", from, to, priority.toString()),
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
        Map<Object, Object> entries = getAllFieldValues();

        mailDeliveryEvent.setCalculatedRoute(calculatedRoute);
        mailDeliveryEvent.setTo((String)entries.get(FieldNames.LocationTo));
        mailDeliveryEvent.setFrom((String)entries.get(FieldNames.LocationFrom));
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
