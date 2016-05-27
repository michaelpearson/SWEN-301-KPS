package kps.gui.windows;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import kps.gui.FormDialog;
import kps.xml.objects.Cost;
import kps.xml.objects.Simulation;
import kps.xml.objects.enums.DayOfWeek;
import kps.xml.objects.enums.TransportType;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;

public class AddRoute extends FormDialog {
    @NotNull private Cost route;
    private boolean isInDocument;

    private enum FieldNames {
        CompanyName,
        LocationTo,
        LocationFrom,
        TransportType,
        WeightCost,
        VolumeCost,
        MaxWeight,
        MaxVolume,
        Duration,
        DayOfWeek,
        Frequency
    }

    public AddRoute(Frame owner, Simulation simulation) {
        this(owner, simulation, null);
    }

    public AddRoute(Frame owner, Simulation simulation, @Nullable Cost previousRoute) {
        super(owner, "Add route", true, simulation);
        this.isInDocument = previousRoute != null;
        this.route = previousRoute == null ? new Cost(simulation) : previousRoute;

        buildDialog();
        setVisible(true);
    }

    @Override
    protected JComponent[][] getAllFields() {
        return new JComponent[][]{
                getField(FieldNames.CompanyName, "Company name", route.getCompany() == null ? "" : route.getCompany()),
                getField(FieldNames.LocationTo, "Location to", route.getTo() != null ? route.getTo().getName() : ""),
                getField(FieldNames.LocationFrom, "Location from", route.getFrom() != null ? route.getFrom().getName() : ""),
                getField(FieldNames.TransportType, "Transportation type", route.getTransportType() == null ? TransportType.Air : route.getTransportType()),
                getField(FieldNames.WeightCost, "Weight cost", route.getWeightCost()),
                getField(FieldNames.VolumeCost, "Volume cost", route.getVolumeCost()),
                getField(FieldNames.MaxWeight, "Max weight", route.getMaxWeight()),
                getField(FieldNames.MaxVolume, "Max volume", route.getMaxVolume()),
                getField(FieldNames.Duration, "Duration", route.getDuration()),
                getField(FieldNames.DayOfWeek, "Day of the week", route.getDay() == null ? DayOfWeek.Monday : route.getDay()),
                getField(FieldNames.Frequency, "Frequency of delivery", route.getFrequency())
        };
    }

    protected void save() {
        Set<Map.Entry<Object, Object>> entries = getAllValues().entrySet();
        for(Map.Entry<Object, Object> entry : entries) {
            switch((FieldNames)entry.getKey()) {
                case CompanyName:
                    route.setCompany((String)entry.getValue());
                    break;
                case LocationTo:
                    route.setTo((String)entry.getValue());
                    break;
                case LocationFrom:
                    route.setFrom((String)entry.getValue());
                    break;
                case DayOfWeek:
                    route.setDay(Arrays.asList(DayOfWeek.values()).stream().filter(v -> v.equals((DayOfWeek)entry.getValue())).findFirst().get());
                    break;
                case Duration:
                    route.setDuration((Integer)entry.getValue());
                    break;
                case Frequency:
                    route.setFrequency((Integer)entry.getValue());
                    break;
                case MaxVolume:
                    route.setMaxVolume((Integer)entry.getValue());
                    break;
                case MaxWeight:
                    route.setMaxWeight((Integer)entry.getValue());
                    break;
                case TransportType:
                    route.setTransportType(Arrays.asList(TransportType.values()).stream().filter(v -> v.equals((TransportType)entry.getValue())).findFirst().get());
                    break;
                case VolumeCost:
                    route.setVolumeCost((Integer)entry.getValue());
                    break;
                case WeightCost:
                    route.setWeightCost((Integer)entry.getValue());
                    break;
                default:
                    throw new RuntimeException("Unknown field");
            }
        }
        if(!isInDocument) {
            simulation.getCosts().add(route);
        }
        cancel();
    }

    protected void cancel() {
        dispose();
    }

}
