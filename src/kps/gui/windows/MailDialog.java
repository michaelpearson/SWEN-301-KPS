package kps.gui.windows;

import kps.gui.FormDialog;
import kps.xml.objects.Mail;
import kps.xml.objects.Simulation;
import kps.xml.objects.enums.DayOfWeek;
import kps.xml.objects.enums.Priority;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

public class MailDialog extends FormDialog {
    private @NotNull Mail route;
    private boolean isInDocument;

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
        this.route = previousMailEvent == null ? new Mail(simulation) : previousMailEvent;

        buildDialog();
        setVisible(true);
    }

    @Override
    protected JComponent[][] getAllFields() {
        return new JComponent[][]{
                getField(FieldNames.DayOfWeek, "Day of the week", route.getDay() == null ? DayOfWeek.Monday : route.getDay()),
                getField(FieldNames.LocationFrom, "Location from", route.getFrom()),
                getField(FieldNames.LocationTo, "Location to", route.getTo()),
                getField(FieldNames.Weight, "Weight", route.getWeight()),
                getField(FieldNames.Volume, "Volume", route.getVolume()),
                getField(FieldNames.Priority, "Priority", route.getPriority() == null ? Priority.DOMESTIC_STANDARD : route.getPriority())
        };
    }

    protected void save() {
        Set<Map.Entry<Object, Object>> entries = getAllValues().entrySet();
        for(Map.Entry<Object, Object> entry : entries) {
            switch((FieldNames)entry.getKey()) {
                case LocationTo:
                    route.setTo((String)entry.getValue());
                    break;
                case LocationFrom:
                    route.setFrom((String)entry.getValue());
                    break;
                case DayOfWeek:
                    route.setDay(Arrays.asList(DayOfWeek.values()).stream().filter(v -> v.equals((DayOfWeek)entry.getValue())).findFirst().get());
                    break;
                case Priority:
                    route.setPriority(Arrays.asList(Priority.values()).stream().filter(v -> v.equals((Priority)entry.getValue())).findFirst().get());
                    break;
                case Volume:
                    route.setVolume((Integer)entry.getValue());
                    break;
                case Weight:
                    route.setWeight((Integer)entry.getValue());
                    break;
                default:
                    throw new RuntimeException("Unknown field");
            }
        }
        if(!isInDocument) {
            simulation.getMail().add(route);
        }
        cancel();
    }

    protected void cancel() {
        dispose();
    }

}
