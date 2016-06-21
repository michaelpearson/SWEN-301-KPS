package kps.gui.windows.form.dialogs;

import kps.business.RouteCalculator;
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

/**
 * The mail item dialog. To create new mail delivery events or update existing ones.
 */
public class MailDialog extends FormDialog {

    private @NotNull Mail mailDeliveryEvent;
    private boolean isInDocument;
    private @Nullable CalculatedRoute calculatedRoute = null;

    /**
     * Field names so we can retrieve the values later.
     */
    private enum FieldNames {
        DayOfWeek,
        LocationTo,
        LocationFrom,
        Weight,
        Volume,
        Priority
    }

    /**
     * Constructor if you don't have an old mail delivery for your dialog.
     * @param owner the frames owner
     * @param simulation the simulation to work from
     */
    public MailDialog(@Nullable Frame owner, @NotNull Simulation simulation) {
        this(owner, simulation, null);
    }

    /**
     * Constructor for your dialog if you have a previous mail event
     * @param owner the frames owner
     * @param simulation the simulation to work from
     * @param previousMailEvent the mail event to update (or null to create a new one)
     */
    public MailDialog(@Nullable Frame owner, @NotNull Simulation simulation, @Nullable Mail previousMailEvent) {
        super(owner, previousMailEvent == null ? "New mail Delivery" : "Edit mail delivery", true, simulation);
        this.isInDocument = previousMailEvent != null;
        this.mailDeliveryEvent = previousMailEvent == null ? new Mail(simulation) : previousMailEvent;
        buildDialog();
        setVisible(true);
    }

    @Override protected void initializeForm(@NotNull FormBuilder builder) {
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

        Mail mail = new Mail(simulation);
        mail.setTo(to);
        mail.setFrom(from);
        mail.setPriority(priority);
        mail.setWeight((int)getValue(FieldNames.Weight));
        mail.setVolume((int)getValue(FieldNames.Volume));

        calculatedRoute = new RouteCalculator(simulation, mail).buildCalculatedRoute();

        if(calculatedRoute == null) {
            JOptionPane.showMessageDialog(
                    this,
                    String.format("Sorry, no route between %s and %s using %s was found. Please change your origin and destination and try again", from, to, priority.toString()),
                    "No viable route", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        int price = mail.calculatePrice();
        if(price == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    String.format("Sorry there is no price between %s and %s", from, to),
                    "No viable route", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        calculatedRoute.setCalculatedPrice(price);

        return true;
    }

    protected void save() {
        if (!validateFields()) {
            return;
        }
        assert(calculatedRoute != null); //Validate fields grantees that calculatedRoute != null

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
