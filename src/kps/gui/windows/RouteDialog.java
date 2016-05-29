package kps.gui.windows;

import kps.gui.FormDialog;
import kps.xml.objects.Cost;
import kps.xml.objects.Location;
import kps.xml.objects.Simulation;
import kps.xml.objects.enums.DayOfWeek;
import kps.xml.objects.enums.TransportType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.Map;
import java.util.Set;

public class RouteDialog extends FormDialog {
    @NotNull
    private Cost route;
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
        Frequency,
        Discontinued
    }

    public RouteDialog(Frame owner, Simulation simulation) {
        this(owner, simulation, null);
    }

    public RouteDialog(Frame owner, Simulation simulation, @Nullable Cost previousRoute) {
        this(owner, simulation, false, previousRoute);
    }
    public RouteDialog(@Nullable Frame owner, @NotNull Simulation simulation, boolean isUpdate, @Nullable Cost previousRoute) {
        super(owner, previousRoute == null ? "Add route" : "Edit route", true, simulation);
        if(isUpdate) {
            isInDocument = false;
            setTitle("Update route");
        } else {
            this.isInDocument = previousRoute != null;
        }
        this.route = previousRoute == null ? new Cost(simulation) : previousRoute;
        buildDialog();
        setVisible(true);
    }



    @Override
    protected JComponent[][] getAllFields() {
        return new JComponent[][]{
                getField(FieldNames.CompanyName, "Company name", route.getCompany(), String.class),
                getField(FieldNames.LocationTo, "Location to", route.getTo(), Location.class),
                getField(FieldNames.LocationFrom, "Location from", route.getFrom(), Location.class),
                getField(FieldNames.TransportType, "Transportation type", route.getTransportType(), TransportType.class),
                getField(FieldNames.WeightCost, "Weight cost (cents/grams)", route.getWeightCost(), Integer.class),
                getField(FieldNames.VolumeCost, "Volume cost (cents/cm^3)", route.getVolumeCost(), Integer.class),
                getField(FieldNames.MaxWeight, "Max weight (grams)", route.getMaxWeight(), Integer.class),
                getField(FieldNames.MaxVolume, "Max volume (cm^3)", route.getMaxVolume(), Integer.class),
                getField(FieldNames.Duration, "Duration (days)", route.getDuration(), Integer.class),
                getField(FieldNames.DayOfWeek, "Day of the week", route.getDay(), DayOfWeek.class),
                getField(FieldNames.Frequency, "Frequency of delivery (days)", route.getFrequency(), Integer.class),
                getField(FieldNames.Discontinued, "Is discontinued", route.isDiscontinued(), Boolean.class)
        };
    }

    protected void save() {
        if (!fieldsValid()) return;
        Map<Object, Object> entries = getAllValues();
        route.setCompany((String)entries.get(FieldNames.CompanyName));
        route.setTo((Location)entries.get(FieldNames.LocationTo));
        route.setFrom((Location)entries.get(FieldNames.LocationFrom));
        route.setDay((DayOfWeek)entries.get(FieldNames.DayOfWeek));
        route.setDuration((Integer)entries.get(FieldNames.Duration));
        route.setFrequency((Integer)entries.get(FieldNames.Frequency));
        route.setMaxVolume((Integer)entries.get(FieldNames.MaxVolume));
        route.setMaxWeight((Integer)entries.get(FieldNames.MaxWeight));
        route.setTransportType((TransportType)entries.get(FieldNames.TransportType));
        route.setVolumeCost((Integer)entries.get(FieldNames.VolumeCost));
        route.setWeightCost((Integer)entries.get(FieldNames.WeightCost));
        route.setDiscontinued((Boolean)entries.get(FieldNames.Discontinued));
        if(!isInDocument) {
            simulation.getCosts().add(route);
        }
        cancel();
    }

    protected boolean cancel() {
        dispose();
        return true;
    }

}
